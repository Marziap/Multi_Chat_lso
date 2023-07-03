package com.progetto.Chat;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;

public class Connessione{
    private String serverAddress;
    private int serverPort;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public Connessione(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void connect() {
        try {
            socket = new Socket(serverAddress, serverPort);
            socket.setKeepAlive(true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String dataToSend) {
        // Send the data
        writer.println(dataToSend);
    }

    public String receive() {
        String response = null;
        try {
            // Read the response
            Log.d("before response", "ok");
            response = reader.readLine();
            Log.d("after response", "ok");
            // Print the response
            System.out.println("Response from server: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public Integer receiveInt() {
        Integer response = null;
        try {
            // Read the response
            Log.d("before response", "ok");
            String responseString = reader.readLine();
            Log.d("after response", "ok");
            // Print the response
            System.out.println("Response from server: " + responseString);
            // Check if the response string is empty
            if (!responseString.isEmpty()) {
                // Convert the response string to an integer
                response = Integer.parseInt(responseString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return response;
    }




    public void closeConnection() {
        try {
            if (reader != null)
                reader.close();
            if (writer != null)
                writer.close();
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
