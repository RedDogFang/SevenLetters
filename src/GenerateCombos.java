

public class GenerateCombos implements Runnable {

	private WordTree mWordTree;
	private ISevenLetters msl;
	private int[] mStartLetter;
	private int[] mStopLetter;
	private int rangeDepth;
	private int mComboCount = 0;
	private int mLetterCnt = 7;
	private int mComboWordCnt = 0;
	public int mBestComboWordCnt = -1;
	private static int mGlobalBestComboWordCnt = -1;
	private int[] comboArray;
	public int[] mBestComboArray = new int[26];
	
	public GenerateCombos(WordTree wordTree, ISevenLetters sl, int letterCnt, int[] startLetter, int[] stopLetter) {
		mLetterCnt = letterCnt;
		comboArray = new int[mLetterCnt];
		for (int i=0; i<startLetter.length; i++)
			comboArray[i] = -1;
		
		msl = sl;
		mWordTree = wordTree;
		mStartLetter = startLetter;
		mStopLetter = stopLetter;
		rangeDepth = mStartLetter.length;	
	}
	
	private synchronized void setGreatest(int newMax) {
		mGlobalBestComboWordCnt = newMax;
	}
	
	public int getCombos() {
		return mComboCount;
	}

	public void run() {
		mGlobalBestComboWordCnt = -1;
		generateCombosAndCountWords(1, mStartLetter[0], comboArray);
		
		msl.done();
	}
	
	private void generateCombosAndCountWords(int numberOfBits, int currentPos, int[] comboArray) {
		// this keeps adding bits and sliding them through all possible positions
		while (currentPos < 26 - mLetterCnt + numberOfBits) {

			if (numberOfBits <= rangeDepth) {
				
				if (numberOfBits == 1) {
					if (mGlobalBestComboWordCnt > mWordTree.countFirstNodes(currentPos,26)) {
						return;
					}
				}

				if (mStartLetter[numberOfBits-1] != -1) {
					currentPos = mStartLetter[numberOfBits-1];
					mStartLetter[numberOfBits-1] = -1;
				}
				comboArray[numberOfBits - 1] = currentPos;
				
				int i;
				for (i=0; i<rangeDepth; i++) {
					if (comboArray[i] < mStopLetter[i])
						break;
				}
				
				if (i == rangeDepth)
					return;

			}

			comboArray[numberOfBits - 1] = currentPos;


			// when number of bits equals letterCnt then this is a valid combo
			// count the words for this combo
			if (numberOfBits == mLetterCnt) {
				mComboCount++;

				mComboWordCnt = mWordTree.countWords(comboArray);

				// record new record if found
				if (mComboWordCnt > mBestComboWordCnt) {

//					System.out.println("combowordcount "+mComboWordCnt+","+mComboCount);
					mBestComboWordCnt = mComboWordCnt;

					System.arraycopy(comboArray, 0, mBestComboArray, 0, mLetterCnt);
					
					if (mGlobalBestComboWordCnt < mBestComboWordCnt) {
						setGreatest(mBestComboWordCnt);
					}
				}
			} else {
				// when rebuilding have to go to next letter
				generateCombosAndCountWords(numberOfBits + 1, currentPos + 1, comboArray);
			}
			currentPos++;
		}
	}
}