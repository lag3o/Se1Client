package com.gruppe2.Client.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.myles.projecto.R;
import com.gruppe2.Client.Database.ApplicationHandler;
import com.gruppe2.Client.Helper.Parser;
import com.gruppe2.Client.Objects.Event;

import static com.gruppe2.Client.Helper.Constants.DESCR;
import static com.gruppe2.Client.Helper.Constants.LOC;
import static com.gruppe2.Client.Helper.Constants.NAME;
import static com.gruppe2.Client.Helper.Constants.START;
/**
Diese Klasse erzeugt eine Ansicht über die Veranstaltungsdetails

 @author  Myles Sutholt

 */
public class EventDetail extends ActionBarActivity {
    private Event event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        event = ((ApplicationHandler) getApplicationContext()).getEvent();
        try {
            TextView text = (TextView) findViewById(R.id.name);
            text.setText(event.getName());
            ((TextView) findViewById(R.id.name)).setText(event.getName());
            ((TextView) findViewById(R.id.time)).setText((new Parser().DateToStringDate(event.getDateStart())) + " - " + (new Parser().DateToStringDate(event.getDateEnd())));
            ((TextView) findViewById(R.id.description)).setText(event.getDescription());
            //((TextView) findViewById(R.id.contact)).setText(event.getUser);
        }
        catch (Exception e){
            Log.d("EventDetail setText", e.toString());
            alert();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
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
                ((ApplicationHandler) getApplicationContext()).setEvent(event);
                intent = new Intent(EventDetail.this, CreateSessionView.class);
                startActivity(intent);
                break;
            case R.id.action_edit:
                ((ApplicationHandler) getApplicationContext()).setEvent(event);
                intent = new Intent(EventDetail.this, CreateEvent.class);
                startActivity(intent);
                break;
            case R.id.action_push:
                //Push Mitteilung abfackeln
                break;
            case R.id.action_delete:
                ((ApplicationHandler) getApplicationContext()).getDatasource().deleteEvent(event);
                break;
            case R.id.action_settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void alert(){
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
                EventDetail.this );

        // set title
        alertDialogBuilder.setTitle("Fehler aufgetreten");

        // set dialog message
        alertDialogBuilder
                .setMessage("Ups. Es ist ein Fehler aufgetreten. Wir bitten um Entschuldigung. Wir befinden uns im Beta-Stadium")
                .setCancelable(false)

                .setNeutralButton("Entschuldigung angenommen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(EventDetail.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

        // create alert dialog
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
