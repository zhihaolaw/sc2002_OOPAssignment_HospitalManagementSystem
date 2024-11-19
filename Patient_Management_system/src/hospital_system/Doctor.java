package hospital_system;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Doctor extends User {

    private String age;
    private String gender;
    private String availability;
    
    private static final String APPOINTMENTS_FILE_PATH = FilePaths.getAppointmentFilePath();
    private static final String PATIENTS_FILE_PATH = FilePaths.getPatientListFilePath();
    private static final String INVENTORY_FILE_PATH = FilePaths.getInventoryFilePath();
    private static final String PRESCRIPTION_FILE_PATH = FilePaths.getPrescriptionFilePath();
    		
    // Store appointments loaded from appointments.txt
    private static Map<String, Appointment> appointments = new HashMap<>();
    private static Map<String, Patient> patients = new HashMap<>(); // a global patient list
    private static Map<String, Patient> prescription = new HashMap<>();

    // Constructor for Doctor class
    public Doctor(String hospitalID, String name, String role, String gender, String age, String contactNumber, String password, String email) {
        super(hospitalID, name, "Doctor", gender, age, contactNumber, password, email);  // Call to the superclass User constructor
        this.age = age;  // Set the doctor's age
    }

    // Override the getAge method to return the age for Doctor
    @Override
    public String getAge() {
        return this.age;
    }
    
    public String getPassword() {
    	return this.password;
    }
    // Method to load appointments from CSV file (appointments.csv)
    private static void loadAppointments() {
        appointments.clear();  // Clear current appointment data

        File file = new File(APPOINTMENTS_FILE_PATH);
        try (Scanner sc = new Scanner(file)) {
            // Skip the header line
            if (sc.hasNextLine()) {
                sc.nextLine();  // Skip the first line (header)
            }

            // Iterate through each line of the CSV file
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;  // Skip empty lines

                // Split the line by commas (since it's a CSV file)
                String[] data = line.split(",");

                // Ensure the line contains at least 11 fields (appointment fields)
                if (data.length < 11) {
                    System.out.println("Skipping invalid line: " + line);
                    continue;
                }

                // Extract data for each field in the CSV
                String appointmentID = data[0];
                Appointment appointment = getAppointment(data, appointmentID);
                appointments.put(appointmentID, appointment);  // Use appointmentID as the key
            }
            System.out.println("Appointments loaded successfully from CSV.");
        } catch (FileNotFoundException e) {
            System.out.println("Appointments CSV file not found!");
        } catch (Exception e) {
            System.out.println("Error loading appointments: " + e.getMessage());
        }
    }

    private static Appointment getAppointment(String[] data, String appointmentID) {
        String patientID = data[1].isEmpty() ? null : data[1];  // Handle empty patientID (can be null for pending appointments)
        String doctorID = data[2];
        String doctorName = data[3];
        String date = data[4];
        String time = data[5];
        String status = data[6];
        String outcome = data[7].isEmpty() ? null : data[7];  // Handle empty outcome
        String service = data[8];
        String medication = data[9];
        String notes = data[10];

        // Create a new Appointment object and store it in the appointments map
        return new Appointment(appointmentID, patientID, doctorID, doctorName, date, time, status, outcome, service, medication, notes);
    }


    // Method to save appointments back to file (appointments.txt)
    private static void saveAppointments() {
        File file = new File(APPOINTMENTS_FILE_PATH);
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            // Write the header first
            writer.println("appointmentID,patientID,doctorID,doctorName,date,time,status,outcome,service,medication,notes");

            // Write each appointment's data
            for (Appointment appointment : appointments.values()) {
                writer.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                        appointment.getAppointmentID(),
                        appointment.getPatientID() != null ? appointment.getPatientID() : "",  // Handle null patientID
                        appointment.getDoctorID(),
                        appointment.getDoctorName(),
                        appointment.getDate(),
                        appointment.getTime(),
                        appointment.getStatus() != null ? appointment.getStatus().replace(",", " ") : "",
                        appointment.getOutcome() != null ? appointment.getOutcome().replace(",", " ") : "",  // Handle null outcome
                        appointment.getService() != null ? appointment.getService().replace(",", " ") : "",
                        appointment.getMedication() != null ? appointment.getMedication().replace(",", " ") : "",  // Handle null medication
                        appointment.getNotes() != null ? appointment.getNotes().replace(",", " ") : ""  // Handle null notes
                );
            }
            System.out.println("Appointments data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving appointments file: " + e.getMessage());
        }
    }


    // Load patient records from file (assuming patients.txt)

    private static void loadPatientData() {
        patients.clear();  // Clear the existing data to prevent old data from interfering
        try {
            File file = new File(PATIENTS_FILE_PATH);
            Scanner sc = new Scanner(file);
            sc.nextLine(); // Skip header

            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(","); // CSV is comma-separated

                // Ensure there are at least 9 fields (Patient data fields)
                if (data.length < 11) {
                    System.out.println("Skipping invalid line: " + line);
                    continue;
                }

                // Create a new Patient object with the correct field order
                String patientID = data[0];
                String name = data[1];
                String gender = data[2];
                String age = data[3];
                String email = data[4];
                String password = data[5];
                String dob = data[6];
                String bloodType = data[7];
                String contactNumber = data[8]; 
                String pastDiagnosis = data[9];
                String pastTreatments = data[10];

                // Store the Patient object in the map
                patients.put(patientID, new Patient(patientID, name, "Patient", gender, age, contactNumber, password, email, dob, bloodType, pastDiagnosis, pastTreatments));
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Patient data file not found!");
        }
    }

    
    private void savePatientData() {    
    	File file = new File(PATIENTS_FILE_PATH);
    	try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {        // Updated header to include Role, Age, and Password columns
    		writer.println("Patient ID,Name,Date of Birth,Gender,Role,Age,Blood Type,Phone Number,Password,Email Address,Past Diagnosis,Past Treatments");
        // Loop through the patients map to write data for each patient        
    		for (Patient patient : patients.values()) {
            // Format each field to handle commas or missing values            
    			writer.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n",
                    patient.getPatientID() != null ? patient.getPatientID() : "",              // Patient ID                    
            		patient.getName() != null ? patient.getName() : "",                        // Name
                    patient.getDob() != null ? patient.getDob() : "",                          // Date of Birth                    
            		patient.getGender() != null ? patient.getGender() : "",                    // Gender
                    patient.getRole() != null ? patient.getRole() : "",                        // Role                    
            		patient.getAge() != null ? patient.getAge() : "",                          // Age
                    patient.getBloodType() != null ? patient.getBloodType() : "",              // Blood Type                    
            		patient.getContactNumber() != null ? patient.getContactNumber() : "",      // Phone Number
                    patient.getPassword() != null ? patient.getPassword() : "",                // Password                    
            		patient.getEmail() != null ? patient.getEmail() : "",                      // Email Address
                    patient.getDiagnoses() != null ? patient.getDiagnoses().replace(",", " ") : "",  // Past Diagnosis (sanitizing commas)                    
            		patient.getTreatments() != null ? patient.getTreatments().replace(",", " ") : "" // Past Treatments (sanitizing commas)
					);        
    			}
    			System.out.println("Patient data saved successfully.");    
        } catch (IOException e) {
        		System.out.println("Error saving patient data: " + e.getMessage());    
        		}
}


    public void viewPatientMedicalRecords() {
        Appointment appt = new Appointment();
        Patient p = new Patient();
        List<String[]> patientRecord = appt.getPatientListByAppointment(this.hospitalID);

        // Use a List to keep track of displayed Patient IDs
        List<String> displayedPatients = new ArrayList<>();

        for (String[] slot : patientRecord) {
            String patientID = slot[1]; // Extract the Patient ID from the record

            if (!displayedPatients.contains(patientID)) { // Check if this Patient ID was already displayed
                p.viewMedicalRecord(patientID); // Display the record
                displayedPatients.add(patientID); // Mark this Patient ID as displayed
            }
        }

        if (displayedPatients.isEmpty()) {
            System.out.println("No patients found.");
        }
    	
    }

    
    

    // Update a patient's medical record
    public void updatePatientMedicalRecords() {
        if (patients.isEmpty()) {
            loadPatientData();  // Ensure patient data is loaded once
        }

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Appointment ID to update patient record: ");
        String appointmentID = sc.nextLine().trim();

        // Check if the appointment exists
        if (appointments.containsKey(appointmentID)) {
            Appointment appointment = appointments.get(appointmentID);
            String patientID = appointment.getPatientID();

            // Check if the patient exists
            if (patientID != null && patients.containsKey(patientID)) {
                Patient patient = patients.get(patientID);

                // Prompt for diagnosis and treatment updates
                System.out.println("Updating Medical Record for Patient: " + patient.getName());
                System.out.print("Enter new diagnosis: ");
                String diagnosis = sc.nextLine().trim();
                System.out.print("Enter new treatment plan: ");
                String treatment = sc.nextLine().trim();

                // Update the patient's data (only diagnosis and treatment)
                patient.setDiagnoses(diagnosis);
                patient.setTreatments(treatment);

                // Save the updated data back to CSV
                savePatientData();
                System.out.println("Patient record updated successfully.");
            } else {
                System.out.println("Error: Patient not found for the given appointment.");
            }
        } else {
            System.out.println("Error: Appointment not found.");
        }
    }


    // Accept or decline appointment requests
    public void manageAppointmentRequests() {
    	Appointment appt = new Appointment();
    	Scanner sc = new Scanner(System.in);
    	System.out.printf("Name: Dr. %s | ID: %s\n",this.name,this.hospitalID);
    	System.out.println();
    	System.out.println("Viewing pending appointment requests...");
    	System.out.println();
    	
    	List<String[]> pendingAppt = appt.loadPendingAppointments(this.hospitalID);
		
		for (String[] slot : pendingAppt) {
		    System.out.printf("Appointment ID: %s | PatientID: %s | Date: %s | Time: %s | Status: %s\n",
		    		slot[0], slot[1], slot[4], slot[5], slot[6]);
		}
		
		
		// Allow doctor to decide on each pending appointment
		for (String[] column : pendingAppt) {
		    System.out.println("\nDo you want to accept the appointment? (y/n): ");
		    String decision = sc.nextLine();
		    if (decision.equalsIgnoreCase("y")) {
		        column[6] = "Scheduled"; // Update status to Scheduled
		        column[7] = "N/A";
		        column[8] = "N/A";
		        column[9] = "N/A";
		        column[10] = "N/A";
		        
		    } else if (decision.equalsIgnoreCase("n")) {
		    	column[1] = "N/A";          // Clear PatientID
		    	column[6] = "Available"; // Revert status to Available
		    	column[7] = "N/A";
		        column[8] = "N/A";
		        column[9] = "N/A";
		        column[10] = "N/A";
		    } else {
		        System.out.println("Invalid input. Skipping this appointment.");
		    }
		}
		try (BufferedReader br = new BufferedReader(new FileReader(APPOINTMENTS_FILE_PATH))) {
		    List<String[]> updatedAppts = new ArrayList<>();
		    String line;

		    // Step 1: Read all data from the file
		    while ((line = br.readLine()) != null) {
		        String[] columns = line.split(",");

		        // Check if the current row matches an appointment being updated
		        boolean found = false;
		        for (String[] updatedSlot : pendingAppt) {
		            if (columns[0].equals(updatedSlot[0])) {
		                updatedAppts.add(updatedSlot); // Use updated data
		                found = true;
		                break;
		            }
		        }

		        if (!found) {
		            updatedAppts.add(columns); // Keep original data for other rows
		        }
		    }

		    // Step 2: Write all data back to the file
		    try (BufferedWriter bw = new BufferedWriter(new FileWriter(APPOINTMENTS_FILE_PATH))) {
		        for (String[] row : updatedAppts) {
		            bw.write(String.join(",", row));
		            bw.newLine();
		        }
		    }

		    System.out.println("\nAppointments updated successfully.");
		} catch (IOException e) {
		    System.err.println("Error updating appointments: " + e.getMessage());
		    e.printStackTrace();
		}
		
		
		if(pendingAppt.isEmpty())
		{
			System.out.println("No upcoming appointments found.");
		}
    	
    }

    // View upcoming appointments for the doctor

    public void viewUpcomingAppointments() {
    	Appointment appt = new Appointment();
    	System.out.printf("Name: Dr. %s | ID: %s\n",this.name,this.hospitalID);
    	System.out.println();
    	System.out.println("Viewing your upcoming appointments...");
    	System.out.println();
    	List<String[]> scheduledAppt = appt.loadUpcomingAppointments(this.hospitalID);
		
		for (String[] slot : scheduledAppt) {
		    System.out.printf("Appointment ID: %s | PatientID: %s | Date: %s | Time: %s | Status: %s\n",
		    		slot[0], slot[1], slot[4], slot[5], slot[6]);
		}
		if(scheduledAppt.isEmpty())
		{
			System.out.println("No upcoming appointments found.");
		}
    }


    // Record Appointment Outcome
    public void recordAppointmentOutcome() {
    	 if (appointments.isEmpty()) {
             loadAppointments();  // Ensure patient data is loaded once
         }
    	boolean found = false;
        Scanner sc = new Scanner(System.in);
    	System.out.printf("Name: Dr. %s | ID: %s\n",this.name,this.hospitalID);
    	System.out.println();
    	System.out.println("Recording appointment outcome...");
    	System.out.println();
    	Appointment appt = new Appointment();
    	List<String[]> scheduledAppt = appt.loadScheduledAppointments(this.hospitalID);
    	
    	Inventory inventory = new Inventory();
    	List<String[]> inventoryList = inventory.loadAllInventory();
    	
		
		for (String[] slot : scheduledAppt) {
		    System.out.printf("Appointment ID: %s | PatientID: %s | Date: %s | Time: %s | Status: %s\n",
		    		slot[0], slot[1], slot[4], slot[5], slot[6]);
		   
		}
		if(scheduledAppt.isEmpty())
		{
			System.out.println("No scheduled appointments found.");
		}
		System.out.print("Enter Appointment ID to record outcome: ");
		String appointmentID = sc.nextLine();
		
		 for (String[] slot : scheduledAppt) {
	            if (slot[0].equals(appointmentID) && slot[6].equals("Scheduled")) { // Ensure it's a valid and scheduled appointment
	                found = true;
	                Appointment a = appointments.get(appointmentID);

	                System.out.print("Enter Outcome: ");
	                String outcome = sc.nextLine();
	                
	                //Print Medicine List
	                System.out.print("------Medcine List------\n");	                
	                for(String[] inventorySlot : inventoryList) {
	                	System.out.printf("Medicine ID: %s | Medicine Name: %s\n",
	        		    		inventorySlot[0], inventorySlot[1]);
	                }
	                
	                System.out.print("Enter Medicine ID: ");
	                String medicationID = sc.nextLine();
	                
	                
	                System.out.print("Enter Quantity of Medication: ");
	                String quantity = sc.nextLine();

	                String prescriptionID = getNextID(PRESCRIPTION_FILE_PATH, "PR");
	                
	                try (PrintWriter writer = new PrintWriter(new FileWriter(PRESCRIPTION_FILE_PATH, true))) {
	                    // Format: Appointment ID, Patient ID, Doctor ID, Doctor Name, Date, Time, Status, Outcome, Service, Medication, Notes
	                    writer.printf("%s,%s,%s,%s,%s\n", 
	                                  prescriptionID,
	                                  appointmentID,
	                                  medicationID,
	                                  quantity,                           
	                                  "Pending");
	                    System.out.println("Medication Succesfully Added");
	                } catch (IOException e) {
	                    System.out.println("Error writing the new prescription to the CSV file: " + e.getMessage());
	                }
	                
	                System.out.print("Enter Type of Service: ");
	                String service = sc.nextLine();
	                System.out.print("Additional Notes: ");
	                String notes = sc.nextLine();

	                // Update the patient's data using setters
	                a.setStatus("Completed");
	                a.setMedication(medicationID);
	                a.setNotes(notes);
	                a.setOutcome(outcome);
	                a.setService(service);
	                

	                saveAppointments();
	                System.out.println("Appointment record updated successfully.");
	                
	                break;
	            }
	        }

	        if (!found) {
	            System.out.println("Invalid or non-scheduled Appointment ID. Please try again.");
	            return;
	        }
	        
 
    }
    
    
    public void setAvailability() {
        Scanner sc = new Scanner(System.in);

        // Validate and input the date
        String date = "";
        while (true) {
            System.out.println("Enter the date for the slot (DD/MM/YYYY): ");
            date = sc.nextLine();
            if (validateDate(date)) {
                break; // Exit loop if date is valid
            } else {
                System.out.println("Invalid date format or value. Please try again.");
            }
        }

        // Validate and input the time
        String time = "";
        while (true) {
            System.out.println("Enter the time for the slot (e.g., 12:00, 13:30): ");
            time = sc.nextLine();
            if (validateTime(time)) {
                break; // Exit loop if time is valid
            } else {
                System.out.println("Invalid time format. Please try again.");
            }
        }

        // Generate the next available Appointment ID
        String appointmentID = getNextID(APPOINTMENTS_FILE_PATH, "A");

        // Set other default values
        String patientID = "N/A";
        String status = "Available";
        String outcome = "N/A";
        String service = "N/A";
        String medication = "N/A";
        String notes = "N/A";

        try (PrintWriter writer = new PrintWriter(new FileWriter(APPOINTMENTS_FILE_PATH, true))) {
            // Format: Appointment ID, Patient ID, Doctor ID, Doctor Name, Date, Time, Status, Outcome, Service, Medication, Notes
            writer.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n", 
                          appointmentID,
                          patientID,
                          super.hospitalID,  // DoctorID (assuming hospitalID refers to DoctorID here)
                          getName(),         // Doctor Name
                          date, 
                          time, 
                          status,
                          outcome,
                          service,
                          medication,
                          notes);
            System.out.println("Slot opened successfully for " + date + " at " + time + " with status 'Available'.");
        } catch (IOException e) {
            System.out.println("Error writing the new slot to the CSV file: " + e.getMessage());
        }
    }

    
    private boolean validateDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    
    private boolean validateTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setLenient(false);
        try {
            sdf.parse(time);
            return true;
        } catch (ParseException e) {
            return false;
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
                if (data.length > 0 && data[0].startsWith(startsWith)) {
                    // Extract the numeric part of the Appointment ID
                    if(startsWith == "A") {
                    	int id = Integer.parseInt(data[0].substring(1));
                    	if (id > maxId) {
                            maxId = id;
                        }
                    }else if(startsWith == "PR") {
                    	int id = Integer.parseInt(data[0].substring(2));
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


    // Method to view the doctor's personal schedule
    public void viewSchedule() {
    	Appointment appt = new Appointment();
    	System.out.printf("Name: DR %s | ID: %s\n",this.name,this.hospitalID);
    	System.out.println();
    	System.out.println("Viewing your schedule...");
    	System.out.println();
    	
    	List<String[]> scheduledAppt = appt.loadUpcomingAppointments(this.hospitalID);
		
		for (String[] slot : scheduledAppt) {
		    System.out.printf("Appointment ID: %s | PatientID: %s | Date: %s | Time: %s | Status: %s\n",
		    		slot[0], slot[1], slot[4], slot[5], slot[6]);
		}
		if(scheduledAppt.isEmpty())
		{
			System.out.println("No upcoming appointments found.");
		}
    	System.out.println();
    	System.out.println("Viewing your Available Slots...");
    	System.out.println();
    	List<String[]> availableAppointment = appt.loadAvailableAppointmentsDoc(this.hospitalID);
		for (String[] slot : availableAppointment) {
		    System.out.printf("Appointment ID: %s | Date: %s | Time: %s | Status: %s\n",
		    		slot[0], slot[3], slot[4], slot[5]);
		}
		if(availableAppointment.isEmpty())
		{
			 System.out.println("No available slots found.");
		}
    }
    
    

    private static void loadPatients() {
        patients.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(PATIENTS_FILE_PATH))) {
            String line;

            // Skip header line
            if ((line = br.readLine()) != null && line.contains("Patient ID")) {
                // Do nothing (skip header)
            }

            // Read each patient
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length < 12) continue; // Ensure all fields exist

                Patient patient = new Patient();
                patient.fromCSV(line);
                patients.put(columns[0], patient); // Use Patient ID as the key
            }
        } catch (IOException e) {
            System.out.println("Error loading patients: " + e.getMessage());
        }
    }

   
    public void updatePatientMedicalRecord() {
        Scanner sc = new Scanner(System.in);

        // Load necessary data
        loadAppointments();
        loadPatients();

        // View all completed appointments for this doctor
        System.out.println("Completed Appointments:");
        List<Appointment> completedAppointments = new ArrayList<>();
        for (Appointment appointment : appointments.values()) {
            if (appointment.getDoctorID().equals(this.hospitalID) && "Completed".equalsIgnoreCase(appointment.getStatus())) {
                completedAppointments.add(appointment);
                System.out.println(appointment.getAppointmentID() + " | Patient: " + appointment.getPatientID() + " | Date: " + appointment.getDate());
            }
        }

        if (completedAppointments.isEmpty()) {
            System.out.println("No completed appointments found.");
            return;
        }

        // Ask the doctor to choose an appointment
        System.out.print("Enter the Appointment ID to update: ");
        String appointmentID = sc.nextLine();

        Appointment selectedAppointment = appointments.get(appointmentID);
        if (selectedAppointment == null) {
            System.out.println("Invalid Appointment ID.");
            return;
        }

        // Retrieve the patient associated with the appointment
        String patientID = selectedAppointment.getPatientID();
        Patient patient = patients.get(patientID);
        if (patient == null) {
            System.out.println("Patient record not found.");
            return;
        }

        // Get new diagnosis and treatment details from the doctor
        System.out.println("Updating record for patient: " + patient.getName());
        System.out.print("Enter new diagnosis: ");
        String newDiagnosis = sc.nextLine();
        System.out.print("Enter new treatment: ");
        String newTreatment = sc.nextLine();

        // Append the new data to the patient's medical record
        patient.setPastDiagnoses(patient.getPastDiagnoses() + "; " + newDiagnosis);
        patient.setPastTreatments(patient.getPastTreatments() + "; " + newTreatment);

        // Save updated patient records
        savePatients();
        System.out.println("Medical record updated successfully.");
    }

   
    private void savePatients() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PATIENTS_FILE_PATH))) {
            // Write header
            bw.write("Patient ID,Name,DOB,Gender,Role,Age,BloodType,ContactNumber,Password,Email,PastDiagnoses,PastTreatments");
            bw.newLine();

            // Write each patient's data
            for (Patient patient : patients.values()) {
                bw.write(patient.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving patient records: " + e.getMessage());
        }
    }
    

    // Override the abstract method from User class to show doctor-specific menu
    @Override
    public void showMenu() {
        Scanner sc = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\n--- Doctor Menu ---");
            System.out.println("1. View Patient Medical Records");
            System.out.println("2. Update Patient Medical Records");
            System.out.println("3. View Personal Schedule");
            System.out.println("4. Set Availability for Appointments");
            System.out.println("5. Accept or Decline Appointment Requests");
            System.out.println("6. View Upcoming Appointments");
            System.out.println("7. Record Appointment Outcome");
            System.out.println("8. Logout");

            // Loop to validate the input
            while (true) {
                System.out.print("Enter your choice: ");
                try {
                    choice = sc.nextInt();  // Attempt to read an integer
                    break;  // Exit the loop if a valid integer is entered
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter an integer.");
                    sc.next();  // Consume the invalid input to avoid infinite loop
                }
            }

            switch (choice) {
                case 1 -> viewPatientMedicalRecords();
                case 2 -> updatePatientMedicalRecord();
                case 3 -> viewSchedule();
                case 4 -> setAvailability();
                case 5 -> manageAppointmentRequests();
                case 6 -> viewUpcomingAppointments();
                case 7 -> recordAppointmentOutcome();
                case 8 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 8);
    }
}
