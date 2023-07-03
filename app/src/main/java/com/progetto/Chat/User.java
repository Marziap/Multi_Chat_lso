package com.progetto.Chat;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

public class User {

    private String username;

    private transient Connessione conn = new Connessione("192.168.1.59", 5555);
    private String password;

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String login(User user) {
        AtomicReference<String> answer = new AtomicReference<>();
        new Thread(() -> {
            conn.connect();
            conn.send("1%"+user.username+"%"+user.password);
            String response = conn.receive();
            conn.closeConnection();

            if (response.equals(user.password)) {
                answer.set("ok");
            } else {
                answer.set("no");
            }

        }).start();

        try {
            Thread.sleep(250);  // Puoi regolare il tempo di attesa come necessario
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Ottenere il valore dalla variabile answer
        String loginResult = answer.get();
        return loginResult;

    }

    public String isInRoom(int user_id, int room_id) {
        AtomicReference<String> answer = new AtomicReference<>();
        new Thread(() -> {
            conn.connect();
            conn.send("13%"+user_id+"%"+room_id+"%");
            String response = conn.receive();
            conn.closeConnection();

            if (!response.isEmpty()) {
                answer.set("free");
            } else {
                answer.set("locked");
            }

        }).start();

        try {
            Thread.sleep(250);  // Puoi regolare il tempo di attesa come necessario
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Ottenere il valore dalla variabile answer
        String loginResult = answer.get();
        return loginResult;

    }


    public Integer getId(String username) {
        AtomicReference<Integer> answer = new AtomicReference<>();
        new Thread(() -> {
            conn.connect();
            conn.send("0%" + username + "%");
            Integer response = conn.receiveInt();
            conn.closeConnection();

            if (response != null) {
                int id = response; // Parse the string response to an int
                answer.set(id);
            } else {
                answer.set(0);
            }

        }).start();

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Integer idResult = answer.get();
        return idResult;
    }

    public Integer getRoomId(String name) {
        AtomicReference<Integer> answer = new AtomicReference<>();
        new Thread(() -> {
            conn.connect();
            conn.send("12%" + name + "%");
            Integer response = conn.receiveInt();
            conn.closeConnection();

            if (response != null) {
                int id = response; // Parse the string response to an int
                answer.set(id);
            } else {
                answer.set(0);
            }

        }).start();

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Integer idResult = answer.get();
        return idResult;
    }




    public String registra(User user){
        AtomicReference<String> answer = new AtomicReference<>();
        new Thread(() -> {
            Log.d("before connect", "ok");
        conn.connect();
            Log.d("after connect", "ok");
        conn.send("2%"+user.username+"%"+user.password);
        String response = conn.receive();
        conn.closeConnection();

            if (response.equals("ok")) {
                answer.set("ok");
            } else {
                answer.set("no");
            }

        }).start();

        try {
            Thread.sleep(250);  // Puoi regolare il tempo di attesa come necessario
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Ottenere il valore dalla variabile answer
        String registerResult = answer.get();
        return registerResult;
    }


    public String addToStanza(int user_id, int room_id){
        AtomicReference<String> answer = new AtomicReference<>();
        new Thread(() -> {
            Log.d("before connect", "ok");
            conn.connect();
            Log.d("after connect", "ok");
            conn.send("11%"+user_id+"%"+room_id);
            String response = conn.receive();
            conn.closeConnection();

            if (response.equals("ok")) {
                answer.set("ok");
            } else {
                answer.set("no");
            }

        }).start();

        try {
            Thread.sleep(250);  // Puoi regolare il tempo di attesa come necessario
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Ottenere il valore dalla variabile answer
        String registerResult = answer.get();
        return registerResult;
    }

    public String getMieStanze(int user_id){ //SELECT name FROM users_in_rooms JOIN rooms ON users_in_rooms.room_id = rooms.id WHERE users_in_rooms.user_id = 2;
        conn.connect();
        conn.send("3%"+user_id);
        String answer = conn.receive();
        conn.closeConnection();
        return answer;
    }

    public ArrayList<String> getAllStanze() {
        ArrayList<String> roomList = new ArrayList<>();
        new Thread(() -> {
            conn.connect();
            conn.send("4");
            String response = conn.receive();
            conn.closeConnection();

            if (response != null) {
                System.out.println("Risposta: " + response);
                // Separare la stringa in elementi usando il separatore "@" stanza1@stanza2@stanza3
                String[] rooms = response.split("@");
                // Aggiungere gli elementi all'ArrayList
                Collections.addAll(roomList, rooms);
            } else {
                // In caso di risposta null, impostare l'ArrayList come vuoto
                roomList.clear();
            }
        }).start();

        try {
            Thread.sleep(250);  // Puoi regolare il tempo di attesa come necessario
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Ritornare l'ArrayList contenente i risultati
        return roomList;
    }


    public ArrayList<String> getWaitList(int ammi_id){ //SELECT name FROM waitlist JOIN rooms ON waitlist.room_id = rooms.id WHERE rooms.ammi_id = 1;
        ArrayList<String> messaggi = new ArrayList<>();
        new Thread(() -> {
            conn.connect();
            conn.send("8%"+ammi_id);
            String response = conn.receive();
            conn.closeConnection();

            if (response != null) {
                System.out.println("Risposta: " + response);
                // Separare la stringa in elementi usando il separatore "@"
                String[] rooms = response.split("@");
                // Aggiungere gli elementi all'ArrayList dopo aver sostituito '&' con ':'
                for (String room : rooms) {
                    messaggi.add(room.replaceAll("&", ": "));
                }
            } else {
                // In caso di risposta null, impostare l'ArrayList come vuoto
                messaggi.clear();
            }
        }).start();

        try {
            Thread.sleep(250);  // Puoi regolare il tempo di attesa come necessario
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Ritornare l'ArrayList contenente i risultati
        return messaggi;
    }

    public String sendRequest(int user_id, int room_id){
        AtomicReference<String> answer = new AtomicReference<>();
        new Thread(() -> {
            Log.d("before connect", "ok");
            conn.connect();
            Log.d("after connect", "ok");
            conn.send("6%"+user_id+"%"+room_id);
            String response = conn.receive();
            conn.closeConnection();

            if (response.equals("ok")) {
                answer.set("ok");
            } else {
                answer.set("no");
            }

        }).start();

        try {
            Thread.sleep(250);  // Puoi regolare il tempo di attesa come necessario
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Ottenere il valore dalla variabile answer
        String registerResult = answer.get();
        return registerResult;
    }

    public String declineRequest(int user_id, int room_id) {
        AtomicReference<String> answer = new AtomicReference<>();
        new Thread(() -> {
            Log.d("before connect", "ok");
            conn.connect();
            Log.d("after connect", "ok");

            // Replace occurrences of ": " with "%"

            conn.send("14%" + user_id + "%" + room_id + "%");
            String response = conn.receive();
            conn.closeConnection();

            if (response.equals("ok")) {
                answer.set("ok");
            } else {
                answer.set("no");
            }
        }).start();

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String registerResult = answer.get();
        return registerResult;
    }

    public String acceptRequest(int user_id, int room_id) {
        AtomicReference<String> answer = new AtomicReference<>();
        new Thread(() -> {
            Log.d("before connect", "ok");
            conn.connect();
            Log.d("after connect", "ok");

            // Replace occurrences of ": " with "%"

            conn.send("15%" + user_id + "%" + room_id + "%");
            String response = conn.receive();
            conn.closeConnection();

            if (response.equals("ok")) {
                answer.set("ok");
            } else {
                answer.set("no");
            }
        }).start();

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String registerResult = answer.get();
        return registerResult;
    }


    public String creaStanza(String nome, int ammi_id){
        AtomicReference<String> answer = new AtomicReference<>();
        new Thread(() -> {
            Log.d("before connect", "ok");
            conn.connect();
            Log.d("after connect", "ok");
            conn.send("7%"+nome+"%"+ammi_id);
            String response = conn.receive();
            conn.closeConnection();

            if (response.equals("ok")) {
                answer.set(nome);
            } else {
                answer.set("no");
            }

        }).start();

        try {
            Thread.sleep(250);  // Puoi regolare il tempo di attesa come necessario
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Ottenere il valore dalla variabile answer
        String registerResult = answer.get();
        return registerResult;
    }

    public ArrayList<String> getMessages(int room_id) {
        ArrayList<String> messaggi = new ArrayList<>();
        new Thread(() -> {
            conn.connect();
            conn.send("9%"+room_id);
            String response = conn.receive();
            conn.closeConnection();

            if (response != null) {
                System.out.println("Risposta: " + response);
                // Separare la stringa in elementi usando il separatore "@"
                String[] rooms = response.split("@");
                // Aggiungere gli elementi all'ArrayList dopo aver sostituito '&' con ':'
                for (String room : rooms) {
                    messaggi.add(room.replaceAll("&", ": "));
                }
            } else {
                // In caso di risposta null, impostare l'ArrayList come vuoto
                messaggi.clear();
            }
        }).start();

        try {
            Thread.sleep(250);  // Puoi regolare il tempo di attesa come necessario
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Ritornare l'ArrayList contenente i risultati
        return messaggi;
    }


    public ArrayList<String> getUsers(){
        ArrayList<String> users = new ArrayList<>();
        new Thread(() -> {
            conn.connect();
            conn.send("10");
            String response = conn.receive();
            conn.closeConnection();

            if (response != null) {
                System.out.println("Risposta: " + response);
                // Separare la stringa in elementi usando il separatore "@"
                String[] utenti = response.split("@");
                // Aggiungere gli elementi all'ArrayList
                Collections.addAll(users, utenti);
            } else {
                // In caso di risposta null, impostare l'ArrayList come vuoto
                users.clear();
            }
        }).start();

        try {
            Thread.sleep(250);  // Puoi regolare il tempo di attesa come necessario
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Ritornare l'ArrayList contenente i risultati
        return users;
    }

    public String sendMessages(int user_id, int room_id, String message){
        AtomicReference<String> answer = new AtomicReference<>();
        new Thread(() -> {
            Log.d("before connect", "ok");
            conn.connect();
            Log.d("after connect", "ok");
            conn.send("5%"+user_id+"%"+room_id+"%"+message+"%");
            String response = conn.receive();
            conn.closeConnection();

            if (response.equals("ok")) {
                answer.set("ok");
            } else {
                answer.set("no");
            }

        }).start();

        try {
            Thread.sleep(250);  // Puoi regolare il tempo di attesa come necessario
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Ottenere il valore dalla variabile answer
        String registerResult = answer.get();
        return registerResult;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
