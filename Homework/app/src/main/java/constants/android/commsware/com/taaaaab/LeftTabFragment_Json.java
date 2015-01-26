package constants.android.commsware.com.taaaaab;

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
import android.widget.TextView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mj on 15. 1. 26.
 */
public class LeftTabFragment_Json extends Fragment {

    Typeface weatherFont;
    public View view;

    TimeThread timeThread;

    //for myClock View
    TextView mTextView_Hour, mTextView_Minute, mTextView_Second, mTextView_Noon;
    TextView mTextView_Temp, mTextView_Status, mTextView_Date, mTextView_Locate;

    String temperature, condition;
    String defaultCity = "Paris, FR";

    TextView weatherIcon;


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //set myClockView
        mTextView_Hour = (TextView)getActivity().findViewById(R.id.text_Hour);
        mTextView_Minute = (TextView)getActivity().findViewById(R.id.text_Minute);
        mTextView_Second = (TextView)getActivity().findViewById(R.id.text_Second);
        mTextView_Noon = (TextView)getActivity().findViewById(R.id.text_Noon);

        //set & start thread for clock
        timeThread = new TimeThread(mainHandler);
        timeThread.setDaemon(true);
        timeThread.start();

        mTextView_Temp = (TextView)getActivity().findViewById(R.id.text_weather_temp);
        mTextView_Status = (TextView)getActivity().findViewById(R.id.text_weather_status);
        mTextView_Date = (TextView)getActivity().findViewById(R.id.text_weather_date);
        mTextView_Locate = (TextView)getActivity().findViewById(R.id.text_weather_locate);

        new weatherTask().execute();
    }

    protected class weatherTask extends AsyncTask<Void, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(Void... arg0) {
            HttpConnect connect = new HttpConnect();

            JSONObject returnResponse = connect.getJason(getActivity(), defaultCity);
            renderWeather(returnResponse);

            return null;
        }

        private void renderWeather(JSONObject json){
            try {
//                mTextView_Locate.setText(json.getString("name").toUpperCase(Locale.US)
//                        + ", " + json.getJSONObject("sys").getString("country"));

                JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                JSONObject main = json.getJSONObject("main");

                condition = details.getString("main");
                temperature = String.format("%.2f", main.getDouble("temp"))+ " ℃";

                setWeatherIcon(details.getInt("id"),
                        json.getJSONObject("sys").getLong("sunrise") * 1000,
                        json.getJSONObject("sys").getLong("sunset") * 1000);
            }catch(Exception e){
                Log.e("SimpleWeather", "One or more fields not found in the JSON data");
            }
        }

        // EXECUTE ON UI THREAD
        protected void onPostExecute(String result) {
            mTextView_Temp.setText(temperature);
            mTextView_Status.setText(condition);
            mTextView_Date.setText(new SimpleDateFormat("MM/dd").format(new Date()));
        }
    }

    public void onResume() { super.onResume();  }

    public void onPause() { super.onPause(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_left_tab, container, false);

        weatherIcon = (TextView)view.findViewById(R.id.image_weather_icon);
        weatherIcon.setTypeface(weatherFont);

        return view;
    }

    Handler mainHandler = new Handler() {
        public void handleMessage (Message msg) {
            mTextView_Hour.setText(String.valueOf(msg.what));
            mTextView_Minute.setText(String.valueOf(msg.arg1));
            mTextView_Second.setText(String.valueOf(msg.arg2));

            if (msg.what <= 12)
                mTextView_Noon.setText(R.string.am);
            else
                mTextView_Noon.setText(R.string.pm);
        }
    };
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "weather.ttf");
    }
    private void setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = getActivity().getString(R.string.weather_sunny);
            } else {
                icon = getActivity().getString(R.string.weather_clear_night);
            }
        } else {
            switch(id) {
                case 2 : icon = getActivity().getString(R.string.weather_thunder);
                    break;
                case 3 : icon = getActivity().getString(R.string.weather_drizzle);
                    break;
                case 7 : icon = getActivity().getString(R.string.weather_foggy);
                    break;
                case 8 : icon = getActivity().getString(R.string.weather_cloudy);
                    break;
                case 6 : icon = getActivity().getString(R.string.weather_snowy);
                    break;
                case 5 : icon = getActivity().getString(R.string.weather_rainy);
                    break;
            }
        }
        weatherIcon.setText(icon);
    }
}