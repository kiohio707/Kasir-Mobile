package com.example.kasirmobile.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.kasirmobile.Activity.MainActivity;
import com.example.kasirmobile.Activity.TambahManualActivity;
import com.example.kasirmobile.Database.DbHelper;
import com.example.kasirmobile.Fragment.ProdukFragment;
import com.example.kasirmobile.Model.Produk;
import com.example.kasirmobile.R;

import java.util.List;

public class RecStrukAdapter extends RecyclerView.Adapter<RecStrukAdapter.ViewHolder> {

    //Inisialisasi objek
    Context context;
    List<Produk> listProduk;
    DbHelper dbHelper;

    //Konstruktor untuk meminta konteks
    public RecStrukAdapter(Context context, List<Produk> listProduk) {
        this.context = context;
        this.listProduk = listProduk;
        dbHelper = new DbHelper(context);
    }

    //Fungsi untuk tampilan recycleview
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(context).inflate(R.layout.row_produk_struk, parent, false);
        return new ViewHolder(rowView);
    }

    //Mengkoneksian adapter dengan tampilan (fungsi yang looping sebanyak data yang ada)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(listProduk.get(position));
    }

    //Menampilkan jumlah susunan data berdasarkan pengulangan
    @Override
    public int getItemCount() {
        return listProduk.size();
    }

    //Kelas utama ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {
        //Inisialisasi objek view
        TextView tvNama, tvHarga, tvJumlah;
        View rootView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNama = itemView.findViewById(R.id.tv_nama_produk_struk);
            tvJumlah = itemView.findViewById(R.id.tv_jumlah_produk_struk);
            tvHarga = itemView.findViewById(R.id.tv_harga_produk_struk);

            rootView = itemView.findViewById(R.id.root_row_produk);
        }

        //Fungsi untuk ngeset data di adapter (fungsi onBindViewHolder)
        void bindData(Produk produk) {
            tvNama.setText(produk.getNama());
            tvHarga.setText(produk.getHarga());
            tvJumlah.setText(produk.getStok());
        }
    }
}
