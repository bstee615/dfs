package org.benjis.project2.messages;

import java.io.Serializable;

public class ReadFileResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    public int bytesRead;
    public byte[] data;
}