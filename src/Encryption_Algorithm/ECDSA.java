package Encryption_Algorithm;

import javax.xml.bind.DatatypeConverter;
import java.security.*;
import java.util.Base64;

public class ECDSA {
	
	public static KeyPair getKeyPair() throws Exception {
        //Creating KeyPair generator object
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("EC");

        //Initializing the KeyPairGenerator
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        keyPairGen.initialize(256, random);

        return keyPairGen.generateKeyPair();
    }

    //generate signature
    public static String signECDSA(PrivateKey privateKey, String message) {
        String result = "";
        try {
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initSign(privateKey);
            signature.update(message.getBytes());

            byte[] sign = signature.sign();

            //System.out.println("ECDSA signature: " + Hex.encodeHexString(sign));
            //return Hex.encodeHexString(sign);
            //System.out.println("ECDSA signature: " + DatatypeConverter.printHexBinary(sign));
            return DatatypeConverter.printHexBinary(sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //verify signature
    public static boolean verifyECDSA(PublicKey publicKey, String signed, String message) {
        try {
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initVerify(publicKey);
            signature.update(message.getBytes());

            //byte[] hex = Hex.decodeHex(signed);
            byte[] hex = DatatypeConverter.parseHexBinary(signed);
            boolean bool = signature.verify(hex);

            System.out.println("verify：" + bool);
            return bool;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
	public static String getStringFromKey(Key key) {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}
	

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
        String data = "This is a message!";
//        //generate KeyPair
		KeyPair keyPair = getKeyPair();
        //Getting the private key from the key pair
        PrivateKey privateKey = keyPair.getPrivate();
//        //Getting the public key from the key pair

        PublicKey publicKey = keyPair.getPublic();
//        //System.out.println("publicKey: " + Hex.encodeHexString(publicKey.getEncoded()));
//        //System.out.println("privateKey: " + Hex.encodeHexString(privateKey.getEncoded()));
        System.out.println("publicKey: " + DatatypeConverter.printHexBinary(publicKey.getEncoded()));
       System.out.println("privateKey: " + DatatypeConverter.printHexBinary(privateKey.getEncoded()));
//
//        //generate signature
       String sign = signECDSA(privateKey, data);
        System.out.println("signature: " + sign);
//
//        //verify signature
        verifyECDSA(publicKey, sign, data);

	}

}
