package com.curtisnewbie.boundary;

import java.io.File;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;

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
@Path("file")
@ApplicationScoped
public class FileResources {

    @Inject
    protected FileManager fmanager;

    @GET
    public File getFile(@HeaderParam("filename") String filename) {
        File f = fmanager.getFile(filename);
        if (f != null)
            return f;
        else
            throw new NotFoundException();
    }
}