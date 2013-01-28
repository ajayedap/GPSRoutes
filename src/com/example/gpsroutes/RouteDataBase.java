package com.example.gpsroutes;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class RouteDataBase {

	  private SQLiteDatabase database;
	  private RouteDataBaseHelper dbHelper;

	  public RouteDataBase(Context context) {
		  dbHelper = new RouteDataBaseHelper(context);
	  }

	  public void open() throws SQLException {
		  database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
		  dbHelper.close();
	  }

	  public void addLocation(String latitude, String longitude, String date) {
		  ContentValues values = new ContentValues();
		  values.put(RouteDataBaseHelper.COLUMN_LAT, latitude);
		  values.put(RouteDataBaseHelper.COLUMN_LON, longitude);
		  values.put(RouteDataBaseHelper.COLUMN_DATE, date);
		  database.insert(RouteDataBaseHelper.TABLE_NAME, null,values);
	  }

	  public void clearDb() {
		  database.delete(RouteDataBaseHelper.TABLE_NAME, null, null);
	  }
	} 