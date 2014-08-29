package wiredhorizon.response;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import wiredhorizon.response.models.Question;

/**
 * Created by chriszhu on 7/10/14.
 */
public class HostQuestionsFragment extends Fragment{
    private ListView questionsList;
    private List<Question> questions;
    private QuestionsAdapter adapter;
    private BroadcastReceiver broadcastReceiver;
    private int roomID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_questions, container, false);
        Bundle bundle = getArguments();
        roomID = bundle.getInt("ROOM_ID");

        questionsList = (ListView) rootView.findViewById(R.id.questions_list);
        questions = new ArrayList<Question>();
        adapter = new QuestionsAdapter(getActivity(), 0, questions);
        questionsList.setAdapter(adapter);

        initialize();
        return rootView;
    }

    private void initialize() {

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("RECIEVED NOTIFICATION IN FRAGMENT !!! YAY", "");
                updateUI(intent);
            }
        };
        fetchDataForFragments();
    }

    private void updateUI(Intent i) {
        String body = i.getStringExtra("QUESTION_BODY");
        Question q = new Question(body);
        questions.add(0, q);
        adapter.notifyDataSetChanged();
    }

    private void fetchDataForFragments() {
        String token = PreferencesManager.getInstance().get(getActivity(), "ACCESS_TOKEN");
        String auth = "?access_token=" + token;
        RequestQueue requestQueue = VolleyManager.getRequestQueue();
        String[] params = {String.valueOf(roomID)};
        JsonObjectRequest request = new JsonObjectRequest(
                Route.ROOM_ROUTE.getMethod(),
                Route.ROOM_ROUTE.getFullRoute(params) + auth,
                null,
                successCallback(),
                errorCallback()
        );
        requestQueue.add(request);
    }

    private Response.ErrorListener errorCallback() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("shitty response", volleyError.toString());
            }
        };
    }

    private Response.Listener<JSONObject> successCallback() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Log.i("Room host response", jsonObject.toString());
                    JSONObject roomJSON = jsonObject.getJSONObject("room");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(NotificationReceiver.NOTIFICATION_RECEIVED));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
    }
}
