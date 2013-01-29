package com.example.gpsroutes;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;

public class RouteTracking extends Service {
	
	private static final int UPDATE_INTERVAL = 1000 * 60;
	private static final int NOTIFICATION_ID = 1;

	private Timer timer = new Timer();  
	private NotificationManager notificationManager;
	private RouteLocationManager rlManager;

	public RouteTracking() {
		// TODO Auto-generated constructor stub
	}
    
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override   
	public void onCreate() {
		String context = Context.LOCATION_SERVICE;
		rlManager = new RouteLocationManager(getSystemService(context));
	}

	@Override   
	public void onDestroy() {
	    if (timer != null){
	        timer.cancel();
	    }
	    rlManager.stopReceiving();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startid) {  

	    notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
	    final int icon = android.R.drawable.stat_notify_sync;
	    final CharSequence tickerText = "GPSRoute Event!";
	    final CharSequence contentTitle = "GPSRoute Location.";
	    
	    final Intent notificationIntent = new Intent(this, GPSRoutes.class);
	    final PendingIntent contentIntent = PendingIntent.getActivity(this,0, notificationIntent, 0);

	    timer.scheduleAtFixedRate(new TimerTask() {

	        @Override
	        public void run() {
	        	
	        	try {
		        	rlManager.startReceiving();	        		
	        	}catch (Exception e) {}
	        	Location currentLocation = rlManager.getCurrentLocation();
	        	
	    	    Context context = getApplicationContext();
	    	    Notification notification = new Notification(icon, tickerText, System.currentTimeMillis());
	    	    notification.setLatestEventInfo(context, contentTitle, currentLocation.getLatitude()+" "+currentLocation.getLongitude(), contentIntent);

	    	    notificationManager.notify(NOTIFICATION_ID, notification);
	    	    rlManager.stopReceiving();
	    	    
	        }       

	    }, 0, UPDATE_INTERVAL);

	    return START_STICKY;
	}

	private void stopService() {
	    if(timer != null)
	        timer.cancel();
	    rlManager.stopReceiving();
	}
	
	
}
