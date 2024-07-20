package Letters7;

// this class generates all combos and counts the
// number of words it can spell
public class GenerateCombos implements Runnable {

	private WordTree wordTree;
	private int letterCnt;
	private int stopLetter;
	
	// current combo
	private int countOfCombos;
	private int[] comboArray;
	// combo with highest word count (found so far)
	public static int bestComboWordCnt = -1;
	public static int[] bestComboArray;
	
	private static int syncCounter = 0;
	private int id; // id is a unique identifier of this thread
	
	public GenerateCombos(WordTree wordTree, int letterCnt, int startLetter, int stopLetter, int id) {
		this.wordTree = wordTree;
		this.letterCnt = letterCnt;
		this.stopLetter = stopLetter;
		this.id = id;
		bestComboWordCnt = -1;
		comboArray = new int[letterCnt];
		bestComboArray = new int[letterCnt];

		// initial combo array
		for(int i=0; i<letterCnt; i++){
			comboArray[i] = startLetter+i;
		}
		syncCounter = 0;
	}

	// allow setting externally to make it easier to do it on the fly
	public void setStopLetter(int a){
		stopLetter = a;
	}

	public static synchronized int increment(int adder){
		syncCounter+=adder;
		return syncCounter;
	}
	
	// thread safe way to save best combo
	private synchronized void setGreatest(int newMax,int[] comboArray) {
		// check again in case two threads tried to do it simultaneously
		if (newMax > bestComboWordCnt){
			bestComboWordCnt = newMax;
			System.arraycopy(comboArray, 0, bestComboArray, 0, comboArray.length);
		}
	}
	
	// helpful for students to know if they are doing things correctly
	public int getNumberOfCombos() {
		return countOfCombos;
	}

	// implement multiple threads to work on finding words simultaneously
	public void run() {
		generateCombosAndCountWords(comboArray);
		// signal main thread that we are done
		increment(1);
	}
	
	// advance last letter to z then advance second to last (if possible)
	// and reset all following letters then repeat until the 0th
	// letter equals the stop letter
	private boolean advanceCombo(int[] comboArray, int stopLetter){
		int maxLetter = SevenLetters.kAlphabetSize - 1;
		int index = comboArray.length - 1;

		while (index >= 0 && comboArray[index]==maxLetter){
			maxLetter--;
			index--;
		}
		if (index>=0){
			comboArray[index]++;
			if (comboArray[0]== stopLetter){
				return false;
			}
			index++;
			for (; index<letterCnt; index++){
				comboArray[index] = comboArray[index-1]+1;
			}
			return true;
		}
		return false;
	}

	// a combo is an array of letterCnt ints
	// each element holds the index of the letter
	// a=0, b=1, c=2,...,z=25 so aelprst is
	// {0,4,11,15,22,23,24}
	private void generateCombosAndCountWords(int[] comboArray) {
		do{
			int cnt = wordTree.getComboArrayCnt(comboArray);
			countOfCombos++;
			// check here then also with the lock
			// this will fail most of the time
			if (cnt>bestComboWordCnt){
				setGreatest(cnt, comboArray);
			}
		}while (advanceCombo(comboArray, stopLetter));
	}
}