package com.kunalcodes.feedme.model;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import lombok.Data;
import lombok.experimental.Builder;

/**
 * Class representing a single Place from the payload of the Google Places API
 * Created by malkan on 5/17/16.
 */

@Builder
@Data
public class GooglePlace {
    private String id;
    private String name;
    private OpeningHours openingHours;
    private List<Photo> photos;
    private String placeId;
    private int priceLevel;
    private float rating;
    private String reference;
    private List<String> types;
    private String vicinity;
    private Geometry geometry;

    public void updatePlace(Place place) {
        place.setTitle(getName());
        place.setAddress(getVicinity());
        place.setLatLng(new LatLng(getGeometry().getLocation().getLatitude(),
                getGeometry().getLocation().getLongitude()));
        place.setPrice(getPriceLevel());
        place.setRating(getRating());
    }

    @Data
    public static class Geometry {
        private Location location;
    }

    @Data
    public static class Location {
        private double latitude;
        private double longitude;
    }

    @Data
    public static class OpeningHours {
        private boolean openNow;
        private List<String> weekdayText;
    }

    @Data
    public static class Photo {
        private int height;
        private int width;
        private List<String> htmlAttributions;
        private String photoReference;
    }
}
