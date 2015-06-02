package com.se1.gruppe2.projecto;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myles.projecto.R;

import javax.sql.DataSource;

import static com.se1.gruppe2.projecto.Constants.NAME;
import static com.se1.gruppe2.projecto.Constants.START;
import static com.se1.gruppe2.projecto.Constants.END;
import static com.se1.gruppe2.projecto.Constants.DESCR;

public class CreateSession extends ActionBarActivity {

    public Event event = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_session);

        Bundle b = getIntent().getExtras();
        event = new Event(b.getString(NAME), b.getString(START), b.getString(END), b.getString(DESCR));

    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSaveEvent:
                EventsDataSource datasource = new EventsDataSource(this);
                datasource.open();

                datasource.createEvent(event);
            case R.id.btnNew:
                event.addSessions(new Session(((EditText) findViewById(R.id.txtName)).getText().toString(),
                        ((EditText) findViewById(R.id.txtEndDate)).getText().toString(), ((EditText) findViewById(R.id.txtStartDate)).getText().toString(),
                        ((EditText) findViewById(R.id.txtAdress)).getText().toString() + ", " + ((EditText) findViewById(R.id.txtPLZ)).getText().toString(),
                        ((EditText) findViewById(R.id.txtDescription)).getText().toString()));
                ((EditText) findViewById(R.id.txtName)).setText("");
                ((EditText) findViewById(R.id.txtEndDate)).setText("");
                ((EditText) findViewById(R.id.txtStartDate)).setText("");
                ((EditText) findViewById(R.id.txtAdress)).setText("");
                ((EditText) findViewById(R.id.txtPLZ)).setText("");
                ((EditText) findViewById(R.id.txtDescription)).setText("");
        }
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
}
