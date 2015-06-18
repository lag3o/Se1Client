package com.gruppe2.Client.SOAP;

import android.os.AsyncTask;
import android.util.Log;

import com.gruppe2.Client.Database.ApplicationHandler;
import com.gruppe2.Client.Helper.DataHelper;
/**
 *
 * Der AsyncTask, der die Veranstaltungslöschung auf dem Server intitiiert
 *
 *@author  Myles Sutholt
 */
public class SOAPDropEvent extends AsyncTask<String, Void, Void> {

    private ApplicationHandler handler;
    public SOAPDropEvent(ApplicationHandler handler){
        super();
        this.handler = handler;

    }
    protected Void doInBackground(String... params) {
        new DataHelper().getEvent(handler);
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