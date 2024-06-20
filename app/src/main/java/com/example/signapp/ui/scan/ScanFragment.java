package com.example.signapp.ui.scan;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.signapp.R;
import com.example.signapp.ui.login.LoginActivity;
import com.yxing.ScanCodeActivity;
import com.yxing.ScanCodeConfig;
import com.yxing.def.ScanStyle;

import java.util.Date;
import java.util.Observer;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;






public class ScanFragment extends Fragment {


    AppCompatActivity appCompatActivity;
    Fragment ScanFragment;
    Button ScanButton ;

    Handler handler = null;

    private String name;
    private String sclass;
    String QRCodeValue;
    private SharedPreferences sharedPreferences;
    TextView homeText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        handler = new Handler();

        appCompatActivity = (AppCompatActivity) getActivity();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(appCompatActivity);

        ScanFragment = getParentFragment();
        homeText= view.findViewById(R.id.text_home);
        ScanButton =  view.findViewById(R.id.ScanButton);
        Permission.checkPermission(appCompatActivity);
        ScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = sharedPreferences.getString("name","未登录");
                sclass = sharedPreferences.getString("sclass","点击登录");

                if(!name.equals("未登录") && !sclass.equals("点击登录")){

                    ScanCodeConfig.create(appCompatActivity,ScanFragment )
                            .setStyle(ScanStyle.WECHAT )
                            //扫码成功是否播放音效  true ： 播放   false ： 不播放
                            .setPlayAudio(false)
                            .setShowFrame(true)
                            .buidler()
                            .start(ScanCodeActivity.class);
                    //跳转扫码页   扫码页可自定义样式

                }else {
                    Toast.makeText(appCompatActivity.getApplicationContext(),"请先登录！",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(appCompatActivity, LoginActivity.class);
                    startActivity(intent);
                }

                Log.d("scan123","starscan");



                }



        });


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }



    //授权相机权限
@Override
public void onRequestPermissionsResult(
        int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == Permission.REQUEST_CODE) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                Log.e("Permission","授权失败！");
                // 授权失败，退出应用
                appCompatActivity.finish();
                return;
            }
        }
    }
}







}