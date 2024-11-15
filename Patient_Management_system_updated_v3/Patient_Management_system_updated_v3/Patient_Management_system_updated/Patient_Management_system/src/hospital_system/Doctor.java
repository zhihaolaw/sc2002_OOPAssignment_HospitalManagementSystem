package hospital_system;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class Doctor extends User {

    private String age;
    private String gender;
    private String availability;
    
    private static final String APPOINTMENTS_FILE_PATH = "C:\\Users\\zhiha\\Downloads\\Patient_Management_system_updated\\Patient_Management_system\\data\\appointments.csv";
    private static final String PATIENTS_FILE_PATH = "C:\\Users\\zhiha\\Downloads\\Patient_Management_system_updated\\Patient_Management_system\\data\\Patient_List.csv";

    // Store appointments loaded from appointments.txt
    private static Map<String, Appointment> appointments = new HashMap<>();
    private static Map<String, Patient> patients = new HashMap<>(); // a global patient list

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
                        appointment.getStatus(),
                        appointment.getOutcome() != null ? appointment.getOutcome() : "",  // Handle null outcome
                        appointment.getService(),
                        appointment.getMedication() != null ? appointment.getMedication() : "",  // Handle null medication
                        appointment.getNotes() != null ? appointment.getNotes() : ""  // Handle null notes
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



    // Save the updated patient data back to file
    // Save the updated patient data back to the CSV file without reloading or redundant operations



    /*private void savePatientData() {
        File file = new File(PATIENTS_FILE_PATH);
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            // Write the header
            writer.println("Patient ID,Name,Date of Birth,Gender,Blood Type,Phone Number,Email Address,Past Diagnosis,Past Treatments");

            // Loop through the patients map to write data for each patient
            for (Patient patient : patients.values()) {
                // Write all fields but update only the Past Diagnosis and Past Treatments
                writer.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                        patient.getPatientID(),              // Patient ID
                        patient.getName(),                   // Name
                        patient.getDob(),                    // Date of Birth
                        patient.getGender(),                 // Gender
                        patient.getBloodType(),              // Blood Type (unchanged)
                        patient.getContactNumber() != null ? patient.getContactNumber() : "",  // Phone Number (unchanged)
                        patient.getEmail() != null ? patient.getEmail() : "",  // Email Address (unchanged)
                        patient.getDiagnoses() != null ? patient.getDiagnoses() : "",  // Updated Past Diagnosis
                        patient.getTreatments() != null ? patient.getTreatments() : ""  // Updated Past Treatments
                );
            }
            System.out.println("Patient data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving patient data: " + e.getMessage());
        }
    }*/
    
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



    // View a patient's medical record
    public void viewPatientMedicalRecords() {
        loadAppointments();
        loadPatientData();
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Appointment ID to view patient record: ");
        String appointmentID = sc.nextLine();

        if (appointments.containsKey(appointmentID)) {
            Appointment appointment = appointments.get(appointmentID);
            String patientID = appointment.getPatientID();

            if (patients.containsKey(patientID)) {
                Patient patient = patients.get(patientID);
                System.out.println("Patient Medical Record:");
                System.out.println("Name: " + patient.getName());
                System.out.println("DOB: " + patient.getDob());
                System.out.println("Blood Type: " + patient.getBloodType());
                System.out.println("Diagnoses: " + patient.getDiagnoses());
                System.out.println("Treatments: " + patient.getTreatments());
            } else {
                System.out.println("Patient not found.");
            }
        } else {
            System.out.println("Appointment not found.");
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
        loadAppointments();
        Scanner sc = new Scanner(System.in);

        // Check if any appointments are loaded
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
            return;
        }

        boolean hasPending = false;
        for (Appointment appointment : appointments.values()) {
            // Check the appointment details
            System.out.println("Checking appointment: " + appointment.getAppointmentID() + ", Doctor ID: " + appointment.getDoctorID() + ", Status: " + appointment.getStatus());

            // Check if the doctor ID matches and the appointment is pending
            if (appointment.getDoctorID().equals(this.hospitalID) && appointment.getStatus().equals("Pending")) {
                hasPending = true;
                System.out.println("Appointment ID: " + appointment.getAppointmentID() + " | Patient ID: " + appointment.getPatientID() + " | Status: " + appointment.getStatus());
                System.out.print("Do you want to accept this appointment? (y/n): ");
                String decision = sc.nextLine();

                if (decision.equalsIgnoreCase("y")) {
                    appointment.setStatus("Confirmed");  // Updated to "Confirmed"
                    System.out.println("Appointment confirmed.");
                } else {
                    appointment.setStatus("Cancelled");  // Updated to "Cancelled"
                    System.out.println("Appointment cancelled.");
                }

                // Save the updated appointment status
                saveAppointments();
            }
        }

        if (!hasPending) {
            System.out.println("No pending appointment requests.");
        }
    }



    // View upcoming appointments for the doctor

    public void viewUpcomingAppointments() {
        loadAppointments();
        loadPatientData(); // Load patient data to get patient details

        // Check if any appointments are loaded
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
            return;
        }

        boolean hasUpcoming = false;
        System.out.println("Upcoming Confirmed Appointments:");

        // Iterate through appointments to find upcoming confirmed ones
        for (Appointment appointment : appointments.values()) {
            // Show upcoming appointments that are confirmed and not completed
            if (appointment.getDoctorID().equals(this.hospitalID) && appointment.getStatus().equalsIgnoreCase("Confirmed")) {
                hasUpcoming = true;
                String patientID = appointment.getPatientID();
                String patientDetails = "";

                // Check if patient details exist for the appointment
                if (patientID != null && patients.containsKey(patientID)) {
                    Patient patient = patients.get(patientID);
                    patientDetails = " | Patient Name: " + patient.getName();
                } else {
                    patientDetails = " | Patient ID: " + (patientID != null ? patientID : "Unknown");
                }

                // Display the appointment with patient details and appointment time
                System.out.println("Appointment ID: " + appointment.getAppointmentID() +
                        patientDetails +
                        " | Date: " + appointment.getDate() +
                        " | Time: " + appointment.getTime() +
                        " | Status: " + appointment.getStatus());
            }
        }

        if (!hasUpcoming) {
            System.out.println("No upcoming confirmed appointments found.");
        }
    }


    // Record Appointment Outcome
    public void recordAppointmentOutcome() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Recording appointment outcome...");
        loadAppointments();

        System.out.print("Enter Appointment ID: ");
        String appointmentID = sc.nextLine();

        if (appointments.containsKey(appointmentID)) {
            Appointment appointment = appointments.get(appointmentID);

            // Ensure the appointment is confirmed before recording an outcome
            if (appointment.getStatus().equalsIgnoreCase("Confirmed")) {
                System.out.println("Current appointment status: " + appointment.getStatus());
                System.out.print("Enter the outcome (e.g., Consultation Completed, Follow-up Needed): ");
                String outcome = sc.nextLine().trim();

                // Update the appointment's outcome and status
                appointment.setOutcome(outcome);
                appointment.setStatus("Completed");

                // Save the updated appointments back to the file
                saveAppointments();
                System.out.println("Appointment outcome recorded successfully.");
            } else {
                System.out.println("Error: Only confirmed appointments can have an outcome recorded.");
            }
        } else {
            System.out.println("Appointment not found.");
        }
    }

    // Method to set or update doctor's availability

    public void setAvailability() {
        loadAppointments();  // Load appointments from the appointments.csv file
        Scanner sc = new Scanner(System.in);

        System.out.println("Current available slots:");

        boolean hasAvailableSlots = false;
        Map<Integer, Appointment> availableSlots = new HashMap<>();
        int slotNumber = 1;

        // Display current available ("Pending") slots for the doctor
        for (Appointment appointment : appointments.values()) {
            if (appointment.getDoctorID().equals(this.hospitalID) && appointment.getStatus().equalsIgnoreCase("Pending") && (appointment.getPatientID() == null || appointment.getPatientID().isEmpty())) {
                System.out.println(slotNumber + ". Date: " + appointment.getDate() + " | Time: " + appointment.getTime());
                availableSlots.put(slotNumber, appointment);  // Store available slots with their corresponding numbers
                slotNumber++;
                hasAvailableSlots = true;
            }
        }

        if (!hasAvailableSlots) {
            System.out.println("No available slots found for modification.");
            return;
        }

        // Allow the doctor to select a slot to update its availability
        System.out.print("Enter the number corresponding to the slot you want to modify, or '0' to cancel: ");
        int selectedSlot = sc.nextInt();
        sc.nextLine();  // Consume newline

        if (selectedSlot == 0) {
            System.out.println("No changes made.");
            return;
        }

        if (!availableSlots.containsKey(selectedSlot)) {
            System.out.println("Invalid selection.");
            return;
        }

        // Get the selected appointment and modify its availability
        Appointment selectedAppointment = availableSlots.get(selectedSlot);

        System.out.print("Do you want to mark this slot as unavailable? (y/n): ");
        String choice = sc.nextLine().trim();

        if (choice.equalsIgnoreCase("y")) {
            selectedAppointment.setStatus("Unavailable");
            System.out.println("Slot has been marked as unavailable.");
        } else {
            selectedAppointment.setStatus("Pending");  // Keep it as available (Pending)
            System.out.println("Slot remains available.");
        }

        // Save the updated appointments back to the appointments.csv file
        saveAppointments();
    }



    // Method to view the doctor's personal schedule


    public void viewSchedule() {
        System.out.println("Viewing your schedule...");
        loadAppointments();  // Load appointments from the CSV file
        boolean hasAppointments = false;
        boolean hasAvailableSlots = false;

        System.out.println("\nYour Upcoming Appointments:");
        for (Appointment appointment : appointments.values()) {
            if (appointment.getDoctorID().equals(this.hospitalID)) {
                if (appointment.getStatus().equalsIgnoreCase("Scheduled")) {
                    // Show only scheduled appointments
                    System.out.println("Appointment ID: " + appointment.getAppointmentID() +
                            " | Patient ID: " + appointment.getPatientID() +
                            " | Date: " + appointment.getDate() +
                            " | Time: " + appointment.getTime() +
                            " | Status: " + appointment.getStatus());
                    hasAppointments = true;
                }
            }
        }

        if (!hasAppointments) {
            System.out.println("No upcoming appointments found.");
        }

        System.out.println("\nYour Available Slots:");
        for (Appointment appointment : appointments.values()) {
            if (appointment.getDoctorID().equals(this.hospitalID)) {
                if (appointment.getStatus().equalsIgnoreCase("Pending") && (appointment.getPatientID() == null || appointment.getPatientID().isEmpty())) {
                    // Show available slots (Pending and no assigned patient)
                    System.out.println("Slot ID: " + appointment.getAppointmentID() +
                            " | Date: " + appointment.getDate() +
                            " | Time: " + appointment.getTime() +
                            " | Status: Available");
                    hasAvailableSlots = true;
                }
            }
        }

        if (!hasAvailableSlots) {
            System.out.println("No available slots found.");
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
                case 1:
                    viewPatientMedicalRecords();
                    break;
                case 2:
                    updatePatientMedicalRecords();
                    break;
                case 3:
                    viewSchedule();
                    break;
                case 4:
                    setAvailability();
                    break;
                case 5:
                    manageAppointmentRequests();
                    break;
                case 6:
                    viewUpcomingAppointments();
                    break;
                case 7:
                    recordAppointmentOutcome();
                    break;
                case 8:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 8);
    }
}
