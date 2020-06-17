package com.curtisnewbie.config;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import io.quarkus.runtime.annotations.CommandLineArguments;

/**
 * ------------------------------------
 * <p>
 * Author: Yongjie Zhuang
 * <p>
 * ------------------------------------
 * <p>
 * Class that stores CliArguments
 * </p>
 */
@ApplicationScoped
public class CliArguments {

    private static final String UPLOAD_ENABLED_PREFIX = "-DUploadingEnabled=";

    @Inject
    @CommandLineArguments
    String[] args;

    public boolean uploadingEnabled() {
        for (String s : args) {
            if (s.indexOf(UPLOAD_ENABLED_PREFIX) >= 0) {
                if (s.substring(UPLOAD_ENABLED_PREFIX.length()).equalsIgnoreCase("false"))
                    return false;
                else
                    return true;
            }
        }
        return true;
    }
}
