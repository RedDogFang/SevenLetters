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
    public int comboCount;
    public int threadCount;
    
    public Solution(Record record){
        filename = record.filename;
        iterations = record.iterations;
        threadCount = record.numberOfThreads;
        numberOfLetters = record.numberOfLetters;
    }

    public String toString(){
        return "filename="+filename+
        ",numberOfLetters="+numberOfLetters+
        ",bestCombo="+winningCombo+
        ",numberOfWordsSpelled="+numberOfWordsSpelled+
        ",totalTime_msec="+fullTime+
        ",loadFileAndParseTime_msec="+fileLoadTime+
        ",iterations="+iterations+
        ",numberOfThreads="+threadCount+
        ",numberOfCombos="+comboCount+
        ",wordsInFile="+wordsInFile+
        ",fileSize="+sizeOfFile;
    }
}
