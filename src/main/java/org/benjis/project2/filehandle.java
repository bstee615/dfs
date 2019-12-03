package org.benjis.project2;

public class FileHandle {
    /*
     * The "filehandle" is simply an integer. We keep a counter in a static variable
     * "cnt" so that no duplication occurs. When filehandle is discarded its number
     * becomes 0.
     */

    private int index;
    private static int cnt = 1;

    public FileHandle() {
        index = cnt++;
    }

    // Is this handle still valid?
    public boolean isAlive() {
        return (this.index != 0);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FileHandle) {
            FileHandle fh = (FileHandle) o;
            return (fh.index == this.index);
        } else {
            return false;
        }
    }

    // Invalidate this handle
    public void discard() {
        index = 0;
    }

    @Override
    public String toString() {
        return index + "/" + (cnt - 1);
    }
}
