package wiredhorizon.response;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import wiredhorizon.response.models.Room;

/**
 * Created by chriszhu on 7/9/14.
 */
public class FindRoomActivity extends Activity{
    private List<Room> suggestedRooms;
    private List<Room> filteredRooms;
    private FindRoomAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_room);
        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRooms();
    }

    private void initialize() {
        suggestedRooms = new ArrayList<Room>();
        filteredRooms = new ArrayList<Room>();
        adapter = new FindRoomAdapter(this, 0, filteredRooms);
        listView = (ListView) findViewById(R.id.room_suggestions);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Integer roomID = (Integer) view.getTag(R.id.suggested_row_room_id);
                subscribeRoom(roomID);
            }
        });
        listView.setAdapter(adapter);

        Button findRoomButton = (Button) findViewById(R.id.search_room);
        EditText searchRoomName = (EditText) findViewById(R.id.find_room_name);
        searchRoomName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterRooms(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void filterRooms(String s) {
        filteredRooms.clear();
        for (Room suggestion : suggestedRooms) {
            String suggestionName = suggestion.getName().toLowerCase();
            if (suggestionName.contains(s)) {
                filteredRooms.add(new Room(suggestion.getId(), suggestion.getName(), suggestion.isOwned()));
            }
        }
        adapter.notifyDataSetChanged();
    }


    private void subscribeRoom(int roomID) {
        RequestQueue requestQueue = VolleyManager.getRequestQueue();
        String token = PreferencesManager.getInstance().get(this, "ACCESS_TOKEN");
        String auth = "?access_token=" + token;
        String[] params = {String.valueOf(roomID)};
        JsonObjectRequest request = new JsonObjectRequest(
                Route.SUBSCRIBE_ROUTE.getMethod(),
                Route.SUBSCRIBE_ROUTE.getFullRoute(params) + auth,
                null,
                subscribeSuccess(roomID),
                subscribeError()
        );
        requestQueue.add(request);
    }

    private Response.ErrorListener subscribeError() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("Shitty response", "fucked up");
            }
        };
    }

    private Response.Listener<JSONObject> subscribeSuccess(final int roomID) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Intent i = new Intent(getBaseContext(), RoomSubscriberActivity.class);
                i.putExtra("ROOM_ID", roomID);
                startActivity(i);
            }
        };
    }

    private void getRooms() {
        RequestQueue requestQueue = VolleyManager.getRequestQueue();
        String token = PreferencesManager.getInstance().get(this, "ACCESS_TOKEN");
        String auth = "?access_token=" + token;
        Route route = Route.ALL_ROOMS_ROUTE;
        JsonArrayRequest request = new JsonArrayRequest(
                route.getFullRoute() + auth,
                successListener(),
                errorListener()
        );
        requestQueue.add(request);
    }

    private Response.Listener<JSONArray> successListener() {
        return new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.i("Find Room Response...", jsonArray.toString());
                try {
                    suggestedRooms.clear();
                    filteredRooms.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject roomJSON = jsonArray.getJSONObject(i).getJSONObject("room");
                        Room room = new Room(roomJSON.getInt("id"), roomJSON.getString("name"), roomJSON.getBoolean("owned"));
                        suggestedRooms.add(room);
                        filteredRooms.add(room);
                    }
                    Log.i("COUNT", "" + adapter.getCount());
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("howd i get here", error.toString());
            }
        };
    }

}
