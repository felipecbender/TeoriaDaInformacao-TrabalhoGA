# Teoria da Informação

Este trabalho tem por finalidade compactar, descompactar, comprimir e descomprimir arquivos. 
Os algoritmos utilizados foram: Huffman Adaptivo e RLE (Run Length Encoding);

- A classe para teste se encontra no pacote: com.algorithm.test, com o nome Main.java.
- O arquivo (alice29.txt) para compactação e compressão se encontra no pacote: com.algorithm.files.
- O local que será salvo os arquivos compactados e comprimidos estes definidos no pacote: com.algorithm.util. na classe: FilesLocation.java.
(Atualmente estão definidos no pacote com.algorithm.files).

Os métodos foram definidos de forma genérica, no sentido de poderem ser usados independente de ordem. Eles recebem o "path" de um arquivo de entrada e um arquivo de saída, quando utilizado o algoritmo RLE. Já os métodos referente ao algoritmo do Huffman Adaptivo recebem como parâmetro três "paths", um arquivo de entrada, um arquivo de saída referente ao código binário em Stream e um arquivo de saída referente ao código binário em "texto".

OBS: Para fazer descompactação é necessário ter um arquivo compactado. Por exemplo: Para descompactar RLE deve se ter um arquivo com conteúdo compactado em RLE.


O classe teste realiza as seguintes operações (métodos) abaixo:
- leArquivoMontaArvoreCriaArquivoBinario
- leArquivoBinarioEUtilizaAlgoritmoRLE
- leArquivoBinarioRLEECompactaHuffman
    
- leArquivoHuffmanEDescompactaParaRLE
- leArquivoRLEEDescompactaParaArquivoBinario
- leArquivoBinarioEConverteParaTexto

No caso, primeiro é utilizado o algoritmo Huffman Adaptivo para compactar o conteúdo do arquivo passado (alice29.txt). Depois o resultado é comprimido com o algoritmo RLE e é compactado de novo com o Huffman Adaptivo. E por fim é realizado o inverso para se ter o arquivo original.



O motivo de se utilizar esta ordem (compactação, compressão e compactação) foi para saber se a compressão de bits, gerado pela primeira utilização de Huffman Adaptivo, teria alguma relevância em economia de espaço.

OBS: O ideal talvez fosse primeiro utilizar o algoritmo RLE e depois o algoritmo Huffman Adaptivo e é possível fazer este teste com os métodos genéricos, porém para o trabalho o interesse de teste foi descrito no parágrafo acima.
