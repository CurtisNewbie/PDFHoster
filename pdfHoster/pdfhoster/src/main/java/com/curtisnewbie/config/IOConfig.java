package com.curtisnewbie.config;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.arc.config.ConfigProperties;

@ConfigProperties(prefix = "io.config")
public interface IOConfig {

    /**
     * Get the default filename used for file uploading.
     * 
     * @return default filename
     */
    @ConfigProperty(name = "def.filename")
    String defaultFileName();

    /**
     * The path to the directory which is scanned and watched for changes. This directory should
     * contain the PDFs files that will be hosted.
     * 
     * @return path to directory
     */
    @ConfigProperty(name = "scan.dir")
    String scannedDir();
}
