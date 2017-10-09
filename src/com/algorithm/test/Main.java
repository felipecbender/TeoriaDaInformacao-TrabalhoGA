package com.algorithm.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import com.algorithm.AdaptiveHuffman.BitInputStream;
import com.algorithm.AdaptiveHuffman.BitOutputStream;
import com.algorithm.AdaptiveHuffman.FrequencyTable;
import com.algorithm.AdaptiveHuffman.HuffmanDecoder;
import com.algorithm.AdaptiveHuffman.HuffmanEncoder;
import com.algorithm.RLE.RLE;
import com.algorithm.io.Files;
import com.algorithm.util.FilesLocation;

public class Main {

	public static void main(String[] args) throws IOException {
		// Compress
		leArquivoMontaArvoreCriaArquivoBinario(FilesLocation.ORIGINAL_FILE_LOCATION, FilesLocation.COMPRESSED_BINARY_FILE_LOCATION, FilesLocation.COMPRESSED_STREAM_FILE_LOCATION);
		leArquivoBinarioEUtilizaAlgoritmoRLE(FilesLocation.COMPRESSED_BINARY_FILE_LOCATION, FilesLocation.COMPRESSED_RLE_BINARY_FILE_LOCATION);
		leArquivoBinarioRLEECompactaHuffman(FilesLocation.COMPRESSED_RLE_BINARY_FILE_LOCATION, FilesLocation.COMPRESSED_HUFFMAN_BINARY_FILE_LOCATION, FilesLocation.COMPRESSED_HUFFMAN_STREAM_FILE_LOCATION);

		// Decompress
		leArquivoHuffmanEDescompactaParaRLE(FilesLocation.COMPRESSED_HUFFMAN_STREAM_FILE_LOCATION, FilesLocation.DECOMPRESSED_RLE_BINARY_FILE_LOCATION, FilesLocation.DECOMPRESSED_RLE_STREAM_FILE_LOCATION);
		leArquivoRLEEDescompactaParaArquivoBinario(FilesLocation.DECOMPRESSED_RLE_BINARY_FILE_LOCATION, FilesLocation.DECOMPRESSED_BINARY_FILE_LOCATION);
		leArquivoBinarioEConverteParaTexto(FilesLocation.COMPRESSED_STREAM_FILE_LOCATION, FilesLocation.DECOMPRESSED_HUFFMAN_BINARY_FILE_LOCATION, FilesLocation.DECOMPRESSED_HUFFMAN_STREAM_FILE_LOCATION);
	}

	private static void leArquivoMontaArvoreCriaArquivoBinario(String arquivoOrigem, String arquivoDestinoFormatoZeroEUm, String arquivoDestinoFormatoBinario) throws IOException {
		compress(arquivoOrigem, arquivoDestinoFormatoZeroEUm, arquivoDestinoFormatoBinario);
	}
	
	private static void leArquivoBinarioEUtilizaAlgoritmoRLE(String arquivoOrigem, String arquivoDestino) {
		String fileContent = Files.readFile(arquivoOrigem);
		String encodedRLE = RLE.encodeRLE(fileContent);
		Files.writeFile(arquivoDestino, encodedRLE);
	}
	
	private static void leArquivoBinarioRLEECompactaHuffman(String arquivoOrigem, String arquivoDestinoFormatoBinarioZeroEUm, String arquivoDestinoFormatoBinarioStream) throws IOException {
		compress(arquivoOrigem, arquivoDestinoFormatoBinarioZeroEUm, arquivoDestinoFormatoBinarioStream);
	}

	private static void leArquivoHuffmanEDescompactaParaRLE(String arquivoOrigem, String arquivoDestinoFormatoBinarioZeroEUm, String arquivoDestinoFormatoBinarioStream) throws IOException {
		decompress(arquivoOrigem, arquivoDestinoFormatoBinarioZeroEUm, arquivoDestinoFormatoBinarioStream);
	}
	
	private static void leArquivoRLEEDescompactaParaArquivoBinario(String arquivoOrigem, String arquivoDestino) throws IOException {
		String fileContent = Files.readFile(arquivoOrigem);
		String decodedRLE = RLE.decodeRLE(fileContent);
		Files.writeFile(arquivoDestino, decodedRLE);
	}

	private static void leArquivoBinarioEConverteParaTexto(String arquivoOrigem, String arquivoDestinoFormatoBinarioZeroEUm, String arquivoDestinoFormatoBinarioStream) throws IOException {
		decompress(arquivoOrigem, arquivoDestinoFormatoBinarioZeroEUm, arquivoDestinoFormatoBinarioStream);
	}

	private static void compress(String arquivoOrigem, String arquivoDestinoFormatoBinarioZeroEUm, String arquivoDestinoFormatoBinarioStream) throws IOException {
		InputStream in = null;
		BitOutputStream out = null;

		String readFile = Files.readFile(arquivoOrigem);
		StringBuilder binaryContent = new StringBuilder();

		try {
			in = new BufferedInputStream(new FileInputStream(arquivoOrigem));
			out = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(arquivoDestinoFormatoBinarioStream)));

			int[] initFreqs = new int[257]; // 256 códigos ascii (contando o zero) + 1 NYT
			Arrays.fill(initFreqs, 1);

			FrequencyTable freqs = new FrequencyTable(initFreqs);
			HuffmanEncoder enc = new HuffmanEncoder(out);
			enc.codeTree = freqs.buildCodeTree();

			int count = 0; // Número de bytes gravados para o arquivo de saída
			for (char character : readFile.toCharArray()) {
				// Decodifica e escreve um byte
				int symbol = character;
				if (symbol == -1) {
					break;
				}

				List<Integer> binary = enc.getBinaryBySymbol(symbol);
				for (int b : binary) {
					binaryContent.append(b);
				}
				enc.write((char)symbol);
				count++;

				// Atualiza a tabela de frequência e, possivelmente, a árvore de código
				freqs.increment(symbol);
				atualizaArvore(freqs, enc, count);
				freqs = redefineTabelaDeFrequencia(initFreqs, freqs, count);
			}
			enc.write(256);
		} finally {
			in.close();
			out.close();
		}
		Files.writeFile(arquivoDestinoFormatoBinarioZeroEUm, binaryContent.toString());
	}

	private static void decompress(String arquivoOrigem, String arquivoDestinoFormatoBinarioZeroEUm, String arquivoDestinoFormatoBinarioStream) {
		BitInputStream in = null;
		OutputStream out = null;

		int[] initFreqs = new int[257]; // 256 códigos ascii (contando o zero) + 1 NYT
		Arrays.fill(initFreqs, 1);

		StringBuilder assciiContent = new StringBuilder();

		try {
			in = new BitInputStream(new BufferedInputStream(new FileInputStream(arquivoOrigem)));
			out = new BufferedOutputStream(new FileOutputStream(arquivoDestinoFormatoBinarioStream));

			FrequencyTable freqs = new FrequencyTable(initFreqs);
			HuffmanDecoder dec = new HuffmanDecoder(in);
			dec.codeTree = freqs.buildCodeTree(); 

			int count = 0; // Número de bytes gravados para o arquivo de saída

			while (true) {
				// Decodifica e escreve um byte
				int symbol = dec.read();
				if (symbol == 256) { // 256 - excede 
					break;
				}
				assciiContent.append((char) symbol);
				out.write(symbol);
				count++;

				// Atualiza a tabela de frequência e, possivelmente, a árvore de código
				freqs.increment(symbol);
				atualizaArvore(freqs, dec, count);
				freqs = redefineTabelaDeFrequencia(initFreqs, freqs, count);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Files.writeFile(arquivoDestinoFormatoBinarioZeroEUm, assciiContent.toString());
	}

	private static void atualizaArvore(FrequencyTable freqs, Object obj, int count) {
		if (count < 262144 && isPowerOf2(count) || count % 262144 == 0) {
			if (obj instanceof HuffmanDecoder) {
				((HuffmanDecoder) obj).codeTree = freqs.buildCodeTree();
			} else if (obj instanceof HuffmanEncoder) {
				((HuffmanEncoder) obj).codeTree = freqs.buildCodeTree();
			}
		}
	}

	private static FrequencyTable redefineTabelaDeFrequencia(int[] initFreqs, FrequencyTable freqs, int count) {
		if (count % 262144 == 0) {
			freqs = new FrequencyTable(initFreqs);
		}
		return freqs;
	}

	private static boolean isPowerOf2(int x) {
		return x > 0 && Integer.bitCount(x) == 1;
	}
}
