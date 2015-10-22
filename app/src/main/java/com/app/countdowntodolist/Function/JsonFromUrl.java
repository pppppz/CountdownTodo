package com.app.countdowntodolist.Function;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonFromUrl extends AsyncTask<String, Void, String> {


    protected void onPreExecute() {
    }

    protected String doInBackground(String... link) {
        URL url;
        StringBuilder sb;
        String json = null;

        try {
            url = new URL(link[0]);
            Log.d("ptest", url.toString());
            HttpURLConnection req = (HttpURLConnection) url.openConnection();
            req.setRequestMethod("GET");
            BufferedReader r = new BufferedReader(new InputStreamReader(req.getInputStream()));
            sb = new StringBuilder();
            String s;
            while ((s = r.readLine()) != null) {
                sb.append(s);
            }
            json = sb.toString();

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    protected void onProgressUpdate(Integer... values) {

    }

    protected void onPostExecute(Void result) {


    }
}
