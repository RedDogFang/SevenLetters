package Letters7;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FullBitmaps {
    final int BITCOUNT = 26;

    public FullBitmaps(){
		try {
			BufferedWriter out = null;
            out = new BufferedWriter(new FileWriter("fullbitmaps.txt"));
            int count=0;
            int bitmap = 0;
            while (bitmap != -1){
                bitmap = getNext(bitmap);
                if (bitmap != -1){
                    out.write((count++)+" "+String.format("%x",bitmap)+" "+intToBinary(bitmap)+"\n");
                }
            }
			out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }

    private int getNext(int bitmap){
        // if (bitmap == 0){
        //     return 0x7f;
        // }
        // if ((bitmap & (0x2000000)) == 0){
        //     return bitmap << 1;
        // }
        // System.out.println("before  -------------------------"+String.format("%x",bitmap)+" "+intToBinary(bitmap));
        // bitmap >>= (LowBitOffsets.getLowBitPos(bitmap));
        // System.out.println("shifted--------------------------"+String.format("%x",bitmap)+" "+intToBinary(bitmap));

        System.out.println("final "+String.format("%x",bitmap));
        bitmap++;
        int count = BitCounts.getBitCount(bitmap);
        while (count != 7){
            // System.out.println(String.format("%x %d",bitmap,BitCounts.getBitCount(bitmap)));
            // bitmap++;
            if (count > 7){
                bitmap += 1<<(LowBitOffsets.getLowBitPos(bitmap));
            }
            else{
                bitmap++;
            }
            if ((bitmap & 0x4000000) != 0){
                return -1;
            }
            count = BitCounts.getBitCount(bitmap);
        }

        System.out.println("final  -------------------------"+String.format("%x",bitmap)+" "+HighBitOffsets.getHighBitPos(bitmap));

        return bitmap;
    }

    private String intToBinary(int bitmap){
        String ans = "";
        for (int i=0; i<BITCOUNT; i++){
            if ((bitmap&1) == 1){
                ans = "1"+ans;
            }
            else{
                ans = "0"+ans;
            }
            bitmap >>= 1;
        }
        return ans;
    }    
}
