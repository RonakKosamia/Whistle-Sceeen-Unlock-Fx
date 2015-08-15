package lock.whistle.unlock;

import java.util.ArrayList;

import my.wsu.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ThemeActivity extends Activity implements OnClickListener,
		OnPageChangeListener {
	SharedPreferences prefs;
	Editor edit;
	ArrayList<ToggleButton> list = new ArrayList<ToggleButton>();
	// WakeLock lock;
	ToggleButton d1, d2, d3;
	ToggleButton view;
	int temp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_theme);
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		// lock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "wake");
		// lock.acquire();
		TextView result = (TextView) findViewById(R.id.result_text);
		result.setText("Lock Screens");
		Typeface face = Typeface.createFromAsset(getAssets(), "fonts/bahu.TTF");
		result.setTypeface(face);
		d1 = (ToggleButton) findViewById(R.id.toggleButton1);
		d2 = (ToggleButton) findViewById(R.id.toggleButton2);
		d3 = (ToggleButton) findViewById(R.id.toggleButton3);
		d1.setChecked(true);

		list.add(d1);
		list.add(d2);
		list.add(d3);

		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		edit = prefs.edit();
		Button more = (Button) findViewById(R.id.button1);
		Button rate = (Button) findViewById(R.id.button2);
		ToggleButton apply = view = (ToggleButton) findViewById(R.id.apply);
		rate.setOnClickListener(this);
		more.setOnClickListener(this);
		apply.setOnClickListener(this);
		pager = (ViewPager) findViewById(R.id.view_pager);
		ImagePagerAdapter adapter = new ImagePagerAdapter();
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(this);
		temp = prefs.getInt("theme_no", 0);
		if (temp == 0)
			view.setChecked(true);
	}

	ViewPager pager;

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.button1:
			Intent rate = new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://play.google.com/store/apps/details?id="
							+ getPackageName()));
			startActivity(rate);
			break;
		case R.id.button2:
			Intent it = new Intent(ThemeActivity.this, MoreActivity.class);
			startActivity(it);
			break;
		case R.id.apply:
			int pos = temp = pager.getCurrentItem();
			edit.putInt("theme_no", pos);
			edit.commit();
			view = (ToggleButton) arg0;
			view.setChecked(true);
			Toast.makeText(getApplicationContext(), "Lock Theme applied",
					Toast.LENGTH_LONG).show();

			break;

		}

	}

	private class ImagePagerAdapter extends PagerAdapter {
		private int[] mImages = new int[] { R.drawable.lock1, R.drawable.lock2,
				R.drawable.lock3 };

		@Override
		public int getCount() {
			return mImages.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((ImageView) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Context context = ThemeActivity.this;
			ImageView imageView = new ImageView(context);
			int padding = 8;
			imageView.setPadding(padding, padding, padding, padding);
			imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			imageView.setImageResource(mImages[position]);
			imageView.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.border));
			imageView.setOnClickListener(imageClick);
			((ViewPager) container).addView(imageView, 0);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((ImageView) object);
		}
	}

	OnClickListener imageClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent it = new Intent(getApplicationContext(), LockActivity.class);
			it.putExtra("theme_no", pos);
			startActivity(it);
			overridePendingTransition(0, 0);

		}
	};

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setChecked(false);
		}
		list.get(arg0).setChecked(true);

		pos = arg0;

		if (pos == temp) {
			view.setChecked(true);
		} else {
			view.setChecked(false);
		}
	}

	public static int pos;

	@Override
	protected void onPause() {
		// if (lock != null && lock.isHeld())
		// lock.release();
		super.onPause();
	}
}
