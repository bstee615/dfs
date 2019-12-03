package org.benjis.project2.messages;

import java.io.Serializable;

// A wrapper around a message sent from the client to the server.
// This is mostly to make it explicit which type of request the client is sending.
public class ClientMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    public String verb;
    public Object obj;

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