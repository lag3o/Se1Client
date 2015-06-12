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

import java.util.Calendar;
import java.util.Date;

import static com.gruppe2.Client.Helper.Constants.DESCR;
import static com.gruppe2.Client.Helper.Constants.END;
import static com.gruppe2.Client.Helper.Constants.NAME;
import static com.gruppe2.Client.Helper.Constants.START;
import static com.gruppe2.Client.Helper.Constants.ID;

public class EventView extends TabActivity {

    EventsDataSource datasource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);


        DatabaseHandler handler = ((DatabaseHandler) getApplicationContext());
        datasource = (handler.getDatasource());
        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt("ID");
        Event event = datasource.getEvent(id);


        TabHost tabHost = getTabHost();
        Bundle b = new Bundle();
        b.putString(START, ((new Parser().DateToString(event.getDateStart()))));
        b.putString(NAME, (event.getName()));
        b.putString(DESCR, (event.getDescription()));
        b.putString(END, ((new Parser().DateToString(event.getDateEnd()))));
        Intent intent = new Intent().setClass(this, CreateEvent.class);
        intent.putExtras(b); //Put your id to your next Intent
        TabHost.TabSpec spec = tabHost.newTabSpec("Details").setIndicator("Info", getResources().getDrawable(R.drawable.abc_tab_indicator_material));
        spec.setContent(intent);

        tabHost.addTab(spec);



        Calendar end = Calendar.getInstance();
        end.setTime(event.getDateEnd());
        Calendar start = Calendar.getInstance();
        start.setTime(event.getDateStart());
        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {

            b = new Bundle();
            start.add(Calendar.DATE, 1);
            Date dateBundle = start.getTime();
            start.add(Calendar.DATE, -1);
            b.putString("Date", (new Parser().DateToString(dateBundle)));
            Intent intent1 = new Intent().setClass(this, SessionList.class);
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