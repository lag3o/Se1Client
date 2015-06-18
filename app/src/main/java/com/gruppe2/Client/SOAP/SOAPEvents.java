package com.gruppe2.Client.SOAP;

import android.os.AsyncTask;
import android.util.Log;

import com.gruppe2.Client.Database.ApplicationHandler;
import com.gruppe2.Client.Helper.DataHelper;
import com.gruppe2.Client.Objects.Event;

import java.util.ArrayList;

/**
 *
 * Der AsyncTask, der eine Serveranfrage für eine Liste von Veranstaltungen initiiert
 *
 *@author  Myles Sutholt
 */
public class SOAPEvents  extends AsyncTask<String, Void, Void> {

    private ApplicationHandler handler;
    public SOAPEvents(ApplicationHandler handler){
        super();
        this.handler = handler;

    }
    protected Void doInBackground(String... params) {
        new DataHelper().getEvents(handler);
        return null;
    }
    protected void onPostExecute(Void result) {
        Log.i("Event Data ", "onPostExecute");        }

    @Override
    protected void onPreExecute() {
        Log.i("Create User ", "onPreExecute");
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        Log.i("Create User ", "onProgressUpdate");
    }
}
