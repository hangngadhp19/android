package com.example.nganth.restaurantapp;

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
import android.widget.RemoteViews;
import android.widget.Toast;

import java.io.IOException;

public class VideoService extends Service {
    private MediaPlayer mediaPlayer;
    private BroadcastReceiver broadcastReceiver;
    private NotificationManager notifManager;

    public VideoService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("pause")) {
                    mediaPlayer.pause();
                } else if (intent.getAction().equals("next")) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();

                    try {
                        mediaPlayer.setDataSource("/storage/emulated/0/Download/food.mp4");
//                        mediaPlayer.setDataSource("https://www.youtube.com/embed/6ZAedvxTHcI");
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (intent.getAction().equals("close")) {
                    // đóng đối tượng phát nhạc
                    mediaPlayer.stop();
                    mediaPlayer.release();

                    // đóng notification
                    stopForeground(true);
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("pause");
        intentFilter.addAction("next");
        intentFilter.addAction("close");
        registerReceiver(broadcastReceiver, intentFilter);

        showNotification();

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        try {
            mediaPlayer.setDataSource("/storage/emulated/0/Download/food.mp4");
//            mediaPlayer.setDataSource("https://www.youtube.com/embed/6ZAedvxTHcI");
            mediaPlayer.prepare();
            mediaPlayer.start();

            //-- bat su kien khi nghe het nhac
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    Toast.makeText(getApplicationContext(), "het nhac roi nhe", Toast.LENGTH_LONG).show();
                    Log.e("Video", "Testing het nhac");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        return START_NOT_STICKY;
    }

    //-- 
    //-- https://stackoverflow.com/questions/46990995/on-android-8-1-api-27-notification-does-not-display
    private void showNotification() {
        String id = "my_package_channel_1"; // The user-visible name of the channel.

        NotificationCompat.Builder notification;

        if (notifManager == null) {
            notifManager =
                    (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new NotificationCompat.Builder(getApplicationContext(), id);
            notification.setContentTitle("Video")  // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder) // required
                    .setContentText(this.getString(R.string.app_name));  // required
        } else {
            notification = new NotificationCompat.Builder(getApplicationContext());
            notification.setContentTitle("Video")                           // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder) // required
                    .setContentText(this.getString(R.string.app_name));  // required
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

        notifManager.notify(11, notification.build());
    }

    public void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}
