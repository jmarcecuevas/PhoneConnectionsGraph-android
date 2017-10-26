package com.luckycode.connectionshelper.presenter;

import android.graphics.Color;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.luckycode.connectionshelper.interactor.MapInteractor;
import com.luckycode.connectionshelper.model.Edge;
import com.luckycode.connectionshelper.model.Graph;
import com.luckycode.connectionshelper.model.TownVertex;
import com.luckycode.connectionshelper.ui.adapter.PlaceAutocompleteAdapter;
import com.luckycode.connectionshelper.ui.viewModel.MapActivityView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by marcelocuevas on 10/16/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class MapPresenterTest {
    private MapActivityView view;
    private MapPresenter presenter;
    private MapInteractor interactor;
    private Graph graph;
    private TownVertex mockVertex1,mockVertex2,
                       mockVertex3,mockVertex4;

    @Before
    public void setUp() throws Exception {
        view=Mockito.mock(MapActivityView.class);
        graph=Mockito.mock(Graph.class);
        interactor=Mockito.mock(MapInteractor.class);
        presenter=new MapPresenter(view,graph,interactor);

        mockVertex1=new TownVertex("4r4343","San Miguel","Argentina",324355,-34.55,-58.45);
        mockVertex2=new TownVertex("333df43","Río de Janeiro","Brasil",33443,-30.5555,-50.453);
        mockVertex3=new TownVertex("983nf","Calama","Chile",50022,-20.5,60.45);
        mockVertex4=new TownVertex("4ig43","La paz","Bolivia",484894,44.55,-51.4445);
    }

    @Test
    public void connectionCostsReadyTest(){
        presenter.onConnectionCostsReady(10.0,50.0,85.0);
        verify(view).showSettingsDialog("10.0","50.0","85.0");
    }

    @Test
    public void onEdgeAddedTest(){
        Edge edge=Mockito.mock(Edge.class);
        presenter.onEdgeAdded(edge);
        verify(interactor).storeEdgeInDB(edge);
    }

    @Test
    public void onEdgesWeightsRecalculatedTest() {
        Set<Edge> edges=Mockito.mock(Set.class);
        presenter.onEdgesWeightsRecalculated(edges);

        verify(interactor).clearEdgesTable();
        verify(interactor).storeEdgesInDatabase(edges);
        verify(view).showRecalculatedGraph();
    }

    @Test
    public void onVertexAddedTest() {
        TownVertex vertex=Mockito.mock(TownVertex.class);
        presenter.onVertexAdded(vertex);
        verify(interactor).storeVertexInDB(vertex);
    }

    @Test
    public void changeConnectionsCostTest() {
        /*  Un caso podria ser que como String este recibiendo letras en lugar de números, pero este caso
            no hace falta testear porque estos Strings vienen de un input del usuario al cual solo le brinda un teclado númerico */
        presenter.changeConnectionsCost("50","35","23");
        verify(interactor).storeConnectionsCost(50.0,35.0,23.0);
    }

    @Test
    public void recalculateWeightsTest() {
        presenter.recalculateWeights(graph,"23.0","42.0","23.0");
        verify(graph).recalculateWeights(23.0,42.0,23.0);
    }

    @Test
    public void handlePlaceClickTest(){
        PlaceAutocompleteAdapter.PlaceAutocomplete placeAutocomplete=Mockito.mock(PlaceAutocompleteAdapter.PlaceAutocomplete.class);
        GoogleApiClient googleApiClient=mock(GoogleApiClient.class);
        presenter.handlePlaceClick(placeAutocomplete,googleApiClient,22424);
        verify(interactor).getPlaceByID(placeAutocomplete,googleApiClient,22424);
    }

    @Test
    public void sendMarkersTest(){
        Set<TownVertex> vertexes=new HashSet<>();
        presenter.sendMarkers(vertexes);

        for(TownVertex vertex:vertexes) {
            verify(view).drawMarker(new LatLng(vertex.getLat(), vertex.getLng()), vertex.getName());
        }
    }

    @Test
    public void sendRoutesTest(){
        List<Edge> edges=new ArrayList<>();
        Edge firstEdge=Mockito.mock(Edge.class);
        Edge secondEdge=Mockito.mock(Edge.class);

        when(firstEdge.getOrigin()).thenReturn(mockVertex1);
        when(firstEdge.getDestination()).thenReturn(mockVertex2);

        when(secondEdge.getOrigin()).thenReturn(mockVertex3);
        when(secondEdge.getDestination()).thenReturn(mockVertex4);

        edges.add(firstEdge);
        edges.add(secondEdge);

        presenter.sendRoutes(edges);

        for(Edge edge:edges){
            LatLng lat= new LatLng(edge.getOrigin().getLat(),edge.getOrigin().getLng());
            LatLng lng= new LatLng(edge.getDestination().getLat(),edge.getDestination().getLng());
            verify(view).drawRoute(lat,lng,Color.BLUE);
        }
    }

    @Test
    public void onTextChangedTest(){
        CharSequence s="a";
        presenter.onTextChanged(s);
        verify(view).showClearButton();
        verify(view).setAdapter();
        verify(view).filter("a");

        s="";
        presenter.onTextChanged(s);
        verify(view).hideClearButton();
    }

    @Test
    public void onSuccessPlaceTest(){
        presenter.onSuccessPlace(mockVertex1);
        //Como los vértices estan representados en un Set, puede que el vértice se agregue o no.
        verify(graph).addVertex(mockVertex1);
        verify(graph).updateEdges(mockVertex1,interactor.getNormalCost(),
                interactor.getExtraDifferentCountries(),interactor.getExtraLargeDistance());
        verify(view).hideRecyclerView();
        verify(view).updateMap(mockVertex1);
    }
}