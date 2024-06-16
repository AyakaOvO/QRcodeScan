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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign, container, false);
        handler = new Handler();

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
        private Date starDate;

        public void distanceTime(Date starDate){
            Date date = new Date();
            if((date.getTime() - starDate.getTime())> (long) SignTime *60*1000){
                thread_stop = false;
            }

        }

        public void stopThread() {
            thread_stop = false;
        }


        @Override
        public void onClick(View v) {

            starDate = new Date();

            final long timeInterval = 5000;
            imageView.setVisibility(View.VISIBLE);
            StarQRCodeButton.setVisibility(View.GONE);
            EndQRCodeButton.setVisibility(View.VISIBLE);



            Thread t  = new Thread() {


                @Override
                public void run() {

                    while (thread_stop  ){

                        Calendar calendar = Calendar.getInstance(Locale.CHINA);

                        Date date = calendar.getTime();
                        distanceTime(date);
                        String url  =date.toString();

                        String dateString = date.toString();
                        Log.d("date",url);
                        bitmap = ScanCodeConfig.createQRCode(url);
                        handler.post(runnable);
                        Log.d("QRCode","QRCode");

                        try {
                            sleep(timeInterval);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }


            }

                };
                t.start();
                ;


            EndQRCodeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopThread();
                    Log.d("Thread","Thread Stop");
                    StarQRCodeButton.setVisibility(View.VISIBLE);
                    EndQRCodeButton.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
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




    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}