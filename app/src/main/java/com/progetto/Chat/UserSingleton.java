package com.progetto.Chat;

import java.util.ArrayList;

public class UserSingleton {
    private static UserSingleton instance;
    private User user;

    private UserSingleton() {
        // Private constructor to prevent instantiation
    }

    public static UserSingleton getInstance() {
        if (instance == null) {
            instance = new UserSingleton();
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String login(User user) {
        if (user != null) {
            return user.login(user);
        } else {
            return "User object is null";
        }
    }

    public String isInRoom(int user_id, int room_id) {
        if (user != null) {
            return user.isInRoom(user_id, room_id);
        } else {
            return "User object is null";
        }
    }


    public Integer getId(String username) {
            return user.getId(username);
    }

    public Integer getRoomId(String name) {
        return user.getRoomId(name);
    }

    public String registra(User user) {
        if (user != null) {
            return user.registra(user);
        } else {
            return "User object is null";
        }
    }

    public String addToStanza(int user_id, int room_id) {
        if (user != null) {
            return user.addToStanza(user_id, room_id);
        } else {
            return "User object is null";
        }
    }

    public String getMieStanze(int user_id) {
        if (user != null) {
            return user.getMieStanze(user_id);
        } else {
            return "User object is null";
        }
    }

    public ArrayList<String> getAllStanze() {
            return user.getAllStanze();
    }

    public ArrayList<String> getWaitList(int ammi_id) {
            return user.getWaitList(ammi_id);
    }

    public String declineRequest(int user_id, int room_id){
        if (user != null) {
            return user.declineRequest(user_id, room_id);
        } else {
            return "User object is null";
        }
    }

    public String acceptRequest(int user_id, int room_id){
        if (user != null) {
            return user.acceptRequest(user_id, room_id);
        } else {
            return "User object is null";
        }
    }

    public String sendRequest(int user_id, int room_id) {
        if (user != null) {
            return user.sendRequest(user_id, room_id);
        } else {
            return "User object is null";
        }
    }

    public String creaStanza(String nome, int ammi_id) {
        if (user != null) {
            return user.creaStanza(nome, ammi_id);
        } else {
            return "User object is null";
        }
    }

    public ArrayList<String> getMessages(int room_id) {
            return user.getMessages(room_id);
    }

    public ArrayList<String> getUsers() {
            return user.getUsers();
    }

    public String sendMessages(int user_id, int room_id, String message) {
        if (user != null) {
            return user.sendMessages(user_id, room_id, message);
        } else {
            return "User object is null";
        }
    }

    public String getUsername() {
        if (user != null) {
            return user.getUsername();
        } else {
            return "User object is null";
        }
    }

    public void setUsername(String username) {
        if (user != null) {
            user.setUsername(username);
        }
    }

    public String getPassword() {
        if (user != null) {
            return user.getPassword();
        } else {
            return "User object is null";
        }
    }

    public void setPassword(String password) {
        if (user != null) {
            user.setPassword(password);
        }
    }
}
