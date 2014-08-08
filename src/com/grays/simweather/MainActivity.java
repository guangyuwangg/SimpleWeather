package com.grays.simweather;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends ActionBarActivity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment(this)).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		private WeatherModel weatherModel;
		private WeatherView  weatherView;
		private Context context;
		
		// check period
		private int mInterval = 60000; // 60 seconds
		private Handler mHandler;
		private int count = 0;
		
		public PlaceholderFragment(Context con) {
			context = con;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			
			// create, initialize my model, view
			weatherModel = new WeatherModel();
			weatherView  = new WeatherView(rootView, weatherModel, context);
			
			// check if any weather update in background
			mHandler = new Handler();
			startRepeatingTask();

			return rootView;
		}
		
		// used to check if any weather update in background
		Runnable mWeatherChecker = new Runnable() {
			@Override
			public void run() {
				// first time enter the app: get and display the weather information
				// not first time: notify for an update
				if (count != 0) {
					weatherModel.checkWeather(true); // this function can change value of mInterval.
					count++;
				} else {
					weatherModel.checkWeather(false);
					count++;
				}
				mHandler.postDelayed(mWeatherChecker, mInterval);
			}
		};

		void startRepeatingTask() {
			mWeatherChecker.run();
		}

		void stopRepeatingTask() {
			mHandler.removeCallbacks(mWeatherChecker);
		}
	}
}
