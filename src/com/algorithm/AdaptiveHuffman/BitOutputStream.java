package com.algorithm.AdaptiveHuffman;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public final class BitOutputStream {

	private OutputStream output;
	private int currentByte;
	private int numBitsFilled;

	public BitOutputStream(OutputStream out) {
		Objects.requireNonNull(out);
		output = out;
		currentByte = 0;
		numBitsFilled = 0;
	}

	public void write(int b) throws IOException {
		if (b != 0 && b != 1) {
			throw new IllegalArgumentException("Argument must be 0 or 1");
		}
		currentByte = (currentByte << 1) | b;
		numBitsFilled++;
		if (numBitsFilled == 8) {
			output.write(currentByte);
			System.out.println(" | currentByte: " + currentByte);
			currentByte = 0;
			numBitsFilled = 0;
		}
	}

	public void close() throws IOException {
		while (numBitsFilled != 0) {
			write(0);
		}
		output.close();
	}
}
