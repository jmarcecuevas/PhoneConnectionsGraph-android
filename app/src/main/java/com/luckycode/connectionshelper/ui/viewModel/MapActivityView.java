package com.luckycode.connectionshelper.ui.viewModel;

import com.google.android.gms.maps.model.LatLng;
import com.luckycode.connectionshelper.model.TownVertex;

/**
 * Created by marcelocuevas on 10/1/17.
 */

public interface MapActivityView {
    void showClearButton();
    void hideClearButton();
    void showRecyclerView();
    void hideRecyclerView();
    void updateMap(TownVertex town);
    void drawRoute(LatLng from,LatLng to,int color);
    void drawMarker(LatLng position,String title);
    void showRecalculatedGraph();
    void showTotalCost(String cost);
    void showSettingsDialog(String normalCost,String extraDiff,String extraLargeDistance);
    void filter(String s);
    void setAdapter();
}
