package com.kunalcodes.feedme.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.kunalcodes.feedme.R;
import com.kunalcodes.feedme.adapter.PlaceAdapter;
import com.kunalcodes.feedme.model.Place;
import com.kunalcodes.feedme.model.response.FoursquarePhotosResponse;
import com.kunalcodes.feedme.model.response.FoursquareResponse;
import com.kunalcodes.feedme.model.response.GooglePlaceResponse;
import com.kunalcodes.feedme.util.HttpRequestQueue;

import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Queue;

/**
 * Created by malkan on 5/12/16.
 */
public class PlaceListFragment extends Fragment {
    private static final String TAG = "FeedMe - PlaceList";
    private static final String ENCODING = "UTF-8";
    private static String PAGE_TOKEN = "";
    private static final int VISIBLE_THRESHOLD = 5;
    private static final int MAX_DEQUEUE = 10;
    private static final int PLACE_PHOTO_WIDTH = 500;

    private List<Place> places;

    private Location mLocation;
    private Double mLatitude = 40.963739;
    private Double mLongitude = -74.081662;
    private String mKeyword = "";
    private String mPlacesRequestUrl;
    private String mPageToken;
    private RecyclerView mCardListView;
    private PlaceAdapter mPlaceAdapter;
    private LinearLayoutManager mLayoutManager;
    private ImageLoader mImageLoader;
    private boolean mLoadingItems;
    private int mPreviousTotal = 0;

    private Queue<GooglePlaceResponse.Result> resultsQueue;

    private Gson gson;
    private HttpRequestQueue requestQueue;

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
        mLayoutManager = new LinearLayoutManager(rootView.getContext());

        mCardListView = (RecyclerView) rootView.findViewById(R.id.card_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mCardListView.setHasFixedSize(true);

        // use a linear layout manager
        mCardListView.setLayoutManager(mLayoutManager);

        mCardListView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int mOnScreenItems = mCardListView.getChildCount();
                int mTotalItemsInList = mLayoutManager.getItemCount();
                int mFirstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                if (mLoadingItems) {
                    if (mTotalItemsInList > mPreviousTotal) {
                        mLoadingItems = false;
                        mPreviousTotal = mTotalItemsInList;
                    }
                }

                if (!mLoadingItems && (mTotalItemsInList - mOnScreenItems) <= (mFirstVisibleItem + VISIBLE_THRESHOLD)) {
                    fetchPlaces();
                    loadPlaces();
                    mPlaceAdapter.notifyDataSetChanged();
                    mLoadingItems = true;
                }
            }
        });

        places = new ArrayList<>();
        gson = new Gson();

        requestQueue = HttpRequestQueue.getInstance(this.getContext());
        mImageLoader = requestQueue.getImageLoader();
        resultsQueue = new ArrayDeque<>();

        mPlacesRequestUrl = getPlacesUrl(mLatitude, mLongitude, true, null, null);

        Log.d(TAG, mPlacesRequestUrl);

        StringRequest getPlacesRequest = new StringRequest(Request.Method.GET, mPlacesRequestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {
                        GooglePlaceResponse response = gson.fromJson(responseString,
                                GooglePlaceResponse.class);
                        if (response.getStatus().equals("OK")) {
                            mPageToken = response.getNextPageToken();
                            resultsQueue.addAll(response.getResults());
                            initAdapter();
                            mLoadingItems = false;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                Log.e(TAG, e.getMessage());
            }
        });
        // Add the request to the RequestQueue.
        mLoadingItems = true;
        requestQueue.addToRequestQueue(getPlacesRequest);

        return rootView;
    }

    private void fetchPlaces() {
        mPlacesRequestUrl = getPlacesUrl(mLatitude, mLongitude, true, null, mPageToken);
        StringRequest getPlacesRequest = new StringRequest(Request.Method.GET, mPlacesRequestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {
                        GooglePlaceResponse response = gson.fromJson(responseString,
                                GooglePlaceResponse.class);
                        if (response.getStatus().equals("OK")) {
                            mPageToken = response.getNextPageToken();
                            resultsQueue.addAll(response.getResults());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                Log.e(TAG, e.getMessage());
            }
        });
        requestQueue.addToRequestQueue(getPlacesRequest);
    }

    private void loadPlaces() {
        for (int i = 0; i < (resultsQueue.size() > MAX_DEQUEUE ? MAX_DEQUEUE : resultsQueue.size()) ; i++) {
            Place place = new Place();
            place.setCardImageDrawable(getResources().getDrawable(R.drawable.fm_filler, null));
            resultsQueue.remove().updatePlace(place);
            places.add(place);
            updatePlaceDetails(place);
        }
    }

    private void initAdapter() {
        loadPlaces();
        mPlaceAdapter = new PlaceAdapter(places);
        mCardListView.setAdapter(mPlaceAdapter);
    }

    private void updatePlaceDetails(final Place place) {
        String detailsRequestUrl = getDetailsUrl(mLatitude, mLongitude, place.getTitle());
        Log.d(TAG, detailsRequestUrl);
        StringRequest getDetailsRequest = new StringRequest(Request.Method.GET, detailsRequestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {
                        FoursquareResponse response = gson.fromJson(responseString, FoursquareResponse.class);
                        response.getResponse().getVenues().get(0).updatePlace(place);
                        updatePlacePhoto(place);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                Log.e(TAG, e.getMessage());
            }
        });

        requestQueue.addToRequestQueue(getDetailsRequest);
    }

    private void updatePlacePhoto(final Place place) {
        if (place.getFsId() != null) {
            StringRequest getPhotosRequest = new StringRequest(Request.Method.GET, getPhotosUrl(place.getFsId(), 1),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String responseString) {
                            FoursquarePhotosResponse response = gson.fromJson(responseString, FoursquarePhotosResponse.class);
                            List<FoursquarePhotosResponse.Item> items = response.getResponse().getPhotos().getItems();
                            if (items.size() > 0) {
                                setPlacePhoto(place, items.get(0));
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {
                    Log.e(TAG, e.getMessage());
                }
            });
            requestQueue.addToRequestQueue(getPhotosRequest);
        }
    }

    private void setPlacePhoto(final Place place, FoursquarePhotosResponse.Item item) {
        mImageLoader.get(item.getPrefix() + "width" + PLACE_PHOTO_WIDTH + item.getSuffix(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                place.setCardImageDrawable(new BitmapDrawable(getResources(), response.getBitmap()));
            }
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
            }
        });
    }

    private void setDefaultPlacePhoto(final Place place) {
        place.setCardImageDrawable(getResources().getDrawable(R.drawable.fm_filler, null));
    }

    private String getPlacesUrl(double latitude, double longitude, boolean openNow,
                                String keyword, String pageToken) {
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

        if (pageToken != null) {
            uri.appendQueryParameter("pagetoken", pageToken);
        }

        return uri.build().toString();
    }

    private String getDetailsUrl(double latitude, double longitude, String name) {
        return new Uri.Builder()
                .scheme("https")
                .authority("api.foursquare.com")
                .path("/v2/venues/search")
                .appendQueryParameter("ll", latitude + "," + longitude)
                .appendQueryParameter("limit", "1")
                .appendQueryParameter("query", name)
                .appendQueryParameter("client_id", getString(R.string.CLIENT_ID))
                .appendQueryParameter("client_secret", getString(R.string.CLIENT_SECRET))
                .appendQueryParameter("v", new SimpleDateFormat("yyyyMMdd", Locale.US).format(new Date()))
                .build().toString();
    }

    private String getPhotosUrl(String id, Integer limit) {
        return new Uri.Builder()
                .scheme("https")
                .authority("api.foursquare.com")
                .path("/v2/venues/" + id + "/photos")
                .appendQueryParameter("limit", limit == null ? "1" : limit.toString() )
                .appendQueryParameter("client_id", getString(R.string.CLIENT_ID))
                .appendQueryParameter("client_secret", getString(R.string.CLIENT_SECRET))
                .appendQueryParameter("v", new SimpleDateFormat("yyyyMMdd", Locale.US).format(new Date()))
                .build().toString();
    }
}
