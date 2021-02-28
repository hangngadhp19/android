package com.example.nganth.restaurantapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.io.IOException;

public class VideoService extends Service {
    private MediaPlayer mediaPlayer;
    private BroadcastReceiver broadcastReceiver;
    private NotificationManager notifManager;
    final int NOTIFY_ID = 1002;

    public VideoService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        if (mediaPlayer == null) {
            mediaPlayer  = MediaPlayer.create(VideoService.this, R.raw.aaa);
            mediaPlayer.start();
            mediaPlayer.setVolume(0,0);

            //-- bat su kien khi nghe het nhac
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    Intent it = new Intent("broadcast");
                    it.putExtra("star", 5);
                    sendBroadcast(intent);
                    Toast.makeText(getApplicationContext(), "het nhac roi nhe", Toast.LENGTH_LONG).show();
                    Log.e("Video", "Testing het nhac");
                }
            });
        }
        //            mediaPlayer.setDataSource("https://www.youtube.com/embed/6ZAedvxTHcI");
        //mediaPlayer.prepare();
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("pause")) {
                    mediaPlayer.pause();
                } else if (intent.getAction().equals("next")) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer = MediaPlayer.create(VideoService.this, R.raw.aaa);
//                        mediaPlayer.setDataSource("https://www.youtube.com/embed/6ZAedvxTHcI");
                    //mediaPlayer.prepare();
                    mediaPlayer.start();
                } else if (intent.getAction().equals("close")) {
                    // đóng đối tượng phát nhạc
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    // đóng notification
                    //stopForeground(true);
                    notifManager.cancel(NOTIFY_ID);
                } else if (intent.getAction().equals("notify")) {
                    showNotification();
                }
//                mediaPlayer.setVolume(0,0);
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("pause");
        intentFilter.addAction("next");
        intentFilter.addAction("close");
        intentFilter.addAction("notify");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    //--
    //-- https://stackoverflow.com/questions/46990995/on-android-8-1-api-27-notification-does-not-display
    private void showNotification() {

        // There are hardcoding only for show it's just strings
        String name = "my_package_channel";
        String id = "my_package_channel_1"; // The user-visible name of the channel.
        String description = "my_package_first_channel"; // The user-visible description of the channel.

        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder notification;

        if (notifManager == null) {
            notifManager =
                    (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, name, importance);
                mChannel.setDescription(description);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            notification = new NotificationCompat.Builder(this, id);

            intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            notification.setContentTitle("Food Video")  // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder) // required
                    .setContentText(this.getString(R.string.app_name))  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker("Food Video")
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        } else {

            notification = new NotificationCompat.Builder(this);

            intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            notification.setContentTitle("Food Video")                           // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder) // required
                    .setContentText(this.getString(R.string.app_name))  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker("Food Video")
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);
        }


//        Notification.Builder notification = new Notification.Builder(getApplicationContext());

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_player);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notification.setCustomBigContentView(contentView);
        } else {
            notification.setContent(contentView);
        }

        // thiết lập dữ liệu cho giao diện notification
        contentView.setTextViewText(R.id.txtTitleVideo, "Food");
//        contentView.setImageViewResource(R.id.imageView, R.drawable.ic_launcher_background);

        // bắt các sự kiện click vào button
        // bắt sự kiện cho nút tạm dừng
        Intent intentPause = new Intent();
        intentPause.setAction("pause");
        PendingIntent pendingIntentPause = PendingIntent.getBroadcast(getApplicationContext(),
                222,
                intentPause,
                PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.btnPause, pendingIntentPause);


        // bắt sự kiện cho nút chuyển bài
        Intent intentNext = new Intent();
        intentNext.setAction("next");
        PendingIntent pendingIntentNext = PendingIntent.getBroadcast(getApplicationContext(),
                222,
                intentNext,
                PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.btnNext, pendingIntentNext);

        // bắt sự kiện tắt nhạc và đồng thời tắt notification
        Intent intentClose = new Intent();
        intentClose.setAction("close");
        PendingIntent pendingIntentClose = PendingIntent.getBroadcast(getApplicationContext(),
                222,
                intentClose,
                PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.btnClose, pendingIntentClose);

        notifManager.notify(NOTIFY_ID, notification.build());
    }

    public void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}
