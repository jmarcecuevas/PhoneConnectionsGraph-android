package com.luckycode.connectionshelper.ui.viewModel;

import com.google.android.gms.location.places.Place;

/**
 * Created by marcelocuevas on 10/1/17.
 */

public interface MapActivityView {
    void showClearButton();
    void hideClearButton();
    void showRecyclerView();
    void hideRecyclerView();
    void updateMap(Place place);
    void filter(String s);
    void setAdapter();
}
