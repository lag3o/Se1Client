package com.gruppe2.Client.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myles.projecto.R;
import com.gruppe2.Client.Database.EventsDataSource;
import com.gruppe2.Client.Database.ApplicationHandler;
import com.gruppe2.Client.Objects.Event;

import org.jboss.aerogear.android.unifiedpush.RegistrarManager;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import static com.gruppe2.Client.Helper.Constants.NAMESPACE;
import static com.gruppe2.Client.Helper.Constants.URL;
import static com.gruppe2.Client.Helper.Constants.SOAP_ACTION;
/**
Diese Klasse registriert einen Benutzer und entscheidet welche Ansicht er sieht


 @author  Myles Sutholt
 */
public class MainActivity extends AppCompatActivity{
    private EventsDataSource datasource;
    private Button btsave;
    private EditText userName;
    private int userID;
    private ApplicationHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
        ApplicationHandler database = new ApplicationHandler(this);


        btsave = (Button) findViewById(R.id.saveNick);
        userName = (EditText) findViewById(R.id.nickname);

        redirect();



    }
    public void setUserID(){

        String method_name = "addUser";

        SoapObject request = new SoapObject(NAMESPACE, method_name);

        PropertyInfo pi = new PropertyInfo();
        String username = userName.getText().toString();
        String deviceID = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        request.addProperty("username", username);
        request.addProperty("deviceID", deviceID);

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
        userID = 0;
        try {
            androidHttpTransport.debug = true;
            androidHttpTransport.call(SOAP_ACTION, envelope, null);
            Log.d("dump Request: " ,androidHttpTransport.requestDump);
            Log.d("dump response: " ,androidHttpTransport.responseDump);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            userID = Integer.parseInt(response.getValue().toString());


            // Aufruf zur PushService Registrierung
            ((ApplicationHandler) getApplicationContext()).getPush().registerDeviceOnPushServer(userName.getText().toString(), this);

        } catch (Exception e) {
            e.printStackTrace();
            alert();
        }



    }
    /*
    Trifft die Entscheidung wo hingeleitet wird.
    Bei initialem Start -> Registrierung
    Bei Start ohne lokalen Daten -> Abruf der Veranstaltungen vom Server
    Bei vorhandenen lokalen Daten -> Anzeige der belegten Veranstaltungen
    Bei aktiver Veranstaltung (zur Zeit stattfindend) -> Zur Ansicht dieser Veranstaltung
     */
    private void redirect(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        String uName = pref.getString("uName", null);

        if (uName!=null) {

            handler = ((ApplicationHandler) getApplicationContext());
            datasource = (handler.getDatasource());
            handler.setUserID(pref.getInt("uID", -1));

            if(datasource.isEmpty()){
                Intent intent = new Intent(MainActivity.this, EventsList.class);
                startActivity(intent);
            }
            else if (datasource.isActive()!= null){
                Intent intent = new Intent(MainActivity.this, EventView.class);
                handler.setEvent(datasource.getEvent(datasource.isActive())); //Your id
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(MainActivity.this, MyEvents.class);
                startActivity(intent);
            }
        }
        else {
            btsave.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        AsyncUser task = new AsyncUser();
                        task.execute();

                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor editor = pref.edit();

                        editor.putString("uName", userName.getText().toString());


                        //Get UserID vom Server
                        editor.putInt("uID", userID);

                        editor.commit();

                        Intent intent = new Intent(MainActivity.this, EventsList.class);
                        Bundle b = new Bundle();
                        b.putInt("ID", 1); //Your id
                        intent.putExtras(b); //Put your id to your next Intent
                        startActivity(intent);
                    } catch (Exception e) {

                    }
                }
            });
        }
    }
    public class AsyncUser extends AsyncTask<String, Void, Void> {



        protected Void doInBackground(String... params) {
            setUserID();
            return null;
        }
        protected void onPostExecute(Void result) {
            Log.i("Create User ", "onPostExecute");        }

        @Override
        protected void onPreExecute() {
            Log.i("Create User ", "onPreExecute");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i("Create User ", "onProgressUpdate");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_majom);
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
                MainActivity.this );

        // set title
        alertDialogBuilder.setTitle("Fehler aufgetreten");

        // set dialog message
        alertDialogBuilder
                .setMessage("Ups. Es ist ein Fehler aufgetreten. Wir bitten um Entschuldigung. Wir befinden uns im Beta-Stadium")
                .setCancelable(false)

                .setNeutralButton("Entschuldigung angenommen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
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

    @Override
    protected void onResume() {
        super.onResume();
        redirect();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
