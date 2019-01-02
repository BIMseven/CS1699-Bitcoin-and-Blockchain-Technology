import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Mine{
//----------------------------------------------------------------------------------FIELDS
    private static ArrayList<String> txlist = null;
    private static final BigInteger MAX_TARGET = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF",16);
//----------------------------------------------------------------------------------METHODS
    // /**
    //  * This increments our String nonce by accepting a String version
    //  * and returning a String version.  For example:
    //  * "000A" -> "000B"
    //  * "FFFE" -> "FFFF"
    //  * @param nonce initial nonce
    //  * @return nonce incremented by one in string form
    //  */
    // public static String incrementStringNonce(String nonce) {
    //     BigInteger bi = new BigInteger(b, 16);
    //     bi = bi.add(BigInteger.ONE);
    //     return bi.toString(16);
    // }
    private static String newNonce(){
        String s = null;
        StringBuilder sb = new StringBuilder();
        Random rn = new Random();
        for(int i = 0 ; i < 4; i++){
            sb.append((char) (rn.nextInt((126-32))+32));
        }
        s = sb.toString();
        return s;
    }
    /**
     * Given some arbitrary byte array bytes, convert it to a hex string.
     * Example: [0xFF, 0xA0, 0x01] -> "FFA001"
     * @param bytes arbitrary-length array of bytes
     * @return String hex string version of byte array
     */
    private static String convertBytesToHexString(byte[] bytes) {
        StringBuffer toReturn = new StringBuffer();
        for (int j = 0; j < bytes.length; j++) {
            String hexit = String.format("%02x", bytes[j]);
            toReturn.append(hexit);
        }
        return toReturn.toString();
    }
    // /**
    //  * Prepend a string with 0's until it is of length n.
    //  * Useful for printing out hash results.
    //  * @param str String to prepend 0's to
    //  * @param n correct size of string after being padded
    //  * @return String str left-padded with 0's
    //  */
    // public static String leftPad(String str, int n) {
    //     return String.format("%1$" + n + "s", str).replace(' ', '0');
    // }
    private static int getWeight(String tx){
        int wgt = 0;
        for (char c : tx.toCharArray())
            if(c == '>')
                wgt++;
        return wgt;
    }
    private static float getFee(String tx){
        int in = 0; int out = 0;int fee = 0;
        String splittx[] = tx.split(";");
        //get inputs
        Pattern p = Pattern.compile(">(\\d+)");
        for(int i =0; i < 2; i++){
            Matcher m = p.matcher(splittx[i]);
            while(m.find()){
                String tnum = m.group();
                Integer tm = Integer.parseInt(tnum.substring(1));
                if(i ==0){
                    in += tm.intValue();
                }else{
                    out += tm.intValue();
                }
            }
        }
        fee = in - out;
        return fee;
    }

    private static boolean setNonceAndHash(BillcoinBlock b){
        String beforeNonce = b.getPrevHash() + b.getWeight() + b.getTimeStamp() + b.getDifficulty();
        BigInteger targetDifficulty = new BigInteger(b.getDifficulty(),16);
        BigInteger target = MAX_TARGET.divide(targetDifficulty);
        String nonce = newNonce();
        while(true){
            BigInteger attempt = Sha256Hash.hashBigInteger(beforeNonce+nonce+b.getConcatRoot());
            if(attempt.compareTo(target) == -1 ){
                b.setNonce(nonce);
                b.setHash(attempt);
                return true;
            }else{
                nonce = newNonce();
            }
        }
    }

    private static void getCandidateBlock(BillcoinBlock b){
        TreeMap<Float,String> map = new TreeMap<Float,String>();
        for(String s : txlist){
            float fee = getFee(s);
            int weight = getWeight(s);
            float ratio = fee/weight;
            map.put(ratio,s);
        }
        String concatRoot = "1333dGpHU6gQShR596zbKHXEeSihdtoyLb>50";
        int blockweight = 1;
        Iterator<Float> keys = map.descendingKeySet().iterator();
        do{
            if(!keys.hasNext()){
                break;
            }
            Float nextR = keys.next();
            String tx = map.get(nextR);
            int txWgt = getWeight(tx); 
            if(txWgt+blockweight <= 16){
                tx.trim();
                b.addTx(tx);
                txlist.remove(tx);
                concatRoot += tx;
                blockweight += txWgt;
            }
        }while((blockweight < 16));
        b.setConcatRoot(Sha256Hash.calculateHash(concatRoot));
        b.setWeight(blockweight);
        return;
    }
//----------------------------------------------------------------------------------MAIN
    public static void main(String[] args) {
        if(args.length < 3){
            System.out.print("Invalid number of arguments");
            System.exit(1);
        }
        //Unpack args
        // int difficulty = args[1];
        // BigInteger prevHash = new BigInteger(args[2]);
        
        TreeMap<Float,String> map = new TreeMap<Float,String>();
        //get list of Tx's, read their data, and then place them in in trees!
        try{
            txlist = new ArrayList<String>();
            FileInputStream fis = new FileInputStream(args[0]);
            Scanner sc = new Scanner(fis);
            while(sc.hasNextLine()){
                txlist.add(sc.nextLine());
            }
        }catch(FileNotFoundException e){
            System.out.print(e.getMessage());
            System.exit(1);
        }
        /*
        we've got a list of our transaction pool... we now compute value/weight ratios
        and place tx's into a treemap based on thier ratios
        */
        // BigInteger prevHash = args[2];
        String prev = args[2];
        do{
            BillcoinBlock bb = new BillcoinBlock(args[1], Instant.now().toEpochMilli(), prev);
            getCandidateBlock(bb);
            setNonceAndHash(bb);
            prev = bb.getHash().toString(16);
            System.out.println(bb.print());
        }while(!txlist.isEmpty());
    }
}
