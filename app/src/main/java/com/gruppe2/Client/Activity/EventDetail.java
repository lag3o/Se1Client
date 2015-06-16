package com.gruppe2.Client.Activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.myles.projecto.R;
import com.gruppe2.Client.Database.ApplicationHandler;
import com.gruppe2.Client.Helper.Parser;
import com.gruppe2.Client.Objects.Event;

import static com.gruppe2.Client.Helper.Constants.DESCR;
import static com.gruppe2.Client.Helper.Constants.LOC;
import static com.gruppe2.Client.Helper.Constants.NAME;
import static com.gruppe2.Client.Helper.Constants.START;

public class EventDetail extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        Event event = ((ApplicationHandler) getApplicationContext()).getEvent();

        TextView text = (TextView) findViewById(R.id.name);
                text.setText(event.getName());
                ((TextView) findViewById(R.id.name)).setText(event.getName());
        ((TextView) findViewById(R.id.time)).setText((new Parser().DateToStringDate(event.getDateStart())) + " - " + (new Parser().DateToStringDate(event.getDateEnd())));
        ((TextView) findViewById(R.id.description)).setText(event.getDescription());
        //((TextView) findViewById(R.id.contact)).setText(event.getUser);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_detail, menu);
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
