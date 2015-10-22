package com.app.countdowntodolist.Function;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.util.Log;

import com.app.countdowntodolist.Constants.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


public class Weather {

    private String longitude;
    private String latitude;
    private String wind;
    private String name;
    private String temp;
    private Context context;
    private boolean HaveLocation = false;

    public Weather(Context c) {
        this.context = c;
        HaveLocation = false;
        init();
    }

    public Weather(String lat, String lon, Context context) {

        this.latitude = lat;
        this.longitude = lon;
        this.context = context;
        HaveLocation = true;
        init();
    }


    public void init() {

        String lat, lon;

        /** prepare link for query **/
        if (!HaveLocation) {
            Location locationHere = getCurrentLocation.get(context);
            lat = String.valueOf(locationHere.getLatitude());
            lon = String.valueOf(locationHere.getLongitude());
        } else {
            lat = this.latitude;
            lon = this.longitude;
        }

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        String units = SP.getString("unitFormat", "metric");
        Log.d("ptest", "units : " + units);
        String link = "http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&units=" + units + "&appid=" + Constants.API_KEY_OPENWEATHER;

        /** get JSON from URL **/
        JsonFromUrl JsonFromUrl = new JsonFromUrl();
        String json = null;
        try {
            json = JsonFromUrl.execute(link).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        /** convert JSON data to information **/
        try {
            JSONObject jsonObject = new JSONObject(json);
            lon = jsonObject.getJSONObject("coord").getString("lon");
            lat = jsonObject.getJSONObject("coord").getString("lat");
            temp = jsonObject.getJSONObject("main").getString("temp");
            wind = jsonObject.getJSONObject("wind").getString("speed");
            name = jsonObject.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getTemp() {
        return temp;
    }

    public String getWind() {
        return wind;
    }
}
