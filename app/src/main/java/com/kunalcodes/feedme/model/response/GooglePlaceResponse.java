package com.kunalcodes.feedme.model.response;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kunalcodes.feedme.model.Place;

import java.util.List;

import lombok.Data;
import lombok.experimental.Builder;

/**
 * Class representing a single Place from the payload of the Google Places API
 * Created by malkan on 5/17/16.
 */

@Builder
@Data
public class GooglePlaceResponse {
    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions;

    @SerializedName("next_page_token")
    @Expose
    private String nextPageToken;

    @SerializedName("results")
    @Expose
    private List<Result> results;

    @SerializedName("status")
    @Expose
    private String status;

    @Data
    public static class Result {
        @SerializedName("geometry")
        @Expose
        private Geometry geometry;

        @SerializedName("icon")
        @Expose
        public String icon;

        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("opening_hours")
        @Expose
        private OpeningHours openingHours;

        @SerializedName("photos")
        @Expose
        private List<Photo> photos;

        @SerializedName("place_id")
        @Expose
        private String placeId;

        @SerializedName("price_level")
        @Expose
        private int priceLevel;

        @SerializedName("reference")
        @Expose
        private String reference;

        @SerializedName("scope")
        @Expose
        private String scope;

        @SerializedName("types")
        @Expose
        private List<String> types;

        @SerializedName("vicinity")
        @Expose
        private String vicinity;

        @SerializedName("rating")
        @Expose
        private float rating;

        public void updatePlace(Place place) {
            place.setTitle(getName());
            place.setAddress(getVicinity());
            place.setLatLng(new LatLng(getGeometry().getLocation().getLatitude(),
                    getGeometry().getLocation().getLongitude()));
            place.setPrice(getPriceLevel());
            place.setRating(getRating());
        }
    }

    @Data
    public static class Geometry {
        @SerializedName("location")
        @Expose
        private Location location;
    }

    @Data
    public static class Location {
        @SerializedName("lat")
        @Expose
        private Double latitude;

        @SerializedName("lng")
        @Expose
        private Double longitude;
    }

    @Data
    public static class OpeningHours {
        @SerializedName("open_now")
        @Expose
        private Boolean openNow;

        @SerializedName("weekday_text")
        @Expose
        private List<String> weekdayText;
    }

    @Data
    public static class Photo {

        @SerializedName("height")
        @Expose
        private int height;

        @SerializedName("width")
        @Expose
        private int width;

        @SerializedName("html_attributions")
        @Expose
        private List<String> htmlAttributions;

        @SerializedName("photo_reference")
        @Expose
        private String photoReference;
    }
}
