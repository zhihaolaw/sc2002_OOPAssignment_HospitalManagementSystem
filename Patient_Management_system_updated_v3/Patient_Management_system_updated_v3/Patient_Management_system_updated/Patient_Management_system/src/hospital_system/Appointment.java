package hospital_system;

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
}
