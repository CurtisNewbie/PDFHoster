package com.curtisnewbie.io;

/**
 * class that contains the name of the file and the id that can later be used to retrieve the actual
 * data.
 * <p>
 * 
 * @author Yongjie Zhuang
 */
public class FileEntry {
    int id;
    String name;

    FileEntry(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
