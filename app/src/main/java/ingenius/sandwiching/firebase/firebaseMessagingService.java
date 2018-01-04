package ingenius.sandwiching.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ingenius.sandwiching.MainActivity;
import ingenius.sandwiching.R;
import ingenius.sandwiching.conection.WebServices;

import static android.content.ContentValues.TAG;

/**
 * Created by CesarLiizcano on 14/12/2017.
 */

public class firebaseMessagingService extends FirebaseMessagingService {

    String message, title ;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        title = remoteMessage.getNotification().getTitle();
        message=  remoteMessage.getNotification().getBody();

        if (remoteMessage.getNotification() != null) {
            Log.d(WebServices.TAG, "Notificación: " + remoteMessage.getNotification().getBody());
            showNotification(title, message);
        }

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Data: " + remoteMessage.getData());
        }
    }

    private void showNotification(String title, String message) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notiticationBuilder = new NotificationCompat.Builder(this);
        notiticationBuilder.setContentTitle(title);
        notiticationBuilder.setContentText(message);
        notiticationBuilder.setAutoCancel(true);
        notiticationBuilder.setVibrate(new long[]{ 1000, 1000, 1000, 1000, 1000 });
        notiticationBuilder.setSound(defaultSoundUri);
        notiticationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notiticationBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notiticationBuilder.build());

    }
}
