package com.gruppe2.Event.Activity;

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
import com.gruppe2.Event.Database.EventsDataSource;
import com.gruppe2.Event.Database.DatabaseHandler;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import static com.gruppe2.Event.Helper.Constants.NAMESPACE;
import static com.gruppe2.Event.Helper.Constants.URL;
import static com.gruppe2.Event.Helper.Constants.SOAP_ACTION;

public class MainActivity extends AppCompatActivity {
    private EventsDataSource datasource;
    public Button btsave;
    public EditText userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
        DatabaseHandler database = new DatabaseHandler(this);


        btsave = (Button) findViewById(R.id.saveNick);
        userName = (EditText) findViewById(R.id.nickname);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        String uName = pref.getString("uName", null);



        if (uName!=null) {

            DatabaseHandler handler = ((DatabaseHandler) getApplicationContext());
            datasource = (handler.getDatasource());

            if(datasource.isEmpty()){
                Intent intent = new Intent(MainActivity.this, EventsList.class);
                startActivity(intent);
            }
            else if (datasource.isActive()!= null){
                Intent intent = new Intent(MainActivity.this, SessionList.class);
                Bundle b = new Bundle();
                b.putInt("ID", datasource.isActive()); //Your id
                intent.putExtras(b); //Put your id to your next Intent
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

                    AsyncUser task = new AsyncUser();
                    task.execute();


                    Intent intent = new Intent(MainActivity.this, EventsList.class);
                    Bundle b = new Bundle();
                    b.putInt("ID", 1); //Your id
                    intent.putExtras(b); //Put your id to your next Intent
                    startActivity(intent);
                    // Aufruf zur PushService Registrierung
                    //registerDeviceOnPushServer(userName.getText().toString());
                }
            });
        }
    }
    public void setUserID(){

        String method_name = "/addUser";

        SoapObject Request = new SoapObject(NAMESPACE, method_name);

        /*
         * Create Category with Id to be passed as an argument
         *
         * */

        /*
         * Set the category to be the argument of the web service method
         *
         * */
        PropertyInfo pi = new PropertyInfo();
        pi.setName("username");
        pi.setValue(userName.getText().toString());
        Request.addProperty(pi);
        pi.setName("deviceID");
        pi.setValue(android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID));
        Request.addProperty(pi);

        /*
         * Set the web service envelope
         *
         * */
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(Request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        /*
         * Call the web service and retrieve result ... how luvly <3
         *
         * */
        int userID = 0;
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject response = (SoapObject) envelope.getResponse();
            userID = Integer.parseInt(response.getProperty(0).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("uName", userName.getText().toString());

        //Get UserID vom Server
        editor.putInt("uID", userID);

        editor.commit();

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
}
