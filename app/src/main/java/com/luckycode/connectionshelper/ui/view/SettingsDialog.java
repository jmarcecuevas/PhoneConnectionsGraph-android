package com.luckycode.connectionshelper.ui.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.luckycode.connectionshelper.R;

import java.util.zip.Inflater;

/**
 * Created by marcelocuevas on 10/16/17.
 */

public class SettingsDialog extends AlertDialog.Builder {
    private LayoutInflater inflater;
    private View view;
    private EditText normalCostET,extraDiffET,
                    extraLargeDistanceET;

    public SettingsDialog(Context context,int viewToInflate) {
        super(context);
        inflater=((Activity)context).getLayoutInflater();
        view=inflater.inflate(viewToInflate,null);
        normalCostET=view.findViewById(R.id.normalCost);
        extraDiffET=view.findViewById(R.id.extraDifferentCountries);
        extraLargeDistanceET=view.findViewById(R.id.extraLargeDistance);
        setCancelable(false);
        setView(view);
    }

    public void setNormalCostToView(String normalCost){
        normalCostET.setText(normalCost);
    }

    public void setExtraDiffCostToView(String extraDiff){
        extraDiffET.setText(extraDiff);
    }

    public void setExtraLargeDistanceCostToView(String extraLargeDistance){
        extraLargeDistanceET.setText(extraLargeDistance);
    }

    public void setOkButton(final Listener listener){
        setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(normalCostET.getText().toString().isEmpty() ||
                            extraDiffET.getText().toString().isEmpty() ||
                            extraLargeDistanceET.getText().toString().isEmpty())
                    listener.onEmptyFieldsInSettingsDialog();
                else
                    listener.onOKButtonViewClicked(normalCostET.getText().toString(),
                            extraDiffET.getText().toString(),extraLargeDistanceET.getText()
                            .toString());
            }
        });
    }

    public interface Listener{
        void onOKButtonViewClicked(String normalCost,String extraDiff,String extraLargeDistance);
        void onEmptyFieldsInSettingsDialog();
    }
}
