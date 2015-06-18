package com.gruppe2.Client.Helper;

/**
 *
 *@author  Malte Lange
 */

import org.jboss.aerogear.android.unifiedpush.MessageHandler;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.myles.projecto.R;
import com.gruppe2.Client.Activity.MainActivity;

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
                        .setContentText(msg)
                        .setAutoCancel(true);


        Intent intent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        // mId allows you to update the notification later on.
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

