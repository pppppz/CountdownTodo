package com.app.countdowntodolist.Foursquare.Main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.app.countdowntodolist.Constants.Constants;
import com.app.countdowntodolist.Foursquare.Criterias.VenuesCriteria;
import com.app.countdowntodolist.Foursquare.Listener.FoursquareVenuesRequestListener;
import com.app.countdowntodolist.Foursquare.Model.Venue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;


public class FoursquareVenuesNearbyRequest extends AsyncTask<String, Integer, List<Venue>> {

    private boolean VenueStyle = true;
    private Activity mActivity;
    private ProgressDialog mProgress;
    private FoursquareVenuesRequestListener mListener;
    private VenuesCriteria mCriteria;
    private boolean sslExp;
    private String TAG = "FoursquareVenuesNearbyRequest";

    public FoursquareVenuesNearbyRequest(Activity activity, FoursquareVenuesRequestListener listener, VenuesCriteria criteria) {
        mActivity = activity;
        mListener = listener;
        mCriteria = criteria;
    }

    public FoursquareVenuesNearbyRequest(Activity activity, VenuesCriteria criteria, boolean venueStyle) {
        mActivity = activity;
        mCriteria = criteria;
        VenueStyle = venueStyle; /** true = explore , false = search **/
    }


    @Override
    protected void onPreExecute() {
        mProgress = new ProgressDialog(mActivity);
        mProgress.setCancelable(false);
        mProgress.setMessage("Getting venues nearby ...");
        mProgress.show();
        super.onPreExecute();
    }

    @Override
    protected List<Venue> doInBackground(String... params) {

        String access_token = params[0];
        List<Venue> venues = new ArrayList<>();
        /** date required **/
        String apiDateVersion = Constants.API_DATE_VERSION;

        try {
            if (VenueStyle) {

                /** Call Foursquare to get the Venues around **/
                String uri = "https://api.foursquare.com/v2/venues/explore"
                        + "?v="
                        + apiDateVersion
                        + "&section=topPicks"
                        + "&ll="
                        + mCriteria.getLocation().getLatitude()
                        + ","
                        + mCriteria.getLocation().getLongitude()
                        + "&llAcc="
                        + mCriteria.getLocation().getAccuracy()
                        + "&query="
                        + mCriteria.getQuery()
                        + "&limit="
                        + mCriteria.getQuantity()
                        + "&intent="
                        + mCriteria.getIntent().getValue()
                        + "&radius="
                        + mCriteria.getRadius();

                Log.d("ptest", "url = " + uri);
                if (!access_token.equals("")) {
                    uri = uri + "&oauth_token=" + access_token;
                } else {
                    uri = uri + "&client_id=" + Constants.CLIENT_ID + "&client_secret=" + Constants.CLIENT_SECRET;
                }

                Log.d("ptest", uri);

                JSONObject venuesJson = executeHttpGet(uri);

                // Get return code
                int returnCode = Integer.parseInt(venuesJson.getJSONObject("meta").getString("code"));
                // 200 = OK
                if (returnCode == 200) {

                    JSONArray json = venuesJson.getJSONObject("response").getJSONArray("groups").getJSONObject(0).getJSONArray("items");
                    // Log.d("ptest" ,"1" + json);


                    for (int i = 0; i < json.length(); i++) {
                        //  Log.d("ptest", "json : " + json);


                        JSONObject jsonVenue = json.getJSONObject(i).getJSONObject("venue");
                        Log.d("ptest", i + " = " + jsonVenue);
                        Venue venue = new Venue();

                        venue.setName(jsonVenue.getString("name"));
                        Log.d("ptest", venue.getName());
                        venue.setId(jsonVenue.getString("id"));
                        venue.setDistance(jsonVenue.getJSONObject("location").getInt("distance"));
                        venue.setLatitude(jsonVenue.getJSONObject("location").getString("lat"));
                        venue.setLongtitude(jsonVenue.getJSONObject("location").getString("lng"));

                        try {
                            venue.setAddress(jsonVenue.getJSONObject("location").getString("address"));
                        } catch (JSONException je) {
                            Log.d("ptest", " JE = " + je);

                            venue.setAddress(jsonVenue.getJSONObject("location").getJSONArray("formattedAddress").getString(0));
                        }
                        // Log.d("ptest" , "venue = " + venue.getAddress());

                        venues.add(venue);
                    }

                } else {
                    if (mListener != null) {
                        Log.d("ptest", "4sqvenue error " + venuesJson.getJSONObject("meta").getString("errorDetail"));
                    }
                }
            }

            /***************************    search    *************************************/


            else {
                String uri = "https://api.foursquare.com/v2/venues/search"
                        + "?v="
                        + apiDateVersion
                        + "&ll="
                        + mCriteria.getLocation().getLatitude()
                        + ","
                        + mCriteria.getLocation().getLongitude()
                        + "&llAcc="
                        + mCriteria.getLocation().getAccuracy()
                        + "&query="
                        + mCriteria.getQuery()
                        + "&limit="
                        + mCriteria.getQuantity()
                        + "&intent="
                        + mCriteria.getIntent().getValue()
                        + "&radius="
                        + mCriteria.getRadius();


                if (!access_token.equals("")) {
                    uri = uri + "&oauth_token=" + access_token;
                } else {
                    uri = uri + "&client_id=" + Constants.CLIENT_ID + "&client_secret=" + Constants.CLIENT_SECRET;
                }
                //  Log.d("ptest" , "url : " +  uri);

                JSONObject venuesJson = executeHttpGet(uri);


                //Log.d("ptest" , venuesJson.toString());

                // Get return code
                int returnCode = venuesJson.getJSONObject("meta").getInt("code");

                // Log.d("ptest " , "code " + returnCode);
                // 200 = OK
                if (returnCode == 200) {

                    JSONArray json = venuesJson.getJSONObject("response").getJSONArray("venues");

                    for (int i = 0; i < json.length(); i++) {

                        Venue venue = new Venue();
                        venue.setName(json.getJSONObject(i).getString("name"));
                        String address;
                        try {
                            address = json.getJSONObject(i).getJSONObject("location").getString("address");
                        } catch (JSONException je) {
                            Log.e(TAG, je.toString());
                            address = json.getJSONObject(i).getJSONObject("location").getString("city");
                        }

                        Log.d("ptest", json.getJSONObject(i).getString("name") + address);
                        venue.setAddress(address);
                        venue.setId(json.getJSONObject(i).getString("id"));
                        venue.setLatitude(json.getJSONObject(i).getJSONObject("location").getString("lat"));
                        venue.setLongtitude(json.getJSONObject(i).getJSONObject("location").getString("lng"));
                        venue.setDistance(json.getJSONObject(i).getJSONObject("location").getInt("distance"));
                        venues.add(venue);

                    }

                } else {
                    if (mListener != null) {
                        Log.d("ptest", "4sqvenue error " + venuesJson.getJSONObject("meta").getString("errorDetail"));
                    }
                }
            }


        } catch (SSLPeerUnverifiedException sslExp) {
            this.sslExp = true;
            sslExp.printStackTrace();
        } catch (Exception exp) {
            exp.printStackTrace();
            if (mListener != null)
                Log.d("ptest", "4sqvenue error " + exp.toString());
        }
        return venues;
    }

    @Override
    protected void onPostExecute(List<Venue> venues) {
        if (sslExp) {
            Toast.makeText(mActivity, "You must log in to the Wifi network first, or disconnect from it and use cellular connection.", Toast.LENGTH_LONG).show();
            mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com")));
        }

        mProgress.dismiss();
        if (mListener != null)
            mListener.onVenuesFetched(venues);
        super.onPostExecute(venues);
    }

    // Calls a URI and returns the answer as a JSON object
    private JSONObject executeHttpGet(String uri) throws Exception {

        URL url = new URL(uri);
        HttpsURLConnection req = (HttpsURLConnection) url.openConnection();
        req.setRequestMethod("GET");

        BufferedReader r = new BufferedReader(new InputStreamReader(req.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = r.readLine()) != null) {
            sb.append(s);
        }

        return new JSONObject(sb.toString());
    }
}
