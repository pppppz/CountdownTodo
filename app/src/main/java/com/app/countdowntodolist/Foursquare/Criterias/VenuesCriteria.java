package com.app.countdowntodolist.Foursquare.Criterias;

import android.location.Location;
import android.location.LocationManager;

public class VenuesCriteria {

    private String mQuery = "";
    private int mRadius = 1000;
    private int mQuantity = 10;
    private Location mLocation = new Location(LocationManager.GPS_PROVIDER);
    private VenuesCriteriaIntent intent = VenuesCriteriaIntent.CHECKIN;
    private String section = "topPicks";

    public String getQuery() {
        return mQuery;
    }

    public void setQuery(String mQuery) {
        this.mQuery = mQuery;
    }

    public int getRadius() {
        return mRadius;
    }

    public void setRadius(int mRadius) {
        this.mRadius = mRadius;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int mQuantity) {
        this.mQuantity = mQuantity;
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location mLocation) {
        this.mLocation = mLocation;
    }

    public VenuesCriteriaIntent getIntent() {
        return intent;
    }

    public void setIntent(VenuesCriteriaIntent intent) {
        this.intent = intent;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public enum VenuesCriteriaIntent {
        BROWSE("browse"), CHECKIN("checkin"), MATCH("match");

        private final String value;

        VenuesCriteriaIntent(String string) {
            value = string;
        }

        public String getValue() {
            return value;
        }
    }

}
