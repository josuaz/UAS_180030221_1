package com.bh183.pranajaya;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
public class DatabaseHandler extends SQLiteOpenHelper {
    private final static int DATABASE_VERSION = 1;
    private final static String DATABASE_NAME = "db_buku";
    private final static String TABLE_BUKU = "tb_buku";
    private final static String KEY_ID = "id";
    private final static String KEY_JUDULBUKU = "judulbuku";
    private final static String KEY_PENULIS = "penulis";
    private final static String KEY_PENERBIT = "penerbit";
    private final static String KEY_TAHUNTERBIT = "tahunterbit";
    private final static String KEY_COVER = "cover";
    private final static String KEY_SINOPSIS = "sinopsis";
    private final static String KEY_LINK = "link";
    private SimpleDateFormat sdFormat = new SimpleDateFormat("dd/MM/yyyy");
    private  Context context;

    public DatabaseHandler(Context ctx){
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = ctx;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_BUKU = "CREATE TABLE " + TABLE_BUKU
                + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_JUDULBUKU + " TEXT, " + KEY_PENULIS + " TEXT, "
                + KEY_PENERBIT + " TEXT, " + KEY_TAHUNTERBIT + " TEXT, "
                + KEY_COVER + " TEXT, " + KEY_SINOPSIS + " TEXT, "
                + KEY_LINK + " TEXT);";

        db.execSQL(CREATE_TABLE_BUKU);
        inisialisasiBeritaAwal(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_BUKU;
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
    public void tambahBuku(Buku dataBuku) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDULBUKU, dataBuku.getJudulbuku());
        cv.put(KEY_PENULIS, dataBuku.getPenulis());
        cv.put(KEY_PENERBIT, dataBuku.getPenerbit());
        cv.put(KEY_TAHUNTERBIT, sdFormat.format(dataBuku.getTahunterbit()));
        cv.put(KEY_COVER, dataBuku.getCover());
        cv.put(KEY_SINOPSIS, dataBuku.getSinopsis());
        cv.put(KEY_LINK, dataBuku.getLink());

        db.insert(TABLE_BUKU, null, cv);
        db.close();
    }

    public void tambahBuku(Buku dataBuku, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDULBUKU, dataBuku.getJudulbuku());
        cv.put(KEY_PENULIS, dataBuku.getPenulis());
        cv.put(KEY_PENERBIT, dataBuku.getPenerbit());
        cv.put(KEY_TAHUNTERBIT, sdFormat.format(dataBuku.getTahunterbit()));
        cv.put(KEY_COVER, dataBuku.getCover());
        cv.put(KEY_SINOPSIS, dataBuku.getSinopsis());
        cv.put(KEY_LINK, dataBuku.getLink());
        db.insert(TABLE_BUKU, null, cv);
    }

    public void editBuku(Buku dataBuku) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDULBUKU, dataBuku.getJudulbuku());
        cv.put(KEY_PENULIS, dataBuku.getPenulis());
        cv.put(KEY_PENERBIT, dataBuku.getPenerbit());
        cv.put(KEY_TAHUNTERBIT, sdFormat.format(dataBuku.getTahunterbit()));
        cv.put(KEY_COVER, dataBuku.getCover());
        cv.put(KEY_SINOPSIS, dataBuku.getSinopsis());
        cv.put(KEY_LINK, dataBuku.getLink());

        db.update(TABLE_BUKU, cv, KEY_ID + "=?", new String[]{String.valueOf(dataBuku.getId())});

        db.close();
    }

    public void hapusBuku(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_BUKU, KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public ArrayList<Buku> getAllBuku() {
        ArrayList<Buku> dataBuku = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_BUKU;
        SQLiteDatabase db = getReadableDatabase();
        Cursor csr = db.rawQuery(query, null);
        if (csr.moveToFirst()){
            do {
                Date tempDate = new Date();
                try {
                    tempDate = sdFormat.parse(csr.getString(4));
                } catch (ParseException er) {
                    er.printStackTrace();
                }

                Buku tempBuku = new Buku(
                        csr.getInt(0),
                        csr.getString(1),
                        csr.getString(2),
                        csr.getString(3),
                        tempDate,
                        csr.getString(5),
                        csr.getString(6),
                        csr.getString(7)
                );

                dataBuku.add(tempBuku);
            } while (csr.moveToNext());
        }

        return dataBuku;
    }

    private String storeImageFile(int id){
        String location;
        Bitmap image = BitmapFactory.decodeResource(context.getResources(), id);
        location = InputActivity.saveImageToStorage(image, context);
        return location;

    }

    private void inisialisasiBeritaAwal(SQLiteDatabase db) {
        int id = 0;
        Date tempDate = new Date();

        //Menambah data Buku ke-1
        try {
            tempDate = sdFormat.parse("13/03/2014");
        } catch (ParseException er) {
            er.printStackTrace();
        }



        Buku buku1 =  new Buku(
                id,
                "That Time I Got Reincarnated as a Slime Vol 1: Empowerment",
                "Fuse",
                "Micro Magazine",
                tempDate,
                storeImageFile(R.drawable.buku1),
                "Tensei Shitara Slime Datta Ken atau setelah ini kita singkat sebagai Slime adalah novel ber-genre isekai / dunia lain yang menceritakan tentang Minami Satoru, seorang laki-laki biasa berumur 37 tahun, yang mati setelah tertusuk pencuri di jalan dan berenkarnasi menjadi seekor Slime di sebuah dunia fantasi.\n" +
                        "\n" +
                        "Meskipun seorang Slime bisa dibilang lemah lemah, tetapi Satoru mendapatkan kemampuan yang bisa dibilang cukup Over Power semenjak berenkarnasi. Pertama, dia memiliki kemampuan [Predator] yang bisa digunakan untuk memakan musuh dan mengambil kemampuannya serta [Great Sage] yang bisa membuatnya mengerti segala sesuatunya bekerja.",
                "https://shopee.co.id/Ready-Stock-That-Time-I-Got-Reincarnated-As-A-Slime-Vol.-1-(Light-Novel)-Limited-Stock-i.117234860.6616319016");

        tambahBuku(buku1, db);
        id++;

    }
}
