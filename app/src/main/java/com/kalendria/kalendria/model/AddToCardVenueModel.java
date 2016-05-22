package com.kalendria.kalendria.model;

import com.google.android.gms.appindexing.Thing;

import java.util.ArrayList;

/**
 * Created by murugan on 01/03/16.
 */
public class AddToCardVenueModel {


    //venue details start

    private String venueName;
    private String venueID;
    private String venueOverallRatting;
    private String venuReviewCount;
    private String venuImage;

    public String getVenuImagethumb() {
        return venuImagethumb;
    }

    public void setVenuImagethumb(String venuImagethumb) {
        this.venuImagethumb = venuImagethumb;
    }

    private String venuImagethumb;


    public AddToCardVenueModel() {
    }

    /*below code for child items start */
    private ArrayList<AddToCardServiceModel> Items;

    public ArrayList<AddToCardServiceModel> getItems() {
        return Items;
    }

    public void setItems(ArrayList<AddToCardServiceModel> items) {
        Items = items;
    }
    /*below code for child items end */

    public String getVenuImage() {
        return venuImage;
    }

    public void setVenuImage(String venuImage) {
        this.venuImage = venuImage;
    }


    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueID() {
        return venueID;
    }

    public void setVenueID(String venueID) {
        this.venueID = venueID;
    }

    public String getVenueOverallRatting() {
        return venueOverallRatting;
    }

    public void setVenueOverallRatting(String venueOverallRatting) {
        this.venueOverallRatting = venueOverallRatting;
    }

    public String getVenuReviewCount() {
        return venuReviewCount;
    }

    public void setVenuReviewCount(String venuReviewCount) {
        this.venuReviewCount = venuReviewCount;
    }


    //cisty and region
    private String city;
    private String region;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }


}
