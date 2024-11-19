package hospital_system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Prescription {
	
	private static final String PRESCRIPTION_FILE_PATH = FilePaths.getPrescriptionFilePath();
	private static final String MEDICINE_INVENTORY_FILE_PATH = FilePaths.getInventoryFilePath();
	private static Map<String, String[]> prescriptions = new HashMap<>();
	private static Map<String, Inventory> inv = new HashMap<>();

	
	public Prescription() {
		// TODO Auto-generated constructor stub
	}
	

	private static void loadPrescriptions() {
        prescriptions.clear();  // Clear existing prescriptions before loading
        try (Scanner sc = new Scanner(new File(PRESCRIPTION_FILE_PATH))) {
            sc.nextLine(); // Skip header line
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");  // Assuming CSV is comma-separated
                if (data.length < 5) continue;  // Ensure valid data

                prescriptions.put(data[0], data);  // Use prescriptionID as the key
            }
        } catch (FileNotFoundException e) {
            System.out.println("Prescriptions CSV file not found!");
        }
    }

	    public void updatePrescriptionStatus() {
	    	boolean updated = false;
	        Scanner sc = new Scanner(System.in);
	        updated = viewPendingPrescriptionRecords();
	        if(!updated)
	        {
	        	return;
	        }
	        System.out.print("Enter Prescription ID to update: ");
	        String prescriptionID = sc.nextLine();
	        
	        int stock = 0;
	        
	        if(prescriptions.isEmpty())loadPrescriptions();

	        if (prescriptions.containsKey(prescriptionID)) {
	            String[] data = prescriptions.get(prescriptionID);
	            
	            System.out.println("Current status: " + data[4]);
	            System.out.print("Enter new status (e.g., Dispensed, Pending): ");
	            String status = sc.nextLine();
	            data[4] = status; // Update the status
	            
	           
	           
	            if(status.equals("Dispensed")) {
	            	 int quantity = Integer.valueOf(data[3]);
	            	 String medID = data[2];
	            	 
	            	 Inventory inventory = new Inventory();	           
	 	            List<String[]> inventoryRecord = inventory.inventoryByID(medID);
	 	            
	 	            for(String[] slot: inventoryRecord) {
	 	            	 System.out.printf("Med ID: %s | Medicine Name: %s | Initial Stock: %s\n",
	 	    		    		slot[0], slot[1], slot[2]);
	 	            	 stock = Integer.valueOf(slot[2]);
	 	            }
	 	            
	 	            int newLevel = stock - quantity;
	 	           
	 	           String level = String.format("%d", newLevel);
 	           
	 	          inventory.updateInventory(medID, level);
	 	          updated = true;
	            }
	            else
	            {
	            	updated = false;
	            }
	            
	            
	            savePrescriptions();  // Save the updated prescriptions back to the file
	            System.out.println("Prescription status updated successfully.");
	        } else {
	            System.out.println("Prescription not found.");
	            
	        }
	       
	    }
	    
	    private static void savePrescriptions() {
	        try (PrintWriter writer = new PrintWriter(new FileWriter(PRESCRIPTION_FILE_PATH))) {
	            writer.println("Prescription ID,Appointment ID,Medication ID,Quantity,Status");
	            for (String[] data : prescriptions.values()) {
	                writer.printf("%s,%s,%s,%s,%s\n", data[0], data[1], data[2], data[3], data[4]);
	            }
	        } catch (IOException e) {
	            System.out.println("Error writing to prescriptions file.");
	        }
	    }
	    
	    
	  public void viewPrescriptionRecords() {
      System.out.println("Viewing prescription records...");
      loadPrescriptions();
      if (prescriptions.isEmpty()) {
          System.out.println("No prescription records found.");
      } else {
          for (String[] data : prescriptions.values()) {
              System.out.printf("Prescription ID: %s | Appointment ID: %s | Medication ID: %s | Quantity: %s | Status: %s\n",
                      data[0], data[1], data[2], data[3], data[4]);
          }
      }
  }
	  
	  public boolean viewPendingPrescriptionRecords() {
		  boolean pending = false;
	      System.out.println("Viewing prescription records...");
	      loadPrescriptions();
	      if (prescriptions.isEmpty()) {
	          System.out.println("No prescription records found.");
	      } else {
	          for (String[] data : prescriptions.values()) {
	        	  if(data[4].equals("Pending")) {
	        		  System.out.printf("Prescription ID: %s | Appointment ID: %s | Medication ID: %s | Quantity: %s | Status: %s\n",
	        				  data[0], data[1], data[2], data[3], data[4]);
	        		  pending = true;
	        	  }
	        	  
	          }	
	          if(!pending) {
        		  System.out.printf("No pending prescription found");
        	  }
	          
	      }
	      return pending;
	  }
	  

}
