public class Solution {
    String filename;
    int numberOfLetters;
    String winningCombo;
    int numberOfWordsSpelled;
    int wordsInFile;
    int sizeOfFile;
    double fileLoadTime;
    double fullTime;
    
    public Solution(String filename, int numLetters){
        this.filename = filename;
        this.numberOfLetters = numLetters;
    }

    public void reset(){
        winningCombo="";
        numberOfWordsSpelled = -1;
        wordsInFile = -1;
        sizeOfFile = -1;
        fileLoadTime = -1;
        fullTime = -1;
    }
}
