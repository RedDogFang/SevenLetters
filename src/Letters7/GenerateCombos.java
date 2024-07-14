package Letters7;

import java.util.Arrays;

public class GenerateCombos implements Runnable {

	// dynamically changing the alphabet size is not
	// supported so it is a constant at the top of 
	// every file that needs it
	final static int kAlphabetSize = 26;
	private WordTree mWordTree;
	private ISevenLetters msl;
	private int mStartLetter;
	private int mStopLetter;
	private int mComboCount = 0;
	private int mLetterCnt = 7;
	public static int mBestComboWordCnt = -1;
	private int[] comboArray;
	public static int[] mBestComboArray = new int[26];
	private static int counter = 0;
	private static Object obj = new Object();
	int id;
	
	public GenerateCombos(WordTree wordTree, ISevenLetters sl, int letterCnt, int startLetter, int stopLetter, int id) {
		counter = 0;
		mStartLetter = startLetter;
		comboArray = new int[letterCnt];
		for(int i=0; i<letterCnt; i++){
			comboArray[i] = startLetter+i;
		}
		mStopLetter = stopLetter;
		mLetterCnt = letterCnt;

		msl = sl;
		mWordTree = wordTree;
		this.id = id;

	}

	public static synchronized int increment(int adder){
		counter+=adder;
		return counter;
	}
	
	public synchronized void setGreatest(int newMax,int[] comboArray) {
		mBestComboWordCnt = newMax;
		System.arraycopy(comboArray, 0, mBestComboArray, 0, comboArray.length);
	}
	
	public int getCombos() {
		return mComboCount;
	}
	int comboCnt = 0;
	boolean allDone;
	// int ccc;
	public void run() {
		// mGlobalBestComboWordCnt = -1;
		// System.out.println("run: start "+Arrays.toString(comboArray)+", stop "+(char)('a'+mStopLetter));
		allDone = false;
		// ccc = 0;
		generateCombosAndCountWords(comboArray);
		// System.out.println("done id "+id);
		// System.out.println("done: start "+(char)('a'+mStartLetter)+", stop "+(char)('a'+mStopLetter)+" combo "+Arrays.toString(comboArray)+", "+id);
		msl.done(id,comboCnt,0);//ccc);
		
		increment(1);
	}
	
	private boolean advanceCombo(int[] comboArray, int stopLetter){
		
		int maxLetter = kAlphabetSize - 1;
		int index = comboArray.length - 1;

		while (index >= 0 && comboArray[index]==maxLetter){
			maxLetter--;
			index--;
		}
		if (index>=0){
			comboArray[index]++;
			if (comboArray[0]== mStopLetter){
				return false;
			}
			index++;
			for (; index<mLetterCnt; index++){
				comboArray[index] = comboArray[index-1]+1;
			}
			return true;
		}

		return false;
	}

	private void generateCombosAndCountWords(int[] comboArray) {
		while (advanceCombo(comboArray, mStopLetter)){
			int cnt = mWordTree.getComboArrayCnt(comboArray);
			if (cnt>mBestComboWordCnt){
				setGreatest(cnt, comboArray);
			}
		}
	}
}