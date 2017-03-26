package com.android.zzw.delicacy.View;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.android.zzw.delicacy.R;
import com.android.zzw.delicacy.View.LocationPicker;

/**
 * Created by zzw on 17/3/5.
 */

public class LocationPickerDialog extends AlertDialog{
    private LocationPicker locationPicker;
    private OnLocationSetListener mOnLocationSetListener;
    private String choose;
    Button sure;
    public LocationPickerDialog(Context context) {
        super(context);
        locationPicker=new LocationPicker(context);
        setView(locationPicker);
        locationPicker.setOnLocationChangedListener(new LocationPicker.onLocationChangedListener() {
            @Override
            public void onLocationChanged(LocationPicker view, String country, String city) {
                    choose = country + city;
            }
        });
        sure= (Button) locationPicker.findViewById(R.id.sure);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnLocationSetListener != null) {
                    mOnLocationSetListener.OnLocationTimeSet(choose);
                }
            }
        });
    }


    public interface OnLocationSetListener {
        void OnLocationTimeSet(String choose);
    }
    public void setOnLocationSetListener(OnLocationSetListener callBack) {
        mOnLocationSetListener = callBack;
    }
}
