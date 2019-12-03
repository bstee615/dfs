package org.benjis.project2.messages;

import java.io.Serializable;
import java.util.Date;

public class LookupFileResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    public Date lastModifiedTime;
    public boolean exists;
    public int size;

    public LookupFileResponse(boolean exists, int size) {
        this.exists = exists;
        this.size = size;
    }
}