package com.liulong.day_homework;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telecom.ConnectionService;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import path.Path;
import reciver.MyReciver;
import tool.MySp;

public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private RadioGroup rg;
    private View line;
    private ViewPager vp;
    private int type[] = {1, 2, 2, 2};
    private ArrayList<Fragment> fArrayList = new ArrayList<>();
    private String pathlift[] = {Path.PATH1, Path.PATH2, Path.PATH3, Path.PATH4};
    private String pathRight[] = {Path.PATH1_1, Path.PATH2_2, Path.PATH3_3, Path.PATH4_4};
    private String tableName[] = {"h_01", "h_02", "h_03", "h_04"};
    private TextView btn_01, btn_02, btn_03, btn_04;
    private DrawerLayout dl;
    private Toolbar tl;
    private TextView gengduo;
    private   int flag=0;
    private PopupWindow window;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter inf=new IntentFilter();
        inf.addAction("aaaaa");
        MyReciver reciver = new MyReciver();
        registerReceiver(reciver,inf);
        MySp.newInstance(this).putSp("0");
        intent = new Intent();
        intent.setAction("aaaaa");
        sendBroadcast(intent);
        rg = (RadioGroup) findViewById(R.id.rg);
        line = findViewById(R.id.line);
        vp = (ViewPager) findViewById(R.id.vp);
        btn_01 = (TextView) findViewById(R.id.myfragment_shouye);
        btn_02 = (TextView) findViewById(R.id.myfragment_erye);
        btn_03 = (TextView) findViewById(R.id.myfragment_sanye);
        btn_04 = (TextView) findViewById(R.id.myfragment_siye);
        dl = (DrawerLayout) findViewById(R.id.dl);
        tl = (Toolbar) findViewById(R.id.toolbar);
        gengduo = (TextView) findViewById(R.id.gengduo);
        tl.setTitle("新闻鉴赏");
        tl.setTitleTextColor(Color.WHITE);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dl, tl, R.string.open, R.string.close);
        toggle.syncState();
        dl.setDrawerListener(toggle);
        int w = getResources().getDisplayMetrics().widthPixels;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) line.getLayoutParams();
        params.width = w / 4;
        line.setLayoutParams(params);
        for (int i = 0; i < pathlift.length; i++) {
            MyFragment myFragment = MyFragment.newInstance(pathlift[i], pathRight[i], type[i], tableName[i]);
            fArrayList.add(myFragment);
        }
        View view = getLayoutInflater().inflate(R.layout.popu, null);
        window = new PopupWindow(view, 200, 300);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);
        vp.setOnPageChangeListener(this);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.btn_01:
                        vp.setCurrentItem(0, true);
                        break;
                    case R.id.btn_02:
                        vp.setCurrentItem(1, true);
                        break;
                    case R.id.btn_03:
                        vp.setCurrentItem(2, true);
                        break;
                    case R.id.btn_04:
                        vp.setCurrentItem(3, true);
                        break;
                }
            }
        });
        btn_01.setOnClickListener(this);
        btn_02.setOnClickListener(this);
        btn_03.setOnClickListener(this);
        btn_04.setOnClickListener(this);

        gengduo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==0){
                    flag=1;
                    window.showAsDropDown(gengduo);
                }else if(flag==1){
                    window.dismiss();
                    flag=0;
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        ConnectivityManager o = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = o.getActiveNetworkInfo();
        if(info!=null&&info.isConnected()){
            int type = info.getType();
            if(type==ConnectivityManager.TYPE_MOBILE){
                MySp.newInstance(this).putSp("0");
            }else if(type==ConnectivityManager.TYPE_WIFI){
                MySp.newInstance(this).putSp("1");
            }
        }
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        int width = widthPixels / 4;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) line.getLayoutParams();
        params.leftMargin = (int) (width * (position + positionOffset));
        line.setLayoutParams(params);
    }
    @Override
    public void onPageSelected(int position) {

    }
    @Override
    public void onPageScrollStateChanged(int state) {
    }
    @Override
    public void onClick(View v) {
        if (v == btn_01) {
            vp.setCurrentItem(0);
        }
        if (v == btn_02) {
            vp.setCurrentItem(1);
        }
        if (v == btn_03) {
            vp.setCurrentItem(2);
        }
        if (v == btn_04) {
            vp.setCurrentItem(3);
        }
        dl.closeDrawer(Gravity.LEFT);
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fArrayList.get(position);
        }

        @Override
        public int getCount() {
            return fArrayList.size();
        }
    }
    private boolean isback=true;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&isback){
            if(isback){
                isback=false;
                Toast.makeText(MainActivity.this,"再次点击退出",Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        isback=true;
                    }
                }).start();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
