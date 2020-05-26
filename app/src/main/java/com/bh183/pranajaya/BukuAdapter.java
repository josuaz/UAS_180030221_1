package com.bh183.pranajaya;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class BukuAdapter extends RecyclerView.Adapter<BukuAdapter.BeritaViewHolder> {

    private Context context;
    private ArrayList<Buku> dataBuku;
    private SimpleDateFormat sdFormat = new SimpleDateFormat("dd/MM/yyyy");

    public BukuAdapter(Context context, ArrayList<Buku> dataBuku) {
        this.context = context;
        this.dataBuku = dataBuku;
    }

    @NonNull
    @Override
    public BeritaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_buku, parent, false);
        return new BeritaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BeritaViewHolder holder, int position) {
        Buku tempBuku = dataBuku.get(position);
        holder.idBuku = tempBuku.getId();
        holder.tvJudul.setText(tempBuku.getJudulbuku());
        holder.tvpenulis.setText(tempBuku.getPenulis());
        holder.tanggal = sdFormat.format(tempBuku.getTahunterbit());
        holder.cover = tempBuku.getCover();
        holder.sinopsis = tempBuku.getSinopsis();
        holder.penerbit = tempBuku.getPenerbit();
        holder.penulis = tempBuku.getPenulis();
        holder.link = tempBuku.getLink();
        try {
            File file = new File(holder.cover);
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            holder.imgcover.setImageBitmap(bitmap);
            holder.imgcover.setContentDescription(holder.cover);
        }catch (FileNotFoundException er){
            er.printStackTrace();
            Toast.makeText(context, "Gagal mengambil cover dari media penyimpanan", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return dataBuku.size();

    }

    public class BeritaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private ImageView imgcover;
        private TextView tvJudul, tvpenulis;
        private int idBuku;
        private String tanggal, cover, penerbit, penulis, link, sinopsis;

        public BeritaViewHolder(@NonNull View itemView) {
            super(itemView);

            imgcover = itemView.findViewById(R.id.iv_cover);
            tvJudul = itemView.findViewById(R.id.tv_judul);
            tvpenulis = itemView.findViewById(R.id.tv_penulis);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Intent bukaBerita = new Intent(context, TampilActivity.class);
            bukaBerita.putExtra("ID", idBuku);
            bukaBerita.putExtra("JUDULBUKU", tvJudul.getText().toString());
            bukaBerita.putExtra("TAHUNTERBIT", tanggal);
            bukaBerita.putExtra("COVER", cover);
            bukaBerita.putExtra("PENERBIT", penerbit);
            bukaBerita.putExtra("SINOPSIS", sinopsis);
            bukaBerita.putExtra("PENULIS", tvpenulis.getText().toString());
            bukaBerita.putExtra("LINK", link);
            context.startActivity(bukaBerita);

        }

        @Override
        public boolean onLongClick(View v) {

            Intent bukaInput = new Intent(context, InputActivity.class);
            bukaInput.putExtra("OPERASI", "update");
            bukaInput.putExtra("ID", idBuku);
            bukaInput.putExtra("JUDULBUKU", tvJudul.getText().toString());
            bukaInput.putExtra("TAHUNTERBIT", tanggal);
            bukaInput.putExtra("COVER", cover);
            bukaInput.putExtra("PENERBIT", penerbit);
            bukaInput.putExtra("SINOPSIS", sinopsis);
            bukaInput.putExtra("PENULIS", tvpenulis.getText().toString());
            bukaInput.putExtra("LINK", link);
            context.startActivity(bukaInput);
            return true;
        }
    }
}

