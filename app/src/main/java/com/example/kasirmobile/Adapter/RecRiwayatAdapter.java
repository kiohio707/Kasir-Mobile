package com.example.kasirmobile.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kasirmobile.Activity.TambahManualActivity;
import com.example.kasirmobile.Database.DbHelper;
import com.example.kasirmobile.Model.Produk;
import com.example.kasirmobile.Model.ProdukRiwayat;
import com.example.kasirmobile.R;

import java.util.List;

public class RecRiwayatAdapter extends RecyclerView.Adapter<RecRiwayatAdapter.ViewHolder> {

    //Inisialisasi objek
    Context context;
    List<ProdukRiwayat> listProduk;
    DbHelper dbHelper;

    //Konstruktor untuk meminta konteks
    public RecRiwayatAdapter(Context context, List<ProdukRiwayat> listProduk) {
        this.context = context;
        this.listProduk = listProduk;
        dbHelper = new DbHelper(context);
    }

    //Fungsi untuk tampilan recycleview
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(context).inflate(R.layout.row_riwayat, parent, false);
        return new ViewHolder(rowView);
    }

    //Untuk mencegah data berubah saat discroll
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    //Untuk mencegah data berubah saat discroll
    @Override
    public long getItemId(int position) {
        return position;
    }

    //Mengkoneksian adapter dengan tampilan (fungsi yang looping sebanyak data yang ada)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(listProduk.get(position));

        //Untuk menyatukan TextView tanggal, jam, serta total harga
        if (position > 0) {
            if (listProduk.get(position).getTglTransaksi().equals(listProduk.get((position - 1)).getTglTransaksi())) {
                holder.tv_Tgl.setVisibility(View.GONE);
            }
        }

        if (position > 0) {
            if (listProduk.get(position).getJamTransaksi().equals(listProduk.get(position - 1).getJamTransaksi())) {
                holder.tv_Jam.setVisibility(View.GONE);
            }
        }

        if (position > 0) {
            if (listProduk.get(position).getTotalHarga().equals(listProduk.get(position - 1).getTotalHarga()) &&
                    listProduk.get(position).getJamTransaksi().equals(listProduk.get(position - 1).getJamTransaksi())) {
                holder.tv_Total.setVisibility(View.GONE);
            }
        }
    }

    //Menampilkan jumlah susunan data berdasarkan pengulangan
    @Override
    public int getItemCount() {
        return listProduk.size();
    }

    //Kelas utama ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {

        //Inisialisasi Objek
        ImageView ivGambarRiwayat;
        TextView tv_Tgl, tv_Jam, tv_Total, tvSku, tvNama, tvHarga, tvStok;
        View rootView;

        //Inisialisasi variabel
        Uri uriGambar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivGambarRiwayat = itemView.findViewById(R.id.iv_gambar_riwayat);
            tv_Tgl = itemView.findViewById(R.id.tv_tgl_transaksi);
            tv_Jam = itemView.findViewById(R.id.tv_jam_transaksi);
            tv_Total = itemView.findViewById(R.id.tv_total_harga);
            tvSku = itemView.findViewById(R.id.tv_sku_produk);
            tvNama = itemView.findViewById(R.id.tv_nama_produk);
            tvHarga = itemView.findViewById(R.id.tv_harga_produk);
            tvStok = itemView.findViewById(R.id.tv_stok_produk);
            rootView = itemView.findViewById(R.id.root_row_produk);

        }

        //Fungsi untuk ngeset data di adapter (fungsi onBindViewHolder)
        void bindData(ProdukRiwayat produk) {

            tv_Tgl.setText(produk.getTglTransaksi());
            tv_Jam.setText(produk.getJamTransaksi());
            tv_Total.setText("Total Harga : " + produk.getTotalHarga());
            tvSku.setText(produk.getSku());
            tvNama.setText(produk.getNama());
            tvHarga.setText("Rp. " + produk.getHarga());
            tvStok.setText("Jumlah : " + produk.getStok());

            uriGambar = Uri.parse(produk.getGambar());
            ivGambarRiwayat.setImageURI(uriGambar);

            //Fungsi saat item adapter diklik
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }

    }
}
