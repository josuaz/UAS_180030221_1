package com.bh183.pranajaya;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class InputActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editJudul, editTanggal, editpenerbit, editPenulis, editsinopsis, editLink;
    private ImageView ivBuku;
    private DatabaseHandler dbHandler;
    private SimpleDateFormat sdFormat = new SimpleDateFormat("dd/MM/yyyy");
    private boolean updateData = false;
    private int idBuku = 0;
    private Button btnsimpan, btnPilihTanggal;
    private String tanggalBuku;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        editJudul = findViewById(R.id.edit_judul);
        editTanggal = findViewById(R.id.edit_tanggal);
        editpenerbit = findViewById(R.id.edit_penerbit);
        editPenulis = findViewById(R.id.edit_penulis);
        editsinopsis = findViewById(R.id.edit_sinopsis);
        editLink = findViewById(R.id.edit_link);
        ivBuku = findViewById(R.id.iv_cover);
        btnsimpan = findViewById(R.id.btn_simpan);
        btnPilihTanggal = findViewById(R.id.btn_pilih_tanggal);

        dbHandler =new DatabaseHandler(this);

        Intent terimaIntent = getIntent();
        Bundle data = terimaIntent.getExtras();
        if (data.getString("OPERASI").equals("insert")){
            updateData = false;
        }else {
            updateData = true;
            idBuku = data.getInt("ID");
            editJudul.setText(data.getString("JUDULBUKU"));
            editTanggal.setText(data.getString("TAHUNTERBIT"));
            editpenerbit.setText(data.getString("PENERBIT"));
            editPenulis.setText(data.getString("PENULIS"));
            editsinopsis.setText(data.getString("SINOPSIS"));
            editLink.setText(data.getString("LINK"));
            loadImageInternalStorage(data.getString("COVER"));
        }

        ivBuku.setOnClickListener(this);
        btnsimpan.setOnClickListener(this);
        btnPilihTanggal.setOnClickListener(this);
    }
    private void pickImage(){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(3,4)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                try {
                    Uri imageUri = result.getUri();
                    InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    String location = saveImageToStorage(selectedImage, getApplicationContext());
                    loadImageInternalStorage(location);
                }catch (FileNotFoundException er){
                    er.printStackTrace();
                    Toast.makeText(this, "Ada kesalahan dalam pemilihan gambar", Toast.LENGTH_SHORT).show();
                }
            }
        }else {
            Toast.makeText(this, "Anda belum memilih gambar", Toast.LENGTH_SHORT).show();
        }
    }

    public static String saveImageToStorage(Bitmap bitmap, Context ctx){
        ContextWrapper ctxWrapper = new ContextWrapper(ctx);
        File file = ctxWrapper.getDir("images", MODE_PRIVATE);
        String uniqueID = UUID.randomUUID().toString();
        file = new File(file, "buku-"+ uniqueID+ ".jpg");
        try {
            OutputStream stream = null;
            stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();
        } catch (IOException er){
            er.printStackTrace();

        }

        Uri savedImage = Uri.parse(file.getAbsolutePath());
        return savedImage.toString();
    }

    private  void loadImageInternalStorage(String imageLocation){
        try {
            File file = new File(imageLocation);
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            ivBuku.setImageBitmap(bitmap);
            ivBuku.setContentDescription(imageLocation);
        }catch (FileNotFoundException er){
            er.printStackTrace();
            Toast.makeText(this, "Gagal mengambil gambar dari media penyimpanan", Toast.LENGTH_SHORT).show();
        }
    }

    private void pilihTanggal(){
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog pickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tanggalBuku = dayOfMonth + "/" + month + "/" + year;
                editTanggal.setText(tanggalBuku);

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        pickerDialog.show();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.item_menu_hapus);

        if (updateData == true) {
            item.setEnabled(true);
            item.getIcon().setAlpha(255);
        }else {
            item.setEnabled(false);
            item.getIcon().setAlpha(130);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.input_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.item_menu_hapus){
            hapusData();
            finish();
        }

        return super.onOptionsItemSelected(item);

    }

    private void simpanData(){
        String judul, penerbit,cover, penulis, sinopsis, link;
        Date tanggal =  new Date();
        judul = editJudul.getText().toString();
        cover = ivBuku.getContentDescription().toString();
        penerbit = editpenerbit.getText().toString();
        penulis = editPenulis.getText().toString();
        sinopsis = editsinopsis.getText().toString();
        link = editLink.getText().toString();


        if (ivBuku != null && judul != ""){
            try {
                tanggal = sdFormat.parse(editTanggal.getText().toString());
            }catch (ParseException er){
                er.printStackTrace();
            }

            Buku tempBuku = new Buku(
                    idBuku, judul,penulis,penerbit, tanggal, cover, sinopsis, link
            );

            if (updateData == true){
                dbHandler.editBuku(tempBuku);
                Toast.makeText(this, "Data buku diperbaharui", Toast.LENGTH_SHORT).show();
            }else {
                dbHandler.tambahBuku(tempBuku);
                Toast.makeText(this, "Data buku ditambahkan", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
        else {
            Toast.makeText(this, "Harap mengisi judul dan cover buku...", Toast.LENGTH_SHORT).show();
        }

    }

    private void hapusData(){
        dbHandler.hapusBuku(idBuku);
        Toast.makeText(this, "Data berita dihapus", Toast.LENGTH_SHORT).show();
    }





    public void onClick(View v) {
        int idView = v.getId();

        if (idView == R.id.btn_simpan){
            simpanData();
        }else if(idView == R.id.iv_cover){
            pickImage();
        }else if (idView == R.id.btn_pilih_tanggal){
            pilihTanggal();
        }
    }
}
