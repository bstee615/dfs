package org.benjis.project2;

public class NetworkFileHandle extends FileHandle {
    String ip;
    String port;
    String path;

    public NetworkFileHandle(String ip, String port, String path) {
        super();

        this.ip = ip;
        this.port = port;
        this.path = path;
    }
}
