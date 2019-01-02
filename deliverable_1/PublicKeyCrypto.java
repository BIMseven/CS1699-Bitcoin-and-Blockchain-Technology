
/**
 * This class demonstrates creating a public/private keypair.
 * For more information, see https://docs.oracle.com/javase/tutorial/security/apisign/index.html
 */

// import java.io.*;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class PublicKeyCrypto {

    /**
     * Given some arbitrary byte array bytes, convert it to a hex string. Example:
     * [0xFF, 0xA0, 0x01] -> "FFA001"
     * 
     * @param bytes arbitrary-length array of bytes
     * @return String hex string version of byte array
     */

    public static String convertBytesToHexString(byte[] bytes) {
        StringBuffer toReturn = new StringBuffer();
        for (int j = 0; j < bytes.length; j++) {
            String hexit = String.format("%02x", bytes[j]);
            toReturn.append(hexit);
        }
        return toReturn.toString();
    }

    /**
     * Given some arbitrary hex string, convert it to a byte array. Example:
     * "FFA001" -> [0xFF, 0xA0, 0x01] NOTE: Assumes that hex string is valid (i.e.,
     * even length)
     * 
     * @param hex arbitrary-length hex string
     * @return byte[] byte array version of hex string
     */

    public static byte[] convertHexToBytes(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        int c = 0;
        for (int j = 0; j < hex.length(); j += 2) {
            String twoHex = hex.substring(j, j + 2);
            byte byteVal = (byte) Integer.parseInt(twoHex, 16);
            bytes[c++] = byteVal;
        }
        return bytes;
    }

    /**
     * Generates a public/private key pair.
     * 
     * @return KeyPair - Public/private key pair
     */

    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "SUN");
        SecureRandom random = new SecureRandom(); // .getInstance("SHA1PRNG", "SUN");
        keyGen.initialize(256, random);
        return keyGen.generateKeyPair();
    }

    /**
     * Generate a public key in bytes given a hex string. We can then store hex
     * strings as our public key instead of raw bytes.
     * 
     * @param stored - Public key in hex
     * @return PublicKey - a usable PublicKey object
     */

    public static PublicKey loadPublicKey(String stored) throws Exception {
        byte[] data = convertHexToBytes(stored);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
        KeyFactory fact = KeyFactory.getInstance("DSA","SUN");
        return fact.generatePublic(spec);
    }

    /**
     * Generate private key in bytes given a hex string. We can then store hex
     * strings as our private key instead of raw bytes.
     * 
     * @param stored - Private key in hex
     * @return PrivateKey - a usable PrivateKey object
     */

    public static PrivateKey loadPrivateKey(String stored) throws Exception {
        byte[] data = convertHexToBytes(stored);
        KeyFactory keyFactory = KeyFactory.getInstance("DSA","SUN");
        PrivateKey privKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(data));
        return privKey;
    }

    /**
     * Sign a message using a String version of the msg and key. Will be useful for
     * command-line tools!
     * 
     * @param msg - The message to sign
     * @param key - The secret key to sign it with (hex string)
     * @return String signature of the message
     */

    public static String signMessage(String msg, String key) throws Exception {
        PrivateKey sk = loadPrivateKey(key);
        byte[] sigBytes = sign(msg, sk);
        String toReturn = convertBytesToHexString(sigBytes);
        return toReturn;
    }

    /**
     * Verify a message using a String version of the msg, signature and key. Will
     * be useful for command-line tools!
     * 
     * @param msg - The message
     * @param sig - The signature (hex string)
     * @param key - The public key of the original signer (hex string)
     * @return Boolean - true if valid, false if not
     */

    /**
     * Verify a message using a String version of the msg, signature and key. Will
     * be useful for command-line tools!
     * 
     * @param msg - The message
     * @param sig - The signature (hex string)
     * @param key - The public key of the original signer (hex string)
     * @return Boolean - true if valid, false if not
     */

    public static boolean verifyMessage(String msg, String sig, String key) throws Exception {
        PublicKey pk = loadPublicKey(key);
        byte[] sigBytes = convertHexToBytes(sig);
        // byte[] sigBytes = sig.getBytes(StandardCharsets.US_ASCII);
        boolean toReturn = verify(msg, sigBytes, pk);
        return toReturn;
    }

    /**
     * Signs a message with a secret key sk and returns signature in byte array
     * format.
     * 
     * @param toSign - string to sign
     * @param sk     - secret key
     * @return byte[] signature of string signed with sk
     */

    public static byte[] sign(String toSign, PrivateKey sk) throws Exception {
        Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
        dsa.initSign(sk);

        byte[] bytes = toSign.getBytes();
        dsa.update(bytes, 0, bytes.length);
        byte[] sig = dsa.sign();
        return sig;
    }

    /**
     * Given a string toCheck, its signature, and a PUBLIC key pk, verifies that the
     * string was signed by the corresponding secret key sk to pk.
     * 
     * @param toCheck - string to check
     * @param sig     - signature in byte array form
     * @parak pk - public key
     * @return boolean - true if valid, false otherwise
     */

    public static boolean verify(String toCheck, byte[] sig, PublicKey pk) throws Exception {
        System.out.println(toCheck+"\n"+sig.toString()+"\n"+pk.toString());
        Signature sig2 = Signature.getInstance("SHA1withDSA", "SUN");
        byte[] bytes = toCheck.getBytes();
        sig2.initVerify(pk);
        sig2.update(bytes, 0, bytes.length);
        return sig2.verify(sig);
    }

}
