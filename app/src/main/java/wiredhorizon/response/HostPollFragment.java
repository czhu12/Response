package wiredhorizon.response;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by chriszhu on 7/10/14.
 */
public class HostPollFragment extends Fragment {
    private int roomID;
    private TextView timer;
    private Thread t;
    private Button openPoll;
    private int timerValue = 0;
    private boolean shouldBeTiming = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_host_poll, container, false);
        Bundle bundle = getArguments();
        roomID = bundle.getInt("ROOM_ID");
        initialize(rootView);
        return rootView;
    }
    private void initialize(View rootView) {
        openPoll = (Button) rootView.findViewById(R.id.open_poll);
        timer = (TextView) rootView.findViewById(R.id.open_poll_timer);
        openPoll.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimerVisibility(View.VISIBLE);
                setPollOpenButtonVisibility(View.INVISIBLE);
                startTimer();
            }
        });
    }

    private void setPollOpenButtonVisibility(int visible) {
        openPoll.setVisibility(visible);
    }

    private void setTimerVisibility(int visible) {
        timer.setVisibility(visible);
    }



    public void startTimer() {
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (shouldBeRunning()) {
                    try {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setTimerValue();
                                incrementTimerValue();
                            }
                        });
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }

    private boolean shouldBeRunning() {
        return shouldBeTiming;
    }

    private void incrementTimerValue () {
        timerValue++;
    }
    private void setTimerValue() {
        String seconds = String.valueOf(timerValue % 60);
        String minutes = String.valueOf(timerValue / 60);
        if (minutes.length() == 1) {
            minutes = "0" + minutes;
        }
        if (seconds.length() == 1) {
            seconds = "0" + seconds;
        }
        timer.setText(minutes + ":" + seconds);
    }

    @Override
    public void onDestroy() {
        shouldBeTiming = false;
        super.onDestroy();
    }
}
