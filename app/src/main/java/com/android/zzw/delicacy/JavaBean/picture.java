package com.android.zzw.delicacy.JavaBean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by zzw on 17/3/6.
 */

public class picture extends BmobObject {
    private String location;
    private String picName;
    private BmobFile picSrc;
    private String introduction;
    private boolean favourite;

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getIntroduction() {
        return introduction;
    }


    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public void setLoaction(String loaction) {
        this.location = loaction;
    }

    public void setPicSrc(BmobFile picSrc) {
        this.picSrc = picSrc;
    }

    public BmobFile getPicSrc() {
        return picSrc;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public String getLoaction() {
        return location;
    }
}
