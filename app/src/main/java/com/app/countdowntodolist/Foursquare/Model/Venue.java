package com.app.countdowntodolist.Foursquare.Model;

import fi.foyt.foursquare.api.entities.HereNow;
import fi.foyt.foursquare.api.entities.Location;
import fi.foyt.foursquare.api.entities.Mayor;
import fi.foyt.foursquare.api.entities.Stats;

public class Venue {

    private String latitude, longtitude;

    private Integer distance;

    private String address;

    private String id;

    private String name;

    private Location location;

    private boolean verified;

    private Stats stats;

    private HereNow beenHere;

    private HereNow hereNow;

    private long createdAt;

    private Mayor mayor;

    private String timeZone;

    private String canonicalUrl;

    private String shortUrl;

    private boolean dislike;

    private String url;

    private boolean like;

    public String getTimeZone() {
        return timeZone;
    }

    public String getCanonicalUrl() {
        return canonicalUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public boolean isDislike() {
        return dislike;
    }

    public HereNow getHereNow() {
        return hereNow;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public Mayor getMayor() {
        return mayor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isVerified() {
        return verified;
    }

    public Stats getStats() {
        return stats;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }
}
