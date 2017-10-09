package com.algorithm.AdaptiveHuffman;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public final class HuffmanEncoder {

	private BitOutputStream output;
	public CodeTree codeTree;
	
	public HuffmanEncoder(BitOutputStream out) {
		Objects.requireNonNull(out);
		output = out;
	}

	public void write(int symbol) throws IOException {
		if (codeTree == null) {
			throw new NullPointerException("Code tree is null");
		}
		List<Integer> bits = codeTree.getCode(symbol);
		
		// Aqui vai a sequencia de bits
		for (int b : bits) {
			System.out.print(b);
			output.write(b);
		}
	}
	
	public List<Integer> getBinaryBySymbol(int symbol) {
		return codeTree.getCode(symbol);
	}

}
