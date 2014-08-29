package wiredhorizon.response;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;

/**
 * Created by chriszhu on 7/9/14.
 */
public class ResponseApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "XsvCYO8sfa96WipBJGWjxhPNfo7daq6Q10U2ZXOV", "fSIAVdhBzXHYEy8JEC7Rm36t8FKTfparyOOQYZE3");
        PushService.setDefaultPushCallback(this, MainActivity.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        VolleyManager.init(getApplicationContext());
    }
}
