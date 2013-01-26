package com.example.gpsroutes;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Location lastKnownLocation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		final LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
		final Button mainButton = (Button)findViewById(R.id.button1);
		Button mapsButton = (Button)findViewById(R.id.buttonMaps);
		mainButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				TextView tv = (TextView)findViewById(R.id.textView1);
				tv.setText("GPS: "+locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)+
						" "+"NETWORK: "+checkInternetConnection(v.getContext()));				
				if (!checkInternetConnection(v.getContext())){
					startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
				}
				
				lastKnownLocation = getLastKnownLocation(locationManager);
				
				TextView textViewLA = (TextView)findViewById(R.id.textViewLA);
				TextView textViewLO = (TextView)findViewById(R.id.textViewLO);
				
				textViewLA.setText("LA: "+lastKnownLocation.getLatitude());
				textViewLO.setText("LO: "+lastKnownLocation.getLongitude());
				
				textViewLA.setVisibility(TextView.VISIBLE);
				textViewLO.setVisibility(TextView.VISIBLE);			
				
			}
			boolean checkInternetConnection(Context context) {

			    ConnectivityManager conMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

			    // ARE WE CONNECTED TO THE NET?
			    if (conMgr.getActiveNetworkInfo() != null
			            && conMgr.getActiveNetworkInfo().isAvailable()
			            && conMgr.getActiveNetworkInfo().isConnected()) {
			        return true;
			    } else {
			        return false;
			    }
			}
		});
		mapsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lastKnownLocation = getLastKnownLocation(locationManager);
				String lat = ""+lastKnownLocation.getLatitude();
				String lon = ""+lastKnownLocation.getLongitude();
				Intent webIntent = new Intent(v.getContext(),RouteWebActivity.class);
				webIntent.putExtra("lat", lat);
				webIntent.putExtra("lon", lon);
				startActivity(webIntent);				
			}
		});
		
		
		
	}
	
	public Location getLastKnownLocation(LocationManager locationManager){
		LocationListener locationListener = new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				
			}
		};
		
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
		Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		locationManager.removeUpdates(locationListener);
		return lastKnownLocation;
	}
	
	

}
