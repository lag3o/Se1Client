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
import com.se1.gruppe2.mock.MockSessions;

import java.util.ArrayList;
import java.util.HashMap;



public class SessionList extends ActionBarActivity {


    private ArrayList<HashMap<String, String>> list;
    private MockSessions ms;
    private EventsDataSource datasource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_list);

        // ListView laden
        ListView listView=(ListView)findViewById(R.id.listView1);

        //Übergebene Parameter entpacken
        Bundle b = getIntent().getExtras();
        Integer id = b.getInt("id");

        //Datenbankverbindung aufbauen
        ms = new MockSessions(id);
        datasource = new EventsDataSource(this);
        datasource.open();

        //Daten abfragen
        list= ms.getAl();
        list = datasource.getSessions(id);

        //ListViewAdapter initialisieren
        ListViewAdapter adapter=new ListViewAdapter(this, list);

        //ListView befüllen
        listView.setAdapter(adapter);

        //ListView clickable machen
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {
                int pos = position + 1;
                Intent intent = new Intent(SessionList.this, SessionView.class);
                Bundle b = new Bundle();
                b.putString("Time", "18:00 Uhr");
                b.putString("Name", "Test");
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
