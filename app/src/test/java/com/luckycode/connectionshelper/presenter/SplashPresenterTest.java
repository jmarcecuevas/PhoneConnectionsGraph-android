package com.luckycode.connectionshelper.presenter;

import android.content.Context;

import com.luckycode.connectionshelper.interactor.MainInteractor;
import com.luckycode.connectionshelper.model.Edge;
import com.luckycode.connectionshelper.model.Graph;
import com.luckycode.connectionshelper.model.TownVertex;
import com.luckycode.connectionshelper.ui.viewModel.SplashView;
import com.luckycode.connectionshelper.utils.DatabaseHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by marcelocuevas on 10/16/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class SplashPresenterTest {
    private SplashPresenter presenter;
    private SplashView view;
    private MainInteractor interactor;
    private TownVertex mockVertex1,mockVertex2,mockVertex3,mockVertex4;

    @Before
    public void setUp() throws Exception {
        view= Mockito.mock(SplashView.class);
        interactor=Mockito.mock(MainInteractor.class);
        presenter=new SplashPresenter(view,interactor);

        mockVertex1=new TownVertex("4r4343","San Miguel","Argentina",324355,-34.55,-58.45);
        mockVertex2=new TownVertex("333df43","Río de Janeiro","Brasil",33443,-30.5555,-50.453);
        mockVertex3=new TownVertex("983nf","Calama","Chile",50022,-20.5,60.45);
        mockVertex4=new TownVertex("4ig43","La paz","Bolivia",484894,44.55,-51.4445);
    }

    @Test
    public void loadDataTest(){
        presenter.loadData();
        verify(interactor).loadDatabaseData();
    }

}