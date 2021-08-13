package Entities;

import Utils.FileType;

import java.util.ArrayList;
import java.util.List;

public class FileMemory {

    private String name;
    private FileType fileType;
    private FileMemory father;
    private List<FileMemory> childs;
    private String message;
    private FileMemory returnFile;

    public FileMemory(FileMemory father, String name, FileType fileType) {
        this.father = father;
        this.name = name;
        this.fileType = fileType;
        this.childs = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public FileType getFileType() {
        return this.fileType;
    }

    public List<FileMemory> getChilds() {
        return this.childs;
    }

    public FileMemory getFather() {
        return this.father;
    }

    public void addChild(FileMemory fileMemory) {
        this.childs.add(fileMemory);
    }

    public FileMemory setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getMessage() {
        return this.message;
    }
}
