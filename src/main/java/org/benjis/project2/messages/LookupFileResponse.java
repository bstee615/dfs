package org.benjis.project2.messages;

import java.io.Serializable;

// res = (fileExists, size, ts)
public class LookupFileResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    public boolean fileExists;
    public int size;
    public long ts;

    public LookupFileResponse(boolean exists, int size, long ts) {
        this.fileExists = exists;
        this.size = size;
        this.ts = ts;
    }
}