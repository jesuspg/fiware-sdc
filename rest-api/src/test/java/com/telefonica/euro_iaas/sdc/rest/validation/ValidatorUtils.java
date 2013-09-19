package com.telefonica.euro_iaas.sdc.rest.validation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class ValidatorUtils {

	protected File createTempFile(String name) {
		File file = null;
		try {
			file = File.createTempFile(name, ".tmp");

			// Write to temp file
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write("bla bla bla");
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	protected void deleteFile(File file) {
		file.deleteOnExit();
	}

	protected byte[] getByteFromFile(File file) throws FileNotFoundException,
			IOException {

		InputStream is = new FileInputStream(file);
		long length = file.length();

		// You cannot create an array using a long type.
		// It needs to be an int type.
		// Before converting to an int type, check
		// to ensure that file is not larger than Integer.MAX_VALUE.
		if (length > Integer.MAX_VALUE) {
			// File is too large
		}

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "
					+ file.getName());
		}

		// Close the input stream and return bytes
		is.close();
		return bytes;
	}
}
