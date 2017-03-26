package com.craftsmen.FCM;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.craftsmen.Activity.SplashScreenActivity;
import com.craftsmen.AppManger.AppController;
import com.craftsmen.R;

import java.util.Map;

/**
 * Created by Sotraa on 6/15/2016.
 */
public class recivedMesseageHandel {
    Context context ;
    int ID = 0 ;
    public recivedMesseageHandel(Context context,Map<String,String> currentElemnt){
        this.context = context.getApplicationContext() ;
        Log.e("fcm mess",currentElemnt.toString());

    try {
            if (currentElemnt.get("user_id").equals(AppController.getInstance().getPrefManager().getUser().getU_Id()))
            send_declearation_notfication(currentElemnt.get("title") , currentElemnt.get("mess")  ) ;


    }catch (Exception e){
        Log.e("receive fcm exception",e.toString());
    }

    }

    private void send_declearation_notfication(String tile , String desc ){
        Intent intent = new Intent (context ,SplashScreenActivity.class);
        //  intent.setAction(Config.UPDATE_BUS_LOCATION);
        intent.putExtra("from","notification");
        intent.putExtra("desc",desc) ;
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(tile)
                .setAutoCancel(true)
                .setContentText(desc)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setPriority( NotificationCompat.PRIORITY_MAX);
        final Notification notification = notificationBuilder.build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID++ /* ID of notification */, notification);

    }

}
