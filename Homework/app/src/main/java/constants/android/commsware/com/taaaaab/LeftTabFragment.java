package constants.android.commsware.com.taaaaab;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LeftTabFragment extends Fragment {
    public View view;

    TimeThread timeThread;

    TextView mTextView_Hour;
    TextView mTextView_Minute;
    TextView mTextView_Seconde;
    TextView mTextView_Noon;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTextView_Hour = (TextView)getActivity().findViewById(R.id.textHour);
        mTextView_Minute = (TextView)getActivity().findViewById(R.id.textMinute);
        mTextView_Seconde = (TextView)getActivity().findViewById(R.id.textSecond);
        mTextView_Noon = (TextView)getActivity().findViewById(R.id.textNoon);

        timeThread = new TimeThread(mainHandler);
        timeThread.setDaemon(true);
        timeThread.start();
    }

    public void onResume() { super.onResume();  }

    public void onPause() { super.onPause(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_left_tab, container, false);
        return view;
    }

    Handler mainHandler = new Handler() {
        public void handleMessage (Message msg) {
            mTextView_Hour.setText(String.valueOf(msg.what));
            mTextView_Minute.setText(String.valueOf(msg.arg1));
            mTextView_Seconde.setText(String.valueOf(msg.arg2));

            if (msg.what <= 12)
                mTextView_Noon.setText(R.string.AM);
            else
                mTextView_Noon.setText(R.string.PM);
        }
    };
}

class TimeThread extends Thread {
    Handler timeHandler;

    int time;
    int minute;
    int second;

    TimeThread (Handler handler) {
        timeHandler = handler;
    }
    public void run() {
        while (true) {
            time = Integer.parseInt(new SimpleDateFormat("HH").format(new Date()));
            minute = Integer.parseInt(new SimpleDateFormat("mm").format(new Date()));
            second = Integer.parseInt(new SimpleDateFormat("ss").format(new Date()));

            Message msg = Message.obtain(timeHandler, time, minute, second, 0);
            timeHandler.sendMessage(msg);
            try { Thread.sleep(1000); }
            catch (InterruptedException e) { ; }
        }
    }
}
