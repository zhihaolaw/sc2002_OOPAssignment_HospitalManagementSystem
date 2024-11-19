package hospital_system;


import okhttp3.*;
import java.io.IOException;

public class TelegramBotService {
    private static final String BOT_TOKEN = "7980566120:AAGBSPj4Bbby40Q5FOLHnIbscBFBNhPxee8";
    private static final String CHAT_ID = "-4535807186"; // Replace with the target chat ID (user or group)

    private static final String TELEGRAM_API_URL = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage";

    // Method to send the message
    public static void sendTelegramMessage(String message) {
        OkHttpClient client = new OkHttpClient();

        // Prepare the data to send in the request
        RequestBody body = new FormBody.Builder()
                .add("chat_id", CHAT_ID)
                .add("text", message)
                .build();

        Request request = new Request.Builder()
                .url(TELEGRAM_API_URL)
                .post(body)
                .build();

        // Execute the request and handle response
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("Error sending message: " + response.body().string());
            } else {
                System.out.println("Message sent successfully.");
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public static void triggerEmergencyAlert() {
        String message = "🚨 Emergency Alert: Immediate Attention Required!";
        sendTelegramMessage(message); // Send the message
    }
    
    
}
