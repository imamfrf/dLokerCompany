package com.dloker.imam.dlokercompany;
public class List_Item {

    private String namaPelamar, descPelamar, idLamaran, imgSrc;
    //private int imgId;

    //constructor item yang akan dibuat
    public List_Item(String namaPelamar, String descPelamar, String idLamaran, String imgSrc) {
        this.namaPelamar = namaPelamar;
        this.descPelamar = descPelamar;
        this.imgSrc = imgSrc;
        this.idLamaran = idLamaran;
    }

    public String getIdLamaran() {
        return idLamaran;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public String getNamaPelamar() {

        return namaPelamar;
    }

    public String getDescPelamar() {

        return descPelamar;

    }
}

