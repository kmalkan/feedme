package com.kunalcodes.feedme.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kunalcodes.feedme.R;
import com.kunalcodes.feedme.model.Place;

import java.util.List;

/**
 * Adapter for the Cards.
 * Created by malkan on 5/11/16.
 */
public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {
    private List<Place> places;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mCardView;
        protected TextView vTitle;
        protected TextView vDescription;
        protected TextView vCategories;
        protected TextView vDistance;
        protected TextView vAddress;
        protected TextView vPrice;
        protected ImageView vImage;
        protected RatingBar vRating;


        public PlaceViewHolder(View v) {
            super(v);
            mCardView = v;
            vTitle = (TextView) v.findViewById(R.id.title);
            vImage = (ImageView) v.findViewById(R.id.image);
            vDescription = (TextView) v.findViewById(R.id.description);
            vCategories = (TextView) v.findViewById(R.id.categories);
            vDistance = (TextView) v.findViewById(R.id.distance);
            vPrice = (TextView) v.findViewById(R.id.price);
            vRating = (RatingBar) v.findViewById(R.id.rating_bar);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PlaceAdapter(List<Place> places) {
        this.places = places;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simple_card_view, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new PlaceViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Place place = places.get(position);
        holder.vTitle.setText(place.getTitle());
        holder.vImage.setImageDrawable(place.getCardImageDrawable());
        holder.vDescription.setText(place.getAddress());
        holder.vDistance.setText(place.getDistance());
        holder.vCategories.setText(place.getCategories());
        holder.vPrice.setText(toPriceSymbol(place.getPrice()));
        holder.vRating.setRating(place.getRating());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return places.size();
    }

    private String toPriceSymbol(int priceLevel) {
        if (priceLevel == 0) {
            return "?";
        }

        StringBuilder builder = new StringBuilder();
        for(int i=0;i<priceLevel;i++) {
            builder.append('$');
        }
        return builder.toString();
    }
}
