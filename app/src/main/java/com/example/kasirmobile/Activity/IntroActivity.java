package com.example.kasirmobile.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.kasirmobile.Adapter.IntroViewPagerAdapter;
import com.example.kasirmobile.Model.ScreenItem;
import com.example.kasirmobile.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    //Inisialisasi Tampilan view
    ViewPager viewScreenPager;
    TabLayout tabIndicator;
    MaterialButton btnLanjut, btnMulai;
    Animation btnAnimation;

    //Inisialisasi Objek
    IntroViewPagerAdapter introViewPagerAdapter;

    //Inisialisasi Tipe Data
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_intro);
        super.onCreate(savedInstanceState);

        //Mengecek SharedPreferences agar activity ini tidak terbuka terus
        if (restorePrefsData()) {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }

        //Init View
        tabIndicator = findViewById(R.id.tab_indicator);
        btnLanjut = findViewById(R.id.btn_lanjut);
        btnMulai = findViewById(R.id.btn_mulai);
        btnAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_mulai_animation);


        //Menyembunyikan toolbar
        getSupportActionBar().hide();

        //Membuat dan mengisi list screen
        List<ScreenItem> listScreenItem = new ArrayList<>();
        listScreenItem.add(new ScreenItem("Kelola Produk",
                "Tambah, Ubah, dan Hapus Data Produk Dagangan Anda",
                "*Agar aplikasi berjalan lancar harap mengelola produk terlebih dahulu",
                R.drawable.ic_produk_berwarna));
        listScreenItem.add(new ScreenItem("Gunakan Barcode",
                "Gunakan Kamera Layaknya Barcode Scanner",
                "*Untuk menggunakan fitur ini harap izinkan aplikasi mengakses kamera",
                R.drawable.ic_barcode_scannercolored));
        listScreenItem.add(new ScreenItem("Isi Keranjang ",
                "Buat Daftar Produk Yang Dibeli Pelanggan",
                "*Keranjang hanya dapat diisi dengan produk yang telah dikelola sebelumnya",
                R.drawable.ic_keranjang_berwarna));
        listScreenItem.add(new ScreenItem("Hitung Cepat",
                "Total Harga Belanja Pelanggan Terhitung Otomatis",
                "*Total harga diperoleh dari jumlah harga satuan produk dikali banyaknya ",
                R.drawable.ic_calculator));
        listScreenItem.add(new ScreenItem("Lihat Riwayat",
                "Catat dan Lihat Setiap Transaksi Yang Dilakukan",
                "*Setiap transaksi yang diproses akan otomatis tercatat",
                R.drawable.ic_riwayat_berwarna));

        //Konfigurasi ViewPager
        viewScreenPager = findViewById(R.id.view_pager_screen);
        introViewPagerAdapter = new IntroViewPagerAdapter(this, listScreenItem);
        viewScreenPager.setAdapter(introViewPagerAdapter);

        //Konfigurasi TabLayout dengan View Pager
        tabIndicator.setupWithViewPager(viewScreenPager);

        //Fungsi saat Button Lanjut ditekan
        btnLanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position = viewScreenPager.getCurrentItem();
                if (position < listScreenItem.size()) {
                    position++;
                    viewScreenPager.setCurrentItem(position);
                }

                //saat di berada di layar akhir sembunyikan Button Lanjut dan indikator
                //lalu munculkan tombol mulai
                if (position == listScreenItem.size() - 1 ) {
                    loadLastScreen();
                }

            }
        });

        //Fungsi Tab saat layar digeser
        tabIndicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == listScreenItem.size() - 1 ) {
                    loadLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Method saat Button Mulai ditekan
        btnMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Membuka MainActivity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                //Shared Preferences untuk menyimpan informasi bahwa user sudah membuka Intro
                savePrefsData();
                finish();
            }
        });
    }

    //Fungsi mengecek Shared Preferences untuk menyimpan informasi bahwa user sudah membuka Intro
    private boolean restorePrefsData() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroOpenedBefore = preferences.getBoolean("isIntroOpened", false);
        return isIntroOpenedBefore;
    }
    // Fungsi Shared Preferences untuk menyimpan informasi bahwa user sudah membuka Intro
    private void savePrefsData() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isIntroOpened", true);
        editor.commit();
    }

    //Fungsi untuk menyembunyikan Button Lanjut dan Indikator lalu menampilkan Button Mulai
    private void loadLastScreen() {
        btnLanjut.setVisibility(View.INVISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        btnMulai.setVisibility(View.VISIBLE);
        btnMulai.setAnimation(btnAnimation);
    }
}