package com.gruppe2.Client.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myles.projecto.R;
import com.gruppe2.Client.Database.DatabaseHandler;
import com.gruppe2.Client.Database.EventsDataSource;
import com.gruppe2.Client.Helper.EventsViewAdapter;
import com.gruppe2.Client.Objects.Event;

import java.util.ArrayList;

/**@author  Myles Sutholt
    Diese Klasse zeigt eine Ansicht aller bereits belegten Veranstaltungen
 */
public class MyEvents extends AppCompatActivity {

    private ListView listview;
    private EventsDataSource datasource;
    private EventsViewAdapter adapter;
    private ArrayList<Event>  events;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);

        //Verbindungsaufbau zur SQLite Datenbank

        DatabaseHandler handler = ((DatabaseHandler) getApplicationContext());
        datasource = (handler.getDatasource());

        //Listview laden
        listview = (ListView) findViewById(R.id.events_list);

        //Daten abfragen und konvertieren
        events= (((DatabaseHandler)getApplicationContext()).datasource.getAllNames());

        //ArrayAdapter initialisieren
        adapter= new EventsViewAdapter(this, events);


        //ArrayList mit ArrayAdapter verbinden
        listview.setAdapter(adapter);

        //ArrayAdaper auf Datenänderung hinweisen
        adapter.notifyDataSetChanged();



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.first);



                Intent intent = new Intent(MyEvents.this, EventView.class);
                Bundle b = new Bundle();
                b.putInt("ID", events.get(position).getEventID()); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_events_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(MyEvents.this, CreateEvent.class);
                startActivity(intent);
            case R.id.action_settings:

        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        events= (((DatabaseHandler)getApplicationContext()).datasource.getAllNames());

        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
