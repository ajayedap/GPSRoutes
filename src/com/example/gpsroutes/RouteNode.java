package com.example.gpsroutes;

public class RouteNode {
	private int id;
	private String date;
	private double lat;
	private double lon;
	public RouteNode(int id,
			String date, double lat, double lon) {
		this.id = id;
		this.date = date;
		this.lat = lat;
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public double getLon() {
		return lon;
	}
}
