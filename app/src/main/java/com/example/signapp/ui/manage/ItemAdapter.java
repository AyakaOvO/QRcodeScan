package com.example.signapp.ui.manage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.signapp.R;
import com.example.signapp.pojo.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.List;

public class ItemAdapter extends ArrayAdapter<User> {

    private int resourceId;
    private List<User> objects;

    public ItemAdapter(Context context, int textViewResourceId, List<User> objects){
        super(context,textViewResourceId,objects);
        this.objects=objects;
        resourceId = textViewResourceId;

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User user = getItem(position);
        View view;

        if(convertView == null){            //converView可以缓存已加载的listView，节约程序性能
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_item,parent,false);
        }else {
            view = convertView;
        }

        TextView item_name = view.findViewById(R.id.item_name);
        TextView item_id = view.findViewById(R.id.item_id);
        TextView item_sign = view.findViewById(R.id.item_sign);
        TextView item_time = view.findViewById(R.id.item_time);



                item_name.setText(user.getName());
                item_id.setText(user.getId());

                if(user.getState().equals("未签到")){
                    item_sign.setTextColor(0xEEFF0000);
                }else {
                    item_sign.setTextColor(0xEE000000);
                }
                item_sign.setText(user.getState());


                item_time.setText(user.getSignintime());






        return view;
    }
}
