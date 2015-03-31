package com.rhythmictracks.rhythmictracks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Logan on 31/03/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "runStatsManager";

    private static final String TABLE_RUNSTATS  = "runstats";
    private static final String KEY_ID  = "id";
    private static final String KEY_MAXSPEED  = "maxspeed";
    private static final String KEY_AVGSPEED  = "avgspeed";
    private static final String KEY_DISTANCE  = "distance";
    private static final String KEY_TIME  = "time";

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_RUNSTATS_TABLE = "CREATE TABLE " + TABLE_RUNSTATS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," + KEY_MAXSPEED + " DOUBLE," +
                KEY_AVGSPEED + " DOUBLE," + KEY_DISTANCE + " DOUBLE," +
                KEY_TIME + ");";
        db.execSQL(CREATE_RUNSTATS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RUNSTATS);
        onCreate(db);
    }


    public void addRunStats(RunStats rs){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_MAXSPEED, rs.getRunMaxSpeed());
        values.put(KEY_AVGSPEED, rs.getRunAvgSpeed());
        values.put(KEY_DISTANCE, rs.getRunDistance());
        values.put(KEY_TIME, rs.getRunTime());

        db.insert(TABLE_RUNSTATS, null, values);
        db.close();
    }

    public RunStats getRunStats(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_RUNSTATS, new String[] {KEY_ID, KEY_MAXSPEED, KEY_AVGSPEED, KEY_DISTANCE, KEY_TIME
        }, KEY_ID + "=?", new String[] {String.valueOf(id)}, null, null ,null ,null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        RunStats rs = new RunStats(Integer.parseInt(cursor.getString(0)),
                                   Double.parseDouble(cursor.getString(1)),
                                   Double.parseDouble(cursor.getString(2)),
                                   Double.parseDouble(cursor.getString(3)),
                                   Double.parseDouble(cursor.getString(4)));
        return rs;
    }

    public List<RunStats> getAllRunStats(){
        List<RunStats> rsList = new ArrayList<RunStats>();
        String selectQuery = "SELECT * FROM " + TABLE_RUNSTATS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                RunStats rs = new RunStats();
                rs.set_id(          Integer.parseInt(cursor.getString(0)));
                rs.setRunMaxSpeed(  Double.parseDouble(cursor.getString(1)));
                rs.setRunAvgSpeed(  Double.parseDouble(cursor.getString(2)));
                rs.setRunDistance(  Double.parseDouble(cursor.getString(3)));
                rs.setRunTime(      Double.parseDouble(cursor.getString(4)));
                rsList.add(rs);
            } while (cursor.moveToNext());
        }
        return rsList;
    }

    public int getRunStatsCount(){
        String countQuery = "SELECT * FROM " + TABLE_RUNSTATS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }

    public int updateRunStats(RunStats rs){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(KEY_MAXSPEED, rs.getRunMaxSpeed());
        value.put(KEY_AVGSPEED, rs.getRunAvgSpeed());
        value.put(KEY_DISTANCE, rs.getRunDistance());
        value.put(KEY_TIME, rs.getRunTime());
        return db.update(TABLE_RUNSTATS, value, KEY_ID + " = ?", new String[] { String.valueOf(rs.get_id())});

    }

    public void deleteRunStats(RunStats rs){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RUNSTATS, KEY_ID + " = ?", new String[] { String.valueOf(rs.get_id())});
        db.close();
    }

}
