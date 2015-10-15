package com.app.countdowntodolist.Foursquare.Main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.app.countdowntodolist.Foursquare.Criterias.VenuesCriteria;
import com.app.countdowntodolist.Foursquare.FoursquareConstants;
import com.app.countdowntodolist.Foursquare.Listener.FoursquareVenuesRequestListener;
import com.app.countdowntodolist.Foursquare.Model.Venue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;


public class FoursquareVenuesNearbyRequest extends AsyncTask<String, Integer, List<Venue>> {

    private Activity mActivity;
    private ProgressDialog mProgress;
    private FoursquareVenuesRequestListener mListener;
    private VenuesCriteria mCriteria;
    private boolean sslExp;

    public FoursquareVenuesNearbyRequest(Activity activity, FoursquareVenuesRequestListener listener, VenuesCriteria criteria) {
        mActivity = activity;
        mListener = listener;
        mCriteria = criteria;
    }

    public FoursquareVenuesNearbyRequest(Activity activity, VenuesCriteria criteria) {
        mActivity = activity;
        mCriteria = criteria;
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

        try {

            /** date required **/
            String apiDateVersion = FoursquareConstants.API_DATE_VERSION;
            /** Call Foursquare to get the Venues around **/
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
                uri = uri + "&client_id=" + FoursquareConstants.CLIENT_ID + "&client_secret=" + FoursquareConstants.CLIENT_SECRET;
            }

            JSONObject venuesJson = executeHttpGet(uri);

            // Get return code
            int returnCode = Integer.parseInt(venuesJson.getJSONObject("meta").getString("code"));
            // 200 = OK
            if (returnCode == 200) {

                JSONArray json = venuesJson.getJSONObject("response").getJSONArray("venues");

                for (int i = 0; i < json.length(); i++) {

                    Venue venue = new Venue();
                    venue.setName(json.getJSONObject(i).getString("name"));

                    /** get convert json array to json object **/
                    String location = json.getJSONObject(i).getString("location");
                    JSONObject object = new JSONObject(location);
                    venue.setAddress(object.getString("address"));
                    venue.setDistance(object.getInt("distance"));
                    venue.setLatitude(object.getString("lat"));
                    venue.setLongtitude(object.getString("lng"));
                    venues.add(venue);


                }
            } else {
                if (mListener != null) {
                    Log.d("ptest", "4sqvenue error " + venuesJson.getJSONObject("meta").getString("errorDetail"));
                }
            }

        } catch (SSLPeerUnverifiedException sslExp) {
            this.sslExp = true;
            sslExp.printStackTrace();
        } catch (Exception exp) {
            exp.printStackTrace();
            if (mListener != null)
                Log.d("ptest", "4sqvenue error " + exp.toString());
            //mListener.onError(exp.toString());
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
