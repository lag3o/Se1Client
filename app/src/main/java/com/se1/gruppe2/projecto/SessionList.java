package com.se1.gruppe2.projecto;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.myles.projecto.R;

import java.util.ArrayList;
import java.util.HashMap;


import static com.se1.gruppe2.projecto.Constants.NAME;
import static com.se1.gruppe2.projecto.Constants.START;
import static com.se1.gruppe2.projecto.Constants.LOC;
import static com.se1.gruppe2.projecto.Constants.DESCR;

public class SessionList extends ActionBarActivity {


    private EventsDataSource datasource;
    private static ArrayList<HashMap<String, String>> sessions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_list);

        // ListView laden
        ListView listView=(ListView)findViewById(R.id.session_list);

        //Übergebene Parameter entpacken
        Bundle b = getIntent().getExtras();
        Integer id = b.getInt("ID");


        //Datenbankverbindung aufbauen
        //datasource = new EventsDataSource(this);
        //datasource.open();
        DatabaseHandler handler = ((DatabaseHandler) getApplicationContext());
        datasource = (handler.getDatasource());


        //Daten abfragen
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
                b.putString(START, (sessions.get(position).get(START)));
                b.putString(LOC, (sessions.get(position).get(LOC)));
                b.putString(NAME, (sessions.get(position).get(NAME)));
                b.putString(DESCR, (sessions.get(position).get(DESCR)));
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_session_list, menu);
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
