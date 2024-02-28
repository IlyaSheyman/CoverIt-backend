package coverit.ImageClient.client;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//Устаревший клиент, который обращался к chatGpt напрямую, без Spring Ai
@Slf4j
public class ChatGptClient {
    public String chatGPT(String text) {
        try {
            String url = "https://api.openai.com/v1/chat/completions";
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer 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");

            JSONArray messages = new JSONArray();

            JSONObject message1 = new JSONObject();
            message1.put("role", "server");
            message1.put("content", text);

            messages.put(message1);

            JSONObject data = new JSONObject();
            data.put("model", "gpt-3.5-turbo");
            data.put("messages", messages);

            log.info("request's json: {}", data.toString());

            con.setDoOutput(true);
            con.getOutputStream().write(data.toString().getBytes());

            String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                    .reduce((a, b) -> a + b).get();

            return new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text");
        } catch (Exception e) {
            log.debug(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}