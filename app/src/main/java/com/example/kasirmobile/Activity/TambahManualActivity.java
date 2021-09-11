package com.example.kasirmobile.Activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.kasirmobile.Database.DbHelper;
import com.example.kasirmobile.Model.Produk;
import com.example.kasirmobile.R;
import com.google.android.material.button.MaterialButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class TambahManualActivity extends AppCompatActivity {

    //Inisialisasi objek view
    TextView tvJudul;
    EditText edtSku, edtNama, edtHarga, edtStok;
    MaterialButton btnTambah;
    ImageView ivGambar;

    //Inisialisasi objek
    DbHelper dbHelper;
    Produk produk;
    Produk newProduk;

    //Inisialisasi variabel
    Boolean isEdit = false;
    Boolean isAllFieldsChecked = false;
    Boolean skuChecked = false;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;
    private static final int IMAGE_PICK_CAMERA_CODE = 102;
    private static final int IMAGE_PICK_GALERRY_CODE = 103;

    private String[] cameraPermissions;
    private String[] storagePermissions;

    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_manual);

        //Membuat tombol kembali di Toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Tambah Manual");

        initView();

    }

    //Fungsi saat tombol kembali di Toolbar ditekan
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    //Fungsi untuk inisialisasi view
    private void initView() {
        dbHelper = new DbHelper(this);
        produk = getIntent().getParcelableExtra("dataproduk");

        tvJudul = findViewById(R.id.tv_judul);
        ivGambar = findViewById(R.id.iv_gambar);
        edtSku = findViewById(R.id.edt_tambah_produk_sku);
        edtNama = findViewById(R.id.edt_tambah_produk_nama);
        edtHarga = findViewById(R.id.edt_tambah_produk_harga);
        edtStok = findViewById(R.id.edt_tambah_produk_stok);
        btnTambah = findViewById(R.id.mbt_tambah);

        cameraPermissions = new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE};


        //Fungsi saat ImageButton ditekan untuk memasukkan gambar
        ivGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickDialog();
            }
        });

        //Jika data tidak kosong (update) maka set EditText dengan data yang sudah ada
        if (produk != null) {
            isEdit = true;

            getSupportActionBar().setTitle("Perbarui Produk");
            edtSku.setEnabled(false);
            btnTambah.setText("Perbarui");
            tvJudul.setText("Perbarui Data Produk");

            imageUri = Uri.parse(produk.getGambar());
            ivGambar.setImageURI(imageUri);

            edtSku.setText(produk.getSku());
            edtNama.setText(produk.getNama());
            edtHarga.setText(produk.getHarga());
            edtStok.setText(produk.getStok());
        }

        //Fungsi pada tombol tambah
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAllFieldsChecked = checkAllFields();

                if (isAllFieldsChecked) {
                    tambahkeDatabase();
                }
            }
        });
    }

    //Fungsi untuk mengecek inputan pengguna
    private boolean checkAllFields() {

        if (edtSku.length() == 0) {
            edtSku.setError("Wajib diisi");
            return false;
        } else if (edtSku.length() < 13) {
            edtSku.setError("SKU terdiri dari 13 digit angka");
            return false;
        }

        if(edtNama.length() == 0) {
            edtNama.setError("Wajib diisi");
            return false;
        }

        if(edtHarga.length() == 0) {
            edtHarga.setError("Wajib diisi");
            return false;
        } else if (edtHarga.length() < 3) {
            edtHarga.setError("Harga harus lebih dari 2 digit angka");
            return false;
        }

        if(edtStok.length() == 0) {
            edtStok.setError("Wajib diisi");
            return false;
        } else if (edtStok.length() < 1) {
            edtStok.setError("Stok minimal terdiri dari 1 digit angka");
            return false;
        }

        return true;
    }

    //Fungsi untuk mengecek apakah sku sudah terdaftar atau belum
    private boolean checkSku(){
        produk = dbHelper.getProdukBySKU(edtSku.getText().toString());
        if (produk.getNama() != null) {
            edtSku.setError("SKU ini sudah ada");
            return false;
        }
        return true;
    }

    //Fungsi untuk memunculkan dialog pilih sumber gambar
    private void imagePickDialog() {
        String [] options = {"Ambil dengan Kamera", "Pilih dari Galeri"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Masukkan Gambar");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    //Ambil dengan kamera
                    if(!checkCameraPermissions()) {
                        requestCameraPermission();
                    } else {
                        //Permission diijinkan
                        pickFromCamera();
                    }
                } else if (which == 1) {
                    //Pilih dari galeri
                    if(!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        //Permission diijinkan
                        pickFromGallery();
                    }
                }
            }
        });

        AlertDialog dialog = builder.create();

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_bg);
        dialog.show();
    }

    //Fungsi untuk mengecek Permission penyimpanan
    private boolean checkStoragePermission(){

        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    //Fungsi untuk meminta Permission penyimpanan
    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    //Fungsi untuk mengecek Permission penyimpanan
    public boolean checkCameraPermissions(){
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return  result && result1;
    }

    //Fungsi untuk meminta Permission kamera
    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    //Fungsi lanjutan dari permintaan Permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted && storageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "Izin kamera dan penyimpanan dibutuhkan", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;

            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Izin penyimpanan dibutuhkan", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //Fungsi mengambil gambar dari galeri, dilanjutkan dengan fungsi onActivityResult
    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALERRY_CODE);
    }

    //Fungsi mengambil gambar dari kamera, dilanjutkan dengan fungsi onActivityResult
    private void pickFromCamera() {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Image title");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Image description");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);
    }

    //Fungsi lanjutan setelah ambil gambar
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALERRY_CODE) {

                //Crop gambar
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);

            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {

                //Crop gambar
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);

                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    imageUri = resultUri;
                    ivGambar.setImageURI(resultUri);

                } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    //Error
                    Exception error = result.getError();
                    Toast.makeText(this, "Error" + error, Toast.LENGTH_SHORT).show();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //Fungsi untuk menambahkan data baru
    void tambahkeDatabase() {
        String sku = edtSku.getText().toString();
        String nama = edtNama.getText().toString();
        String harga = edtHarga.getText().toString();
        String stok = edtStok.getText().toString();
        String gambar;

        if(imageUri == null) {

            imageUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.ic_kardus);

            gambar = imageUri.toString();

        } else {

            gambar = imageUri.toString();

        }

        //Jika update maka ambil id dari produk yang dipilih
        if (isEdit) {
            newProduk = new Produk(
                    this.produk.getId(),
                    sku,
                    nama,
                    harga,
                    stok,
                    gambar
            );
        } else {
            newProduk = new Produk(
                    "0",
                    sku,
                    nama,
                    harga,
                    stok,
                    gambar
            );

        }

        //Jika data kosong maka tambah data, jika ada maka update data
        if (isEdit) {
            dbHelper.updateProduk(newProduk);
            dbHelper.updateKeranjang(newProduk);
            finish();
        } else {
            skuChecked = checkSku();
            if (skuChecked) {
                dbHelper.addProduk(newProduk);
                finish();
            }
        }

    }
}