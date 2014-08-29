package wiredhorizon.response;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.parse.PushService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by chriszhu on 6/28/14.
 */
public class AuthenticationActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(PreferencesManager.getInstance().contains(this, "ACCESS_TOKEN")) {
            Log.i("Redirecting to main", "access token found");
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        bindLogin();
        bindSignUp();

    }

    private void bindSignUp() {
        final EditText signUpEmailField = (EditText)
                findViewById(R.id.authentication_signup_email);
        final EditText signUpPasswordField = (EditText)
                findViewById(R.id.authentication_signup_password);
        final EditText signUpPasswordConfirmationField = (EditText)
                findViewById(R.id.authentication_signup_password_confirmation);
        final EditText signUpFirstNameField = (EditText)
                findViewById(R.id.authentication_signup_first_name);
        final EditText signUplastNameField = (EditText)
                findViewById(R.id.authentication_signup_last_name);



        signUpPasswordConfirmationField.setImeOptions(EditorInfo.IME_ACTION_DONE);
        signUpPasswordConfirmationField.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String email = signUpEmailField.getText().toString();
                    String password = signUpPasswordField.getText().toString();
                    String passwordConfirmation = signUpPasswordConfirmationField
                            .getText().toString();
                    String firstName = signUpFirstNameField.getText().toString();
                    String lastName = signUplastNameField.getText().toString();
                    signUpUser(email, password, passwordConfirmation, firstName, lastName);
                }
                return false;
            }
        });
    }
    private void bindLogin() {
        final EditText loginPasswordField = (EditText)
                findViewById(R.id.authentication_login_password);
        final EditText loginEmailField = (EditText)
                findViewById(R.id.authentication_login_email);
        loginPasswordField.setImeOptions(EditorInfo.IME_ACTION_DONE);
        loginPasswordField.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String email = loginEmailField.getText().toString();
                    String password = loginPasswordField.getText().toString();
                    loginUser(email, password);
                }

                return false;
            }
        });
    }

    private void loginUser(String email, String password) {
        JSONObject userData = new JSONObject();
        JSONObject loginData = new JSONObject();
        try {
            userData.put("email", email);
            userData.put("password", password);
            loginData.put("user", userData);
            sendCredentials(loginData, Route.LOGIN_ROUTE);
            Log.i("login data", loginData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void signUpUser(String email, String password, String passwordConfirmation, String firstName, String lastName) {
        JSONObject signUpData = new JSONObject();
        JSONObject userData = new JSONObject();
        try {
            userData.put("email", email);
            userData.put("password", password);
            userData.put("password_confirmation", passwordConfirmation);
            userData.put("first_name", firstName);
            userData.put("last_name", lastName);
            signUpData.put("user", userData);
            sendCredentials(signUpData, Route.SIGN_UP_ROUTE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void sendCredentials(JSONObject data, Route route) {

        RequestQueue requestQueue = VolleyManager.getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(
                route.getMethod(),
                route.getFullRoute(),
                data,
                createRequestSuccessListener(),
                createRequestErrorListener()
        );
        requestQueue.add(request);
    }

    private Response.Listener<JSONObject> createRequestSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("User data:", response.toString());
                String accessToken = null;
                int userID = 0;
                try {
                    accessToken = response.getString("access_token");
                    userID = response.getInt("user_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                PreferencesManager.getInstance().put(getBaseContext(), "ACCESS_TOKEN", accessToken);
                String userChannel = "user_" + userID;
                PushService.subscribe(getBaseContext(), userChannel, AuthenticationActivity.class);

                Intent i = new Intent(getBaseContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };
    }

    private Response.ErrorListener createRequestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("shitty response", error.toString());
            }
        };
    }

    private void showErrorDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(AuthenticationActivity.this);
        b.setMessage("Error occured");
        b.show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            Log.v("Activity", "Touch event " + event.getRawX() + "," + event.getRawY() + " " + x + "," + y + " rect " + w.getLeft() + "," + w.getTop() + "," + w.getRight() + "," + w.getBottom() + " coords " + scrcoords[0] + "," + scrcoords[1]);
            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom()) ) {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }
}