package com.grays.simweather;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;

public class WeatherView implements Observer{
	private WeatherModel weatherModel;
	
	// views
	private TextView condition;
	private TextView temperature;
	private TextView feelsLike;
	private ImageView conditionIcon;
	private TableRow entry1;
	private TextView location;
	private ProgressBar loading;
	private ImageView hasUpdate;
	private TextView updateMsg;
	private EditText userId;
	private Button saveButton;
	private ImageView offline;
	private Context context;
	private TextView welcomeMsg;
	
	public WeatherView(View rootView, WeatherModel model, Context con){
		weatherModel = model;
		model.addObserver(this);
		context = con;
		
		// default userId shown
		userId = (EditText) rootView.findViewById(R.id.userId);
		userId.setText("Guest");
		
		// save button to save user id
		saveButton = (Button) rootView.findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				weatherModel.setUserId(userId.getText().toString());
				welcomeMsg.setText("Hello, " + userId.getText().toString() + "!");
				InputMethodManager imm = (InputMethodManager)((Activity)context).getSystemService(
					      Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(userId.getWindowToken(), 0);
			}
		});
		
		// used to indicate the app status: offline
		offline = (ImageView) rootView.findViewById(R.id.offline);
		offline.setVisibility(View.GONE);
		
		welcomeMsg     = (TextView) rootView.findViewById(R.id.welcomeMsg);
		condition      = (TextView) rootView.findViewById(R.id.conditionText);
		temperature    = (TextView) rootView.findViewById(R.id.currTemp);
		feelsLike      = (TextView) rootView.findViewById(R.id.feelsLike);
		conditionIcon  = (ImageView) rootView.findViewById(R.id.conditionIcon);
		location       = (TextView) rootView.findViewById(R.id.location);
		loading        = (ProgressBar) rootView.findViewById(R.id.loading);
		updateMsg      = (TextView) rootView.findViewById(R.id.updateMsg);
		hasUpdate      = (ImageView) rootView.findViewById(R.id.update);
		entry1         = (TableRow) rootView.findViewById(R.id.entry1);
		
		loading.setVisibility(View.VISIBLE);
		hasUpdate.setVisibility(View.GONE);
		updateMsg.setVisibility(View.GONE);
		
		// update weather on click
		entry1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loading.setVisibility(View.VISIBLE);
				weatherModel.checkWeather(false);
			}
		});
		
	}
	
	@Override
	public void update(Observable observable, Object data) {
		// show an icon tells the user offline status
		if (weatherModel.getIsOffline()) offline.setVisibility(View.VISIBLE);
		
		// if user called for an update, display the information
		// else if background task called for an update, just notify user for update
		if (weatherModel.isWeatherChanged()) {
			// show notification
			hasUpdate.setVisibility(View.VISIBLE);
			updateMsg.setVisibility(View.VISIBLE);
		} else {
			welcomeMsg.setText("Hello, " + userId.getText().toString() + "!");
			hasUpdate.setVisibility(View.GONE);
			updateMsg.setVisibility(View.GONE);
			loading.setVisibility(View.INVISIBLE);
			temperature.setText("Temperature: " + weatherModel.getTemperature()+" °„C");
			condition.setText(weatherModel.getCondition());
			feelsLike.setText("Feels like: " + weatherModel.getFeelsLike()+" °„C");
			conditionIcon.setImageBitmap(weatherModel.getConditionIcon());
			location.setText(weatherModel.getLocation());
		}
	}
}
