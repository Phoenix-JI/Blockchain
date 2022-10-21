package Encryption_Algorithm;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {
	
	
    public static String getHashForStr(String data) {
        try {
            MessageDigest algo = MessageDigest.getInstance("SHA-256");
            algo.update(data.getBytes());
            byte[] hashOfData = algo.digest();
            return byteToHex(hashOfData);

        }catch (NoSuchAlgorithmException ex) {
            return "";
        }
    }
    
    private static String byteToHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
            return String.format("%0" + paddingLength + "d", 0) + hex;
        else
            return hex;
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String answer = getHashForStr("Oh my go!");
		
		System.out.println(answer);

	}

}
