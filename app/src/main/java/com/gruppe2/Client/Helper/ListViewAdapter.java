package com.gruppe2.Client.Helper;

/**
 * Created by myles on 17.05.15.
 */

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
        import com.gruppe2.Client.Objects.Session;
/**
 *Generierung der Ansicht für den Ablaufplan in einer dreispaltigen ListView
 *
 *@author  Myles Sutholt
 */
public class ListViewAdapter extends BaseAdapter {

    public ArrayList<Session> list;
    Activity activity;
    TextView txtFirst;
    TextView txtSecond;
    TextView txtThird;

    public ListViewAdapter(Activity activity, ArrayList<Session> list) {
        super();
        this.activity = activity;
        this.list = list;
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

//Generiert die Veranstaltungsübersicht aus den entsprechenden Veranstaltungsdaten und setzt die Hintergrundfarbe abwechselnd standard und heller

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
// TODO Auto-generated method stub
        LayoutInflater inflater = activity.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_multi_column, null);
            txtFirst = (TextView) convertView.findViewById(R.id.first);
            txtSecond = (TextView) convertView.findViewById(R.id.second);
            txtThird = (TextView) convertView.findViewById(R.id.last);
        }
        Session session = list.get(position);
        txtFirst.setText((new Parser().DateToStringTime(session.getDateStart())));
        txtSecond.setText(session.getName());
        txtThird.setText(session.getLocation());
        if ((position % 2) == 0) {
            changeBGColor(position);
        }
        return convertView;
    }

    private void changeBGColor(int position) {
        //"#353535"
        Log.d("POSTITION", ((Integer) position).toString() + " " + ((Integer) getCount()).toString());
        txtFirst.setBackgroundColor(Color.parseColor("#353535"));
        txtSecond.setBackgroundColor(Color.parseColor("#353535"));
        txtThird.setBackgroundColor(Color.parseColor("#353535"));
    }
}

