package com.gruppe2.Client.Activity;

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

import com.example.myles.projecto.R;
import com.gruppe2.Client.Database.DatabaseHandler;
import com.gruppe2.Client.Database.EventsDataSource;
import com.gruppe2.Client.Helper.ListViewAdapter;
import com.gruppe2.Client.Helper.Parser;
import com.gruppe2.Client.Objects.Event;
import com.gruppe2.Client.Objects.Session;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Date;


import static com.gruppe2.Client.Helper.Constants.NAME;
import static com.gruppe2.Client.Helper.Constants.NAMESPACE;
import static com.gruppe2.Client.Helper.Constants.SOAP_ACTION;
import static com.gruppe2.Client.Helper.Constants.START;
import static com.gruppe2.Client.Helper.Constants.LOC;
import static com.gruppe2.Client.Helper.Constants.DESCR;
import static com.gruppe2.Client.Helper.Constants.URL;
/**@author  Myles Sutholt
    Diese Klasse zeigt einen Ablaufplan einer Veranstaltung
 */
public class SessionList extends AppCompatActivity {

    private static Integer id;
    private static Event event;
    private EventsDataSource datasource;
    private ArrayList<Session> sessions = new ArrayList<Session>();
    private ListViewAdapter adapter;
    private static boolean dataReady=false;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_list);

        //Übergebene Parameter entpacken
        bundle = getIntent().getExtras();
        this.id = bundle.getInt("ID");

        //Datenbankverbindung aufbauen
        //try{
        try {
            AsyncEvent task = new AsyncEvent();
            task.execute();
        }
        catch (Exception e){

        }

        while(!dataReady){}

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

    private void showList(){
        Log.d("List", bundle.getString("Date")+ " Name:" + event.getName()+ " Session:" + event.getSessions().size());
        Date date = (new Parser().StringToDate(bundle.getString("Date")));
        for (int i = 0; i<event.getSessions().size(); i++){
            if (event.getSessions().get(i).getDateStart().before(date)){
                sessions.add(event.getSessions().get(i));
            }
        }

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

    private void getEvent() {

        String method_name = "getEvent";

        SoapObject request = new SoapObject(NAMESPACE, method_name);

        PropertyInfo pi = new PropertyInfo();

        request.addProperty("eventID", 2);

        /*
         * Set the web service envelope
         *
         * */
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        /*
         * Call the web service and retrieve result ... how luvly <3
         *
         * */
        try {
            androidHttpTransport.debug = true;
            androidHttpTransport.call(SOAP_ACTION, envelope, null);
            Log.d("dump Request: ", androidHttpTransport.requestDump);
            Log.d("dump response: ", androidHttpTransport.responseDump);
            SoapObject response = (SoapObject) envelope.getResponse();
            event = (Event) response.getProperty(0);

        } catch (Exception e) {
            e.printStackTrace();
            getDatabase();
        }
        finally {
            showList();
        }

    }
    private void getDatabase(){
        DatabaseHandler handler = ((DatabaseHandler) getApplicationContext());
        datasource = (handler.getDatasource());

        //Daten abfragen für favorisierte Sessions
        event = datasource.getEvent(id);
    }


    public class AsyncEvent extends AsyncTask<String, Void, Void> {



        protected Void doInBackground(String... params) {
            getEvent();
            dataReady = true;
            return null;
        }
        protected void onPostExecute(Void result) {
            dataReady = true;
            Log.i("Event Data ", "onPostExecute");        }

        @Override
        protected void onPreExecute() {
            Log.i("Create User ", "onPreExecute");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("Create User ", "onProgressUpdate");
        }
    }

}
