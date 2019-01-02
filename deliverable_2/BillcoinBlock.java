import java.math.BigInteger;
import java.util.ArrayList;
public class BillcoinBlock{
    public static ArrayList<String> txList;
    public static BigInteger hash;
    public static String nonce;
    public static String prevHash;//replace with bigint
    public static String concatRoot;
    public static String difficulty;
    public static int totalInputsOutputs;    
    public static long timestamp; //maybe bigint?
    
    public BillcoinBlock(String difficultyLevel, long time, String prev){
        txList = new ArrayList<String>();
        nonce = null;
        concatRoot = null;
        hash = null;
        difficulty = difficultyLevel;
        prevHash = prev;
        timestamp = time;
    }
    public void addTx(String s){
        txList.add(s);
        return;
    }
    public void setConcatRoot(String s){
        concatRoot = s;
        return;
    }
    public void setWeight(int w){
        totalInputsOutputs = w;
        return;
    }
    public void setNonce(String s){
        nonce = s;
        return;
    }
    public void setHash(BigInteger b){
        hash = b;
        return;
    }
    public BigInteger getHash(){
        return hash;
    }
    public BigInteger getHashAsHex(){
        byte[] bytes = hash.toByteArray();
        StringBuffer toReturn = new StringBuffer();
        for (int j = 0; j < bytes.length; j++) {
            String hexit = String.format("%02x", bytes[j]);
            toReturn.append(hexit);
        }
        
        return hash;
    }
    public String getConcatRoot(){
        return concatRoot;
    }
    public int getWeight(){
        return totalInputsOutputs;
    }
    public String getnonce(){
        return nonce;
    }
    public String getDifficulty(){
        return difficulty;
    }
    public String getTimeStamp(){
        return String.valueOf(timestamp);
    }
    public String getPrevHash(){
        return prevHash;
    }
    public String print(){
        String h = hash.toString(16);
        // String.format("%064c" , h);
        String s = "CANDIDATE BLOCK = Hash " + h + "\n---\n";
        s += prevHash + "\n";
        s += totalInputsOutputs + "\n";
        s += timestamp + "\n";
        s += difficulty + "\n";
        s += nonce + "\n";
        s += concatRoot + "\n";
        for(String tx : txList)
            s += tx + "\n";     
        return s;
    }
}