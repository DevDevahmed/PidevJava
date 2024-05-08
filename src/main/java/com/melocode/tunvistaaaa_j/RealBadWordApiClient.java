package com.melocode.tunvistaaaa_j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class RealBadWordApiClient {

    private final String apiKey;

    public RealBadWordApiClient(String apiKey, String endpoint, String key) {
        this.apiKey = apiKey;
    }

    public boolean containsBadWord(String text) throws IOException {
        String encodedText = URLEncoder.encode(text, "UTF-8");
        String urlString = "https://api.badwordservice.com/check?text=" + encodedText + "&api_key=" + apiKey;

        HttpURLConnection connection = null;
        BufferedReader in = null;

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                // Parse response JSON and check if bad words are present
                String jsonResponse = response.toString();
                // Assuming the response contains a JSON object with a 'containsBadWord' field
                // Replace this with the actual parsing logic for your API response
                return jsonResponse.contains("\"containsBadWord\":true");
            } else {
                // Handle error response
                System.err.println("Error response code: " + responseCode);
                return false;
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
