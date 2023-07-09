package com.progetto.Chat;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

public class User {

    private String username;

    private Connessione conn = new Connessione("35.93.152.29", 5555);
    private String password;

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String login(User user) {
        AtomicReference<String> answer = new AtomicReference<>();
        Thread loginThread = new Thread(() -> {
            conn.connect();
            conn.send("1%"+user.username+"%"+user.password);
            String response = conn.receive();
            conn.closeConnection();

            if (response.equals(user.password)) {
                answer.set("ok");
            } else {
                answer.set("no");
            }

        });

        loginThread.start();

        try {
            loginThread.join();  // Attende che il thread termini
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String loginResult = answer.get();
        return loginResult;
    }


    public String isInRoom(int user_id, int room_id) {
        AtomicReference<String> answer = new AtomicReference<>();
        Thread isInRoomThread = new Thread(() -> {
            conn.connect();
            conn.send("13%"+user_id+"%"+room_id+"%");
            String response = conn.receive();
            conn.closeConnection();

            if (response.isEmpty()) {
                answer.set("locked");
            } else {
                answer.set("free");
            }
        });

        isInRoomThread.start();

        try {
            isInRoomThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String loginResult = answer.get();
        return loginResult;

    }


    public Integer getId(String username) {
        AtomicReference<Integer> answer = new AtomicReference<>();
        Thread getIdThread = new Thread(() -> {
            conn.connect();
            conn.send("0%" + username + "%");
            Integer response = conn.receiveInt();
            conn.closeConnection();

            if (response != null) {
                int id = response;
                answer.set(id);
            } else {
                answer.set(0);
            }

        });

        getIdThread.start();

        try {
            getIdThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Integer idResult = answer.get();
        return idResult;
    }

    public Integer getRoomId(String name) {
        AtomicReference<Integer> answer = new AtomicReference<>();
        Thread getRoomIdThread = new Thread(() -> {
            conn.connect();
            conn.send("12%" + name + "%");
            Integer response = conn.receiveInt();
            conn.closeConnection();

            if (response != null) {
                int id = response;
                answer.set(id);
            } else {
                answer.set(0);
            }

        });

        getRoomIdThread.start();

        try {
            getRoomIdThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Integer idResult = answer.get();
        return idResult;
    }


    public String registra(User user){
        AtomicReference<String> answer = new AtomicReference<>();
        Thread registraThread = new Thread(() -> {
        conn.connect();
        conn.send("2%"+user.username+"%"+user.password);
        String response = conn.receive();
        conn.closeConnection();

            if (response.equals("ok")) {
                answer.set("ok");
            } else {
                answer.set("no");
            }

        });

        registraThread.start();

        try {
            registraThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String registerResult = answer.get();
        return registerResult;
    }


    public String addToStanza(int user_id, int room_id){
        AtomicReference<String> answer = new AtomicReference<>();
        Thread addToStanzaThread = new Thread(() -> {
            conn.connect();
            conn.send("11%"+user_id+"%"+room_id);
            String response = conn.receive();
            conn.closeConnection();

            if (response.equals("ok")) {
                answer.set("ok");
            } else {
                answer.set("no");
            }

        });

        addToStanzaThread.start();

        try {
            addToStanzaThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String registerResult = answer.get();
        return registerResult;
    }


    public ArrayList<String> getAllStanze() {
        ArrayList<String> roomList = new ArrayList<>();
        Thread getAllStanzeThread = new Thread(() -> {
            conn.connect();
            conn.send("4");
            String response = conn.receive();
            conn.closeConnection();

            if (response != null) {
                // Separare la stringa in elementi usando il separatore "@" stanza1@stanza2@stanza3
                String[] rooms = response.split("@");
                // Aggiungere gli elementi all'ArrayList
                Collections.addAll(roomList, rooms);
            } else {
                // In caso di risposta null, impostare l'ArrayList come vuoto
                roomList.clear();
            }
        });
        getAllStanzeThread.start();
        try {
            getAllStanzeThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return roomList;
    }

    public ArrayList<String> getWaitList(int ammi_id){
        ArrayList<String> messaggi = new ArrayList<>();
        Thread getWaitlistThread = new Thread(() -> {
            conn.connect();
            conn.send("8%"+ammi_id);
            String response = conn.receive();
            conn.closeConnection();

            if (response != null) {
                // Separare la stringa in elementi usando il separatore "@"
                String[] rooms = response.split("@");
                // Aggiungere gli elementi all'ArrayList dopo aver sostituito '&' con ':'
                for (String room : rooms) {
                    messaggi.add(room.replaceAll("&", ": "));
                }
            } else {
                messaggi.clear();
            }
        });

        getWaitlistThread.start();

        try {
            getWaitlistThread.join();  // Attendere che il thread di login termini
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Ritornare l'ArrayList contenente i risultati
        return messaggi;
    }

    public String sendRequest(int user_id, int room_id){
        AtomicReference<String> answer = new AtomicReference<>();
        Thread sendRequestThread = new Thread(() -> {
            conn.connect();
            conn.send("6%"+user_id+"%"+room_id);
            String response = conn.receive();
            conn.closeConnection();

            if (response.equals("ok")) {
                answer.set("ok");
            } else {
                answer.set("no");
            }

        });

        sendRequestThread.start();

        try {
            sendRequestThread.join();  // Attendere che il thread di login termini
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Ottenere il valore dalla variabile answer
        String registerResult = answer.get();
        return registerResult;
    }

    public String declineRequest(int user_id, int room_id) {
        AtomicReference<String> answer = new AtomicReference<>();
        Thread declineRequestThread = new Thread(() -> {
            conn.connect();
            conn.send("14%" + user_id + "%" + room_id + "%");
            String response = conn.receive();
            conn.closeConnection();

            if (response.equals("ok")) {
                answer.set("ok");
            } else {
                answer.set("no");
            }
        });

        declineRequestThread.start();

        try {
            declineRequestThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String registerResult = answer.get();
        return registerResult;
    }

    public String acceptRequest(int user_id, int room_id) {
        AtomicReference<String> answer = new AtomicReference<>();
        Thread acceptRequestThread = new Thread(() -> {
            conn.connect();
            conn.send("15%" + user_id + "%" + room_id + "%");
            String response = conn.receive();
            conn.closeConnection();

            if (response.equals("ok")) {
                answer.set("ok");
            } else {
                answer.set("no");
            }
        });

        acceptRequestThread.start();

        try {
            acceptRequestThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String registerResult = answer.get();
        return registerResult;
    }


    public String creaStanza(String nome, int ammi_id){
        AtomicReference<String> answer = new AtomicReference<>();
        Thread creaStanzaThread = new Thread(() -> {
            conn.connect();
            conn.send("7%"+nome+"%"+ammi_id);
            String response = conn.receive();
            conn.closeConnection();

            if (response.equals("ok")) {
                answer.set(nome);
            } else {
                answer.set("no");
            }

        });

        creaStanzaThread.start();

        try {
            creaStanzaThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String registerResult = answer.get();
        return registerResult;
    }

    public ArrayList<String> getMessages(int room_id) {
        ArrayList<String> messaggi = new ArrayList<>();
        Thread getMessagesThread = new Thread(() -> {
            conn.connect();
            conn.send("9%"+room_id);
            String response = conn.receive();
            conn.closeConnection();

            if (response != null) {
                // Separare la stringa in elementi usando il separatore "@"
                String[] rooms = response.split("@");
                // Aggiungere gli elementi all'ArrayList dopo aver sostituito '&' con ':'
                for (String room : rooms) {
                    messaggi.add(room.replaceAll("&", ": "));
                }
            } else {
                messaggi.clear();
            }
        });

        getMessagesThread.start();

        try {
            getMessagesThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return messaggi;
    }

    public ArrayList<String> getUsers(){
        ArrayList<String> users = new ArrayList<>();
        Thread getUsersThread = new Thread(() -> {
            conn.connect();
            conn.send("10");
            String response = conn.receive();
            conn.closeConnection();

            if (response != null) {
                // Separare la stringa in elementi usando il separatore "@"
                String[] utenti = response.split("@");
                // Aggiungere gli elementi all'ArrayList
                Collections.addAll(users, utenti);
            } else {
                users.clear();
            }
        });

        getUsersThread.start();

        try {
            getUsersThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return users;
    }

    public String sendMessages(int user_id, int room_id, String message){
        AtomicReference<String> answer = new AtomicReference<>();
        Thread sendMessagesThread = new Thread(() -> {
            conn.connect();
            conn.send("5%"+user_id+"%"+room_id+"%"+message+"%");
            String response = conn.receive();
            conn.closeConnection();

            if (response.equals("ok")) {
                answer.set("ok");
            } else {
                answer.set("no");
            }

        });

        sendMessagesThread.start();

        try {
            sendMessagesThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
