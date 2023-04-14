package com.liferay.testmap.Model;

import java.io.InputStream;

public class Client {
    private Long id;
    private String name;
    private InputStream testMapFile;

    public Client() {

    }

    public Client(Long id, String name, InputStream testMapFile) {
        this.id = id;
        this.name = name;
        this.testMapFile = testMapFile;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InputStream getTestMapFile() {
        return testMapFile;
    }

    public void setTestMapFile(InputStream testMapFile) {
        this.testMapFile = testMapFile;
    }
}

