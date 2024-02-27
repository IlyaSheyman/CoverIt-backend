package coverit.ImageClient.client;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class ChatGptClient {
    public String chatGPT(String text) {
        try {
            String url = "https://api.openai.com/v1/completions";
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer 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");

            JSONObject data = new JSONObject();
            data.put("model", "gpt-3.5-turbo-0125");
            data.put("prompt", text);
            data.put("max_tokens", 4000);
            data.put("temperature", 1.0);

            con.setDoOutput(true);
            con.getOutputStream().write(data.toString().getBytes());

            String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                    .reduce((a, b) -> a + b).get();

            return new JSONObject(output).getJSONArray("choices").getJSONObject(0).getString("text");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}