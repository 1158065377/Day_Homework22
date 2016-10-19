package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/10/16.
 */

public class DBManger  {

    private  DBhelper bhelper;
    private String tablename;
    public DBManger(Context context,String tablename) {
        bhelper = new DBhelper(context);
        this.tablename=tablename;
    }
    public void insert(String id,String title,String img_url){
        SQLiteDatabase database = bhelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("id",id);
        values.put("title",title);
        values.put("img_url",img_url);
        database.insert(tablename,null,values);
        database.close();
    }
    public Cursor query(){
        SQLiteDatabase database = bhelper.getReadableDatabase();
        Cursor cursor = database.query(tablename, null, null, null, null, null, null);
        return cursor;
    }
    public void delete(){
        SQLiteDatabase database = bhelper.getReadableDatabase();
        database.delete(tablename,null,null);
        database.close();
    }

    class DBhelper extends SQLiteOpenHelper{
        public DBhelper(Context context) {
            super(context, "myhomework", null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql="create table h_01(_id integer primary key autoincrement," +
                    "id text," +
                    "title text," +
                    "img_url text)";
            db.execSQL(sql);
            String sql1="create table h_02(_id integer primary key autoincrement," +
                    "id text," +
                    "title text," +
                    "img_url text)";
            db.execSQL(sql1);
            String sql2="create table h_03(_id integer primary key autoincrement," +
                    "id text," +
                    "title text," +
                    "img_url text)";
            db.execSQL(sql2);
            String sql3="create table h_04(_id integer primary key autoincrement," +
                    "id text," +
                    "title text," +
                    "img_url text)";
            db.execSQL(sql3);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
