package hospital_system;

import java.io.*;
import java.util.*;

public class Administrator extends User implements CSV_PathHandler {

	//HospitalManagementSystem_Main staffHandler;
	private String age;
	
	private static final String STAFF_LIST_FILE_PATH = "C:\\Users\\zhiha\\Downloads\\Patient_Management_system_updated\\Patient_Management_system\\data\\Staff_List.csv";
    private static final String APPOINTMENTS_FILE_PATH = "C:\\Users\\zhiha\\Downloads\\Patient_Management_system_updated\\Patient_Management_system\\data\\appointments.csv";
    private static final String INVENTORY_FILE_PATH = "C:C:\\Users\\zhiha\\Downloads\\Patient_Management_system_updated\\Patient_Management_system\\data\\Medicine_List.csv";
    private static final String REPLENISHMENT_FILE_PATH = "C:\\Users\\zhiha\\Downloads\\Patient_Management_system_updated\\Patient_Management_system\\data\\replenishment_requests.csv";
    private static final String FEEDBACK_FILE_PATH = "C:\\Users\\zhiha\\Downloads\\Patient_Management_system_updated\\Patient_Management_system\\data\\Feedback.csv";

    // Store staff, appointments, and inventory data
    private static final Map<String, User> staffList = new HashMap<>();
    private static final List<String[]> appointments = new ArrayList<>();
    private static final Map<String, String[]> inventory = new HashMap<>();
    private static final List<String[]> replenishmentRequests = new ArrayList<>();
    private static final List<String[]> feedbackList = new ArrayList<>();

    // Constructor for Administrator class
    public Administrator(String hospitalID, String name, String role, String gender, String age, String contactNumber, String password, String email) {
        super(hospitalID, name, "Administrator", gender, age, contactNumber, password, email);
        this.age = age;
    }

    @Override
    public String getAge() {
        return this.age;
    }
    
    @Override
    public String toCSV() {
        return String.join(",", hospitalID, name, "Administrator", gender, age, contactNumber, password, email);
    }

    @Override
    public void fromCSV(String csvLine) {
        String[] values = csvLine.split(",");
        this.hospitalID = values[0];
        this.name = values[1];
        this.role = values[2];
        this.gender = values[3];
        this.age = values[4];
        this.contactNumber = values[5];
        this.password = values[6];
        this.email = values[7];
    }

    private static void loadStaff() {
        staffList.clear();
        List<String[]> data = CSV_Handler.loadFromCSV(STAFF_LIST_FILE_PATH);
        
        for (String[] row : data) {
            if (row.length < 8) { // Ensure there are enough columns
                System.out.println("Skipping incomplete row: " + Arrays.toString(row));
                continue; // Skip this row
            }
            
            String role = row[2].trim();
            User user;
            switch (role) {
                case "Doctor" -> user = new Doctor(row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7]);
                case "Pharmacist" -> user = new Pharmacist(row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7]);
                case "Administrator" -> user = new Administrator(row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7]);
                default -> {
                    System.out.println("Unknown role: " + role + " in row: " + Arrays.toString(row));
                    continue;
                }
            }
            
            staffList.put(row[0], user);
        }
    }



    // Method to save hospital staff to file (Staff_List.csv)
    private static void saveStaff() {
        List<String[]> data = new ArrayList<>();
        for (User staff : staffList.values()) {
            data.add(new String[]{staff.getHospitalID(), staff.getName(), staff.getRole(), staff.getGender(), staff.getAge(), staff.getContactNumber(), staff.getPassword(), staff.getEmail()});
        }
        String header = "Staff ID,Name,Role,Gender,Age,Phone,Password,Email";
        CSV_Handler.saveToCSV(STAFF_LIST_FILE_PATH, data, header);
    }


    // Method to manage hospital staff (e.g., add, update, remove staff)
    public void manageStaff() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- Manage Hospital Staff ---");
        System.out.println("1. Add Staff");
        System.out.println("2. Update Staff");
        System.out.println("3. Remove Staff");
        System.out.println("4. View All Staff");
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();
        sc.nextLine();  // Consume newline

        loadStaff();

        switch (choice) {
            case 1 -> {
                System.out.println("Adding new staff...");
                System.out.print("Enter Staff ID: ");
                String newID = sc.nextLine();
                System.out.print("Enter Staff Name: ");
                String newName = sc.nextLine();
                System.out.print("Enter Staff Role (Doctor/Pharmacist/Administrator): ");
                String newRole = sc.nextLine();
                System.out.print("Enter Gender: ");
                String newGender = sc.nextLine();
                System.out.print("Enter Age: ");
                String newAge = sc.nextLine();
                System.out.print("Enter Phone: ");
                String newContactNumber = sc.nextLine();
                System.out.print("Enter Email: ");
                String newEmail = sc.nextLine();

                if (newRole.equalsIgnoreCase("Doctor")) {
                    staffList.put(newID, new Doctor(newID, newName, "Doctor", newGender, newAge, newContactNumber, "password", newEmail));
                } else if (newRole.equalsIgnoreCase("Pharmacist")) {
                    staffList.put(newID, new Pharmacist(newID, newName, "Pharmacist", newGender, newAge, newContactNumber, "password", newEmail));
                } else if (newRole.equalsIgnoreCase("Administrator")) {
                    staffList.put(newID, new Administrator(newID, newName, "Administrator", newGender, newAge, newContactNumber, "password", newEmail));
                }
                saveStaff();
                System.out.println("New staff added successfully.");
            }
            case 2 -> {
                System.out.println("Updating staff...");
                System.out.print("Enter Staff ID to update: ");
                String updateID = sc.nextLine();
                if (staffList.containsKey(updateID)) {
                    User staff = staffList.get(updateID);
                    System.out.print("Enter new name (Leave blank to keep current): ");
                    String newNameUpdate = sc.nextLine();
                    System.out.print("Enter new phone (Leave blank to keep current): ");
                    String newPhoneUpdate = sc.nextLine();
                    System.out.print("Enter new email (Leave blank to keep current): ");
                    String newEmailUpdate = sc.nextLine();

                    if (!newNameUpdate.isBlank()) staff.setName(newNameUpdate);
                    if (!newPhoneUpdate.isBlank()) staff.setPhone(newPhoneUpdate);
                    if (!newEmailUpdate.isBlank()) staff.setEmail(newEmailUpdate);

                    saveStaff();
                    System.out.println("Staff updated successfully.");
                } else {
                    System.out.println("Staff not found.");
                }
            }
            case 3 -> {
                System.out.println("Removing staff...");
                ViewStaff();
                System.out.print("Enter Staff ID to remove: ");
                String removeID = sc.nextLine();

                if (staffList.containsKey(removeID)) {
                	staffList.remove(removeID);
                    saveStaff();
                    System.out.println("Staff removed successfully.");
                    ViewStaff();
                } else {
                    System.out.println("Staff not found.");
                }
            }
            case 4 -> {
                System.out.println("Viewing all staff...");
                ViewStaff();
//                for (User staff : staffList.values()) {
//                    // Correct order for output
//                    System.out.printf("StaffID: %s | Name: %s | Role: %s | Gender: %s | Age: %s | Phone: %s | Email: %s\n",
//                            staff.getHospitalID(),
//                            staff.getName(),
//                            staff.getRole(),
//                            staff.getGender(),   // Gender in correct place
//                            staff.getAge(),      // Age in correct place
//                            staff.getContactNumber(),    // Phone in correct place
//                            staff.getEmail());
//                	
//                }
            }
            default -> System.out.println("Invalid choice.");
        }
    }
    
    private void ViewStaff()
    {
    	for(User s : staffList.values())
    	{
    		System.out.printf("StaffID: %s | Name: %s | Role: %s | Gender: %s | Age: %s | Phone: %s | Email: %s\n",
                    s.getHospitalID(),
                    s.getName(),
                    s.getRole(),
                    s.getGender(),   // Gender in correct place
                    s.getAge(),      // Age in correct place
                    s.getContactNumber(),    // Phone in correct place
                    s.getEmail());
    	}
    }
    
    // Method to load appointments from file (appointments.csv)
    private static void loadAppointments() {
        appointments.clear();
        appointments.addAll(CSV_Handler.loadFromCSV(APPOINTMENTS_FILE_PATH));
    }

    // Method to view details of all appointments
    public void viewAppointments() {
        System.out.println("Viewing all appointments...");
        loadAppointments();
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
        } else {
            for (String[] appointment : appointments) {
                System.out.printf("Appointment ID: %s | Patient ID: %s | Doctor ID: %s | Date: %s | Time: %s | Status: %s\n",
                        appointment[0], appointment[1], appointment[2], appointment[4], appointment[5], appointment[6]);
            }
        }
    }

    // Method to load inventory from file (Medicine_List.csv)
    private static void loadInventory() {
        inventory.clear();
        List<String[]> data = CSV_Handler.loadFromCSV(INVENTORY_FILE_PATH);
        for (String[] item : data) {
            inventory.put(item[0], item);
        }
    }

    // Method to save inventory to file
    /*private static void saveInventory() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("C:\\Users\\zhiha\\OneDrive - Nanyang Technological University\\Desktop\\Patient_Management_system\\Patient_Management_system\\data\\Medicine_List.csv"))) {
            writer.println("Med ID,Medicine Name,Initial Stock,Low Stock Level Alert");
            for (String[] medication : inventory.values()) {
                writer.printf("%s,%s,%s,%s\n", medication[0], medication[1], medication[2], medication[3]);
            }
        } catch (IOException e) {
            System.out.println("Error writing to Medicine List CSV file.");
        }
    }*/
    
    private static void saveInventory() {
        List<String[]> data = new ArrayList<>(inventory.values());
        String header = "Med ID,Medicine Name,Initial Stock,Low Stock Level Alert";
        CSV_Handler.saveToCSV(INVENTORY_FILE_PATH, data, header);
    }

    // Method to view and manage the medication inventory
    public void manageInventory() {
        Scanner sc = new Scanner(System.in);
        loadInventory();
        System.out.println("\n--- Manage Inventory ---");
        System.out.println("1. View Inventory");
        System.out.println("2. Update Inventory");
        System.out.println("3. Update Stock alert");
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();
        sc.nextLine(); // Consume newline
        
       // Prescription prescription = new Prescription();
       //Inventory inventory = new Inventory();

        switch (choice) {
            case 1 -> {
                System.out.println("Viewing inventory...");
                for(String[] data: inventory.values())
                {
                    String medId = data[0];
                    String medicineName = data[1];
                    int stockLevel = Integer.parseInt(data[2]); // Convert stock level to integer
                    int lowStockLevel = Integer.parseInt(data[3]); // Convert low stock alert level to integer
                    System.out.printf("Medication ID: %s | Name: %s | Stock Level: %d | Low Stock Alert Level: %d\n",
                    medId, medicineName, stockLevel, lowStockLevel);
                    
                    if(stockLevel < lowStockLevel)
                    {
                    	System.out.printf("** ALERT: Stock level for %s is below the threshold. Replenishment needed! **\n",
                                medicineName);
                    }
                }

                
//                for (String[] medication : inventory.values()) {
//                    System.out.printf("ID: %s | Name: %s | Stock Level: %s | Low Stock Alert Level: %s\n", medication[0], medication[1], medication[2], medication[3]);
//                }
            	
            	//inventory.viewInventory();
            }
            case 2 -> {
                System.out.print("Enter Medication ID to update: ");
                String updateID = sc.nextLine();
                if (inventory.containsKey(updateID)) {
                    String[] medication = inventory.get(updateID);
                    System.out.printf("Current Stock Level: %s\n", medication[2]);
                    System.out.print("Enter new stock level: ");
                    medication[2] = sc.nextLine();
                    saveInventory();
                    System.out.println("Medication stock updated successfully.");
                } else {
                    System.out.println("Medication not found.");
                }
            }
            
            case 3 -> {
             for(String[] d : inventory.values())
             {
            	 System.out.printf("ID: %s | Name: %s | Stock Level: %s | Low Stock Alert Level: %s\n", d[0], d[1], d[2], d[3]);
             }
    		 System.out.print("Enter Medication ID to update low stock alert level: ");
    		 String medId = sc.nextLine();
    		 if(inventory.containsKey(medId))
    		 {
    			 String[] lowStockAlert = inventory.get(medId);
    			 System.out.printf("Current Low Stock Level: %s\n", lowStockAlert[3]);
    			 System.out.print("Enter new Low Stock level: ");
                 lowStockAlert[3] = sc.nextLine();
                 saveInventory();
                 System.out.printf("Medication %s Low Stock with %s updated successfully.",lowStockAlert[1],lowStockAlert[3]);
    		 }
    		 else {
                 System.out.println("Medication not found.");
             }
//                System.out.print("Enter Medication ID to update: ");
//                String updateID = sc.nextLine();
//                if (inventory.containsKey(updateID)) {
//                    String[] medication = inventory.get(updateID);
//                    System.out.printf("Current Stock Level: %s\n", medication[2]);
//                    System.out.print("Enter new stock level: ");
//                    medication[2] = sc.nextLine();
//                    saveInventory();
//                    System.out.println("Medication stock updated successfully.");
//                } else {
//                    System.out.println("Medication not found.");
//                }
            }
            default -> System.out.println("Invalid choice.");
        }
    }

    // Method to load replenishment requests from file (replenishment_requests.csv)
    /*private static void loadReplenishmentRequests() {
        replenishmentRequests.clear();
        try (Scanner sc = new Scanner(new File("C:\\Users\\zhiha\\OneDrive - Nanyang Technological University\\Desktop\\Patient_Management_system\\Patient_Management_system\\data\\replenishment_requests.csv"))) {
            sc.nextLine();  // Skip the header line
            while (sc.hasNextLine()) {
                replenishmentRequests.add(sc.nextLine().split(","));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Replenishment requests CSV file not found!");
        }
    }*/
    
    private static void loadReplenishmentRequests() {
        replenishmentRequests.clear();
        replenishmentRequests.addAll(CSV_Handler.loadFromCSV(REPLENISHMENT_FILE_PATH));
    }

    // Method to save replenishment requests to file (replenishment_requests.csv)
    /*private static void saveReplenishmentRequests() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("C:\\Users\\zhiha\\OneDrive - Nanyang Technological University\\Desktop\\Patient_Management_system\\Patient_Management_system\\data\\replenishment_requests.csv"))) {
            writer.println("Request ID,Medication ID,Quantity,Status");
            for (String[] request : replenishmentRequests) {
                writer.printf("%s,%s,%s,%s\n", request[0], request[1], request[2], request[3]);
            }
        } catch (IOException e) {
            System.out.println("Error writing to replenishment requests CSV file.");
        }
    }*/
    
    private static void saveReplenishmentRequests() {
        String header = "Request ID,Medication ID,Quantity,Status";
        CSV_Handler.saveToCSV(REPLENISHMENT_FILE_PATH, replenishmentRequests, header);
    }

    // Method to approve replenishment requests
    public void approveReplenishmentRequests() {
        Scanner sc = new Scanner(System.in);
        loadReplenishmentRequests();
        loadInventory();

        System.out.println("\n--- Replenishment Requests ---");
        if (replenishmentRequests.isEmpty()) {
            System.out.println("No replenishment requests found.");
            return;
        }

        // Display all replenishment requests with numbers for selection
        for (int i = 0; i < replenishmentRequests.size(); i++) {
            String[] request = replenishmentRequests.get(i);
            System.out.printf("%d. Request ID: %s | Medication ID: %s | Quantity: %s | Status: %s\n",
                    i + 1, request[0], request[1], request[2], request[3]);
        }

        System.out.print("Select the request to process by entering the corresponding number: ");
        int requestIndex = -1;

        // Ensure valid integer input
        while (true) {
            try {
                requestIndex = Integer.parseInt(sc.nextLine()) - 1;
                if (requestIndex >= 0 && requestIndex < replenishmentRequests.size()) {
                    break;
                } else {
                    System.out.println("Invalid selection. Please enter a valid number corresponding to a request.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        // Get the selected request
        String[] selectedRequest = replenishmentRequests.get(requestIndex);
        System.out.println("Request Details: " + Arrays.toString(selectedRequest));

        // Approve or Deny the request using integer input
        System.out.println("1. Approve");
        System.out.println("2. Deny");
        System.out.print("Enter your choice: ");
        int decision = -1;

        // Ensure valid integer input for approval or denial
        while (true) {
            try {
                decision = Integer.parseInt(sc.nextLine());
                if (decision == 1 || decision == 2) {
                    break;
                } else {
                    System.out.println("Invalid selection. Please enter 1 for Approve or 2 for Deny.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter 1 for Approve or 2 for Deny.");
            }
        }

        // Process the decision
        if (decision == 1) {
            selectedRequest[3] = "Approved";
            String medicationID = selectedRequest[1];
            if (inventory.containsKey(medicationID)) {
                String[] medication = inventory.get(medicationID);
                // Update the stock level of the medication
                medication[2] = String.valueOf(Integer.parseInt(medication[2]) + Integer.parseInt(selectedRequest[2]));
                saveInventory();
                System.out.println("Replenishment approved and inventory updated.");
            } else {
                System.out.println("Medication not found in the inventory.");
            }
        } else if (decision == 2) {
            selectedRequest[3] = "Denied";
            System.out.println("Replenishment request denied.");
        }

        // Save the updated requests back to the file
        saveReplenishmentRequests();
    }
    
    // Method to load appointments from file (appointments.csv)
    private static void loadFeedback() {
    	feedbackList.clear();
    	feedbackList.addAll(CSV_Handler.loadFromCSV(FEEDBACK_FILE_PATH));
    }

    // Method to view details of all appointments
    public void viewFeedback() {
        System.out.println("Viewing all Feedback...");
        loadFeedback();
        if (feedbackList.isEmpty()) {
            System.out.println("No Feedback found.");
        } else {
            for (String[] f : feedbackList) {
                System.out.printf("Patient Name: %s | Feedback: %s | Time: %s\n",
                        f[0], f[1], f[2]);
            }
        }
    }


    // Override the abstract method from User class to show administrator-specific menu
    @Override
    public void showMenu() {
        Scanner sc = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\n--- Administrator Menu ---");
            System.out.println("1. View and Manage Hospital Staff");
            System.out.println("2. View Appointments Details");
            System.out.println("3. View and Manage Medication Inventory");
            System.out.println("4. Approve Replenishment Requests");
            System.out.println("5. View Patient Feedback");
            System.out.println("6. Logout");

            while (true) {
                System.out.print("Enter your choice: ");
                try {
                    choice = sc.nextInt();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter an integer.");
                    sc.next();
                }
            }

            switch (choice) {
                case 1 -> manageStaff();
                case 2 -> viewAppointments();
                case 3 -> manageInventory();
                case 4 -> approveReplenishmentRequests();
                case 5 -> viewFeedback();
                case 6 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 6);
    }
}
