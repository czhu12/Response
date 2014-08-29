package wiredhorizon.response;

/**
 * Created by chriszhu on 7/9/14.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

/**
 * Created by chriszhu on 6/28/14.
 */

public class PreferencesManager {
    private static PreferencesManager preferencesManager = new PreferencesManager();

    public void put(Context context, String key, String value) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(key, value).apply();
    }

    public String get(Context context, String key) throws IllegalArgumentException {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        if(!preferences.contains(key)) {
            throw new IllegalArgumentException(String.format("The key: %s doesn't exist", key));
        }

        return preferences.getString(key, "");
    }

    public String get(Context context, String key, String defaultString) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, defaultString);
    }

    public boolean contains(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.contains(key);
    }
    public void delete(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(preferences.contains(key)) {
            preferences.edit().remove(key).apply();
        }
    }

    public static PreferencesManager getInstance() {
        return preferencesManager;
    }

    private PreferencesManager() {
    }
}