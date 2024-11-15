package hospital_system;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class Inventory {

	private String medId, medicineName, initialStock, lowStockAlert;
	
	private static final String MEDICINE_INVENTORY_FILE_PATH = "C:\\Users\\usama\\OneDrive\\Desktop\\Edu\\Y2S1\\SC2002 (OOP)\\Patient_Management_system_updated\\Patient_Management_system_updated\\Patient_Management_system\\data\\Medicine_List.csv";
	private static final String REPLENISHMENT_REQUEST_FILE_PATH = "C:\\Users\\usama\\OneDrive\\Desktop\\Edu\\Y2S1\\SC2002 (OOP)\\Patient_Management_system_updated\\Patient_Management_system_updated\\Patient_Management_system\\data\\replenishment_requests.csv";
	
	private static Map<String, String[]> inventory = new HashMap<>();
	
	public Inventory() {
		
	}
	
	public Inventory(String medId, String medicineName, String initialStock, String lowStockAlert) {
		// TODO Auto-generated constructor stub
		this.medId = medId; this.medicineName = medicineName; this.initialStock = initialStock;
		this.lowStockAlert = lowStockAlert;
	}
	
	
	 public void loadInventory() {
	        inventory.clear();  // Clear existing inventory before loading
	        try (Scanner sc = new Scanner(new File(MEDICINE_INVENTORY_FILE_PATH))) {
	            sc.nextLine(); // Skip header line
	            while (sc.hasNextLine()) {
	                String line = sc.nextLine().trim();
	                if (line.isEmpty()) continue;

	                String[] data = line.split(",");  // Assuming CSV is comma-separated
	                if (data.length < 4) continue;  // Ensure valid data

	                inventory.put(data[0], data);  // Use medicationID as the key
	            }
	        } catch (FileNotFoundException e) {
	            System.out.println("Inventory CSV file not found!");
	        }
	    }
	 
	 private static void saveInventory() {
	        try (PrintWriter writer = new PrintWriter(new FileWriter(MEDICINE_INVENTORY_FILE_PATH))) {
	            writer.println("Med ID,Medicine Name,Initial Stock,Low Stock Level Alert");
	            for (String[] data : inventory.values()) {
	                writer.printf("%s,%s,%s,%s\n", data[0], data[1], data[2], data[3]);
	            }
	        } catch (IOException e) {
	            System.out.println("Error writing to inventory file.");
	        }
	    }
	 
	 
	 public void viewInventory() {
	        System.out.println("Viewing medication inventory...");
	        loadInventory();
	        if (inventory.isEmpty()) {
	            System.out.println("No inventory records found.");
	        } else {
	            for (String[] data : inventory.values()) {
	               
	                System.out.printf("Medication ID: %s | Name: %s | Stock Level: %s | Low Stock Alert Level: %s\n",
	                        data[0], data[1], data[2], data[3]);
	            }

	        }
	    }
	 

	 
	 
	    public void submitReplenishmentRequest() {
	        loadInventory();  // Load current stock from the Medicine_List.csv file

	        Scanner sc = new Scanner(System.in);

	        System.out.print("Enter Medication ID to request replenishment for: ");
	        String medicationID = sc.nextLine();

	        // Check if the medication exists in the inventory
	        if (inventory.containsKey(medicationID)) {
	            String[] data = inventory.get(medicationID);  // Get current stock info
	            System.out.println("Current stock level for " + data[1] + ": " + data[2]);  // Display current stock

	            // Get replenishment quantity
	            int quantity;
	            while (true) {
	                try {
	                    System.out.print("Enter the quantity to request for replenishment: ");
	                    quantity = sc.nextInt();
	                    break;  // Exit the loop when valid input is provided
	                } catch (InputMismatchException e) {
	                    System.out.println("Invalid input. Please enter a valid integer.");
	                    sc.next();  // Clear invalid input
	                }
	            }

	            // Write the replenishment request to the file
	            writeReplenishmentRequestToFile(medicationID, quantity);
	            System.out.printf("Replenishment request for %d units of %s has been submitted.\n", quantity, data[1]);
	        } else {
	            System.out.println("Medication not found in inventory.");
	        }
	    }

	    // Helper method to write the replenishment request to a file
	    private void writeReplenishmentRequestToFile(String medicationID, int quantity) {
	        try (PrintWriter writer = new PrintWriter(new FileWriter(REPLENISHMENT_REQUEST_FILE_PATH, true))) {
	            int requestID = generateIncrementalRequestID();  // Generate a numeric request ID starting from 1
	            writer.printf("%d,%s,%d,Pending\n", requestID, medicationID, quantity);  // Append new request in CSV format
	            System.out.println("Replenishment request saved successfully.");
	        } catch (IOException e) {
	            System.out.println("Error writing to replenishment requests file.");
	        }
	    }
	    
	    private int generateIncrementalRequestID() {
	        return getNextRequestID();  // Always increments the ID, starting from 1
	    }

	    
	    // Always return the next incrementing ID based on the count of records, assuming no existing data
	    private int getNextRequestID() {
	        int currentID = 1;  // Start from 1

	        // Open the file and count the number of lines to determine the next ID
	        try (BufferedReader reader = new BufferedReader(new FileReader(REPLENISHMENT_REQUEST_FILE_PATH))) {
	            while (reader.readLine() != null) {
	                currentID++;  // Increment for each line found
	            }
	        } catch (IOException e) {
	            System.out.println("File not found. Starting from ID 1.");
	        }

	        return currentID;
	    }
	    
	    
	    public void checkLowStock() {
	    	System.out.println("\n\n");
	    	
	        for (String[] data : inventory.values()) {
	            try {
	                String medicineName = data[1];
	                int stockLevel = Integer.parseInt(data[2].trim());
	                int lowStockThreshold = Integer.parseInt(data[3].trim());
	                
	                if (stockLevel <= lowStockThreshold) {
	                    alertPharmacist(medicineName, stockLevel, lowStockThreshold);
	                }
	            } catch (NumberFormatException e) {
	                System.out.println("Error: Invalid number format in inventory data for medication " + data[1]);
	            } catch (Exception e) {
	                System.out.println("Unexpected error while checking stock for " + data[1] + ": " + e.getMessage());
	            }
	        }
	        System.out.println("Low stock check completed.");
	    }


	    
	    private void alertPharmacist(String medicineName, int stockLevel, int lowStockThreshold) {
	        System.out.println("ALERT: Low stock on " + medicineName +
	                           " - Current stock: " + stockLevel +
	                           ", Threshold: " + lowStockThreshold);
	    }





}
