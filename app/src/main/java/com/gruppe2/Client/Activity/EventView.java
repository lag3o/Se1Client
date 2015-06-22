package com.gruppe2.Client.Activity;

import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TabHost;

import com.example.myles.projecto.R;
import com.gruppe2.Client.Database.ApplicationHandler;
import com.gruppe2.Client.Database.EventsDataSource;
import com.gruppe2.Client.Helper.Parser;
import com.gruppe2.Client.Objects.Event;
import com.gruppe2.Client.Objects.Session;
import com.gruppe2.Client.SOAP.SOAPDropEvent;
import com.gruppe2.Client.SOAP.SOAPEvent;
import com.gruppe2.Client.SOAP.SOAPPush;
import com.gruppe2.Client.SOAP.SOAPSessions;

import java.sql.Array;
import java.util.ArrayList;
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
    private ApplicationHandler handler;
    private Bundle b;
    private String value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);

        Bundle bundle = getIntent().getExtras();
        handler = ((ApplicationHandler) getApplicationContext());
        //datasource = (handler.getDatasource());
        if (handler.getEvent() == null) {
            id = bundle.getInt("ID");
            try {
                handler.setEvent(new Event(id));
                SOAPEvent task = new SOAPEvent(handler, id);
                task.execute().get(5000, TimeUnit.MILLISECONDS);

            } catch (Exception e) {

            }
        }
        else{
            id = handler.getEvent().getEventID();
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

        //Zunächst wird ein Tab mit den Veranstaltungsinformationen erstellt
        Intent intent = new Intent().setClass(this, EventDetail.class);
        intent.putExtras(b); //Put your id to your next Intent
        TabHost.TabSpec spec = tabHost.newTabSpec("Details").setIndicator("Info", getResources().getDrawable(R.drawable.abc_tab_indicator_material));
        spec.setContent(intent);

        tabHost.addTab(spec);

        //Anschließend n Tabs für jeden Tag der Veranstaltung mit entsprechendem Ablaufplan
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
        if (handler.isAdmin()) {
            getMenuInflater().inflate(R.menu.menu_event_view, menu);
        }
        else {
            getMenuInflater().inflate(R.menu.menu_event, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_edit:
                intent = new Intent(EventView.this, CreateEvent.class);
                startActivity(intent);
                break;
            case R.id.action_push:
                try {
                    createMessage();
                }
                catch (Exception e){
                    Log.d("sendPush", e.toString());
                    alert("Nachricht konnte nicht gesendet werden.");
                }
                break;
            case R.id.action_delete:
                try {
                    //SOAPDropEvent task = new SOAPDropEvent(handler);
                    //task.execute();
                    handler.getDatasource().deleteEvent(event);
                }
                catch (Exception e){
                    Log.d("dropEvent", e.toString());
                    alert("Veranstaltung konnte nicht gelöscht werden.");
                }
                finally {
                    intent = new Intent(EventView.this, MainActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.action_showPush:
                Log.d("Show_all", "TOP");
                intent = new Intent(EventView.this, Messages.class);
                startActivity(intent);
                break;
            case R.id.action_all:
                Log.d("Show_all", "TOP");
                intent = new Intent(EventView.this, EventsList.class);
                startActivity(intent);
                break;
            case R.id.action_show_my:
                Log.d("Show_my", "TOP");
                intent = new Intent(EventView.this, MyEvents.class);
                startActivity(intent);
                break;
        }
        //noinspection SimplifiableIfStatement
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
                        startActivity(intent);
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        try {
            alertDialog.show();
        }
        catch (Exception e){

        }
    }

    private void createMessage(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Push Nachricht senden");
        alert.setMessage("Bitte Nachricht eingeben");

// Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                value = input.getText().toString();
                Log.d("MessageCreate", value + " ");
                try{
                    SOAPPush sendPush = new SOAPPush(value, handler);
                    sendPush.execute().get(5000, TimeUnit.MILLISECONDS);
                }
                catch (Exception e){
                    Log.d("sendPush", e.toString());
                    alert("Nachricht konnte nicht gesendet werden.");
                }
                // Do something with value!
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }
    private void alert(String msg){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                EventView.this );

        // set title
        alertDialogBuilder.setTitle("Fehler aufgetreten");

        // set dialog message
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)

                .setNeutralButton("Entschuldigung angenommen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        try {
            alertDialog.show();
        }
        catch (Exception e){

        }
    }


}