# Kasir Mobile
Aplikasi kasir yang diharapkan nantinya pemilik usaha mikro dan kecil dapat memanajemen usahanya mulai dari inventarisasi produk hingga proses transaksi dengan pelanggan.

## Latar Belakang
Dikarenakan harga dari Barcode Scanner serta printer untuk mencetak struk atau bukti transaksi yang terlampau mahal bagi pemilik usaha mikro dan kecil, maka penulis memutuskan untuk membuat sebuah aplikasi mobile berbasis Android “Kasir Mobile”

![Gambar Kasir Mobile](https://user-images.githubusercontent.com/80617774/132947974-057b2835-67b1-49b6-9014-57037569a159.jpg)

## Fitur
1. Mesin kasir menghitung total belanja yang dilengkapi Barcode Reader jenis EAN-13
2. Output berupa struk belanja dengan format .pdf

## Teknologi
1. Menggunakan bahasa Java Android
2. Basis data menggunakan SQLite
3. Barcode Reader dari Zxing (https://github.com/zxing/zxing)
   `'implementation 'com.journeyapps:zxing-android-embedded:3.5.0'`
4. Image Croper (https://github.com/ArthurHub/Android-Image-Cropper)
   `'implementation 'com.theartofdev.edmodo:android-image-cropper:2.8.+'`
5. PDF Generator (https://github.com/Gkemon/Android-XML-to-PDF-Generator)
    `'implementation 'com.github.Gkemon:XML-to-PDF-generator:2.5'`

## Publikasi Ilmiah
Artikel ilmiah mengenai penelitian ini:
https://www.researchgate.net/publication/352978582_APLIKASI_KASIR_MOBILE_BERBASIS_ANDROID_UNTUK_USAHA_MIKRO_DAN_KECIL

## Cara Menginstall
Download APK Kasir Mobile dari repository di atas, kemudian install
