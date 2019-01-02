import java.math.BigInteger;
import java.util.ArrayList; 

public class LaboonHash
{
    private static boolean verbose = false;
    private static final String INITIALIZATION_VECTOR = "1AB0"; 
    private static String charsToHexString(char[] chars){
        StringBuffer hex = new StringBuffer();
        for(int i = 0; i < chars.length; i++){
            hex.append(Integer.toHexString((int)chars[i]));
        }
        return hex.toString();
    }

    private static String[] chunk(String input){
        ArrayList<String> blockList = new ArrayList<String>();
        String[] retArr;
        for(int i = 0; i < input.length(); i +=8){
            if(input.length() - i < 8){
                String shortBlock = input.substring(i);
                int remspaces = 8 - shortBlock.length();
                BigInteger mod = new BigInteger("16");
                // System.out.println("mod: "+mod.toString());
                BigInteger maxLength = mod.pow(remspaces);
                // System.out.println("mod^n: "+maxLength.toString());
                BigInteger inputLength = new BigInteger(Integer.toString(input.length()));
                // System.out.println("in length: "+inputLength.toString());

                BigInteger padVal = inputLength.mod(maxLength);
                String pad = padVal.toString(16);
                
                // System.out.println("arrlen % 16^n: "+pad);
                StringBuffer sb = new StringBuffer();
                int numZeros = remspaces - pad.length();
                sb.append(shortBlock);
                for(int j = 0; j < numZeros ;j++)
                    sb.append("0");
                sb.append(pad);
                // System.out.println("finished end block "+sb.toString());
                blockList.add(sb.toString());
                break;
            }
            blockList.add(input.substring(i, i+8));
        }
        retArr = new String[blockList.size()];
        blockList.toArray(retArr);
        return retArr;
    }
    public static String c (String arg0, String block ){
        char[] lhs = arg0.toCharArray();
        char[] rhs = block.toCharArray();
        char[] res = new char[4];
        //phase 1
        res[0] = (char) (lhs[0] + rhs[3]); 
        res[1] = (char) (lhs[1] + rhs[2]); 
        res[2] = (char) (lhs[2] + rhs[1]); 
        res[3] = (char) (lhs[3] + rhs[0]); 
        //phase 2
        res[0] = (char) (res[0] ^ rhs[7]);
        res[1] = (char) (res[1] ^ rhs[6]);
        res[2] = (char) (res[2] ^ rhs[5]);
        res[3] = (char) (res[3] ^ rhs[4]);
        //phase 3
        res[0] = (char) (res[0] ^ res[3]);
        res[1] = (char) (res[1] ^ res[2]);
        res[2] = (char) (res[2] ^ res[1]);
        res[3] = (char) (res[3] ^ res[0]);
        //format output
        for (int i = 0; i < res.length; i++){
            res[i] = (char) (res[i] % 16);
        }
        String hexResult = charsToHexString(res);
        hexResult = hexResult.toUpperCase();
        return hexResult;
    }
    public static String laboonHash(String input){
        String[] blocks = chunk(input);
        String result = "";
        if(verbose){
            System.out.println("Blocks:");
            for(String s : blocks){
                System.out.println(s);
            }
        }
        
        String cArg = INITIALIZATION_VECTOR;
        for(String block : blocks)
        {
            result = c(cArg,block);
            vlog("Iterating with "+ cArg + " / " + block +" = " + result);
            cArg = result;
        }
        vlog("Final result: " + result);
        return result;
    }

    private static void showUsage(){
        System.out.println(
            "Usage:\n"+
            "java LaboonHash *string* *verbosity_flag*\n"+
            "Verbosity flag can be omitted for hash output only\n"+
            "Other options: -verbose");
        System.exit(-1);
    }
    private static void vlog(String s){
        if( verbose ){
            System.out.println(s);
        }
    }
    public static boolean setVerbosity(boolean b){
        verbose = b;
        return verbose;
    }
    public static void main(String[] args){
        if (args.length < 1 || args.length > 2){
            showUsage();
        }
        else if(args.length == 2){
            if (args[1].equals("-verbose")){
                verbose = true;
            }
            else{
                showUsage();
            }
        }
        String inputString = args[0];
        if(inputString.contains("\"")){
            inputString = inputString.replaceAll("^\"|\"$", "");
        }
        String hash = laboonHash(inputString);
        System.out.println("LaboonHash hash = "+hash);
        System.exit(0);
    }
}