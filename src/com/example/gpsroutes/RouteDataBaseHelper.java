package com.example.gpsroutes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RouteDataBaseHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "GPSRouteLocationDB";
    public static final String TABLE_NAME = "LocationTable";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LAT = "latitude";
    public static final String COLUMN_LON = "longitude";
    public static final String COLUMN_DATE = "date";
    
    private static final String DATABASE_CREATE = "create table "
    	      + TABLE_NAME + "(" + COLUMN_ID + " integer primary key autoincrement, " 
    	      + COLUMN_LAT + " varchar not null,"
    	      + COLUMN_LON + " varchar not null,"
    	      + COLUMN_DATE + " datetime default current_timestamp);";
	
    public RouteDataBaseHelper(Context context) {
		// TODO Auto-generated constructor stub
    	super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.w(RouteDataBaseHelper.class.getName(),
		        "Upgrading database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");
		    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		    onCreate(db);
	}
	

    
}