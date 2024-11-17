package hospital_system;


import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HospitalManagementSystem_Main {

	private static final String STAFF_LIST_FILE_PATH = FilePaths.getStaffListFilePath();
	private static final String PATIENT_LIST_FILE_PATH = FilePaths.getPatientListFilePath();
    // List to store staff details
    private static List<User> staffList = new ArrayList<>();
    private static List<Patient> patientList = new ArrayList<>();


    public static void main(String[] args) {
        // Load data from CSV files for both staff and patients
        loadStaffFromFile(STAFF_LIST_FILE_PATH);
        loadPatientsFromFile(PATIENT_LIST_FILE_PATH);

        Scanner sc = new Scanner(System.in);

        while (true) {
            // Start the login process
            LoginSystem user = login(sc);  // Changed from User to LoginSystem
            if (user != null) {
                // Show the menu based on the user's role if login is successful
                showMenu(user);
            }
        }
    }

    
    /*public static void main(String[] args) {
        loadStaffFromFile(STAFF_LIST_FILE_PATH);
        loadPatientsFromFile(PATIENT_LIST_FILE_PATH);
        
        try (Scanner sc = new Scanner(System.in)) { // Use try-with-resources to automatically close Scanner
            while (true) {
                System.out.println("Welcome to our Hospital");
                System.out.println("-------------------------------------------");
                System.out.println("Select a number:");
                System.out.println("1.Staff");
                System.out.println("2.Patient");
                
                int mainChoice = sc.nextInt();
                
                switch(mainChoice)
                {
                	case 1 -> 
                	{
                		// Start the login process
                        User user = login(sc);
                        if (user != null) {
                        	// Show the menu based on the user's role if login is successful
                            showMenu(user);
                            }
                    }
                	
                	case 2 -> 
                	{
                		//printPatientList();
                		System.out.print("Enter Patient ID: ");
                	    String patientID = sc.next();
                	    Patient patient = findPatientByID(patientID);
                	    if (patient != null) { 
                	    	System.out.printf("Hi %s ",patient.name);
                	        patient.showMenu();
                	    }
                	    //	showMenu(patient);
                    }
                }
                
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }*/


    // Method to load staff data from a CSV file
    public static void loadStaffFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 8) {
                    String hospitalID = values[0];
                    String name = values[1];
                    String role = values[2];
                    String contactNumber = values[3];
                    String gender = values[4];
                    String age = values[5];
                    String password = values[6];
                    String email = values[7];
                    
                    
                    // Add staff to list (instantiate appropriate subclass based on role)
                    switch (role) {
                        case "Doctor":
                            staffList.add(new Doctor(hospitalID, name, role, contactNumber, gender, age, password, email));
                            break;
                        case "Pharmacist":
                            staffList.add(new Pharmacist(hospitalID, name, role, contactNumber, gender, age, password, email));
                            break;
                        case "Administrator":
                            staffList.add(new Administrator(hospitalID, name, role, contactNumber, gender, age, password, email));
                            break;
                        default:
                            //System.out.println("Unknown role: " + role);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public static void loadPatientsFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 12) {  // Ensure all fields are present
                    String patientID = values[0];
                    String name = values[1];
                    String dob = values[2];
                    String gender = values[3];
                    String role = values[4];
                    String age = values[5];
                    String bloodType = values[6];
                    String contactNumber = values[7];
                    String password = values[8];
                    String email = values[9];
                    String pastDiagnoses = values[10];
                    String pastTreatments = values[11];
                    
                    // Create and add patient to the list
                    Patient patient = new Patient(patientID, name, role, gender, age, contactNumber, password, email, dob, bloodType, pastDiagnoses, pastTreatments);
                    patientList.add(patient);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading patient file: " + e.getMessage());
        }
    }



 // Method to handle the login process, accepting Scanner as a parameter
    /*public static User login(Scanner sc) {
        int maxAttempts = 3;
        int attempts = 0;

        System.out.println("Welcome to our Hospital Management System");
        System.out.println("-------------------------------------------");

        while (attempts < maxAttempts) {
            System.out.print("Enter User ID: ");
            String userID = sc.nextLine();

            System.out.print("Enter Password: ");
            String password = sc.nextLine();
            System.out.println("-------------------------------------------");

            boolean userFound = false;

            // Search for the user in the staff list
            for (User user : staffList) {
                if (user.getHospitalID().equals(userID)) {
                    userFound = true;  // User ID exists
                    if (user.login(password)) {
                        System.out.println("Login successful! Welcome " + user.getName());
                        System.out.println("-------------------------------------------");
                        handleFirstLogin(user);
                        return user;
                    } else {
                        System.out.println("Invalid password. Please try again.");
                        //attempts++;
                        break;
                    }
                }
            }

            if (!userFound) {
                System.out.println("User ID not found. Please try again.");
            }

            attempts++;
        }

        System.out.println("Maximum login attempts exceeded. Please contact an administrator.\nExiting the program...");
        //System.exit(0);
        return null;
    }*/
    
 // Method to handle the login process, accepting Scanner as a parameter
    public static LoginSystem login(Scanner sc) {
        int maxAttempts = 3;
        int attempts = 0;

        System.out.println("Welcome to our Hospital Management System");
        System.out.println("-------------------------------------------");

        while (attempts < maxAttempts) {
            System.out.print("Enter User ID: ");
            String userID = sc.nextLine();

            System.out.print("Enter Password: ");
            String password = sc.nextLine();
            System.out.println("-------------------------------------------");

            // Attempt login in the staff list
            for (User user : staffList) {
                if (user.getHospitalID().equals(userID)) {  // Check if user ID matches
                    if (user.login(password)) {  // Check if password matches
                        System.out.println("Login successful! Welcome " + user.getName());
                        System.out.println("-------------------------------------------");
                        handleFirstLogin(user);
                        return user;  // Return the logged-in user
                    } else {
                        System.out.println("Invalid password. Please try again.");
                        break;
                    }
                }
            }

            // If not found in staff list, attempt login in the patient list
            for (Patient patient : patientList) {
                if (patient.getPatientID().equals(userID)) {  // Check if patient ID matches
                    if (patient.login(password)) {  // Check if password matches
                        System.out.println("Login successful! Welcome " + patient.getName());
                        System.out.println("-------------------------------------------");
                        handleFirstLogin(patient);
                        return patient;  // Return the logged-in patient
                    } else {
                        System.out.println("Invalid password. Please try again.");
                        break;
                    }
                }
            }

            // If no match was found
            System.out.println(""
            		+ "User ID not found. Please try again.");
            attempts++;
        }

        System.out.println("Maximum login attempts exceeded. Please contact an administrator.\nExiting the program...");
        return null;
    }




 // Display the menu based on the user's role
    public static void showMenu(LoginSystem user) {
        try {
            if (user instanceof User) {  // Staff member
                User staffUser = (User) user;
                if (staffUser instanceof Doctor) {
                    ((Doctor) staffUser).showMenu();  // Show Doctor menu
                } else if (staffUser instanceof Pharmacist) {
                    ((Pharmacist) staffUser).showMenu();  // Show Pharmacist menu
                } else if (staffUser instanceof Administrator) {
                    ((Administrator) staffUser).showMenu();  // Show Administrator menu
                }else if (staffUser instanceof Patient) {
                	((Patient) staffUser).showMenu();
                }
            /*} else if (user instanceof Patient) {  // Patient
                Patient patientUser = (User) user;
                patientUser.showMenu();  // Show Patient menu
            }*/
            }
        } catch (Exception e) {
            System.err.println("An error occurred in showMenu: " + e.getMessage());
            e.printStackTrace();  // Print the stack trace for debugging
        }
    }



 // Method to save staff data to a CSV file by updated the current Staff_List.csv
    /*public static void saveStaffToFile(String filePath) {
        List<String> updatedStaffData = new ArrayList<>();

        // Re-create CSV file contents from updated staffList
        for (User staff : staffList) {
            StringBuilder line = new StringBuilder();
            line.append(staff.getHospitalID()).append(",")
                .append(staff.getName()).append(",")
                .append(staff.getRole()).append(",")
                .append(staff.getContactNumber()).append(",")
                .append(staff.getGender()).append(",")
                .append(staff.getAge()).append(",")
                .append(staff.getPassword()).append(",")
                .append(staff.getEmail());
            
            updatedStaffData.add(line.toString());
        }

        // Write the updated data to the CSV file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String staffLine : updatedStaffData) {
                bw.write(staffLine);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    
    public static void saveStaffToFile() {
        List<String[]> staffData = new ArrayList<>();
        for (User staff : staffList) {
            staffData.add(new String[]{
                staff.getHospitalID(),
                staff.getName(),
                staff.getRole(),
                staff.getContactNumber(),
                staff.getGender(),
                staff.getAge(),
                staff.getPassword(),
                staff.getEmail()
            });
        }
        CSV_Handler.saveToCSV(STAFF_LIST_FILE_PATH, staffData, "HospitalID,Name,Role,ContactNumber,Gender,Age,Password,Email");
    }




// Handle first login to prompt the user to change their password
    private static void handleFirstLogin(User user) {
        if (user.isFirstLogin()) {
            System.out.println("You are logging in for the first time. You must change your password.");
            user.changePassword();
            user.setFirstLogin(false);  // Update the state
            saveStaffToFile(); // Persist the change
        }
    }



private static String convertDateFormat(String date) {
        String[] parts = date.split("/");
        if (parts.length == 3) {
            return parts[2] + "-" + parts[1] + "-" + parts[0];
        }
        return date;  // If already formatted correctly
    }

public static Patient findPatientByID(String pid) {
    for (Patient patient : patientList) {
        if (patient.getPatientID().equalsIgnoreCase(pid)) {
            return patient;
        }
    }
    System.out.println("Patient not found.");
    return null;
}

public static void printPatientList() {
    if (patientList.isEmpty()) {
        System.out.println("No patients found in the list.");
    } else {
        System.out.println("\n--- Patient List ---");
        for (Patient patient : patientList) {
            System.out.println("Patient ID      : " + patient.getHospitalID());
            System.out.println("Name            : " + patient.getName());
            System.out.println("Gender          : " + patient.getGender());
            System.out.println("Age             : " + patient.getAge());
            System.out.println("Contact Number  : " + patient.getContactNumber());
            System.out.println("Email           : " + patient.getEmail());
            System.out.println("Date of Birth   : " + patient.getDob());
            System.out.println("Blood Type      : " + patient.getBloodType());
            System.out.println("Past Diagnoses  : " + patient.getDiagnoses());
            System.out.println("Past Treatments : " + patient.getTreatments());
            System.out.println("--------------------------");
        }
    }
}

}




