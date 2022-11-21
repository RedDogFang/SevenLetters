
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
 * Finds the "7" letters that spells the most words
 * in an input text file
 * Speed is very important, that is where the learning really happens
 */

public class SevenLetters implements ISevenLetters{

	final static int mAlphabetSize = 26;
	
	// letters count
	private int mLetterCnt = 7;
    private final int mask13 = 0x1fff;
	// tracks best solution
	private int mBestComboWordCnt = 0;
		
	private int[] histogram = new int[mAlphabetSize];
	private int[] reorder = new int[mAlphabetSize];
	private int[] neworder = new int[mAlphabetSize];
	
	// modified b-tree 
//	private Node mTopNode = null;
	
	// holds file of words
	private byte[] mFileArray = null;
	
	// track time and print output path
	private long mStartTime=0;
    private int[] bitCounts = new int[0x2000]; // 13 bits
    private int[] lowBitPos = new int[0x2000]; // 13 bits
	
	public SevenLetters() {
        for (int i=1; i<bitCounts.length; i++){
            bitCounts[i] = countBits(i);
            lowBitPos[i] = getLowBitPos(i);
        }
	}
	
    private int getLowBitPos(int bitmap){
		int position = 0;
		
		while ((bitmap & 1) == 0){
			position++;

			bitmap = bitmap >> 1;
		}
		
		return position;
	}

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
	public String doTheWork(Solution sol) {
		// track time to read file and setup tree
		mStartTime = System.currentTimeMillis();
		mLetterCnt = sol.numberOfLetters;
		
		// create base of tree
		WordTree wordTree = new WordTree(mLetterCnt, mAlphabetSize, lowBitPos);
		
		// read file and fill in tree
		readWordFileAndBuildTree(wordTree, sol);
		// wordTree.dumpTree(reorder);
		sol.fileLoadTime = System.currentTimeMillis()-mStartTime;

		mBestComboWordCnt = 0;

		GenerateCombos[] gcs = {
			new GenerateCombos(wordTree, this, mLetterCnt, new int[] {0}, new int[] {1}),
			new GenerateCombos(wordTree, this, mLetterCnt, new int[] {1}, new int[] {2}),
			new GenerateCombos(wordTree, this, mLetterCnt, new int[] {2}, new int[] {3}),
			new GenerateCombos(wordTree, this, mLetterCnt, new int[] {3}, new int[] {26}),				
		};
		
		joy = gcs.length;
		// joy = 4;
		
		for (int i=0; i<joy; i++) {
			Thread t = new Thread(gcs[i]);
			t.start();
		}

		guardedJoy();

		int best = -1;
		for (int i=0; i<gcs.length; i++) {

            if (gcs[i].mBestComboWordCnt > mBestComboWordCnt) {
				mBestComboWordCnt = gcs[i].mBestComboWordCnt; 
				best = i;
			}
		}
		sol.winningCombo = printLetters(gcs[best].mBestComboArray);
		sol.numberOfWordsSpelled = gcs[best].mBestComboWordCnt;

		return printLetters(gcs[best].mBestComboArray);
	}
	
	public synchronized void done() {
	    if (--joy == 0)    
	    	notifyAll();
	}
	
	int joy = 0;
	
	public synchronized void guardedJoy() {
	    while(joy > 0) {
	        try {
	            wait();
	        } catch (InterruptedException e) {}
	    }
	}

	// converts the bitmap into a string
	// this is used to print the winning combo
	public String printLetters(int[] array){
		String s="";

		for (int i=0; i<mLetterCnt; i++) {
			s += (char)('a'+reorder[array[i]]);
		}
		return s;
	}
	
	int[] fourBits = {0,1,1,2,1,2,2,3,1,2,2,3,2,3,3,4};
	int countBits(int mask){
		int count = 0;
		
		while (mask>0){
			count += fourBits[ mask&0xf ];
			mask >>= 4;
		}
		return count;
	}
	
	// read file
	// parse file to extract words (letters only, case insensitive)
	// remove duplicate letters
	// encode as bitmap in an int
	// add to tree structure (modified b-tree)
	// each node is a letter
	private void readWordFileAndBuildTree(WordTree wordTree, Solution sol){

		// count the words
		Path file = Paths.get(sol.filename);

		try {
			mFileArray = Files.readAllBytes(file);
		} catch (IOException e) {
			System.out.println("Unable to open file "+sol.filename);
			System.exit(0);
		}
		
		// build bitmap for each unique letter in each word
		// add to tree immediately 

		// assume eol is 0xa 0xd
		// split words on space and eol
		// case insensitive
		// ignore non letters
		int wordBitmap = 0;

		// build histogram to put the most used letters first
		// this optimizes the search
		for (int i=0; i<histogram.length; i++)
			histogram[i] = 0;
		
		for (byte b : mFileArray) {
			if ((b >= 'a') && (b <= 'z')) {
				histogram[b-'a']++;
			}
			else if ((b >= 'A') && (b <= 'Z')) {
				histogram[b-'A']++;
			}
		}

		for (int j=0; j<reorder.length; j++) {
			int max = -1;
			int index = -1;
			for (int i = 0; i < reorder.length; i++) {
				if (histogram[i] > max) {
					max = histogram[i];
					index = i;
				}
			}
			histogram[index] = -1;
			neworder[index] = j;
			reorder[j] = index;
		}
		
		for (byte b : mFileArray) {
			if ((b >= 'a') && (b <= 'z')) {
				wordBitmap |= 1 << (neworder[b-'a']);
			}
			else if (b<=' ') {
				// end of word
				// ignore long words or no word (i.e. multiple blank lines or multiple spaces
				if (wordBitmap>0){
					sol.wordsInFile++;
                    if ((bitCounts[wordBitmap&mask13]+bitCounts[wordBitmap>>13])<=mLetterCnt) {
                        wordTree.addWordToTree(wordBitmap);
					}
				}
				// reset mask for next word
				wordBitmap = 0;
			}
			else if ((b >= 'A') && (b <= 'Z')) {
				wordBitmap |= 1 << (neworder[b-'A']);
			}
			// ignore all non letter characters
		}
		
		// add last word if no 0xd 0xa is at end
		if (wordBitmap>0){
			sol.wordsInFile++;
            if ((bitCounts[wordBitmap&mask13]+bitCounts[wordBitmap>>13])<=mLetterCnt) {
                wordTree.addWordToTree(wordBitmap);
			}
		}

		sol.sizeOfFile = mFileArray.length;
	}
	
}