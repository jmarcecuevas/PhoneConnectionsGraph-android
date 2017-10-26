package com.luckycode.connectionshelper.ui.activity;

import android.app.Dialog;
import android.os.Bundle;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.model.PolylineOptions;
import com.luckycode.connectionshelper.interactor.MapInteractor;
import com.luckycode.connectionshelper.model.Edge;
import com.luckycode.connectionshelper.model.Graph;
import com.luckycode.connectionshelper.model.TownVertex;
import com.luckycode.connectionshelper.ui.adapter.PlaceAutocompleteAdapter.PlaceAutocomplete;
import com.google.android.gms.location.places.Places;
import com.luckycode.connectionshelper.R;
import com.luckycode.connectionshelper.common.LuckyActivity;
import com.luckycode.connectionshelper.presenter.MapPresenter;
import com.luckycode.connectionshelper.ui.adapter.PlaceAutocompleteAdapter;
import com.luckycode.connectionshelper.ui.view.PopulationDialog;
import com.luckycode.connectionshelper.ui.view.SettingsDialog;
import com.luckycode.connectionshelper.ui.viewModel.MapActivityView;
import com.luckycode.connectionshelper.utils.KeyboardHelper;

import butterknife.BindView;
import butterknife.OnClick;

public class MapActivity extends LuckyActivity implements PlaceAutocompleteAdapter.PlaceAutoCompleteInterface, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,OnMapReadyCallback, TextWatcher,MapActivityView,RadioGroup.OnCheckedChangeListener,
    SettingsDialog.Listener,PopulationDialog.Listener{

    @BindView(R.id.list_search)RecyclerView mRecyclerView;
    @BindView(R.id.search_et)AutoCompleteTextView mSearchEdittext;
    @BindView(R.id.clear)ImageView mClear;
    @BindView(R.id.menu_more)ImageButton menuMore;
    @BindView(R.id.progressBar)ProgressBar progressBar;
    @BindView(R.id.viewOptions)RadioGroup viewOptions;
    @BindView(R.id.graphRB)RadioButton graphRB;
    @BindView(R.id.solutionRB)RadioButton solutionRB;
    @BindView(R.id.vertexesRB)RadioButton vertexesRB;
    private Graph graph;
    private MapPresenter mPresenter;
    private MapInteractor mInteractor;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;
    private PlaceAutocomplete mPlaceAutocomplete;

    @Override
    protected void init() {
        Bundle bundle= getIntent().getExtras();
        graph= (Graph) bundle.getSerializable("GRAPH");
        printGraphInfo(graph);
        initMap();
        initGoogleClient();
        setRecyclerView();
        initListeners();
        mInteractor=new MapInteractor(this,getHelper());
        mPresenter=new MapPresenter(this,graph,mInteractor);
    }

    public void printGraphInfo(Graph graph){
        for(Edge edge:graph.getEdges()){
            Log.e("/////////////////","//////////");
            Log.e("ORIGIN",edge.getOrigin().getName());
            Log.e("DESTINATION",edge.getDestination().getName());
            Log.e("DISTANCE",String.valueOf(edge.getDistanceInKms()));
            Log.e("SAME COUNTRY",String.valueOf(edge.vertexesHaveSameCountry()));
            Log.e("MAYOR A 200",String.valueOf(edge.distanceIsGreaterThan200()));
            Log.e("TOTAL",String.valueOf(edge.getWeight()));
            Log.e("////////////////////","//////////////");
            Log.e("///////////////////","////////////////");
        }
    }

    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initGoogleClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 , this)
                .addApi(Places.GEO_DATA_API)
                .build();
    }

    private void initListeners(){
        mSearchEdittext.addTextChangedListener(this);
        viewOptions.setOnCheckedChangeListener(this);
    }

    public void showPopulationDialog(){
        PopulationDialog dialog= new PopulationDialog(this,R.layout.population_dialog);
        dialog.setOkButton(this);
        dialog.show();
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

    public void setRecyclerView(){
        LinearLayoutManager linearlm=new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(linearlm);
        mAdapter=new PlaceAutocompleteAdapter(this,mGoogleApiClient,null);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onPlaceClick(PlaceAutocomplete placeAutocomplete){
        mPlaceAutocomplete=placeAutocomplete;
        showPopulationDialog();
    }

    @Override
    public void updateMap(TownVertex town) {
        LatLng latLng = new LatLng(town.getLat(),town.getLng());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 6));
        if(graphRB.isChecked()){
            mPresenter.getMapInfoToDraw(graph,true);
        }else if(solutionRB.isChecked())
            mPresenter.calculateSolution(graph);
        else{
            mPresenter.getMapInfoToDraw(graph,false);
        }
        KeyboardHelper.hideKeyboard(MapActivity.this);
    }

    @Override
    public void drawRoute(LatLng from, LatLng to,int color) {
        PolylineOptions rectOptions = new PolylineOptions()
                .add(from)
                .add(to)
                .color(color);
        mMap.addPolyline(rectOptions);
    }

    @Override
    public void drawMarker(LatLng position, String title) {
        Marker marker=mMap.addMarker(new MarkerOptions().position(position).
                title(title));
        marker.showInfoWindow();
    }

    @Override
    public void showRecalculatedGraph() {
        if(solutionRB.isChecked()){
            mMap.clear();
            mPresenter.calculateSolution(graph);
        }
    }

    @Override
    public void showTotalCost(String cost) {
        Snackbar.make(mClear, "El costo de la instalación es: $ "+ cost, Snackbar.LENGTH_LONG)
                .setAction("SETTINGS", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showSettingsDialog();
                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                .show();
    }

    public void showSettingsDialog(){
        mPresenter.getConnectionCosts();
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
        LatLng initPosition = new LatLng(-34.5430, -58.71185);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initPosition,6));
        mPresenter.getMapInfoToDraw(graph,false);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mPresenter.onTextChanged(s);
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


    private void handleGraphOptionChecked(){
        mPresenter.getMapInfoToDraw(graph,true);
    }

    private void handleSolutionOptionChecked(){
        mPresenter.calculateSolution(graph);
    }

    private void handleVertexesOptionChecked(){
        mPresenter.getMapInfoToDraw(graph,false);
    }

    @Override
    public void onOKButtonViewClicked(String normalCost, String extraDiff, String extraLargeDistance) {
        mPresenter.recalculateWeights(graph,normalCost,extraDiff,extraLargeDistance);
        mPresenter.changeConnectionsCost(normalCost,extraDiff,extraLargeDistance);
        Toast.makeText(this,"Cambios guardados",Toast.LENGTH_SHORT).show();
        printGraphInfo(graph);
    }

    @Override
    public void onEmptyFieldsInSettingsDialog() {
        showSettingsDialog();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int id) {
        mMap.clear();
        if(id==R.id.graphRB)
            handleGraphOptionChecked();
        else if(id==R.id.solutionRB)
            handleSolutionOptionChecked();
        else if(id==R.id.vertexesRB)
            handleVertexesOptionChecked();
    }

    @Override
    public void showSettingsDialog(String normalCost, String extraDiff, String extraLargeDistance) {
        SettingsDialog dialog= new SettingsDialog(this,R.layout.dialog_settings_custom);
        dialog.setNormalCostToView(normalCost);
        dialog.setExtraDiffCostToView(extraDiff);
        dialog.setExtraLargeDistanceCostToView(extraLargeDistance);
        dialog.setOkButton(this);
        dialog.show();
    }

    @OnClick(R.id.menu_more)
    public void onMenuMoreClicked(View view){
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_settings:
                        showSettingsDialog();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    @Override
    public void onPopulationDialogClick(int population) {
        mPresenter.handlePlaceClick(mPlaceAutocomplete,mGoogleApiClient,population);
    }

    @Override
    public void onEmptyPopulation() {
        showPopulationDialog();
    }
}


