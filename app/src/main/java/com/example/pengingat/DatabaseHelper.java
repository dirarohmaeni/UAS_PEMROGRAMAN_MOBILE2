package com.example.pengingat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "pengingat.db";
    public static final int DB_VERSION = 3; // 🔥 WAJIB NAIK

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE obat (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nama TEXT," +
                "deskripsi TEXT," +
                "durasi TEXT," +
                "dosis TEXT," +
                "tanggal TEXT," +
                "status TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS obat");
        onCreate(db);
    }

    // ==========================
    // 🔥 INSERT (UPDATED)
    // ==========================
    public void insertObat(String nama, String tanggal, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO obat(nama, deskripsi, durasi, dosis, tanggal, status) VALUES(?,?,?,?,?,?)",
                new Object[]{
                        nama,
                        "Obat untuk meredakan gejala", // default
                        "3 Hari",
                        "1 Tablet",
                        tanggal,
                        status
                });
    }

    // ==========================
    // 🔥 READ (SEMUA)
    // ==========================
    public List<String[]> getAllObatFull() {
        List<String[]> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nama, tanggal, status FROM obat", null);

        while (cursor.moveToNext()) {
            list.add(new String[]{
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2)
            });
        }

        cursor.close();
        return list;
    }

    // ==========================
    // 🔥 READ BY NAMA
    // ==========================
    public String[] getObatByNama(String nama) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT nama, tanggal, status FROM obat WHERE nama=? LIMIT 1",
                new String[]{nama}
        );

        if (cursor.moveToFirst()) {

            String namaObat = cursor.getString(0);
            String tanggal = cursor.getString(1);
            String status = cursor.getString(2);

            cursor.close();
            return new String[]{namaObat, tanggal, status};
        }

        cursor.close();
        return null;
    }

    // ==========================
    // 🔥 READ AKTIF (PAKAI ID)
    // ==========================
    public List<String[]> getObatAktif() {

        List<String[]> list =
                new ArrayList<>();

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT id, nama, tanggal, status FROM obat WHERE status!='selesai' OR status IS NULL",
                        null
                );

        while (cursor.moveToNext()) {

            list.add(new String[]{

                    cursor.getString(0), // id
                    cursor.getString(1), // nama
                    cursor.getString(2), // tanggal
                    cursor.getString(3)  // status
            });
        }

        cursor.close();

        return list;
    }


    // ==========================
    // 🔥 UPDATE STATUS (BY ID)
    // ==========================
    public void updateStatus(int id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE obat SET status=? WHERE id=?",
                new Object[]{status, id});
    }

    // ==========================
    // 🔥 UPDATE FULL (EDIT)
    // ==========================
    public void updateObat(int id, String nama, String deskripsi, String durasi, String dosis) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE obat SET nama=?, deskripsi=?, durasi=?, dosis=? WHERE id=?",
                new Object[]{nama, deskripsi, durasi, dosis, id});
    }

    // ==========================
    // 🔥 DELETE (BY ID)
    // ==========================
    public void deleteObat(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM obat WHERE id=?",
                new Object[]{id});
    }

    // ==========================
    // 🔥 BACKUP (LAMA - MASIH DIPAKAI)
    // ==========================
    public void updateStatusByNama(String nama, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE obat SET status=? WHERE nama=?",
                new Object[]{status, nama});
    }

    public void deleteObatByNama(String nama) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM obat WHERE nama=?",
                new Object[]{nama});
    }

    // ==========================
    // 🔥 FILTER
    // ==========================
    public List<String[]> getObatHariIni() {
        List<String[]> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT nama, tanggal, status FROM obat",
                null
        );

        while (cursor.moveToNext()) {
            list.add(new String[]{
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2)
            });
        }

        cursor.close();
        return list;
    }

    public List<String[]> getRiwayat() {

        List<String[]> list =
                new ArrayList<>();

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT id, nama, tanggal, status FROM obat WHERE status='selesai'",
                        null
                );

        while (cursor.moveToNext()) {

            list.add(new String[]{

                    cursor.getString(0), // id
                    cursor.getString(1), // nama
                    cursor.getString(2), // tanggal
                    cursor.getString(3)  // status
            });
        }

        cursor.close();

        return list;
    }

    // ==========================
// 🔥 GET DATA BERDASARKAN ID
// ==========================
    public String[] getObatById(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id, nama, tanggal, status FROM obat WHERE id=? LIMIT 1",
                new String[]{String.valueOf(id)}
        );

        if (cursor.moveToFirst()) {

            String idObat = cursor.getString(0);
            String nama = cursor.getString(1);
            String tanggal = cursor.getString(2);
            String status = cursor.getString(3);

            cursor.close();

            return new String[]{idObat, nama, tanggal, status};
        }

        cursor.close();
        return null;
    }

    public void updateNamaDanTanggal(int id, String nama, String tanggal) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE obat SET nama=?, tanggal=? WHERE id=?",
                new Object[]{nama, tanggal, id});
    }

    public void deleteRiwayat(String nama) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(
                "DELETE FROM obat WHERE nama=? AND status='selesai'",
                new Object[]{nama}
        );
    }

    // ==========================
    // 🔥 VALIDASI
    // ==========================
    public boolean isObatExist(String nama) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM obat WHERE nama=? AND status='aktif'",
                new String[]{nama}
        );

        boolean ada = cursor.getCount() > 0;
        cursor.close();
        return ada;
    }
}