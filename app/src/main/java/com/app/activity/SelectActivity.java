package com.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.app.zxing.R;


public class SelectActivity extends Activity implements View.OnClickListener {

    private Button shebei,tanjing,wangguan,peidianxiang,cheku,hongwai,kongtiao;
    private void initView(){
        shebei = (Button) findViewById(R.id.btn_shebei);
        tanjing = (Button) findViewById(R.id.btn_tanjing);
        wangguan = (Button) findViewById(R.id.btn_wangguan);
        peidianxiang = (Button) findViewById(R.id.btn_peidianxiang);
        cheku = (Button) findViewById(R.id.btn_cheku);
        hongwai = (Button) findViewById(R.id.btn_hongwai);
        kongtiao = (Button) findViewById(R.id.btn_kongtiao);

        shebei.setOnClickListener(this);
        tanjing.setOnClickListener(this);
        wangguan.setOnClickListener(this);
        peidianxiang.setOnClickListener(this);
        cheku.setOnClickListener(this);
        hongwai.setOnClickListener(this);
        kongtiao.setOnClickListener(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        initView();
    }

    @Override
    public void onClick(View v) {
        Intent in = new Intent();
        switch (v.getId()){
            case R.id.btn_shebei:
                in.setClass(SelectActivity.this,SheBeiActivity.class);
                break;
            case R.id.btn_tanjing:
                in.setClass(SelectActivity.this,TanJingActivity.class);
                break;
            case R.id.btn_wangguan:
                in.setClass(SelectActivity.this,WangGuanActivity.class);
                break;
            case R.id.btn_peidianxiang:
                in.setClass(SelectActivity.this,PeiDianXiangActivity.class);
                break;
            case R.id.btn_cheku:
                in.setClass(SelectActivity.this,CheKuActivity.class);
                break;
            case R.id.btn_hongwai:
                in.setClass(SelectActivity.this,ContentActivity.class);
                break;
            case R.id.btn_kongtiao:
                in.setClass(SelectActivity.this,KongTiaoActivity.class);
                break;
        }
        startActivity(in);
        overridePendingTransition(R.anim.fade, R.anim.hold);
    }
}
