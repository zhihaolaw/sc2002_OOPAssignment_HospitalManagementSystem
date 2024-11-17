package hospital_system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.text.SimpleDateFormat;


public class Patient extends User implements CSV_PathHandler,LoginSystem {

    private String dob;  // Date of birth
    private String gender;
    private String bloodType;
    private String pastDiagnoses;
    private String pastTreatments;
    
    private static final String FEEDBACK_FILE_PATH = FilePaths.getFeedbackFilePath();
    private static final String PATIENT_LIST_FILE_PATH = FilePaths.getPatientListFilePath();
    private static final String APPOINTMENT_LIST_FILE_PATH = FilePaths.getAppointmentFilePath();

    // Data structure to store loaded appointments
    private static final Map<String, Appointment> appointments = new HashMap<>();

    // Constructor for Patient class
    public Patient(String hospitalID, String name, String role, String gender, String age, String contactNumber, String password, String email, String dob, String bloodType, String pastDiagnoses, String pastTreatments) {
        super(hospitalID, name, "Patient", gender, age, contactNumber, password, email);  // Pass the required fields to the User constructor
        this.dob = dob;
        this.bloodType = bloodType;
        this.pastDiagnoses = pastDiagnoses;
        this.pastTreatments = pastTreatments;
        this.gender = gender;
    }
    
    public Patient()
    {
		
    	
    }

    
    @Override
    public String toCSV() {
        return String.join(",", hospitalID, name, dob, gender, "Patient", age, bloodType, contactNumber, password, email, pastDiagnoses, pastTreatments);
    }

    @Override
    public void fromCSV(String csvLine) {
        String[] values = csvLine.split(",");
        if (values.length < 12) {
            throw new IllegalArgumentException("Insufficient data in CSV line.");
        }
     // Assign values to the fields
        this.hospitalID = values[0]; // hospitalID
        this.name = values[1];        // name
        //this.dob = values[2];         // dob (final, should be set in constructor)
        //this.gender = values[3];      // gender (final, should be set in constructor)
        this.role = values[4];        // role ("Patient")
        this.age = values[5];         // age
        //this.bloodType = values[6];   // bloodType (final, should be set in constructor)
        this.contactNumber = values[7];// contactNumber
        this.password = values[8];     // password
        this.email = values[9];        // email
        this.pastDiagnoses = values[10]; // pastDiagnoses
        this.pastTreatments = values[11]; // pastTreatments
    }
    
    // Implement login method from LoginSystem interface
    @Override
    public boolean login(String password) {
        return this.password.equals(password);
    }
    
    @Override
    public void changePassword() {
        Scanner sc = new Scanner(System.in);
        String newPassword, confirmPassword = "";
        int attempts = 0;
        final int MAX_ATTEMPTS = 3;

        do {
            System.out.print("Enter new password: ");
            newPassword = sc.nextLine();

            // Password validation
            if (newPassword.length() < 8) {
                System.out.println("Password must be at least 8 characters long. Please try again.");
                continue;
            }

            System.out.print("Confirm new password: ");
            confirmPassword = sc.nextLine();

            if (!newPassword.equals(confirmPassword)) {
                System.out.println("Passwords do not match. Please try again.");
            } else {
                // Valid password change
                break; // Only exit loop when password is confirmed
            }

            attempts++;
            if (attempts >= MAX_ATTEMPTS) {
                System.out.println("Too many failed attempts. Exiting password change.");
                return; // exit from change password
            }
        } while (true); // Keep looping until password is confirmed

        // Update password and user state
        this.password = newPassword; // Update the password
        this.firstLogin = false;  // Set first login to false after password update
        System.out.println("Password updated successfully.");

        // Attempt to update the password in the CSV file
        boolean updateSuccessful = updatePasswordInCSV(this.hospitalID, newPassword);
        if (updateSuccessful) {
            System.out.println("Password successfully updated in Staff_List.csv.");
        } else {
            System.out.println("Failed to update password in Staff_List.csv.");
        }

        // Check user state after change
        System.out.println("Current User State:");
        System.out.println("Hospital ID: " + this.hospitalID);
        System.out.println("First Login: " + this.firstLogin);
        
        //sc.close(); // Close Scanner to free resources
    }


    // Helper method to update password in Staff_List.csv
    private boolean updatePasswordInCSV(String userId, String newPassword) {
        List<String[]> records = CSV_Handler.loadFromCSV(PATIENT_LIST_FILE_PATH);
        boolean recordFound = false;

        for (String[] record : records) {
            if (record[0].equals(userId)) { // Assuming userId is the first column in CSV
                record[8] = newPassword; // Assuming password is in the seventh column in CSV
                recordFound = true;
                break;
            }
        }

        if (recordFound) {
            String header = "Patient ID,Name,Date of Birth,Gender,Role,Age,Blood Type,Phone Number,Password,Email Address,Past Diagnosis,Past Treatments";
            try {
                CSV_Handler.saveToCSV(PATIENT_LIST_FILE_PATH, records, header);
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


    // Getter methods
    public String getPatientID() {
        return hospitalID;  // hospitalID is acting as patientID
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getDiagnoses() {
        return pastDiagnoses;
    }

    public String getTreatments() {
        return pastTreatments;
    }

    // Setter methods for pastDiagnoses and pastTreatments
    public void setDiagnoses(String diagnoses) {
        this.pastDiagnoses = diagnoses;
    }

    public void setTreatments(String treatments) {
        this.pastTreatments = treatments;
    }
    
    public void setPastDiagnoses(String pastDiagnoses) {
        this.pastDiagnoses = pastDiagnoses;
    }
    
    public void setPastTreatments(String pastTreatments) {
        this.pastTreatments = pastTreatments;
    }
    

    public String getPastDiagnoses() {
        return this.pastDiagnoses;
    }
    
    public String getPastTreatments() {
        return this.pastTreatments;
    }
    
    // Method to display patient's medical record
    public void viewMedicalRecord() {
        System.out.println("Patient ID      :" + hospitalID);
        System.out.println("Patient Name    :" + name);
        System.out.println("Date of Birth   :" + dob);
        System.out.println("Gender          :" + gender);
        System.out.println("Email Address   :" + email);
        System.out.println("Phone number    :" + contactNumber);
        System.out.println("Blood Type      :" + bloodType);
        System.out.println("Past Diagnoses  :" + pastDiagnoses);
        System.out.println("Past Treatments :" + pastTreatments);
    }
    
    public void viewMedicalRecord(String patientID) {
        boolean recordFound = false;

        try (BufferedReader br = new BufferedReader(new FileReader(PATIENT_LIST_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns[0].equals(patientID)) { // Assuming PatientID is in the first column
                    System.out.println("\n---- Patient Medical Record ----");
                    System.out.println("Patient ID      : " + columns[0]);
                    System.out.println("Name            : " + columns[1]);
                    System.out.println("Date of Birth   : " + columns[2]);
                    System.out.println("Gender          : " + columns[3]);
                    System.out.println("Age             : " + columns[5]);
                    System.out.println("Blood Type      : " + columns[6]);
                    System.out.println("Phone Number    : " + columns[7]);
                    System.out.println("Email Address   : " + columns[9]);
                    System.out.println("Past Diagnoses  : " + columns[10]);
                    System.out.println("Past Treatments : " + columns[11]);
                    recordFound = true;
                    break; // Exit loop once record is found
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading patient records: " + e.getMessage());
        }

        if (!recordFound) {
            System.out.println("No record found for Patient ID: " + patientID);
        }
    }
    

    public void updateContactInfo() {
        Scanner sc = new Scanner(System.in);

        // Display current contact info
        System.out.println("Current phone number: " + this.contactNumber);
        System.out.println("Current email address: " + this.email);

        // Prompt for new contact info
        System.out.print("Enter new phone number: ");
        String newPhone = sc.nextLine().trim();
        System.out.print("Enter new email address: ");
        String newEmail = sc.nextLine().trim();

        // Update the patient's contact information in memory
        this.contactNumber = newPhone;
        this.email = newEmail;

        // Save the updated information back to the CSV file without altering other patients
        updatePatientContactInCSV();

        // Verify the update by displaying the updated medical record
        System.out.println("\nYour updated contact information:");
        viewMedicalRecord();
    }

    // Method to update only the phone and email for the specific patient in the CSV
    private void updatePatientContactInCSV() {
        File file = new File("data/Patient_List.csv");
        StringBuilder updatedData = new StringBuilder();

        try (Scanner sc = new Scanner(file)) {
            boolean patientFound = false;

            // Read the CSV and store each line
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;  // Skip empty lines

                String[] data = line.split(",");  // Split line by commas (CSV format)

                // Assuming hospitalID is the first column (index 0)
                if (data[0].equals(this.hospitalID)) {
                    // Update the phone and email for the matching patient
                    data[5] = this.contactNumber;   // Assuming phone is at index 5
                    data[6] = this.email;   // Assuming email is at index 6
                    patientFound = true;
                }

                // Rebuild the line after update (or no change if different patient)
                updatedData.append(String.join(",", data)).append("\n");
            }

            // If the patient was not found in the file
            if (!patientFound) {
                System.out.println("Patient not found in the file.");
                return;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Patient data file not found!");
            return;
        }

        // Write the updated data back to the CSV file
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.print(updatedData);  // Write the modified content
            System.out.println("Patient contact information updated successfully.");
        } catch (IOException e) {
            System.out.println("Error saving patient data.");
        }
    }


    // Method to load appointments from the CSV file

    // Method to load appointments from the CSV file
    private static void loadAppointments() {
        appointments.clear();  // Clear the existing appointments before loading new ones

        File file = new File(APPOINTMENT_LIST_FILE_PATH);  // CSV file path
        try (Scanner sc = new Scanner(file)) {
            // Skip the header row
            if (sc.hasNextLine()) {
                sc.nextLine();
            }

            // Read each line in the CSV file
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) {
                    continue;  // Skip empty lines
                }

                // Split the line into data fields (comma-separated)
                String[] data = line.split(",");

                // Ensure there are at least 11 fields in the data (based on the CSV structure)
                if (data.length < 11) {
                    System.out.println("Skipping invalid or incomplete line: " + line);
                    continue;
                }

                // Extract data for each appointment field
                String appointmentID = data[0];
                Appointment appointment = getAppointment(data, appointmentID);
                appointments.put(appointmentID, appointment);  // Use appointmentID as the key
            }
        } catch (FileNotFoundException e) {
            System.out.println("Appointments file not found! Please check the file path.");
        } catch (Exception e) {
            System.out.println("Error loading appointments: " + e.getMessage());
        }
    }

    private static Appointment getAppointment(String[] data, String appointmentID) {
        String patientID = data[1];  // This may be empty for available slots
        String doctorID = data[2];
        String doctorName = data[3];
        String date = data[4];
        String time = data[5];
        String status = data[6];  // Should be "Pending", "Scheduled", "Completed", etc.
        String outcome = data[7];  // This can be empty for "Pending" appointments
        String service = data[8];  // Type of service provided (e.g., checkup)
        String medication = data[9];  // This can be empty or filled after the appointment
        String notes = data[10];  // Additional notes for the appointment

        // Create an Appointment object and add it to the appointments map
        return new Appointment(
                appointmentID, patientID, doctorID, doctorName, date, time, status, outcome, service, medication, notes);
    }


    // Method to save appointments back to the CSV file

    // Method to save appointments back to the CSV file without reloading
    private static void saveAppointments() {
        File file = new File(APPOINTMENT_LIST_FILE_PATH);

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            // Write the header first
            writer.println("appointmentID,patientID,doctorID,doctorName,date,time,status,outcome,service,medication,notes");

            // Write each appointment's data from the `appointments` map
            for (Appointment appointment : appointments.values()) {
                writer.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                        appointment.getAppointmentID(),
                        appointment.getPatientID() != null ? appointment.getPatientID() : "",
                        appointment.getDoctorID(),
                        appointment.getDoctorName(),
                        appointment.getDate(),
                        appointment.getTime(),
                        appointment.getStatus(),
                        appointment.getOutcome() != null ? appointment.getOutcome() : "",
                        appointment.getService(),
                        appointment.getMedication() != null ? appointment.getMedication() : "",
                        appointment.getNotes() != null ? appointment.getNotes() : "");
            }
            System.out.println("Appointments data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving appointments file: " + e.getMessage());
        }
    }


    // Method to view available appointment slots
    // Method to view available appointment slots with doctors
    public void viewAvailableAppointmentSlots() {
        loadAppointments();  // Load all the appointments from the CSV file

        System.out.println("\nAvailable Appointment Slots:");

        boolean hasAvailableSlots = false;
        int slotNumber = 1;  // Track the available slot numbers for easier selection later

        // Iterate through all appointments
        for (Appointment appointment : appointments.values()) {
            // Check if the appointment slot is available (i.e., its status is "Pending")
            if (appointment.getStatus().equalsIgnoreCase("Pending")) {
                System.out.println(slotNumber + ". Doctor: " + appointment.getDoctorName()
                        + " | Date: " + appointment.getDate()
                        + " | Time: " + appointment.getTime());
                slotNumber++;
                hasAvailableSlots = true;  // Mark that at least one available slot was found
            }
        }

        // If no available slots are found, display a message to the user
        if (!hasAvailableSlots) {
            System.out.println("No available appointment slots.");
        }
    }


    // Method to view scheduled appointments
    public void viewScheduledAppointments() {
        Appointment appointment = new Appointment();
        List<String[]> scheduledApptP = appointment.loadScheduledAppointment(this.hospitalID);
        System.out.printf("Your Scheduled Appointments(%s):\n",this.hospitalID);
        
        for(String[] row: scheduledApptP)
        {
        	System.out.printf("Appointment ID: %s | Doctor ID: %s | Doctor Name: Dr %s | Date: %s | Time: %s | Status: %s\n",
		    		row[0], row[1], row[2], row[3], row[4], row[5]);
        }
        
        if(scheduledApptP.isEmpty())
        {
        	System.out.println("No scheduled appointments found.");
        }
    }


    // Method to view past appointment outcome records
    // Method to view past appointment outcome records
    public void viewPastAppointmentOutcomes() {
    	Appointment appt = new Appointment();
    	List<String[]> pastAppointment = appt.ViewPastPatient(this.hospitalID);
    	System.out.printf("Your past appointments(%s):\n",this.hospitalID);
        
        for(String[] row: pastAppointment)
        {
        	System.out.printf("Appointment ID: %s | Doctor ID: %s | Doctor Name: Dr %s | Date: %s | Time: %s | Status: %s| Outcome: %s| Service: %s| Medication: %s | Notes: %s\n",
		    		row[0], row[2], row[3], row[4], row[5], row[6], row[7], row[8], row[9], row[10]);
        }
        
        if(pastAppointment.isEmpty())
        {
        	System.out.println("No past appointments found.");
        }
    }

    // Method to schedule an appointment

    public void scheduleAppointment() {
        Scanner sc = new Scanner(System.in);
        Appointment appointment = new Appointment();
        List<String[]> availableAppointmentP = appointment.loadAvailableAppointmentsPatient();
        
        for (String[] slot : availableAppointmentP) {
		    System.out.printf("Appointment ID: %s | Dr.: %s | Date: %s | Time: %s\n",
		    		slot[0], slot[4], slot[5], slot[6]);
		}
		if(availableAppointmentP.isEmpty())
		{
			System.out.println("No available slots found.");
		}
        
      //Book an Appointment
        System.out.println("Enter Appointment ID to book:");
        String selectedAppointmentID = sc.nextLine();
        
        //Find the doctor id in selected appointment
        String selectedDoctorID = null;
        for (String[] appt1 : availableAppointmentP) {
			if (appt1[0].equals(selectedAppointmentID)) {
				selectedDoctorID = appt1[1];  // Assuming DoctorID is at index 1
				break;
			}
		}
        
        if (selectedDoctorID == null) {
            System.out.println("Invalid Appointment ID selected.");
            return;
        }
        
//        if(!appointment.loadUpcomingAppointments(selectedDoctorID).isEmpty())
//        {
//        	System.out.println("You already have an existing appointment with this doctor");
//        	return;
//        }//checking if patient already have an appointment with the doctor
//        
        
        for (String[] appt1 : availableAppointmentP) {
			if (appt1[0].equals(selectedAppointmentID)) {
				selectedDoctorID = appt1[1];  // Assuming DoctorID is at index 1
				break;
			}
		}
        
        
        
        for (String[] bookAppt : availableAppointmentP) {
            if (bookAppt[0].equals(selectedAppointmentID) && "Available".equalsIgnoreCase(bookAppt[5])) {
            	bookAppt[1] = this.hospitalID;  // Set PatientID
            	bookAppt[5] = "Pending";  // Set status to Pending
                break;
            }
        }
        
        /*Booking of slot*/
    	List<String[]> patientBookSlot = new ArrayList<>();
      	try(BufferedReader br = new BufferedReader(new FileReader(APPOINTMENT_LIST_FILE_PATH)))
      	{
      		String line;
      		while((line = br.readLine()) != null)
      		{
      			String[] columns = line.split(",");
                  
                  if(columns[0].equals(selectedAppointmentID))
                  {
                	  columns[1] = this.hospitalID;
                	  columns[6] = "Pending";
                  }//end of if
                  patientBookSlot.add(columns);
      		}//end of while
      	}//end of try
   catch (FileNotFoundException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
      	
      	/*Writing to CSV*/
      	
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(APPOINTMENT_LIST_FILE_PATH))) {
            for (String[] row : patientBookSlot) {
                bw.write(String.join(",", row));
                bw.newLine();
                
            }
            System.out.println("Appointment booked successfully");
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


    // Method to reschedule an appointment
    public void rescheduleAppointment() {
    	cancelAppointment();
        Scanner sc = new Scanner(System.in);
        Appointment appointment = new Appointment();
        List<String[]> availableAppointmentP = appointment.loadAvailableAppointmentsPatient();
        
        for (String[] slot : availableAppointmentP) {
		    System.out.printf("Appointment ID: %s | Date: %s | Time: %s | Status: %s\n",
		    		slot[0], slot[3], slot[4], slot[5]);
		}
		if(availableAppointmentP.isEmpty())
		{
			System.out.println("No available slots found.");
		}
		
		//Book an Appointment
        System.out.println("Enter Appointment ID to book:");
        String selectedAppointmentID = sc.nextLine();
        
        for (String[] bookAppt : availableAppointmentP) {
            if (bookAppt[0].equals(selectedAppointmentID) && "Available".equalsIgnoreCase(bookAppt[5])) {
            	bookAppt[1] = this.hospitalID;  // Set PatientID
            	bookAppt[5] = "Pending";  // Set status to Pending
                //appointmentFound = true;
                break;
            }
        }
        
        /*Booking of slot*/
    	List<String[]> patientBookSlot = new ArrayList<>();
      	try(BufferedReader br = new BufferedReader(new FileReader(APPOINTMENT_LIST_FILE_PATH)))
      	{
      		String line;
      		while((line = br.readLine()) != null)
      		{
      			String[] columns = line.split(",");
                  
                  if(columns[0].equals(selectedAppointmentID))
                  {
                	  columns[1] = this.hospitalID;
                	  columns[6] = "Pending";
                  }//end of if
                  patientBookSlot.add(columns);
      		}//end of while
      	}//end of try
   catch (FileNotFoundException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
      	
      	/*Writing to CSV*/
      	
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(APPOINTMENT_LIST_FILE_PATH))) {
            for (String[] row : patientBookSlot) {
                bw.write(String.join(",", row));
                bw.newLine();
                
            }
            System.out.println("Appointment booked successfully");
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    // Method to cancel an appointment

    public void cancelAppointment() {
    	
        Scanner sc = new Scanner(System.in);
        Appointment appointment = new Appointment();
        List<String[]> scheduledApptPcancel = appointment.loadScheduledAppointment(this.hospitalID);
        viewScheduledAppointments();
        System.out.println("To cancel the appointment, enter the Appoinment ID:");
        String cancelId = sc.next();
        
     // Step 1: Validate the Appointment ID
        boolean found = false;
        for (String[] appointmentRow : scheduledApptPcancel) {
            if (appointmentRow[0].equals(cancelId)) {
                found = true;
                //appointmentRow[1] = ""; // Clear PatientID
                //appointmentRow[6] = "Available"; // Update status to Available
                break;
            }
        }

        if (!found) {
            System.out.println("Invalid Appointment ID. Please try again.");
            return;
        }
        
        // Step 2: Update the CSV file
        try (BufferedReader br = new BufferedReader(new FileReader(APPOINTMENT_LIST_FILE_PATH))) {
            List<String[]> allAppointments = new ArrayList<>();
            String line;

            // Read all rows from the CSV file
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",");
                // Check if this is the row to update
                if (columns[0].equals(cancelId) && columns[1].equals(this.hospitalID)) {
                    columns[1] = ""; // Clear PatientID
                    columns[6] = "Available"; // Update status to Available
                }
                allAppointments.add(columns); // Add the (updated or unchanged) row to the list
            }

            // Write all rows back to the CSV
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(APPOINTMENT_LIST_FILE_PATH))) {
                for (String[] row : allAppointments) {
                    bw.write(String.join(",", row));
                    bw.newLine();
                }
            }

            System.out.println("Appointment canceled and updated in the system.");
        } catch (IOException e) {
            System.err.println("Error updating the CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    public void submitFeedback() {
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter your feedback: ");
        String feedbackMessage = sc.nextLine();

        // Write feedback to CSV directly here
        //String feedbackFilePath = "C:\\path\\to\\your\\feedback.csv"; // Update to your file path
        
     // Create a Date object to get the current date and time
        Date now = new Date();

        // Define the desired format (12-hour format)
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");

        // Format the current time
        String formattedTime = sdf.format(now);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FEEDBACK_FILE_PATH, true))) {
            String line = this.name + "," + feedbackMessage + "," + formattedTime;
            bw.write(line);
            bw.newLine();
            System.out.println("Feedback submitted successfully.");
        } catch (IOException e) {
            System.err.println("Error writing feedback to file: " + e.getMessage());
        }
    }
    
    public void updateMedicalRecord(String diagnosis, String treatment) {
        this.pastDiagnoses += "; " + diagnosis;
        this.pastTreatments += "; " + treatment;
    }


    // Override the abstract method from User class to show patient-specific menu
    @Override
    public void showMenu() {
        Scanner sc = new Scanner(System.in);
        int choice;
        Appointment testAppointment = new Appointment();
        do {
            System.out.println("\n--- Patient Menu ---");
            System.out.println("1. View Medical Record");
            System.out.println("2. Update Personal Information");
            System.out.println("3. View Available Appointment Slots");
            System.out.println("4. Schedule an Appointment");
            System.out.println("5. Reschedule an Appointment");
            System.out.println("6. Cancel an Appointment");
            System.out.println("7. View Scheduled Appointments");
            System.out.println("8. View Past Appointment Outcome Records");
            System.out.println("9. Submit Feedback");
            System.out.println("10. Logout");

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
                case 1:
                    viewMedicalRecord();
                    break;
                case 2:
                    updateContactInfo();
                    break;
                case 3:
				    testAppointment.displayAvailableAppointmentsForPatient();
                    break;
                case 4:
                    scheduleAppointment();
                    break;
                case 5:
                    rescheduleAppointment();
                    break;
                case 6:
                    cancelAppointment();
                    break;
                case 7:
                    viewScheduledAppointments();
                    break;
                case 8:
                    viewPastAppointmentOutcomes();
                    break;
                case 9:
                	submitFeedback();
                    break;
                case 10:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 10);
    }
}
