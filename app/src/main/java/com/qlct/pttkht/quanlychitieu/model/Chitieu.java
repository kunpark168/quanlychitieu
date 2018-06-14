package com.qlct.pttkht.quanlychitieu.model;

public class Chitieu {

    private String id;
    private String note;
    private long timeStamp;
    private int type;  //0: Cố định   1: Đột xuất
    private double amount;
    private String category;
    private int mounth;
    private String date;

    public Chitieu() {
    }

    public Chitieu(int mounth, String id, String note, long timeStamp, int type, double amount, String date) {

        this.mounth = mounth;
        this.id = id;
        this.note = note;
        this.timeStamp = timeStamp;
        this.type = type;
        this.amount = amount;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
