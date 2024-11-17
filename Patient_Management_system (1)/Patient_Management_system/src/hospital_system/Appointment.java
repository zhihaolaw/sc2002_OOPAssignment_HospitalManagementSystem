package hospital_system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Appointment {

    private String appointmentID;
    private String patientID;
    private String doctorID;
    private String doctorName;  // Add doctor name
    private String date;
    private String time;
    private String status;  // For example: "Confirmed", "Scheduled", "Rescheduled", "Cancelled"
    private String outcome;  // For example: "Pending", "Consultation Completed", "Follow-up Required"
    private String service;  // Services provided during the appointment
    private String medication;  // Prescribed medications
    private String notes;  // Consultation notes

    
    private static final String APPOINTMENTS_FILE_PATH = FilePaths.getAppointmentFilePath();

    
    private static final List<String[]> appointmentsList = new ArrayList<>();
    private static final Map<String, Appointment> appointmentsHash = new HashMap<>();
    private static final Map<String, Appointment> availableAppointmentsHash = new HashMap<>();
    private static Map<String, Appointment> appointments = new HashMap<>();




    // Constructor to include the new fields
    public Appointment(String appointmentID, String patientID, String doctorID, String doctorName, String date, String time, String status, String outcome, String service, String medication, String notes) {
        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.doctorName = doctorName;  // New field
        this.date = date;
        this.time = time;
        this.status = status;
        this.outcome = outcome;
        this.service = service;  // New field
        this.medication = medication;  // New field
        this.notes = notes;  // New field
    }
    
//    public Appointment(String doctorID, String doctorName, String date, String time, String status) {
//        this.doctorID = doctorID;
//        this.doctorName = doctorName;  // New field
//        this.date = date;
//        this.time = time;
//        this.status = status;
//    }
    
    public Appointment() {
    	
    }

    // Getter and Setter methods for each field
    public String getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Optional: Override the toString method to display appointment details in a readable format
    @Override
    public String toString() {
        return "Appointment ID: " + appointmentID +
                ", Patient ID: " + patientID +
                ", Doctor ID: " + doctorID +
                ", Doctor Name: " + doctorName +  // Include doctor name
                ", Date: " + date +
                ", Time: " + time +
                ", Status: " + status +
                ", Outcome: " + outcome +
                ", Service: " + service +  // Include service
                ", Medication: " + medication +  // Include medication
                ", Notes: " + notes;  // Include consultation notes
    }
    
    public static void loadAppointments() {
        appointmentsList.clear();
        appointmentsList.addAll(CSV_Handler.loadFromCSV(APPOINTMENTS_FILE_PATH));

    }

    // Method to view details of all appointments
    public void viewAppointments() {
        System.out.println("Viewing all appointments...");
        loadAppointments();
        if (appointmentsList.isEmpty()) {
            System.out.println("No appointments found.");
        } else {
            for (String[] appointment : appointmentsList) {
                System.out.printf("Appointment ID: %s | Patient ID: %s | Doctor ID: %s | Date: %s | Time: %s | Status: %s\n",
                        appointment[0], appointment[1], appointment[2], appointment[4], appointment[5], appointment[6]);
            }
        }
    }
    
    private Appointment getAppointment(String[] data, String appointmentID) {
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

    
    public List<String[]> loadAvailableAppointmentsDoc(String DoctorID) {
    	
    	List<String[]> availableSlotsDoc = new ArrayList<>();
      boolean hasAvailableSlots = false;
    	
    	
    	try(BufferedReader br = new BufferedReader(new FileReader(APPOINTMENTS_FILE_PATH)))
    	{
    		String line;
    		while((line = br.readLine()) != null)
    		{
    			String[] columns = line.split(",");
                String appointmentDoctorID = columns[2];
                String status = columns[6];
                
                if(appointmentDoctorID.equals(DoctorID)&& status.equals("Available"))
                {
                	availableSlotsDoc.add(new String[]{
                            columns[0], // AppointmentID
                            columns[2], // DoctorID
                            columns[3], // Doctor Name
                            columns[4], // Date
                            columns[5], // Time
                            columns[6]  // Status
                        });
                	hasAvailableSlots = true;
                }//end of if
    		}//end of while
    	}//end of try
 catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return availableSlotsDoc;
    	
    }
    
    public List<String[]> loadAvailableAppointmentsPatient()  {
    	
      List<String[]> availableSlotsPatient = new ArrayList<>();
      boolean hasAvailableSlots = false;
    	
    	
    	try(BufferedReader br = new BufferedReader(new FileReader(APPOINTMENTS_FILE_PATH)))
    	{
    		String line;
    		while((line = br.readLine()) != null)
    		{
    			String[] columns = line.split(",");
                String appointmentDoctorID = columns[2];
                String status = columns[6];
                
                if(status.equals("Available"))
                {
                	availableSlotsPatient.add(new String[]{
                            columns[0], // AppointmentID
                            columns[1], //patient ID
                            columns[2], // DoctorID
                            columns[3], // Doctor Name
                            columns[4], // Date
                            columns[5], // Time
                            columns[6]  // Status
                        });
                	hasAvailableSlots = true;
                }//end of if
    		}//end of while
    	}//end of try
 catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return availableSlotsPatient;
    	
    }
    
    
    public List<String[]> loadUpcomingAppointments(String DoctorID) {
    	
    	List<String[]> scheduleSlots = new ArrayList<>();
        boolean hasAppointments = false;
    	
    	
    	try(BufferedReader br = new BufferedReader(new FileReader(APPOINTMENTS_FILE_PATH)))
    	{
    		String line;
    		while((line = br.readLine()) != null)
    		{
    			String[] columns = line.split(",");
                String appointmentDoctorID = columns[2];
                String status = columns[6];
                
                if(appointmentDoctorID.equals(DoctorID)&& status.equals("Scheduled"))
                {
                	scheduleSlots.add(new String[]{
                            columns[0], // AppointmentID
                            columns[1], // PatientID
                            columns[2], // DoctorID
                            columns[3], // Doctor Name
                            columns[4], // Date
                            columns[5], // Time
                            columns[6] // Status

                        });
                	hasAppointments = true;
                }//end of if
    		}//end of while
    	}//end of try
 catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	return scheduleSlots;
    }
    
    public List<String[]> loadPendingAppointments(String DoctorID) {
    	
    	List<String[]> PendingSlots = new ArrayList<>();
        boolean hasAppointments = false;
    	
    	
    	try(BufferedReader br = new BufferedReader(new FileReader(APPOINTMENTS_FILE_PATH)))
    	{
    		String line;
    		while((line = br.readLine()) != null)
    		{
    			String[] columns = line.split(",");
                String appointmentDoctorID = columns[2];
                String status = columns[6];
                
                if(appointmentDoctorID.equals(DoctorID)&& status.equals("Pending"))
                {
                	PendingSlots.add(new String[]{
                            columns[0], // AppointmentID
                            columns[1], // PatientID
                            columns[2], // DoctorID
                            columns[3], // Doctor Name
                            columns[4], // Date
                            columns[5], // Time
                            columns[6], // Status
                            columns[7],
                            columns[8],
                            columns[9],
                            columns[10],
                        });
                	hasAppointments = true;
                }//end of if
    		}//end of while
    	}//end of try
    	catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	return PendingSlots;
    }
    
public List<String[]> loadScheduledAppointments(String DoctorID) {
    	
    	List<String[]> ScheduledSlots = new ArrayList<>();
        boolean hasAppointments = false;
    	
    	
    	try(BufferedReader br = new BufferedReader(new FileReader(APPOINTMENTS_FILE_PATH)))
    	{
    		String line;
    		while((line = br.readLine()) != null)
    		{
    			String[] columns = line.split(",");
                String appointmentDoctorID = columns[2];
                String status = columns[6];
                
                if(appointmentDoctorID.equals(DoctorID)&& status.equals("Scheduled"))
                {
                	ScheduledSlots.add(new String[]{
                            columns[0], // AppointmentID
                            columns[1], // PatientID
                            columns[2], // DoctorID
                            columns[3], // Doctor Name
                            columns[4], // Date
                            columns[5], // Time
                            columns[6], // Status
                            columns[7],
                            columns[8],
                            columns[9],
                            columns[10]
                        });
                	hasAppointments = true;
                }//end of if
    		}//end of while
    	}//end of try
    	catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	return ScheduledSlots;
    }
    
    
    public List<String[]> loadScheduledAppointment(String PatientID)
    {
        List<String[]> scheduledSlotsPatient = new ArrayList<>();
        //boolean hasAvailableSlots = false;
      	
      	
      	try(BufferedReader br = new BufferedReader(new FileReader(APPOINTMENTS_FILE_PATH)))
      	{
      		String line;
      		while((line = br.readLine()) != null)
      		{
      			String[] columns = line.split(",");
                  String patientId = columns[1];
                  String status = columns[6];
                  
                  if(patientId.equals(PatientID) && status.equals("Scheduled"))
                  {
                	  scheduledSlotsPatient.add(new String[]{
                              columns[0], // AppointmentID
                              columns[2], // DoctorID
                              columns[3], // Doctor Name
                              columns[4], // Date
                              columns[5], // Time
                              columns[6] // Status
                          });
                  	//hasAvailableSlots = true;
                  }//end of if
      		}//end of while
      	}//end of try
   catch (FileNotFoundException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
      	
      	return scheduledSlotsPatient;
    }
    
    public List<String[]> loadScheduledAppointmentDoc(String DoctorID)
    {
        List<String[]> scheduledSlotDoc= new ArrayList<>();
        //boolean hasAvailableSlots = false;
      	
      	
      	try(BufferedReader br = new BufferedReader(new FileReader(APPOINTMENTS_FILE_PATH)))
      	{
      		String line;
      		while((line = br.readLine()) != null)
      		{
      			String[] columns = line.split(",");
                  String docId = columns[2];
                  String status = columns[6];
                  
                  if(docId.equals(DoctorID) && status.equals("Scheduled"))
                  {
                	  scheduledSlotDoc.add(new String[]{
                              columns[0], // AppointmentID
                              columns[1], // PatientID
                              columns[2], // DoctorID
                              columns[4], // Date
                              columns[5], // Time
                              columns[6] // Status
                          });
                  	//hasAvailableSlots = true;
                  }//end of if
      		}//end of while
      	}//end of try
   catch (FileNotFoundException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
      	
      	return scheduledSlotDoc;
    }
    
    public List<String[]> getPatientListByAppointment(String DoctorID)
    {
        List<String[]> gplba= new ArrayList<>();
        //boolean hasAvailableSlots = false;
      	
      	
      	try(BufferedReader br = new BufferedReader(new FileReader(APPOINTMENTS_FILE_PATH)))
      	{
      		String line;
      		while((line = br.readLine()) != null)
      		{
      			String[] columns = line.split(",");
                  String docId = columns[2];
                  String status = columns[6];
                  
                  if(docId.equals(DoctorID) && (status.equals("Scheduled")|| status.equals("Completed")))
                  {
                	  gplba.add(new String[]{
                			  columns[0], // AppointmentID
                              columns[1], // PatientID
                              columns[2], // DoctorID
                              columns[3], // Doctor Name
                              columns[4], // Date
                              columns[5], // Time
                              columns[6], // Status
                              columns[7],
                              columns[8],
                              columns[9],
                              columns[10]
                          });
                  	//hasAvailableSlots = true;
                  }//end of if
      		}//end of while
      	}//end of try
   catch (FileNotFoundException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
      	
      	return gplba;
    }
    
    public List<String[]> ViewPastPatient(String PatientID)
    {
        List<String[]> viewPast= new ArrayList<>();
        //boolean hasAvailableSlots = false;
      	
      	
      	try(BufferedReader br = new BufferedReader(new FileReader(APPOINTMENTS_FILE_PATH)))
      	{
      		String line;
      		while((line = br.readLine()) != null)
      		{
      			String[] columns = line.split(",");
                  String patientID = columns[1];
                  String status = columns[6];
                  
                  if(patientID.equals(PatientID) && status.equals("Completed"))
                  {
                	  viewPast.add(new String[]{
                              columns[0], // AppointmentID
                              columns[1], // PatientID
                              columns[2], // DoctorID
                              columns[3], // Doctor Name
                              columns[4], // Date
                              columns[5], // Time
                              columns[6], // Status
                              columns[7], // Outcome
                              columns[8], // Service
                              columns[9], // Medication
                              columns[10] // Notes
                          });
                  	//hasAvailableSlots = true;
                  }//end of if
      		}//end of while
      	}//end of try
   catch (FileNotFoundException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
      	
      	return viewPast;
    }
    
    public List<String[]> loadAllRecordByApptId(String apptID)
    {
        List<String[]> recordAppt= new ArrayList<>();
        //boolean hasAvailableSlots = false;
      	
      	
      	try(BufferedReader br = new BufferedReader(new FileReader(APPOINTMENTS_FILE_PATH)))
      	{
      		String line;
      		while((line = br.readLine()) != null)
      		{
      			String[] columns = line.split(",");
                  String apptid = columns[0];
                  String status = columns[6];
                  
                  if(apptid.equals(apptID))
                  {
                	  recordAppt.add(new String[]{
                              columns[0], // AppointmentID
                              columns[6], // Status
                              columns[7], // Outcome
                              columns[8], // Service
                              columns[9], // Medication
                              columns[10], // Notes
                          });
                  	//hasAvailableSlots = true;
                  }//end of if
      		}//end of while
      	}//end of try
   catch (FileNotFoundException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
      	
      	return recordAppt;
    }
    
    public void updateAppointmentOutcome(String appointmentID, String outcome, String service, String medication, String notes) {
        List<String[]> allRecords = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(APPOINTMENTS_FILE_PATH))) {
            String line;
            boolean recordFound = false;

            // Read all records into memory
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",");
                
                // Ensure the row has the expected number of columns before proceeding
                if (columns.length < 11) {
                    System.err.println("Skipping malformed row: " + line);
                    allRecords.add(columns);
                    continue;
                }

                // Check if this is the record to update
                if (columns[0].equals(appointmentID)) {
                    columns[6] = "Completed";  // Update status to Completed
                    columns[7] = outcome;     // Update outcome
                    columns[8] = service;     // Update service
                    columns[9] = medication;  // Update medication
                    columns[10] = notes;      // Update notes
                    recordFound = true;
                }

                allRecords.add(columns);
            }

            if (!recordFound) {
                System.out.println("Appointment ID not found in the CSV.");
                return;
            }
        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // Write the updated records back to the CSV file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(APPOINTMENTS_FILE_PATH))) {
            for (String[] record : allRecords) {
                bw.write(String.join(",", record));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to the CSV file: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Appointment outcome successfully updated.");
    }

    
    public void displayAvailableAppointmentsForPatient() {
        List<String[]> availableSlots = loadAvailableAppointmentsPatient();
        if (availableSlots.isEmpty()) {
            System.out.println("No available slots found.");
        } else {
            for (String[] slot : availableSlots) {
                System.out.printf("Appointment ID: %s | Date: %s | Time: %s | Doctor: %s | Status: %s\n",
                        slot[0], slot[3], slot[4], slot[2], slot[5]);
            }
        }
    }

    public void displayUpcomingAppointmentsForDoctor(String doctorID) throws IOException {
        List<String[]> upcomingAppointments = loadUpcomingAppointments(doctorID);
        if (upcomingAppointments.isEmpty()) {
            System.out.println("No upcoming appointments found.");
        } else {
            for (String[] appointment : upcomingAppointments) {
                System.out.printf("Appointment ID: %s | Patient ID: %s | Date: %s | Time: %s | Status: %s\n",
                        appointment[0], appointment[1], appointment[4], appointment[5], appointment[6]);
            }
        }
    }
    
    public void updatePatientMedicalRecord(String appointmentID, String diagnosis, String treatment) {
        try {
            // Load the appointment data
            loadAppointments();

            // Find the specific appointment
            Appointment appointment = appointmentsHash.get(appointmentID);
            if (appointment == null) {
                System.out.println("Appointment not found.");
                return;
            }

            String patientID = appointment.getPatientID();
            if (patientID == null || patientID.isEmpty()) {
                System.out.println("No patient linked to this appointment.");
                return;
            }

            // Load the patient data
            File patientsFile = new File(FilePaths.getPatientListFilePath());
            List<String> updatedPatients = new ArrayList<>();
            boolean recordUpdated = false;

            try (BufferedReader br = new BufferedReader(new FileReader(patientsFile))) {
                String line;

                // Process each line
                while ((line = br.readLine()) != null) {
                    String[] columns = line.split(",");
                    if (columns.length < 12) {
                        updatedPatients.add(line); // Add invalid lines as is
                        continue;
                    }

                    if (columns[0].equals(patientID)) {
                        // Update the patient's medical record
                        columns[10] += "; " + diagnosis; // Append diagnosis
                        columns[11] += "; " + treatment; // Append treatment
                        recordUpdated = true;
                    }

                    // Add updated or unchanged patient record to the list
                    updatedPatients.add(String.join(",", columns));
                }
            }

            // Save the updated patient data
            if (recordUpdated) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(patientsFile))) {
                    for (String updatedLine : updatedPatients) {
                        bw.write(updatedLine);
                        bw.newLine();
                    }
                    System.out.println("Patient medical record updated successfully.");
                }
            } else {
                System.out.println("Patient ID not found in the records.");
            }
        } catch (IOException e) {
            System.out.println("Error updating medical record: " + e.getMessage());
        }
    }
    
    public static Appointment getAppointmentByID(String appointmentID) {
        return appointmentsHash.get(appointmentID);
    }

 
    public void setOutcomeDetails(String service, String medication, String notes) {
        this.status = "Completed";
        this.outcome = "Consultation Completed";
        this.service = service;
        this.medication = medication;
        this.notes = notes;
    }
    

    public void updateOutcomeRecord(String status, String outcome, String service, String medication, String notes) {
        this.status = status;
        this.outcome = outcome;
        this.service = service;
        this.medication = medication;
        this.notes = notes;
    }

    
    
}
