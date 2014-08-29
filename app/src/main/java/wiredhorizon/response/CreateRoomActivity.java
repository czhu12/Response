package wiredhorizon.response;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chriszhu on 7/9/14.
 */
public class CreateRoomActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
        initialize();
    }

    private void initialize() {
        final EditText roomNameEditText = (EditText) findViewById(R.id.create_room_name);
        final Button submitRoom = (Button) findViewById(R.id.submit_room);
        submitRoom.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRoom.setEnabled(false);
                String roomName = roomNameEditText.getText().toString();
                postRoom(roomName);
            }
        });
    }
    private void postRoom(String roomName) {
        RequestQueue queue = VolleyManager.getRequestQueue();
        String token = PreferencesManager.getInstance().get(this, "ACCESS_TOKEN");
        JSONObject payload = new JSONObject();
        try {
            payload.put("name", roomName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String auth = "?access_token=" + token;
        JsonObjectRequest request = new JsonObjectRequest(
                Route.CREATE_ROOM_ROUTE.getMethod(),
                Route.CREATE_ROOM_ROUTE.getFullRoute() + auth,
                payload,
                successHandler(),
                errorHandler()
        );
        queue.add(request);
    }
    private Response.Listener<JSONObject> successHandler () {
        return new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.i("RESPONSE", jsonObject.toString());
                JSONObject roomJSON = null;
                int roomID = 0;
                String roomName = null;
                try {
                    roomJSON = jsonObject.getJSONObject("room");
                    roomID = roomJSON.getInt("id");
                    roomName = roomJSON.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getBaseContext(), RoomHostActivity.class);
                intent.putExtra("ROOM_ID", roomID);
                intent.putExtra("ROOM_NAME", roomName);
                startActivity(intent);
            }
        };
    }

    private Response.ErrorListener errorHandler() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Button submit = (Button) findViewById(R.id.submit_room);
                submit.setEnabled(true);

                Log.i("shitty response", error.toString());
            }
        };
    }
}
