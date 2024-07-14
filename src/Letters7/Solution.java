package Letters7;

public class Solution {
    public String filename;
    public int numberOfLetters;
    public String winningCombo;
    public int numberOfWordsSpelled;
    public int wordsInFile;
    public int sizeOfFile;
    public long fileLoadTime;
    public long fullTime;
    public int iterations;
    
    public Solution(String filename, int numLetters){
        this.filename = filename;
        this.numberOfLetters = numLetters;
    }

    public void reset(){
        winningCombo="";
        numberOfWordsSpelled = 0;
        wordsInFile = 0;
        sizeOfFile = 0;
        fileLoadTime = 0;
        fullTime = 0;
    }
}
