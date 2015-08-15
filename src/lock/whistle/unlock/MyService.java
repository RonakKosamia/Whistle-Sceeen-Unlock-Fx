package lock.whistle.unlock;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class MyService extends Service {
	BroadcastReceiver mReceiver;
	IntentFilter filter;
	SharedPreferences prefs;
	boolean flag;
	private final IBinder binder = new SampleLocalBinder();

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return binder;
	}

	public class SampleLocalBinder extends Binder {
		MyService getService() {
			return MyService.this;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_USER_PRESENT);
		mReceiver = new ScreenReceiver();
		manager = (KeyguardManager) getApplicationContext().getSystemService(
				Context.KEYGUARD_SERVICE);
		lock = manager.newKeyguardLock("lock");
	}

	KeyguardManager manager;
	KeyguardLock lock;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		lock.disableKeyguard();
		flag = prefs.getBoolean("main_switch", false);
		if (flag) {
			if (!mReceiver.isOrderedBroadcast()) {

				registerReceiver(mReceiver, filter);
				Log.d("main", "Registering...");
			}
		} else {
			if (mReceiver.isOrderedBroadcast()) {
				unregisterReceiver(mReceiver);
				Log.d("main", "Unregistered");
			}

		}
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("main", "OnDestroy");
		unregisterReceiver(mReceiver);
	}

	public void setAsForeground() {
		startForeground(Notif.notifId,
				Notif.getNotification(getApplicationContext()));
	}

	public void setAsBackground() {
		stopForeground(true);
	}
}
