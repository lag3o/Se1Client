package com.gruppe2.Client.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myles.projecto.R;
import com.gruppe2.Client.Database.ApplicationHandler;
import com.gruppe2.Client.Exceptions.ParamMissingException;
import com.gruppe2.Client.Exceptions.WrongDateException;
import com.gruppe2.Client.Helper.Parser;
import com.gruppe2.Client.Helper.Validade;
import com.gruppe2.Client.Objects.Event;
import com.gruppe2.Client.Database.EventsDataSource;
import com.gruppe2.Client.Objects.Session;
import com.gruppe2.Client.SOAP.SOAPCreateEvent;
import com.gruppe2.Client.SOAP.SOAPEvents;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.gruppe2.Client.Helper.Constants.NAME;
import static com.gruppe2.Client.Helper.Constants.NAMESPACE;
import static com.gruppe2.Client.Helper.Constants.SOAP_ACTION;
import static com.gruppe2.Client.Helper.Constants.START;
import static com.gruppe2.Client.Helper.Constants.END;
import static com.gruppe2.Client.Helper.Constants.DESCR;
import static com.gruppe2.Client.Helper.Constants.URL;

/**
 Diese Klasse erzeugt eine gesamte Veranstaltung inklusive Termine und persistiert sie

 @author Myles Sutholt

 */
public class CreateSession extends AppCompatActivity {

    private Bundle b;
    private Event event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_session);
        event = ((ApplicationHandler) getApplicationContext()).getEvent();
        b = getIntent().getExtras();

    }
    //Handler für die Buttons, die eingebunden wurde. Ruft bestimmte Aktionen auf, wenn betätigt
    public void onClick(View view) {
        Session session;
        switch (view.getId()) {
            case R.id.btnSaveEvent:
                session = saveSession();
                if ( session != null) {
                    event.getSessions().add(session);
                    createEvent();
                }
                break;
            case R.id.btnNew:
                session = saveSession();
                if ( session != null) event.getSessions().add(session);
                Toast.makeText(this, "Termin erstellt", Toast.LENGTH_SHORT).show();

        }
    }
    /*
    Die Veranstaltung erzeugen. Temporär, weil der Server dies nicht beherrscht lokal mit selbst errechneter ID.
    SOAP für Serverkommunikation allerdings vorhanden
     */
    private void createEvent(){
        ApplicationHandler handler = ((ApplicationHandler) getApplicationContext());

        /**
         *
         handler.setEvent(event);
         try {
         SOAPCreateEvent task = new SOAPCreateEvent(handler);
         task.execute().get(5000, TimeUnit.MILLISECONDS);
         }
         catch (Exception e){
         alert();
         handler.resetEvent();
         Log.d("Server Create Event", e.toString());
         }

         */
        EventsDataSource datasource = (handler.getDatasource());

        // ID zuweisen
        int id1 = event.getEventID();
        if (event.getEventID() == -1) {
            int id = (datasource.countEvents() + 1) * 100;
            try {
                event.setEventID(id);
            } catch (Exception e) {
                //alert();
            }
        }

        //Über das update des Events die lokale Persisierung anstoßen
        handler.updateEvent(event);
        Intent intent = new Intent(CreateSession.this, MyEvents.class);
        startActivity(intent);
    }
    /**
     *Test auf richtige Eingabe der verschiedenen Parameter über die Eingabefelder
     *Zunächst Prüfung auf korrekte Angabe der Daten
     *@exception : falls eines der Daten fehlt oder das Datum in einem falschen Format eingegeben wurde.
     *
     *@return session: wenn alles korrekt ist.
     * Anschließend über Erzeugung einer Veranstaltung die Prüfung ob die restlichen Parameter korrekt eingegeben wurden.
     */
    private Session saveSession(){
        Session session;
        try {

            EditText name= ((EditText) findViewById(R.id.txtName));
            EditText endDate= ((EditText) findViewById(R.id.txtEndDate));
            EditText startDate= ((EditText) findViewById(R.id.txtStartDate));
            EditText adress= ((EditText) findViewById(R.id.txtAdress));
            EditText plz= ((EditText) findViewById(R.id.txtPLZ));
            EditText description= ((EditText) findViewById(R.id.txtDescription));

            if (controlParameter(name.getText().toString()) && (controlParameter(endDate.getText().toString()))&&
                    (controlParameter(startDate.getText().toString())) && (controlParameter(adress.getText().toString()))&&
                    (controlParameter(plz.getText().toString())) && (controlParameter(description.getText().toString())))
                    {
                createEvent();
                return null;
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
            simpleDateFormat.setLenient(false);
            Date start = simpleDateFormat.parse(b.getString("Date") + " " + startDate.getText().toString());
            Date end = simpleDateFormat.parse(b.getString("Date") + " " + endDate.getText().toString());

            if (start == null || end == null){
                throw new ParamMissingException("Anfangs- und/oder Endzeit");
            }
            else {
                session = new Session(name.getText().toString(), start, end,
                        adress.getText().toString(), plz.getText().toString(),
                        description.getText().toString());
                new Validade().validateDates(session);
            }
            Log.d("Session Create", b.getString("Date") + " " + new Parser().DateToString(session.getDateStart()));
        }
        //Exception Handling über diverse Popups, die Fehlererkennung vereinfachen sollen
        catch (ParamMissingException exception){
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateSession.this);
            builder.setTitle("Fehlende Angabe");
            builder.setMessage(exception.getMessage());
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.show();
            return null;
        }
        catch (ParseException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateSession.this);
            builder.setTitle("Ungültige Zeitformat");
            builder.setMessage("Benötigtes Format: HH:mm, Stunden 0-23; Minuten 0-59");
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.show();
            return null;
        }
        catch (WrongDateException ex){
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateSession.this);
            builder.setTitle("Ungültige Zeit");
            builder.setMessage("Endzeitpunkt ist vor Startzeitpunkt");
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.show();
            return null;
        }


        //Falls alles richtig eingegeben wurde werden alle Eingabefelder resettet und ein Sessionobjekt zur Speicherung zurückgegeben

        ((EditText) findViewById(R.id.txtName)).setText("");
        ((EditText) findViewById(R.id.txtEndDate)).setText("");
        ((EditText) findViewById(R.id.txtStartDate)).setText("");
        ((EditText) findViewById(R.id.txtAdress)).setText("");
        ((EditText) findViewById(R.id.txtPLZ)).setText("");
        ((EditText) findViewById(R.id.txtDescription)).setText("");

        return session;
    }
    public boolean controlParameter (String param){
        //String name,String dateStart, String dateEnd, String location,String description
        if (param.toString().equalsIgnoreCase("")) return true;
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_session, menu);
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
/**
    private void alert(){
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
                CreateSession.this );

        // set title
        alertDialogBuilder.setTitle("Fehler aufgetreten");

        // set dialog message
        alertDialogBuilder
                .setMessage("Ups. Es ist ein Fehler aufgetreten. Wir bitten um Entschuldigung. Wir befinden uns im Beta-Stadium")
                .setCancelable(false)

                .setNeutralButton("Entschuldigung angenommen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(CreateSession.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

        // create alert dialog
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
*/
}
