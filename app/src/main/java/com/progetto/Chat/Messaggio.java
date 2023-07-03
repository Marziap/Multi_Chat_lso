package com.progetto.Chat;

import java.util.Date;

public class Messaggio {
    private String message;
    private int room_id;
    private int ammi_id;
    private Date timestamp;


    public Messaggio(String message, int room_id, int ammi_id, Date timestamp) {
        this.message = message;
        this.room_id = room_id;
        this.ammi_id = ammi_id;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public int getAmmi_id() {
        return ammi_id;
    }

    public void setAmmi_id(int ammi_id) {
        this.ammi_id = ammi_id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
