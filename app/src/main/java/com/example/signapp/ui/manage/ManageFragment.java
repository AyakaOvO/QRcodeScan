package com.example.signapp.ui.manage;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.alibaba.fastjson.JSON;
import com.example.signapp.R;
import com.example.signapp.pojo.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ManageFragment extends Fragment {

    private SharedPreferences sharedPreferences;

    private Button button;
    private String ralid;

    private String selectSubject;
    private EditText subjectNameInput;

    private ProgressDialog progressDialog;
    private ListView listView;
    private List<User> userList = null;

    private Handler handler =null;

    private AppCompatActivity appCompatActivity;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage, container, false);
        appCompatActivity = (AppCompatActivity) getActivity();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(appCompatActivity);
        progressDialog = new ProgressDialog(appCompatActivity);
        progressDialog.setMessage("加载中");
        handler =new Handler();
        FragmentManager fragmentManager = getFragmentManager();
        ralid =sharedPreferences.getString("ralid","1");

        if(Integer.parseInt(ralid) < 2){
            fragmentManager.popBackStack();
            Toast.makeText(appCompatActivity.getApplicationContext(),"无权限！",Toast.LENGTH_SHORT).show();

        }
        subjectNameInput = view.findViewById(R.id.subjectManageInput);
        button = view.findViewById(R.id.manage_button);
        listView = view.findViewById(R.id.manage_list);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                selectSubject = subjectNameInput.getText().toString();
                Log.d("manageselectClass",selectSubject);

                new Thread(new Runnable() {

                    @Override
                    public void run() {


                        try{

                            FormBody.Builder params = new FormBody.Builder();
                            params.add("subject",selectSubject);

                            Log.d("manageMessage",selectSubject);
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url("http://192.168.223.94:8080/teacherselect")
                                    .post(params.build())
                                    .build();
                            Response response = client.newCall(request).execute();
                            if ( response.body() != null) {


                                String responseDate = response.body().string();
                                Log.d("manageMessage",responseDate);
                                JSONArray jsonData = new JSONArray(responseDate);
                                Log.d("manageMessage", String.valueOf(jsonData.length()));

                                userList = JSON.parseArray(responseDate, User.class);


                                Log.d("manageMesssage",userList.toString());

                                progressDialog.dismiss();
                                Toast.makeText(appCompatActivity.getApplicationContext(),"请求成功",Toast.LENGTH_SHORT).show();

                            }


                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        handler.post(runnable);
                    }
                }).start();




            }
        });


        return view;
    }

Runnable runnable = new Runnable() {


    @Override
    public void run() {
        Log.d("manageMessage","UIrefresh");
        ItemAdapter itemAdapter =new ItemAdapter(requireContext(),R.layout.manage_item,userList);
        listView.setAdapter(itemAdapter);
    }
};

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}