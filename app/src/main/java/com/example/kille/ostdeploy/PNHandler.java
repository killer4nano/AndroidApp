package com.example.kille.ostdeploy;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by kille on 2017-08-09.
 */

public class PNHandler extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Transfer.getConnection().updateTasks();
        if(!Transfer.getConnection().getAct().isVisible() && !Transfer.getConnection().isOnJob()) {
            Transfer.getConnection().getAct().showNotification();
        }
    }
}
