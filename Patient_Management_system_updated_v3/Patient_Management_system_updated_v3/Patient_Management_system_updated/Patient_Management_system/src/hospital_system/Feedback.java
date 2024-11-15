package hospital_system;

public class Feedback {
	
	
    private String patientName;
    private String feedbackMessage;
    private String timeStamp;

    public Feedback(String patientName, String feedbackMessage,String timeStamp) {
        this.timeStamp = timeStamp;
        this.patientName = patientName;
        this.feedbackMessage = feedbackMessage;
    }

    public String getPatientID() {
        return timeStamp;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getFeedbackMessage() {
        return feedbackMessage;
    }

}
