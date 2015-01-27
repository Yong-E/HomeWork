package constants.android.commsware.com.taaaaab;

import android.support.v7.internal.view.menu.MenuView;
import android.util.JsonReader;
import android.util.JsonToken;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class LeftTabFragment_Json extends Fragment {
    public View mView;

    TimeThread mTimeThread;

    // for myClock View
    TextView mTVHour, mTVMinute, mTVSecond, mTVNoon;

    // for weather variable
    // OpenWeatherMap API with Json & BufferedReader (1st Weather View)
    TextView mTVTempBfr, mTVStatusBfr, mTVDateBfr, mTVWeatherIconBfr, mTVCityBfr;
    Typeface mWeatherFontBfr;
    String mTempBfr, mStatusBfr, mIconBfr;

    // OpenWeatherMap API with Json & JsonReader (2st Weather View)
    TextView mTVTempJsr, mTVStatusJsr, mTVDateJsr, mTVWeatherIconJsr, mTVCityJsr;
    Typeface mWeatherFontJsr;
    String mTempJsr, mStatusJsr, mIconJsr;

    // Yahoo API using Xml
    TextView mTVTempXml, mTVStatusXml, mTVDateXml;
    String mTempXml, mStatusXml, mDateXml;

    String mDefaultCity = "Seoul, KR";

    JSONObject mReponseJsonObj = null;
    JsonReader mJsonReader = null;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // OpenWeatherMap API using Json with BufferedReader
        mWeatherFontBfr = Typeface.createFromAsset(getActivity().getAssets(), "weather.ttf");

        // OpenWeatherMap API using Json with JsonReader
        mWeatherFontJsr = Typeface.createFromAsset(getActivity().getAssets(), "weather.ttf");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_left_tab, container, false);

        // set myClockView
        mTVHour = (TextView)mView.findViewById(R.id.text_Hour);
        mTVMinute = (TextView)mView.findViewById(R.id.text_Minute);
        mTVSecond = (TextView)mView.findViewById(R.id.text_Second);
        mTVNoon = (TextView)mView.findViewById(R.id.text_Noon);

        // set & start thread for clock
        mTimeThread = new TimeThread(mainHandler);
        mTimeThread.setDaemon(true);
        mTimeThread.start();

        // 1st Weather View
        // set View using OpenWeatherMap API with Json & BufferedReader
        mTVTempBfr = (TextView)mView.findViewById(R.id.text_wTemp_Bfr);
        mTVStatusBfr = (TextView)mView.findViewById(R.id.text_wStatus_Bfr);
        mTVDateBfr = (TextView)mView.findViewById(R.id.text_wDate_Bfr);
        mTVCityBfr = (TextView)mView.findViewById(R.id.text_wCity_Bfr);
        mTVCityBfr.setText(mDefaultCity);

        mTVWeatherIconBfr = (TextView)mView.findViewById(R.id.text_wIcon_Bfr);
        mTVWeatherIconBfr.setTypeface(mWeatherFontBfr);

        // 2nd Weather View
        // set View using OpenWeatherMap API with Json & BufferedReader
        mTVTempJsr = (TextView)mView.findViewById(R.id.text_wTemp_Jsr);
        mTVStatusJsr = (TextView)mView.findViewById(R.id.text_wStatus_Jsr);
        mTVDateJsr = (TextView)mView.findViewById(R.id.text_wDate_Jsr);
        mTVCityJsr = (TextView)mView.findViewById(R.id.text_wCity_Jsr);
        mTVCityJsr.setText(mDefaultCity);

        mTVWeatherIconJsr = (TextView)mView.findViewById(R.id.text_wIcon_Jsr);
        mTVWeatherIconJsr.setTypeface(mWeatherFontJsr);

        new weatherTaskUsingOpenWeatherMapAPI().execute();

        // 3rd Weather View
        // set View using Yahoo Weather API with XML Parsing
        mTVTempXml = (TextView)mView.findViewById(R.id.text_wTemp_Xml);
        mTVStatusXml = (TextView)mView.findViewById(R.id.text_wTemp_Xml);
        mTVDateXml = (TextView)mView.findViewById(R.id.text_wTemp_Xml);

        new weatherTaskUsingYahhoAPI().execute();

        return mView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onResume() { super.onResume(); }

    public void onPause() { super.onPause(); }

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

    // use JSON with BufferedStream
    protected class weatherTaskUsingOpenWeatherMapAPI extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() { super.onPreExecute(); }

        protected Void doInBackground(Void... arg0) {

            HttpConnect connect = new HttpConnect();

            // use JSON with BufferedStream
            mReponseJsonObj = connect.getJasonWithBufferedReader(mDefaultCity);
            getWeatherFromJsonObject(mReponseJsonObj);

            // use JSON with JsonReader
            mJsonReader = connect.getJasonStream(mDefaultCity);
            getWeatherWithJsonReader(mJsonReader);

            return null;
        }

        // EXECUTE ON UI THREAD
        protected void onPostExecute(Void result) {
            // set 1st Weather View
            mTVTempBfr.setText(mTempBfr);
            mTVStatusBfr.setText(mStatusBfr);
            mTVDateBfr.setText(new SimpleDateFormat("MM/dd").format(new Date()));

            mTVWeatherIconBfr.setText(mIconBfr);

            // set 2nd Weather View
            mTVTempJsr.setText(mTempJsr);
            mTVStatusJsr.setText(mStatusJsr);
            mTVDateJsr.setText(new SimpleDateFormat("MM/dd").format(new Date()));

            mTVWeatherIconJsr.setText(mIconJsr);
        }

        private void getWeatherFromJsonObject(JSONObject json) {
            try {
                JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                JSONObject main = json.getJSONObject("main");

                mTempBfr = String.format("%.2f", main.getDouble("temp")) + " ℃";
                mStatusBfr= details.getString("main");

                mIconBfr = setWeatherIcon(details.getInt("id"), json.getJSONObject("sys").getLong("sunrise") * 1000, json.getJSONObject("sys").getLong("sunset") * 1000);

            } catch(Exception e) {
                Log.e("getWeatherWithBufferedReader: ", e.toString());
            }
        }

        private void getWeatherWithJsonReader(JsonReader reader) {
            try {
                readJson(reader);
            } catch (Exception e) {
                Log.e("getWeatherWithJsonReader: ", e.toString());
            }
        }

        private void readJson (JsonReader reader) {
            String main = "";
            int id = 0;

            try {
                reader.beginObject();

                while (reader.hasNext()) {
                    main = reader.nextName();
                    if (main.equals("main")) {
                        readMainObject(reader);
                    } else if (main.equals("weather")) {
                        readWeatherArray(reader);
                    } else {
                        reader.skipValue();
                    }
                }

                reader.endObject();

            } catch (Exception e) {
                Log.e("readJson: ", e.toString());
            }

            //readWeatherInfo(reader);
        }

        private void readMainObject(JsonReader reader) {
            String detail = "";

            try {
                reader.beginObject();

                while (reader.hasNext()) {
                    detail = reader.nextName();
                    if (detail.equals("temp")) {
                        mTempJsr = String.format("%.2f", reader.nextDouble()) + " ℃";
                    } else {
                        reader.skipValue();
                    }
                }

                reader.endObject();

            } catch (Exception e) {
                Log.e("readMainObject: ", e.toString());
            }
        }

//        private void readSysObject(JsonReader reader, int id) {
//            String detail = "";
//            Long sunrise = null, sunset = null;
//
//            try {
//                reader.beginObject();
//
//                while (reader.hasNext()) {
//                    detail = reader.nextName();
//                    if (detail.equals("sunrise")) {
//                        sunrise = reader.nextLong() * 1000;
//                    } else if (detail.equals("sunset")) {
//                        sunset = reader.nextLong() * 1000;
//                    } else {
//                        reader.skipValue();
//                    }
//                }
//
//                reader.endObject();
//
//                mIconJsr = setWeatherIcon(id, sunrise, sunset);
//
//            } catch (Exception e) {
//                Log.e("readMainObject: ", e.toString());
//            }
//        }

        private void readWeatherArray(JsonReader reader) {
            try {
                reader.beginArray();
                reader.beginObject();

                while (reader.hasNext()) {
                    if (reader.nextName().equals("main")) {
                        mStatusJsr = reader.nextString();
                    } else {
                        reader.skipValue();
                    }
                }

                reader.endObject();
                reader.endArray();

            } catch (Exception e) {
                Log.e("readWeatherArray: ", e.toString());
            }
        }

        private String setWeatherIcon (int actualId, long sunrise, long sunset) {
            int mId = actualId / 100;
            String mIcon = "";

            if(actualId == 800) {
                long currentTime = new Date().getTime();

                if(currentTime >= sunrise && currentTime < sunset) {
                    mIcon = getString(R.string.weather_sunny);
                } else {
                    mIcon = getString(R.string.weather_clear_night);
                }
            } else {
                switch(mId) {
                    case 2 : mIcon = getString(R.string.weather_thunder);
                        break;
                    case 3 : mIcon = getString(R.string.weather_drizzle);
                        break;
                    case 7 : mIcon = getString(R.string.weather_foggy);
                        break;
                    case 8 : mIcon = getString(R.string.weather_cloudy);
                        break;
                    case 6 : mIcon = getString(R.string.weather_snowy);
                        break;
                    case 5 : mIcon = getString(R.string.weather_rainy);
                        break;
                }
            }

            return mIcon;
        }
    }

    // use XML
    protected class weatherTaskUsingYahhoAPI extends AsyncTask<Void, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(Void... arg0) {
            String qResult = "";
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpGet httpGet = new HttpGet("http://weather.yahooapis.com/forecastrss?w=1132599&u=c&#8221");

            try {
                HttpResponse response = httpClient.execute(httpGet, localContext);
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    InputStream inputStream = entity.getContent();
                    Reader in = new InputStreamReader(inputStream);
                    BufferedReader bufferedreader = new BufferedReader(in);
                    StringBuilder stringBuilder = new StringBuilder();

                    String stringReadLine = null;
                    while ((stringReadLine = bufferedreader.readLine()) != null) {
                        stringBuilder.append(stringReadLine + "\n");
                    }

                    qResult = stringBuilder.toString();
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Convert a String to document
            Document dest = null;
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser;

            try {
                parser = dbFactory.newDocumentBuilder();
                dest = parser.parse(new ByteArrayInputStream(qResult.getBytes()));
            } catch (ParserConfigurationException e1) {
                e1.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Retrieves the weather from the xml-file
            Node temperatureNode = dest.getElementsByTagName("yweather:condition").item(0);
            mTempXml = temperatureNode.getAttributes().getNamedItem("temp").getNodeValue().toString();

            Node tempUnitNode = dest.getElementsByTagName("yweather:units").item(0);
            mTempXml= mTempXml + "°" +tempUnitNode.getAttributes().getNamedItem("temperature").getNodeValue().toString();

            Node dateNode = dest.getElementsByTagName("yweather:forecast").item(0);
            mDateXml = dateNode.getAttributes().getNamedItem("date").getNodeValue().toString();

            Node conditionNode = dest.getElementsByTagName("yweather:condition").item(0);
            mStatusXml = conditionNode.getAttributes().getNamedItem("text").getNodeValue().toString();

            return qResult;
        }

        // EXECUTE ON UI THREAD
        protected void onPostExecute(String result) {
            mTVTempXml.setText(mTempXml);
            mTVStatusXml.setText(mStatusXml);
            mTVDateXml.setText(mDateXml);
        }
    }
}

class TimeThread extends Thread {
    Handler mTimeHandler;

    int mTime;
    int mMinute;
    int mSecond;

    TimeThread (Handler handler) {
        mTimeHandler = handler;
    }
    public void run() {
        while (true) {
            mTime = Integer.parseInt(new SimpleDateFormat("HH").format(new Date()));
            mMinute = Integer.parseInt(new SimpleDateFormat("mm").format(new Date()));
            mSecond = Integer.parseInt(new SimpleDateFormat("ss").format(new Date()));

            Message msg = Message.obtain(mTimeHandler, mTime, mMinute, mSecond, 0);
            mTimeHandler.sendMessage(msg);
            try { Thread.sleep(1000); }
            catch (InterruptedException e) { ; }
        }
    }
}
