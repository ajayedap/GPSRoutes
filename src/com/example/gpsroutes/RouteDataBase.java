package com.example.gpsroutes;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
	  
	  public ArrayList<RouteNode> getAllLocations() {
		  ArrayList<RouteNode> route = new ArrayList<RouteNode>();
		  String selectQuery = "SELECT  * FROM " + dbHelper.TABLE_NAME;
		  
		  Cursor cursor = database.rawQuery(selectQuery, null);
		  if (cursor.moveToFirst()) {
		        do {
		            RouteNode node = new RouteNode(
		            		Integer.parseInt(cursor.getString(0)),
		            		cursor.getString(1),
		            		Double.valueOf(cursor.getString(2)),
		            		Double.valueOf(cursor.getString(3)));
		            route.add(node);
		        } while (cursor.moveToNext());
		    }
		  
		  return route;
	  }

	  public void clearDb() {
		  database.delete(RouteDataBaseHelper.TABLE_NAME, null, null);
	  }
	} 