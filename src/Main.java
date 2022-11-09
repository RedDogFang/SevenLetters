import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Main {
	public static void main(String[] args) {

		// createTestFile();
		int iterations = 10;
		SevenLetters sl = new SevenLetters();

		// this will check how many words a combo spells 
		// it does not verify that the combo is the best
		// Verifier verifier = new Verifier(); 

		// recordIndex is an index into the Record array below.
		// it determines which test is run
		// NOTE: change recordIndex in 'for' loop to select which test(s) to run
		for (int recordIndex=3;recordIndex<=3;recordIndex++){
			Solution sol = new Solution(records[recordIndex].filename,
			                        	records[recordIndex].numberOfLetters);

			
			long start = System.currentTimeMillis();
			for (int i=0; i<iterations; i++){
				sl.doTheWork(sol);
			}
			sol.fullTime = (double)(System.currentTimeMillis() - start)/1000/(double)iterations;

			// use this to verify the matched words for a single combo
			// Verifier.verify(sol);
			printSummary(sol, recordIndex);
		}
		System.out.println("all done");
	}

	private static void printSummary(Solution sol, int recordIndex){
		System.out.print(sol.filename+" "+sol.numberOfLetters +" letters, {"+sol.winningCombo+"} spelled "+sol.numberOfWordsSpelled+" taking "+sol.fullTime+" seconds (prev best "+records[recordIndex].duration+")");
		
		if (verifyCombo(sol.winningCombo, records[recordIndex].combo)){
			if (sol.numberOfWordsSpelled == records[recordIndex].numberOfWordsSpelled){
				if (sol.fullTime < records[recordIndex].duration){
					System.out.print("  A NEW RECORD!!!!");
				}
			}
			else{
				System.out.print("\n  combo is correct but word count should be "+records[recordIndex].numberOfWordsSpelled);
			}
		}
		else{
			System.out.print("\n  combo does not match expected ("+records[recordIndex].combo+") expected count is "+records[recordIndex].numberOfWordsSpelled);
		}

		if (sol.fileLoadTime>0){
			System.out.print("\n txt file size: "+sol.sizeOfFile+", number of words in file: "+sol.wordsInFile+", time to load and parse file: "+sol.fileLoadTime);
		}
		System.out.println("\n");
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

	static Record[] records =  { 
/* 0 */		new Record("englishwords.txt",4,50,"esat",.0144),
/* 1 */		new Record("englishwords.txt",5,164,"esart",.0127),
/* 2 */		new Record("englishwords.txt",6,345,"aeprst",0.0382),
/* 3 */		new Record("englishwords.txt",7,622,"aelprst",0.1537),
/* 4 */		new Record("englishwords.txt",8,1217,"adeinrst",0.796),
/* 5 */		new Record("englishwords.txt",9,2112,"adeginrst",2.908),
/* 6 */		new Record("englishwords.txt",10,3428,"acdeinorst",9.139),
/* 7 */		new Record("englishwords.txt",11,5498,"acdeilnorst",23.932),
/* 8 */		new Record("englishwords.txt",12,8483,"acdeilnoprst",52.876),
/* 9 */		new Record("englishwords.txt",13,11850,"acdeilnoprstu",93.232),
/* 10 */	new Record("englishwords.txt",14,16418,"acdeilmnoprstu",149.749),

/* 11 */		new Record("bible.txt",4,-1,"",-1),
/* 12 */		new Record("bible.txt",5,-1,"",-1),
/* 13 */		new Record("bible.txt",6,-1,"",-1),
/* 14 */		new Record("bible.txt",7,197213,"adehint",0.212),
/* 15 */		new Record("bible.txt",8,236634,"adehinst",0.696),
/* 16 */		new Record("bible.txt",9,276675,"adehinost",2.174),
/* 17 */		new Record("bible.txt",10,315749,"adefhinost",6.025),
/* 18 */		new Record("bible.txt",11,361859,"adefhinorst",15.049),
/* 19 */		new Record("bible.txt",12,406916,"adefhilnorst",29.974),
/* 20 */		new Record("bible.txt",13,453156,"adefhilnorstw",50.937),
/* 21 */		new Record("bible.txt",14,500140,"adefhilmnorstw",73.626),

/* 22 */		new Record("shakespeare.txt",4,-1,"",-1),
/* 23 */		new Record("shakespeare.txt",5,-1,"",-1),
/* 24 */		new Record("shakespeare.txt",6,148477,"aehist",0.132),
/* 25 */		new Record("shakespeare.txt",7,180944,"aehiost",0.28),
/* 26 */		new Record("shakespeare.txt",8,225207,"aehinost",0.85),
/* 27 */		new Record("shakespeare.txt",9,275461,"adehinost",2.898),
/* 28 */		new Record("shakespeare.txt",10,322352,"adehinorst",8.648),
/* 29 */		new Record("shakespeare.txt",11,373780,"adehinorstw",21.819),
/* 30 */		new Record("shakespeare.txt",12,423410,"adehimnorstw",44.821),
/* 31 */		new Record("shakespeare.txt",13,477064,"adefhilnorstw",80.486),
/* 32 */		new Record("shakespeare.txt",14,534176,"adefhilmnorstw",117.594)
				};

	// class to hold records
	static private class Record{
		String filename;
		int numberOfLetters;
		int numberOfWordsSpelled;
		String combo;
		double duration;

		public Record(String filename, int numberOfLetters, int numberOfWordsSpelled, String combo, double duration){
			this.filename = filename;
			this.numberOfLetters = numberOfLetters;
			this.numberOfWordsSpelled = numberOfWordsSpelled;
			this.combo = combo;
			this.duration = duration;
		}

	}

	// a support routine used to generate a test file
	private static void createTestFile(){
		String str = "abcdefg";
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