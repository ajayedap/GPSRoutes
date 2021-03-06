package com.example.gpsroutes;

import java.util.List;

import android.R.bool;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class RouteLocationManager {
	
	private static final int UPDATE_INTERVAL = 1000 * 60;
	private static final double LOCATION_DELTA = 0.0005;
	private LocationManager locationManager;
	private Location currentLocation;
	
	public RouteLocationManager(Object locationManagerHandle) {
		// TODO Auto-generated constructor stub
		this.locationManager = (LocationManager)locationManagerHandle;
		this.startReceiving();
	}
	
	public Location getCurrentLocation() {
		Location tmpLocation = currentLocation;
		for (String p : locationManager.getProviders(new Criteria(), false)) {
			updateCurLocation(locationManager.getLastKnownLocation(p));
		}
		if (tmpLocation == null) {
			return currentLocation;
		}
		if (Math.abs(tmpLocation.getLatitude() - currentLocation.getLatitude()) > LOCATION_DELTA) {
			return currentLocation;
		}
		return tmpLocation;
	}
	
	public void stopReceiving() {
		try {
	    	locationManager.removeUpdates(locationListener);
	    	locationManager.removeUpdates(locationCrsListener);
	    	locationManager.removeUpdates(locationPsvListener);
	    } catch(Exception e) {}
	}
	
	public void startReceiving(){
		List<String> providers = locationManager.getProviders(new Criteria(), false);
		for (String p : providers) {
			updateCurLocation(locationManager.getLastKnownLocation(p));
		}
		
		locationManager.requestLocationUpdates(
				LocationManager.PASSIVE_PROVIDER, 1000, 1f,locationPsvListener);
		
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		String crsProvider = locationManager.getBestProvider(criteria, true);
		
		if (crsProvider != null && locationManager.isProviderEnabled(crsProvider)) {
			locationManager.requestLocationUpdates(crsProvider, 1000,1f, locationCrsListener);
		}
		
		String gpsProvider = LocationManager.GPS_PROVIDER;
		if (gpsProvider != null && locationManager.isProviderEnabled(gpsProvider)) {
			currentLocation = locationManager.getLastKnownLocation(gpsProvider);
			updateCurLocation(currentLocation);
			locationManager.requestLocationUpdates(gpsProvider, 1000, 1f,locationListener);
		}
	}
	
	private void updateCurLocation(Location location) {
		if (isBetterLocation(location, currentLocation)) {
			currentLocation = location;
		}
	}
	
	private final LocationListener locationListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			updateCurLocation(location);
			//Intent intent = new Intent(MessageManager.PLAYER_LOCATION_CHANGED);
			//context.sendBroadcast(intent);
		}

		@Override
		public void onProviderDisabled(String provider) {
			try {
				locationManager.removeUpdates(locationListener);
			} catch (Exception e) {
			}
		}

		@Override
		public void onProviderEnabled(String provider) {
		
		}
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
	};
	private final LocationListener locationCrsListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			updateCurLocation(location);
		}

		@Override
		public void onProviderDisabled(String provider) {
			try {
				locationManager.removeUpdates(locationCrsListener);
			} catch (Exception e) {
			}
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};
	private final LocationListener locationPsvListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			updateCurLocation(location);
		}

		@Override
		public void onProviderDisabled(String provider) {
			try {
				locationManager.removeUpdates(locationPsvListener);
			} catch (Exception e) {
			}
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};
	
	/**
	 * Determines whether one Location reading is better than the current
	 * Location fix
	 * 
	 * @param location
	 *            The new Location that you want to evaluate
	 * @param currentBestLocation
	 *            The current Location fix, to which you want to compare the new
	 *            one
	 */
	protected boolean isBetterLocation(Location location,
			Location currentBestLocation) {
		if (location == null) {
			// a new null location is always bad
			return false;
		}

		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > UPDATE_INTERVAL/2;
		boolean isSignificantlyOlder = timeDelta < -UPDATE_INTERVAL/2;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use
		// the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be
			// worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
				.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate
				&& isFromSameProvider) {
			return true;
		} else if (isNewer && !isLessAccurate && !isMoreAccurate) {
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}
}
