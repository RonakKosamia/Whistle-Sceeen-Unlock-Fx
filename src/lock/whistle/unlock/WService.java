package lock.whistle.unlock;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class WService extends Service {
	public static final int DETECT_NONE = 0;
	public static final int DETECT_WHISTLE = 1;
	public static int selectedDetection = DETECT_NONE;

	// detection parameters
	private DetectorThread detectorThread;
	private RecorderThread recorderThread;
	private Thread h;
	public static int whistleValue = 0;
	public static boolean flag;

	@Override
	public IBinder onBind(Intent arg0) {
		Log.d("main", "Bind");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		Log.d("main", "oncreate");
		super.onCreate();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// setHeadSetConnectEmulated();
		Log.d("main", "startcommand");
		selectedDetection = DETECT_WHISTLE;
		if (recorderThread == null) {

			recorderThread = new RecorderThread();
			recorderThread.start();
			detectorThread = new DetectorThread(recorderThread);
			detectorThread.start();
			if (h == null) {
				h = new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							while (recorderThread != null
									&& detectorThread != null) {
								Log.d("main", "listening "
										+ DetectorThread.totalWhistlesDetected);
								if (DetectorThread.totalWhistlesDetected > 1) {
									if (!flag) {
										Log.d("main", "flag" + flag);
										detectorThread.stopDetection();
										recorderThread.stopRecording();
										detectorThread = null;
										recorderThread = null;

										flag = true;
										// Intent homeIntent = new Intent(
										// Intent.ACTION_MAIN);
										// homeIntent
										// .addCategory(Intent.CATEGORY_HOME);
										// homeIntent
										// .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
										// | Intent.FLAG_ACTIVITY_CLEAR_TOP);
										// startActivity(homeIntent);
										if (LockActivity.activity != null) {
											LockActivity.activity.finish();
										}

										else {
											Intent homeIntent = new Intent(
													Intent.ACTION_MAIN);
											homeIntent
													.addCategory(Intent.CATEGORY_HOME);
											homeIntent
													.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
															| Intent.FLAG_ACTIVITY_CLEAR_TOP);
											startActivity(homeIntent);
										}
										break;
									}
								}
								Thread.sleep(200);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							h = null;
						}
					}

				});
				h.start();
			}
		}
		return START_NOT_STICKY;
	}

	// private void setHeadSetConnectEmulated() {
	// Intent headSetUnPluggedintent = new Intent(Intent.ACTION_HEADSET_PLUG);
	// headSetUnPluggedintent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY);
	// headSetUnPluggedintent.putExtra("state", 1); // 0 = unplugged 1 =
	// // Headset with
	// // microphone 2 =
	// // Headset without
	// // microphone
	// headSetUnPluggedintent.putExtra("name", "Headset");
	// // TODO: Should we require a permission?
	// getApplicationContext().sendOrderedBroadcast(headSetUnPluggedintent,
	// null);
	// }

	@Override
	public void onDestroy() {
		Log.d("main", "Destroy000000000");

		DetectorThread.totalWhistlesDetected = 0;
		flag = false;
		if (detectorThread != null) {
			Log.d("main", "Destroy");

			detectorThread.stopDetection();
			recorderThread.stopRecording();
			detectorThread = null;
			recorderThread = null;
			h = null;
		}
		// android.os.Process.killProcess(android.os.Process.myPid());

		super.onDestroy();
	}

}
