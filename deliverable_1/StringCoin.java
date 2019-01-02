//@authoor Benjamin Miller
//email: bim7@pitt.edu
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Set;
/* 
    Bill (Creator of all coins):

    Public key (pk): 3081f03081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca403430002405b0656317dd257ec71982519d38b42c02621290656eba54c955704e9b5d606062ec663bdeef8b79daa2631287d854da77c05d3e178c101b2f0a1dbbe5c7d5e10
    Private key (sk): 3081c60201003081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca404160214556d46e1888b30bccf9c4a5ea71b41c107b5d219

    Mario:

    Public key (pk): 3081f03081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca40343000240437cefda4a79ded357c6a976803e73ba9f08cebc257e401dd42d8e71a04e7d8fb97f3d70c7fdd1b7579af65b407b2f382f316d3afb9b687d1c289c1bf6a1ee04
    Private key (sk): 3081c60201003081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca4041602141da8d6f0b3653346a5377cab2fc7140022206a31

    Peach:

    Public key (pk): 3081f03081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca40343000240502d40adb1c58beaede4cee3ce8450626a5922b60ef66f4d96d7496cda8661dd2c06c3a09b4fadcd3a6c36b7bdec474fde18cf7bff68258f0edfa281857d72aa
    Private key (sk): 3081c60201003081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca4041602140767f5828e24761782413054778d969a06b0ce26 

*/
/*
    Noted Errors: Signing produces output different from that expected from blockchain file.
                    
                Verify causes java.security.SignatureException: Invalid encoding for signature error
                so nothing is verified.
*/
public class StringCoin {

    static String billpk = "3081f03081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca403430002405b0656317dd257ec71982519d38b42c02621290656eba54c955704e9b5d606062ec663bdeef8b79daa2631287d854da77c05d3e178c101b2f0a1dbbe5c7d5e10";
    static String billsk = "3081c60201003081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca404160214556d46e1888b30bccf9c4a5ea71b41c107b5d219";
    static Hashtable coins;
    public static void main(String[] args){
        try {
            // KeyPair keys = PublicKeyCrypto.getKeyPair();
            // pkBill = keys.getPublic();
            // skBill = keys.getPrivate();
            PublicKey pkBill = PublicKeyCrypto.loadPublicKey(billpk);
            PrivateKey skBill = PublicKeyCrypto.loadPrivateKey(billsk);

            
            // Read the blockchain from args[0]
            FileInputStream fis = new FileInputStream(args[0]);
            Scanner sc = new Scanner(fis);
            ArrayList<String> lines = new ArrayList();
            while(sc.hasNextLine()){
                String s = sc.nextLine();
                if(!s.equals("")){
                    lines.add(s);
                }
            }
            // mainloop
            
            coins = new Hashtable<String,String>();
            boolean proceed = true;
            String hashOfPrev = null;
            int blockCount = 0;
            
            for(String line : lines){
                // break up into byte arrays
                // System.out.println(line);
                String fields[] = new String[5];
                fields = line.split(",");
                String prev = new String(fields[0].getBytes(StandardCharsets.US_ASCII)); String type = new String(fields[1].getBytes(StandardCharsets.US_ASCII));String coinid = new String(fields[2].getBytes(StandardCharsets.US_ASCII));
                String toAddr = new String(fields[3].getBytes(StandardCharsets.US_ASCII));String sig  = fields[4]; 

                // System.out.println(PublicKeyCrypto.signMessage(coinid, billsk));
                //check PREV
                if(hashOfPrev == null && prev.equals("0")){
                    hashOfPrev = Sha256Hash.calculateHash(line);
                }else if(prev.equals(hashOfPrev)){
                    hashOfPrev = Sha256Hash.calculateHash(line);
                }else{
                    printEnd("previous hash on block "+blockCount+"is incorrect");
                }
                // verify the coin

                // billpk.getBytes(StandardCharsets.US_ASCII)
                // && coins.contains(coinid)
                // coins.put(coinid,billpk);
                if(type.equals("CREATE")){
                    //until we get sign/verify to work...
                    coins.put(coinid,billpk);
                    // if(PublicKeyCrypto.verifyMessage(toAddr, coinid, billpk)){
                    //     System.out.print("happy");
                    // }else{
                    //     System.out.print("creation of coin "+coinid+" was invalidly signed");
                    //     // printEnd("creation of coin "+coinid+" was invalidly signed");
                    // }
                }
                //check for transfering of an old key
                if(type.equalsIgnoreCase("TRANSFER")){
                    if(coins.containsKey(coinid)){
                        coins.replace(coinid, toAddr);
                    }else{
                        printEnd("Transfer of coin "+coinid+" failed");
                    }
                }
                blockCount++;
            }
            System.out.println("Blockchain is valid!");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("Blockchain" + args[0] + "doesn't exist!");
            System.exit(-1);
        }catch(Exception e){
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(-1);
        }

        Set<String> coinlist = coins.keySet();
        Object[] coinarr = coinlist.toArray();
        Arrays.sort(coinarr); 
        for(int i =0;i<coinarr.length; i++) {
            System.out.println("Coin "+coinarr[i].toString()+" / Owner "+coins.get(coinarr[i].toString()));
        }
        
        // on exit
        System.exit(0);
    }
public static void printEnd(String ermsg){
    System.out.println(ermsg);
    System.exit(-1);
    return;
}
}
