package com.project.flashcardapp;

import static com.project.flashcardapp.AppSetup.CHANNEL_1_ID;
import static com.project.flashcardapp.home.repo.FlashCardRepository.FLASHCARD;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.project.flashcardapp.home.dto.FlashCardModel;
import com.project.flashcardapp.home.repo.FlashCardRepository;

import java.util.HashMap;
import java.util.Map;



public class AlertReceiver extends BroadcastReceiver {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private NotificationManagerCompat notificationManager;
    private FlashCardRepository repo = new FlashCardRepository();
    private static final String TAG = "AlertReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context);
        String  ID = intent.getStringExtra("ID");
        String  QUES = intent.getStringExtra("QUES");
        String  ANS = intent.getStringExtra("ANS");
        String  DATE = intent.getStringExtra("DATE");
        String  STATUS = intent.getStringExtra("STATUS");
        String  DECK_ID = intent.getStringExtra("DECK_ID");

        FlashCardModel model = new FlashCardModel();
        model.setId(ID);
        model.setQuestion(QUES);
        model.setAnswer(ANS);
        model.setNextReviewDate(DATE);
        model.setReviewStatus(STATUS);
        model.setDeckId(DECK_ID);
        repo.addToQueue(model);
    }

    private void showNotification(Context context) {

        Intent i = new Intent(context, AppMainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_IMMUTABLE);
        notificationManager = NotificationManagerCompat.from(context);
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setContentTitle("New card in queue!")
                .setContentText("New card added in queue")
                .setSmallIcon(R.drawable.baseline_add_alert_24)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        else
            notificationManager.notify(Integer.parseInt(String.valueOf(System.currentTimeMillis() % 10000)), notification);
    }
}

