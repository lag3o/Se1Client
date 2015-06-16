package com.gruppe2.Client.Activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.example.myles.projecto.R;
import com.gruppe2.Client.Database.ApplicationHandler;
import com.gruppe2.Client.Database.EventsDataSource;
import com.gruppe2.Client.Helper.Parser;
import com.gruppe2.Client.Objects.Event;
import com.gruppe2.Client.Objects.Session;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Calendar;
import java.util.Date;

import static com.gruppe2.Client.Helper.Constants.DESCR;
import static com.gruppe2.Client.Helper.Constants.END;
import static com.gruppe2.Client.Helper.Constants.NAME;
import static com.gruppe2.Client.Helper.Constants.NAMESPACE;
import static com.gruppe2.Client.Helper.Constants.SOAP_ACTION;
import static com.gruppe2.Client.Helper.Constants.START;
import static com.gruppe2.Client.Helper.Constants.URL;

public class EventView extends TabActivity {

    private Event event;
    private int id;
    private EventsDataSource datasource;
    private boolean dataReady;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);


        ApplicationHandler handler = ((ApplicationHandler) getApplicationContext());
        datasource = (handler.getDatasource());
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("ID");

        try {
            AsyncEvent task = new AsyncEvent();
            task.execute();
        }
        catch (Exception e){

        }

        while(!dataReady){}

        TabHost tabHost = getTabHost();
        Bundle b = new Bundle();
        b.putString(START, ((new Parser().DateToStringDate(event.getDateStart()))));
        b.putString(NAME, (event.getName()));
        b.putString(DESCR, (event.getDescription()));
        b.putString(END, ((new Parser().DateToStringDate(event.getDateEnd()))));
        Intent intent = new Intent().setClass(this, EventDetail.class);
        intent.putExtras(b); //Put your id to your next Intent
        TabHost.TabSpec spec = tabHost.newTabSpec("Details").setIndicator("Info", getResources().getDrawable(R.drawable.abc_tab_indicator_material));
        spec.setContent(intent);

        tabHost.addTab(spec);



        Calendar end = Calendar.getInstance();
        end.setTime(event.getDateEnd());
        Calendar start = Calendar.getInstance();
        start.setTime(event.getDateStart());
        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {

            b = new Bundle();
            start.add(Calendar.DATE, 1);
            Date dateBundle = start.getTime();
            b.putString("Date", (new Parser().DateToStringDate(dateBundle)));
            start.add(Calendar.DATE, -1);
            dateBundle = start.getTime();
            b.putString("DateV", (new Parser().DateToStringDate(dateBundle)));
            Intent intent1 = new Intent().setClass(this, SessionList.class);
            intent1.putExtras(b);
            String day = start.get(Calendar.DAY_OF_MONTH) + "." + start.get(Calendar.MONTH);
            TabHost.TabSpec spec1 = tabHost.newTabSpec(day).setIndicator(day, getResources().getDrawable(R.drawable.abc_tab_indicator_material));
            spec1.setContent(intent1);
            tabHost.addTab(spec1);
        }
            tabHost.setCurrentTab(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_view, menu);
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
            java.util.Vector<SoapObject> responseSession = (java.util.Vector<SoapObject>) envelope.getResponse();

            /** lists property count */
            if (response.hasProperty("id")) {
                event.setEventID(Integer.parseInt(response.getPropertyAsString("id")));
            }
            if (response.hasProperty("name")) {
                event.setName(response.getPropertyAsString("name"));
            }

            if (response.hasProperty("dateEnd")) {
                event.setDateEnd((new Parser().StringToTempDate(response.getPropertyAsString("dateEnd"))));
            }

            if (response.hasProperty("description")) {
                event.setDescription(response.getPropertyAsString("description"));
            }

            if (response.hasProperty("dateStart")) {
                event.setDateStart((new Parser().StringToTempDate(response.getPropertyAsString("dateStart"))));
            }
            /** loop */
            if (response != null) {
                for (SoapObject cs : responseSession) {
                    /** temp PhongTro object */
                    Session tempObj = new Session();

                    if (cs.hasProperty("id")) {
                        tempObj.setSessionID(Integer.parseInt(cs.getPropertyAsString("id")));
                    }
                    if (cs.hasProperty("name")) {
                        tempObj.setName(cs.getPropertyAsString("name"));
                    }

                    if (cs.hasProperty("dateEnd")) {
                        tempObj.setDateEnd((new Parser().StringToTempDate(cs.getPropertyAsString("dateEnd"))));
                    }

                    if (cs.hasProperty("description")) {
                        tempObj.setDescription(cs.getPropertyAsString("description"));
                    }

                    if (cs.hasProperty("dateStart")) {
                        tempObj.setDateStart((new Parser().StringToTempDate(cs.getPropertyAsString("dateStart"))));
                    }
                    if (cs.hasProperty("location")) {
                        tempObj.setLocation((cs.getPropertyAsString("location")));
                    }
                    if (cs.hasProperty("plz")) {
                        tempObj.setLocation((cs.getPropertyAsString("plz")));
                    }
                    Log.d("LOG",cs.getProperty(0).toString());

                    /** Adding temp PhongTro object to list */
                    event.getSessions().add(tempObj);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            /** if an error handled events setting null */
            event = new Event();
            getDatabase();
        }
        finally{
            ApplicationHandler handler = ((ApplicationHandler) getApplicationContext());
            handler.setEvent(event);
        }

    }
    private void getDatabase(){
        datasource = ((ApplicationHandler) getApplicationContext()).getDatasource();

        //Daten abfragen für favorisierte Sessions
        event = datasource.getEvent(id);
        Log.d("Sessions", "Anzahl: " + event.getSessions().size() );
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