package hospital_system;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymptomChecker {

    // Replace this with your Hugging Face API token
    private static final String API_TOKEN = "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";
    
    // Hugging Face model endpoint for disease prediction
    private static final String API_URL = "https://api-inference.huggingface.co/models/shanover/symps_disease_bert_v3_c41";

    // Disease names corresponding to label indices (this map should be in the same order as the labels in the model)
    private static final Map<String, String> DISEASE_LABELS = new HashMap<>();
    static {
        DISEASE_LABELS.put("LABEL_0", "Paroxysmal Positional Vertigo");
        DISEASE_LABELS.put("LABEL_1", "AIDS");
        DISEASE_LABELS.put("LABEL_2", "Acne");
        DISEASE_LABELS.put("LABEL_3", "Alcoholic hepatitis");
        DISEASE_LABELS.put("LABEL_4", "Allergy");
        DISEASE_LABELS.put("LABEL_5", "Arthritis");
        DISEASE_LABELS.put("LABEL_6", "Bronchial Asthma");
        DISEASE_LABELS.put("LABEL_7", "Cervical spondylosis");
        DISEASE_LABELS.put("LABEL_8", "Chicken pox");
        DISEASE_LABELS.put("LABEL_9", "Chronic cholestasis");
        DISEASE_LABELS.put("LABEL_10", "Common Cold");
        DISEASE_LABELS.put("LABEL_11", "Dengue");
        DISEASE_LABELS.put("LABEL_12", "Diabetes");
        DISEASE_LABELS.put("LABEL_13", "Dimorphic hemorrhoids (piles)");
        DISEASE_LABELS.put("LABEL_14", "Drug Reaction");
        DISEASE_LABELS.put("LABEL_15", "Fungal infection");
        DISEASE_LABELS.put("LABEL_16", "GERD");
        DISEASE_LABELS.put("LABEL_17", "Gastroenteritis");
        DISEASE_LABELS.put("LABEL_18", "Heart attack");
        DISEASE_LABELS.put("LABEL_19", "Hepatitis B");
        DISEASE_LABELS.put("LABEL_20", "Hepatitis C");
        DISEASE_LABELS.put("LABEL_21", "Hepatitis D");
        DISEASE_LABELS.put("LABEL_22", "Hepatitis E");
        DISEASE_LABELS.put("LABEL_23", "Hypertension");
        DISEASE_LABELS.put("LABEL_24", "Hyperthyroidism");
        DISEASE_LABELS.put("LABEL_25", "Hypoglycemia");
        DISEASE_LABELS.put("LABEL_26", "Hypothyroidism");
        DISEASE_LABELS.put("LABEL_27", "Impetigo");
        DISEASE_LABELS.put("LABEL_28", "Jaundice");
        DISEASE_LABELS.put("LABEL_29", "Malaria");
        DISEASE_LABELS.put("LABEL_30", "Migraine");
        DISEASE_LABELS.put("LABEL_31", "Osteoarthritis");
        DISEASE_LABELS.put("LABEL_32", "Paralysis (brain hemorrhage)");
        DISEASE_LABELS.put("LABEL_33", "Peptic ulcer disease");
        DISEASE_LABELS.put("LABEL_34", "Pneumonia");
        DISEASE_LABELS.put("LABEL_35", "Psoriasis");
        DISEASE_LABELS.put("LABEL_36", "Tuberculosis");
        DISEASE_LABELS.put("LABEL_37", "Typhoid");
        DISEASE_LABELS.put("LABEL_38", "Urinary tract infection");
        DISEASE_LABELS.put("LABEL_39", "Varicose veins");
        DISEASE_LABELS.put("LABEL_40", "Hepatitis A");
    }

    // Send symptom data to API and return the response
    public static String sendSymptomDataToApi(String symptoms) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            // Open connection to the API URL
            URL url = new URL(API_URL);
            connection = (HttpURLConnection) url.openConnection();
            
            // Set HTTP method to POST
            connection.setRequestMethod("POST");
            
            // Set the Authorization header with your Hugging Face API token
            connection.setRequestProperty("Authorization", "Bearer " + API_TOKEN);
            
            // Enable input and output streams
            connection.setDoOutput(true);
            
            // Prepare the request body with symptoms data
            String jsonBody = "{\"inputs\": \"" + symptoms + "\"}";

            // Send the request
            connection.getOutputStream().write(jsonBody.getBytes());

            int responseCode = connection.getResponseCode();
            
            if (responseCode != 200) {
                // Read the error response to provide more context
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                StringBuilder errorResponse = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    errorResponse.append(line);
                }
                System.out.println("Error Response: " + errorResponse.toString());
                throw new Exception("Failed to connect to the API. Response Code: " + responseCode);
            }

            // Read the response from Hugging Face
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // Return the response as a string
            return response.toString();

        } catch (MalformedURLException e) {
            System.out.println("Invalid URL: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O error during API call: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
        return null;
    }

    // Process the API response
    public static void processResponse(String response) {
        try {
            
            try {
                // Parse the outer array (it’s a JSON array containing a single array)
                JSONArray outerArray = new JSONArray(response);

                // Extract the first element of the array, which is the list of predictions
                JSONArray predictions = outerArray.getJSONArray(0);

                // Convert the JSONArray to a List of JSONObject
                List<JSONObject> predictionList = new ArrayList<>();
                for (int i = 0; i < predictions.length(); i++) {
                    // Cast each element to JSONObject
                    predictionList.add(predictions.getJSONObject(i));
                }

                // Sort the list based on the prediction score in descending order
                predictionList.sort(new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject a, JSONObject b) {
                        return Double.compare(b.getDouble("score"), a.getDouble("score"));
                    }
                });

                // Display the top 2 predictions
                for (int i = 0; i < Math.min(2, predictionList.size()); i++) {
                    JSONObject prediction = predictionList.get(i);
                    String label = prediction.getString("label");  // Get the label as a string
                    double score = prediction.getDouble("score");

                    // Get the disease name from the label string
                    String diseaseName = DISEASE_LABELS.get(label);

                    // Display the disease prediction and score
                    System.out.println("Predicted Disease: " + diseaseName);
                    //System.out.println("Prediction Score: " + score);
                }
            } catch (Exception e) {
                // Handle the case where the response is not as expected
                System.out.println("Failed to process response: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Failed to process response: " + e.getMessage());
        }
    }

  
   
}
