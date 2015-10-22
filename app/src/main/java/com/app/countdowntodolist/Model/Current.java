package com.app.countdowntodolist.Model;

import android.graphics.Bitmap;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Current")
public class Current extends ParseObject {


    public Current() {

    }

    public String getAddress() {
        return getString("address");
    }

    public void setAddress(String address) {
        put("address", address);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description", description);
    }

    public String getTitle() {
        return getString("title");
    }

    public void setTitle(String title) {
        put("title", title);
    }

    public String getObjectID() {
        return getString("objectId");
    }

    public void setDeadline(Date date) {
        put("deadlineAt", date);
    }

    public Date getdateDeadline() {
        return getDate("deadlineAt");

    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public String getTemp() {
        return getString("temp");
    }

    public void setTemp(String temp) {

        put("temp", temp);
    }

    public String getLatitude() {
        return getString("latitude");
    }

    public void setLatitude(String latitude) {
        put("latitude", latitude);
    }

    public String getLongitude() {
        return getString("longitude");
    }

    public void setLongitude(String longitude) {
        put("longitude", longitude);
    }

    public String getUser() {
        return getString("_user");
    }

    public void setUser(ParseUser currentUser) {
        put("user", currentUser);
    }

    public void setVenueIMG(Bitmap venueIMG) {
        put("image", venueIMG);
    }


}
