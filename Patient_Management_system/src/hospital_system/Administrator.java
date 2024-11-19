package hospital_system;

import java.io.*;
import java.util.*;

public class Administrator extends User implements CSV_PathHandler {

	//HospitalManagementSystem_Main staffHandler;
	private String age;
	
	private static final String STAFF_LIST_FILE_PATH = FilePaths.getStaffListFilePath();
    private static final String REPLENISHMENT_FILE_PATH = FilePaths.getReplenismentFilePath();
    
    // Store staff, appointments, and inventory data
    private static final Map<String, User> staffList = new HashMap<>();
    private static final Map<String, String[]> inventory = new HashMap<>();
    private static final List<String[]> replenishmentRequests = new ArrayList<>();
   

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
        Validation valid = new Validation();
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
                String newRole = valid.getValidRole();
                String newName = valid.getValidName();
                String newGender = valid.getValidGender();
                String newAge = valid.getValidAge();
                String newContactNumber = valid.getValidPhoneNumber();
                String newEmail = valid.getValidEmail();
           
                
                String newID = getNextID(STAFF_LIST_FILE_PATH,String.valueOf(newRole.charAt(0)));
                
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
                ViewStaff();
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
                    loadStaff();
                    ViewStaff();
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
                	deleteStaffByID(removeID);
                    System.out.println("Staff removed successfully.");
                    loadStaff();
                    ViewStaff();
                } else {
                    System.out.println("Staff not found.");
                }
            }
            case 4 -> {
                System.out.println("Viewing all staff...");
                ViewStaff();
            }
            default -> System.out.println("Invalid choice.");
        }
    }
    
    // Method to get the next available Appointment ID by reading the CSV file
    private String getNextID(String file_path, String startsWith) {
        int maxId = 0;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file_path))) {
            String line;
            reader.readLine(); // Skip header line
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 0) {
                    // Extract the numeric part of the Appointment ID
                    if(startsWith.equals("A")) {
                    	int id = Integer.parseInt(data[0].substring(1));
                    	if (id > maxId) {
                            maxId = id;
                        }
                    }else if(startsWith.equals("P")) {
                    	int id = Integer.parseInt(data[0].substring(1));
                    	if (id > maxId) {
                            maxId = id;
                        }
                    }
                    else if(startsWith.equals("D")) {
                    	
                    	int id = Integer.parseInt(data[0].substring(1));
                    	if (id > maxId) {
                            maxId = id;
                        }
                    }
                    
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading IDs: " + e.getMessage());
        }
        
        
        

        // Increment the max ID and format it with a prefix
        return startsWith + String.format("%03d", maxId + 1);
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
    

    // Method to view and manage the medication inventory
    public void manageInventory() {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("\n--- Manage Inventory ---");
        System.out.println("1. View Inventory");
        System.out.println("2. Update Inventory");
        System.out.println("3. Update Stock alert");
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();
        sc.nextLine(); // Consume newline
        
       // Prescription prescription = new Prescription();
        Inventory inventory1 = new Inventory();
        inventory1.loadInventory();
        
        switch (choice) {
            case 1 -> inventory1.viewInventory();
            
            case 2 -> inventory1.updateInventory();
               
            case 3 -> inventory1.updateLowStockAlert();
             
            default -> System.out.println("Invalid choice.");
        }
    }

    
    
    private static void loadReplenishmentRequests() {
        replenishmentRequests.clear();
        replenishmentRequests.addAll(CSV_Handler.loadFromCSV(REPLENISHMENT_FILE_PATH));
    }
    
    public void deleteStaffByID(String staffID) {
        List<String[]> records = new ArrayList<>();
        boolean isDeleted = false;

        // Read all records from the file
        try (BufferedReader br = new BufferedReader(new FileReader(STAFF_LIST_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns[0].equals(staffID)) { // Assuming Staff ID is in the first column
                    // Replace the row with empty strings
                    Arrays.fill(columns, "");
                    isDeleted = true;
                }
                records.add(columns);
            }
        } catch (IOException e) {
            System.out.println("Error reading staff records: " + e.getMessage());
            return;
        }

        // Write back the updated records to the file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(STAFF_LIST_FILE_PATH))) {
            for (String[] record : records) {
                bw.write(String.join(",", record));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to staff records: " + e.getMessage());
        }

        if (isDeleted) {
            System.out.println("Staff with ID " + staffID + " has been cleared from the file.");
        } else {
            System.out.println("Staff with ID " + staffID + " not found.");
        }
    }

    
    
    private static void saveReplenishmentRequests() {
        String header = "Request ID,Medication ID,Quantity,Status";
        CSV_Handler.saveToCSV(REPLENISHMENT_FILE_PATH, replenishmentRequests, header);
    }

    // Method to approve replenishment requests
    public void approveReplenishmentRequests() {
    	int q = 0;
        Scanner sc = new Scanner(System.in);
        loadReplenishmentRequests();
        Inventory inventory1 = new Inventory();
        boolean pendingRequest = false;
        
        inventory1.loadInventory();

        System.out.println("\n--- Replenishment Requests ---");
        if (replenishmentRequests.isEmpty()) {
            System.out.println("No replenishment requests found.");
            return;
        }

        // Display all replenishment requests with numbers for selection
        for (int i = 0; i < replenishmentRequests.size(); i++) {
            String[] request = replenishmentRequests.get(i);
            if(request[3].equals("Pending"))
            {
            	System.out.printf("%d. Request ID: %s | Medication ID: %s | Quantity: %s | Status: %s\n",
                        i + 1, request[0], request[1], request[2], request[3]);
            	pendingRequest = true;
            	
            }
            
        }
        if(!pendingRequest)
        {
        	System.out.print("No pending request");
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
            int requestQuantity = Integer.valueOf(selectedRequest[2]);
            
            Inventory inventory = new Inventory();	           
	        List<String[]> inventoryRecord = inventory.inventoryByID(medicationID);
	            
	            for(String[] slot: inventoryRecord) {
	            	 q = Integer.valueOf(slot[2]);
	            }
	            
	            int newLevel = q + requestQuantity;
	           
	           String level = String.format("%d", newLevel);
        
	          inventory.updateInventory(medicationID, level);

        }
        else if (decision == 2) {
            selectedRequest[3] = "Denied";
            System.out.println("Replenishment request denied.");
        }
        
        // Display all replenishment requests with numbers for selection
        for (int i = 0; i < replenishmentRequests.size(); i++) {
            String[] request = replenishmentRequests.get(i);
            System.out.printf("%d. Request ID: %s | Medication ID: %s | Quantity: %s | Status: %s\n",
                    i + 1, request[0], request[1], request[2], request[3]);
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
            
            
            Feedback feedback = new Feedback();
            Appointment appointment = new Appointment();

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
                case 2 -> appointment.viewAppointments();
                case 3 -> manageInventory();
                case 4 -> approveReplenishmentRequests();
                case 5 -> feedback.viewFeedback();
                case 6 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 6);
    }
}
