import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import Letters7.SevenLetters;
import Letters7.Solution;
import Letters7.Record;

public class Main {
	public static void main(String[] args) {
		ArrayList<Solution> solutions = new ArrayList<>();
		String recordsRead = "runs.txt";
		String recordsWrite = "runs.txt";
		ArrayList<Record> records = loadRuns(recordsRead);
		SevenLetters sl = new SevenLetters();

		// this will check how many words a combo spells 
		// it does not verify that the combo is the best
		// Verifier verifier = new Verifier(); 

		// recordIndex is an index into the Record array below.
		// it determines which test is run
		// NOTE: change recordIndex in 'for' loop to select which test(s) to run
		for (int recordIndex=0; recordIndex<records.size(); recordIndex++){
			Record currentRecord = records.get(recordIndex);
			if (currentRecord.passThru){
				continue; // skip comments, empty or invalid lines
			}

			for (int i=0; i<currentRecord.iterations; i++){
				System.gc();
				// a lightweight class for tracking each run
				Solution sol = new Solution(currentRecord);
				solutions.add(sol);
				long startTime = System.currentTimeMillis();
				sl.doTheWork(sol);
				sol.fullTime = System.currentTimeMillis() - startTime;
			}

			// while (solutions.size() > 1){
			// 	Solution s0 = solutions.get(0);
			// 	Solution s1 = solutions.get(1);

			// 	if (s0.winningCombo.length() > s1.winningCombo.length()){
			// 		// if ()
			// 	}
			// }
			
			for(Solution sol : solutions){
				if (printSummary(sol,currentRecord)){
					currentRecord.totalTime_msec = sol.fullTime;
					currentRecord.fileSize = sol.sizeOfFile;
					currentRecord.bestCombo = sol.winningCombo;
					currentRecord.loadFileAndParseTime_msec = sol.fileLoadTime;
					currentRecord.numberOfCombos = sol.comboCount;
					currentRecord.numberOfWordsSpelled = sol.numberOfWordsSpelled;
					currentRecord.wordsInFile = sol.wordsInFile;
				}
			}
		}

		saveRuns(records,recordsWrite);
		System.out.println("all done");
	}

	private static boolean printSummary(Solution sol,Record record){
		System.out.println(sol.toString());
		boolean newRecord = false;
		if (verifyCombo(sol.winningCombo, record.bestCombo)){
			if (sol.numberOfWordsSpelled == record.numberOfWordsSpelled){
				if (sol.fullTime < record.totalTime_msec){
					System.out.println("  A NEW RECORD!!!!");
					newRecord = true;
				}
			}
			else{
				System.out.print("\n  combo is correct but word count should be "+record.numberOfWordsSpelled);
			}
		}
		else if (record.numberOfWordsSpelled==-1){
			System.out.println("  SAVING FIRST TIME RUN");
			newRecord = true;
		}
		else{
			// System.out.print("\n  combo does not match expected ("+record.combo+"), expected count is "+record.numberOfWordsSpelled);
		}

		// if (sol.fileLoadTime>0){
		// 	System.out.print("\n ("+sol.sizeOfFile+" filesize, "
		// 						   +sol.wordsInFile+" words, "
		// 	                       +sol.fileLoadTime+" msec load and parse, "
		// 						   +sol.comboCount+" combo count, "
		// 						   +sol.threadCount+" thread count)");
		// 						//    +sol.lettersChecked+" letters checked)");
		// }
		// System.out.println("\n");

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
			String line = "";
			do{
				line = in.readLine();
				if (line == null){
					break;
				}
				records.add(new Record(line));
			} while (true);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return records;
	}

	private static void saveRuns(ArrayList<Record> records, String filename){
		BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(filename));
			for(Record r:records){
				out.write(r.toString()+"\n");
			}
			out.close();
		} catch (IOException e) {
            e.printStackTrace();
        }
	}
}