package com.gruppe2.Client.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.myles.projecto.R;
import com.gruppe2.Client.Database.ApplicationHandler;
import com.gruppe2.Client.Database.EventsDataSource;
import com.gruppe2.Client.Helper.ListViewAdapter;
import com.gruppe2.Client.Helper.Parser;
import com.gruppe2.Client.Objects.Event;
import com.gruppe2.Client.Objects.Session;
import com.gruppe2.Client.SOAP.SOAPPush;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;


import static com.gruppe2.Client.Helper.Constants.NAME;
import static com.gruppe2.Client.Helper.Constants.NAMESPACE;
import static com.gruppe2.Client.Helper.Constants.SOAP_ACTION;
import static com.gruppe2.Client.Helper.Constants.START;
import static com.gruppe2.Client.Helper.Constants.LOC;
import static com.gruppe2.Client.Helper.Constants.DESCR;
import static com.gruppe2.Client.Helper.Constants.URL;
/**
Diese Klasse zeigt einen Ablaufplan einer Veranstaltung

 @author  Myles Sutholt
 */
public class SessionList extends AppCompatActivity {

    private Integer id;
    private  Event event;
    private ArrayList<Session> sessions = new ArrayList<Session>();
    private ListViewAdapter adapter;
    private Bundle bundle;
    private String value;
    private ApplicationHandler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_list);

        // Daten von der Application Class holen
        handler = ((ApplicationHandler) getApplicationContext());
        event = handler.getEvent();

        for (int i = 0; i<event.getSessions().size(); i++){
            Log.d("EventInfo:", event.getName() +"; " + event.getSessions().get(i).getName());
        }
        showList();
        //ListViewAdapter initialisieren

        ListView listView=(ListView)findViewById(R.id.session_list);
        adapter=new ListViewAdapter(this, sessions);

        //ListView befüllen
        listView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        //ListView clickable machen
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {


                Intent intent = new Intent(SessionList.this, SessionView.class);
                Bundle b = new Bundle();
                b.putString(START, ((new Parser().DateToString(sessions.get(position).getDateStart()))));
                b.putString(LOC, (sessions.get(position).getLocation()));
                b.putString(NAME, (sessions.get(position).getName()));
                b.putString(DESCR, (sessions.get(position).getDescription()));
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
            }

        });


    }
    /*
     Algorithmus zur Bestimmung welche Sessions zu diesem Tag gehören.
     Im Best Case kann man von einer sortierten Liste ausgehen und könnte nach der ersten Tagüberschreitung stoppen.
     Hier wird vom Worst Case ausgegangen.
      */
    private void showList(){
        bundle = getIntent().getExtras();
        Log.d("List", bundle.getString("Date")+ " Name:" + event.getName()+ " Session:" + event.getSessions().size());
        Date date = (new Parser().StringToDate(bundle.getString("Date") + " 00:00"));
        Date dateV = (new Parser().StringToDate(bundle.getString("DateV") + " 00:00"));
        for (int i = 0; i<event.getSessions().size(); i++){
            Log.d("Session Date", new Parser().DateToStringDate(event.getSessions().get(i).getDateStart()) + " "+ new Parser().DateToStringDate(date));
            if (event.getSessions().get(i).getDateStart().before(date)) {
                if (event.getSessions().get(i).getDateStart().after(dateV) ||event.getSessions().get(i).getDateStart().equals(dateV) ){
                    sessions.add(event.getSessions().get(i));
                    Log.d("Session Name:", event.getSessions().get(i).getName());
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        //Entscheidung ob das Admin-Menü gewählt wird oder das normale Usermenü
       /*
        if (handler.isAdmin()) {
            getMenuInflater().inflate(R.menu.menu_event_view, menu);
        }
        else {
            getMenuInflater().inflate(R.menu.menu_event, menu);
        }
        */
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_addSession){
            Intent intent = new Intent(SessionList.this, CreateSession.class);
            intent.putExtras(bundle);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

}
