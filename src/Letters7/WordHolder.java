package Letters7;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

// array of occurances for different amount of letters (bits) for 26 letters
// 1 bit => 26
// 2 bits => 325
// 3 bits => 2600
// 4 bits => 14950
// 5 bits => 65780
// 6 bits => 230230
// 7 bits => 657800
// 8 bits => 1562275
// 9 bits => 3124550
// 10 bits => 5311735
// 11 bits => 7726160
// 12 bits => 9657700
// 13 bits => 10400600
// 14 bits => 9657700
// 15 bits => 11 bits
// and so on

public class WordHolder {
    // HashMap<Integer, Integer> wordsMap;
    // int[] wordsArray;
    // int arraySize;
    // private final int kDigitSize = 5;
    // private final int kDigitMask = 0x1f;
    // private final int kAlphabetSize = 26;
    // boolean arrayBacked;
    int maxCount;
    int maxBits;
    int letterCnt;
    // BufferedWriter out = null;
    // int[] arraySizes = {0, 26, 325, 2600, 14950, 65780, 230230, 657800};

    WordTree wordTree ;

    public WordHolder(int letterCnt){
        this.letterCnt = letterCnt;
        // wordTree = new WordTree(letterCnt);    
    }

    public void addWord(int wordBits, int additional){

        wordTree.addWord(wordBits,additional);
        // try {
        //     out.write(letterCnt+" new word "+printLetters(wordBits)+" bitmap "+String.format("%x ",wordBits)+" "+newCount+"\n");
        // } catch (IOException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }

        // if (newCount > maxCount){
        //     maxCount = newCount;
        //     maxBits = wordBits;
        // }
    }

    public int getMaxWordCount(){
        return maxCount;
    }

    public int getMaxWordBits(){
        return maxBits;
    }

    public void combineWithTree(WordHolder wordHolder){
        // try {
        //     out.write(letterCnt+"*********** combine word *************"+wordHolder.letterCnt+"\n");
        // } catch (IOException e1) {
        //     // TODO Auto-generated catch block
        //     e1.printStackTrace();
        // }
        // if (arrayBacked){
        //     for (int j=0; j<arraySize; j++){
        //         int wordCount = wordsArray[j];
        //         if (wordCount>0){
        //             int origBitmap = indexToBitmap(j);
        //             for (int i=0; i<kAlphabetSize; i++){
        //                 int newLetter = 1<<i;
        //                 int newBitmap = origBitmap|newLetter;
        //                 if (newBitmap != origBitmap){
        //                     wordHolder.addWord(newBitmap, wordCount);
        //                 }
        //             }
        //         }
        //     }
        // }
    }

    public void dumpHolder(){
        // try {
        //     out.write("++++++++++ dump holder ++++++++++++\n");
        // } catch (IOException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
        // if (arrayBacked){
        //     for (int i=0; i<arraySize; i++){
        //         if (wordsArray[i]>0){
        //             try {
        //                 out.write(letterCnt +" index: "+i+",  bitmap: "+indexToBitmap(i)+", count: "+wordsArray[i]+"\n");
        //             } catch (IOException e) {
        //                 // TODO Auto-generated catch block
        //                 e.printStackTrace();
        //             }
        //         }
        //     }
        // }
    }

	public String printLetters(int wordBits){
		String s="";

		while (wordBits>0){
			int letter = LowBitOffsets.getLowBitPos(wordBits);
			s += (char)('a'+letter);
			wordBits &= ~1<<letter;
		}
		return s;
	}
}