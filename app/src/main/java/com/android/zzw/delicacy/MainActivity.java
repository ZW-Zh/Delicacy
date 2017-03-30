package com.android.zzw.delicacy;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zzw.delicacy.Adapter.FoodAdapter;
import com.android.zzw.delicacy.JavaBean.Food;
import com.android.zzw.delicacy.JavaBean.picture;
import com.android.zzw.delicacy.Utils.GetDiskCacheSizeTask;
import com.android.zzw.delicacy.Utils.GlideCacheUtil;
import com.android.zzw.delicacy.View.LocationPickerDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.github.jorgecastilloprz.completefab.CompleteFABView;
import com.github.jorgecastilloprz.listeners.FABProgressListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {
    FABProgressCircle fabProgressCircle;
    private TextView cache;
    private String location;
    private ActionBarDrawerToggle mDrawerToggle;
    TextView foodtitle;
    FloatingActionButton fab;
    FoodAdapter adapter;
    Toolbar toolbar;
    private List<Food> foodList = new ArrayList<>();
    DrawerLayout mDrawerLayout;
    public static SparseArray<Bitmap> sPhotoCache = new SparseArray<Bitmap>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "4fb8597681c01d4eb298e9d4f01b4742");
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        foodtitle= (TextView) findViewById(R.id.foodtitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开关/闭监听
        setFloatingButton();
        setDrawerlayout();
        setRecyclerview();
    }

    private void iniFood(String location) {
        showFloatingButtonCircle();
        //foodList.clear();
        adapter.notifyDataSetChanged();
        BmobQuery<picture> query = new BmobQuery<>();
        location=location.substring(0,location.length()-1);
        query.addWhereEqualTo("location", location);
        query.setLimit(50);
        query.findObjects(new FindListener<picture>() {
            @Override
            public void done(final List<picture> list, BmobException e) {
                if (e == null) {
                    for (picture food : list) {
                        Food a = new Food(food.getPicName(),food.getIntroduction(),food.isFavourite(),food.getPicSrc().getUrl(),food.getObjectId());
                        foodList.add(a);
                        adapter.notifyDataSetChanged();
                        fabProgressCircle.attachListener(new FABProgressListener() {
                            @Override
                            public void onFABProgressAnimationEnd() {
                                /*Toasty.custom(MainActivity.this, list.size() + "个结果", null, Color.parseColor("#ffffff"),
                                        Color.parseColor("#e16531"), Toast.LENGTH_SHORT, false, true).show();*/
                                list.clear();
                            }
                        });
                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }


    private void showDialog() {
        final LocationPickerDialog locationPickerDialog = new LocationPickerDialog(this);
        locationPickerDialog.setOnLocationSetListener(new LocationPickerDialog.OnLocationSetListener() {
            @Override
            public void OnLocationTimeSet(String choose) {
                if (choose == null) {
                    location = "中国北京 ";
                    locationPickerDialog.dismiss();
                    foodtitle.setText("中国北京");
                    iniFood(location);
                } else {
                    location = choose;
                    foodtitle.setText(choose);
                    locationPickerDialog.dismiss();
                    iniFood(location);
                }
            }
        });
        locationPickerDialog.show();
    }

    public void showFloatingButtonCircle() {
        fabProgressCircle.show();
        fabProgressCircle.beginFinalAnimation();
        for (int i = 0; i < fabProgressCircle.getChildCount(); i++) {
            if (fabProgressCircle.getChildAt(i) instanceof CompleteFABView) {
                fabProgressCircle.removeViewAt(i);
                break;
            }
        }
        fabProgressCircle.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                ImageView imgView = (ImageView) view.findViewById(R.id.completeFabIcon);
                if ((imgView != null) && (imgView.getScaleType() != ImageView.ScaleType.CENTER_INSIDE)) {
                    imgView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                }
            }
        });
    }

    public void setRecyclerview() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.foodrecyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new FoodAdapter(MainActivity.this, foodList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new FoodAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                Food food = foodList.get(postion);
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, DetailActivity.class);
                intent.putExtra("name", food.getName());
                intent.putExtra("favourite", food.isFavourite());
                intent.putExtra("detail", food.getIntroduction());
                intent.putExtra("photo", 0);
                intent.putExtra("id",food.getId());
                ImageView foodimg = (ImageView) view.findViewById(R.id.foodimg);
                sPhotoCache.put(0,
                        ((BitmapDrawable) foodimg.getDrawable()).getBitmap());
                System.out.println(((BitmapDrawable) foodimg.getDrawable()).getBitmap());

                ActivityOptions options =
                        ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, foodimg, "photo_hero");
                startActivityForResult(intent,postion+1,options.toBundle());
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    fab.hide();
                    fabProgressCircle.setVisibility(View.INVISIBLE);
                } else {
                    fabProgressCircle.setVisibility(View.VISIBLE);
                    fab.show();
                }
            }
        });

    }

    public void setDrawerlayout() {
        cache= (TextView) findViewById(R.id.cache);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                fab.hide();
                new GetDiskCacheSizeTask(cache).execute(new File(getCacheDir(), DiskCache.Factory.DEFAULT_DISK_CACHE_DIR));
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                fab.show();
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        RelativeLayout favourite = (RelativeLayout) findViewById(R.id.favourite);
        RelativeLayout cleancache = (RelativeLayout) findViewById(R.id.cleancache);
        RelativeLayout aboutus = (RelativeLayout) findViewById(R.id.aboutus);
        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodList.clear();
                mDrawerLayout.closeDrawers();
                foodtitle.setText("我的最爱");
                showFloatingButtonCircle();
                adapter.notifyDataSetChanged();
                BmobQuery<picture> query = new BmobQuery<>();
                query.addWhereEqualTo("favourite", true);
                query.setLimit(50);
                query.findObjects(new FindListener<picture>() {
                    @Override
                    public void done(final List<picture> list, BmobException e) {
                        if (e == null) {
                            for (picture food : list) {
                                Food a = new Food(food.getPicName(),food.getIntroduction(),food.isFavourite(),food.getPicSrc().getUrl(),food.getObjectId());
                                foodList.add(a);
                                adapter.notifyDataSetChanged();
                                fabProgressCircle.attachListener(new FABProgressListener() {
                                    @Override
                                    public void onFABProgressAnimationEnd() {
                                        list.clear();
                                    }
                                });
                            }
                        } else {
                            Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                        }
                    }
                });
            }
        });
        cleancache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlideCacheUtil.getInstance().clearImageAllCache(MainActivity.this);
                new GetDiskCacheSizeTask(cache).execute(new File(getCacheDir(), DiskCache.Factory.DEFAULT_DISK_CACHE_DIR));
                new MyThread().start();
            }
        });
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    public void setFloatingButton(){
        fabProgressCircle = (FABProgressCircle) findViewById(R.id.fabprogress);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                showDialog();
                foodList.clear();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    class MyThread extends Thread
    {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    try {
                        //延迟两秒更新
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    cache.setText("0.00 B");
                    Toasty.success(MainActivity.this,"清理完成",Toast.LENGTH_SHORT,true).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==0) {
            /*foodList.clear();
            iniFood(location);
            fabProgressCircle.onCompleteFABAnimationEnd();
            adapter.notifyDataSetChanged();*/
            foodList.get(requestCode-1).setFavourite(!foodList.get(requestCode-1).isFavourite());
            adapter.notifyDataSetChanged();
        }

    }
}

