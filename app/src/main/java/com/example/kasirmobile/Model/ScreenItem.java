package com.example.kasirmobile.Model;

public class ScreenItem {
    String title, desc, subdesc;
    int screenImage;

    public ScreenItem(String title, String desc, String subdesc, int screenImage) {
        this.title = title;
        this.desc = desc;
        this.subdesc = subdesc;
        this.screenImage = screenImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSubdesc() {
        return subdesc;
    }

    public void setSubdesc(String subdesc) {
        this.subdesc = subdesc;
    }

    public int getScreenImage() {
        return screenImage;
    }

    public void setScreenImage(int screenImage) {
        this.screenImage = screenImage;
    }
}
