package com.curtisnewbie.boundary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
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
import javax.ws.rs.core.Response.Status;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
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

    private final FileManager fmanager;

    public FileResources(FileManager fmanager) {
        this.fmanager = fmanager;
    }

    @GET
    public File getFile(@HeaderParam("filename") String filename) {
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
            File file = fmanager
                    .createFile(formDataInput.getFormDataPart("filename", String.class, null));
            if (file == null) {
                throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
            }

            try (FileOutputStream fileOut = new FileOutputStream(file);
                    ReadableByteChannel channalIn = Channels.newChannel(
                            formDataInput.getFormDataPart("file", InputStream.class, null));) {

                FileChannel channelOut = fileOut.getChannel();
                channelOut.transferFrom(channalIn, 0, Long.MAX_VALUE);
            }
        } catch (IOException e) {
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        }
    }
}
