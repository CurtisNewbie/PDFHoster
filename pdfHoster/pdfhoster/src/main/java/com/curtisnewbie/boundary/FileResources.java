package com.curtisnewbie.boundary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import io.quarkus.runtime.StartupEvent;
import com.curtisnewbie.config.CliArguments;
import com.curtisnewbie.io.FileManager;

/**
 * ------------------------------------
 * <p>
 * Author: Yongjie Zhuang
 * <p>
 * ------------------------------------
 * <p>
 * REST endpoints for File Resources
 * </p>
 */
@Path("/file")
@ApplicationScoped
public class FileResources {

    private static final Logger logger = Logger.getLogger(FileResources.class);
    private final FileManager fmanager;
    private volatile boolean uploadingEnabled = false;

    public FileResources(FileManager fmanager, CliArguments cli) {
        this.fmanager = fmanager;
        uploadingEnabled = cli.uploadingEnabled();
    }

    void onStartup(@Observes StartupEvent se) {
        logger.info("File Uploading is " + (uploadingEnabled ? "enabled" : "disabled") + ".");
    }

    @GET
    public File getFile(@HeaderParam("filename") String filename) {
        if (filename == null)
            throw new NotFoundException();

        File f = fmanager.getFile(filename);
        if (f != null)
            return f;
        else
            throw new NotFoundException();
    }

    @GET
    @Path("/names")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getFileNames() {
        return fmanager.getAllFileNames();
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void uploadFile(MultipartFormDataInput formDataInput,
            @Suspended AsyncResponse asyncResponse) {
        try {
            File tempFile = null;
            // functionality disabled or no file in the MultiPart Form
            if (!uploadingEnabled || (tempFile =
                    formDataInput.getFormDataMap().get("file").get(0).getBody(File.class, null))
                            .length() == 0) {
                if (uploadingEnabled)
                    logger.info("File uploading denied due to empty file.");
                asyncResponse.resume(Response.seeOther(URI.create("/")).build());
                return;
            }

            File file = fmanager
                    .createFile(formDataInput.getFormDataPart("filename", String.class, null));
            if (file == null) {
                throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
            }
            try (FileOutputStream fileOut = new FileOutputStream(file);
                    ReadableByteChannel channalIn =
                            Channels.newChannel(new FileInputStream(tempFile));) {

                FileChannel channelOut = fileOut.getChannel();
                channelOut.transferFrom(channalIn, 0, tempFile.length());
            }
            asyncResponse.resume(Response.seeOther(URI.create("/")).build());
        } catch (IOException e) {
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        } finally {
            formDataInput.close();
        }
    }
}
