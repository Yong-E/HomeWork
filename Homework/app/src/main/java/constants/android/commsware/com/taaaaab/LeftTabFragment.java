package constants.android.commsware.com.taaaaab;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
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

public class LeftTabFragment { //extends Fragment {
//    public View mView;
//
//    TimeThread mTimeThread;
//
//    // for myClock View
//    TextView mTVHour, mTVMinute, mTVSecond, mTVNoon;
//
//    // for weather View
//    TextView mTVTemp_Xml, mTVStatus_Xml, mTVDate_Xml;
//
//    String mTemp_Xml, mDate_Xml, mStatus_Xml;
//
//    public void onActivityCreated(Bundle savedInstanceState) { super.onActivityCreated(savedInstanceState); }
//
//    protected class weatherTask extends AsyncTask<Void, String, String> {
//
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        protected String doInBackground(Void... arg0) {
//            String qResult = "";
//            HttpClient httpClient = new DefaultHttpClient();
//            HttpContext localContext = new BasicHttpContext();
//            HttpGet httpGet = new HttpGet("http://weather.yahooapis.com/forecastrss?w=1132599&u=c&#8221");
//
//            try {
//                HttpResponse response = httpClient.execute(httpGet, localContext);
//                HttpEntity entity = response.getEntity();
//
//                if (entity != null) {
//                    InputStream inputStream = entity.getContent();
//                    Reader in = new InputStreamReader(inputStream);
//                    BufferedReader bufferedreader = new BufferedReader(in);
//                    StringBuilder stringBuilder = new StringBuilder();
//
//                    String stringReadLine = null;
//                    while ((stringReadLine = bufferedreader.readLine()) != null) {
//                        stringBuilder.append(stringReadLine + "\n");
//                    }
//
//                    qResult = stringBuilder.toString();
//                }
//            } catch (ClientProtocolException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            // Convert a String to document
//            Document dest = null;
//            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder parser;
//
//            try {
//                parser = dbFactory.newDocumentBuilder();
//                dest = parser.parse(new ByteArrayInputStream(qResult.getBytes()));
//            } catch (ParserConfigurationException e1) {
//                e1.printStackTrace();
//            } catch (SAXException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            // Retrieves the weather from the xml-file
//            Node temperatureNode = dest.getElementsByTagName("yweather:condition").item(0);
//            mTemp_Xml = temperatureNode.getAttributes().getNamedItem("temp").getNodeValue().toString();
//
//            Node tempUnitNode = dest.getElementsByTagName("yweather:units").item(0);
//            mTemp_Xml= mTemp_Xml + "°" +tempUnitNode.getAttributes().getNamedItem("temperature").getNodeValue().toString();
//
//            Node dateNode = dest.getElementsByTagName("yweather:forecast").item(0);
//            mDate_Xml = dateNode.getAttributes().getNamedItem("date").getNodeValue().toString();
//
//            Node conditionNode = dest.getElementsByTagName("yweather:condition").item(0);
//            mStatus_Xml = conditionNode.getAttributes().getNamedItem("text").getNodeValue().toString();
//
//            return qResult;
//        }
//
//        // EXECUTE ON UI THREAD
//        protected void onPostExecute(String result) {
//            mTVTemp_Xml.setText(mTemp_Xml);
//            mTVStatus_Xml.setText(mStatus_Xml);
//            mTVDate_Xml.setText(mDate_Xml);
//        }
//    }
//
//    public void onResume() { super.onResume();  }
//
//    public void onPause() { super.onPause(); }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mView = inflater.inflate(R.layout.fragment_left_tab, container, false);
//
//        //set myClockView
//        mTVHour = (TextView)mView.findViewById(R.id.text_Hour);
//        mTVMinute = (TextView)mView.findViewById(R.id.text_Minute);
//        mTVSecond = (TextView)mView.findViewById(R.id.text_Second);
//        mTVNoon = (TextView)mView.findViewById(R.id.text_Noon);
//
//        //set & start thread for clock
//        mTimeThread = new TimeThread(mainHandler);
//        mTimeThread.setDaemon(true);
//        mTimeThread.start();
//
//        mTVTemp_Xml = (TextView)mView.findViewById(R.id.text_wTemp_Obj);
//        mTVStatus_Xml = (TextView)mView.findViewById(R.id.text_wStatus_Obj);
//        mTVDate_Xml = (TextView)mView.findViewById(R.id.text_wDate_Obj);
//
//        new weatherTask().execute();
//
//        return mView;
//    }
//
//    Handler mainHandler = new Handler() {
//        public void handleMessage (Message msg) {
//            mTVHour.setText(String.valueOf(msg.what));
//            mTVMinute.setText(String.valueOf(msg.arg1));
//            mTVSecond.setText(String.valueOf(msg.arg2));
//
//            if (msg.what <= 12)
//                mTVNoon.setText(R.string.am);
//            else
//                mTVNoon.setText(R.string.pm);
//        }
//    };
}
