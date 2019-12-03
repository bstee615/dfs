package org.benjis.project2.messages;

import java.io.Serializable;

public class ReadFileRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    public String path;
    public int position;
    public int n;

    public ReadFileRequest(String path, int position, int n) {
        this.path = path;
        this.position = position;
        this.n = n;
    }
}