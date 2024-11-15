package hospital_system;

public interface CSV_PathHandler {
	
	String toCSV();               // Converts the object to a CSV-formatted string
    void fromCSV(String csvLine);  // Populates the object from a CSV line
}
