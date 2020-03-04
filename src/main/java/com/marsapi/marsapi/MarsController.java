package com.marsapi.marsapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.json.JSONArray;
import org.json.JSONObject;

@RestController
public class MarsController {

    private static double averageTemperature = 0;

    @GetMapping("/nasa/temperature")
    public Mars mars(@RequestParam(value = "sol", defaultValue = "0") String sol) throws IOException {

        final String API_KEY = "b7bciIb4uywoczTjn7JZia5HV0EuQQe9pTuZCYVo";

        BufferedReader reader = new BufferedReader(new InputStreamReader(getRequest(API_KEY)));

        String inputLine;

        StringBuffer content = new StringBuffer();

        // "reader" will be read line by line and passed to StringBuffer "content"
        while ((inputLine = reader.readLine()) != null) {
            content.append(inputLine);
        }
        
        reader.close();
        
        // "content" converted to a JSON Object
        JSONObject jsonObjContent = new JSONObject(content.toString());

        // Values of "sol_keys" key in a JSONArray
        JSONArray jsonArrayOfSOLs = jsonObjContent.getJSONArray("sol_keys");

        if (sol.equals("0")) {

            double avgTemp = 0;

            for (int i = 0; i < jsonArrayOfSOLs.length(); i++) {

                JSONObject SOL = jsonObjContent.getJSONObject(jsonArrayOfSOLs.get(i).toString());
                JSONObject AT = SOL.getJSONObject("AT");

                avgTemp += AT.getDouble("av");
            }

            averageTemperature = (avgTemp /= jsonArrayOfSOLs.length());
        } 
        // If a SOL has been chosen its average temperature will be printed
        else {
            
            JSONObject SOL = jsonObjContent.getJSONObject(sol);
            JSONObject AT = SOL.getJSONObject("AT");

            averageTemperature = AT.getDouble("av");
        }
        
        return new Mars(averageTemperature);
    }

    /**
     * @param apiKeyParam
     * @return an input stream from an open connection
     * @throws IOException
     */
    public static InputStream getRequest(final String apiKeyParam) throws IOException {
        URL url = new URL("https://api.nasa.gov/insight_weather/?api_key=" + apiKeyParam + "&s&feedtype=json&ver=1.0");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        connection.disconnect();

        return connection.getInputStream();
    }
}
