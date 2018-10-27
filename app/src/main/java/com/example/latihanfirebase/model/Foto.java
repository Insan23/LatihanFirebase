package com.example.latihanfirebase.model;

public class Foto {

    private String urlFoto;
    private String nama;

    public Foto() {
    }

    public Foto(String urlFoto, String nama) {
        this.urlFoto = urlFoto;
        this.nama = nama;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
