package com.example.parentalapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static String TAG = "DatabaseOpenHelper";
    private static final String DATABASE_PATH = "/data/data/com.example.parentalapp/databases/";
    private static final String DATABASE_NAME = "parentalAppDB.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db;
    private final Context context;

    @Override
    public void onCreate(SQLiteDatabase db) {
        //empty
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //empty
    }

    public DatabaseOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        try{
            createDB();
        }catch (IOException e){
            e.printStackTrace();
        }
        db = getWritableDatabase();
    }

    public void createDB() throws IOException{
        boolean dbExist = checkDB();
        if(!dbExist){
            this.getReadableDatabase();
            this.close();
            try{
                copyDB();
            }catch (IOException e){
                e.printStackTrace();
                throw new Error("ErrorCopyingDatabase");
            }finally {
                this.close();
            }
        }
    }

    private boolean checkDB(){
        try{
            final String path = DATABASE_PATH + DATABASE_NAME;
            final File file = new File(path);
            if(file.exists()){
                return true;
            }else{
                return false;
            }
        }catch (SQLiteException e){
            e.printStackTrace();
            return false;
        }
    }

    private void copyDB() throws IOException {
        try{
            InputStream inputStream = context.getAssets().open(DATABASE_NAME);
            String outFileName = DATABASE_PATH + DATABASE_NAME;
            OutputStream outputStream = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0){
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean openDB() throws SQLException {
        String path = DATABASE_PATH + DATABASE_NAME;
        db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        return db != null;
    }

    @Override
    public synchronized void close() {
        if(db != null){
            db.close();
        }
        super.close();
    }

    public Cursor query(String sql){
        return db.rawQuery(sql, null);
    }

    public void execSQL(String table, ContentValues cv){
        db.insert(table, null, cv);
    }

}
