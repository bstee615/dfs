package org.benjis.project2.messages;

import java.io.Serializable;

public class ReadFileRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    public String path;

    public ReadFileRequest(String path) {
        this.path = path;
    }
}