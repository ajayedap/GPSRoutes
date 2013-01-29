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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class GPSRoutes extends Activity {

	private Location lastKnownLocation;
	private RouteDataBase rdb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		final LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
		lastKnownLocation = getLastKnownLocation(locationManager);
		
		rdb = new RouteDataBase(this);
		rdb.open();
		
		/*final Button mainButton = (Button)findViewById(R.id.button1);
		mainButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				TextView tv = (TextView)findViewById(R.id.textView1);
				tv.setText("GPS: "+locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)+
						" "+"NETWORK: "+checkInternetConnection(v.getContext()));				
				//if (!checkInternetConnection(v.getContext())){
				//	startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
				//}
				
				
				
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
		});*/
		Button mapsButton = (Button)findViewById(R.id.buttonMaps);
		mapsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*lastKnownLocation = getLastKnownLocation(locationManager);*/
				String lat = ""+lastKnownLocation.getLatitude();
				String lon = ""+lastKnownLocation.getLongitude();
				Intent webIntent = new Intent(v.getContext(),RouteWebActivity.class);
				webIntent.putExtra("lat", lat);
				webIntent.putExtra("lon", lon);
				startActivity(webIntent);
				startService(new Intent(v.getContext(),RouteTracking.class));
				
			}
		});
		ToggleButton tButton = (ToggleButton)findViewById(R.id.toggleButtonServiceStatus);
		tButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					startService(new Intent(buttonView.getContext(),RouteTracking.class));
				}
				else {
					
				}
			}
		});
		tButton.setChecked(true);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
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
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			rdb.close();
		} catch (Exception e) {}
	}
	
	

}
