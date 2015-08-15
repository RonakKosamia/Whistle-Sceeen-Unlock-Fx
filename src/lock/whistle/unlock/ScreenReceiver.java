package lock.whistle.unlock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

public class ScreenReceiver extends BroadcastReceiver {

	SharedPreferences prefs;
	Editor edit;
	boolean flag, received, lock_flag = true, isRunnig;

	@Override
	public void onReceive(Context context, Intent intent) {

		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		edit = prefs.edit();

		if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
			Log.d("main", "screen on");
			DetectorThread.totalWhistlesDetected = 0;
			WService.flag = false;

			// Intent it_service = new Intent(context, UnlockService.class);
			// it_service.putExtra("main", "abc");
			context.startService(new Intent(context, WService.class));
			// context.startService(it_service);

		} else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
			// context.startService(new Intent(context, UnlockService.class));
			// Intent it_service = new Intent(context, UnlockService.class);
			// context.startService(it_service);
			Log.d("main", "user present");
		} else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
			// edit.putBoolean("bool", true);
			// edit.commit();
			Log.d("main", "screen off.");
			context.stopService(new Intent(context, WService.class));
			Intent new_it = new Intent(context, LockActivity.class);
			new_it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(new_it);

		}

	}
}
