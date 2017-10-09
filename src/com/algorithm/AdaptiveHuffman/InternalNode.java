package com.algorithm.AdaptiveHuffman;

import java.util.Objects;

public final class InternalNode extends Node {

	public final Node leftChild;
	public final Node rightChild;

	public InternalNode(Node left, Node right) {
		Objects.requireNonNull(left);
		Objects.requireNonNull(right);
		leftChild = left;
		rightChild = right;
	}

}
