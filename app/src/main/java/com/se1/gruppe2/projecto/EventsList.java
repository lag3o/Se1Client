package com.se1.gruppe2.projecto;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myles.projecto.R;
import com.se1.gruppe2.mock.MockEvents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;


public class EventsList extends ActionBarActivity {

    private Button bt;
    private ListView al;
    private ArrayList<String> activityArr;
    private ArrayAdapter<String> arrAdap;
    private TextView tv;
    private MockEvents server;
    private EventsDataSource datasource;
    private static int POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);

        //Verbindungsaufbau zur SQLite Datenbank
        datasource = new EventsDataSource(this);
        datasource.open();

        //Listview laden
        al = (ListView) findViewById(R.id.activityList);

        //Daten abfragen und konvertieren

        activityArr = new ArrayList<String>();
        HashMap<Integer, String> events = datasource.getAllNames();
        Iterator it = events.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            activityArr.add((String) pair.getValue());
            it.remove();
        }

        //ArrayAdapter initialisieren
        arrAdap = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, activityArr);

        //ArrayList mit ArrayAdapter verbinden
        al.setAdapter(arrAdap);

        //ArrayAdaper auf Datenänderung hinweisen
        arrAdap.notifyDataSetChanged();
        al.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                    POSITION = position;


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
                                    b.putInt("ID", POSITION); //Your id
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
        getMenuInflater().inflate(R.menu.menu_activity_list, menu);
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
    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }
}