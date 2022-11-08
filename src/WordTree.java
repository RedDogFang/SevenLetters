
public class WordTree{

	private Node mTopNode;
	private int mAlphabetSize;
	private int mLetterCnt;
	
	
	public WordTree(int letterCnt, int alphabetSize) {
		mLetterCnt = letterCnt;
		mAlphabetSize = alphabetSize;
		mTopNode = new Node();
		mTopNode.moreNodes = new Node[mAlphabetSize];
	}

	public void addWordToTree(int wordBitmap){
		addWordToTree(mTopNode,wordBitmap);
	}

	// recursively add letters (word) to tree
	private void addWordToTree(Node n, int wordBitmap){

		// get next letter offset and remove corresponding bit
		int letterOffset = lowBitPos(wordBitmap);
		wordBitmap &= ~(1<<letterOffset);
		
		// check if node array is needed
		if (n.moreNodes == null)
			n.moreNodes = new Node[mAlphabetSize];
		
		// check if node is needed
		if (n.moreNodes[letterOffset] == null)
			n.moreNodes[letterOffset] = new Node();
		
		// if wordBitmap is 0 then this is the last letter of word 
		// reach down and increment count (saves a recurse)
		if (wordBitmap == 0) {
			n.moreNodes[letterOffset].wordCnt++;
		}
		else {
			// drop into node and repeat
			addWordToTree(n.moreNodes[letterOffset], wordBitmap);
		}

		n.moreNodes[letterOffset].totalCount++;
	}

	private int lowBitPos(int bitmap){
		int position = 0;
		
		while ((bitmap & 1) == 0){
			position++;

			bitmap = bitmap >> 1;
		}
		
		return position;
	}

	public int countWords(int[] comboArray){
		return countWords(mTopNode, comboArray, 0);
	}

	// recursively search tree looking for all words that can be spelled by comboArray 
	private int countWords(Node n, int[] comboArray, int index) {
		int count = n.wordCnt;

		// if no more letters then done
		if(n.moreNodes == null) {
			return count;
		}
		
		// go down and sideways to cover everything possible
		for (int i=index; i<mLetterCnt; i++) {
			
			if (n.moreNodes[comboArray[i]] != null) {
				count += countWords(n.moreNodes[comboArray[i]], comboArray, index+1);
			}
		}
		return count;
	}

	public void dumpFirstNodes(int[] reorder) {
		for (byte i=0; i<mAlphabetSize; i++) {
			char a = (char) ('a'+(byte) reorder[i]);
			if (mTopNode.moreNodes[i] != null)
				System.out.println(a+": "+mTopNode.moreNodes[i].totalCount);
			else
				System.out.println(a+": 0");
		}
	}
	
	public int countFirstNodes(int start, int finish) {
		int count = 0;
		for (int i=start; i<finish; i++) {
			if (mTopNode.moreNodes[i] != null)
				count+= mTopNode.moreNodes[i].totalCount;
		}
		return count;
	}

	private class Node{
		int totalCount = 0;
		int wordCnt = 0;			// how many words end here
		Node[] moreNodes = null; 	// 26 nodes for more letters, created when needed
		int filledinCount = 0;
	}
}