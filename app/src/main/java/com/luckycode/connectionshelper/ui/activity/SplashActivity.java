package com.luckycode.connectionshelper.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.luckycode.connectionshelper.R;
import com.luckycode.connectionshelper.common.LuckyActivity;
import com.luckycode.connectionshelper.model.Edge;
import com.luckycode.connectionshelper.model.Graph;
import com.luckycode.connectionshelper.model.TownVertex;
import com.luckycode.connectionshelper.presenter.SplashPresenter;
import com.luckycode.connectionshelper.ui.viewModel.SplashView;

import java.io.Serializable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends LuckyActivity implements SplashView{
    private SplashPresenter mPresenter;
    private List<TownVertex> vertexes;
    private List<Edge> edges;

    @Override
    protected void init() {
        mPresenter=new SplashPresenter(this,this,getHelper());
        mPresenter.loadData();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected Class getFragmentToAdd() {
        return null;
    }

    @Override
    protected int getFragmentLayout() {
        return R.id.main_container;
    }

    @Override
    public void onGraphReady(Graph graph) {
        Bundle bundle=new Bundle();
        bundle.putSerializable("GRAPH",graph);
        goToMapActivity(bundle);
    }

    private void goToMapActivity(Bundle args){
        Intent intent=new Intent(this,MapActivity.class);
        intent.putExtras(args);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        finish();
    }
}
