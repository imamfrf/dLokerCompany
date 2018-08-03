package com.dloker.imam.dlokercompany;
public class List_Item {

    private String namaPelamar, descPelamar;
    private int imgId;

    //constructor item yang akan dibuat
    public List_Item(String namaPelamar, String descPelamar) {
        this.namaPelamar = namaPelamar;
        this.descPelamar = descPelamar;
        this.imgId = imgId;
    }

    public int getImgId() {

        return imgId;
    }

    public String getNamaPelamar() {

        return namaPelamar;
    }

    public String getDescPelamar() {

        return descPelamar;

    }
}

