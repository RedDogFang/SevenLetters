import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Verifier{

    public Verifier(String filename, String comboStr){
        char[] combo = comboStr.toCharArray();
        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String lineStr;
        int wordCount = 0;
        boolean inWord = false;
        boolean skipAhead = false;
        int comboLast = combo.length-1;
            
        try {
            while ((lineStr = in.readLine()) != null) {
                skipAhead = false;
                inWord = false;

                for(int i=0; i<lineStr.length(); i++){
                    char c = lineStr.charAt(i);

                    if (c<=' '){

                        if (inWord){
                            wordCount++;
                        }
                        skipAhead = false;
                        inWord = false;
                        continue;
                    }

                    // check if skipAhead set and not between words
                    if (skipAhead){
                        continue;  // move to next letter
                    }
    
                    // lower case letters
                    if (c>='A' && c<='Z'){
                        c += 0x20;
                    }

                    // 
                    if (c >='a' && c<='z'){
                        // compare one letter from word to all chars in combo
                        int j;                        

                        for (j=0; j<=comboLast; j++){
                            if (c == combo[j]){
                                break;
                            }
                        }

                        if (j>comboLast){
                            skipAhead = true;
                            inWord = false;
                            continue;
                        }

                        inWord = true;
                        
                    }
                }
                if (inWord){
                    wordCount++;
                }
                skipAhead = false;
            }
            in.close();
        }
        catch (IOException e){
            System.out.println("IOException ");
        }

        System.out.println(filename + " "+ comboStr+ " "+wordCount);
    }
}