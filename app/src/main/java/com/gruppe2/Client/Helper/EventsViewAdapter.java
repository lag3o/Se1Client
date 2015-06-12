package com.gruppe2.Client.Helper;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myles.projecto.R;
import com.gruppe2.Client.Objects.Event;

/**@author Myles Sutholt
Generierung der Ansicht für Veranstaltungen. Dreispaltige ListView
 */
public class EventsViewAdapter extends BaseAdapter{

    public ArrayList<Event> list;
    Activity activity;
    TextView txtFirst;
    TextView txtSecond;
    TextView txtThird;
    int i;
    public EventsViewAdapter(Activity activity,ArrayList<Event> list){
        super();
        this.activity=activity;
        this.list=list;
        i = 0;
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
            txtFirst.setText(event.getEventID().toString());
            txtSecond.setText(event.getName());
            txtThird.setText((new Parser().DateToStringDate(event.getDateStart())));
            if ((position % 2) == 0) {
                changeBGColor(position);
            }
            return convertView;
        }
    private void changeBGColor(int position){
        //"#353535"
        Log.d("POSTITION", ((Integer) position).toString() + " " + ((Integer) getCount()).toString());
        txtFirst.setBackgroundColor(Color.parseColor("#353535"));
        txtSecond.setBackgroundColor(Color.parseColor("#353535"));
        txtThird.setBackgroundColor(Color.parseColor("#353535"));
    }

}
