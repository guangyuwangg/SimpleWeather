package com.grays.simweather;

import java.util.Observable;
import android.graphics.Bitmap;



public class WeatherModel extends Observable implements OnTaskCompleted{
	private String curCondition = "";
	private String curTemperature = "";
	private String feelsLike = "";
	private Bitmap curConditionIcon;
	private String location = "";
	private boolean weatherUpdated = false;
	private boolean backgroundChecking = false;
	private String userId = "Guest";
	private boolean isOffline = false;
	
	public WeatherModel(){
		setChanged();
	}
	// get current condition as a String
	public String getCondition(){
		return curCondition;
	}
	// get current temperature as a string
	public String getTemperature(){
		return curTemperature;
	}
	// get the corresponding icon for current condition
	public Bitmap getConditionIcon(){
		return curConditionIcon;
	}
	// get the location information
	public String getLocation(){
		return location;
	}
	// get the temperature "feels like" as a string
	public String getFeelsLike(){
		return feelsLike;
	}
	// get the user id
	public String getUserId(){
		return userId;
	}
	// get the indicator which shows if the app is offline
	public boolean getIsOffline(){
		return isOffline;
	}
	// set offline status
	public void setIsOffline(boolean offline){
		isOffline = offline;
	}
	// set user id
	public void setUserId(String id){
		userId = id;
	}
	// set location
	public void setLocation(String loc){
		location = loc;
		setChanged();
		notifyObservers();
	}
	
	public void setFeelsLike(float temp){
		// keep 1 significant digit
		String tmp = String.format("%.1f", temp);
		if(!feelsLike.equals(tmp))      // if info is updated, app should notify user
			weatherUpdated = true;
		feelsLike  = tmp;
	}

	public void setConditionIcon(Bitmap bitmap) {
		curConditionIcon = bitmap;
	}
	
	public void setTemperature(float temp){
		String tmp = String.format("%.1f", temp);
		if(!curTemperature.equals(tmp)) // if info is updated, app should notify user
			weatherUpdated = true;
		curTemperature = tmp;
	}
	
	public void setCondition(String con){
		if(!curCondition.equals(con))   // if info is updated, app should notify user
			weatherUpdated = true;
		curCondition = con;
	}
	// check if there's a weather update
	public boolean isWeatherChanged(){
		return weatherUpdated;
	}
	// check the current weather, 
	// isBackgroundChecking is used to indicate the caller of the checking
	public void checkWeather(boolean isBackgroundChecking) {
		backgroundChecking = isBackgroundChecking;
		new GetWeatherTask(this).execute();
	} 
	
	@Override
	protected void setChanged() {
		super.setChanged();
	}

	@Override
	public void onTaskCompleted() {
		// if user initialized the check, the new inforamtion should be displayed
		if (!backgroundChecking) {
			weatherUpdated = false;
		}
		setChanged();
		notifyObservers();
	}
	
}
