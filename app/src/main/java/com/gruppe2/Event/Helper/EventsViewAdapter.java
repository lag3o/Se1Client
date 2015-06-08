package com.gruppe2.Event.Helper;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myles.projecto.R;
import com.gruppe2.Event.Objects.Event;

import static com.gruppe2.Event.Helper.Constants.ID;
import static com.gruppe2.Event.Helper.Constants.NAME;
import static com.gruppe2.Event.Helper.Constants.START;

/**
 * Created by myles on 01.06.15.
 */
public class EventsViewAdapter extends BaseAdapter{

    public ArrayList<Event> list;
    Activity activity;
    TextView txtFirst;
    TextView txtSecond;
    TextView txtThird;
    public EventsViewAdapter(Activity activity,ArrayList<Event> list){
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
            Event event = list.get(position);
            txtFirst.setText(event.getId().toString());
            txtSecond.setText(event.getName());
            txtThird.setText((new Parser().DateToString(event.getStartDate())));
            return convertView;
        }

}
