package com.luckycode.connectionshelper.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AlertDialogLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.luckycode.connectionshelper.R;

/**
 * Created by marcelocuevas on 10/16/17.
 */

public class PopulationDialog extends AlertDialog.Builder {
    private LayoutInflater inflater;
    private View view;
    private EditText population;

    public PopulationDialog(Context context,int viewToInflate) {
        super(context);
        inflater=((Activity)context).getLayoutInflater();
        view=inflater.inflate(viewToInflate,null);
        population=view.findViewById(R.id.population);
        setCancelable(false);
        setView(view);
    }

    public void setOkButton(final Listener listener){
        setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(population.getText().toString().isEmpty()){
                    listener.onEmptyPopulation();
                }else
                    listener.onPopulationDialogClick(Integer.valueOf(population.getText().toString()));
            }
        });
    }

    public interface Listener{
        void onPopulationDialogClick(int population);
        void onEmptyPopulation();
    }

}
