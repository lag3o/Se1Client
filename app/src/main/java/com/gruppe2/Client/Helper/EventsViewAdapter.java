package com.gruppe2.Client.Helper;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
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
    int i = 0;
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
            txtThird.setText((new Parser().DateToString(event.getDateStart())));
            int i = getCount();
            int it = position % 2;
            if (getCount() % 2 > 0) {
                if (position % 2 > 0) {
                    changeBGColor();
                }
            }
            else {
                if (position % 2 == 0) {
                    changeBGColor();
                }
            }
            return convertView;
        }
    private void changeBGColor(){
        //"#353535"
        txtFirst.setBackgroundColor(Color.parseColor("#FFFFFF"));
        txtSecond.setBackgroundColor(Color.parseColor("#FFFFFF"));
        txtThird.setBackgroundColor(Color.parseColor("#FFFFFF"));
    }

}
