package org.benjis.project2.messages;

import java.io.Serializable;
import java.util.Date;

// res = (fileExists, size, ts)
public class LookupFileResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    public boolean fileExists;
    public int size;
    public Date ts;

    public LookupFileResponse(boolean exists, int size) {
        this.fileExists = exists;
        this.size = size;
    }
}