

package Letters7;

import java.io.BufferedWriter;
import java.io.IOException;

public class WordTree{

	// dynamically changing the alphabet size is not
	// supported so it is a constant at the top of 
	// every file that needs it
	final static int kAlphabetSize = 26;
	int maxBitmap;
	int maxCnt;
	private Node mTopNode;
	int letterCnt;
	int origBitmap;
	
	public WordTree(int letterCnt) {
		this.letterCnt = letterCnt;
		mTopNode = new Node(0);
		mTopNode.moreNodes = new Node[kAlphabetSize];
	}

	public void addWord(int bitmap, int count){
		int origBitmap = bitmap;
		int newBitmap = 0;
		Node n = mTopNode;

		while (bitmap > 0){
			n.totalCount+=count;

			// get next letter offset and remove corresponding bit
			int lowBitOffset = LowBitOffsets.getLowBitPos(bitmap);
			bitmap &= ~(1<<lowBitOffset);
			newBitmap |= (1<<lowBitOffset);

			// check if node array is needed
			if (n.moreNodes == null) {
				n.moreNodes = new Node[kAlphabetSize];
			}
			
			// check if node is needed
			if (n.moreNodes[lowBitOffset] == null){
				n.moreNodes[lowBitOffset] = new Node(newBitmap);
			}
		
			n = n.moreNodes[lowBitOffset];
			n.bitmap = origBitmap; // can be used for a sanity check later
		}

		// n is the node with the last letter
		n.wordCnt += count;
	}

	public int getComboArrayCnt(int[] comboArray){
		return getComboCnt(comboArray,0, mTopNode);
	}

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

	// private int getBitmapCnt(Node n, int bitmap){
	// 	// if (bitmap == 0){
	// 	// 	// System.out.println("bitmap count for "+String.format("%x",origBitmap)+"("+bitmapToStr(origBitmap)+") = "+n.wordCnt);
	// 	// 	return n.wordCnt;
	// 	// }
	// 	// else {
	// 		if (bitmap>0 && n.moreNodes != null){
	// 			int lowBitPos = LowBitOffsets.getLowBitPos(bitmap);
	// 			Node n2 = n.moreNodes[lowBitPos];
	// 			if (n2 != null){
	// 				bitmap &= ~(1<<lowBitPos);
	// 				return n.wordCnt+getBitmapCnt(n2, bitmap);
	// 			}
	// 		}
	// 	// }
	// 	return 0;
	// }

	public void dumpTree(BufferedWriter out, boolean full) throws IOException{
		Dump dump = new Dump(out, full);

		if (out != null){
			out.write("dumpTree start\n");
		}

		traverseTree(mTopNode,0,dump);

		if (out != null){
			out.write("dumpTree ends\n");
		}
	}

	private void traverseTree(Node n, int bitmap, ITraverseAction action){
		if(n==null){
			return;
		}

		action.traverseAction(n, bitmap);
		if (n.moreNodes == null){
			return;
		}
		int level = BitCounts.getBitCount(bitmap);

		for (int i=level; i<kAlphabetSize; i++){
			if (n.moreNodes[i] != null)
			{
				traverseTree(n.moreNodes[i], bitmap | 1<<i, action);
			}
		}
	}



	// private int advBits(int bitmap){
	// 	int bitPos = 25;

	// 	int topBit = HighBitOffsets.getHighBitPos(bitmap);
	// 	while ((bitmap & (1<<bitPos)) > 0){
	// 		bitPos--;
	// 	}

	// 	// if ()
	// 	return -1;
	// }

	// public class FillIn implements ITraverseAction{
	// 	BufferedWriter out;

	// 	public FillIn(BufferedWriter out){
	// 		this.out = out;
	// 	}

	// 	@Override
	// 	public void traverseAction(Node n, int bitmap) {
	// 		if (n.wordCnt != 0){
	// 			int count = n.wordCnt;
	// 			n.wordCnt = 0;
		
	// 			for(int i=bitmap; i < (1<<kAlphabetSize); i = i<<1){
	// 				if (BitCounts.getBitCount(i) == letterCnt && ((i&bitmap) == bitmap)){

	// 					addWord(i, count);
	// 				}
	// 			}
	// 		}
	// 	}
	// }
	
	public class Dump implements ITraverseAction{
		BufferedWriter out;
		boolean full;
		
		public Dump(BufferedWriter out, boolean full){
			this.out = out;
			this.full = full;
		}
		
		@Override
		public void traverseAction(Node n, int bitmap){
			if (out != null && full || n.wordCnt>0){
				try {
					out.write("dump "+n.toString(bitmap, full));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}				
		}
	}
	
	interface ITraverseAction{
		public void traverseAction(Node n, int bitmap);
	}


	class Node{
		int totalCount;	            // how many words end here and lower down this branch
		int wordCnt;				// how many words end here
		int bitmap;
		Node[] moreNodes = null; 	// 26 nodes for more letters, created when needed

		public Node(int bitmap){
			this.bitmap = bitmap;
		}

		public String toString(int bitmap, boolean more){
			
			String nodes = "";
			if (more){
				if (moreNodes == null){
					nodes = "null";
				}
				else{
					for (int i=0; i<moreNodes.length; i++){
						if (moreNodes[i] == null){
							nodes += " ,";
						}
						else{
							nodes += (char)('a'+i)+",";
						}
					}
				}
				nodes = " moreNodes {"+nodes+"}";
			}

			String str = "level "+BitCounts.getBitCount(bitmap)+" letters "+bitmapToStr(bitmap)+
			    " ("+String.format("%x",bitmap)+"), wordCnt "+wordCnt+", totalCnt "+totalCount+nodes+"\n";

			// if (this.bitmap != bitmap){
			// 	System.out.println("fatal error - bitmaps are not equal. Internal bitmap is "+bitmapToStr(this.bitmap)+
			// 	                   " ("+String.format("%x",bitmap)+")\n"+str);
				
			// 	if (cnt++ >= 10){
			// 		System.exit(0);
			// 	}
			// }
			return str;
		}
	}

	static public String bitmapToStr(int bitmap){
		String s="";

		while (bitmap>0){
			int letter = LowBitOffsets.getLowBitPos(bitmap);
			s += (char)('a'+letter);
			bitmap &= ~1<<letter;
		}
		return s;
	}
}