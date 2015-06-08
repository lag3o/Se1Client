package com.gruppe2.Event.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import static com.gruppe2.Event.Helper.Constants.NAME;
import static com.gruppe2.Event.Helper.Constants.START;
import static com.gruppe2.Event.Helper.Constants.END;
import static com.gruppe2.Event.Helper.Constants.DESCR;
import com.example.myles.projecto.R;
import com.gruppe2.Event.Database.DatabaseHandler;
import com.gruppe2.Event.Database.EventsDataSource;
import com.gruppe2.Event.Exceptions.ParamMissingException;
import com.gruppe2.Event.Exceptions.WrongDateException;
import com.gruppe2.Event.Helper.Parser;
import com.gruppe2.Event.Helper.Validade;
import com.gruppe2.Event.Objects.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*@author: Myles Sutholt
    Diese Klasse erzeugt eine Veranstaltung und übergibt die Daten an die nächste Activity
 */
public class CreateEvent extends AppCompatActivity {

    private static Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("ID")) {

                EventsDataSource datasource = ((DatabaseHandler) getApplicationContext()).datasource;
                Event event = datasource.getEvent(bundle.getInt("ID"));


                ((EditText) findViewById(R.id.txtName)).setText(event.getName());
                ((EditText) findViewById(R.id.txtDescription)).setText(event.getDescription());
                ((EditText) findViewById(R.id.txtEndDate)).setText((new Parser().DateToString(event.getEndDate())));
                ((EditText) findViewById(R.id.txtStartDate)).setText((new Parser().DateToString(event.getStartDate())));
            }
        }

            //Button Clickable machen
            Button btn = (Button) findViewById(R.id.btnSaveEvent);
            btn.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (saveEvent()) {
                        Bundle b = new Bundle();
                        b.putString(NAME, ((EditText) findViewById(R.id.txtName)).getText().toString());
                        b.putString(DESCR, ((EditText) findViewById(R.id.txtDescription)).getText().toString());
                        b.putString(START, ((EditText) findViewById(R.id.txtStartDate)).getText().toString());
                        b.putString(END, ((EditText) findViewById(R.id.txtEndDate)).getText().toString());

                        ((EditText) findViewById(R.id.txtName)).setText("");
                        ((EditText) findViewById(R.id.txtDescription)).setText("");
                        ((EditText) findViewById(R.id.txtEndDate)).setText("");
                        ((EditText) findViewById(R.id.txtStartDate)).setText("");
                        
                        Intent intent;
                        if (bundle != null) {
                            b.putInt("ID", bundle.getInt("ID"));
                            intent = new Intent(CreateEvent.this, EditSession.class);
                        }
                        else{
                            intent = new Intent(CreateEvent.this, CreateSession.class);
                        }
                        intent.putExtras(b); //Put your id to your next Intent
                        startActivity(intent);
                    }
                }
            });
    }


    private boolean saveEvent(){
        try {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            simpleDateFormat.setLenient(false);
            Date start = simpleDateFormat.parse(((EditText) findViewById(R.id.txtStartDate)).getText().toString());
            Date end = simpleDateFormat.parse(((EditText) findViewById(R.id.txtEndDate)).getText().toString());

            if (start == null || end == null){
                throw new ParamMissingException("Anfangs- und/oder Enddatum");
            }
            else {
                Event event = new Event(((EditText) findViewById(R.id.txtName)).getText().toString(), start, end,
                        ((EditText) findViewById(R.id.txtDescription)).getText().toString());

                new Validade().validateDates(event);
            }

        }
        catch (ParamMissingException exception){
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateEvent.this);
            builder.setTitle("Fehlende Angabe");
            builder.setMessage(exception.getMessage());
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.show();
            return false;
        }
        catch (ParseException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateEvent.this);
            builder.setTitle("Ungültiges Datumsformat");
            builder.setMessage("Beispiele: 31.12.2015/ 1.1.05");
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.show();
            return false;
        }
        catch (WrongDateException ex){
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateEvent.this);
            builder.setTitle("Ungültige Zeit");
            builder.setMessage("Endzeitpunkt ist vor Startzeitpunkt");
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.show();
            return false;
        }
        return true;
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_event, menu);
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

    @Override
    protected void onPause() {

        super.onPause();
    }
}
