package org.benjis.project2;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class FileHandleTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void fileHandleTest() {
        filehandle fh1, fh2;

        fh1 = new filehandle();

        assertTrue(fh1.isAlive());

        fh2 = new filehandle();
        assertTrue(fh2.isAlive());

        assertTrue(!fh1.Equals(fh2));

        fh1.discard();
        fh2.discard();
        assertTrue(fh1.Equals(fh2));
    }
}
