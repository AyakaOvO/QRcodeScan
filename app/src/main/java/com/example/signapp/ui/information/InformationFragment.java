package com.example.signapp.ui.information;



import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.signapp.MainActivity;
import com.example.signapp.R;
import com.example.signapp.ui.login.LoginActivity;


public class InformationFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private AppCompatActivity appCompatActivity;
    private String name;

    private String sclass;

    private String id;

    private String ralid;

    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_information, container, false);


        appCompatActivity = (AppCompatActivity) getActivity();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(appCompatActivity);


        id = sharedPreferences.getString("id","0");
        ralid =sharedPreferences.getString("ralid","1");
        name = sharedPreferences.getString("name","未登录");
        sclass = sharedPreferences.getString("sclass","点击登录");

        if(!name.equals("未登录") && !sclass.equals("点击登录")){
            TextView nameText = view.findViewById(R.id.information_name_view);
            nameText.setText(name);
            TextView sclassText = view.findViewById(R.id.information_sclass_view);
            sclassText.setText(sclass);
            TextView idText = view.findViewById(R.id.information_id_view);
            idText.setText(id);
            TextView ralidText = view.findViewById(R.id.information_valid_view);
            ralidText.setText(ralid);

        }else {
            Toast.makeText(appCompatActivity.getApplicationContext(),"请先登录！",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(appCompatActivity, LoginActivity.class);
            startActivity(intent);
        }


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        name = sharedPreferences.getString("name","未登录");
        sclass = sharedPreferences.getString("sclass","点击登录");
        id = sharedPreferences.getString("id","0");
        ralid =sharedPreferences.getString("ralid","1");
        if(!name.equals("未登录") && !sclass.equals("点击登录")){
            TextView nameText = view.findViewById(R.id.information_name_view);
            nameText.setText(name);
            TextView sclassText = view.findViewById(R.id.information_sclass_view);
            sclassText.setText(sclass);
            TextView idText = view.findViewById(R.id.information_id_view);
            idText.setText(id);
            TextView ralidText = view.findViewById(R.id.information_valid_view);
            ralidText.setText(ralid);

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}