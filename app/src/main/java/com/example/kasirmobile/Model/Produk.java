package com.example.kasirmobile.Model;

//Model untuk menampung data (tipe data baru)
//Menggunakan konstruktor, setter, dan getter
//Karena akan dikirim antar activity, maka implement parcelable

import android.os.Parcel;
import android.os.Parcelable;

public class Produk implements Parcelable{
    String id;
    String sku;
    String nama;
    String harga;
    String stok;
    String gambar;

    public Produk() {
    }

    public Produk(String id, String sku, String nama, String harga, String stok, String gambar) {
        this.id = id;
        this.sku = sku;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
        this.gambar = gambar;
    }

    protected Produk(Parcel in) {
        id = in.readString();
        sku = in.readString();
        nama = in.readString();
        harga = in.readString();
        stok = in.readString();
        gambar = in.readString();
    }

    public static final Creator<Produk> CREATOR = new Creator<Produk>() {
        @Override
        public Produk createFromParcel(Parcel in) {
            return new Produk(in);
        }

        @Override
        public Produk[] newArray(int size) {
            return new Produk[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(sku);
        dest.writeString(nama);
        dest.writeString(harga);
        dest.writeString(stok);
        dest.writeString(gambar);
    }
}
