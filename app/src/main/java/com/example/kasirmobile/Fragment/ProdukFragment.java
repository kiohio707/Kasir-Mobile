package com.example.kasirmobile.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.kasirmobile.Activity.MainActivity;
import com.example.kasirmobile.Activity.TambahManualActivity;
import com.example.kasirmobile.Activity.TambahOtomatisActivity;
import com.example.kasirmobile.Adapter.RecProdukAdapter;
import com.example.kasirmobile.Database.DbHelper;
import com.example.kasirmobile.Model.Produk;
import com.example.kasirmobile.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.List;

public class ProdukFragment extends Fragment {

    //Inisialisasi objek
    View rootView;
    RecProdukAdapter adapter;
    DbHelper dbHelper;

    //Inisialisasi tampilan view
    RecyclerView recProduk;
    FloatingActionButton fabAdd;
    SearchView searchView;
    ImageView ivProdukKosong;
    TextView tvProdukKosongTitle, tvProdukKosongDesc;

    //Inisialisasi variabel
    List<Produk> listProduk;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_produk, container, false);

        initView();

        return rootView;
    }

    //Fungsi untuk merefresh fragment saat ada perubahan
    @Override
    public void onResume() {
        super.onResume();

        ivProdukKosong.setVisibility(View.GONE);
        tvProdukKosongTitle.setVisibility(View.GONE);
        tvProdukKosongDesc.setVisibility(View.GONE);

        initRecyclerData("");
    }

    //Fungsi inisilisasi tampilan
    void initView(){
        dbHelper = new DbHelper(getContext());

        //Inisialisasi koneksi view
        recProduk = rootView.findViewById(R.id.rec_produk);
        fabAdd = rootView.findViewById(R.id.fab_add_produk);
        searchView = rootView.findViewById(R.id.src_frag_produk);
        ivProdukKosong = rootView.findViewById(R.id.iv_produk_kosong);
        tvProdukKosongTitle = rootView.findViewById(R.id.tv_produk_kosong_title);
        tvProdukKosongDesc = rootView.findViewById(R.id.tv_produk_kosong_description);

        //Menyembunyikan informasi produk kosong
        ivProdukKosong.setVisibility(View.GONE);
        tvProdukKosongTitle.setVisibility(View.GONE);
        tvProdukKosongDesc.setVisibility(View.GONE);

        //Fungsi untuk mencari data produk
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                initRecyclerDataCari(newText);
                return false;
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetMenuTambah();
            }
        });

        initRecyclerData("");
    }

    //Fungsi inisialisasi RecyclerView
    void initRecyclerData(String nama){
        listProduk = dbHelper.getAllProduk(nama);

        //Jika produk kosong maka munculkan informasi kosong
        if(listProduk.size() == 0) {
            ivProdukKosong.setVisibility(View.VISIBLE);
            tvProdukKosongTitle.setVisibility(View.VISIBLE);
            tvProdukKosongDesc.setVisibility(View.VISIBLE);
        }

        adapter = new RecProdukAdapter(getContext(), listProduk);

        //Mengatur bentuk tampilan dari RecylerView
        recProduk.setLayoutManager(new LinearLayoutManager(getContext()));
        recProduk.setAdapter(adapter);
    }

    //Fungsi inisialisasi RecyclerView
    void initRecyclerDataCari(String nama){
        listProduk = dbHelper.getAllProduk(nama);

        adapter = new RecProdukAdapter(getContext(), listProduk);

        //Mengatur bentuk tampilan dari RecylerView
        recProduk.setLayoutManager(new LinearLayoutManager(getContext()));
        recProduk.setAdapter(adapter);
    }

    //Fungsi untuk menampilkan menu bottomsheet tambah produk
    void showBottomSheetMenuTambah() {
        BottomSheetDialog btmSheetMenu = new BottomSheetDialog(getContext());

        View rootMenuView = LayoutInflater.from(getContext()).inflate(R.layout.view_btm_sheet__tambah_produk,null,false);

        LinearLayout menuTambahManual = rootMenuView.findViewById(R.id.menu_tambah_manual);
        LinearLayout menuTambahOtomatis = rootMenuView.findViewById(R.id.menu_tambah_otomatis);

        btmSheetMenu.setContentView(rootMenuView);
        btmSheetMenu.show();

        menuTambahManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),TambahManualActivity.class));
                btmSheetMenu.dismiss();
            }
        });

        menuTambahOtomatis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TambahOtomatisActivity.class));
                btmSheetMenu.dismiss();
            }
        });
    }
}
