package com.gruppe2.Client.Activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.example.myles.projecto.R;
import com.gruppe2.Client.Database.DatabaseHandler;
import com.gruppe2.Client.Database.EventsDataSource;
import com.gruppe2.Client.Helper.Parser;
import com.gruppe2.Client.Objects.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.gruppe2.Client.Helper.Constants.DESCR;
import static com.gruppe2.Client.Helper.Constants.END;
import static com.gruppe2.Client.Helper.Constants.NAME;
import static com.gruppe2.Client.Helper.Constants.START;

/**
 * Created by myles on 12.06.15.
 */
public class CreateSessionView extends TabActivity {

    EventsDataSource datasource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);


        Bundle b = getIntent().getExtras();
        Date dateEnd = new Date();
        Date dateStart = new Date();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
            simpleDateFormat.setLenient(false);
            dateEnd = simpleDateFormat.parse((b.getString(END) + " 23:59"));
            dateStart = simpleDateFormat.parse((b.getString(START) + " 00:00"));
        }
        catch (Exception e){

        }


        TabHost tabHost = getTabHost();

        Calendar end = Calendar.getInstance();
        end.setTime(dateEnd);
        Calendar start = Calendar.getInstance();
        start.setTime(dateStart);
        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {

            b.putString("Date", (new Parser().DateToStringDate(date)));
            Intent intent1 = new Intent().setClass(this, CreateSession.class);
            intent1.putExtras(b);
            String day = start.get(Calendar.DAY_OF_MONTH) + "." + start.get(Calendar.MONTH);
            TabHost.TabSpec spec1 = tabHost.newTabSpec(day).setIndicator(day, getResources().getDrawable(R.drawable.abc_tab_indicator_material));
            spec1.setContent(intent1);
            tabHost.addTab(spec1);
        }
        tabHost.setCurrentTab(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
