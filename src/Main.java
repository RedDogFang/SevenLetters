
public class Main {

	public static void main(String[] args) {

		SevenLetters sl = new SevenLetters();
		String filename = "englishwords.txt";
		String combo;
		for (int i=7;i<=7;i++){
			combo = sl.doTheWork(filename, i);
			new Verifier(filename, combo);
		}
		System.out.println("all done");
	}
}

//multithreaded 9/16/18
//-----------Processing englishwords.txt------------
//done (6 letters) 345: aeprst  0.072
//done (7 letters) 622: aelprst  0.226
//done (8 letters) 1217: adeinrst  0.796
//done (9 letters) 2112: adeginrst  2.908
//done (10 letters) 3428: acdeinorst  9.139
//done (11 letters) 5498: acdeilnorst  23.932
//done (12 letters) 8483: acdeilnoprst  52.876
//done (13 letters) 11850: acdeilnoprstu  93.232
//done (14 letters) 16418: acdeilmnoprstu  149.749
//
//-----------Processing bible.txt------------
//done (6 letters) 166322: adehnt  0.131
//done (7 letters) 197213: adehint  0.212
//done (8 letters) 236634: adehinst  0.696
//done (9 letters) 276675: adehinost  2.174
//done (10 letters) 315749: adefhinost  6.025
//done (11 letters) 361859: adefhinorst  15.049
//done (12 letters) 406916: adefhilnorst  29.974
//done (13 letters) 453156: adefhilnorstw  50.937
//done (14 letters) 500140: adefhilmnorstw  73.626
//
//-----------Processing shakespeare.txt------------
//done (6 letters) 148477: aehist  0.132
//done (7 letters) 180944: aehiost  0.28
//done (8 letters) 225207: aehinost  0.85
//done (9 letters) 275461: adehinost  2.898
//done (10 letters) 322352: adehinorst  8.648
//done (11 letters) 373780: adehinorstw  21.819
//done (12 letters) 423410: adehimnorstw  44.821
//done (13 letters) 477064: adefhilnorstw  80.486
//done (14 letters) 534176: adefhilmnorstw  117.594
