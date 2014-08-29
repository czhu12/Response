package wiredhorizon.response;

/**
 * Created by chriszhu on 7/9/14.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;


import java.util.Map;

public abstract class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        printPrefs();
        verifyLoginOrRedirect();
    }

    @Override
    protected void onResume() {
        super.onResume();

        printPrefs();

        verifyLoginOrRedirect();

    }

    private void printPrefs() {
        /*
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Map<String,?> keys = prefs.getAll();

        for(Map.Entry<String,?> entry : keys.entrySet()){
            Log.d("map values",
                    entry.getKey() + ": " +
                    entry.getValue().toString());
        }
        */
    }

    private void verifyLoginOrRedirect() {
        if(!PreferencesManager.getInstance().contains(this, "ACCESS_TOKEN")) {
            Intent i = new Intent(this, AuthenticationActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }
    }
}