package com.example.kasirmobile.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.kasirmobile.Model.Produk;
import com.example.kasirmobile.Model.ProdukRiwayat;

import java.util.ArrayList;
import java.util.List;

//Kelas untuk berhubungan dengan Database
public class DbHelper {
    Context context;
    DbConfig dbConfig;
    SQLiteDatabase database;
    Cursor cursor;

    public DbHelper(Context context) {
        this.context = context;
        dbConfig = new DbConfig(context);
    }

//CRUD untuk Tabel Produk ================================================================================================================================

    //Fungsi untuk menampilkan data berdasarkan nama
    public List<Produk> getAllProduk(String nama) {
        List<Produk> produks = new ArrayList<>();

        database = dbConfig.getReadableDatabase();
        //Perintah untuk menampilkan data sekalian untuk pencarian produk berdasarkan nama (terdapat di nama dari depan dan belakang)
        cursor = database.rawQuery("SELECT * FROM " + DbConfig.TB_PRODUK + " WHERE nama LIKE '%" + nama + "%'", null);

        //Jika cursor mendapati ada data maka
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                produks.add(new Produk(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                ));
            } while (cursor.moveToNext());
        }
        return produks;
    }

    //Fungsi untuk menampilkan data berdasarkan id untuk proses stok
    public Produk getProduk(String id) {
        Produk produk = new Produk();

        database = dbConfig.getReadableDatabase();

        cursor = database.rawQuery("SELECT * FROM " + DbConfig.TB_PRODUK + " WHERE id  = " + id, null);

        //Jika cursor mendapati ada data maka
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                produk = new Produk(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                );
            } while (cursor.moveToNext());
        }
        return produk;
    }

    //Fungsi untuk menampilkan data berdasarkan sku untuk tambah produk di keranjang dengan SKU
    public Produk getProdukBySKU(String sku) {
        Produk produk = new Produk();

        database = dbConfig.getReadableDatabase();
        //Perintah untuk menampilkan data sekalian untuk pencarian produk berdasarkan nama (terdapat di nama dari depan dan belakang)
        cursor = database.rawQuery("SELECT * FROM " + DbConfig.TB_PRODUK + " WHERE sku  = " + sku, null);

        //Jika cursor mendapati ada data maka
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                produk = new Produk(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                );
            } while (cursor.moveToNext());
        }
        return produk;
    }

    //Fungsi untuk memasukkan data
    public void addProduk(Produk produk) {
        database = dbConfig.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("sku", produk.getSku());
        values.put("nama", produk.getNama());
        values.put("harga", produk.getHarga());
        values.put("stok", produk.getStok());
        values.put("gambar", produk.getGambar());

        database.insert(DbConfig.TB_PRODUK, null, values);
    }

    //Fungsi untuk menghapus data
    public void deleteProduk(String id) {
        database = dbConfig.getWritableDatabase();

        String where = "id=?";
        String[] whereArg = new String[]{id};

        database.delete(DbConfig.TB_PRODUK, where, whereArg);
    }

    //Fungsi untuk memperbarui data
    public void updateProduk(Produk produk) {
        database = dbConfig.getWritableDatabase();

        String where = "id=?";
        String[] whereArg = new String[]{produk.getId()};

        ContentValues values = new ContentValues();
        values.put("sku", produk.getSku());
        values.put("nama", produk.getNama());
        values.put("harga", produk.getHarga());
        values.put("stok", produk.getStok());
        values.put("gambar", produk.getGambar());

        database.update(DbConfig.TB_PRODUK, values, where, whereArg);
    }

//CRUD untuk Tabel Produk ================================================================================================================================

//CRUD untuk Tabel Keranjang ================================================================================================================================

    //Fungsi untuk menampilkan data
    public List<Produk> getAllKeranjang(String nama) {
        List<Produk> produks = new ArrayList<>();

        database = dbConfig.getReadableDatabase();
        //Perintah untuk menampilkan data sekalian untuk pencarian produk berdasarkan nama (terdapat di nama dari depan dan belakang)
        cursor = database.rawQuery("SELECT * FROM " + DbConfig.TB_KERANJANG + " WHERE nama LIKE '%" + nama + "%'", null);

        //Jika cursor mendapati ada data maka
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                produks.add(new Produk(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                ));
            } while (cursor.moveToNext());
        }
        return produks;
    }

    //Fungsi untuk menampilkan data berdasarkan id
    public List<Produk> getAllKeranjangbyId(String id) {
        List<Produk> produks = new ArrayList<>();

        database = dbConfig.getReadableDatabase();

        cursor = database.rawQuery("SELECT * FROM " + DbConfig.TB_KERANJANG + " WHERE id  = " + id, null);

        //Jika cursor mendapati ada data maka
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                produks.add(new Produk(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                ));
            } while (cursor.moveToNext());
        }
        return produks;
    }

    //Fungsi untuk memasukkan data
    public void addKeranjang(Produk produk) {
        database = dbConfig.getWritableDatabase();

        cursor = database.rawQuery("SELECT * FROM " + DbConfig.TB_KERANJANG + " WHERE id = " + produk.getId(), null);

        //Jika data sudah ada maka yang ditambahkan stoknya jika tidak tambahkan data baru
        if (cursor.getCount() > 0) {
            ContentValues values = new ContentValues();

            cursor.moveToFirst();

            int stok = Integer.parseInt(cursor.getString(4)) + Integer.parseInt(produk.getStok());

            values.put("id", produk.getId());
            values.put("sku", produk.getSku());
            values.put("nama", produk.getNama());
            values.put("harga", produk.getHarga());
            values.put("stok", stok);
            values.put("gambar", produk.getGambar());

            String where = "id=?";
            String[] whereArg = new String[]{produk.getId()};

            database.update(DbConfig.TB_KERANJANG, values, where, whereArg);

        } else {
            ContentValues values = new ContentValues();
            values.put("id", produk.getId());
            values.put("sku", produk.getSku());
            values.put("nama", produk.getNama());
            values.put("harga", produk.getHarga());
            values.put("stok", produk.getStok());
            values.put("gambar", produk.getGambar());

            database.insert(DbConfig.TB_KERANJANG, null, values);
        }
    }

    //Fungsi untuk memperbarui data
    public void updateKeranjang(Produk produk) {
        database = dbConfig.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("sku", produk.getSku());
        values.put("nama", produk.getNama());
        values.put("harga", produk.getHarga());
        values.put("gambar", produk.getGambar());

        String where = "id=?";
        String[] whereArg = new String[]{produk.getId()};
        database.update(DbConfig.TB_KERANJANG, values, where, whereArg);
    }

    //Fungsi untuk memperbarui stok produk di keranjang
    public void updateStokKeranjang(Produk produk) {
        database = dbConfig.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("stok", produk.getStok());

        String where = "id=?";
        String[] whereArg = new String[]{produk.getId()};
        database.update(DbConfig.TB_KERANJANG, values, where, whereArg);
    }

    //Fungsi untuk menghapus data
    public void deleteKeranjang(String id) {
        database = dbConfig.getWritableDatabase();

        String where = "id=?";
        String[] whereArg = new String[]{id};

        database.delete(DbConfig.TB_KERANJANG, where, whereArg);
    }

    //Fungsi untuk menghapus semua data keranjang

//CRUD untuk Tabel Keranjang ================================================================================================================================

//CRUD untuk Tabel Riwayat ==================================================================================================================================

    //Fungsi untuk menampilkan data berdasarkan nama
    public List<ProdukRiwayat> getAllHistory(String nama) {
        List<ProdukRiwayat> produks = new ArrayList<>();

        database = dbConfig.getReadableDatabase();
        //Perintah untuk menampilkan data sekalian untuk pencarian produk berdasarkan nama (terdapat di nama dari depan dan belakang)
        cursor = database.rawQuery("SELECT * FROM " + DbConfig.TB_RIWAYAT + " WHERE nama LIKE '%" + nama + "%'", null);

        //Jika cursor mendapati ada data maka
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            do {
                produks.add(new ProdukRiwayat(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8)
                ));
            } while (cursor.moveToPrevious());
        }
        return produks;
    }

    //Fungsi untuk memasukkan data
    public void addHistory(ProdukRiwayat produk) {
        database = dbConfig.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("tgl_transaksi", produk.getTglTransaksi());
        values.put("jam_transaksi", produk.getJamTransaksi());
        values.put("total_harga", produk.getTotalHarga());
        values.put("sku", produk.getSku());
        values.put("nama", produk.getNama());
        values.put("harga", produk.getHarga());
        values.put("stok", produk.getStok());
        values.put("gambar", produk.getGambar());

        database.insert(DbConfig.TB_RIWAYAT, null, values);
    }
}

