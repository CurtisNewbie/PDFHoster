package com.curtisnewbie.io;

import java.util.*;
import java.util.Map.Entry;
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
    private final ConcurrentMap<Integer, File> files = new ConcurrentHashMap<>();

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
        Map<Integer, File> scannedFiles = scanner.scanDir();
        for (Map.Entry<Integer, File> entry : scannedFiles.entrySet())
            files.putIfAbsent(entry.getKey(), entry.getValue());
    }

    /**
     * Get all files in forms of {@code FileEntry}, which contains the name of the file and the id
     * that can later be used to retrieve the actual data.
     * 
     * @return list of {@code FileEntry}
     */
    public List<FileEntry> getAllFileEntries() {
        List<FileEntry> fileEntries = new ArrayList<>();
        for (Entry<Integer, File> e : files.entrySet())
            fileEntries.add(new FileEntry(e.getKey(), e.getValue().getName()));
        return fileEntries;
    }

    /**
     * Get File by the id of the file.
     * 
     * @param id file id
     * @return File or {@code NULL} if not exists
     * @see FileManager#getAllFileEntries()
     */
    public File getFile(int id) {
        File f = this.files.get(id);
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
