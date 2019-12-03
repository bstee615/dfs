package org.benjis.project2.messages;

import java.io.Serializable;

public class LookupFileRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    public String path;

    public LookupFileRequest(String path) {
        this.path = path;
    }
}