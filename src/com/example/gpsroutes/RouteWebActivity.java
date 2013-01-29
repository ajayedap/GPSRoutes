package com.example.gpsroutes;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class RouteWebActivity extends Activity {
	
	private WebView webView;
	private ArrayList<RouteNode> route;
	private double lat;
	private double lon;
	
	private class JavaScriptInterface {
		public double getLatitude(){
			return lat;
		}
		public double getLongitude(){
		    return lon;
		}
		/*public double[][] getRoute(){
			double[][] route;
			
			
			
			return route;
		}*/
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.route_web_activity);
	    
	    RouteDataBase db = new RouteDataBase(this);
	    db.open();
	    route = db.getAllLocations();
	    db.close();
	    
	    Intent intent = getIntent();
	    lat = Double.parseDouble(intent.getStringExtra("lat"));
	    lon = Double.parseDouble(intent.getStringExtra("lon"));
	    
	    webView = (WebView) findViewById(R.id.webView1);
	    webView.getSettings().setJavaScriptEnabled(true);	    
	    webView.setWebViewClient(new WebViewClient());
	    webView.loadUrl("file:///android_asset/google_maps.html");
	    webView.addJavascriptInterface(new JavaScriptInterface(), "android");
	    
	    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	//private 
}
