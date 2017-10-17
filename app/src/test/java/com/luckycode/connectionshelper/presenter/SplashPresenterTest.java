package com.luckycode.connectionshelper.presenter;

import android.content.Context;

import com.luckycode.connectionshelper.interactor.MainInteractor;
import com.luckycode.connectionshelper.ui.viewModel.SplashView;
import com.luckycode.connectionshelper.utils.DatabaseHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

/**
 * Created by marcelocuevas on 10/16/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class SplashPresenterTest {
    @Mock
    private Context context;
    @Mock
    private SplashView view;
    @Mock
    private DatabaseHelper dbHelper;
    @Mock
    private MainInteractor interactor;
    private SplashPresenter presenter;

//    @Before
//    public void setUp() throws Exception {
//        presenter=new SplashPresenter(view,context,dbHelper);
//        presenter.toString();
//
//    }
//
//    @Test
//    public void loadData() throws Exception {
//        assertEquals(4,4);
//    }

//    @Test
//    public void onLocalJSONStored() throws Exception {
//
//    }
//
//    @Test
//    public void onDataLoaded() throws Exception {
//
//    }
//
//    @Test
//    public void onEdgeAdded() throws Exception {
//
//    }
//
//    @Test
//    public void onVertexAdded() throws Exception {
//
//    }
//
//    @Test
//    public void onEdgesWeightsRecalculated() throws Exception {
//
//    }

}