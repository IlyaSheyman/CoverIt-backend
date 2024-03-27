package image_client.client;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
@Slf4j
public class DalleClient {
    private final String secretKey;

    public DalleClient(@Value(value = "${openai_secretkey}")String secretKey) {
        this.secretKey = secretKey;
    }

    public String generateImage(String prompt) {
        try {
            String url = "https://api.openai.com/v1/images/generations";
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + secretKey);

//            String data = String.format("{\"model\":\"dall-e-3\",\"prompt\":\"%s\",\"n\":1,\"size\":\"1024x1024\"}", prompt);
            String data = String.format("{\"prompt\":\"%s\",\"n\":1,\"size\":\"1024x1024\"}", prompt);

            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(data);
            osw.flush();
            osw.close();
            os.close();

            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray dataArray = jsonResponse.getJSONArray("data");
                String imageUrl = dataArray.getJSONObject(0).getString("url");

                return imageUrl;
            } else {
                throw new RuntimeException("Failed to retrieve image. HTTP error code: " + con.getResponseCode());
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}