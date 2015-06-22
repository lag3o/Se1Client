package com.gruppe2.Client.SOAP;

import android.os.AsyncTask;
import android.util.Log;

import com.gruppe2.Client.Database.ApplicationHandler;
import com.gruppe2.Client.Helper.DataHelper;

/**
 * Created by myles on 20.06.15.
 */
public class SOAPPush extends AsyncTask<String, Void, Void> {
    private ApplicationHandler handler;
    private String msg;
    public SOAPPush(String msg, ApplicationHandler handler){
        super();
        this.handler = handler;
        this.msg = msg;
    }
    protected Void doInBackground(String... params) {
        Log.d("Message Async", msg + " ");
        new DataHelper().sendMessage(msg, handler);
        handler.getDatasource().createMessage(msg, handler.getEvent().getEventID());
        return null;
    }
    protected void onPostExecute(Void result) {
        Log.i("Event Data ", "onPostExecute");
        }

    @Override
    protected void onPreExecute() {
        Log.i("Create User ", "onPreExecute");
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        Log.i("Create User ", "onProgressUpdate");
    }
}
