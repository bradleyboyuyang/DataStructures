import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanCompression {
	public static String getCompressedCode(String inputText, String[] huffmanCodes) {
		StringBuilder compressedCode = new StringBuilder();
		for (int i = 0; i < inputText.length(); i++) {
			int b = inputText.charAt(i);
			compressedCode.append(huffmanCodes[b]);
		}
		return compressedCode.toString();
	}

	public static String[] getHuffmanCode(String inputText) {
		String[] huffmanCodes = new String[128];
		List<Character> keyList = new ArrayList<Character>();
		List<Integer> valList = new ArrayList<Integer>();
		HashMap<Character, Integer> freq = new HashMap<Character, Integer>(); // stores character frequency in string
		HashMap<Character, String> codeMap = new HashMap<Character, String>();
		freq_Calc(inputText, freq);

		for (Map.Entry<Character, Integer> entry : freq.entrySet()) {
			Character ch = entry.getKey();
			Integer in = entry.getValue();
			valList.add(in);
			keyList.add(ch);
		}

		// create priority queue
		PriorityQueue<Node> q = new PriorityQueue<Node>(freq.size(), new HuffmanCompression().new Compare());

		for (int i = 0; i < freq.size(); i++) {
			Node hn = new HuffmanCompression().new Node();
			hn.key = keyList.get(i);
			hn.data = valList.get(i);
			hn.left = null;
			hn.right = null;
			q.add(hn);
		}
		Node root = null;

		while (q.size() > 1) {
			Node x = q.peek();
			q.poll();

			Node y = q.peek();
			q.poll();

			Node f = new HuffmanCompression().new Node();

			f.data = x.data + y.data;
			f.key = '#';
			f.left = x;
			f.right = y;
			root = f;
			q.add(f);
		}
		MappingCode(root, "", codeMap);

		for (int i = 0; i < inputText.length(); i++) {
			char c = inputText.charAt(i);
			huffmanCodes[(int) c] = (String) codeMap.get(c);
		}
		return huffmanCodes;
	}

	class Node {
		int data;
		char key;
		Node left;
		Node right;
	}

	class Compare implements Comparator<Node> {
		public int compare(Node x, Node y) {
			return x.data - y.data;
		}
	}

	public static void freq_Calc(String str, HashMap<Character, Integer> map) {
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			map.merge(c, 1, Integer::sum);
		}
	}

	public static void MappingCode(Node node, String s, HashMap<Character, String> codeMap) {
		int ascii = node.key;
		if (ascii >= 0 && ascii <= 255 && node.left == null && node.right == null) {
			codeMap.putIfAbsent(node.key, s);
			return;
		}
		MappingCode(node.left, s + "0", codeMap);
		MappingCode(node.right, s + "1", codeMap);
	}

    public static void main(String[] args) throws Exception {
        // obtain input text from a text file encoded with ASCII code
        String inputText = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(args[0])), "US-ASCII");
        // get Huffman codes for each character and write them to a dictionary file
        String[] huffmanCodes = HuffmanCompression.getHuffmanCode(inputText);
        FileWriter fwriter1 = new FileWriter(args[1], false);
        BufferedWriter bwriter1 = new BufferedWriter(fwriter1);
        for (int i = 0; i < huffmanCodes.length; i++) 
            if (huffmanCodes[i] != null) {
                bwriter1.write(Integer.toString(i) + ":" + huffmanCodes[i]);
                bwriter1.newLine();
            }
        bwriter1.flush();
        bwriter1.close();
        // get compressed code for input text based on huffman codes of each character
        String compressedCode = HuffmanCompression.getCompressedCode(inputText, huffmanCodes);
        FileWriter fwriter2 = new FileWriter(args[2], false);
        BufferedWriter bwriter2 = new BufferedWriter(fwriter2);
        if (compressedCode != null) 
            bwriter2.write(compressedCode);
        bwriter2.flush();
        bwriter2.close();
    }
}
