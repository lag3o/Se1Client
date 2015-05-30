package com.se1.gruppe2.projecto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myles.projecto.R;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    public ImageButton bt;
    public ImageButton imbt;
    public Button btsave;
    public TextView greetings;
    public EditText userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btsave = (Button) findViewById(R.id.saveNick);
        userName = (EditText) findViewById(R.id.nickname);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        String uName = pref.getString("uName", null);

        if (uName!=null) {

            bt = (ImageButton) findViewById(R.id.myEvents);
            imbt = (ImageButton) findViewById(R.id.events_list);

            bt.setVisibility(View.VISIBLE);
            imbt.setVisibility(View.VISIBLE);
            btsave.setVisibility(View.INVISIBLE);
            userName.setVisibility(View.INVISIBLE);


            bt.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, myEvents.class);
                    Bundle b = new Bundle();
                    b.putInt("ID", 1); //Your id
                    intent.putExtras(b); //Put your id to your next Intent
                    startActivity(intent);
                }
            });
            imbt.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, EventsList.class);
                    Bundle b = new Bundle();
                    b.putInt("ID", 1); //Your id
                    intent.putExtras(b); //Put your id to your next Intent
                    startActivity(intent);
                }
            });
        }
        else {
            btsave.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                    SharedPreferences.Editor editor = pref.edit();

                    editor.putString("uName", userName.getText().toString());

                    editor.commit();
                    Intent intent = new Intent(MainActivity.this, EventsList.class);
                    Bundle b = new Bundle();
                    b.putInt("ID", 1); //Your id
                    intent.putExtras(b); //Put your id to your next Intent
                    startActivity(intent);
                }
            });
        }
    }

            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
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