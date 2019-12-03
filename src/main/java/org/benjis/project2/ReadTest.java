package org.benjis.project2;

/* standard java classes. */
import java.util.*;

/* fileSystemAPI should be implemented by your client-side file system. */

public class ReadTest {

	public static void main(String[] args) throws java.lang.InterruptedException, java.io.IOException {

		String host = "localhost";
		int port = 8080;
		String path = "largedata.txt";

		if (args.length > 0) {
			host = args[0];
		}
		if (args.length > 1) {
			port = Integer.parseInt(args[1]);
		}
		if (args.length > 2) {
			path = args[2];
		}

		String Filename = String.format("%s:%d/%s", host, port, path); // file to read

		System.out.println("Getting url " + Filename);

		/*
		 * Initialise the client-side file system. The following line should be replaced
		 * by something like: fileSystemAPI fs = new YourClientSideFileSystem() in your
		 * version.
		 */

		FileSystemAPI fs = new NetworkFileSystem();

		FileHandle fh = fs.open(Filename);

		long startTime, endTime;
		long turnAround;

		int count = 1;
		int totalTime = 0;
		byte[] data = new byte[1024];

		// repeat reading remote data and displaying turnaround time.
		while (true) {
			if (count == 11) {
				break;
			}

			// open file.
			fh = fs.open(Filename);

			if (fh == null) {
				System.out.println("File not found");
				return;
			}

			// read the whole file, check the time needed.
			startTime = Calendar.getInstance().getTime().getTime();
			while (!fs.isEOF(fh)) {

				// read data.
				fs.read(fh, data);

			}
			endTime = Calendar.getInstance().getTime().getTime();
			turnAround = endTime - startTime;

			// print the turnaround time.
			System.out.println("");
			System.out.println("Round " + count + ", This round took " + turnAround + " ms.");
			totalTime += turnAround;

			// wait a bit.
			Thread.sleep(500);
			count++;
		}

		System.out.println("Total 10 rounds, Average turnaround time: " + totalTime / 10 + " ms");

	}

}
