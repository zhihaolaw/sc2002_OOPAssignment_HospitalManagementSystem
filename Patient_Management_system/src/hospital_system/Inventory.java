package hospital_system;

import java.io.*;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Inventory {

	private String medId, medicineName, initialStock, lowStockAlert;
	
	private static final String MEDICINE_INVENTORY_FILE_PATH = FilePaths.getInventoryFilePath();
	private static final String REPLENISHMENT_REQUEST_FILE_PATH = FilePaths.getReplenismentFilePath();
	
	private static Map<String, String[]> inventory = new HashMap<>();
	
	public Inventory() {
		
	}
	
	public Inventory(String medId, String medicineName, String initialStock, String lowStockAlert) {
		// TODO Auto-generated constructor stub
		this.medId = medId; this.medicineName = medicineName; this.initialStock = initialStock;
		this.lowStockAlert = lowStockAlert;
	}
	
	public void setMedId(String medId) {
		this.medId = medId;
	}
	
	public String getMedId() {
		return medId;
	}
	
	
	public void setMedicineName(String medicineName) {
		this.medicineName = medicineName;
	}
	
	public String getMedicineName() {
		return medicineName;
	}
	
	public void setInitialStock(String initialStock) {
		this.initialStock = initialStock;
	}
	
	public String getInitialStock() {
		return initialStock;
	}
	
	
	public void setLowStockAlert(String lowStockAlert) {
		this.lowStockAlert = lowStockAlert;
	}
	
	public String getLowStockAlert() {
		return lowStockAlert;
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
	 
	 
	 public void updateInventory(String medID, String quantity) {
	        List<String[]> allRecords = new ArrayList<>();

	        try (BufferedReader br = new BufferedReader(new FileReader(MEDICINE_INVENTORY_FILE_PATH))) {
	            String line;
	            boolean recordFound = false;

	            // Read all records into memory
	            while ((line = br.readLine()) != null) {
	                String[] columns = line.split(",");
	                
	                // Ensure the row has the expected number of columns before proceeding
	                if (columns.length < 4) {
	                    System.err.println("Skipping malformed row: " + line);
	                    allRecords.add(columns);
	                    continue;
	                }

	                // Check if this is the record to update
	                if (columns[0].equals(medID)) {
	                    columns[2] = quantity;  // Update status to Completed
	                    recordFound = true;
	                }

	                allRecords.add(columns);
	            }

	            if (!recordFound) {
	                System.out.println("Med ID not found in the CSV.");
	                return;
	            }
	        } catch (IOException e) {
	            System.err.println("Error reading the CSV file: " + e.getMessage());
	            e.printStackTrace();
	            return;
	        }

	        // Write the updated records back to the CSV file
	        try (BufferedWriter bw = new BufferedWriter(new FileWriter(MEDICINE_INVENTORY_FILE_PATH))) {
	            for (String[] record : allRecords) {
	                bw.write(String.join(",", record));
	                bw.newLine();
	            }
	        } catch (IOException e) {
	            System.err.println("Error writing to the CSV file: " + e.getMessage());
	            e.printStackTrace();
	        }

	        System.out.println("Inventory successfully updated.");
	    }

	 
	 public List<String[]> loadAllInventory() {
		    List<String[]> inventory = new ArrayList<>();
		    Scanner sc = new Scanner(System.in);

		    try (BufferedReader br = new BufferedReader(new FileReader(MEDICINE_INVENTORY_FILE_PATH))) {
		        String line;
		        while ((line = br.readLine()) != null) {
		            // Split line by comma
		        	 
		            String[] columns = line.split(",");

		            // Ensure the line has the expected number of columns
		            if (columns.length >= 4) { // Adjust if you expect more or fewer fields
		                inventory.add(new String[]{
		                        columns[0], // Medicine ID
		                        columns[1], // Medicine Name
		                        columns[2], // Initial Stock
		                        columns[3], // Initial Stock Level
		                });
		            } else {
		                System.err.println("Skipping malformed line: " + line);
		            }
		        }
		    } catch (IOException e) {
		        System.err.println("Error reading the inventory file: " + e.getMessage());
		    }

		    return inventory;
		}
	 
	 public List<String []> inventoryByID(String medID) {
	        
	        boolean recordFound = false;
	        List<String[]> inventoryByID = new ArrayList<>();

	        try (BufferedReader br = new BufferedReader(new FileReader(MEDICINE_INVENTORY_FILE_PATH))) {
	            String line;
	            while ((line = br.readLine()) != null) {
	                String[] columns = line.split(",");
	                if (columns[0].equals(medID)) { // Assuming PatientID is in the first column
	                	inventoryByID.add(new String[]{
	                            columns[0], // AppointmentID
	                            columns[1], // PatientID
	                            columns[2], // DoctorID
	                            columns[3], // Doctor Name
	                        });
	                    recordFound = true;
	                    break; // Exit loop once record is found
	                }
	            }
	        } catch (IOException e) {
	            System.out.println("Error reading patient records: ");
	        }

	        if (!recordFound) {
	            System.out.println("No Medication ID Found");
	        }
	        
	        return inventoryByID;
	    }
	    
	 
	 public static void saveInventory() {
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

	        }
	    }
	 

	 
	 
	    public void submitReplenishmentRequest() {
	        loadInventory();  // Load current stock from the Medicine_List.csv file
	        viewInventory();

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
	                    if(quantity >0)
	                    {
	                    	break;  // Exit the loop when valid input is provided
	                    }
	                    else
	                    {
	                    	System.out.println("Invalid input. Quantity must be at least 1");
	                    }
	                    
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

	    
	    public void updateInventory() {
	    	Scanner sc = new Scanner(System.in);
	    	for(String[] d : inventory.values())
            {
           	 System.out.printf("ID: %s | Name: %s | Stock Level: %s | Low Stock Alert Level: %s\n", d[0], d[1], d[2], d[3]);
            }
	    	 System.out.print("Enter Medication ID to update: ");
             String updateID = sc.nextLine();
             if (inventory.containsKey(updateID)) {
                 String[] medication = inventory.get(updateID);
                 System.out.printf("Current Stock Level: %s\n", medication[2]);
                 System.out.print("Enter new stock level: ");
                 medication[2] = sc.nextLine();
                 saveInventory();
                 System.out.println("Medication stock updated successfully.");
                 for(String[] d : inventory.values())
                 {
                	 System.out.printf("ID: %s | Name: %s | Stock Level: %s | Low Stock Alert Level: %s\n", d[0], d[1], d[2], d[3]);
                 }
             } else {
                 System.out.println("Medication not found.");
             }
         }
	    
	   
            
	    public void updateLowStockAlert() {
	    	
	    	Scanner sc = new Scanner(System.in);
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
                for(String[] d : inventory.values())
                {
               	 System.out.printf("ID: %s | Name: %s | Stock Level: %s | Low Stock Alert Level: %s\n", d[0], d[1], d[2], d[3]);
                }
   		 }
   		 else {
                System.out.println("Medication not found.");
            }
      }
	    
	    
	    
}
