package com.luckycode.connectionshelper.ui.activity;

import android.support.annotation.NonNull;
import android.os.Bundle;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.android.gms.common.ConnectionResult;
import com.luckycode.connectionshelper.interactor.MapInteractor;
import com.luckycode.connectionshelper.ui.adapter.PlaceAutocompleteAdapter.PlaceAutocomplete;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;
import com.luckycode.connectionshelper.R;
import com.luckycode.connectionshelper.common.LuckyActivity;
import com.luckycode.connectionshelper.presenter.MapPresenter;
import com.luckycode.connectionshelper.ui.adapter.PlaceAutocompleteAdapter;
import com.luckycode.connectionshelper.ui.viewModel.MapActivityView;
import com.luckycode.connectionshelper.utils.KeyboardHelper;

import butterknife.BindView;
import butterknife.OnClick;

public class MapActivity extends LuckyActivity implements PlaceAutocompleteAdapter.PlaceAutoCompleteInterface, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,OnMapReadyCallback, TextWatcher,MapActivityView{

    @BindView(R.id.list_search)RecyclerView mRecyclerView;
    @BindView(R.id.search_et)AutoCompleteTextView mSearchEdittext;
    @BindView(R.id.clear)ImageView mClear;
    private MapPresenter mPresenter;
    private MapInteractor mapInteractor;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;
    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(new LatLng(-0, 0), new LatLng(0, 0));

    @Override
    protected void init() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();

        mPresenter=new MapPresenter(this,dbHelper);

        setRecyclerView();
        mSearchEdittext.addTextChangedListener(this);
    }

    public void setRecyclerView(){
        LinearLayoutManager linearlm=new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(linearlm);
        mAdapter=new PlaceAutocompleteAdapter(this,mGoogleApiClient,BOUNDS_INDIA,null);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onPlaceClick(PlaceAutocomplete placeAutocomplete){
        mPresenter.handlePlaceClick(placeAutocomplete,mGoogleApiClient);
    }

    @Override
    public void updateMap(Place place) {
        if(mMap != null){
            mMap.clear();
            LatLng latLng = place.getLatLng();
            Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(place.getName().toString()));
            marker.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
            KeyboardHelper.hideKeyboard(MapActivity.this);
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_map;
    }

    @Override
    protected Class getFragmentToAdd() {
        return null;
    }

    @Override
    protected int getFragmentLayout() {
        return R.id.search_layout;
    }


    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @OnClick(R.id.clear)
    public void onClick(View view){
        mSearchEdittext.setText("");
        if(mAdapter!=null)
            mAdapter.clearList();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mPresenter.onTextChanged(s,count);
    }

    @Override
    public void setAdapter(){
        if(mAdapter!=null){
            mRecyclerView.setAdapter(mAdapter);
            showRecyclerView();
        }
    }

    @Override
    public void filter(String s){
        if(mGoogleApiClient.isConnected())
            mAdapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void showClearButton() {
        mClear.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideClearButton() {
        mClear.setVisibility(View.GONE);
    }

    @Override
    public void showRecyclerView() {
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRecyclerView() {
        mRecyclerView.setVisibility(View.GONE);
    }
}


