package utils;

import java.math.BigInteger;

public class HexString {
	
	  public static String toHexString(byte[] hash)
	    {
	        // Convert byte array into signum representation 
	        BigInteger number = new BigInteger(1, hash); 
	  
	        // Convert message digest into hex value 
	        StringBuilder hexString = new StringBuilder(number.toString(16)); 
	  
	        // Pad with leading zeros
	        while (hexString.length() < 32) 
	        { 
	            hexString.insert(0, '0'); 
	        } 
	  
	        return hexString.toString(); 
	    }

}
