package com.gruppe2.Event.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.myles.projecto.R;
import com.gruppe2.Event.Database.DatabaseHandler;
import com.gruppe2.Event.Database.EventsDataSource;
import com.gruppe2.Event.Helper.ListViewAdapter;
import com.gruppe2.Event.Helper.Parser;
import com.gruppe2.Event.Objects.Session;

import java.util.ArrayList;
import java.util.HashMap;


import static com.gruppe2.Event.Helper.Constants.NAME;
import static com.gruppe2.Event.Helper.Constants.START;
import static com.gruppe2.Event.Helper.Constants.LOC;
import static com.gruppe2.Event.Helper.Constants.DESCR;

public class SessionList extends AppCompatActivity {

    Integer id;
    private EventsDataSource datasource;
    private static ArrayList<Session> sessions;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_list);

        // ListView laden
        ListView listView=(ListView)findViewById(R.id.session_list);

        //Übergebene Parameter entpacken
        bundle = getIntent().getExtras();
        this.id = bundle.getInt("ID");


        //Datenbankverbindung aufbauen
        //datasource = new EventsDataSource(this);
        //datasource.open();
        DatabaseHandler handler = ((DatabaseHandler) getApplicationContext());
        datasource = (handler.getDatasource());

        //Daten abfragen für favorisierte Sessions
        sessions = datasource.getSessions(id);

        //ListViewAdapter initialisieren
        ListViewAdapter adapter=new ListViewAdapter(this, sessions);

        //ListView befüllen
        listView.setAdapter(adapter);

        //ListView clickable machen
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {


                Intent intent = new Intent(SessionList.this, SessionView.class);
                Bundle b = new Bundle();
                b.putString(START, ((new Parser().TimeToString(sessions.get(position).getDateStart()))));
                b.putString(LOC, (sessions.get(position).getLocation()));
                b.putString(NAME, (sessions.get(position).getName()));
                b.putString(DESCR, (sessions.get(position).getDescription()));
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_session_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;
        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_addSession:
                intent = new Intent(SessionList.this, EditSession.class);
                startActivity(intent);
            case R.id.action_edit:
                intent = new Intent(SessionList.this, CreateEvent.class);
                intent.putExtras(bundle);
                startActivity(intent);
            case R.id.action_push:
                //Push Mitteilung abfackeln
            case R.id.action_deleteEvent:
                //Veranstaltung löschen
            case R.id.action_settings:

        }

        return super.onOptionsItemSelected(item);
    }

}
