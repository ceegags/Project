package abrewster.cganong.unb.ca.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;


public class DBHelper extends SQLiteOpenHelper {
    public static final int VERSION_NUMBER = 2;
    public static final String DATABASE_NAME = "MyPlaces";
    public static final String SETTINGS_TABLE_NAME = "settings";
    public static final String SETTINGS_COLUMN_LOCATION = "location";
    public static final String SETTINGS_COLUMN_ADDRESS = "address";
    public static final String SETTINGS_COLUMN_BLUETOOTH = "bluetooth";
    public static final String SETTINGS_COLUMN_WIFI = "wifi";
    public static final String SETTINGS_COLUMN_RINGER = "ringer";
    public static final String SETTINGS_COLUMN_RINGER_VOLUME = "ringer_volume";
    public static final String SETTINGS_COLUMN_VIBRATE = "vibrate";
    public static final String SETTINGS_COLUMN_ROTATION = "rotation";
    public static final String SETTINGS_COLUMN_BRIGHTNESS = "brightness";
    private HashMap hp;

    public DBHelper(Context c) {
        super(c,DATABASE_NAME,null,VERSION_NUMBER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + SETTINGS_TABLE_NAME + " " +
                        "(" + SETTINGS_COLUMN_LOCATION + " text primary key, " +
                        SETTINGS_COLUMN_ADDRESS + " text not null,"+
                        SETTINGS_COLUMN_BLUETOOTH + " integer not null," +
                        SETTINGS_COLUMN_WIFI + " integer not null,"+
                        SETTINGS_COLUMN_RINGER + " integer not null," +
                        SETTINGS_COLUMN_RINGER_VOLUME + " integer not null,"+
                        SETTINGS_COLUMN_VIBRATE + " integer not null," +
                        SETTINGS_COLUMN_ROTATION + " integer not null,"+
                        SETTINGS_COLUMN_BRIGHTNESS + " integer not null" +
                        ")"
        );

    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+SETTINGS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertSettings (     String location,
                                        String address,
                                        boolean bluetooth,
                                        boolean wifi,
                                        boolean ringer,
                                        int ringer_volume,
                                        boolean vibrate,
                                        boolean rotation,
                                        int brightness
    ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SETTINGS_COLUMN_LOCATION, location);
        cv.put(SETTINGS_COLUMN_ADDRESS, address);
        cv.put(SETTINGS_COLUMN_BLUETOOTH, bluetooth? 1 : 0);
        cv.put(SETTINGS_COLUMN_WIFI, wifi? 1 : 0);
        cv.put(SETTINGS_COLUMN_RINGER, ringer? 1 : 0);
        cv.put(SETTINGS_COLUMN_RINGER_VOLUME, ringer_volume);
        cv.put(SETTINGS_COLUMN_VIBRATE, vibrate? 1 : 0);
        cv.put(SETTINGS_COLUMN_ROTATION, rotation? 1 : 0);
        cv.put(SETTINGS_COLUMN_BRIGHTNESS, brightness);

        if (db.insert(SETTINGS_TABLE_NAME,null,cv) == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

    public Cursor getSettings(String location) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+SETTINGS_TABLE_NAME+" where "+SETTINGS_COLUMN_LOCATION+" = "+location+"",null);
        db.close();
        return res;
    }

    public int numberOfRowsSettings() {
        SQLiteDatabase db = this.getReadableDatabase();
        int res = (int) DatabaseUtils.queryNumEntries(db,SETTINGS_TABLE_NAME);
        db.close();
        return res;
    }

    public boolean updateSettings (     String location,
                                        String address,
                                        boolean bluetooth,
                                        boolean wifi,
                                        boolean ringer,
                                        int ringer_volume,
                                        boolean vibrate,
                                        boolean rotation,
                                        int brightness
    ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SETTINGS_COLUMN_LOCATION, location);
        cv.put(SETTINGS_COLUMN_ADDRESS, address);
        cv.put(SETTINGS_COLUMN_BLUETOOTH, bluetooth? 1 : 0);
        cv.put(SETTINGS_COLUMN_WIFI, wifi? 1 : 0);
        cv.put(SETTINGS_COLUMN_RINGER, ringer? 1 : 0);
        cv.put(SETTINGS_COLUMN_RINGER_VOLUME, ringer_volume);
        cv.put(SETTINGS_COLUMN_VIBRATE, vibrate? 1 : 0);
        cv.put(SETTINGS_COLUMN_ROTATION, rotation? 1 : 0);
        cv.put(SETTINGS_COLUMN_BRIGHTNESS, brightness);
        if (db.update(SETTINGS_TABLE_NAME,cv,""+SETTINGS_COLUMN_LOCATION+" = ? ",new String[] {location}) == 0) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

    public boolean deleteSettings (String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean res = !(db.delete(SETTINGS_TABLE_NAME,""+SETTINGS_COLUMN_LOCATION+" = ? ",new String[] {location})==0);
        db.close();
        return res;
    }

    public ArrayList<Location> getAllSettings() {
        ArrayList<Location> arrayList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+SETTINGS_TABLE_NAME,null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            arrayList.add(new Location(res.getString(res.getColumnIndex(SETTINGS_COLUMN_LOCATION))));
            res.moveToNext();
        }
        res.close();
        db.close();
        return arrayList;

    }
}
