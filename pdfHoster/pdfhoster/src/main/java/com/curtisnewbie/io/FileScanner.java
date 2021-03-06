package com.curtisnewbie.io;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardWatchEventKinds.*;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import com.curtisnewbie.config.CliArguments;
import com.curtisnewbie.config.IOConfig;
import org.jboss.logging.Logger;

import io.quarkus.runtime.StartupEvent;

/**
 * ------------------------------------
 * <p>
 * Author: Yongjie Zhuang
 * <p>
 * ------------------------------------
 * <p>
 * Class that is responsible for scanning of files in the specified directory.
 * <p>
 * It provides the {@link FileScanner#scanDir()} method to retrieves files in the directory in forms
 * of a Map. This method is {@code synchronized} thus can be accessed by multiple threads.
 * <p>
 * Notice that on application startup, if the specified directory doesn't exist, it creates the
 * directory to use. It also creates a new Thread that watches for changes in the directory, when a
 * change is detected (modified, created or deleted), an event {@code DirChangeEvent} is fired
 * asynchronously. The class that needs to handle such changes will need to observes the event using
 * {@code @ObservesAsync}
 * 
 */
@ApplicationScoped
public class FileScanner {

    private static final Logger logger = Logger.getLogger(FileScanner.class);

    private final String SCANNED_DIR;

    private final File dirFile;

    @Inject
    private Event<DirChangeEvent> dirChangeEvent;

    @Inject
    private Event<ScannerStartup> scannerInitEvent;

    public FileScanner(IOConfig ioConfig, CliArguments cli) {
        String temp;
        if ((temp = cli.directory()) != null)
            this.SCANNED_DIR = temp;
        else
            this.SCANNED_DIR = ioConfig.scannedDir();
        this.dirFile = createDirIfNotExists();
    }

    protected void onStart(@Observes StartupEvent se) {
        logger.info(String.format("Initialising FileScanner. Directory: '%s'",
                this.dirFile.getAbsolutePath()));
        this.scannerInitEvent.fireAsync(ScannerStartup.SCANNER_STARTUP);
        initDirChangeWatcher();
    }

    /**
     * Scan the directory and return a map of files. This method is synchrounized and the files
     * scanned can be of any type, it's the user's reponsibility to make sure that the files in this
     * directory are PDFs.
     * 
     * @return a Map of files, where the key is the absolute path to the file and the value is the
     *         actual file.
     */
    public synchronized Map<Integer, File> scanDir() {
        Map<Integer, File> tempMap = new HashMap<>();
        if (dirFile.exists()) {
            for (File f : listDir(dirFile)) {
                if (f != null)
                    tempMap.put(f.getAbsolutePath().hashCode(), f);
            }
        }
        return tempMap;
    }

    /**
     * Start a new Thread which uses WatchService to detect changes in a directory. If a change is
     * detected, it fires a {@code DirChangeEvent} event asynchrounously.
     */
    private void initDirChangeWatcher() {
        new Thread(() -> {
            final DirChangeEvent dce = DirChangeEvent.DIR_CHANGE_EVENT;
            try {
                WatchService watcher = FileSystems.getDefault().newWatchService();
                dirFile.toPath().register(watcher, ENTRY_DELETE, ENTRY_MODIFY, ENTRY_CREATE);
                while (true) {
                    WatchKey key = watcher.poll(1, TimeUnit.SECONDS);
                    if (key != null) {
                        for (var e : key.pollEvents()) {
                            logger.info("Detected changes in directory.");
                            var kind = e.kind();
                            if (kind == ENTRY_MODIFY || kind == ENTRY_CREATE
                                    || kind == ENTRY_DELETE) {
                                dirChangeEvent.fireAsync(dce);
                            }
                        }
                        key.reset();
                    }
                }
            } catch (Exception e) {
                logger.fatal(e);
            }
        }).start();
    }

    /**
     * Create specified directory if not exists
     */
    private File createDirIfNotExists() {
        File file = new File(SCANNED_DIR);
        if (!file.exists())
            file.mkdir();
        return file;
    }

    /**
     * List all files in a directory recursively.
     * 
     * @param dir
     * @return all files in a directory
     */
    private List<File> listDir(File dir) {
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        for (File f : files) {
            if (f.isDirectory())
                list.addAll(listDir(f));
            else
                list.add(f);
        }
        return list;
    }
}
