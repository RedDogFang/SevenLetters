package Letters7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
 * Finds the "7" letters that spells the most words
 * in an input text file
 * Speed is very important, that is where the learning really happens
 */

public class SevenLetters {

	// dynamically changing the alphabet size is not
	// supported so it is a constant at the top of 
	// every file that needs it
	public final static int kAlphabetSize = 26;
		
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
		long startFileLoadParse = System.currentTimeMillis();
		int letterCnt = sol.numberOfLetters;
		
		// create base of tree
		WordTree wordTree = new WordTree(letterCnt);

		// read file and fill in tree
		readWordFileAndBuildTree(wordTree, sol);

		sol.fileLoadTime = System.currentTimeMillis()-startFileLoadParse;

		int threadCount = sol.threadCount;
		GenerateCombos[] gc = new GenerateCombos[threadCount];
		for (int i=0; i<threadCount; i++){
			gc[i] = new GenerateCombos(wordTree, letterCnt, i, i+1, i);	
		}
		gc[threadCount-1].setStopLetter(kAlphabetSize-1);

		for (int i=0; i<threadCount; i++){
			(new Thread(gc[i])).start();
		}

		while (GenerateCombos.increment(0) != threadCount){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		sol.comboCount = 0;
		for(int i=0; i<threadCount; i++){
			sol.comboCount += gc[i].getNumberOfCombos();		
		}

		sol.numberOfWordsSpelled = GenerateCombos.bestComboWordCnt;
		String str = "";
		for (int i=0; i<letterCnt; i++){
			str += (char)('a'+GenerateCombos.bestComboArray[i]);
		}
		sol.winningCombo = str;
	}
	
	private void readWordFileAndBuildTree(WordTree wordTree, Solution sol){
		int letterCnt = sol.numberOfLetters;
		int wordsInFile = 0;

		try {
			mFileArray = Files.readAllBytes(Paths.get(sol.filename));
		} catch (IOException e) {
			System.out.println("Unable to open file "+sol.filename);
			System.exit(0);
		}
		// build bitmap for each unique letter in each word
		// add to tree immediately 
		// split words on all characters <= ' '
		// ignore a non lower and upper case letters a-z, A-Z
		int wordBitmap = 0;
	
		for (byte b : mFileArray) {
			if ((b >= 'a') && (b <= 'z')) {
				wordBitmap |= (1 << (b-'a'));//(neworder[b-'a']);
			}
			else if (b<=' ') {
				// end of word
				// ignore long words or no word (i.e. multiple blank lines or multiple spaces
				if (wordBitmap>0){
					wordsInFile++;
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
		
		// add last word if no eol is at end
		if (wordBitmap>0){
			wordsInFile++;
			int bitCount = BitCounts.getBitCount(wordBitmap);
			if (bitCount<=letterCnt) {
				wordTree.addWord(wordBitmap, 1);
			}
		}

		sol.sizeOfFile = mFileArray.length;
		sol.wordsInFile = wordsInFile;
		// System.out.println("_________file loading complete__________");

	}

}