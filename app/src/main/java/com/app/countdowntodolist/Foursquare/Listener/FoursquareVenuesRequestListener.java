package com.app.countdowntodolist.Foursquare.Listener;

import com.app.countdowntodolist.Foursquare.Model.Venue;

import java.util.List;

import javax.xml.transform.ErrorListener;


public interface FoursquareVenuesRequestListener extends ErrorListener {

    void onVenuesFetched(List<Venue> venues);

}
