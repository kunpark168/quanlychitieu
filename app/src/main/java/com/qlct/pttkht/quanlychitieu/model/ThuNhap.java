package com.qlct.pttkht.quanlychitieu.model;

public class ThuNhap {
    private String id;
    private String note;
    private double amount;
    private int type; //0 cố định , 1 đột xuất
    private long timeStamp;
    private int mounth;
    private String date;

    public ThuNhap() {
    }

    public ThuNhap(int mounth, String node, String id, double amount, int type, long timeStamp, String date) {
        this.mounth = mounth;
        this.note = node;
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.timeStamp = timeStamp;
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

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
