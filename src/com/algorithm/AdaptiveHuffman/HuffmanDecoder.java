package com.algorithm.AdaptiveHuffman;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public final class HuffmanDecoder {

	private BitInputStream input;
	public CodeTree codeTree;

	public HuffmanDecoder(BitInputStream in) {
		Objects.requireNonNull(in);
		input = in;
	}

	public int read() throws IOException {
		if (codeTree == null) {
			throw new NullPointerException("Code tree is null");
		}
		InternalNode currentNode = codeTree.root;
		while (true) {
			int temp = input.readNoEof();
			Node nextNode;
			if (temp == 0) {
				nextNode = currentNode.leftChild;
			} else if (temp == 1) {
				nextNode = currentNode.rightChild;
			} else {
				throw new AssertionError("Invalid value from readNoEof()");
			}

			if (nextNode instanceof Leaf) {
				return ((Leaf) nextNode).symbol;
			} else if (nextNode instanceof InternalNode) {
				currentNode = (InternalNode) nextNode;
			} else {
				throw new AssertionError("Illegal node type");
			}
		}

	}

	public List<Integer> getBinaryBySymbol(int symbol) {
		return codeTree.getCode(symbol);
	}
}