package com.gruppe2.Event.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.gruppe2.Event.Database.DatabaseHandler;
import com.gruppe2.Event.Database.EventsDataSource;
import com.gruppe2.Event.Helper.EventsViewAdapter;
import com.gruppe2.Event.Objects.Event;

import java.util.ArrayList;
import java.util.HashMap;


public class EventsList extends AppCompatActivity {

    private ListView listview;
    private EventsDataSource datasource;
    public static Integer EVENTID;

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
        //ArrayList<HashMap<String, String>>  events = datasource.getAllNames();

        ArrayList<Event>  events= (((DatabaseHandler)getApplicationContext()).datasource.getAllNames());

        //ArrayAdapter initialisieren
        EventsViewAdapter adapter= new EventsViewAdapter(this, events);


        //ArrayList mit ArrayAdapter verbinden
        listview.setAdapter(adapter);

        //ArrayAdaper auf Datenänderung hinweisen
        adapter.notifyDataSetChanged();



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.first);
                EVENTID = Integer.parseInt(tv.getText().toString());


                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            EventsList.this );

                    // set title
                    alertDialogBuilder.setTitle("Veranstaltung beitreten");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Willst du der Veranstaltung beitreten?")
                            .setCancelable(false)

                            .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            })

                            .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(EventsList.this, SessionList.class);
                                    Bundle b = new Bundle();
                                    b.putInt("ID", EVENTID); //Your id
                                    intent.putExtras(b); //Put your id to your next Intent
                                    startActivity(intent);
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();


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
                Intent intent = new Intent(EventsList.this, CreateEvent.class);
                startActivity(intent);
            case R.id.action_settings:

        }

            return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}