package hospital_system;

import java.io.*;
import java.util.*;

public class CSV_Handler {
 
	// Load data from a CSV file into a list of String arrays (each array represents a row)
	public static List<String[]> loadFromCSV(String filePath) {
		List<String[]> data = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			br.readLine(); // Skip header line
			while ((line = br.readLine()) != null) {
				data.add(line.split(","));
			}
		} catch (IOException e) {
			System.out.println("Error reading from " + filePath + ": " + e.getMessage());
		}
		return data;
	}
 
	// Save data to a CSV file, updating existing records based on unique identifier
	public static void saveToCSV(String filePath, List<String[]> newData, String header) {
		List<String[]> existingData = loadFromCSV(filePath);  // Load current file contents (excluding header)

		// Convert existing data to a map for easy lookup and update by ID (assume first column is unique ID)
		Map<String, String[]> dataMap = new HashMap<>();
		for (String[] row : existingData) {
			if (row.length > 0) {
				dataMap.put(row[0], row);  // Use the first column (ID) as the key
			}
		}

		// Update or add each row in newData based on the ID
		for (String[] newRow : newData) {
			if (newRow.length > 0) {
				dataMap.put(newRow[0], newRow);  // If ID exists, it updates; otherwise, it adds a new entry
			}
		}

		// Write the updated data to the file
		try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
			writer.println(header);  // Write header once
			for (String[] row : dataMap.values()) {
				writer.println(String.join(",", row));  // Write each row
			}
		} catch (IOException e) {
         System.out.println("Error writing to " + filePath + ": " + e.getMessage());
		}
	}
	
}


