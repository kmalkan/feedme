package com.kunalcodes.feedme.model;

import android.graphics.drawable.Drawable;
import com.google.android.gms.maps.model.LatLng;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

/**
 * Represents a card containing restaurant data.
 * Created by malkan on 5/12/16.
 */
@Builder
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
@NoArgsConstructor
public class Place {
    private String title;
    private String fsId;
    private String description;
    private String categories;
    private String distance;
    private LatLng latLng;
    private String address;
    private int price;
    private float rating;
    private Drawable cardImageDrawable;

}