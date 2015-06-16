package com.gruppe2.Client.Helper;

import android.content.Context;
import android.util.Log;

import org.jboss.aerogear.android.core.Callback;
import org.jboss.aerogear.android.unifiedpush.PushRegistrar;
import org.jboss.aerogear.android.unifiedpush.RegistrarManager;
import org.jboss.aerogear.android.unifiedpush.gcm.AeroGearGCMPushConfiguration;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

/**@author Malte Lange
Erzeugt eine PushMitteilung
 */
public class Push {



    private final String VARIANT_ID = "22356023-d9f3-4e45-881e-5c4cadba9e31";
    private final String SECRET = "377d77bb-40e0-4a1d-a693-4d7ec238ff19";
    private final String GCM_SENDER_ID = "122610241119";
    private final String UNIFIED_PUSH_URL = "https://seprojektpush-fhmuenster.rhcloud.com/ag-push/";


    public void registerDeviceOnPushServer(String alias, Context context) {

        try {

            RegistrarManager.config("AeroDoc", AeroGearGCMPushConfiguration.class)
                    .setPushServerURI(new URI(UNIFIED_PUSH_URL))
                    .setSenderIds(GCM_SENDER_ID)
                    .setVariantID(VARIANT_ID)
                    .setSecret(SECRET)
                    .setAlias(alias)
                    .setCategories(Arrays.asList("lead"))
                    .asRegistrar();

            PushRegistrar registrar = RegistrarManager.getRegistrar("AeroDoc");
            registrar.register(context, new Callback<Void>() {


                @Override
                public void onSuccess(Void data) {
                    Log.i("Push registriert"," ");
                }

                @Override
                public void onFailure(Exception e) {

                    Log.i("Push nicht registriert", " ");

                    //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }
}


/* In die Activities

        On Terminate
        RegistrarManager.unregisterMainThreadHandler(this); // 2

    @Override
    public void onMessage(Context context, Bundle message) {   // 3
        // display the message contained in the payload
        //TextView text = (TextView) findViewById(R.id.text_view1);
        //text.setText(message.getString("alert"));
    //    text.invalidate();
    }

    @Override
    public void onDeleteMessage(Context context, Bundle message) {
        // handle GoogleCloudMessaging.MESSAGE_TYPE_DELETED
    }

    @Override
    public void onError() {
        // handle GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
    }
 */

