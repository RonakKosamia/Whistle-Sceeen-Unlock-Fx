package lock.whistle.unlock;

import my.wsu.R;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class Notif {

	public static int notifId = 654654;

	public static Notification getNotification(Context context) {
		Intent notificationIntent = new Intent(context, ThemeActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, PendingIntent.FLAG_ONE_SHOT);

		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification.Builder builder = new Notification.Builder(context);

		builder.setContentIntent(contentIntent)
				.setSmallIcon(R.drawable.icon)
				.setLargeIcon(
						BitmapFactory.decodeResource(context.getResources(),
								R.drawable.icon))
				.setTicker("Change anytime theme from notification")
				.setWhen(System.currentTimeMillis()).setAutoCancel(false)
				.setContentTitle("Whistle Unlocker")
				.setContentText("click to change theme");
		Notification n = builder.build();

		nm.notify(notifId, n);
		return n;
	}

	public static void cancel(Context context) {
		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(notifId);

	}

}
