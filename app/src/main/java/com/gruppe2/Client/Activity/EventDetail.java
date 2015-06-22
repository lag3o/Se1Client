package com.gruppe2.Client.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myles.projecto.R;
import com.gruppe2.Client.Database.ApplicationHandler;
import com.gruppe2.Client.Helper.Parser;
import com.gruppe2.Client.Objects.Event;
import com.gruppe2.Client.SOAP.SOAPPush;

import java.util.concurrent.TimeUnit;

import static com.gruppe2.Client.Helper.Constants.DESCR;
import static com.gruppe2.Client.Helper.Constants.LOC;
import static com.gruppe2.Client.Helper.Constants.NAME;
import static com.gruppe2.Client.Helper.Constants.START;
/**
Diese Klasse erzeugt eine Ansicht über die Veranstaltungsdetails

 @author  Myles Sutholt

 */
public class EventDetail extends AppCompatActivity {
    private Event event;
    private String value;
    private ApplicationHandler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        handler = ((ApplicationHandler) getApplicationContext());
        event = handler.getEvent();
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
         //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
     }
    private void alert(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                EventDetail.this );

        // set title
        alertDialogBuilder.setTitle("Fehler aufgetreten");

        // set dialog message
        alertDialogBuilder
                .setMessage("Ups. Es ist ein Fehler aufgetreten. Wir bitten um Entschuldigung. Wir befinden uns im Beta-Stadium")
                .setCancelable(false)

                .setNeutralButton("Entschuldigung angenommen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(EventDetail.this, EventsList.class);
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

}
