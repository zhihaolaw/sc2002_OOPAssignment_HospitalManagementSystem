package hospital_system;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Validation {
	private static final Scanner sc = new Scanner(System.in);
	
	public Validation()
	{
		
	}
	
	// Method to validate the role
	public String getValidRole() {
        String newRole;
        while (true) {
            System.out.print("Enter Staff Role (Doctor/Pharmacist/Administrator): ");
            newRole = sc.nextLine().trim();
            if (newRole.equalsIgnoreCase("Doctor") || newRole.equalsIgnoreCase("Pharmacist") || newRole.equalsIgnoreCase("Administrator")) {
                break;
            } else {
                System.out.println("Invalid role. Please enter Doctor, Pharmacist, or Administrator.");
            }
        }
        return newRole;
    }

    // Method to validate the name
    public String getValidName() {
        String newName;
        while (true) {
            System.out.print("Enter Staff Name: ");
            newName = sc.nextLine().trim();
            if (!newName.isEmpty()) {
                break;
            } else {
                System.out.println("Name cannot be empty.");
            }
        }
        return newName;
    }

    // Method to validate the gender
    public String getValidGender() {
        String newGender;
        while (true) {
            System.out.print("Enter Gender (Male/Female/Other): ");
            newGender = sc.nextLine().trim();
            if (newGender.equalsIgnoreCase("Male") || newGender.equalsIgnoreCase("Female") || newGender.equalsIgnoreCase("Other")) {
                break;
            } else {
                System.out.println("Invalid gender. Please enter Male, Female, or Other.");
            }
        }
        return newGender;
    }

    // Method to validate the age
    public String getValidAge() {
        String newAge;
        while (true) {
            System.out.print("Enter Age: ");
            newAge = sc.nextLine().trim();
            if (newAge.matches("\\d+")) { // Check if the age is a valid number
                break;
            } else {
                System.out.println("Invalid age. Please enter a valid number.");
            }
        }
        return newAge;
    }

    // Method to validate the phone number
    public  String getValidPhoneNumber() {
        String newContactNumber;
        while (true) {
            System.out.print("Enter Phone: ");
            newContactNumber = sc.nextLine().trim();
            if (newContactNumber.matches("^\\d{8}$")) { // Simple check for 10-digit phone number
                break;
            } else {
                System.out.println("Invalid phone number. Please enter a valid 8-digit phone number.");
            }
        }
        return newContactNumber;
    }

    // Method to validate the email address
    public String getValidEmail() {
        String newEmail;
        while (true) {
            System.out.print("Enter Email: ");
            newEmail = sc.nextLine().trim();
            if (Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", newEmail)) { // Basic regex to validate email format
                break;
            } else {
                System.out.println("Invalid email format. Please enter a valid email.");
            }
        }
        return newEmail;
    }
    
    public boolean validateDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    
    public boolean validateTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setLenient(false);
        try {
            sdf.parse(time);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
    
    
    public boolean validatePassword(String inputPassword,String classPassword) {
        // Hash the input password
        String hashedInputPassword = PasswordHandling.hashPassword(inputPassword);

        // Remove "HASHED:" prefix before comparison
        String cleanStoredPassword = classPassword.replaceFirst("^HASHED:", "");

        return MessageDigest.isEqual(
            hashedInputPassword.replaceFirst("^HASHED:", "").getBytes(StandardCharsets.UTF_8),
            cleanStoredPassword.getBytes(StandardCharsets.UTF_8)
        );
    }

    public boolean isValidPassword(String password) {
        // Check minimum length
        if (password.length() < 8) {
            System.out.println("Password must be at least 8 characters long.");
            return false;
        }

        // Check for at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            System.out.println("Password must contain at least one uppercase letter.");
            return false;
        }

        // Check for at least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            System.out.println("Password must contain at least one lowercase letter.");
            return false;
        }

        // Check for at least one digit
        if (!password.matches(".*\\d.*")) {
            System.out.println("Password must contain at least one number.");
            return false;
        }

        // Check for at least one special character
        if (!password.matches(".*[@#$%^&+=!].*")) {
            System.out.println("Password must contain at least one special character (@#$%^&+=!).");
            return false;
        }

        // All checks passed
        return true;
    }
}
