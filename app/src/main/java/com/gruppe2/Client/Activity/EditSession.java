package com.gruppe2.Client.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.myles.projecto.R;
import com.gruppe2.Client.Database.ApplicationHandler;
import com.gruppe2.Client.Exceptions.ParamMissingException;
import com.gruppe2.Client.Exceptions.WrongDateException;
import com.gruppe2.Client.Helper.Parser;
import com.gruppe2.Client.Helper.Validade;
import com.gruppe2.Client.Objects.Event;
import com.gruppe2.Client.Database.EventsDataSource;
import com.gruppe2.Client.Objects.Session;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.gruppe2.Client.Helper.Constants.NAME;
import static com.gruppe2.Client.Helper.Constants.START;
import static com.gruppe2.Client.Helper.Constants.END;
import static com.gruppe2.Client.Helper.Constants.DESCR;
/**@author Myles Sutholt
    Diese Klasse bearbeitet Termine und persistiert sie
 */
public class EditSession extends AppCompatActivity {
    private int i = 0;
    private Event event = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_session);

        Bundle b = getIntent().getExtras();
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
            simpleDateFormat.setLenient(false);
            Date end = simpleDateFormat.parse(b.getString(END));
            Date start = simpleDateFormat.parse(b.getString(START));
            String name = b.getString(NAME);
            event = new Event(b.getString(NAME), start, end, b.getString(DESCR));
            event.setEventID(b.getInt("ID"));
            EventsDataSource datasource = ((ApplicationHandler) getApplicationContext()).getDatasource();
            event.setSessions(datasource.getSessions(b.getInt("ID")));
            datasource.deleteEvent(event);
        }
        catch (ParamMissingException exception){

        }
        catch (ParseException e){

        }
        if ((event.getSessions().size()) >0) updateTextFields(event.getSessions().get(i));
    }
    public void onClick(View view) {
        Session session;
        switch (view.getId()) {
            case R.id.btnSaveEvent:
                session = saveSession();
                if (!( session == null)) {
                    if ((event.getSessions().size()) >0) event.getSessions().set(i, session);

                    ApplicationHandler handler = ((ApplicationHandler) getApplicationContext());
                    EventsDataSource datasource = (handler.getDatasource());

                    datasource.createEvent(event);

                    Intent intent = new Intent(EditSession.this, MyEvents.class);
                    startActivity(intent);
                }
            case R.id.btnNext:
                session = saveSession();
                if (session != null){
                    if ((event.getSessions().size()) <i ){
                        event.getSessions().set(i, session);
                        i++;
                        updateTextFields(event.getSessions().get(i));
                    }
                    else{
                        event.getSessions().add(session);
                    }
                }
        }
    }
    private Session saveSession(){
        Session session;
        try {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            simpleDateFormat.setLenient(false);
            Date start = simpleDateFormat.parse(((EditText) findViewById(R.id.txtStartDate)).getText().toString());
            Date end = simpleDateFormat.parse(((EditText) findViewById(R.id.txtEndDate)).getText().toString());

            if (start == null || end == null){
                throw new ParamMissingException("Anfangs- und/oder Endzeit");
            }
            else {
                session = new Session(((EditText) findViewById(R.id.txtName)).getText().toString(), start, end,
                        ((EditText) findViewById(R.id.txtAdress)).getText().toString(), ((EditText) findViewById(R.id.txtPLZ)).getText().toString(),
                        ((EditText) findViewById(R.id.txtDescription)).getText().toString());
                new Validade().validateDates(session);
            }

        }
        catch (ParamMissingException exception){
            AlertDialog.Builder builder = new AlertDialog.Builder(EditSession.this);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(EditSession.this);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(EditSession.this);
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

        ((EditText) findViewById(R.id.txtName)).setText("");
        ((EditText) findViewById(R.id.txtEndDate)).setText("");
        ((EditText) findViewById(R.id.txtStartDate)).setText("");
        ((EditText) findViewById(R.id.txtAdress)).setText("");
        ((EditText) findViewById(R.id.txtPLZ)).setText("");
        ((EditText) findViewById(R.id.txtDescription)).setText("");

        return session;
    }
    private void updateTextFields(Session session){

        ((EditText) findViewById(R.id.txtName)).setText(session.getName());
        ((EditText) findViewById(R.id.txtEndDate)).setText((new Parser().DateToString(session.getDateEnd())));
        ((EditText) findViewById(R.id.txtStartDate)).setText((new Parser().DateToString(session.getDateStart())));
        ((EditText) findViewById(R.id.txtAdress)).setText(session.getLocation());
        ((EditText) findViewById(R.id.txtPLZ)).setText(session.getPlz());
        ((EditText) findViewById(R.id.txtDescription)).setText(session.getDescription());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_session, menu);
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
