package com.bh183.pranajaya;

import java.util.Date;

public class Buku {
    private int id;
    private String judulbuku;
    private String penulis;
    private String penerbit;
    private Date tahunterbit;
    private String cover;
    private String sinopsis;
    private String link;

    public Buku(int id, String judulbuku, String penulis, String penerbit, Date tahunterbit, String cover, String sinopsis, String link) {
        this.id = id;
        this.judulbuku = judulbuku;
        this.penulis = penulis;
        this.penerbit = penerbit;
        this.tahunterbit = tahunterbit;
        this.cover = cover;
        this.sinopsis = sinopsis;
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJudulbuku() {
        return judulbuku;
    }

    public void setJudulbuku(String judulbuku) {
        this.judulbuku = judulbuku;
    }

    public String getPenulis() {
        return penulis;
    }

    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }

    public String getPenerbit() {
        return penerbit;
    }

    public void setPenerbit(String penerbit) {
        this.penerbit = penerbit;
    }

    public Date getTahunterbit() {
        return tahunterbit;
    }

    public void setTahunterbit(Date tahunterbit) {
        this.tahunterbit = tahunterbit;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
