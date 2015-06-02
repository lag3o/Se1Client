package com.se1.gruppe2.projecto;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myles.projecto.R;

import static com.se1.gruppe2.projecto.Constants.ID;
import static com.se1.gruppe2.projecto.Constants.NAME;
import static com.se1.gruppe2.projecto.Constants.START;

/**
 * Created by myles on 01.06.15.
 */
public class EventsViewAdapter extends BaseAdapter{

    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    TextView txtFirst;
    TextView txtSecond;
    TextView txtThird;
    public EventsViewAdapter(Activity activity,ArrayList<HashMap<String, String>> list){
        super();
        this.activity=activity;
        this.list=list;
    }
        @Override
        public int getCount() {
// TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int position) {
// TODO Auto-generated method stub
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
// TODO Auto-generated method stub
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
// TODO Auto-generated method stub
            LayoutInflater inflater=activity.getLayoutInflater();
            if(convertView == null){
                convertView=inflater.inflate(R.layout.layout_multi_column, null);
                txtFirst=(TextView) convertView.findViewById(R.id.first);
                txtSecond=(TextView) convertView.findViewById(R.id.second);
                txtThird=(TextView) convertView.findViewById(R.id.last);
            }
            HashMap<String, String> map=list.get(position);
            txtFirst.setText(map.get(ID));
            txtSecond.setText(map.get(NAME));
            txtThird.setText(map.get(START));
            return convertView;
        }

}
