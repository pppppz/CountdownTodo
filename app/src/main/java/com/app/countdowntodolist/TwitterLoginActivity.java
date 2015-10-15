package com.app.countdowntodolist;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.twitter.Twitter;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class TwitterLoginActivity extends AppCompatActivity {

    private static String TAG = "TwitterLoginActivity";
    public Bitmap profilePic;
    String profilePicLink;
    ParseUser parseUser;
    String name, username;

    public static Bitmap DownloadImageBitmap(String url) {

        Log.d("ptest", "url " + url);
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e(TAG, "Error getting bitmap" + e);
        }
        return bm;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParseTwitterUtils.logIn(TwitterLoginActivity.this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {

                if (e == null) {

                    if (user == null) {
                        Log.d("MyApp", "Uh oh. The user cancelled the Twitter login.");
                        Intent intent = new Intent(TwitterLoginActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (user.isNew()) {
                        Log.d("MyApp", "User signed up and logged in through Twitter!");
                        try {
                            getUserDetailsFromTwitter();
                            Intent intent = new Intent(TwitterLoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        Log.d("MyApp", "User logged in through Twitter!");
                        Intent intent = new Intent(TwitterLoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Log.d("ptest", "twitter " + e);
                    Toast.makeText(TwitterLoginActivity.this, "Something went wrong. Please check your data", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TwitterLoginActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }

    private void saveProfilePicNewUser(Bitmap bitmap) {

        parseUser = ParseUser.getCurrentUser();
        /**  Saving profile photo as a ParseFile **/
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] data = stream.toByteArray();
        String thumbName = parseUser.getUsername().replaceAll("\\s+", "");
        final ParseFile parseFile = new ParseFile(thumbName + "_thumb.jpg", data);
        parseFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                parseUser.put("profileThumb", parseFile);
                /** Finally save all the user details **/
                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.d(TAG, "profileThumb has saved");
                    }
                });
            }
        });


    }

    private void saveNewUser(String name, String username) {
        parseUser = ParseUser.getCurrentUser();
        parseUser.put("first_name", name);
        parseUser.put("name", name);
        parseUser.setUsername(username);

        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    /** Hooray! Let them use the app now. Finally save all the user details **/
                    Toast.makeText(TwitterLoginActivity.this, "New user:" + TwitterLoginActivity.this.name + " Signed up", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Hooray! new user had created");
                } else {
                    /** Sign up didn't succeed. Look at the ParseException to figure out what went **/
                    Log.d(TAG, "didn't succeed =" + e);
                }
            }
        });

    }

    public void getUserDetailsFromTwitter() throws IOException {


        Twitter twitter = ParseTwitterUtils.getTwitter();

        /** deprecated from Parse library **/
        HttpGet verifyGet = new HttpGet("https://api.twitter.com/1.1/users/show.json?user_id=" + twitter.getUserId());

        ParseTwitterUtils.getTwitter().signRequest(verifyGet);
        OkHttpClient client = new OkHttpClient();
        Request request = null;

        try {
            request = new Request.Builder()
                    .url(verifyGet.getURI().toURL())
                    .addHeader("Authorization", verifyGet.getFirstHeader("Authorization").getValue())
                    .build();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                Log.d(TAG, "onFailure " + e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonString = response.body().string();
                    JSONObject jsonObj;
                    try {
                        jsonObj = new JSONObject(jsonString);
                        name = jsonObj.getString("name");
                        username = jsonObj.getString("screen_name");
                        profilePicLink = jsonObj.getString("profile_image_url");
                        saveNewUser(name, username);

                        Log.d("ptest", "profile pic link :" + profilePicLink);
                        ProfilePhotoAsync profilePhotoAsync = new ProfilePhotoAsync(profilePicLink);
                        profilePhotoAsync.execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    class ProfilePhotoAsync extends AsyncTask<String, String, Bitmap> {
        String ProfileLink;

        public ProfilePhotoAsync(String picLink) {
            ProfileLink = picLink;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            /** Fetching data from URI and storing in bitmap **/
            Log.d(TAG, "download photo");
            return profilePic = DownloadImageBitmap(ProfileLink);
        }

        @Override
        protected void onPostExecute(Bitmap picBitmap) {
            super.onPostExecute(picBitmap);
            if (picBitmap == null) {
                Log.d(TAG, "null bitmap");
            } else {
                saveProfilePicNewUser(picBitmap);
            }

        }
    }

}


