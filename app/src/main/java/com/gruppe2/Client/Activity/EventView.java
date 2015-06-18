package com.gruppe2.Client.Activity;

import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.example.myles.projecto.R;
import com.gruppe2.Client.Database.ApplicationHandler;
import com.gruppe2.Client.Database.EventsDataSource;
import com.gruppe2.Client.Helper.Parser;
import com.gruppe2.Client.Objects.Event;
import com.gruppe2.Client.SOAP.SOAPDropEvent;
import com.gruppe2.Client.SOAP.SOAPEvent;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.gruppe2.Client.Helper.Constants.DESCR;
import static com.gruppe2.Client.Helper.Constants.END;
import static com.gruppe2.Client.Helper.Constants.NAME;
import static com.gruppe2.Client.Helper.Constants.START;

/**
Diese Klasse erzeugt eine Ansicht der Veranstaltung. Hierbei wird für jeden Tag der Veranstaltung ein
 Tab mit einem Ablaufplan erzeugt sowie ein "Info"-Tab für die allgemeinen Veranstaltungsinformationen

 @author  Myles Sutholt

 */
public class EventView extends TabActivity {

    private Event event;
    private int id;
    private EventsDataSource datasource;
    private boolean dataReady;
    private ApplicationHandler handler;
    private Bundle b;
    private boolean deleteEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);

        handler = ((ApplicationHandler) getApplicationContext());
        //datasource = (handler.getDatasource());
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("ID");

        try {
            handler.setEvent(new Event(id));
            SOAPEvent task = new SOAPEvent(handler);
            task.execute().get(5000, TimeUnit.MILLISECONDS);

        }
        catch (Exception e){

        }
        event = handler.getEvent();

        TabHost tabHost = getTabHost();
        b = new Bundle();
        try {
            b.putInt("ID", id);
            b.putString(START, ((new Parser().DateToStringDate(event.getDateStart()))));
            b.putString(NAME, (event.getName()));
            b.putString(DESCR, (event.getDescription()));
            b.putString(END, ((new Parser().DateToStringDate(event.getDateEnd()))));
        }
        catch (Exception e){
            Log.d("EventView Bundle", e.toString());
            alert();
        }
        Intent intent = new Intent().setClass(this, EventDetail.class);
        intent.putExtras(b); //Put your id to your next Intent
        TabHost.TabSpec spec = tabHost.newTabSpec("Details").setIndicator("Info", getResources().getDrawable(R.drawable.abc_tab_indicator_material));
        spec.setContent(intent);

        tabHost.addTab(spec);


        try {
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
                String day = start.get(Calendar.DAY_OF_MONTH) + "." + (start.get(Calendar.MONTH)+1);
                TabHost.TabSpec spec1 = tabHost.newTabSpec(day).setIndicator(day, getResources().getDrawable(R.drawable.abc_tab_indicator_material));
                spec1.setContent(intent1);
                tabHost.addTab(spec1);
            }
            tabHost.setCurrentTab(0);
        }
        catch (Exception e){
            Log.d("EventView TabHost", e.toString());
            alert();
        }
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

        Intent intent;
        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_addSession:
                intent = new Intent(EventView.this, EditSession.class);
                startActivity(intent);
                break;
            case R.id.action_edit:
                intent = new Intent(EventView.this, CreateEvent.class);
                startActivity(intent);
                break;
            case R.id.action_push:
                //Push Mitteilung abfackeln
                break;
            case R.id.action_delete:
                try {
                    deleteEvent = true;
                    //SOAPDropEvent task = new SOAPDropEvent(handler);
                    //task.execute();
                    datasource.deleteEvent(event);
                }
                catch (Exception e){

                }
                finally {
                    intent = new Intent(EventView.this, MainActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.action_settings:
                break;

        }

        return super.onOptionsItemSelected(item);
    }
    private void alert(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                EventView.this );

        // set title
        alertDialogBuilder.setTitle("Fehler aufgetreten");

        // set dialog message
        alertDialogBuilder
                .setMessage("Ups. Es ist ein Fehler aufgetreten. Wir bitten um Entschuldigung. Wir befinden uns im Beta-Stadium")
                .setCancelable(false)

                .setNeutralButton("Entschuldigung angenommen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(EventView.this, EventsList.class);
                        intent.putExtras(b); //Put your id to your next Intent
                        startActivity(intent);
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


}