package com.system.operaciones.Firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.system.operaciones.R;
import com.system.operaciones.activities.ClientesActivity;
import com.system.operaciones.activities.InstalacionesActivity;
import com.system.operaciones.activities.MantenimientosActivity;
import com.system.operaciones.activities.UrgenciasActivity;
import com.system.operaciones.utils.Credentials;

import java.util.Random;

class MyNotificationManager {
    private Context mCtx;
    private static MyNotificationManager mInstance;

    private MyNotificationManager(Context context) {
        mCtx = context;
    }

    static synchronized MyNotificationManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyNotificationManager(context);
        }
        return mInstance;
    }

    void displayNotification(String body, String title, RemoteMessage message) {
        Random random=new Random();
        int notificationId=random.nextInt();
        // Start without a delay
        // Each element then alternates between vibrate, sleep, vibrate, sleep...
        long[] pattern = {0, 500, 250, 500, 250,500, 250};

        Intent intent = null;

        String cliente_id = message.getData().get("cliente");
        String tienda_id = message.getData().get("tienda");
        String view = message.getData().get("view");
        new Credentials(mCtx).save_data("key_cliente",cliente_id);
        switch (view)
        {
            case "1":
                intent = new Intent(mCtx, InstalacionesActivity.class);
                break;
            case "2":
                intent = new Intent(mCtx, MantenimientosActivity.class);
                break;
            case "3":
                intent = new Intent(mCtx, UrgenciasActivity.class);
                break;
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(mCtx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


//        Notification mBuilder;
//        mBuilder = new Notification.Builder(mCtx);
//        mBuilder.setSmallIcon(R.drawable.uezu_software);
//        mBuilder.setContentTitle(title);
//        mBuilder.setContentText(body);
//        mBuilder.setContentIntent(pendingIntent);
//        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(body));
//        mBuilder.setOnlyAlertOnce(true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            mBuilder.setChannelId("1");
//        }
//        mBuilder.setAutoCancel(true);
//
        NotificationManager nm=(NotificationManager)mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
//        mBuilder.setVibrate(pattern);
        Bitmap icon = BitmapFactory.decodeResource(mCtx.getResources(),
                R.drawable.icon_play);
        Notification notification = new NotificationCompat.Builder(mCtx, "1")
                .setSmallIcon(R.drawable.icon_play)
                .setLargeIcon(icon)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(body))
                .setVibrate(pattern)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();

        if (nm != null) {
            nm.notify(notificationId, notification);
        }
    }
}
