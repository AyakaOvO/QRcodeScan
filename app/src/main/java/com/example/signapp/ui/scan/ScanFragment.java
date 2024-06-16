package com.example.signapp.ui.scan;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.signapp.MainActivity;
import com.example.signapp.R;
import com.example.signapp.scan.Permission;
import com.yxing.ScanCodeActivity;
import com.yxing.ScanCodeConfig;
import com.yxing.def.ScanStyle;


public class ScanFragment extends Fragment {


    AppCompatActivity appCompatActivity;
    Fragment ScanFragment;
    Button ScanButton ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_scan, container, false);


       appCompatActivity = (AppCompatActivity) getActivity();
       ScanFragment = getParentFragment();

        ScanButton =  view.findViewById(R.id.ScanButton);
        Permission.checkPermission(appCompatActivity);
        ScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    ScanCodeConfig.create(appCompatActivity,ScanFragment )
                            .setStyle(ScanStyle.WECHAT )
                            //扫码成功是否播放音效  true ： 播放   false ： 不播放
                            .setPlayAudio(false)
                            .buidler()
                            //跳转扫码页   扫码页可自定义样式
                            .start(ScanCodeActivity.class);
                }



        });


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


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