package com.progetto.Chat;

public class WaitList {
    private int user_id;
    private int room_id;
    private String status;

    public WaitList(int user_id, int room_id, String status) {
        this.user_id = user_id;
        this.room_id = room_id;
        this.status = status;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
