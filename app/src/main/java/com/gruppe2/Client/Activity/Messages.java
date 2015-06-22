package com.gruppe2.Client.Activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.myles.projecto.R;
import com.gruppe2.Client.Database.ApplicationHandler;

import java.util.ArrayList;

/*
 Eine Klasse um alle eingehenden PushNachrichten für eine spezifische Veranstaltung anzuzeigen
 Funtkioniert derzeit nur für selbst erstellte Nachrichten, weil der Server die EventID nicht mitsendet.

 @author  Myles Sutholt
 */
public class Messages extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesages);

        // Get ListView object from xml
        ListView listView = (ListView) findViewById(R.id.messages);

        // Defined Array values to show in ListView
        ArrayList<String> messages = new ArrayList<String>();
        ApplicationHandler handler = ((ApplicationHandler)getApplicationContext());
        messages = ((ApplicationHandler) getApplicationContext()).getDatasource().getMessages(handler.getEvent().getEventID());


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, messages);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mesages, menu);
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
