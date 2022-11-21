public class Solution {
    String filename;
    int numberOfLetters;
    String winningCombo;
    int numberOfWordsSpelled;
    int wordsInFile;
    int sizeOfFile;
    long fileLoadTime;
    long fullTime;
    
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
