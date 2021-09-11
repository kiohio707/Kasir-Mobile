package com.example.kasirmobile.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kasirmobile.Activity.MainActivity;
import com.example.kasirmobile.Adapter.RecKeranjangAdapter;
import com.example.kasirmobile.Adapter.RecStrukAdapter;
import com.example.kasirmobile.Database.DbHelper;
import com.example.kasirmobile.Model.Produk;
import com.example.kasirmobile.Model.ProdukRiwayat;
import com.example.kasirmobile.R;
import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class KeranjangFragment extends Fragment{

    //Inisialisasi objek
    View rootView;
    RecKeranjangAdapter adapter;
    DbHelper dbHelper;
    Produk produkFromAlert;
    Calendar calendar;
    SimpleDateFormat dateFormat;
    SimpleDateFormat clockFormat;

    //Inisialisasi objek View
    RecyclerView recKeranjang;
    TextView tvTotalKeranjang;
    MaterialButton btnProses;
    MaterialButton btnReset;
    FloatingActionButton fabTambahKeranjang;
    ImageView ivKeranjangKosong;
    TextView tvKeranjangKosongTitle, tvKeranjangKosongDesc;

    //Inisialisasi Variabel
    List<Produk> listKeranjang, listKeranjangStruk;
    int totalHarga = 0;

    //Inisialisasi variabel
    Uri uriGambar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_keranjang, container, false);

        initView();

        return rootView;
    }

    //Fungsi inisilisasi tampilan
    private void initView() {
        dbHelper = new DbHelper(getContext());

        recKeranjang = rootView.findViewById(R.id.rec_keranjang);
        tvTotalKeranjang = rootView.findViewById(R.id.tv_total_keranjang);
        btnProses = rootView.findViewById(R.id.btn_proses);
        btnReset = rootView.findViewById(R.id.btn_reset);
        fabTambahKeranjang = rootView.findViewById(R.id.fab_add_keranjang);
        ivKeranjangKosong = rootView.findViewById(R.id.iv_keranjang_kosong);
        tvKeranjangKosongTitle = rootView.findViewById(R.id.tv_keranjang_kosong_title);
        tvKeranjangKosongDesc = rootView.findViewById(R.id.tv_keranjang_kosong_description);

        //Menyembunyikan informasi keranjang kosong
        ivKeranjangKosong.setVisibility(View.GONE);
        tvKeranjangKosongTitle.setVisibility(View.GONE);
        tvKeranjangKosongDesc.setVisibility(View.GONE);

        //Fungsi saat FAB Tambah ditekan
        fabTambahKeranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekPermission();
            }
        });

        //Fungsi saat Button Proses ditekan
        btnProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertProses = new AlertDialog.Builder(getContext());

                calendar = Calendar.getInstance();
                dateFormat = new SimpleDateFormat("EEE, d MMM, yyyy");
                clockFormat = new SimpleDateFormat("hh:mm:ss a");

                View alertViewProses = LayoutInflater.from(getContext()).inflate(R.layout.view_alert_proses,null,false);

                //Inisiliasasi objek view
                EditText edtTotalBelanja, edtUang;
                TextView tvKembalian;

                //Inisialisasi koneksi objek view
                edtTotalBelanja = alertViewProses.findViewById(R.id.edt_total_belanja);
                edtUang = alertViewProses.findViewById(R.id.edt_uang);
                tvKembalian = alertViewProses.findViewById(R.id.tv_kembalian);

                alertProses.setView(alertViewProses);
                edtUang.requestFocus();

                edtTotalBelanja.setText(String.valueOf(totalHarga/2));

                alertProses
                        .setTitle("Pembayaran")
                        .setPositiveButton("Bayar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //Dialog menampilkan Struk Pembelian Untuk Diproses
                                AlertDialog.Builder alertProsesStruk = new AlertDialog.Builder(getContext());

                                //Inisialisasi objek
                                RecStrukAdapter strukAdapter;
                                RecyclerView recStruk;
                                TextView tvTotalStruk, tvDibayarkanStruk, tvKembalianStruk, tvTanggalStruk;

                                View alertViewtStruk = LayoutInflater.from(getContext()).inflate(R.layout.view_alert_proses_struk, null, false);

                                //Inisiliasasi objek view
                                recStruk = alertViewtStruk.findViewById(R.id.rec_struk);
                                tvTotalStruk = alertViewtStruk.findViewById(R.id.tv_total_harga_struk);
                                tvDibayarkanStruk = alertViewtStruk.findViewById(R.id.tv_dibayarkan_struk);
                                tvKembalianStruk = alertViewtStruk.findViewById(R.id.tv_kembalian_struk);
                                tvTanggalStruk = alertViewtStruk.findViewById(R.id.tv_tanggal_struk);

                                listKeranjangStruk = dbHelper.getAllKeranjang("");

                                strukAdapter = new RecStrukAdapter(getContext(), listKeranjangStruk);
                                recStruk.setLayoutManager(new LinearLayoutManager(getContext()));
                                recStruk.setAdapter(strukAdapter);

                                tvTotalStruk.setText("Rp. " + String.valueOf(totalHarga/2));
                                tvDibayarkanStruk.setText("Rp. " + String.valueOf(edtUang.getText()));
                                tvKembalianStruk.setText("Rp. " + (Integer.parseInt(String.valueOf(edtUang.getText())) - Integer.parseInt(String.valueOf(edtTotalBelanja.getText()))));
                                tvTanggalStruk.setText(dateFormat.format(calendar.getTime()) + " " + clockFormat.format(calendar.getTime()));

                                alertProsesStruk.setView(alertViewtStruk);

                                alertProsesStruk
                                        .setTitle("Struk Pembelian")
                                        .setPositiveButton("Proses", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                for (Produk produk : listKeranjang) {
                                                    dbHelper.deleteKeranjang(produk.getId());
                                                    dbHelper.addHistory(new ProdukRiwayat(
                                                            "",
                                                            dateFormat.format(calendar.getTime()),
                                                            clockFormat.format(calendar.getTime()),
                                                            String.valueOf(totalHarga/2),
                                                            produk.getSku(),
                                                            produk.getNama(),
                                                            produk.getHarga(),
                                                            produk.getStok(),
                                                            produk.getGambar()
                                                    ));
                                                }

                                                PdfGenerator.getBuilder()
                                                        .setContext(getContext())
                                                        .fromViewSource()
                                                        .fromView(alertViewtStruk)
                                                        .setFileName("Struk")
                                                        .setFolderName("Struk Kasir Mobile")
                                                        .openPDFafterGeneration(true)
                                                        .build(new PdfGeneratorListener() {
                                                            @Override
                                                            public void onStartPDFGeneration() {

                                                            }

                                                            @Override
                                                            public void onFinishPDFGeneration() {

                                                            }
                                                        });

                                                onResume();
                                                ((MainActivity)getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new KeranjangFragment()).commit();
                                                Toast.makeText(getContext(), "Transaksi Berhasil", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                AlertDialog dialogProsesStruk = alertProsesStruk.create();
                                dialogProsesStruk.getWindow().setBackgroundDrawableResource(R.drawable.rounded_bg);
                                dialogProsesStruk.show();

                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = alertProses.create();

                dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_bg);
                dialog.show();

                ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                //Memantau perubahan pada Edit Text uang
                edtUang.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        try {
                            if (Integer.parseInt(String.valueOf(edtUang.getText())) < Integer.parseInt(String.valueOf(edtTotalBelanja.getText()))) {
                                edtUang.setError("Masukkan dengan benar");
                                ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                            } else {
                                tvKembalian.setText("Rp. " + (Integer.parseInt(String.valueOf(edtUang.getText())) - Integer.parseInt(String.valueOf(edtTotalBelanja.getText()))));
                                ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                            }
                        } catch (NumberFormatException ex) {
                            edtUang.setError("Masukkan dengan benar");
                            ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        });

        //Fungsi saat Button Reset ditekan
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertReset = new AlertDialog.Builder(getContext());

                alertReset
                        .setTitle("Reset Keranjang")
                        .setMessage("Yakin Reset Keranjang ?")
                        .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (Produk produk : listKeranjang) {
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
                                }
                                ((MainActivity)getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new KeranjangFragment()).commit();

                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = alertReset.create();
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_bg);
                dialog.show();

            }
        });

        initRecyclerData();
    }

    //Fungsi untuk mengecek izin penggunaan kamera
    private void cekPermission() {
        String camera = Manifest.permission.CAMERA;

        if (ContextCompat.checkSelfPermission(getContext(), camera) != PackageManager.PERMISSION_GRANTED) {
            //Jika SDK > 23 maka minta izin
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{camera}, 2);
            } else {
                showAlertScanProduk();
            }
        } else {
            showAlertScanProduk();
        }
    }

    //Fungsi lanjutan dari pengecekan izin kamera
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showAlertScanProduk();
        } else {
            Toast.makeText(getContext(), "Anda Tidak Dapat Menggunakan Fitur Ini", Toast.LENGTH_SHORT).show();
        }
    }

    //Fungsi untuk memunculkan dialog saat menekan FAB Tambah Keranjang
    private void showAlertScanProduk() {
        View alertView = LayoutInflater.from(getContext()).inflate(R.layout.view_alert_scan_to_add, null, false);

        //Inisiliasasi objek view
        TextView tvSku, tvNama, tvStok, tvHarga;
        ImageView ivGambarAlertKeranjang;
        NumberPicker numberPicker;
        DecoratedBarcodeView barcodeView;

        //Inisialisasi koneksi objek view
        tvSku = alertView.findViewById(R.id.tv_sku_alert_keranjang);
        tvNama = alertView.findViewById(R.id.tv_nama_alert_keranjang);
        tvHarga = alertView.findViewById(R.id.tv_harga_alert_keranjang);
        tvStok = alertView.findViewById(R.id.tv_stok_alert_keranjang);
        ivGambarAlertKeranjang = alertView.findViewById(R.id.iv_gambar_alert_keranjang);
        numberPicker = alertView.findViewById(R.id.number_picker_alert_keranjang);
        barcodeView = alertView.findViewById(R.id.barcode_view_alert_add_keranjang);


        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        alert
                .setTitle("Tambah ke Keranjang")
                .setView(alertView)
                .setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (produkFromAlert.getNama() != null) {
                            if (Integer.parseInt(produkFromAlert.getStok()) > 0) {
                                int stok = Integer.parseInt(produkFromAlert.getStok()) - numberPicker.getValue();

                                dbHelper.updateProduk(new Produk(
                                        produkFromAlert.getId(),
                                        produkFromAlert.getSku(),
                                        produkFromAlert.getNama(),
                                        produkFromAlert.getHarga(),
                                        String.valueOf(stok),
                                        produkFromAlert.getGambar()
                                ));

                                dbHelper.addKeranjang(new Produk(
                                        produkFromAlert.getId(),
                                        produkFromAlert.getSku(),
                                        produkFromAlert.getNama(),
                                        produkFromAlert.getHarga(),
                                        String.valueOf(numberPicker.getValue()),
                                        produkFromAlert.getGambar()
                                ));

                                ((MainActivity)getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new KeranjangFragment()).commit();

                                onResume();
                            } else {
                                Toast.makeText(getContext(), "Stok Kosong", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Produk Tidak Tersedia", Toast.LENGTH_SHORT).show();
                        }


                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_bg);

        dialog.show();

        ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        barcodeView.resume();
        barcodeView.setStatusText("Arahkan ke Barcode");

        BarcodeCallback callback = new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {

                produkFromAlert = dbHelper.getProdukBySKU(result.getText());

                if (produkFromAlert.getNama() != null) {
                    barcodeView.setStatusText("Produk Tersedia");
                    ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);

                    tvSku.setText(result.getText());
                    tvNama.setText(produkFromAlert.getNama());
                    tvHarga.setText("Rp " + produkFromAlert.getHarga());
                    tvStok.setText("Stok : " + produkFromAlert.getStok());

                    uriGambar = Uri.parse(produkFromAlert.getGambar());
                    ivGambarAlertKeranjang.setImageURI(uriGambar);

                    numberPicker.setMinValue(1);
                    numberPicker.setMaxValue(Integer.parseInt(produkFromAlert.getStok()));
                } else {
                    tvSku.setText("");
                    tvNama.setText("");
                    tvHarga.setText("");
                    tvStok.setText("");

                    barcodeView.setStatusText("Produk Tidak Tersedia");
                }

            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {

            }
        };

        barcodeView.decodeContinuous(callback);

    }

    //Fungsi untuk merefresh fragment saat ada perubahan
    @Override
    public void onResume() {
        super.onResume();

        initRecyclerData();
        List<Produk> list = dbHelper.getAllProduk("");
        if(list.size() == 0) {
            fabTambahKeranjang.setEnabled(false);
        }
    }

    //Fungsi inisialisasi RecyclerView
    void initRecyclerData() {
        listKeranjang = dbHelper.getAllKeranjang("");

        if(listKeranjang.size() == 0) {
            btnProses.setEnabled(false);
            btnReset.setEnabled(false);

            ivKeranjangKosong.setVisibility(View.VISIBLE);
            tvKeranjangKosongTitle.setVisibility(View.VISIBLE);
            tvKeranjangKosongDesc.setVisibility(View.VISIBLE);

        }

        hitungtotal();

        adapter = new RecKeranjangAdapter(getContext(), listKeranjang);

        recKeranjang.setLayoutManager(new LinearLayoutManager(getContext()));
        recKeranjang.setAdapter(adapter);
    }

    //Fungsi hitung total harga
    private void hitungtotal() {
        for (Produk produk : listKeranjang) {
            totalHarga += (Integer.parseInt(produk.getHarga()) * Integer.parseInt(produk.getStok()));
        }

        tvTotalKeranjang.setText("Rp. " + totalHarga/2);
    }

}
