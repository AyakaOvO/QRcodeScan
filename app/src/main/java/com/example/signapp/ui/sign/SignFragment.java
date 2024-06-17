package com.example.signapp.ui.sign;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.signapp.R;
import com.yxing.ScanCodeConfig;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class SignFragment extends Fragment {

        private Handler handler =null;
        private Bitmap bitmap ;
        private ImageView imageView;
        private Button StarQRCodeButton;
        private Button EndQRCodeButton;
        private EditText editText;

        private int SignTime;

        private EditText classNameInput;

        private String className;





    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign, container, false);
        //新建的ui线程
        handler = new Handler();

        classNameInput =view.findViewById(R.id.className_input);
        editText = view.findViewById(R.id.SignTime_input);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                SignTime = Integer.parseInt(editText.getText().toString());
                return false;
            }
        });
        SignTime = Integer.parseInt(editText.getText().toString());

        StarQRCodeButton = view.findViewById(R.id.Start_Sign_Button);
        EndQRCodeButton = view.findViewById(R.id.End_Sign_Button);
        EndQRCodeButton.setVisibility(View.GONE);
        StarQRCodeButton.setOnClickListener(new BtnOnClickListener());
        imageView = view.findViewById(R.id.sign_QRCode);
        imageView.setVisibility(View.GONE);
        return view;
    }


    class BtnOnClickListener implements View.OnClickListener {

        private volatile boolean thread_stop = true;
        private Date starDate ;


        //计算时间签到开始时间和现在时间的时间差，如果大于规定的时间就结束线程
        public void distanceTime(Date starDate){
            Date date = new Date();
            if((date.getTime() - starDate.getTime())> (long) SignTime *60*1000){
                thread_stop = false;
                Log.d("threadTime","threadStop");
            }

        }


        //结束线程的方法
        public void stopThread() {
            thread_stop = false;
            Log.d("Thread","ThreadStop");
        }



        //当点击开始生成二维码时
        @Override
        public void onClick(View v) {

            thread_stop = true;
            starDate = new Date();
            className = classNameInput.getText().toString();


            //5秒刷新一次
            final long timeInterval = 5000;

            imageView.setVisibility(View.VISIBLE);
            StarQRCodeButton.setVisibility(View.GONE);
            EndQRCodeButton.setVisibility(View.VISIBLE);


            //新建线程用于生成二维码并更新UI
            Thread t  = new Thread() {

    //+gF7ztMFt0pt云服务器数据库密码
                @Override
                public void run() {

                    while (thread_stop ){


                        Date date = new Date();
                        long dateLong = date.getTime();
                        distanceTime(starDate);


                        String dateString = dateLong+","+className ;
                        Log.d("date",dateString );
                        bitmap = ScanCodeConfig.createQRCode(dateString);
                        handler.post(runnable);
                        Log.d("QRCode","QRCode");

                        try {
                            sleep(timeInterval);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    }

                    if(!thread_stop){
                        handler.post(UIrun);
                    }

                }

            };
            t.start();

            //结束签到按钮
            EndQRCodeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopThread();

                }
            });
    }




    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            imageView.setImageBitmap(bitmap);
        }
    };

    Runnable UIrun = new Runnable() {
        @Override
        public void run() {
            StarQRCodeButton.setVisibility(View.VISIBLE);
            EndQRCodeButton.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
        }
    };




    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}