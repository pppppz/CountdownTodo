package com.app.countdowntodolist;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FacebookLoginActivity extends AppCompatActivity {

    public static final List<String> mPermissions = new ArrayList<String>() {{
        add("public_profile");
        add("email");
    }};
    public Bitmap bitmap;
    public Bitmap profilePic;
    Profile fbProfile;
    ParseUser parseUser;
    String name, email, firstName, lastName, user_id;
    Object profilePicObj;
    // public static final List<String> mPermissions  = Arrays.asList("publish_actions");

    public static Bitmap DownloadImageBitmap(String url) {
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
            Log.d("IMAGE", "completed");
        } catch (IOException e) {
            Log.e("IMAGE", "Error getting bitmap", e);
        }
        return bm;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //  Use this to output your Facebook Key Hash to Logs
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.app.countdowntodolist",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

            Log.e("PackageManager", "error = " + e);

        } catch (NoSuchAlgorithmException e) {
            Log.e("NoSuchAlgorithm", "error = " + e);
        }


        ParseFacebookUtils.logInWithReadPermissionsInBackground(FacebookLoginActivity.this, Arrays.asList("public_profile", "user_friends", "email", "user_birthday"), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {

                fbProfile = Profile.getCurrentProfile();

                if (user == null) {
                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Facebook!");
                    getUserDetailsFromFB();
                    Log.d("ptest", "after get user details");
                    Intent intent = new Intent(FacebookLoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    Log.d("ptest", "after start mainactivity");
                    finish();

                } else {
                    Log.d("MyApp", "User logged in through Facebook!");
                    Intent intent = new Intent(FacebookLoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }

    private void saveNewUser(String name, String firstName, String lastName, String user_id, String email, Object profilePicObj) {
        parseUser = ParseUser.getCurrentUser();
        parseUser.setUsername(name);
        parseUser.setEmail(email);
        parseUser.put("first_name", firstName);
        parseUser.put("last_name", lastName);
        parseUser.put("user_id", user_id);

        /* because facebook don't give permission of password. this line will set password by default
        for every user who signup with this application.
         */
        //  parseUser.put("password", "examplefacebookpassword");


//        Saving profile photo as a ParseFile
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        //  Bitmap bitmap = ((BitmapDrawable) mProfileImage.getDrawable()).getBitmap();
        //   Bitmap bitmap = ((BitmapDrawable) mProfileImage.getDrawable()).getBitmap();
        //   bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] data = stream.toByteArray();
        String thumbName = parseUser.getUsername().replaceAll("\\s+", "");
        final ParseFile parseFile = new ParseFile(thumbName + "_thumb.jpg", data);

        parseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.

                    //    parseUser.put("profileThumb", parseFile);

                    //Finally save all the user details
                    Toast.makeText(FacebookLoginActivity.this, "New user:" + FacebookLoginActivity.this.name + " Signed up", Toast.LENGTH_SHORT).show();
                    Log.d("ptest", "Hooray!");
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    Log.d("ptest", "didn't succeed =" + e);
                }
            }
        });

    }

    public void getUserDetailsFromFB() {


        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Insert your code here


                        try {
                            email = object.getString("email");
                            Log.d("ptest", email);
                            name = response.getJSONObject().getString("name");
                            Log.d("ptest", name);
                            firstName = response.getJSONObject().getString("first_name");
                            Log.d("ptest", firstName);
                            lastName = response.getJSONObject().getString("last_name");
                            Log.d("ptest", lastName);
                            user_id = response.getJSONObject().getString("id");
                            Log.d("ptest", user_id);

                            profilePicObj = response.getJSONObject().getJSONObject("picture");
                            //  profilePic = BitmapFactory.decodeFile(response.getJSONObject().get("picture"));
                            saveNewUser(name, firstName, lastName, user_id, email, profilePicObj);
                            Log.d("ptest ", name + "," + firstName + "," + lastName + "," + user_id + "," + email);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id,name,picture");
        request.setParameters(parameters);
        request.executeAsync();

        ProfilePhotoAsync profilePhotoAsync = new ProfilePhotoAsync(fbProfile);
        profilePhotoAsync.execute();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
     /*   if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);*/

        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    class ProfilePhotoAsync extends AsyncTask<String, String, Bitmap> {
        Profile profile;


        public ProfilePhotoAsync(Profile profile) {
            this.profile = profile;
            Log.d("ptest ", "profilephotoasync");
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            // Fetching data from URI and storing in bitmap
            Log.d("ptest ", "download photo");
            return bitmap = DownloadImageBitmap(profile.getProfilePictureUri(200, 200).toString());

        }

        @Override
        protected void onPostExecute(Bitmap s) {
            super.onPostExecute(s);
            //   mProfileImage.setImageBitmap(bitmap);
        }

    }
}

