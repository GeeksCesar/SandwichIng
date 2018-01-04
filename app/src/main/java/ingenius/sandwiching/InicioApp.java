package ingenius.sandwiching;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.orm.SugarApp;

/**
 * Created by CesarLiizcano on 6/12/2017.
 */

public class InicioApp extends SugarApp {

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}
