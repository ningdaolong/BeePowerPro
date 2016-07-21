package com.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.data.City;
import com.app.data.Content;
import com.app.zxing.R;
import com.google.gson.Gson;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.util.HashMap;
import java.util.Map;

public class ContentActivity extends Activity implements View.OnClickListener {
    //网络加载
    RequestQueue mQueue;
    StringRequest request;
    //网络加载弹窗
    private ProgressDialog progressDialog = null;

    private Bundle budle;

    private ImageView back;
    private Button xuanqu;
    private Button ok;
    private EditText H_Name;
    private EditText K_Name;

    private TextView a,b,c,d,e,f;

    private PercentLinearLayout xinxi;

    private void initView(){
        back = ((ImageView) findViewById(R.id.content_img_back));
        xuanqu = ((Button) findViewById(R.id.content_btn_xuanqu));
        ok = ((Button) findViewById(R.id.content_btn_ok));
        H_Name = ((EditText) findViewById(R.id.et_HongNAME));
        K_Name = ((EditText) findViewById(R.id.et_KongNAME));

        a = (TextView) findViewById(R.id.content_tv_a);
        b = (TextView) findViewById(R.id.content_tv_b);
        c = (TextView) findViewById(R.id.content_tv_c);
        d = (TextView) findViewById(R.id.content_tv_d);
        e = (TextView) findViewById(R.id.content_tv_e);
        f = (TextView) findViewById(R.id.content_tv_f);
        xinxi = (PercentLinearLayout) findViewById(R.id.content_pll_xinxi);

        back.setOnClickListener(this);
        xuanqu.setOnClickListener(this);
        ok.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        initView();
        //创建volley请求队列
        mQueue = Volley.newRequestQueue(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.content_img_back:
                finish();
                break;
            case R.id.content_btn_xuanqu:
                Intent in = new Intent(this,HongWaiActivit.class);
                startActivityForResult(in,1);
                break;
            case R.id.content_btn_ok:
                if (H_Name.getText().toString().equals("") || K_Name.getText().toString().equals("")) {
                    Toast.makeText(ContentActivity.this,"型号或名称不能为空?",Toast.LENGTH_SHORT).show();
                } else {
                    if (budle == null){
                        Toast.makeText(ContentActivity.this,"设备信息,空?",Toast.LENGTH_SHORT).show();
                    }else {
                        //提示连接中
                        progressDialog = ProgressDialog.show(this, "",
                                "正在加载 . . .", true);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Content.SJSC, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                City ew = new Gson().fromJson(s, City.class);
                                if (ew.getStatus().equals("success")) {
                                    Toast.makeText(ContentActivity.this, ew.getMessage() + "", Toast.LENGTH_SHORT).show();
                                    H_Name.setText("");
                                    K_Name.setText("");
                                    progressDialog.dismiss();
                                } else {
                                    Toast.makeText(ContentActivity.this, ew.getMessage() + "", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                progressDialog.dismiss();
                                Toast.makeText(ContentActivity.this,"失败? = "+volleyError.toString(),Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> map = new HashMap<String, String>();
//                            map.put("mac", "Sck-16-"+et_MAC.getText().toString()+"2707004b1200");
                                map.put("mac", budle.getString("hMac")); //红外Mac
                                map.put("socketMac", budle.getString("cMac")); //插座Mac
                                map.put("city", budle.getString("city"));
                                map.put("floor", budle.getString("floor"));
                                map.put("name", H_Name.getText().toString());
                                map.put("acBrand", K_Name.getText().toString());
                                map.put("room", budle.getString("room"));
                                map.put("hh", budle.getString("hh"));
                                map.put("longitude", budle.getString("longitude"));
                                map.put("latitude", budle.getString("latitude"));
                                return map;
                            }
                        };
                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
                        mQueue.add(stringRequest);
                    }
                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            budle = data.getExtras();
            //显示扫描到的内容
           if (budle != null){
               Log.e("qianguan","bundle添加信息");

               a.setText("定位: " + budle.getString("latitude") +" / "+ budle.getString("longitude"));
               b.setText("楼层: "+budle.getString("floor_name"));
               c.setText("房间: "+budle.getString("room_name"));
               d.setText("用户: "+budle.getString("hh_name"));
               e.setText(budle.getString("hMac"));
               f.setText(budle.getString("cMac"));

               xinxi.setVisibility(View.VISIBLE);

           }else {
               Toast.makeText(ContentActivity.this,"返回设备信息失败失败?",Toast.LENGTH_SHORT).show();
           }
        }
    }
}
