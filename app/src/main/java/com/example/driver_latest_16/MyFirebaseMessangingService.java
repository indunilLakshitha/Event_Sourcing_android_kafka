package com.example.driver_latest_16;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessangingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification() != null) {

            String title=remoteMessage.getNotification().getTitle();
            String text=remoteMessage.getNotification().getBody();

        }

    }
}
