package wiredhorizon.response;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chriszhu on 7/10/14.
 */
public class NotificationReceiver extends BroadcastReceiver {
    public static final String NOTIFICATION_RECEIVED = "com.request.app.IN_APP_NOTIFICATION";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {

            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            Log.i("NOTIFICATION RECIEVED", json.toString());
            String type = json.getString("type");
            String body = json.getString("question_body");
            if (type.equals("question")) {
                Intent inAppIntent = new Intent(NOTIFICATION_RECEIVED);
                inAppIntent.putExtra("QUESTION_BODY", body);
                context.sendBroadcast(inAppIntent);
            } else {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
