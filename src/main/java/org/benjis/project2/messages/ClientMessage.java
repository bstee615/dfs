package org.benjis.project2.messages;

import java.io.Serializable;

public class ClientMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    String verb;
    Object obj;

    public ClientMessage(ReadFileRequest o) {
        this.verb = "read";
        this.obj = o;
    }

    public ClientMessage(WriteFileRequest o) {
        this.verb = "write";
        this.obj = o;
    }

    public ClientMessage(LookupFileRequest o) {
        this.verb = "lookup";
        this.obj = o;
    }
}