package com.gruppe2.Client.Activity;

import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.example.myles.projecto.R;
import com.gruppe2.Client.Database.ApplicationHandler;
import com.gruppe2.Client.Database.EventsDataSource;
import com.gruppe2.Client.Exceptions.ParamMissingException;
import com.gruppe2.Client.Helper.Parser;
import com.gruppe2.Client.Objects.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.gruppe2.Client.Helper.Constants.DESCR;
import static com.gruppe2.Client.Helper.Constants.END;
import static com.gruppe2.Client.Helper.Constants.NAME;
import static com.gruppe2.Client.Helper.Constants.START;

/**
Diese Klasse erzeugt eine Tabübersicht. Dabei wird für jeden Tag ein eigener Tab erzeugt, welchem das Datum
 übergeben wird.

 @author  Myles Sutholt

 */
public class CreateSessionView extends TabActivity {

    private EventsDataSource datasource;
    private Event event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);

        Bundle b = getIntent().getExtras();

        ApplicationHandler handler = ((ApplicationHandler) getApplicationContext());
        event = handler.getEvent();
        Date dateEnd = new Date();
        Date dateStart = new Date();
        /*
        Prüfung ob in der Application Class ein Event-Objekt vorhanden ist. Falls nicht wird eins erzeugt.
        Es erfolgt eine Fehlermeldung bei Exception
         */
        if (event == null) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
                simpleDateFormat.setLenient(false);
                dateEnd = simpleDateFormat.parse((b.getString(END) + " 23:59"));
                dateStart = simpleDateFormat.parse((b.getString(START) + " 00:00"));

                event = new Event(b.getString(NAME), dateStart, dateEnd, b.getString(DESCR));
            } catch (ParamMissingException exception) {
                alert();
            } catch (ParseException e) {
                alert();
            }
            handler.setEvent(event);
        }
        else{
            dateEnd = event.getDateEnd();
            dateStart = event.getDateStart();
            b = new Bundle();
        }


        /*
        Erzeugt n Tabs. Wobei n die Anzahl an Tagen ist, die eine Veranstaltung dauert und übergibt jeweils das Datum
        an den Tab, damit die CreateSession/EditSession weiß zu welchem Tag sie gehört.

        Jeder Tab wird mit dem Tag und dem Monat beschriftet
         */
        TabHost tabHost = getTabHost();

        Calendar end = Calendar.getInstance();
        end.setTime(dateEnd);
        Calendar start = Calendar.getInstance();
        start.setTime(dateStart);
        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {


            b.putString("Date", (new Parser().DateToStringDate(start.getTime())));
            Intent intent1;
            if (event.getEventID() == -1) {
                intent1 = new Intent().setClass(this, CreateSession.class);
            }
            else{
                intent1 = new Intent().setClass(this, EditSession.class);
            }
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
    private void alert(){
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
                CreateSessionView.this );

        // set title
        alertDialogBuilder.setTitle("Fehler aufgetreten");

        // set dialog message
        alertDialogBuilder
                .setMessage("Ups. Es ist ein Fehler aufgetreten. Wir bitten um Entschuldigung. Wir befinden uns im Beta-Stadium")
                .setCancelable(false)

                .setNeutralButton("Entschuldigung angenommen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(CreateSessionView.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

        // create alert dialog
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        try {
            alertDialog.show();
        }
        catch (Exception e){

        }
    }
}
