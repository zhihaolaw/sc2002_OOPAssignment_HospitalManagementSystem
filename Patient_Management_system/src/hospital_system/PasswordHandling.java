package hospital_system;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHandling {
	public static String hashPassword(String password) {
	    if (password.startsWith("HASHED:")) {
	        // Password is already hashed
	        return password;
	    }

	    try {
	        MessageDigest md = MessageDigest.getInstance("SHA-256");
	        byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
	        
	        // Convert byte array to hex string
	        StringBuilder hexString = new StringBuilder();
	        for (byte b : hashedBytes) {
	            String hex = Integer.toHexString(0xff & b);
	            if (hex.length() == 1) {
	                hexString.append('0');
	            }
	            hexString.append(hex);
	        }

	        // Add the prefix to indicate it's hashed
	        return "HASHED:" + hexString.toString();
	    } catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException("Error: Hashing algorithm not found.", e);
	    }
	}

}

