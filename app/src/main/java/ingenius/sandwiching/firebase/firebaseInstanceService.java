package ingenius.sandwiching.firebase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import ingenius.sandwiching.utils.SharedPrefManager;

/**
 * Created by CesarLiizcano on 14/12/2017.
 */

public class firebaseInstanceService extends FirebaseInstanceIdService {

    String recent_token;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        recent_token = FirebaseInstanceId.getInstance().getToken();

        storeToken(recent_token);
    }
    private void storeToken(String recent_token) {
        SharedPrefManager.getInstance(getApplicationContext()).storeToken(recent_token);
    }
}
