package com.kunalcodes.feedme.fragment;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.kunalcodes.feedme.R;
import com.kunalcodes.feedme.adapter.PlaceAdapter;
import com.kunalcodes.feedme.model.GooglePlace;
import com.kunalcodes.feedme.model.Place;
import com.kunalcodes.feedme.util.HttpRequestQueue;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by malkan on 5/12/16.
 */
public class PlaceListFragment extends Fragment {
    private static final String TAG = "FeedMe - PlaceList";
    private static final String ENCODING = "UTF-8";
    private static String PAGE_TOKEN = "";

    private List<Place> places;
    private Location mLocation;
    private Double mLatitude = 40.963739;
    private Double mLongitude = -74.081662;
    private String mKeyword = "";
    private String mPlacesRequestUrl;

    private RecyclerView mCardListView;
    private PlaceAdapter mPlaceAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Gson gson;

    public PlaceListFragment() {}

    /**
     * Returns a new instance of this fragment.
     */
    public static PlaceListFragment newInstance(int sectionNumber) {
        PlaceListFragment fragment = new PlaceListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mCardListView = (RecyclerView) rootView.findViewById(R.id.card_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mCardListView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(rootView.getContext());
        mCardListView.setLayoutManager(mLayoutManager);
        List<Place> placeList = getList();
        // specify an adapter
        mPlaceAdapter = new PlaceAdapter(placeList);
        mCardListView.setAdapter(mPlaceAdapter);
        gson = new Gson();

        mPlacesRequestUrl = new Uri.Builder()
                .scheme("https")
                .authority("maps.googleapis.com")
                .path("/maps/api/place/search/json")
                .appendQueryParameter("location", mLatitude + "," + mLongitude)
                .appendQueryParameter("opennow", "true")
                .appendQueryParameter("rankby","distance")
                .appendQueryParameter("types", "bakery|bar|cafe|meal_delivery|meal_takeaway|restaurant")
                .appendQueryParameter("key", getString(R.string.GOOGLE_KEY)).build().toString();

        Log.d(TAG, mPlacesRequestUrl);

        StringRequest getPlacesRequest = new StringRequest(Request.Method.GET, mPlacesRequestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        gson.fromJson(response, GooglePlace.class);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                Log.e(TAG, e.getMessage());
            }
        });
        StringRequest getDetailsRequest = new StringRequest(Request.Method.GET, mPlacesRequestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        gson.fromJson(response, GooglePlace.class);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                Log.e(TAG, e.getMessage());
            }
        });
        // Add the request to the RequestQueue.
        HttpRequestQueue requestQueue = HttpRequestQueue.getInstance(this.getContext());
        requestQueue.addToRequestQueue(getPlacesRequest);


        return rootView;
    }

    private List<Place> getList() {
        List<Place> placeList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Place place = new Place("Test",
                    "Description",
                    null,
                    "Thai",
                    new LatLng(0,0),
                    "$$",
                    2,
                    4.5f,
                    getResources().getDrawable(R.drawable.fm_filler, null) );
            placeList.add(place);
        }
        return placeList;
    }

    private String getPlacesUrl(double latitude, double longitude, boolean openNow,
                                String keyword, Integer page) {
        Uri.Builder uri = new Uri.Builder()
                .scheme("https")
                .authority("maps.googleapis.com")
                .path("/maps/api/place/search/json")
                .appendQueryParameter("location", latitude + "," + longitude)
                .appendQueryParameter("opennow", String.valueOf(openNow))
                .appendQueryParameter("rankby","distance")
                .appendQueryParameter("types", "bakery|bar|cafe|meal_delivery|meal_takeaway|restaurant")
                .appendQueryParameter("key", getString(R.string.GOOGLE_KEY));
        if (keyword != null) {
            uri.appendQueryParameter("keyword", keyword);
        }

        if (page != null) {
            uri.appendQueryParameter()
        }
    }
}
