package lock.whistle.unlock;

import java.util.ArrayList;

import my.wsu.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PagerActivity extends Activity {
	ViewPager pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pager);
		pager = (ViewPager) findViewById(R.id.viewPager);

		MyPagerAdapter adapter = new MyPagerAdapter(this);

		pager.setAdapter(adapter);

	}

	class MyPagerAdapter extends PagerAdapter {
		private ArrayList<View> views;
		LayoutInflater inflator;

		public MyPagerAdapter(Context c) {
			inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			views = new ArrayList<View>();
			View v = inflator.inflate(R.layout.activity_lock, null);
			views.add(v);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View v = views.get(position);
			container.addView(v);
			return v;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeAllViews();
			super.destroyItem(container, position, object);
		}

	}
}
