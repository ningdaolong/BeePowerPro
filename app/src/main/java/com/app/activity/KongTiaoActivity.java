package com.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.app.data.Utils;
import com.app.zxing.R;
import com.app.zxing.activity.MipcaActivityCapture;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KongTiaoActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private final static int SCANNIN_GREQUEST_CODE = 1;
    private Spinner spinnerA, spinnerB, spinnerC, spinnerD, spinnerE, spinnerF, spinner_Name;
    EditText et_MAC, et_NAME, et_BZ;
    Button scan, tijiao;
    ImageView back;
    TextView dingwei;

    //网络加载
    RequestQueue mQueue;
    StringRequest request_a;
    StringRequest request_b;
    StringRequest request_c;
    StringRequest request_d;
    StringRequest request_e;
    StringRequest request_f;

    //网络加载弹窗
    private ProgressDialog progressDialog = null;

    //显示的数组
    String province_ID;
    String[] province_arr;
    List<City.OrgListEntity> province_orgList;

    String city_ID;
    String[] city_arr;
    List<City.OrgListEntity> city_orgList;

    String[] name_arr;
    String custID;
    String custName = "";
    List<City.CustListEntity> county_custList;
    String county_ID;
    String[] county_arr;
    List<City.OrgListEntity> county_orgList;

    String company_ID;
    String[] company_arr;
    List<City.OrgListEntity> company_orgList;

    String floor_ID;
    String gateway = "";
    String[] floor_arr;
    List<City.OrgListEntity> floor_orgList;

    String room_ID;
    String[] room_arr;
    List<City.OrgListEntity> room_orgList;

    //获取全局经纬度
    String lo;
    String la;

    //定位
    public LocationClient mLocationClient = null;

    public void onCreate() {
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(mListener);    //注册监听函数
    }

    //初始化
    private void initView() {
        spinnerA = (Spinner) findViewById(R.id.kongtiao_spinner1);
        spinnerB = (Spinner) findViewById(R.id.kongtiao_spinner2);
        spinnerC = (Spinner) findViewById(R.id.kongtiao_spinner3);
        spinnerD = (Spinner) findViewById(R.id.kongtiao_spinner4);
        spinnerE = (Spinner) findViewById(R.id.kongtiao_spinner5);
        spinnerF = (Spinner) findViewById(R.id.kongtiao_spinner6);
        spinner_Name = (Spinner) findViewById(R.id.kongtiao_spinner_Name);

        spinnerA.setOnItemSelectedListener(this);
        spinnerB.setOnItemSelectedListener(this);
        spinnerC.setOnItemSelectedListener(this);
        spinnerD.setOnItemSelectedListener(this);
        spinnerE.setOnItemSelectedListener(this);
        spinnerF.setOnItemSelectedListener(this);
        spinner_Name.setOnItemSelectedListener(this);

        dingwei = (TextView) findViewById(R.id.kongtiao_tv_dingwei);

        et_NAME = (EditText) findViewById(R.id.kongtiao_et_NAME);
        et_MAC = (EditText) findViewById(R.id.kongtiao_et_MAC);
        et_BZ = (EditText) findViewById(R.id.kongtiao_et_BZ);

        back = (ImageView) findViewById(R.id.kongtiao_img_back);
        scan = (Button) findViewById(R.id.kongtiao_btn_scan);
        tijiao = (Button) findViewById(R.id.kongtiao_btn_tijiao);

        back.setOnClickListener(this);
        scan.setOnClickListener(this);
        tijiao.setOnClickListener(this);
    }

    //网络请求
    private void httpVolley() {
        volleyTest_a();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kong_tiao);

        //创建volley请求队列
        mQueue = Volley.newRequestQueue(this);
        //提示连接中
        progressDialog = ProgressDialog.show(this, "",
                "正在加载 . . .", true);
        initView();

        //网络请求
        httpVolley();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.kongtiao_img_back:
                finish();
                break;
            case R.id.kongtiao_btn_scan:
                Intent intent = new Intent();
                intent.setClass(KongTiaoActivity.this, MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                break;
            case R.id.kongtiao_btn_tijiao:
                if (et_NAME.getText().toString().equals("") || et_MAC.getText().toString().equals("")||et_BZ.getText().toString().equals("")) {
                    dialog("昵称,MAC或品牌不能为空?", false);
                } else {
                    progressDialog.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Content.KT, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Log.e("qianguan",s);
                            City ew = new Gson().fromJson(s, City.class);
                            if (ew.getStatus().equals("success")) {
                                Toast.makeText(KongTiaoActivity.this, ew.getMessage() + "", Toast.LENGTH_SHORT).show();
                                et_MAC.setText("");
                                progressDialog.dismiss();
                            } else {
                                Toast.makeText(KongTiaoActivity.this, ew.getMessage() + "", Toast.LENGTH_LONG).show();
//                                mac_test = et_MAC.getText().toString();
                                et_MAC.setText("");
                                progressDialog.dismiss();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            progressDialog.dismiss();
                            Log.e("qianguan",volleyError.toString());
                            dialog("数据上传失败，重新开始？", false);
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("KTBH", et_MAC.getText().toString());
                            map.put("city", city_ID);
                            map.put("name", et_NAME.getText().toString());
                            map.put("room", room_ID);
                            map.put("acBrand", et_BZ.getText().toString()+""); //备注
                            map.put("remoteType", ""); //备注
                            return map;
                        }
                    };
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
                    mQueue.add(stringRequest);
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
        switch (parent.getId()) {
            case R.id.kongtiao_spinner1:
                volleyTest_b(i);
                break;
            case R.id.kongtiao_spinner2:
                volleyTest_c(i);
                break;
            case R.id.kongtiao_spinner3:
                volleyTest_d(i);
                break;
            case R.id.kongtiao_spinner4:
                volleyTest_e(i);
                break;
            case R.id.kongtiao_spinner5:
                volleyTest_f(i);
                break;
            case R.id.kongtiao_spinner6:
                //地图定位
                onCreate();
                initLocation();
                mLocationClient.start();

                room_ID = room_orgList.get(i).getOrgID();
                progressDialog.dismiss();
                break;
            case R.id.kongtiao_spinner_Name:
                custID = county_custList.get(i).getCustID();
                custName = county_custList.get(i).getCustName();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //扫描并返回数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    //显示扫描到的内容
                    String str = Utils.replaceBlank(bundle.getString("result"));
                    et_MAC.setText(str);
                }
                break;
        }
    }

    //dialog 弹窗
    protected void dialog(final String str, final Boolean flag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(KongTiaoActivity.this);
        builder.setMessage(str);
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (flag) {
                    progressDialog.show();
                    httpVolley();
                }
                if (str.equals("是否拆回插座?")) {
//                    volleyTest_CH();
                }
            }

        });
        builder.setCancelable(false);
        builder.create().show();
    }

    private void volleyTest_a() {
        request_a = new StringRequest(Request.Method.POST, Content.JGCX, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                City province = new Gson().fromJson(s, City.class);
                province_orgList = province.getOrgList();
                province_arr = new String[province_orgList.size()];
                for (int i = 0; i < province_orgList.size(); i++) {
                    province_arr[i] = province_orgList.get(i).getOrgName();
                }
                //添加内容
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(KongTiaoActivity.this, android.R.layout.simple_spinner_item, province_arr);
                //设置样式
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //添加适配器
                spinnerA.setAdapter(arrayAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                dialog("数据加载失败，重新开始？", true);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("orgType", "province");
                map.put("superOrgID", "100000");
                return map;
            }
        };
        mQueue.add(request_a);
    }

    private void volleyTest_b(int i) {
        progressDialog.show();
        province_ID = province_orgList.get(i).getOrgID();
        if (province_ID.equals("1000011")) {
            city_arr = new String[]{"上海"};
            //添加内容
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(KongTiaoActivity.this, android.R.layout.simple_spinner_item, city_arr);
            //设置样式
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //添加适配器
            spinnerB.setAdapter(arrayAdapter);
        } else {
            request_b = new StringRequest(Request.Method.POST, Content.JGCX, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    City city = new Gson().fromJson(s, City.class);
                    city_orgList = city.getOrgList();
                    city_arr = new String[city_orgList.size()];
                    for (int i = 0; i < city_orgList.size(); i++) {
                        city_arr[i] = city_orgList.get(i).getOrgName();
                    }
                    //添加内容
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(KongTiaoActivity.this, android.R.layout.simple_spinner_item, city_arr);
                    //设置样式
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //添加适配器
                    spinnerB.setAdapter(arrayAdapter);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    progressDialog.dismiss();
                    dialog("数据加载失败，重新开始？", true);
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("orgType", "city");
                    map.put("superOrgID", province_ID);
                    return map;
                }
            };
            mQueue.add(request_b);
        }
    }

    private void volleyTest_c(int i) {
        progressDialog.show();
        if (city_arr[i].equals("上海")) {
            city_ID = "1000011";
        }else if (city_arr[i].equals("千贯")){
            city_ID = "100001101";
        } else {
            city_ID = city_orgList.get(i).getOrgID();
        }
        request_c = new StringRequest(Request.Method.POST, Content.JGCX, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                City county = new Gson().fromJson(s, City.class);

                county_custList = county.getCustList();
                county_orgList = county.getOrgList();

                name_arr = new String[county_custList.size()];
                county_arr = new String[county_orgList.size()];

                for (int i = 0; i < county_custList.size(); i++) {
                    name_arr[i] = county_custList.get(i).getCustName();
                }

                //添加内容
                ArrayAdapter<String> arrayAdapter_Name = new ArrayAdapter<String>(KongTiaoActivity.this, android.R.layout.simple_spinner_item, name_arr);
                //设置样式
                arrayAdapter_Name.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //添加适配器
                spinner_Name.setAdapter(arrayAdapter_Name);

                for (int i = 0; i < county_orgList.size(); i++) {
                    county_arr[i] = county_orgList.get(i).getOrgName();
                }

                //添加内容
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(KongTiaoActivity.this, android.R.layout.simple_spinner_item, county_arr);
                //设置样式
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //添加适配器
                spinnerC.setAdapter(arrayAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                dialog("数据加载失败，重新开始？", true);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("orgType", "county");
                map.put("superOrgID", city_ID);
                return map;
            }
        };
        mQueue.add(request_c);
    }

    private void volleyTest_d(int i) {
        progressDialog.show();
        county_ID = county_orgList.get(i).getOrgID();
        request_d = new StringRequest(Request.Method.POST, Content.JGCX, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                City company = new Gson().fromJson(s, City.class);
                company_orgList = company.getOrgList();
                company_arr = new String[company_orgList.size()];
                for (int i = 0; i < company_orgList.size(); i++) {
                    company_arr[i] = company_orgList.get(i).getOrgName();
                }
                //添加内容
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(KongTiaoActivity.this, android.R.layout.simple_spinner_item, company_arr);
                //设置样式
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //添加适配器
                spinnerD.setAdapter(arrayAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                dialog("数据加载失败，重新开始？", true);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("orgType", "company");
                map.put("superOrgID", county_ID);
                map.put("superOrgID", city_ID);
                return map;
            }
        };
        mQueue.add(request_d);
    }

    private void volleyTest_e(int i) {
        progressDialog.show();
        company_ID = company_orgList.get(i).getOrgID();
        request_e = new StringRequest(Request.Method.POST, Content.JGCX, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                City floor = new Gson().fromJson(s, City.class);
                floor_orgList = floor.getOrgList();
                floor_arr = new String[floor_orgList.size()];
                for (int i = 0; i < floor_orgList.size(); i++) {
                    floor_arr[i] = floor_orgList.get(i).getOrgName();
                }

                //添加内容
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(KongTiaoActivity.this, android.R.layout.simple_spinner_item, floor_arr);
                //设置样式
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //添加适配器
                spinnerE.setAdapter(arrayAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                dialog("数据加载失败，重新开始？", true);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("orgType", "floor");
                map.put("superOrgID", company_ID);
                map.put("belongOrgID", city_ID);
                return map;
            }
        };
        mQueue.add(request_e);
    }

    private void volleyTest_f(int i) {
        progressDialog.show();
        floor_ID = floor_orgList.get(i).getOrgID();
        gateway = floor_orgList.get(i).getGateway();
        request_f = new StringRequest(Request.Method.POST, Content.JGCX, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                City room = new Gson().fromJson(s, City.class);
                room_orgList = room.getOrgList();
                room_arr = new String[room_orgList.size()];
                for (int i = 0; i < room_orgList.size(); i++) {
                    room_arr[i] = room_orgList.get(i).getOrgName();
                }
                //添加内容
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(KongTiaoActivity.this, android.R.layout.simple_spinner_item, room_arr);
                //设置样式
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //添加适配器
                spinnerF.setAdapter(arrayAdapter);
                if (room_orgList.size() == 0) {
                    progressDialog.dismiss();
                    dialog("数据为空？", false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                dialog("数据加载失败，重新开始？", true);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("orgType", "room");
                map.put("superOrgID", floor_ID);
                map.put("belongOrgID", city_ID);
                return map;
            }
        };
        mQueue.add(request_f);
    }


    /**
     * 以下是百度地图定位
     */

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    /**
     * 显示请求字符串
     *
     * @param latitude  纬度
     * @param lontitude 经度
     */
    public void logMsg(String str, final String latitude, final String lontitude) {
        try {
            if (dingwei != null)

//          Log.e("nnnn", "纬度" + latitude + "@@" + "经度" + lontitude);

                dingwei.setText("纬度" + latitude + "  " + "经度" + lontitude);
            mLocationClient.stop();

            //获取经纬度
            lo = lontitude;
            la = latitude;


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*****
     * copy funtion to you project
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
//                sb.append("\nerror code : ");
//                sb.append(location.getLocType());
                sb.append("\nlatitude : ");
                sb.append(location.getLatitude());
                //纬度
                String latitude = String.valueOf(location.getLatitude());

                sb.append("\nlontitude : ");
                sb.append(location.getLongitude());
                //经度
                String lontitude = String.valueOf(location.getLongitude());
//                sb.append("\nradius : ");
//                sb.append(location.getRadius());
//                sb.append("\nCountryCode : ");
//                sb.append(location.getCountryCode());
//                sb.append("\nCountry : ");
//                sb.append(location.getCountry());
//                sb.append("\ncitycode : ");
//                sb.append(location.getCityCode());
//                sb.append("\ncity : ");
//                sb.append(location.getCity());
//                sb.append("\nDistrict : ");
//                sb.append(location.getDistrict());
//                sb.append("\nStreet : ");
//                sb.append(location.getStreet());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\nDescribe: ");
                sb.append(location.getLocationDescribe());
//                sb.append("\nDirection(not all devices have value): ");
//                sb.append(location.getDirection());
//                sb.append("\nPoi: ");
//                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
//                    for (int i = 0; i < location.getPoiList().size(); i++) {
//                        Poi poi = (Poi) location.getPoiList().get(i);
//                        sb.append(poi.getName() + ";");
//                    }
//                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 单位：米
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
//                    sb.append("\noperationers : ");
//                    sb.append(location.getOperators());
                    sb.append("\n网络定位结果 : ");
                    sb.append("成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                logMsg(sb.toString(), latitude, lontitude);
            }
        }
    };

    /**
     * 软键盘无响应自动隐藏
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
