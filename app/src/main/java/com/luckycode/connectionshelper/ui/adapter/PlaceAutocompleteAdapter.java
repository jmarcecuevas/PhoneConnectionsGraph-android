package com.luckycode.connectionshelper.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;
import com.luckycode.connectionshelper.R;

import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * Created by marcelocuevas on 10/1/17.
 */

public class PlaceAutocompleteAdapter extends RecyclerView.Adapter<PlaceAutocompleteAdapter.PlaceViewHolder> implements Filterable{

    private Context context;
    private PlaceAutoCompleteInterface listener;
    private static final CharacterStyle STYLE_BOLD = new StyleSpan(Typeface.BOLD);
    private ArrayList<PlaceAutocomplete> mResultList;
    private GoogleApiClient googleApiClient;
    private AutocompleteFilter mPlaceFilter;

    public PlaceAutocompleteAdapter(Context context, GoogleApiClient googleApiClient, AutocompleteFilter filter){
        this.context = context;
        this.googleApiClient = googleApiClient;
        mPlaceFilter = filter;
        this.listener = (PlaceAutoCompleteInterface) this.context;
    }

    public void clearList(){
        if(mResultList!=null && mResultList.size()>0){
            mResultList.clear();
        }
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null) {
                    mResultList = getAutocomplete(constraint);
                    if (mResultList != null) {
                        results.values = mResultList;
                        results.count = mResultList.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
            }
        };
        return filter;
    }

    private ArrayList<PlaceAutocomplete> getAutocomplete(CharSequence constraint) {
        if (googleApiClient.isConnected()) {
            PendingResult<AutocompletePredictionBuffer> results= Places.GeoDataApi
                    .getAutocompletePredictions(googleApiClient,constraint.toString(),null,mPlaceFilter);

            AutocompletePredictionBuffer autocompletePredictions = results
                    .await(60, TimeUnit.SECONDS);

            final Status status = autocompletePredictions.getStatus();
            if (!status.isSuccess()) {
                Log.e("", "Error getting autocomplete prediction API call: " + status.toString());
                autocompletePredictions.release();
                return null;
            }

            Iterator<AutocompletePrediction> iterator = autocompletePredictions.iterator();
            ArrayList resultList = new ArrayList<>(autocompletePredictions.getCount());
            while (iterator.hasNext()) {
                AutocompletePrediction prediction = iterator.next();
                // Get the details of this prediction and copy it into a new PlaceAutocomplete object.
                resultList.add(new PlaceAutocomplete(prediction.getPlaceId(),
                        prediction.getPrimaryText(STYLE_BOLD),prediction.getSecondaryText(STYLE_BOLD)));
            }
            // Release the buffer now that all data has been copied.
            autocompletePredictions.release();

            return resultList;
        }
        return null;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(R.layout.view_placesearch, viewGroup, false);
        PlaceViewHolder mPredictionHolder = new PlaceViewHolder(convertView);
        return mPredictionHolder;
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder mPredictionHolder, final int i) {
        mPredictionHolder.mAddress.setText(mResultList.get(i).name);
        mPredictionHolder.mDetail.setText(mResultList.get(i).detail);

        mPredictionHolder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPlaceClick(mResultList.get(i));
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mResultList != null)
            return mResultList.size();
        else
            return 0;
    }

    public PlaceAutocomplete getItem(int position) {
        return mResultList.get(position);
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mParentLayout;
        private TextView mAddress;
        private TextView mDetail;

        private PlaceViewHolder(View itemView) {
            super(itemView);
            mParentLayout = itemView.findViewById(R.id.predictedRow);
            mAddress = itemView.findViewById(R.id.address);
            mDetail= itemView.findViewById(R.id.detail);
        }
    }

    /**
     * Holder for Places Geo Data Autocomplete API results.
     */
    public class PlaceAutocomplete {
        public CharSequence placeId;
        public CharSequence name;
        public CharSequence detail;

        PlaceAutocomplete(CharSequence placeId, CharSequence name,CharSequence detail) {
            this.placeId = placeId;
            this.name = name;
            this.detail=detail;
        }

        @Override
        public String toString() {
            return name.toString();
        }
    }

    public interface PlaceAutoCompleteInterface{
        void onPlaceClick(PlaceAutocomplete placeAutocomplete);
    }
}