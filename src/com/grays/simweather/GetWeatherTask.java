package com.grays.simweather;

import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

// A task to request/parse the weather information in JSON format
public class GetWeatherTask extends AsyncTask<String, String, JSONObject> {
	private OnTaskCompleted listener;
	private Bitmap iconHolder;
	
	public GetWeatherTask(OnTaskCompleted taskListener){
		listener = taskListener;
	}
	
    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

	@Override
	protected JSONObject doInBackground(String... params) {
       // Create query url
		String url = "http://ec2-54-198-118-231.compute-1.amazonaws.com:8080/wunderground/getWeather?input=%7B%22gps%22:%7B%22latitude%22:39.923614,%22longitude%22:116.396086,%22radius%22:0.0%7D%7D&oauthToken=ANDROIDINTERVIEW&requestInfo=%7B%22userId%22:%22"
		+((WeatherModel)listener).getUserId()
		+"%22%7D";
		
		// Getting JSON from URL
		GetJsonUsingURL jParser = new GetJsonUsingURL();
		JSONObject json = jParser.getJson(url);
		
		try {
			// Parse json objects 
			JSONObject platformResponse = json
					.getJSONObject("platformResponse");
			JSONObject weather = platformResponse.getJSONObject("weather");
			JSONObject currentCondition = weather
					.getJSONObject("currentCondition");
			JSONArray providerImages = currentCondition
					.getJSONArray("providerImages");
			JSONObject icon = providerImages.getJSONObject(1);
			String conditionIconUrl = icon.getString("url");

			// acquire the condition icon here
			iconHolder = getIconFromUrl(conditionIconUrl);
			
			// the app still connecting to Internet
			((WeatherModel)listener).setIsOffline(false);
			
		} catch (Exception e) {
			// indicate offline
			((WeatherModel)listener).setIsOffline(true);
			e.printStackTrace();
		}

        return json;
	}
	
	// Parse the json object into weather information here
	@Override
    protected void onPostExecute(JSONObject json) {
		
		try {
			// parse json objects
			JSONObject platformResponse = json.getJSONObject("platformResponse");
			JSONObject weather = platformResponse.getJSONObject("weather");
			JSONObject currentCondition = weather.getJSONObject("currentCondition");
			String condition = currentCondition.getString("currCondition");
			JSONObject weatherData = currentCondition.getJSONObject("weatherData");
			JSONObject temperatureData = weatherData.getJSONObject("temperatureData");
			float tempFahrenheit = Float.parseFloat(temperatureData.getString("tempFahrenheit"));
			float feelsLike = Float.parseFloat(temperatureData.getString("feelsLikeFahrenheit"));
			float tempCelsius = (tempFahrenheit - 32) * 5 / 9;
			float feelsLikeCelsius = (feelsLike - 32) * 5 / 9;
			
			JSONObject locationData = currentCondition.getJSONObject("locationData");
			String city = locationData.getString("city");
			String state = locationData.getString("state");
			String country = locationData.getString("country");
			
			((WeatherModel)listener).setCondition(condition);
			((WeatherModel)listener).setTemperature(tempCelsius);
			((WeatherModel)listener).setFeelsLike(feelsLikeCelsius);
			((WeatherModel)listener).setConditionIcon(iconHolder);
			((WeatherModel)listener).setLocation(city + ", " + state + ", " + country);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		listener.onTaskCompleted();
    }
	
	// given an url to an image, download the image
	public Bitmap getIconFromUrl(String bitmapUrl){
		try {
			URL url = new URL(bitmapUrl);
			return BitmapFactory.decodeStream(url.openConnection()
					.getInputStream());
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
