package Letters7;

import java.io.BufferedWriter;
import java.io.IOException;

public class WordTree{

	// int maxBitmap;
	// int maxCnt;
	private Node topNode;
	int letterCnt;
	// int origBitmap;

	// a WordTree is like a b tree only each node
	// has 26 lower nodes organized in an array
	// each word from the source file is converted to a bitmap
	// and stored in the word tree
	public WordTree(int letterCnt) {
		this.letterCnt = letterCnt;
		topNode = new Node();
		topNode.moreNodes = new Node[SevenLetters.kAlphabetSize];
	}

	// this adds a "word" into the word tree
	// a word is an int with bits set for each letter
	// a is 1, b is 2, c is 4, d is 8, e is 16, ...
	public void addWord(int bitmap, int count){
		Node n = topNode;

		while (bitmap > 0){
			// n.totalCount+=count;

			// get next letter offset and remove corresponding bit
			int lowBitOffset = LowBitOffsets.getLowBitPos(bitmap);
			bitmap &= ~(1<<lowBitOffset);

			// check if node array is needed
			if (n.moreNodes == null) {
				n.moreNodes = new Node[SevenLetters.kAlphabetSize];
			}
			
			// check if node is needed
			if (n.moreNodes[lowBitOffset] == null){
				n.moreNodes[lowBitOffset] = new Node();
			}
		
			n = n.moreNodes[lowBitOffset];
			// n.bitmap = origBitmap; // can be used for a sanity check later
		}

		// n is the node with the last letter
		n.wordCnt += count;
	}

	public int getComboArrayCnt(int[] comboArray){
		return getComboCnt(comboArray,0, topNode);
	}

	// recursively traverse tree looking for all words that
	// can be spelled with the letters in the combo
	private int getComboCnt(int[] comboArray, int index, Node n){
		if (n.moreNodes == null){
			return n.wordCnt;
		}

		int cnt = n.wordCnt;
		for (;index<comboArray.length;index++){
			Node nNext = n.moreNodes[comboArray[index]];
			if (nNext != null){
				cnt += getComboCnt(comboArray, index+1, nNext);
			}
		}

		return cnt;
	}

	// to print tree for debugging
	// public void dumpTree(BufferedWriter out, boolean full) throws IOException{
	// 	Dump dump = new Dump(out, full);

	// 	if (out != null){
	// 		out.write("dumpTree start\n");
	// 	}

	// 	traverseTree(topNode,0,dump);

	// 	if (out != null){
	// 		out.write("dumpTree ends\n");
	// 	}
	// }

	// private void traverseTree(Node n, int bitmap, ITraverseAction action){
	// 	if(n==null){
	// 		return;
	// 	}

	// 	action.traverseAction(n, bitmap);
	// 	if (n.moreNodes == null){
	// 		return;
	// 	}
	// 	int level = BitCounts.getBitCount(bitmap);

	// 	for (int i=level; i<SevenLetters.kAlphabetSize; i++){
	// 		if (n.moreNodes[i] != null)
	// 		{
	// 			traverseTree(n.moreNodes[i], bitmap | 1<<i, action);
	// 		}
	// 	}
	// }

	// public class Dump implements ITraverseAction{
	// 	BufferedWriter out;
	// 	boolean full;
		
	// 	public Dump(BufferedWriter out, boolean full){
	// 		this.out = out;
	// 		this.full = full;
	// 	}
		
	// 	@Override
	// 	public void traverseAction(Node n, int bitmap){
	// 		if (out != null && full || n.wordCnt>0){
	// 			try {
	// 				out.write("dump "+n.toString(bitmap, full));
	// 			} catch (IOException e) {
	// 				e.printStackTrace();
	// 			}
	// 		}				
	// 	}
	// }
	
	// interface ITraverseAction{
	// 	public void traverseAction(Node n, int bitmap);
	// }

	class Node{
		// int totalCount;	            // how many words end here and lower down this branch
		int wordCnt;				// how many words end here
		Node[] moreNodes = null; 	// 26 nodes for more letters, created when needed

		public Node(){
		}

	// 	public String toString(int bitmap, boolean more){
			
	// 		String nodes = "";
	// 		if (more){
	// 			if (moreNodes == null){
	// 				nodes = "null";
	// 			}
	// 			else{
	// 				for (int i=0; i<moreNodes.length; i++){
	// 					if (moreNodes[i] == null){
	// 						nodes += " ,";
	// 					}
	// 					else{
	// 						nodes += (char)('a'+i)+",";
	// 					}
	// 				}
	// 			}
	// 			nodes = " moreNodes {"+nodes+"}";
	// 		}

	// 		String str = "level "+BitCounts.getBitCount(bitmap)+" letters "+bitmapToStr(bitmap)+
	// 		    " ("+String.format("%x",bitmap)+"), wordCnt "+wordCnt+", totalCnt "+totalCount+nodes+"\n";

	// 		return str;
	// 	}
	// }

	// static public String bitmapToStr(int bitmap){
	// 	String s="";

	// 	while (bitmap>0){
	// 		int letter = LowBitOffsets.getLowBitPos(bitmap);
	// 		s += (char)('a'+letter);
	// 		bitmap &= ~1<<letter;
	// 	}
	// 	return s;
	}
}