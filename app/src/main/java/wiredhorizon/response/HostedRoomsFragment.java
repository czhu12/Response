package wiredhorizon.response;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import wiredhorizon.response.models.Room;

/**
 * Created by chriszhu on 7/9/14.
 */
public class HostedRoomsFragment extends Fragment {
    private HostedRoomsAdapter adapter;
    private List<Room> hostedRooms;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_hosted_rooms, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.hosted_rooms_list);
        hostedRooms = new ArrayList<Room>();
        adapter = new HostedRoomsAdapter(getActivity(), 0, hostedRooms);
        listView.setAdapter(adapter);

        Button hostRoomButton = (Button) rootView.findViewById(R.id.create_host_room);
        hostRoomButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CreateRoomActivity.class);
                startActivity(i);
            }
        });
        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        initialize();
    }

    private void initialize() {
        getHostedRooms();
    }

    private void getHostedRooms() {
        RequestQueue requestQueue = VolleyManager.getRequestQueue();
        String token = PreferencesManager.getInstance().get(getActivity(), "ACCESS_TOKEN");
        String auth = "?access_token=" + token;
        Route route = Route.ROOMS_ROUTE;
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
                Log.i("Response", jsonArray.toString());

                try {
                    hostedRooms.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject roomJSON = jsonArray.getJSONObject(i).getJSONObject("room");
                        Room room = new Room(roomJSON.getInt("id"), roomJSON.getString("name"), roomJSON.getBoolean("owned"));
                        if (room.isOwned()) {
                            hostedRooms.add(room);
                        }
                    }
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.log_out) {

            PreferencesManager prefMan = PreferencesManager.getInstance();
            prefMan.delete(getActivity(), "ACCESS_TOKEN");

            Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return false;
    }

}
