package com.algorithm.RLE;

public class RLE {
	
	public static String decodeRLE(String encoded) {
		char[] charArray = encoded.toCharArray();
		int times;
		String previous = ""; // pode ser int pq vou trabalhar com o binario
		StringBuilder decoded = new StringBuilder();

		for (char textArray : charArray) {
			if (previous.isEmpty()) {
				if (Character.isDigit(textArray) && textArray != '0' && textArray != '1') {
					previous = String.valueOf(textArray);
				} else {
					decoded.append(textArray);
				}
			} else if (Character.isDigit(previous.charAt(0))) {
				if (Character.isDigit(textArray) && textArray != '0' && textArray != '1') {
					previous += textArray;
				} else {
					times = Integer.valueOf(previous);
					while (times > 0) {
						decoded.append(textArray);
						times--;
					}
					previous = "";
				}
			}
		}
		return decoded.toString();
	}

	public static String encodeRLE(String text) {
		char[] charArray = text.toCharArray();
		int times = 1;
		String previous = ""; // pode ser int pq vou trabalhar com o binario
		StringBuilder encoded = new StringBuilder();

		for (int i = 0; i < charArray.length; i++) {
			char textArray = charArray[i];
			if (previous.isEmpty()) {
				previous = String.valueOf(textArray);
			} else if (previous.charAt(0) == textArray) {
				times++;
			} else if (previous.charAt(0) != textArray) {
				if (times > 1) {
					encoded.append(times);
				}
				encoded.append(previous);
				times = 1;
				previous = String.valueOf(textArray);
			}
			if (i == charArray.length - 1 && previous != null) {
				if (times > 1) {
					encoded.append(times);
				}
				encoded.append(previous);
			}
		}
		return encoded.toString();
	}

}