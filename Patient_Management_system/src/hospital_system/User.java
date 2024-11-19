package hospital_system;

import java.util.List;
import java.util.Scanner;

public abstract class User implements LoginSystem {
    protected String hospitalID;
    protected String password;
    protected String name;
    protected String contactNumber;
    protected String email;
    protected String gender;
    protected String age;
    protected boolean firstLogin;
    protected String role;  // Differentiates user types (Doctor, Patient, etc.)
    //String newPassword, confirmPassword;
    
    private static final String STAFF_LIST_FILE_PATH = FilePaths.getStaffListFilePath();

    // Constructor
    public User(String hospitalID, String name, String role, String gender, String age, String contactNumber, String password, String email) {
        this.hospitalID = hospitalID;
        this.name = name;
        this.email = email;
        this.role = role;
        this.contactNumber = contactNumber;
        this.gender = gender;
        this.age = age;
        this.password = password;  // Default password
        this.firstLogin = true;  // First login triggers password change
    }
    //Staff ID,Name,Role,Gender,Age,Phone,Password,Email
    public User()
    {
    	
    }

  
	// Getters and Setters for hospitalID, name, phone, email, role, and gender
    public String getHospitalID() {
        return hospitalID;
    }
    public String getAge() {
        return "";  // Default empty for users without age (like Patients)
    }
    public void setHospitalID(String hospitalID) {
        this.hospitalID = hospitalID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setPhone(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getPassword() {
    	return this.password;
    }
    
    public void setPassword(String password) {
    	this.password = PasswordHandling.hashPassword(password); // Hash the password before storing
    }
    
    /*public String toCSV() {
        return String.join(",", hospitalID, name, role, contactNumber, gender, age, password, email);
    }*/

    // Getter for firstLogin flag
    public boolean isFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    // Implement login method from LoginSystem interface
    @Override
    public boolean login(String inputPassword) {
    	Validation valid = new Validation();
        return valid.validatePassword(inputPassword,getPassword());
    }
    


    
    @Override
    public void changePassword() {
        Scanner sc = new Scanner(System.in);
        Validation valid = new Validation();
        String newPassword, confirmPassword = "";
        int attempts = 0;
        final int MAX_ATTEMPTS = 3;

        do {
            System.out.print("Enter new password (Password lenth of at least 8 characters which must include uppercase character, lowercase character, number and special character such as @#$%^&+=!): ");
            newPassword = sc.nextLine();

            if (!valid.isValidPassword(newPassword)) {
                attempts++;
                if (attempts >= MAX_ATTEMPTS) {
                    System.out.println("Too many failed attempts. Exiting password change.");
                    return;
                }
                continue;
            }

            System.out.print("Confirm new password: ");
            confirmPassword = sc.nextLine();

            if (!newPassword.equals(confirmPassword)) {
                System.out.println("Passwords do not match. Please try again.");
                attempts++;
            } else {
                break;
            }

            if (attempts >= MAX_ATTEMPTS) {
                System.out.println("Too many failed attempts. Exiting password change.");
                return;
            }
        } while (true);

        // Hash the password before storing
        String hashedPassword = PasswordHandling.hashPassword(newPassword);

        // Update hashed password in memory
        this.password = hashedPassword;
        this.firstLogin = false;

        System.out.println("Password updated successfully.");

        // Update hashed password in the CSV file
        boolean updateSuccessful = updatePasswordInCSV(this.hospitalID, hashedPassword);
        if (updateSuccessful) {
            System.out.println("Password successfully updated in Staff_List.csv.");
        } else {
            System.out.println("Failed to update password in Staff_List.csv.");
        }
    }


    // Helper method to update password in Staff_List.csv
    private boolean updatePasswordInCSV(String userId, String newPassword) {
        List<String[]> records = CSV_Handler.loadFromCSV(STAFF_LIST_FILE_PATH);
        boolean recordFound = false;

        for (String[] record : records) {
            if (record[0].equals(userId)) { // Assuming userId is the first column in CSV
                record[6] = newPassword; // Assuming password is in the seventh column in CSV
                recordFound = true;
                break;
            }
        }

        if (recordFound) {
        	//String hospitalID, String name, String role, String gender, String age, String contactNumber, String password, String email
            String header = "HospitalID,Name,Role,Gender,Age,ContactNumber,Password,Email";
            try {
                CSV_Handler.saveToCSV(STAFF_LIST_FILE_PATH, records, header);
            } catch (Exception e) {
                System.out.println("Error saving to CSV: " + e.getMessage());
                return false;
            }
            return true;
        } else {
            System.out.println("User ID not found in Staff_List.csv.");
            return false;
        }
    }
    
    
    // Abstract method to be implemented by subclasses (each user has its own menu)
    public abstract void showMenu();

    // Utility method to display basic user information
    public void displayUserInfo() {
        System.out.println("User Information:");
        System.out.println("Hospital ID: " + hospitalID);
        System.out.println("Name: " + name);
        System.out.println("Phone: " + contactNumber);
        System.out.println("Email: " + email);
        System.out.println("Gender: " + gender);
        System.out.println("Role: " + role);
    }
}
