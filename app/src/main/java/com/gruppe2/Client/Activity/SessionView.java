package com.gruppe2.Client.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import static com.gruppe2.Client.Helper.Constants.NAME;
import static com.gruppe2.Client.Helper.Constants.START;
import static com.gruppe2.Client.Helper.Constants.LOC;
import static com.gruppe2.Client.Helper.Constants.DESCR;

import com.example.myles.projecto.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
/**
Diese Klasse zeigt eine Detailansicht einer Veranstaltung


 @author  Myles Sutholt
 */
public class SessionView extends AppCompatActivity {
    private static double longitude;
    private static double latitude;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_view);
        Bundle b = getIntent().getExtras();
        ((TextView) findViewById(R.id.name)).setText(b.getString(NAME));
        ((TextView) findViewById(R.id.time)).setText(b.getString(START));
        ((TextView) findViewById(R.id.description)).setText(b.getString(DESCR));
        ((TextView) findViewById(R.id.location)).setText(b.getString(LOC));
        flag = false;
        ImageButton btn = (ImageButton) findViewById(R.id.navigation);

        List<Address> addresses = null;

        //Prüft ob eine Netzwerkverbidnung vorhanden ist. Falls ja wird der geocode (Latitude und longitude) abgerufen
        if(isOnline()) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                addresses = geocoder.getFromLocationName(b.getString(LOC), 1);
                if (addresses.size() > 0) {
                    latitude = addresses.get(0).getLatitude();
                    longitude = addresses.get(0).getLongitude();
                    flag = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Startet Maps, wenn geocode ermittelt werden konnte und der Nutzer auf den Button klickt. Falls nicht erscheint eine Fehlermeldung
        btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (flag == true){
                    Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");

                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                }
                else{
                    alert();
                }


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_session_view, menu);
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
                SessionView.this );

        // set title
        alertDialogBuilder.setTitle("Fehler aufgetreten");

        // set dialog message
        alertDialogBuilder
                .setMessage("Es ist keine Internetverbindung vorhanden. Daher kann Google Maps nicht aufgerufen werden")
                .setCancelable(false)

                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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
    //Prüfung ob Netzwerkverbindung vorhanden ist.
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
