package org.benjis.project2.messages;

import java.io.Serializable;

// lookup(filename)
public class LookupFileRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    public String filename;

    public LookupFileRequest(String path) {
        this.filename = path;
    }
}