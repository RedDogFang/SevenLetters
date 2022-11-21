import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {

		int startRecord = 0;
		int endRecord = 6;
		String recordsRead = "runs.txt";
		String recordsWrite = "runs.txt";
		int iterations = 10;
		// createTestFile();
		ArrayList<Record> records = loadRuns(recordsRead);
		SevenLetters sl = new SevenLetters();

		// this will check how many words a combo spells 
		// it does not verify that the combo is the best
		// Verifier verifier = new Verifier(); 

		// recordIndex is an index into the Record array below.
		// it determines which test is run
		// NOTE: change recordIndex in 'for' loop to select which test(s) to run
		for (int recordIndex=startRecord; recordIndex<=endRecord; recordIndex++){

			Solution sol = new Solution(records.get(recordIndex).filename,
			                        	records.get(recordIndex).numberOfLetters);

			
			long start = System.currentTimeMillis();
			for (int i=0; i<iterations; i++){
				sol.reset();
				sl.doTheWork(sol);
			}
			sol.fullTime = (System.currentTimeMillis() - start)/iterations;

			// use this to verify the matched words for a single combo
			// Verifier.verify(sol);
			if (printSummary(sol, records.get(recordIndex))){
				records.get(recordIndex).duration = sol.fullTime;
			}
		}

		saveRuns(records,recordsWrite);
		System.out.println("all done");
	}

	private static boolean printSummary(Solution sol, Record record){
		System.out.print(sol.filename+", "+sol.numberOfLetters +" letters, {"+sol.winningCombo+"} spelled "+sol.numberOfWordsSpelled+" words, "+sol.fullTime+" msec (prev best "+record.duration+" msec)");
		boolean newRecord = false;
		if (verifyCombo(sol.winningCombo, record.combo)){
			if (sol.numberOfWordsSpelled == record.numberOfWordsSpelled){
				if (sol.fullTime < record.duration){
					System.out.print("  A NEW RECORD!!!!");
					newRecord = true;
				}
			}
			else{
				System.out.print("\n  combo is correct but word count should be "+record.numberOfWordsSpelled);
			}
		}
		else{
			System.out.print("\n  combo does not match expected ("+record.combo+"), expected count is "+record.numberOfWordsSpelled);
		}

		if (sol.fileLoadTime>0){
			System.out.print("\n ("+sol.sizeOfFile+" filesize, "+sol.wordsInFile+" words, "+sol.fileLoadTime+" msec load and parse");
		}
		System.out.println("\n");

		return newRecord;
	}

	private static boolean verifyCombo(String combo1, String combo2){
		if (combo1.length() != combo2.length()){
			return false;
		}
		for (int i=0; i<combo1.length(); i++){
			if (combo2.indexOf(combo1.charAt(i))<0){
				return false;
			}
		}
		return true;
	}

	private static ArrayList<Record> loadRuns(String filename){
		ArrayList<Record> records = new ArrayList<>();
		BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		String line = "";
		do{
			try {
				line = in.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (line == null){
				break;
			}

			records.add(new Record(line));
		} while (true);
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return records;
	}

	private static void saveRuns(ArrayList<Record> records, String filename){
		BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		for(Record r:records){
			try {
				out.write(r.toString()+"\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// class to hold records
	static private class Record{
		String filename;
		int numberOfLetters;
		int numberOfWordsSpelled;
		String combo;
		long duration;

		public Record(String filename, int numberOfLetters, int numberOfWordsSpelled, String combo, long duration){
			this.filename = filename;
			this.numberOfLetters = numberOfLetters;
			this.numberOfWordsSpelled = numberOfWordsSpelled;
			this.combo = combo;
			this.duration = duration;
		}

		public Record(String line){
			String[] values = line.split(",");
			this.filename = values[0];
			this.numberOfLetters = Integer.parseInt(values[1]);
			this.numberOfWordsSpelled = Integer.parseInt(values[2]);
			this.combo = values[3];
			this.duration = Long.parseLong(values[4]);
		}

		public String toString(){
			return filename+","+numberOfLetters+","+numberOfWordsSpelled+","+combo+","+duration;
		}

	}

	// a support routine used to generate a test file
	private static void createTestFile(){
		String str = "abcd";
		byte[] bArray = new byte[str.length()];
		for (int i=0; i<str.length(); i++){
			bArray[i] = (byte) str.charAt(i);
		}
		PrintStream mOut = null;

		try {
			mOut = new PrintStream(new File("englishWordsTest.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		int limit = 1<<str.length();
		byte[] mOutBytes = new byte[str.length()+1];

		for (int bits=1; bits<limit; bits++){
			int b = bits;
			int index = 0;
			String strOut = "";
			int bIndex = 0;
			while(b>0){
				if ((b&1) == 1){
					mOutBytes[bIndex++] = bArray[index];
				}
				index++;
				b = b >> 1;
			}
			mOutBytes[bIndex++] = 0xd;
			mOut.write(mOutBytes, 0, bIndex);
		}

		mOut.close();
	}
}