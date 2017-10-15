package com.luckycode.connectionshelper.ui.viewModel;

import com.luckycode.connectionshelper.model.Town;
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
    void filter(String s);
    void setAdapter();
}
