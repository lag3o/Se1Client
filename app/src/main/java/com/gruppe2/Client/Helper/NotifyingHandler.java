package com.gruppe2.Client.Helper;

/**
 * Created by Malte on 12.06.15.
 */

import org.jboss.aerogear.android.unifiedpush.MessageHandler;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.example.myles.projecto.R;

public class NotifyingHandler implements MessageHandler {

    public static final int NOTIFICATION_ID = 1;

    @Override
    public void onMessage(Context context, Bundle message) {
        String msg = message.getString("alert");

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =  // 3
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_majom)
                        .setContentTitle("Neuigkeiten")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    @Override
    public void onDeleteMessage(Context context, Bundle arg0) {
        // handle GoogleCloudMessaging.MESSAGE_TYPE_DELETED
    }

    @Override
    public void onError() {
        // handle GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
    }
}

