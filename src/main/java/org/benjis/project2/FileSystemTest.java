package org.benjis.project2;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class FileSystemTest {

	private static String host = "localhost";
	private static int port = 6666;

	public static void main(String[] args) throws UnknownHostException, IOException {
		if (args.length > 0) {
			host = args[0];
		}
		if (args.length > 1) {
			port = Integer.parseInt(args[1]);
		}

		byte[] data = new byte[100];

		System.out.println("IP: " + host + "; port: " + port);
		Scanner scan = new Scanner(System.in);

		System.out.println("Please specify filename to Open: ");
		String fileName = scan.nextLine();

		/*
		 * Initialise the client-side file system. The following line should be replaced
		 * by something like: fileSystemAPI fs = new YourClientSideFileSystem() in your
		 * version.
		 */

		FileSystemAPI fs = new NetworkFileSystem();

		String url = String.format("%s:%s/%s", host, port, fileName);
		FileHandle fh = fs.open(url);

		System.out.println("Start reading file...");
		while (!fs.isEOF(fh)) {

			// read data.
			int res = fs.read(fh, data);
			System.out.println("Read " + res + " bytes.");
		}
		System.out.println("Done reading file...");

		System.out.println("Please enter data to write to file or 'q' to stop");
		String contents = scan.nextLine();

		while (!(contents.equals("q"))) {
			contents = contents + "\n";
			byte[] toWrite = contents.getBytes();
			boolean write_res = fs.write(fh, toWrite);
			if (write_res) {
				System.out.println("Data is written to file");
			} else {
				System.out.println("Could not write to file");
			}
			System.out.println("Please enter data to write to file or 'q' to stop");
			contents = scan.nextLine();
		}

		scan.close();
		fs.close(fh);
		return;

	}
}
