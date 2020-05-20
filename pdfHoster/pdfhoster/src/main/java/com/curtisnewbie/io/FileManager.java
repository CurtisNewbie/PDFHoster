package com.curtisnewbie.io;

import java.util.*;
import java.util.concurrent.*;
import java.io.*;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.ObservesAsync;
import javax.inject.Inject;

/**
 * ------------------------------------
 * <p>
 * Author: Yongjie Zhuang
 * <p>
 * ------------------------------------
 * <p>
 * Class that provides methods to retrieve files discovered in directory (which
 * might or might not be PDFs).
 * </p>
 */
@ApplicationScoped
public class FileManager {

    @Inject
    protected FileScanner scanner;

    private Map<String, File> files = new ConcurrentHashMap<>();

    protected void onScannerStart(@ObservesAsync ScannerStartup ss) {
        updateFiles(); // scan files for the first time
    }

    /**
     * Handle {@code DirChangeEvent} fired by {@code FileScanner} and update the
     * internal Map accordingly.
     * 
     * @param ce a DirChangeEvent indicating the change of the directory
     */
    protected void onDirChanged(@ObservesAsync DirChangeEvent ce) {
        updateFiles();
    }

    /**
     * This method undertakes two operations, first one is to iterate the Map
     * {@code files} to remove files that don't exist. The second operation is to
     * scan the directory, and update the Map {@code files} only for new discovered
     * entries.
     */
    private void updateFiles() {
        Map<String, File> scannedFiles = scanner.scanDir();
        for (Map.Entry<String, File> entry : this.files.entrySet())
            if (!entry.getValue().exists())
                files.remove(entry.getKey());

        for (Map.Entry<String, File> entry : scannedFiles.entrySet())
            if (!files.containsKey(entry.getKey()))
                files.put(entry.getKey(), entry.getValue());
    }

    /**
     * Get the names of all files.
     * <p>
     * These names can be used later to retrieve the actual file. The returned List
     * is not backed by any data structure (e.g., Map), thus will not be updated
     * accordingly once returned. I.e., it might only reflect the view that it's
     * created.
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
}