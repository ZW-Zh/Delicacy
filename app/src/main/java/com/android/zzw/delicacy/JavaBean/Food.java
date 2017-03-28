package com.android.zzw.delicacy.JavaBean;

/**
 * Created by zzw on 17/2/25.
 */

public class Food {
    private String name;
    private String introduction;
    private boolean favourite;
    private String url;
    private String id;

    public Food(String name,String introduction,boolean favourite,String url,String id){
        this.name=name;
        this.introduction=introduction;
        this.favourite=favourite;
        this.url=url;
        this.id=id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
