package lock.whistle.unlock;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import my.wsu.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class LockActivity extends Activity {
	TextView time;
	// detection parameters
	public static Activity activity;
	SharedPreferences prefs;
	int CASE;
	HomeKeyLocker locker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);

		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		Intent it = getIntent();
		Typeface face = null;
		int color = 0;
		if (it.getExtras() != null) {
			startService(new Intent(getApplicationContext(), WService.class));

			CASE = it.getIntExtra("theme_no", 0);
			switch (CASE) {
			case 0:

				setContentView(R.layout.theme1);
				face = Typeface.createFromAsset(getAssets(), "fonts/bahu.TTF");
				color = Color.rgb(255, 255, 255);
				break;
			case 1:
				setContentView(R.layout.theme2);
				face = Typeface.createFromAsset(getAssets(), "fonts/love.TTF");
				color = Color.rgb(247, 201, 212);
				break;
			case 2:
				setContentView(R.layout.theme3);
				face = Typeface.createFromAsset(getAssets(), "fonts/crack.ttf");
				color = Color.rgb(0, 0, 0);
				break;
			}

		} else {
			CASE = prefs.getInt("theme_no", 0);
			switch (CASE) {
			case 0:
				setContentView(R.layout.theme1);
				face = Typeface.createFromAsset(getAssets(), "fonts/bahu.TTF");
				color = Color.rgb(255, 255, 255);
				break;
			case 1:
				setContentView(R.layout.theme2);
				face = Typeface.createFromAsset(getAssets(), "fonts/love.TTF");
				color = Color.rgb(247, 201, 212);
				break;
			case 2:
				setContentView(R.layout.theme3);
				face = Typeface.createFromAsset(getAssets(), "fonts/crack.ttf");
				color = Color.rgb(0, 0, 0);
				break;
			}
		}

		activity = LockActivity.this;
		time = (TextView) findViewById(R.id.textView_time);
		TextView date = (TextView) findViewById(R.id.textView_date);
		TextView batery = (TextView) findViewById(R.id.textView_batery);
		TextView msg = (TextView) findViewById(R.id.textView_msg);
		TextView missed = (TextView) findViewById(R.id.textView_missed);

		ImageView iv_bat = (ImageView) findViewById(R.id.bat_icon);
		time.setTypeface(face);
		date.setTypeface(face);
		batery.setTypeface(face);
		msg.setTypeface(face);
		missed.setTypeface(face);
		time.setTextSize(50);
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
		String s = sdf.format(Calendar.getInstance().getTime());
		time.setText(s);
		sdf = new SimpleDateFormat("EEE, d MMM yyyy");
		s = sdf.format(Calendar.getInstance().getTime());
		date.setText(s);
		String[] projection = { CallLog.Calls.CACHED_NAME,
				CallLog.Calls.CACHED_NUMBER_LABEL, CallLog.Calls.TYPE };
		String where = CallLog.Calls.TYPE + "=" + CallLog.Calls.MISSED_TYPE
				+ " And " + CallLog.Calls.NEW + "=1";
		Cursor c = this.getContentResolver().query(CallLog.Calls.CONTENT_URI,
				projection, where, null, null);
		c.moveToFirst();
		Log.d("main", "" + c.getCount()); // do some other operation
		Uri sms_content = Uri.parse("content://sms/inbox");

		Cursor c1 = this.getContentResolver().query(sms_content, null,
				"read=0", null, null);
		c1.moveToFirst();
		Log.d("main", "count: " + c1.getCount());
		msg.setText(c1.getCount() + "");
		missed.setText(c.getCount() + "");
		getBatteryPercentage(batery, iv_bat);
		setTimeBroadcast();
		time.setTextColor(color);
		date.setTextColor(color);
		// batery.setTextColor(color);
		msg.setTextColor(color);
		missed.setTextColor(color);
		// EmbossMaskFilter filter=new EmbossMaskFilter(new float[] {0.0f, 2.0f,
		// 0.5f}, 0.8f, 20f, 1f);
		// time.getPaint().setMaskFilter(filter);
		// date.getPaint().setMaskFilter(filter);
		locker = new HomeKeyLocker();
		locker.lock(LockActivity.this);
	}

	private void setTimeBroadcast() {
		filter = new IntentFilter();
		filter.addAction(Intent.ACTION_TIME_TICK);
		mrec = new BroadcastReceiver() {

			@Override
			public void onReceive(Context arg0, Intent arg1) {
				if (arg1.getAction().equals(Intent.ACTION_TIME_TICK)) {
					SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
					String s = sdf.format(Calendar.getInstance().getTime());
					time.setText(s);
				}

			}
		};
		registerReceiver(mrec, filter);
	}

	IntentFilter filter;
	BroadcastReceiver mrec;

	protected void onDestroy() {
		unregisterReceiver(mrec);
		stopService(new Intent(getApplicationContext(), WService.class));

		super.onDestroy();
	}

	@Override
	protected void onPause() {
		locker.unlock();
		super.onPause();
	}

	protected void onResume() {
		if (locker != null)
			locker.lock(LockActivity.this);
		super.onResume();

	};

	BroadcastReceiver batteryLevelReceiver;

	private void getBatteryPercentage(final TextView tv, final ImageView iv) {
		batteryLevelReceiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				context.unregisterReceiver(this);
				int currentLevel = intent.getIntExtra(
						BatteryManager.EXTRA_LEVEL, -1);
				int status = intent
						.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
				boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING
						|| status == BatteryManager.BATTERY_STATUS_FULL;
				Log.d("main", "ischa: " + isCharging);
				int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
				int level = -1;
				if (currentLevel >= 0 && scale > 0) {
					level = (currentLevel * 100) / scale;
				}
				tv.setText(level + "%");
				if (isCharging) {
					tv.setText(level + "%");
					iv.setBackgroundResource(R.drawable.bat_charging);
				}

				else if (level < 21)
					iv.setBackgroundResource(R.drawable.batery_20);
				else if (level < 50)
					iv.setBackgroundResource(R.drawable.batery_40);
				else if (level < 61)
					iv.setBackgroundResource(R.drawable.batery_60);
				else if (level < 81)
					iv.setBackgroundResource(R.drawable.batery_80);
				else
					iv.setBackgroundResource(R.drawable.batery_100);
			}
		};
		IntentFilter batteryLevelFilter = new IntentFilter();
		batteryLevelFilter.addAction(Intent.ACTION_POWER_CONNECTED);
		batteryLevelFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(batteryLevelReceiver, batteryLevelFilter);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

	}
}
