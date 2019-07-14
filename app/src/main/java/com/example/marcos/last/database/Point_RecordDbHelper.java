package com.example.marcos.last.database;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.marcos.last.Record;

import java.util.ArrayList;


/**
 * Created by windows7 on 7/12/2019.
 */
public class Point_RecordDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PointRecords.db";

    Context mcontext;

     public Point_RecordDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Point_RecordContract.Point_RecordEntry.TABLE_NAME + " ("
                + Point_RecordContract.Point_RecordEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Point_RecordContract.Point_RecordEntry.ID + " TEXT NOT NULL,"
                + Point_RecordContract.Point_RecordEntry.TRIP_ID + " TEXT NOT NULL,"
                + Point_RecordContract.Point_RecordEntry.LATITUD + " TEXT NOT NULL,"
                + Point_RecordContract.Point_RecordEntry.LONGITUD + " TEXT NOT NULL,"
                + Point_RecordContract.Point_RecordEntry.RECORDED_AT + " TEXT NOT NULL,"
                + "UNIQUE (" + Point_RecordContract.Point_RecordEntry.ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Point_RecordContract.Point_RecordEntry.TABLE_NAME);
        onCreate(db);

    }
    //Add Point to db
    public void addPoint_Record(Point_Record record){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(Point_RecordContract.Point_RecordEntry.TABLE_NAME, null, record.toContentValues());
        db.close();
    }
    //Obtener la lista de comentarios en la base de datos
    public ArrayList<Point_Record> getAllPoint_Record(){
        //Creamos el cursor
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Point_Record> lista= new ArrayList<>();
        Cursor c = db.rawQuery("select "+ Point_RecordContract.Point_RecordEntry._ID + ","+Point_RecordContract.Point_RecordEntry.ID+","+Point_RecordContract.Point_RecordEntry.TRIP_ID +","
                +Point_RecordContract.Point_RecordEntry.LATITUD+","+Point_RecordContract.Point_RecordEntry.LONGITUD+","+Point_RecordContract.Point_RecordEntry.RECORDED_AT+" from "+Point_RecordContract.Point_RecordEntry.TABLE_NAME, null);
        if (c != null && c.getCount()>0) {
            c.moveToFirst();
            do {
                //Asignamos el valor en nuestras variables para crear un nuevo objeto Comentario
                String trip_id = c.getString(c.getColumnIndex(Point_RecordContract.Point_RecordEntry.TRIP_ID));
                String longitude = c.getString(c.getColumnIndex(Point_RecordContract.Point_RecordEntry.LONGITUD));
                String latitude = c.getString(c.getColumnIndex(Point_RecordContract.Point_RecordEntry.LATITUD));
                String recorded_at = c.getString(c.getColumnIndex(Point_RecordContract.Point_RecordEntry.RECORDED_AT));
                String id = c.getString(c.getColumnIndex(Point_RecordContract.Point_RecordEntry._ID));
                Point_Record pr =new Point_Record(trip_id,latitude,longitude,recorded_at);
                //Añadimos el comentario a la lista
                lista.add(pr);
            } while (c.moveToNext());
        }

        //Cerramos el cursor
        c.close();
        return lista;
    }
    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Point_RecordContract.Point_RecordEntry.TABLE_NAME,null,null);
        db.close();
    }
    public long countAllRecord(){
        SQLiteDatabase db = this.getWritableDatabase();
        long numRows = DatabaseUtils.queryNumEntries(db,Point_RecordContract.Point_RecordEntry.TABLE_NAME);
        return numRows;

    }
}

