package Letters7;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
 * Finds the "7" letters that spells the most words
 * in an input text file
 * Speed is very important, that is where the learning really happens
 */

public class SevenLetters implements ISevenLetters{

	// dynamically changing the alphabet size is not
	// supported so it is a constant at the top of 
	// every file that needs it
	final static int kAlphabetSize = 26;
	
	// tracks best solution
	private int mBestComboWordCnt;
	
	// holds file of words
	// this reads all bytes of the word file in a
	// single call. This should probably be changed 
	// to read it in chunks
	private byte[] mFileArray = null;

    // Separated so it can be printed once for multiple runs
	public void description() {
		// myOutput(this.getClass().getName()+"() "+(new Date()).toString());
		// myOutput("Reads file once as byte array");
		// myOutput("Uses bitmap for removing dup letters and words, ordering letters,");
		// myOutput("Uses tree structure");
		// myOutput("Recursively create masks");
		// myOutput("Can handle any text input file and any number of letters");
		// myOutput("order letters based on histogram"); 
		// myOutput("early exit when too few remaining words");
		// myOutput("Uses multiple threads");
	}
	
	// starting point for real work
	public void doTheWork(Solution sol) {

		// track time to read file and setup tree
		long start = System.currentTimeMillis();
		int letterCnt = sol.numberOfLetters;
		
		// create base of tree
		WordTree wordTree = new WordTree(letterCnt);

		// read file and fill in tree
		readWordFileAndBuildTree(wordTree, sol);
		// new FullBitmaps();
		int startLetters = 0;
		int stopLetters = 25;
		BufferedWriter out2;
		try {
			out2 = new BufferedWriter(new FileWriter("wordTreeDump.txt"));
			wordTree.dumpTree(out2, true);
			out2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		GenerateCombos[] gc = new GenerateCombos[4];
		// for (int ii=0; ii<4; ii++){
		gc[0] = new GenerateCombos(wordTree, this, letterCnt, 0, 1, letterCnt);	
		gc[1] = new GenerateCombos(wordTree, this, letterCnt, 1, 2, letterCnt);	
		gc[2] = new GenerateCombos(wordTree, this, letterCnt, 2, 3, letterCnt);	
		gc[3] = new GenerateCombos(wordTree, this, letterCnt, 3, 25, letterCnt);	
		// }
		
		(new Thread(gc[0])).start();
		(new Thread(gc[1])).start();
		(new Thread(gc[2])).start();
		(new Thread(gc[3])).start();

		while (GenerateCombos.increment(0) != 4){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		sol.numberOfWordsSpelled = GenerateCombos.mBestComboWordCnt;
		String str = "";
		for (int i=0; i<GenerateCombos.mBestComboArray.length; i++){
			str += (char)('a'+GenerateCombos.mBestComboArray[i]);
		}
		sol.winningCombo = str;
		try {
			BufferedWriter out = null;
            out = new BufferedWriter(new FileWriter("wordprogress.txt"));
			// wordTree.dumpTree(out, true);
			// wordTree.fillInTree(out);
			// wordTree.dumpTree(out, true);
			out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// for (int i=1; i<wordTrees.length; i++){
		// 	wordTrees[i].dumpTree(i);
		// }
		long start2 = System.currentTimeMillis();
		sol.fileLoadTime = start2-start;
		
		// sol.winningCombo = WordTree.bitmapToStr(wordTree.getMaxBitmap());
		// sol.numberOfWordsSpelled = wordTree.getMaxCnt();
	}
	
	// converts the bitmap into a string
	// this is used to print the winning combo
	// public String printLetters(int[] array, int letterCnt){
	// 	String s="";

	// 	for (int i=0; i<letterCnt; i++) {
	// 		s += (char)('a'+array[i]);//reorder[array[i]]);
	// 	}
	// 	return s;
	// }

	// no longer used
	// int[] fourBits = {0,1,1,2,1,2,2,3,1,2,2,3,2,3,3,4};
	// int countBits(int mask){
	// 	int count = 0;
		
	// 	while (mask>0){
	// 		count += fourBits[ mask&0xf ];
	// 		mask >>= 4;
	// 	}
	// 	return count;
	// }
	
	// read file
	// parse file to extract words (letters only, case insensitive)
	// remove duplicate letters
	// encode as bitmap in an int
	// add to tree structure (modified b-tree)
	// each node is a letter
	// private void readWordFileAndBuildTree(WordTree wordTree, Solution sol){

	// 	int letterCnt = sol.numberOfLetters;
	// 	try {
	// 		mFileArray = Files.readAllBytes(Paths.get(sol.filename));
	// 	} catch (IOException e) {
	// 		System.out.println("Unable to open file "+sol.filename);
	// 		System.exit(0);
	// 	}

	// 	// build bitmap for each unique letter in each word
	// 	// add to tree immediately 

	// 	// assume eol is 0xa 0xd
	// 	// split words on space and eol
	// 	// case insensitive
	// 	// ignore non letters
	// 	int wordBitmap = 0;

	// 	// build histogram to put the most used letters first
	// 	// this optimizes the search
	// 	for (int i=0; i<histogram.length; i++)
	// 		histogram[i] = 0;
		
	// 	for (byte b : mFileArray) {
	// 		if ((b >= 'a') && (b <= 'z')) {
	// 			histogram[b-'a']++;
	// 		}
	// 		else if ((b >= 'A') && (b <= 'Z')) {
	// 			histogram[b-'A']++;
	// 		}
	// 	}

	// 	for (int j=0; j<reorder.length; j++) {
	// 		int max = -1;
	// 		int index = -1;
	// 		for (int i = 0; i < reorder.length; i++) {
	// 			if (histogram[i] > max) {
	// 				max = histogram[i];
	// 				index = i;
	// 			}
	// 		}
	// 		histogram[index] = -1;
	// 		neworder[index] = j;
	// 		reorder[j] = index;
	// 	}
		
	// 	for (byte b : mFileArray) {
	// 		if ((b >= 'a') && (b <= 'z')) {
	// 			wordBitmap |= 1 << (neworder[b-'a']);
	// 		}
	// 		else if (b<=' ') {
	// 			// end of word
	// 			// ignore long words or no word (i.e. multiple blank lines or multiple spaces
	// 			if (wordBitmap>0){
	// 				sol.wordsInFile++;
    //                 if ((bitCounts[wordBitmap&kMask13]+bitCounts[wordBitmap>>13])<=letterCnt) {
    //                     wordTree.addWordToTree(wordBitmap);
	// 				}
	// 			}
	// 			// reset mask for next word
	// 			wordBitmap = 0;
	// 		}
	// 		else if ((b >= 'A') && (b <= 'Z')) {
	// 			wordBitmap |= 1 << (neworder[b-'A']);
	// 		}
	// 		// ignore all non letter characters
	// 	}
		
	// 	// add last word if no 0xd 0xa is at end
	// 	if (wordBitmap>0){
	// 		sol.wordsInFile++;
    //         if ((bitCounts[wordBitmap&kMask13]+bitCounts[wordBitmap>>13])<=letterCnt) {
    //             wordTree.addWordToTree(wordBitmap);
	// 		}
	// 	}

	// 	sol.sizeOfFile = mFileArray.length;
	// }
	private void readWordFileAndBuildTree(WordTree wordTree, Solution sol){

		int letterCnt = sol.numberOfLetters;
		try {
			mFileArray = Files.readAllBytes(Paths.get(sol.filename));
		} catch (IOException e) {
			System.out.println("Unable to open file "+sol.filename);
			System.exit(0);
		}
		// int num0 = 0x4010;
		// int c = 26;
		// while ((num0>0) && (c-- > 0)){

		// 	int low = LowBitOffsets.getLowBitPos(num0);
		// 	int num1 = num0 & ~(1<<low);

		// 	System.out.println( "before "+num0+" lowPos "+low+" after "+num1);
		// 	num0 = num1;
		// }
		// System.exit(0);
		// build bitmap for each unique letter in each word
		// add to tree immediately 

		// assume eol is 0xa 0xd
		// split words on space and eol
		// case insensitive
		// ignore non letters
		int wordBitmap = 0;

		// build histogram to put the most used letters first
		// this optimizes the search
		// for (byte b : mFileArray) {
		// 	if ((b >= 'a') && (b <= 'z')) {
		// 		wordBitmap |= (1 << (b-'a'));
		// 	}
		// 	else if (b<=' ') {
		// 		// end of word
		// 		// ignore long words or no word (i.e. multiple blank lines or multiple spaces
		// 		if (wordBitmap>0 && BitCounts.getBitCount(wordBitmap) <= letterCnt) {
		// 					histogram[LowBitOffsets.getLowBitPos(wordBitmap)]++;         // record low bit
		// 		}
		// 		wordBitmap = 0;
		// 	}
		// 	else if ((b >= 'A') && (b <= 'Z')) {
		// 		wordBitmap |= (1 << (b-'A'));
		// 	}
		// 	// ignore all non letter characters
		// }
		
		// // add last word if no 0xd 0xa is at end
		// if (wordBitmap>0 && (BitCounts.getBitCount(wordBitmap)<=letterCnt)) {
		// 	histogram[LowBitOffsets.getLowBitPos(wordBitmap)]++;         // record low bit
		// }

		// System.out.println("histogram: "+Arrays.toString(histogram));

		// for (int j=0; j<reorder.length; j++) {
		// 	int max = -1;
		// 	int index = -1;
		// 	for (int i = 0; i < reorder.length; i++) {
		// 		if (histogram[i] > max) {
		// 			max = histogram[i];
		// 			index = i;
		// 		}
		// 	}
		// 	histogram[index] = -1;
		// 	neworder[index] = j;
		// 	reorder[j] = index;
		// }
		// System.out.println("reorder: "+Arrays.toString(reorder));
		// System.out.println("neworder: "+Arrays.toString(neworder));
		
		// wordBitmap = 0;
		for (byte b : mFileArray) {
			if ((b >= 'a') && (b <= 'z')) {
				wordBitmap |= (1 << (b-'a'));//(neworder[b-'a']);
			}
			else if (b<=' ') {
				// end of word
				// ignore long words or no word (i.e. multiple blank lines or multiple spaces
				if (wordBitmap>0){
					sol.wordsInFile++;
					int bitCount = BitCounts.getBitCount(wordBitmap);
                    if (bitCount <= letterCnt) {
                        wordTree.addWord(wordBitmap, 1);
					}
				}
				// reset mask for next word
				wordBitmap = 0;
			}
			else if ((b >= 'A') && (b <= 'Z')) {
				wordBitmap |= 1 << (b-'A');//(neworder[b-'A']);
			}
			// ignore all non letter characters
		}
		
		// add last word if no 0xd 0xa is at end
		if (wordBitmap>0){
			sol.wordsInFile++;
			int bitCount = BitCounts.getBitCount(wordBitmap);
			if (bitCount==letterCnt) {
				wordTree.addWord(wordBitmap, 1);
			}
		}

		sol.sizeOfFile = mFileArray.length;

		// System.out.println("_________file loading complete__________");

	}

	@Override
	public void done(int id, int comboCnt, int ccc) {
		// TODO Auto-generated method stub
		
	}
}