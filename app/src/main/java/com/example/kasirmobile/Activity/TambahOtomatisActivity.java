package com.example.kasirmobile.Activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.List;

public class TambahOtomatisActivity extends AppCompatActivity {

    //Inisialisasi objek view
    DecoratedBarcodeView barcodeView;
    EditText edtSku, edtNama, edtHarga, edtStok;
    MaterialButton btnTambah;
    ImageView ivGambarOtomatis;

    //Inisialisasi objek
    Produk produk;
    DbHelper dbHelper;

    //Inisialisasi variabel
    Boolean isEdit = false;
    Boolean isAllFieldsChecked = false;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;
    private static final int IMAGE_PICK_CAMERA_CODE = 102;
    private static final int IMAGE_PICK_GALERRY_CODE = 103;

    private String[] cameraPermissions;
    private String[] storagePermissions;

    Uri imageUri;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_otomatis);

        getSupportActionBar().setTitle("Tambah Otomatis");

        //Membuat tombol kembali di Toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initView();
        cekPermission();
    }

    //Fungsi saat tombol kembali di Toolbar ditekan
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    //Fungsi untuk inisialisasi view
    private void initView() {
        dbHelper = new DbHelper(this);

        ivGambarOtomatis = findViewById(R.id.iv_gambar_otomatis);
        barcodeView = findViewById(R.id.barcode_view);
        edtSku = findViewById(R.id.edt_tambah_produk_sku_otomatis);
        edtNama = findViewById(R.id.edt_tambah_produk_nama_otomatis);
        edtHarga = findViewById(R.id.edt_tambah_produk_harga_otomatis);
        edtStok = findViewById(R.id.edt_tambah_produk_stok_otomatis);
        btnTambah = findViewById(R.id.mbt_tambah_otomatis);

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        //Fungsi saat ImageButton ditekan untuk memasukkan gambar
        ivGambarOtomatis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickDialog();
            }
        });

        //Fungsi saat tombol tambah ditekan
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAllFieldsChecked = checkAllFields();
                if (isAllFieldsChecked) {
                    if (imageUri == null) {
                        imageUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.ic_kardus);
                    } else {
                        imageUri.toString();
                    }

                    Produk newProduk = new Produk(
                            isEdit ? produk.getId() : "0",
                            edtSku.getText().toString(),
                            edtNama.getText().toString(),
                            edtHarga.getText().toString(),
                            edtStok.getText().toString(),
                            imageUri.toString()

                    );
                    if (isEdit) {
                        dbHelper.updateProduk(newProduk);
                    } else {
                        dbHelper.addProduk(newProduk);
                    }

                    finish();
                }

            }
        });
    }

    //Fungsi untuk mengecek inputan pengguna
    private boolean checkAllFields() {
        if(edtSku.length() == 0) {
            edtSku.setError("Harap scan barcode produk");
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
    //Fungsi untuk mengecek izin penggunaan kamera
    private void cekPermission() {
        String camera = Manifest.permission.CAMERA;

        if (ContextCompat.checkSelfPermission(this, camera) != PackageManager.PERMISSION_GRANTED) {
            //Jika SDK > 23 maka minta izin
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{camera}, 2);
            } else {
                startToScan();
            }
        } else {
            startToScan();
        }
    }

    //Fungsi untuk memunculkan dialog pilih sumber gambar
    private void imagePickDialog() {
        String[] options = {"Ambil dengan Kamera", "Pilih dari Galeri"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Masukkan Gambar");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    //Ambil dengan kamera
                    if (!checkCameraPermissions()) {
                        requestCameraPermission();
                    } else {
                        //Permission diijinkan
                        pickFromCamera();
                    }
                } else if (which == 1) {
                    //Pilih dari galeri
                    if (!checkStoragePermission()) {
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
    private boolean checkStoragePermission() {

        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    //Fungsi untuk meminta Permission penyimpanan
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    //Fungsi untuk mengecek Permission penyimpanan
    public boolean checkCameraPermissions() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    //Fungsi untuk meminta Permission kamera
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    //Fungsi lanjutan dari permintaan Permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startToScan();
        } else {
            Toast.makeText(this, "Anda Tidak Dapat Menggunakan Fitur Ini", Toast.LENGTH_SHORT).show();
            finish();
        }

        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted) {
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
        values.put(MediaStore.Images.Media.TITLE, "Image title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image description");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    //Fungsi lanjutan setelah ambil gambar
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALERRY_CODE) {

                //Crop gambar
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);

            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {

                //Crop gambar
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);

                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    imageUri = resultUri;
                    ivGambarOtomatis.setImageURI(resultUri);

                } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    //Error
                    Exception error = result.getError();
                    Toast.makeText(this, "Error" + error, Toast.LENGTH_SHORT).show();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //Fungsi untuk mereset data pada EditText saat berganti data SKU hasil scan
    void resetInput() {
        edtNama.requestFocus();
        edtNama.setText("");
        edtHarga.setText("");
        edtStok.setText("");
    }

    //Fungsi untuk menjalankan barcode scanner
    void startToScan() {
        barcodeView.resume();
        barcodeView.setStatusText("Arahkan ke Barcode");

        //Method untuk menerima hasil barcode scanner
        BarcodeCallback callback = new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                resetInput();
                edtSku.setText(result.getText());

                produk = dbHelper.getProdukBySKU(result.getText());

                if (produk.getNama() != null) {
                    isEdit = true;
                    btnTambah.setText("Perbarui");

                    ivGambarOtomatis.setImageURI(Uri.parse(produk.getGambar()));
                    edtNama.setText(produk.getNama());
                    edtHarga.setText(produk.getHarga());
                    edtStok.setText(produk.getStok());
                } else {
                    resetInput();
                    btnTambah.setText("Tambah");
                }
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {

            }
        };
        barcodeView.decodeSingle(callback);
    }
}
