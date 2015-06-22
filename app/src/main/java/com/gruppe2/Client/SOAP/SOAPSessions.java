package com.gruppe2.Client.SOAP;

import android.os.AsyncTask;
import android.util.Log;

import com.gruppe2.Client.Database.ApplicationHandler;
import com.gruppe2.Client.Helper.DataHelper;

/**
 * Created by myles on 21.06.15.
 */
public class SOAPSessions  extends AsyncTask<String, Void, Void> {
    private ApplicationHandler handler;
    public SOAPSessions(ApplicationHandler handler){
        super();
        this.handler = handler;

    }
    protected Void doInBackground(String... params) {
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
