package com.qlct.pttkht.quanlychitieu.model;

public class Kehoach {
    private int mounth;
    private String id;
    private long timeStamp;
    private double amount;
    private String note;
    private String date;

    public Kehoach() {
    }

    public Kehoach(int mounth, String id, long timeStamp, double amount, String note) {
        this.mounth = mounth;
        this.id = id;
        this.timeStamp = timeStamp;
        this.amount = amount;
        this.note = note;
    }

    public Kehoach(int mounth, String id, long timeStamp, double amount, String note, String date) {
        this.mounth = mounth;
        this.id = id;
        this.timeStamp = timeStamp;
        this.amount = amount;
        this.note = note;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMounth() {
        return mounth;
    }

    public void setMounth(int mounth) {
        this.mounth = mounth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
