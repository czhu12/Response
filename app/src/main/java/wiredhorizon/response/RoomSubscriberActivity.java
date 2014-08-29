package wiredhorizon.response;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chriszhu on 7/9/14.
 */
public class RoomSubscriberActivity extends Activity {
    private int roomID;
    private String roomName;
    private int selectedButtonIndex;
    private Button submitQuestion;
    private Button submitVote;
    private Button[] selectionButtons = new Button[5];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber_room);
        Intent i = getIntent();
        roomID = i.getIntExtra("ROOM_ID", 0);
        roomName = i.getStringExtra("ROOM_NAME");
        initialize();
    }

    private void initialize() {
        initializeSelectionButtons();
        final EditText text = (EditText) findViewById(R.id.question_text);
        submitQuestion = (Button) findViewById(R.id.submit_question);
        submitQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = text.getText().toString();
                if (question.length() > 0) {
                    postQuestion(question);
                }
            }
        });

        submitVote = (Button) findViewById(R.id.submit_vote);
        submitVote.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                postVote();
            }
        });
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                modifySubmitQuestionButton(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void postVote() {
        JSONObject payload = new JSONObject();
        try {
            payload.put("vote", selectedButtonIndex);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = VolleyManager.getRequestQueue();
        String token = PreferencesManager.getInstance().get(this, "ACCESS_TOKEN");
        String auth = "?access_token=" + token;
        String[] params = {String.valueOf(roomID)};
        JsonObjectRequest request = new JsonObjectRequest(
                Route.VOTE_POLL_ROUTE.getMethod(),
                Route.VOTE_POLL_ROUTE.getFullRoute(params) + auth,
                payload,
                voteSuccessHandler(),
                voteErrorHandler()
        );

        requestQueue.add(request);
    }

    private Response.ErrorListener voteErrorHandler() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("Fuckd upmang", volleyError.toString());
            }
        };
    }

    private Response.Listener<JSONObject> voteSuccessHandler() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.i("response from room sub activity", jsonObject.toString());
            }
        };
    }

    private void initializeSelectionButtons() {
        selectionButtons[0] = (Button) findViewById(R.id.select_a);
        selectionButtons[1] = (Button) findViewById(R.id.select_b);
        selectionButtons[2] = (Button) findViewById(R.id.select_c);
        selectionButtons[3] = (Button) findViewById(R.id.select_d);
        selectionButtons[4] = (Button) findViewById(R.id.select_e);

        for(int i = 0; i < selectionButtons.length; i++) {
            Log.i("setting click listener for button"  + i, "");
            Button button = selectionButtons[i];
            button.setOnClickListener(new SelectionButtonClickHandler(this, i));
        }
    }

    private void postQuestion(String question) {
        JSONObject payload = new JSONObject();
        try {
            payload.put("body", question);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String[] params = {String.valueOf(roomID)};
        RequestQueue requestQueue = VolleyManager.getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(
                Route.POST_QUESTION_ROUTE.getMethod(),
                Route.POST_QUESTION_ROUTE.getFullRoute(params),
                payload,
                successCallback(),
                errorCallback()
        );
        requestQueue.add(request);
    }

    private Response.Listener<JSONObject> successCallback() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Toast.makeText(getApplicationContext(), "Successfully submitted", Toast.LENGTH_LONG).show();
                EditText text = (EditText) findViewById(R.id.question_text);
                text.setText("");
            }
        };
    }

    private Response.ErrorListener errorCallback() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("Dun goofed", volleyError.toString());
            }
        };
    }

    private void modifySubmitQuestionButton(String s) {
        if (s.length() > 0) {
            submitQuestion.setVisibility(View.VISIBLE);
        } else if (s.length() <= 0) {
            submitQuestion.setVisibility(View.GONE);
        }
    }

    public void setSelectedButton(int selectedButtonIndex) {
        Log.i("selected button", String.valueOf(selectedButtonIndex));
        this.selectedButtonIndex = selectedButtonIndex;
        for(Button button : selectionButtons) {
            button.setBackground(getResources().getDrawable(R.drawable.button_background));
        }
        selectionButtons[selectedButtonIndex].setBackground(getResources().getDrawable(R.drawable.button_background_focus));
        submitVote.setVisibility(View.VISIBLE);
    }

    class SelectionButtonClickHandler implements Button.OnClickListener {
        private RoomSubscriberActivity activity;
        private int buttonIndex;
        public SelectionButtonClickHandler (RoomSubscriberActivity activity, int buttonIndex) {
            this.activity = activity;
            this.buttonIndex = buttonIndex;
        }
        @Override
        public void onClick(View v) {
            Log.i("selected button from click listener", String.valueOf(buttonIndex));
            this.activity.setSelectedButton(buttonIndex);
        }
    }
}
