package com.se1.gruppe2.projecto;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import static com.se1.gruppe2.projecto.Constants.NAME;
import static com.se1.gruppe2.projecto.Constants.START;
import static com.se1.gruppe2.projecto.Constants.END;
import static com.se1.gruppe2.projecto.Constants.DESCR;
import com.example.myles.projecto.R;

/*@author: Myles Sutholt
    Diese Klasse erzeugt eine Veranstaltung und übergibt die Daten an die nächste Activity
 */
public class CreateEvent extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);


        //Button Clickable machen
        Button btn = (Button) findViewById(R.id.btnSaveEvent);
        btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CreateEvent.this, CreateSession.class);
                Bundle b = new Bundle();
                b.putString(NAME, ((EditText) findViewById(R.id.txtName)).getText().toString());
                b.putString(DESCR, ((EditText) findViewById(R.id.txtDescription)).getText().toString());
                b.putString(END, ((EditText) findViewById(R.id.txtEndDate)).getText().toString());
                b.putString(START, ((EditText) findViewById(R.id.txtStartDate)).getText().toString());

                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
            }
        });
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
        ((EditText) findViewById(R.id.txtName)).setText("");
        ((EditText) findViewById(R.id.txtDescription)).setText("");
        ((EditText) findViewById(R.id.txtEndDate)).setText("");
        ((EditText) findViewById(R.id.txtStartDate)).setText("");

        super.onPause();
    }
}
