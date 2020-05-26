package com.bh183.pranajaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;

public class TampilActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgCover;
    private TextView tvJudulbuku, tvtahunterbit, tvpenerbit, tvPenulis, tvsinopsis;
    private String linkBuku;
    private Button btnBeli;
    private SimpleDateFormat sdFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil);

        imgCover = findViewById(R.id.iv_cover);
        tvJudulbuku = findViewById(R.id.tv_judulbuku);
        tvtahunterbit = findViewById(R.id.tv_tahunterbit);
        tvpenerbit = findViewById(R.id.tv_penerbit);
        tvPenulis = findViewById(R.id.tv_penulis);
        tvsinopsis = findViewById(R.id.tv_sinopsis);
        btnBeli = findViewById(R.id.beli);

        Intent terimaData = getIntent();
        tvJudulbuku.setText(terimaData.getStringExtra("JUDULBUKU"));
        tvtahunterbit.setText("Tahun Terbit : "+terimaData.getStringExtra("TAHUNTERBIT"));
        tvpenerbit.setText(terimaData.getStringExtra("PENERBIT"));
        tvPenulis.setText("Penulis : "+terimaData.getStringExtra("PENULIS"));
        tvsinopsis.setText(terimaData.getStringExtra("SINOPSIS"));
        String imgLocation = terimaData.getStringExtra("COVER");

        btnBeli.setOnClickListener(this);


        try {
            File file = new File(imgLocation);
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            imgCover.setImageBitmap(bitmap);
            imgCover.setContentDescription(imgLocation);
        }catch (FileNotFoundException er){
            er.printStackTrace();
            Toast.makeText(this, "Gagal mengambil gambar dari media penyimpanan", Toast.LENGTH_SHORT).show();
        }
        linkBuku = terimaData.getStringExtra("LINK");
        if (linkBuku.equals("")){
            btnBeli.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tampil_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_bagikan){
            Intent bagikanBerita = new Intent(Intent.ACTION_SEND);
            bagikanBerita.putExtra(Intent.EXTRA_SUBJECT, tvJudulbuku.getText().toString());
            bagikanBerita.putExtra(Intent.EXTRA_TEXT, linkBuku);
            bagikanBerita.setType("text/plain");
            startActivity(Intent.createChooser(bagikanBerita, "Bagikan Berita"));
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(linkBuku)));
    }
}