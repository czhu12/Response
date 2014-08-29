package wiredhorizon.response;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chriszhu on 7/9/14.
 */
public class RoomHostActivity extends FragmentActivity implements ActionBar.TabListener {
    private int roomID;
    private String roomName;
    private ViewPager viewPager;
    private HostTabsPagerAdapter mAdapter;
    private static int tabIndex = 0;
    private final String[] TABS = {"Poll", "Questions"};
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_room);
        initialize();
    }

    private void initialize() {
        Intent i = getIntent();
        roomID = i.getIntExtra("ROOM_ID", 0);
        roomName = i.getStringExtra("ROOM_NAME");

        mAdapter = new HostTabsPagerAdapter(getSupportFragmentManager(), roomID);
        viewPager = (ViewPager) findViewById(R.id.host_pager);
        viewPager.setAdapter(mAdapter);
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for (String tab : TABS) {
            ActionBar.Tab t = actionBar.newTab().setText(tab).setTabListener(this);
            actionBar.addTab(t);
        }
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(tabIndex);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
