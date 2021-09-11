package com.example.kasirmobile.Model;

//Model untuk menampung data (tipe data baru)
//Menggunakan konstruktor, setter, dan getter

import android.os.Parcel;
import android.os.Parcelable;

public class ProdukRiwayat {
    String id;
    String tglTransaksi;
    String jamTransaksi;
    String totalHarga;
    String sku;
    String nama;
    String harga;
    String stok;
    String gambar;

    public ProdukRiwayat(String id, String tglTransaksi, String jamTransaksi, String totalHarga, String sku, String nama, String harga, String stok, String gambar) {
        this.id = id;
        this.tglTransaksi = tglTransaksi;
        this.jamTransaksi = jamTransaksi;
        this.totalHarga = totalHarga;
        this.sku = sku;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
        this.gambar = gambar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTglTransaksi() {
        return tglTransaksi;
    }

    public void setTglTransaksi(String tglTransaksi) {
        this.tglTransaksi = tglTransaksi;
    }

    public String getJamTransaksi() {
        return jamTransaksi;
    }

    public void setJamTransaksi(String jamTransaksi) {
        this.jamTransaksi = jamTransaksi;
    }

    public String getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(String totalHarga) {
        this.totalHarga = totalHarga;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getStok() {
        return stok;
    }

    public void setStok(String stok) {
        this.stok = stok;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
}
