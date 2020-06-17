package com.curtisnewbie.io;

import java.util.*;
import java.util.concurrent.*;
import java.io.*;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.ObservesAsync;
import com.curtisnewbie.config.CliArguments;
import com.curtisnewbie.config.IOConfig;

import org.jboss.logging.Logger;

/**
 * ------------------------------------
 * <p>
 * Author: Yongjie Zhuang
 * <p>
 * ------------------------------------
 * <p>
 * Class that provides methods to retrieve files discovered in directory (which might or might not
 * be PDFs).
 * </p>
 */
@ApplicationScoped
public class FileManager {

    private static final Logger logger = Logger.getLogger(FileManager.class);
    private final FileScanner scanner;
    private final String DEFAULT_FNAME;
    private final String DIR;
    private final ConcurrentMap<String, File> files = new ConcurrentHashMap<>();

    public FileManager(FileScanner scanner, IOConfig ioConfig, CliArguments cli) {
        this.scanner = scanner;
        String temp;
        if ((temp = cli.directory()) != null)
            this.DIR = temp;
        else
            this.DIR = ioConfig.scannedDir();
        this.DEFAULT_FNAME = ioConfig.defaultFileName();
    }

    protected void onScannerStart(@ObservesAsync ScannerStartup ss) {
        updateFiles(); // scan files for the first time
    }

    /**
     * Handle {@code DirChangeEvent} fired by {@code FileScanner} and update the internal Map
     * accordingly.
     * 
     * @param ce a DirChangeEvent indicating the change of the directory
     */
    protected void onDirChanged(@ObservesAsync DirChangeEvent ce) {
        updateFiles();
    }

    /**
     * This method undertakes two operations, first one is to iterate the Map {@code files} to
     * remove files that don't exist. The second operation is to scan the directory, and update the
     * Map {@code files} only for new discovered entries.
     */
    private void updateFiles() {
        this.files.forEach((k, v) -> {
            if (!v.exists())
                files.remove(k);
        });
        Map<String, File> scannedFiles = scanner.scanDir();
        for (Map.Entry<String, File> entry : scannedFiles.entrySet())
            files.putIfAbsent(entry.getKey(), entry.getValue());
    }

    /**
     * Get the names of all files.
     * <p>
     * These names can be used later to retrieve the actual file. The returned List is not backed by
     * any data structure (e.g., Map), thus will not be updated accordingly once returned. I.e., it
     * might only reflect the view when it's created.
     * 
     * @return list of names of all files discovered
     */
    public List<String> getAllFileNames() {
        List<String> names = new ArrayList<>();
        for (String s : this.files.keySet())
            names.add(s);
        return names;
    }

    /**
     * Get File by the name of the file.
     * 
     * @param fn file name
     * @return File or {@code NULL} if not exists
     * @see FileManager#getAllFileNames()
     */
    public File getFile(String fn) {
        File f = this.files.get(fn);
        if (f != null && !f.exists()) // return null if the file doesn't exist
            f = null;
        return f;
    }

    /**
     * Create an empty file in directory
     * 
     * @param name
     * @return File or NULL if IOException is caught
     */
    public File createFile(String name) {
        if (name == null)
            name = DEFAULT_FNAME;
        logger.info("Create file " + name);
        File file = new File(DIR, name);
        try {
            file.createNewFile();
        } catch (IOException e) {
            file = null;
        }
        return file;
    }
}
