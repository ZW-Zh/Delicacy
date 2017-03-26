package com.android.zzw.delicacy.View;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

import com.android.zzw.delicacy.R;

import java.lang.reflect.Field;

/**
 * Created by zzw on 17/3/5.
 */

public class LocationPicker extends FrameLayout {
    private final NumberPicker countryPicker;
    private final NumberPicker cityPicker;
    private String nation="中国";
    private String city;

    private onLocationChangedListener mOnLocationChangedListener;
    String[] country= {"中国","法国","日本","西班牙","希腊","俄罗斯","意大利","韩国"};
    int count[]={6,1,1,1,1,1,1,1};
    String[] chinaprovince = {"北京","上海","广州","四川","江苏","天津"};
    String[] France={" "},Spanish={" "},Greek={" "},Russia={" "},Italy={" "},Koera={" "},Japan={" "};
    String[] args=chinaprovince;
    public LocationPicker(Context context) {
        super(context);

        inflate(context, R.layout.locationpicker,this);
        countryPicker= (NumberPicker) this.findViewById(R.id.country);
        countryPicker.setMinValue(0);
        countryPicker.setMaxValue(country.length - 1);
        countryPicker.setDisplayedValues(country);
        countryPicker.setOnValueChangedListener(mProvinceChangedListener);

        cityPicker= (NumberPicker) this.findViewById(R.id.city);

        cityPicker.setMinValue(0);
        cityPicker.setMaxValue(chinaprovince.length-1);
        cityPicker.setDisplayedValues(chinaprovince);
        cityPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        cityPicker.setOnValueChangedListener(mcityChangedListener);

        setNumberPickerDividerColor(cityPicker);
        setNumberPickerDividerColor(countryPicker);
    }
    private NumberPicker.OnValueChangeListener mProvinceChangedListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            switch (picker.getValue()){

                case 0:if(count[oldVal]>=count[0]){
                            cityPicker.setMaxValue(chinaprovince.length-1);
                            cityPicker.setDisplayedValues(chinaprovince);
                        }else {
                            cityPicker.setDisplayedValues(chinaprovince);
                            cityPicker.setMaxValue(chinaprovince.length-1);
                        }
                    nation=country[countryPicker.getValue()];
                    args=cityPicker.getDisplayedValues();
                    city=args[0];
                    break;
                case 1:if(count[oldVal]>=count[1]){
                            cityPicker.setMinValue(0);
                            cityPicker.setMaxValue(0);
                            cityPicker.setDisplayedValues(France);
                        }else {
                            cityPicker.setDisplayedValues(France);
                            cityPicker.setMaxValue(France.length-1);
                        }
                    nation=country[countryPicker.getValue()];
                    args=cityPicker.getDisplayedValues();
                    city=args[0];
                    break;
                case 2:if(count[oldVal]>=count[2]){
                            cityPicker.setMaxValue(Japan.length-1);
                            cityPicker.setDisplayedValues(Japan);
                        }else {
                            cityPicker.setDisplayedValues(Japan);
                            cityPicker.setMaxValue(Japan.length-1);
                        }
                    nation=country[countryPicker.getValue()];
                    args=cityPicker.getDisplayedValues();
                    city=args[0];
                    break;
                case 3:if(count[oldVal]>=count[3]){
                    cityPicker.setMaxValue(Spanish.length-1);
                    cityPicker.setDisplayedValues(Spanish);
                }else {
                    cityPicker.setDisplayedValues(Spanish);
                    cityPicker.setMaxValue(Spanish.length-1);
                }
                    nation=country[countryPicker.getValue()];
                    args=cityPicker.getDisplayedValues();
                    city=args[0];
                    break;
                case 4:if(count[oldVal]>=count[4]){
                    cityPicker.setMaxValue(Greek.length-1);
                    cityPicker.setDisplayedValues(Greek);
                }else {
                    cityPicker.setDisplayedValues(Greek);
                    cityPicker.setMaxValue(Greek.length-1);
                }
                    nation=country[countryPicker.getValue()];
                    args=cityPicker.getDisplayedValues();
                    city=args[0];
                    break;
                case 5:if(count[oldVal]>=count[5]){
                    cityPicker.setMaxValue(Russia.length-1);
                    cityPicker.setDisplayedValues(Russia);
                }else {
                    cityPicker.setDisplayedValues(Russia);
                    cityPicker.setMaxValue(Russia.length-1);
                }
                    nation=country[countryPicker.getValue()];
                    args=cityPicker.getDisplayedValues();
                    city=args[0];
                    break;
                case 6:if(count[oldVal]>=count[6]){
                    cityPicker.setMaxValue(Italy.length-1);
                    cityPicker.setDisplayedValues(Italy);
                }else {
                    cityPicker.setDisplayedValues(Italy);
                    cityPicker.setMaxValue(Italy.length-1);
                }
                    nation=country[countryPicker.getValue()];
                    args=cityPicker.getDisplayedValues();
                    city=args[0];
                    break;
                case 7:if(count[oldVal]>=count[7]){
                    cityPicker.setMaxValue(Koera.length-1);
                    cityPicker.setDisplayedValues(Koera);
                }else {
                    cityPicker.setDisplayedValues(Koera);
                    cityPicker.setMaxValue(Koera.length-1);
                }
                    nation=country[countryPicker.getValue()];
                    args=cityPicker.getDisplayedValues();
                    city=args[0];
                    break;
                default: break;
            }
            onLocationChanged();
        }
    };
    private NumberPicker.OnValueChangeListener mcityChangedListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            city=args[cityPicker.getValue()];
            onLocationChanged();
        }
    };

    public interface onLocationChangedListener{
        void onLocationChanged(LocationPicker view,String country,String city);
    }
    public void setOnLocationChangedListener(onLocationChangedListener callback){
        mOnLocationChangedListener=callback;
    }
    private void onLocationChanged(){
        if(mOnLocationChangedListener!=null){
            mOnLocationChangedListener.onLocationChanged(this,nation,city);
        }
    }
    /**
     * 设置分割线的颜色值
     * @param numberPicker
     */
    @SuppressWarnings("unused")
    public void setNumberPickerDividerColor(NumberPicker numberPicker) {
        NumberPicker picker = numberPicker;
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    pf.set(picker, new ColorDrawable(Color.parseColor("#ea986c")));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
