package com.example.signapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.signapp.ui.login.LoginActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.signapp.databinding.ActivityMainBinding;
import com.yxing.ScanCodeConfig;


import java.util.Arrays;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static ProgressDialog progressDialog;
    private Handler handler = null;

    private TextView homeText;
    private String id;
    private String name;
    private String sclass;
    private String ralid;
    private SharedPreferences sharedPreferences;
    private View loginView;


    String QRCodeValue;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        handler = new Handler();
        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        NavigationView navigationView = binding.navView;
        loginView = navigationView.getHeaderView(0);
        loginView.setOnClickListener(new loginViewOnclick());

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_scan, R.id.nav_sign, R.id.nav_manage,R.id.nav_information)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        name = sharedPreferences.getString("name","未登录");
        sclass = sharedPreferences.getString("sclass","点击登录");
        id = sharedPreferences.getString("id","0");
        ralid =sharedPreferences.getString("ralid","1");
        if(!name.equals("未登录") && !sclass.equals("点击登录")){
            TextView login_text1 =  loginView.findViewById(R.id.login_text1);
            TextView login_text2 = loginView.findViewById(R.id.login_text2);
            login_text1.setText(name);
            login_text2.setText(sclass);
        }



        homeText = findViewById(R.id.text_home);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    protected void onResume() {
        super.onResume();

        name = sharedPreferences.getString("name","未登录");
        sclass = sharedPreferences.getString("sclass","点击登录");
        id = sharedPreferences.getString("id","0");
        ralid =sharedPreferences.getString("ralid","1");
        Log.d("userMessage",name+sclass);

        if(!name.equals("未登录") && !sclass.equals("点击登录")){
            TextView login_text1 =  loginView.findViewById(R.id.login_text1);
            TextView login_text2 = loginView.findViewById(R.id.login_text2);
            login_text1.setText(name);
            login_text2.setText(sclass);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("scan123","activity开始扫描");
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            progressDialog.show();
            Log.d("scan123", String.valueOf(requestCode));
            Log.d("scan123", String.valueOf(ScanCodeConfig.QUESTCODE));

                    //接收扫码结果
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        int codeType = extras.getInt(ScanCodeConfig.CODE_TYPE);
                        QRCodeValue = extras.getString(ScanCodeConfig.CODE_KEY);
                        String[] str = new String[0];
                        if (QRCodeValue != null) {
                            str = QRCodeValue.split(",");
                        }
                        Log.d("json", Arrays.toString(str));




                        String creatTime = str[0];
                        String subject = str[1];
                        Log.d("json", creatTime);
                        Log.d("json", subject);

                        if (QRCodeValue != null) {
                            Log.d("scan123",QRCodeValue);
                        }else {
                            Log.d("scan123","null");
                        }

                        Date postTime = new Date();
                        //申请网络权限
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                try {


                                    String json = "{\n" +
                                            "    \"id\":"+ id +",\n" +
                                            "    \"name\":\""+ name +"\",\n" +
                                            "    \"subject\":\"" +  subject  + "\",\n" +
                                            "    \"sclass\":\"" + sclass  + "\",\n" +
                                            "    \"posttime\":\""+ postTime.getTime() + "\",\n" +
                                            "    \"creattime\":\""+ creatTime + "\"\n"+
                                            "}";

                                    Log.d("json",json);

                                    OkHttpClient client = new OkHttpClient();
                                    Request request = new Request.Builder()
                                            .url("http://192.168.105.94:8080/signin")
                                            .post(RequestBody.create(MediaType.parse("application/json"),json))
                                            .build();

                                    Response response = client.newCall(request).execute();
                                    String responseData = response.body().string();
                                    Log.d("response",responseData);
                                    if( responseData.equals("签到成功")){
                                        progressDialog.dismiss();
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                String message = sclass+name+"签到成功";
                                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                                                homeText.setText(message);
                                                Log.d("签到","签到成功");
                                            }
                                        });
                                    }else {
                                        progressDialog.dismiss();
                                        handler.post(new Runnable() {

                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(),"签到失败！请重试",Toast.LENGTH_SHORT).show();
                                                homeText.setText("签到失败！请重试");
                                            }
                                        });
                                    }

                                }catch (Exception e){
                                    e.printStackTrace();
                                    progressDialog.dismiss();
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(),"网络连接失败！请重试",Toast.LENGTH_SHORT).show();
                                            homeText.setText("签到失败！请重试");
                                            Log.d("签到","签到失败");
                                        }
                                    });

                                }

                            }
                        }).start();




                    }

        }else {
            Log.d("scan123","扫描失败");
        }
    }

    class loginViewOnclick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Log.d("click","click");

            if(name.equals("未登录") || sclass.equals("点击登录")){
                Toast.makeText(getApplicationContext(),"请先登录！",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }


        }
    }







}