package com.kunalcodes.feedme.model.response;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kunalcodes.feedme.model.Place;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.experimental.Builder;

/**
 * Created by malkan on 5/18/16.
 */
@Builder
@Data
public class FoursquareResponse {
    
    @SerializedName("meta")
    @Expose
    private Meta meta;

    @SerializedName("response")
    @Expose
    private Response response;

    @Data
    public static class Meta {
        
        @SerializedName("code")
        @Expose
        private int code;
        @SerializedName("requestId")
        @Expose
        private String requestId;
        
    }

    @Data
    public static class Response {
        
        @SerializedName("venues")
        @Expose
        private List<Venue> venues;
        
    }

    @Data
    public static class Venue {
        
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("contact")
        @Expose
        private Contact contact;
        @SerializedName("location")
        @Expose
        private Location location;
        @SerializedName("categories")
        @Expose
        private List<Category> categories;
        @SerializedName("verified")
        @Expose
        private boolean verified;
        @SerializedName("stats")
        @Expose
        private Stats stats;
        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("hasMenu")
        @Expose
        private boolean hasMenu;
        @SerializedName("menu")
        @Expose
        private Menu menu;
        @SerializedName("allowMenuUrlEdit")
        @Expose
        private boolean allowMenuUrlEdit;
        @SerializedName("specials")
        @Expose
        private Specials specials;
        @SerializedName("venuePage")
        @Expose
        private VenuePage venuePage;
        @SerializedName("hereNow")
        @Expose
        private HereNow hereNow;
        @SerializedName("referralId")
        @Expose
        private String referralId;
        @SerializedName("venueChains")
        @Expose
        private List<Object> venueChains;

        public void updatePlace(Place place) {
            List<String> categoryList = new ArrayList<>();
            for (Category category : categories) {
                categoryList.add(category.getName());
            }
            place.setCategories(TextUtils.join(",",categoryList));
            place.setFsId(getId());
            place.setDistance(getLocation().getDistance() + " m");
            if (getMenu() != null && getMenu().getMobileUrl() != null) {
                place.setDescription(getMenu().getMobileUrl());
            }
        }
        
    }

    @Data
    public static class Category {
        
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("pluralName")
        @Expose
        private String pluralName;
        @SerializedName("shortName")
        @Expose
        private String shortName;
        @SerializedName("icon")
        @Expose
        private Icon icon;
        @SerializedName("primary")
        @Expose
        private boolean primary;
        
    }

    @Data
    public static class Contact {

        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("formattedPhone")
        @Expose
        private String formattedPhone;
        @SerializedName("twitter")
        @Expose
        private String twitter;
        
    }

    @Data
    public static class HereNow {

        @SerializedName("count")
        @Expose
        private int count;
        @SerializedName("summary")
        @Expose
        private String summary;
        @SerializedName("groups")
        @Expose
        private List<Object> groups;

    }

    @Data
    public static class Icon {

        @SerializedName("prefix")
        @Expose
        private String prefix;
        @SerializedName("suffix")
        @Expose
        private String suffix;

    }

    @Data
    public static class Location {

        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("crossStreet")
        @Expose
        private String crossStreet;
        @SerializedName("lat")
        @Expose
        private double lat;
        @SerializedName("lng")
        @Expose
        private double lng;
        @SerializedName("distance")
        @Expose
        private int distance;
        @SerializedName("postalCode")
        @Expose
        private String postalCode;
        @SerializedName("cc")
        @Expose
        private String cc;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("formattedAddress")
        @Expose
        private List<String> formattedAddress;

    }

    @Data
    public static class Menu {

        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("label")
        @Expose
        private String label;
        @SerializedName("anchor")
        @Expose
        private String anchor;
        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("mobileUrl")
        @Expose
        private String mobileUrl;

    }

    @Data
    public static class Specials {

        @SerializedName("count")
        @Expose
        private int count;
        @SerializedName("items")
        @Expose
        private List<Object> items;

    }

    @Data
    public static class Stats {

        @SerializedName("checkinsCount")
        @Expose
        private int checkinsCount;
        @SerializedName("usersCount")
        @Expose
        private int usersCount;
        @SerializedName("tipCount")
        @Expose
        private int tipCount;

    }

    @Data
    public static class VenuePage {

        @SerializedName("id")
        @Expose
        private String id;

    }
}
