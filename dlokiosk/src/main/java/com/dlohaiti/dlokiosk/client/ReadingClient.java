package com.dlohaiti.dlokiosk.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class ReadingClient {
    private RestClient restClient;

    public ReadingClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public int send(String jsonReadings) {
        StringReader stringReader = new StringReader(jsonReadings);
        BufferedReader bufferedReader = new BufferedReader(stringReader);

        String line;
        int count = 0;

        try {
            while ((line = bufferedReader.readLine()) != null) {
                if (restClient.post("/reading", line)) {
                    count++;
                } else {
                    return 0;
                }
            }
        } catch (IOException e) {
            return 0;
        }

        return count;
    }
}
