package com.example.kasirmobile.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kasirmobile.Database.DbHelper;
import com.example.kasirmobile.Fragment.*;
import com.example.kasirmobile.Activity.MainActivity;
import com.example.kasirmobile.Model.Produk;
import com.example.kasirmobile.R;

import java.util.List;

public class RecKeranjangAdapter extends RecyclerView.Adapter<RecKeranjangAdapter.ViewHolder> {

    //Inisialisasi objek
    Context context;
    List<Produk> listProduk;
    DbHelper dbHelper;

    //Konstruktor untuk meminta konteks
    public RecKeranjangAdapter(Context context, List<Produk> listProduk) {
        this.context = context;
        this.listProduk = listProduk;
        dbHelper = new DbHelper(context);
    }

    //Fungsi untuk tampilan recycleview
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(context).inflate(R.layout.row_keranjang,parent,false);
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
    public class ViewHolder extends RecyclerView.ViewHolder{

        //Inisialisasi objek view
        TextView tvSKu, tvNama, tvHarga, tvStok;
        View rootView;
        ImageView ivGambar;

        //Inisialisasi variabel
        Uri uriGambar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivGambar = itemView.findViewById(R.id.iv_gambar_keranjang);
            tvSKu = itemView.findViewById(R.id.tv_sku_produk);
            tvNama = itemView.findViewById(R.id.tv_nama_produk);
            tvHarga = itemView.findViewById(R.id.tv_harga_produk);
            tvStok = itemView.findViewById(R.id.tv_stok_produk);
            rootView = itemView.findViewById(R.id.root_row_produk);

        }

        //Fungsi untuk ngeset data di adapter (fungsi onBindViewHolder)
        void bindData(Produk produk) {
            tvSKu.setText(produk.getSku());
            tvNama.setText(produk.getNama());
            tvHarga.setText("Rp. " + produk.getHarga());
            tvStok.setText("Jumlah : " + produk.getStok());

            uriGambar = Uri.parse(produk.getGambar());
            ivGambar.setImageURI(uriGambar);

            //Fungsi saat item adapter diklik
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProdukDialog(produk);
                }
            });
        }

        //Fungsi untuk memunculkan dialog detail produk
        void showProdukDialog(Produk produk) {
            //Objek buat nyimpan stok produk
            Produk stokProduk = dbHelper.getProduk(produk.getId());

            AlertDialog.Builder alert = new AlertDialog.Builder(context);

            View viewAlert = LayoutInflater.from(context).inflate(R.layout.view_produk_dialog,null,false);

            //Inisialisasi koneksi view
            ImageView ivDialogGambar = viewAlert.findViewById(R.id.iv_dialog_produk_gambar);
            TextView tvDialogSku = viewAlert.findViewById(R.id.tv_dialog_produk_sku);
            TextView tvDialogNama = viewAlert.findViewById(R.id.tv_dialog_produk_nama);
            TextView tvDialogHarga = viewAlert.findViewById(R.id.tv_dialog_produk_harga);
            TextView tvDialogStok = viewAlert.findViewById(R.id.tv_dialog_produk_stok);
            NumberPicker numberPicker = viewAlert.findViewById(R.id.number_picker_jumlah);

            numberPicker.setMinValue(1);
            numberPicker.setMaxValue((Integer.parseInt(stokProduk.getStok()) + Integer.parseInt(produk.getStok())));

            uriGambar = Uri.parse(produk.getGambar());
            ivDialogGambar.setImageURI(uriGambar);

            tvDialogSku.setText(produk.getSku());
            tvDialogNama.setText(produk.getNama());
            tvDialogHarga.setText("Rp. " + produk.getHarga());
            tvDialogStok.setText("Jumlah : " + produk.getStok());


            alert.setTitle("Detail Produk");
            alert.setView(viewAlert);
            alert.setPositiveButton("Perbarui", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int stok = 0;

                    stok = (Integer.parseInt(stokProduk.getStok()) + Integer.parseInt(produk.getStok())) - numberPicker.getValue();

                    listProduk.set(getAdapterPosition(),new Produk(
                            produk.getId(),
                            produk.getSku(),
                            produk.getNama(),
                            produk.getHarga(),
                            String.valueOf(numberPicker.getValue()),
                            produk.getGambar()
                    ));
                    notifyDataSetChanged();

                    dbHelper.updateProduk(new Produk(
                            produk.getId(),
                            produk.getSku(),
                            produk.getNama(),
                            produk.getHarga(),
                            String.valueOf(stok),
                            produk.getGambar()
                    ));

                    dbHelper.updateStokKeranjang(new Produk(
                            produk.getId(),
                            produk.getSku(),
                            produk.getNama(),
                            produk.getHarga(),
                            String.valueOf(numberPicker.getValue()),
                            produk.getGambar()
                    ));
                    ((MainActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new KeranjangFragment()).commit();
                }
            });
            alert.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alert.setNeutralButton("Buang", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listProduk.remove(produk);
                    notifyDataSetChanged();

                    //Objek buat nyimpan stok produk
                    Produk stokProduk = dbHelper.getProduk(produk.getId());

                    //Variabel menyimpan stok akhir saat produk dihapus dan stok dikembalikan kesemula
                    int stokAkhirProduk = Integer.parseInt(stokProduk.getStok()) + Integer.parseInt(produk.getStok());

                    dbHelper.updateProduk(new Produk(
                            produk.getId(),
                            produk.getSku(),
                            produk.getNama(),
                            produk.getHarga(),
                            String.valueOf(stokAkhirProduk),
                            produk.getGambar()
                    ));
                    dbHelper.deleteKeranjang(produk.getId());
                    ((MainActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new KeranjangFragment()).commit();
                }
            });

            AlertDialog dialog = alert.create();
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_bg);
            dialog.show();
        }

    }

}
