package com.gruppe2.Client.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myles.projecto.R;
import com.gruppe2.Client.Database.ApplicationHandler;
import com.gruppe2.Client.Helper.EventsViewAdapter;
import com.gruppe2.Client.Helper.Parser;
import com.gruppe2.Client.Objects.Event;
import com.gruppe2.Client.SOAP.SOAPEvents;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static com.gruppe2.Client.Helper.Constants.NAMESPACE;
import static com.gruppe2.Client.Helper.Constants.SOAP_ACTION;
import static com.gruppe2.Client.Helper.Constants.URL;

/**
Diese Klasse erzeugt eine Liste aller Veranstaltungen zur Übersicht

 @author Myles Sutholt
 */
public class EventsList extends AppCompatActivity {

    private ListView listview;
    private static Integer EVENTID;
    private ArrayList<Event> events = new ArrayList<Event>();
    private EventsViewAdapter adapter;
    private static boolean dataReady=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        ApplicationHandler handler = ((ApplicationHandler) getApplicationContext());
        //Verbindungsaufbau zur SQLite Datenbank
        try {
            SOAPEvents task = new SOAPEvents(handler);
            task.execute().get(5000, TimeUnit.MILLISECONDS);
        }
        catch (Exception e){
            alert();
        }
        events = handler.getEvents();
        //Listview laden


        setContentView(R.layout.activity_events_list);
        listview = (ListView) findViewById(R.id.events_list);

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
                EVENTID = Integer.parseInt(tv.getText().toString());
                tv = (TextView) view.findViewById(R.id.last);


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
                                Intent intent = new Intent(EventsList.this, EventView.class);
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
                break;
            case R.id.action_settings:

        }

            return super.onOptionsItemSelected(item);
    }
    private void alert(){
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
                EventsList.this );

        // set title
        alertDialogBuilder.setTitle("Fehler aufgetreten");

        // set dialog message
        alertDialogBuilder
                .setMessage("Ups. Es ist ein Fehler aufgetreten. Wir bitten um Entschuldigung. Wir befinden uns im Beta-Stadium")
                .setCancelable(false)

                .setNeutralButton("Entschuldigung angenommen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(EventsList.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

        // create alert dialog
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
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