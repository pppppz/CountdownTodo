package com.app.countdowntodolist;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.countdowntodolist.Constants.Constants;
import com.app.countdowntodolist.Foursquare.Criterias.VenuesCriteria;
import com.app.countdowntodolist.Foursquare.Main.FoursquareVenuesNearbyRequest;
import com.app.countdowntodolist.Foursquare.Model.Venue;
import com.app.countdowntodolist.Function.Weather;
import com.app.countdowntodolist.Function.getCurrentLocation;
import com.app.countdowntodolist.Model.Current;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AddEvent_Fragment extends Fragment {

    TextView.OnClickListener tvLocationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Fragment fragment = new Location_Fragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().add(R.id.frame_container, fragment).addToBackStack(null).commit();
        }
    };
    private String mAccessToken = "";
    private EditText TaskInput, DesInput;
    private TextView tvLocation, tvWeather;
    private FloatingActionButton mFab;
    private FragmentActivity fragmentActivity;
    private String temp, latitude, longtitude, titleLocation;
    private Button chooseImg;
    private String TAG;
    private View view;
    private Bitmap venueIMG;
    FloatingActionButton.OnClickListener floatingButtonListener = new FloatingActionButton.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (TaskInput.getText().length() > 0) {

                /**  Set up a progress dialog **/
                final ProgressDialog dialog = new ProgressDialog(fragmentActivity);
                dialog.setMessage("updating please wait");
                dialog.show();

                /** get data from task_input and set data by method in Current.class **/
                final Current c = new Current();
                c.setACL(new ParseACL(ParseUser.getCurrentUser()));
                c.setUser(ParseUser.getCurrentUser());
                c.setTitle(TaskInput.getText().toString());
                c.setDescription(DesInput.getText().toString());
                c.setTemp(temp);
                c.setLatitude(latitude);
                c.setLongitude(longtitude);
                c.setAddress(titleLocation);

                if (venueIMG != null) {

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    venueIMG.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                    byte[] data = stream.toByteArray();
                    final ParseFile parseFile = new ParseFile("img_venue.jpg", data);
                    c.put("image", parseFile);
                    /*parseFile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Log.d("ptest" , "upload venueIMG completed");
                        }
                    });*/

                }

                c.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        /** hidden soft keyboard before swap fragment **/
                        InputMethodManager inputManager = (InputMethodManager) fragmentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        dialog.dismiss();


                        /** finish this class and swap to Main class **/
                        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
                        fragmentManager.beginTransaction().remove(AddEvent_Fragment.this).commit();
                    }
                });
            }

        }
    };
    private String venueID;
    private ParseUser parseUser;
    private int PICK_IMAGE_REQUEST = 1;
    Button.OnClickListener chooseImgLisnter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent();
// Show only images, no videos or anything else
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.addevent_fragment, container, false);
        fragmentActivity = getActivity();
        TaskInput = (EditText) view.findViewById(R.id.task_input);
        DesInput = (EditText) view.findViewById(R.id.etDescription);
        tvLocation = (TextView) view.findViewById(R.id.tvLocation);
        tvLocation.setOnClickListener(tvLocationListener);
        tvWeather = (TextView) view.findViewById(R.id.tvWeather);
        chooseImg = (Button) view.findViewById(R.id.chooseIMG);
        chooseImg.setOnClickListener(chooseImgLisnter);

        //declare and config floating button
        mFab = (FloatingActionButton) view.findViewById(R.id.FAB_ADD_EVENT);
        mFab.setOnClickListener(floatingButtonListener);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        GetLocation(5);
        Weather weather = new Weather(latitude, longtitude, fragmentActivity);
        temp = weather.getTemp();
        tvWeather.setText(temp);
        tvLocation.setText(titleLocation);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                venueIMG = MediaStore.Images.Media.getBitmap(fragmentActivity.getContentResolver(), uri);
                Log.d("ptest", "photo in binary = " + String.valueOf(venueIMG));

                ImageView imageView = (ImageView) view.findViewById(R.id.imgView);
                imageView.setImageBitmap(venueIMG);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void GetLocation(int radius) {

        /** setting venues criteria **/
        VenuesCriteria venuesCriteria = new VenuesCriteria();
        venuesCriteria.setQuantity(1);
        venuesCriteria.setRadius(radius);
        venuesCriteria.setSection("topPicks");
        venuesCriteria.setIntent(VenuesCriteria.VenuesCriteriaIntent.BROWSE);
        Location locationHere = getCurrentLocation.get(getActivity());
        venuesCriteria.setLocation(locationHere);

        /** true = explore / false = search **/
        FoursquareVenuesNearbyRequest request = new FoursquareVenuesNearbyRequest(fragmentActivity, venuesCriteria, false);
        request.execute(getAccessToken());

        List<Venue> venues = new ArrayList<>();
        try {
            venues = request.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if (venues != null) {

            try {
                titleLocation = venues.get(0).getName();
                latitude = venues.get(0).getLatitude();
                longtitude = venues.get(0).getLongtitude();
                venueID = venues.get(0).getId();


            } catch (IndexOutOfBoundsException e) {

                Log.d("ptest ", "radius = " + radius);

                /** expand radius if no venue nearby **/
                if (radius == 5) {
                    GetLocation(10);
                } else if (radius == 10) {
                    GetLocation(50);
                } else if (radius == 50) {
                    GetLocation(100);
                } else if (radius == 100) {
                    GetLocation(300);
                } else if (radius == 300) {
                    GetLocation(500);
                } else if (radius == 500) {
                    GetLocation(1000);
                } else {
                    GetLocation(5000);
                }

                e.printStackTrace();

            }

        }

    }

    private String getAccessToken() {
        if (mAccessToken.equals("")) {
            SharedPreferences settings = fragmentActivity.getSharedPreferences(Constants.SHARED_PREF_FILE, 0);
            mAccessToken = settings.getString(Constants.ACCESS_TOKEN, "");
        }
        return mAccessToken;
    }
}

