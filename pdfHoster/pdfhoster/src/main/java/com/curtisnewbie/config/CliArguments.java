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
    private static final String DIR_PATH_PREFIX = "-DDirectory=";

    @Inject
    @CommandLineArguments
    String[] args;

    /**
     * Return whether uploading is enabled, this is configured in runtime via CLI
     * 
     * @return
     */
    public boolean uploadingEnabled() {
        String res = find(UPLOAD_ENABLED_PREFIX);
        if (res == null || !res.equalsIgnoreCase("false")) // by default enabled
            return true;
        else
            return false;
    }

    /**
     * Return path to directory that is provided in runtime via CLI
     * 
     * @return
     */
    public String directory() {
        return find(DIR_PATH_PREFIX);
    }

    /**
     * Find arguments that starts with the {@code prefix}
     * 
     * @param prefix
     * @return the matched argument without the prefix or NULL if not found
     */
    private String find(String prefix) {
        for (String s : args) {
            if (s.indexOf(prefix) >= 0) {
                return s.substring(prefix.length());
            }
        }
        return null;
    }
}
