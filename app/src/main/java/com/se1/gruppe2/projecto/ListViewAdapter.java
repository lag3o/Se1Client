package com.se1.gruppe2.projecto;

/**
 * Created by myles on 17.05.15.
 */
        import static com.se1.gruppe2.projecto.Constants.FIRST_COLUMN;
        import static com.se1.gruppe2.projecto.Constants.SECOND_COLUMN;
        import static com.se1.gruppe2.projecto.Constants.THIRD_COLUMN;

        import java.util.ArrayList;
        import java.util.HashMap;

        import android.app.Activity;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.TextView;

        import com.example.myles.projecto.R;

public class ListViewAdapter extends BaseAdapter{

    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    TextView txtFirst;
    TextView txtSecond;
    TextView txtThird;
    public ListViewAdapter(Activity activity,ArrayList<HashMap<String, String>> list){
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
            txtFirst=(TextView) convertView.findViewById(R.id.time);
            txtSecond=(TextView) convertView.findViewById(R.id.session);
            txtThird=(TextView) convertView.findViewById(R.id.location);
        }
        HashMap<String, String> map=list.get(position);
        txtFirst.setText(map.get(FIRST_COLUMN));
        txtSecond.setText(map.get(SECOND_COLUMN));
        txtThird.setText(map.get(THIRD_COLUMN));
        return convertView;
    }

}
