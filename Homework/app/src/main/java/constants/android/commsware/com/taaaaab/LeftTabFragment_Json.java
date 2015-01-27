package constants.android.commsware.com.taaaaab;

import android.widget.TextView;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mj on 15. 1. 26.
 */
public class LeftTabFragment_Json extends Fragment {
    public View mView;

    TimeThread mTimeThread;

    // for myClock View
    TextView mTVHour, mTVMinute, mTVSecond, mTVNoon;

    // for weather View
    TextView mTVTemp, mTVStatus, mTVDate, mTVWeatherIcon;
    Typeface weatherFont;

    String mTemperature, mStatus;
    String mDefaultCity = "Seoul, KR";

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    protected class weatherTask extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() { super.onPreExecute(); }

        protected Void doInBackground(Void... arg0) {
            HttpConnect connect = new HttpConnect();
            JSONObject returnResponse = connect.getJason(mDefaultCity);
            getWeather(returnResponse);

            return null;
        }

        private void getWeather(JSONObject json){
            try {
                JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                JSONObject main = json.getJSONObject("main");

                mStatus = details.getString("main");
                mTemperature = String.format("%.2f", main.getDouble("temp"))+ " ℃";

                setWeatherIcon(details.getInt("id"), json.getJSONObject("sys").getLong("sunrise") * 1000, json.getJSONObject("sys").getLong("sunset") * 1000);

            } catch(Exception e) {
                Log.e("SimpleWeather", "One or more fields not found in the JSON data");
            }
        }

        // EXECUTE ON UI THREAD
        protected void onPostExecute(Void result) {
            mTVTemp.setText(mTemperature);
            mTVStatus.setText(mStatus);
            mTVDate.setText(new SimpleDateFormat("MM/dd").format(new Date()));
        }
    }

    public void onResume() { super.onResume(); }

    public void onPause() { super.onPause(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_left_tab, container, false);

        mTVWeatherIcon = (TextView)mView.findViewById(R.id.image_weather_icon);
        mTVWeatherIcon.setTypeface(weatherFont);

        //set myClockView
        mTVHour = (TextView)mView.findViewById(R.id.text_Hour);
        mTVMinute = (TextView)mView.findViewById(R.id.text_Minute);
        mTVSecond = (TextView)mView.findViewById(R.id.text_Second);
        mTVNoon = (TextView)mView.findViewById(R.id.text_Noon);

        //set & start thread for clock
        mTimeThread = new TimeThread(mainHandler);
        mTimeThread.setDaemon(true);
        mTimeThread.start();

        mTVTemp = (TextView)mView.findViewById(R.id.text_weather_temp);
        mTVStatus = (TextView)mView.findViewById(R.id.text_weather_status);
        mTVDate = (TextView)mView.findViewById(R.id.text_weather_date);

        new weatherTask().execute();

        return mView;
    }

    Handler mainHandler = new Handler() {
        public void handleMessage (Message msg) {
            mTVHour.setText(String.valueOf(msg.what));
            mTVMinute.setText(String.valueOf(msg.arg1));
            mTVSecond.setText(String.valueOf(msg.arg2));

            if (msg.what <= 12)
                mTVNoon.setText(R.string.am);
            else
                mTVNoon.setText(R.string.pm);
        }
    };

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "weather.ttf");
    }

    private void setWeatherIcon (int actualId, long sunrise, long sunset) {
        int mId = actualId / 100;
        String mIcon = "";

        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                mIcon = getActivity().getString(R.string.weather_sunny);
            } else {
                mIcon = getActivity().getString(R.string.weather_clear_night);
            }
        } else {
            switch(mId) {
                case 2 : mIcon = getActivity().getString(R.string.weather_thunder);
                    break;
                case 3 : mIcon = getActivity().getString(R.string.weather_drizzle);
                    break;
                case 7 : mIcon = getActivity().getString(R.string.weather_foggy);
                    break;
                case 8 : mIcon = getActivity().getString(R.string.weather_cloudy);
                    break;
                case 6 : mIcon = getActivity().getString(R.string.weather_snowy);
                    break;
                case 5 : mIcon = getActivity().getString(R.string.weather_rainy);
                    break;
            }
        }

        mTVWeatherIcon.setText(mIcon);
    }
}