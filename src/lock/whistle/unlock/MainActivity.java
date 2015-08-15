package lock.whistle.unlock;

import lock.whistle.unlock.MyService.SampleLocalBinder;
import my.wsu.R;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements OnCheckedChangeListener,
		OnClickListener {
	public static int whistleValue = 0;
	SharedPreferences prefs;
	Editor edit;
	boolean lock = true;

	// WakeLock mlock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		PowerManager manager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		// mlock = ma1ire();
		Button more = (Button) findViewById(R.id.button1);
		Button rate = (Button) findViewById(R.id.button2);
		Button themes = (Button) findViewById(R.id.btn_themes);
		Button test = (Button) findViewById(R.id.btn_test);
		Button rate_us = (Button) findViewById(R.id.btn_rate_us);
		rate.setOnClickListener(this);
		more.setOnClickListener(this);
		themes.setOnClickListener(this);
		test.setOnClickListener(this);
		rate_us.setOnClickListener(this);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		edit = prefs.edit();
		ToggleButton tb1 = (ToggleButton) findViewById(R.id.toggleButton1);
		tb1.setChecked(prefs.getBoolean("main_switch", false));
		tb1.setOnCheckedChangeListener(this);
		if (prefs.getBoolean("short", true)) {
			edit.putBoolean("short", false);
			edit.commit();

			// Handler h = new Handler();
			// h.postDelayed(new Runnable() {
			//
			// @Override
			// public void run() {
			// if (bound) {
			// sampleService.setAsForeground();
			// }
			//
			// }
			// }, 1000);

		}
		Intent serviceIntent = new Intent(getApplicationContext(),
				MyService.class);
		startService(serviceIntent);

		bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);

	}

	private MyService sampleService;
	private boolean bound;
	private ServiceConnection serviceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			bound = false;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			SampleLocalBinder binder = (SampleLocalBinder) service;
			sampleService = binder.getService();
			bound = true;

		}
	};

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		switch (arg0.getId()) {
		case R.id.toggleButton1:
			if (arg1) {

				Intent serviceIntent = new Intent(this, MyService.class);
				startService(serviceIntent);
				bindService(serviceIntent, serviceConnection,
						Context.BIND_AUTO_CREATE);
				Handler h = new Handler();
				h.postDelayed(new Runnable() {

					@Override
					public void run() {
						if (bound) {
							sampleService.setAsForeground();
						}

					}
				}, 1000);
				// if (bound) {
				// sampleService.setAsForeground();
				// }

				// if (bound) {
				// sampleService.setAsForeground();
				// }
			}

			else {
				sampleService.setAsBackground();
				Intent serviceIntent = new Intent(this, MyService.class);
				stopService(serviceIntent);
				bound = false;
				unbindService(serviceConnection);

			}
			lock = arg1;
			edit.putBoolean("main_switch", arg1);
			edit.commit();

			break;
		}
	}

	@Override
	public void onBackPressed() {
		if (!lock) {
			Intent serviceIntent = new Intent(this, MyService.class);
			stopService(serviceIntent);
		}
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		if (bound) {
			bound = false;
			unbindService(serviceConnection);
		}

		super.onDestroy();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.button2:
			Intent rate = new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://play.google.com/store/apps/details?id="
							+ getPackageName()));
			startActivity(rate);
			break;
		case R.id.btn_rate_us:
			Intent rat = new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://play.google.com/store/apps/details?id="
							+ getPackageName()));
			startActivity(rat);
			break;
		case R.id.button1:
			Intent it = new Intent(MainActivity.this, MoreActivity.class);
			startActivity(it);
			break;
		case R.id.btn_themes:
			Intent it_themes = new Intent(MainActivity.this,
					ThemeActivity.class);
			startActivity(it_themes);
			break;
		case R.id.btn_test:
			Intent it_test = new Intent(MainActivity.this, TestActivity.class);
			startActivity(it_test);
			break;

		}
	}

	@Override
	protected void onPause() {
		// if (mlock != null)
		// mlock.release();

		super.onPause();
	}

	@Override
	protected void onResume() {
		// if (mlock != null)
		// mlock.acquire();

		super.onResume();
	}
}
