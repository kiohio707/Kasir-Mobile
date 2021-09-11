package com.example.kasirmobile.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//Kelas untuk membuat Database dan Tabel

public class DbConfig extends SQLiteOpenHelper {

    public static String TB_PRODUK = "tbProduk";
    public static String TB_KERANJANG= "tbKeranjang";
    public static String TB_RIWAYAT = "tbRiwayat";

    private static String DATABASE_NAME = "dbKasir";
    private static int DATABASE_VERSION = 1;

    private static String CREATE_TB_PRODUK = "CREATE TABLE tbProduk (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "sku VARCHAR(225)," +
            "nama VARCHAR(225)," +
            "harga INTEGER, " +
            "stok INTEGER, " +
            "gambar TEXT)";

    private static String CREATE_TB_KERANJANG = "CREATE TABLE tbKeranjang (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "sku VARCHAR(225), " +
            "nama VARCHAR(225), "+
            "harga INTEGER, "+
            "stok INTEGER, "+
            "gambar TEXT)";

    private static String CREATE_TB_RIWAYAT = "CREATE TABLE tbRiwayat (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "tgl_transaksi VARCHAR(225), " +
            "jam_transaksi VARCHAR(225), " +
            "total_harga VARCHAR(225), " +
            "sku VARCHAR(225), " +
            "nama VARCHAR(225), "+
            "harga INTEGER, "+
            "stok INTEGER, " +
            "gambar TEXT)";

    public DbConfig(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TB_PRODUK);
        db.execSQL(CREATE_TB_KERANJANG);
        db.execSQL(CREATE_TB_RIWAYAT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
