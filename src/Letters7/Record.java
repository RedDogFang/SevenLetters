package Letters7;

public class Record {

	// class to hold records
    // variables needed for comments and blank lines
    // will pass straight through
    String originalLine = "";
    public Boolean passThru=false;

    // variables needed to run 
    public String filename = "";
    public int numberOfLetters = 7;

    // run results
    public int numberOfWordsSpelled = -1;
    public String bestCombo = "abcdefg";
    public long totalTime_msec = Long.MAX_VALUE;

    // stats and other info
    public int fileSize = 0;
    public long loadFileAndParseTime_msec = 0;
    public int iterations = 1;
    public int numberOfThreads = 1;
    public int numberOfCombos = 0;
    public int wordsInFile = 0;

    public Record(String line){

        originalLine = line;
        if (line.startsWith("//") || line.length() == 0){
            passThru = true;
        }
        String[] keyValuePairs = line.split(",");

        for(int i=0; i<keyValuePairs.length; i++){
            String[] keyValue = keyValuePairs[i].split("=");
            if(keyValue.length != 2){
                if ("bestcombo".equals(keyValue[0].toLowerCase())){
                    keyValue = new String[2];
                    keyValue[0] = "bestCombo";
                    keyValue[1] = "";
                }
                else {
                    System.out.println("corrupt run file");
                    System.out.println("clue: "+keyValuePairs[0]);
                    System.out.println(originalLine);
                    passThru = true;
                    continue;
                }
            }
            if("filename".equals(keyValue[0].toLowerCase())){
                filename = keyValue[1];
            }
            else if("bestcombo".equals(keyValue[0].toLowerCase())){
                bestCombo = keyValue[1];
            }
            else if("numberofwordsspelled".equals(keyValue[0].toLowerCase())){
                numberOfWordsSpelled = Integer.parseInt(keyValue[1]);
            }
            else if("wordsinfile".equals(keyValue[0].toLowerCase())){
                wordsInFile = Integer.parseInt(keyValue[1]);
            }
            else if("numberofletters".equals(keyValue[0].toLowerCase())){
                numberOfLetters = Integer.parseInt(keyValue[1]);
            }
            else if("filesize".equals(keyValue[0].toLowerCase())){
                fileSize = Integer.parseInt(keyValue[1]);
            }
            else if("numberofthreads".equals(keyValue[0].toLowerCase())){
                numberOfThreads = Integer.parseInt(keyValue[1]);
            }
            else if("iterations".equals(keyValue[0].toLowerCase())){
                iterations = Integer.parseInt(keyValue[1]);
            }
            else if("numberofcombos".equals(keyValue[0].toLowerCase())){
                numberOfCombos = Integer.parseInt(keyValue[1]);
            }
            else if("totaltime_msec".equals(keyValue[0].toLowerCase())){
                totalTime_msec = Long.parseLong(keyValue[1]);
            }
            else if("loadfileandparsetime_msec".equals(keyValue[0].toLowerCase())){
                loadFileAndParseTime_msec = Long.parseLong(keyValue[1]);
            }
            
            if (filename.length() == 0){
                System.out.println("skipping invalid line");
                System.out.println(originalLine);
                passThru = true;
            }
        }
    }

    public String toString(){
        if(passThru){
            return originalLine;
        }
        else{
            return "filename="+filename+
                ",numberOfLetters="+numberOfLetters+
                ",bestCombo="+bestCombo+
                ",numberOfWordsSpelled="+numberOfWordsSpelled+
                ",totalTime_msec="+totalTime_msec+
                ",loadFileAndParseTime_msec="+loadFileAndParseTime_msec+
                ",iterations="+iterations+
                ",numberOfThreads="+numberOfThreads+
                ",numberOfCombos="+numberOfCombos+
                ",wordsInFile="+wordsInFile+
                ",fileSize="+fileSize;
        }
    }
}
