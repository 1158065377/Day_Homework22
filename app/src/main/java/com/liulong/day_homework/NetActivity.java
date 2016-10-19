package com.liulong.day_homework;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import tool.MySp;

/**
 * Created by Administrator on 2016/10/15.
 */

public class NetActivity extends Activity implements View.OnClickListener {

    private Button ok,no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.netactivity);
        ok = (Button) findViewById(R.id.ok);
        no = (Button) findViewById(R.id.no);
        ok.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ok:
                MySp.newInstance(this).putSp("1");
                Intent i = new Intent();
                i.setAction(Settings.ACTION_WIFI_SETTINGS);
                startActivity(i);
                break;
            case R.id.no:
                finish();
                MySp.newInstance(this).putSp("0");
                break;
        }
    }
}
