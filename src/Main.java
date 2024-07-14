import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

import Letters7.BitCounts;
import Letters7.HighBitOffsets;
import Letters7.LowBitOffsets;
import Letters7.SevenLetters;
import Letters7.Solution;

public class Main {
	public static void main(String[] args) {
		// int mm = 1 << -1;
		// System.out.println("="+String.format("%x",mm)+"=");
		// System.exit(mm);
		// BitCounts.dumpCounts();
		// HighBitOffsets.dumpHigh();
		// LowBitOffsets.dumpLow();
		// System.out.println();
		// System.out.println();
		// Random rnd = new Random();
		// for (int i=1;i<0x4000000; i++){
		// 	int cnt = BitCounts.getBitCount(i);
		// 	int tmp = i;
		// 	for (int j=0; j<cnt; j++){
		// 		int bitOffset;
				
		// 		if (rnd.nextBoolean()){
		// 			bitOffset = LowBitOffsets.getLowBitPos(tmp);
		// 		}
		// 		else{
		// 			bitOffset = HighBitOffsets.getHighBitPos(tmp);
		// 		}
		// 		tmp &= ~(1<<bitOffset);
		// 	}
		// 	if (tmp != 0){
		// 		System.out.println(""+i+" "+tmp);
		// 	}
		// 	if ((i%2000) ==0){
		// 		System.out.println(i);
		// 	}
		// }
			// int low = LowBitOffsets.getLowBitPos(i);
			// int high = HighBitOffsets.getHighBitPos(i);
			// System.out.print(String.format("%x low=%d, high=%d",i,low,high));
			// if (low != high){
			// 	System.out.println("   unequal");
			// }
			// else{
			// 	System.out.println("     equal");
			// }
		// }
		// System.exit(0);
		int startRecord = 0;
		int endRecord = 0;
		String recordsRead = "runs.txt";
		String recordsWrite = "runs.txt";
		int iterations = 5;
		// createTestFile();
		ArrayList<Record> records = loadRuns(recordsRead);
		SevenLetters sl = new SevenLetters();

		// this will check how many words a combo spells 
		// it does not verify that the combo is the best
		// Verifier verifier = new Verifier(); 

		// recordIndex is an index into the Record array below.
		// it determines which test is run
		// NOTE: change recordIndex in 'for' loop to select which test(s) to run
		for (int recordIndex=0; recordIndex<records.size(); recordIndex++){

			Solution sol = new Solution(records.get(recordIndex).filename,
			                        	records.get(recordIndex).numberOfLetters);

			long start = 0;
			long fastestRun = Long.MAX_VALUE;
			for (int i=0; i<iterations; i++){
				sol.reset();
				System.gc();
				start = System.currentTimeMillis();
				sl.doTheWork(sol);
				long duration = System.currentTimeMillis() - start;
	
				if (duration < fastestRun){
					fastestRun = duration;
				}
			}
			sol.fullTime = fastestRun;
			sol.iterations = iterations;

			// use this to verify the matched words for a single combo
			// Verifier.verify(sol);
			if (printSummary(sol, records.get(recordIndex))){
				records.get(recordIndex).duration = sol.fullTime;

				// save first time runs
				if (records.get(recordIndex).numberOfWordsSpelled == -1){
					records.get(recordIndex).numberOfWordsSpelled = sol.numberOfWordsSpelled;
					records.get(recordIndex).combo = sol.winningCombo;
				}
			}
		}

		saveRuns(records,recordsWrite);
		System.out.println("all done");
	}

	private static boolean printSummary(Solution sol, Record record){
		System.out.print(sol.filename+", "+sol.numberOfLetters +" letters, {"+sol.winningCombo+"} spelled "+sol.numberOfWordsSpelled+" words, "+sol.fullTime+" msec (prev best "+record.duration+" msec), iterations = "+sol.iterations);
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
		else if (record.numberOfWordsSpelled==-1){
			System.out.print("  SAVING FIRST TIME RUN");
			newRecord = true;
		}
		else{
			System.out.print("\n  combo does not match expected ("+record.combo+"), expected count is "+record.numberOfWordsSpelled);
		}

		if (sol.fileLoadTime>0){
			System.out.print("\n ("+sol.sizeOfFile+" filesize, "+sol.wordsInFile+" words, "+sol.fileLoadTime+" msec load and parse)");
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