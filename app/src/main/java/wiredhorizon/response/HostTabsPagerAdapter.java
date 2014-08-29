package wiredhorizon.response;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chriszhu on 7/10/14.
 */
public class HostTabsPagerAdapter extends FragmentPagerAdapter {
    private int roomID;
    public HostTabsPagerAdapter(FragmentManager fm, int roomID) {
        super(fm);
        this.roomID = roomID;
    }

    @Override
    public Fragment getItem(int index) {
        Bundle bundle = new Bundle();
        bundle.putInt("ROOM_ID", roomID);
        switch (index) {
            case 0:
                HostPollFragment pollFragment = new HostPollFragment();
                pollFragment.setArguments(bundle);
                return pollFragment;
            case 1:
                HostQuestionsFragment questionsFragment = new HostQuestionsFragment();
                questionsFragment.setArguments(bundle);
                return questionsFragment;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

}
