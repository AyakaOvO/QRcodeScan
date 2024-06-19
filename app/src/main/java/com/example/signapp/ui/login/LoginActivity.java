package com.example.signapp.ui.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.signapp.R;
import com.example.signapp.databinding.ActivityMainBinding;
import com.example.signapp.databinding.LoginActivityBinding;
import com.example.signapp.pojo.User;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.prefs.Preferences;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity {
    private static ProgressDialog progressDialog;
    private EditText userIdInput;
    private EditText passwdInput;
    private Button loginButton;

    private String userId;
    private String passwd;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Handler handler =null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("登录界面");
        handler = new Handler();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        userIdInput = findViewById(R.id.userIdInput);
//        userIdInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
////                if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED){
//                    //IME_ACTION_DONE回车动作 IME_ACTION_UNSPECIFIED
//                    userId = userIdInput.getText().toString();
//                    //计算
//
//                return false;
//            }
//        });
        passwdInput = findViewById(R.id.passwdInput);
//        passwdInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//
//                    //IME_ACTION_DONE回车动作 IME_ACTION_UNSPECIFIED
//                    passwd = passwdInput.getText().toString();
//                    //计算
//
//                return false;
//            }
//        });
        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();



                userId = userIdInput.getText().toString();
                passwd = passwdInput.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("userMessage",userId+" "+passwd);
                        try {
                            FormBody.Builder params = new FormBody.Builder();
                            params.add("id",userId);
                            params.add("password",passwd);
                            Log.d("userMessage",userId+" "+passwd);
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url("http://192.168.105.94:8080/toLogin")
                                    .post(params.build())
                                    .build();
                            Response response = client.newCall(request).execute();
                            if ( response.body() != null) {


                                String responseDate = response.body().string();
                                Log.d("userMessage",responseDate);
                                JSONObject jsonObject = new JSONObject(responseDate);

                                String id = jsonObject.getString("id");
                                Log.d("userMessage"," "+id);
                                editor.putString("id", id);

                                String name = jsonObject.getString("name");
                                editor.putString("name", name);
                                String password = jsonObject.getString("password");
                                editor.putString("password", password);
                                String sclass = jsonObject.getString("sclass");
                                editor.putString("sclass", sclass);
                                String ralid = jsonObject.getString("ralid");
                                editor.putString("ralid", ralid);
                                editor.commit();

                                Log.d("userMessage", id + name + password + sclass + ralid);
                                progressDialog.dismiss();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                                        Log.d("userMessage", "登录成功");
                                    }
                                });

                                finish();

                            }else {
                                Log.d("userMessage","登录失败");
                                progressDialog.dismiss();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"登录失败，请检查账号和密码是否正确！",Toast.LENGTH_SHORT).show();
                                        Log.d("userMessage","登录失败");
                                    }
                                });
                            }


                        }catch (Exception e){
                            e.printStackTrace();
                            progressDialog.dismiss();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),"网络请求失败，请检查网络！",Toast.LENGTH_SHORT).show();
                                    Log.d("userMessage","网络请求失败");
                                }
                            });
                        }


                    }
                }).start();
            }
        });


    }
}
