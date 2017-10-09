package com.algorithm.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Files {

	public static String readFile(String compressedBinaryFileLocation) {
		StringBuilder readLine = new StringBuilder();
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(compressedBinaryFileLocation));
			String line = br.readLine();

			while (line != null) {
				readLine.append(line);
//				readLine.append(System.lineSeparator());
				line = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return readLine.toString();
	}

	public static void writeFile(String fileLocation, String fileContent) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(fileLocation));
			writer.write(fileContent);
			System.out.println(fileContent);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
			}
		}
	}
}
