package com.example.gpsroutes;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class RouteWebActivity extends Activity {
	
	private WebView webView;
	private double lat;
	private double lon;
	
	private class JavaScriptInterface {
		public double getLatitude(){
			return lat;
		}
		public double getLongitude(){
		    return lon;
		}
	}
	
	@Override
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.route_web_activity);
	    
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
}
