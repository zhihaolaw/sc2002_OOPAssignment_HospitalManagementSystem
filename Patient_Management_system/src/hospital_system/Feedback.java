package hospital_system;

import java.util.ArrayList;
import java.util.List;

public class Feedback {
	
	
    private String patientName;
    private String feedbackMessage;
    private String timeStamp;
    private static final String FEEDBACK_FILE_PATH = FilePaths.getFeedbackFilePath();

    private static final List<String[]> feedbackList = new ArrayList<>();

    public Feedback(String patientName, String feedbackMessage,String timeStamp) {
        this.timeStamp = timeStamp;
        this.patientName = patientName;
        this.feedbackMessage = feedbackMessage;
    }
    
    //default con
    public Feedback() {
    	
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
    
    
    
    
    private static void loadFeedback() {
    	feedbackList.clear();
    	feedbackList.addAll(CSV_Handler.loadFromCSV(FEEDBACK_FILE_PATH));
    }

    // Method to view details of all appointments
    public void viewFeedback() {
        System.out.println("Viewing all Feedback...");
        loadFeedback();
        if (feedbackList.isEmpty()) {
            System.out.println("No Feedback found.");
        } else {
            for (String[] f : feedbackList) {
                System.out.printf("Patient Name: %s | Feedback: %s | Time: %s\n",
                        f[0], f[1], f[2]);
            }
        }
    }

}
