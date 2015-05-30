package com.se1.gruppe2.projecto;

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


public class myEvents extends ActionBarActivity {

    private Button bt;
    private ListView al;
    private ArrayList<String> activityArr;
    private ArrayAdapter<String> arrAdap;
    private TextView tv;
    private MockEvents server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);
        al = (ListView) findViewById(R.id.activityList);
        server = new MockEvents();
        activityArr = server.getAl();
        arrAdap = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, activityArr);
        al.setAdapter(arrAdap);
        arrAdap.notifyDataSetChanged();
        al.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                Intent intent = new Intent(myEvents.this, SessionList.class);
                Bundle b = new Bundle();
                b.putInt("ID", position); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
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
}