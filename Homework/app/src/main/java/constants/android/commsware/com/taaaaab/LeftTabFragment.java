package constants.android.commsware.com.taaaaab;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class LeftTabFragment extends Fragment {
    public View view;

    TimeThread timeThread;

    //for myClock View
    TextView mTextView_Hour, mTextView_Minute, mTextView_Second, mTextView_Noon;
    TextView mTextView_Temp, mTextView_Status, mTextView_Date; //mTextView_Locate

    Bitmap icon = null;

    ProgressDialog dialog;

    String temperature, humidity, wind, date, condition, link;
    ArrayList<String> weather = new ArrayList<String>();

    Activity currentActivity = null;

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

<<<<<<< HEAD
        //currentActivity = getActivity();
        //System.out.println("currentActivity: *** " + currentActivity);

        //set myClockView
=======
>>>>>>> db34d3cc5bd62d5e76f8ca43c4834a4108ea5c29
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

        new weatherTask().execute();
    }

    protected class weatherTask extends AsyncTask<Void, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(getActivity());
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.show();
        }

        protected String doInBackground(Void... arg0) {
            String qResult = "";
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpGet httpGet = new HttpGet("http://weather.yahooapis.com/forecastrss?w=2295425&u=c&#8221");

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
                //Toast.makeText(currentActivity, e.toString(), Toast.LENGTH_LONG);
            } catch (IOException e) {
                e.printStackTrace();
                //Toast.makeText(currentActivity, e.toString(), Toast.LENGTH_LONG);
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
                //Toast.makeText(currentActivity, e1.toString(), Toast.LENGTH_LONG);
            } catch (SAXException e) {
                e.printStackTrace();
                //Toast.makeText(currentActivity, e.toString(), Toast.LENGTH_LONG);
            } catch (IOException e) {
                e.printStackTrace();
                //Toast.makeText(currentActivity, e.toString(), Toast.LENGTH_LONG);
            }

            // Retrieves the weather from the xml-file
            Node temperatureNode = dest.getElementsByTagName("yweather:condition").item(0);
            temperature = temperatureNode.getAttributes().getNamedItem("temp").getNodeValue().toString();

            Node tempUnitNode = dest.getElementsByTagName("yweather:units").item(0);
            temperature = temperature + "°" +tempUnitNode.getAttributes().getNamedItem("temperature").getNodeValue().toString();

            Node dateNode = dest.getElementsByTagName("yweather:forecast").item(0);
            date = dateNode.getAttributes().getNamedItem("date").getNodeValue().toString();

            Node conditionNode = dest.getElementsByTagName("yweather:condition").item(0);
            condition = conditionNode.getAttributes().getNamedItem("text").getNodeValue().toString();

            Node humidityNode = dest.getElementsByTagName("yweather:atmosphere").item(0);
            humidity = humidityNode.getAttributes().getNamedItem("humidity").getNodeValue().toString();
            humidity = humidity + "%";

            Node windNode = dest.getElementsByTagName("yweather:wind").item(0);
            wind = windNode.getAttributes().getNamedItem("speed").getNodeValue().toString();

//            Node windUnitNode = dest.getElementsByTagName("yweather:units").item(0);
//            wind = wind + " " + windUnitNode.getAttributes().getNamedItem("speed").getNodeValue().toString();

            String desc = dest.getElementsByTagName("item").item(0).getChildNodes().item(13).getTextContent().toString();
            StringTokenizer str = new StringTokenizer(desc, "<=>");
            System.out.println("Tokens: " + str.nextToken("=>"));
            String src = str.nextToken();
            System.out.println("src: " + src);
            String url1 = src.substring(1, src.length() - 2);
            Pattern TAG_REGEX = Pattern.compile("(.+?)<br />");
            Matcher matcher = TAG_REGEX.matcher(desc);
            while (matcher.find()) {
                weather.add(matcher.group(1));
            }

//            Pattern links = Pattern.compile("(.+?)<BR/>");
//            matcher = links.matcher(desc);
//            while(matcher.find()){
//                System.out.println("Match Links: " + (matcher.group(1)));
//                link = matcher.group(1);
//            }

            /* String test = (Html.fromHtml(desc)).toString();
            System.out.println(“test: “+ test);
            StringTokenizer tkn = new StringTokenizer(test);
            for(int i=0; i < tkn.countTokens(); i++){
            System.out.println(“Remaining: “+tkn.nextToken());
            }*/

            InputStream in = null;
            try {
                // in = OpenHttpConnection(url1);
                int response = -1;
                URL url = new URL(url1);
                URLConnection conn = url.openConnection();

                if (!(conn instanceof HttpURLConnection)) throw new IOException("Not an HTTP connection");
                HttpURLConnection httpConn = (HttpURLConnection) conn;
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");
                httpConn.connect();

                response = httpConn.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {
                    System.out.println("*********************");
                    in = httpConn.getInputStream();
                }
                icon = BitmapFactory.decodeStream(in);
                in.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return qResult;
        }

        // EXECUTE ON UI THREAD
        protected void onPostExecute(String result) {
            System.out.println("POST EXECUTE");
            if(dialog.isShowing())
                dialog.dismiss();

            mTextView_Temp.setText("Temperature: " + temperature);
            mTextView_Status.setText("Condition: " + condition);
            //dateText.setText("Date: " + date);
            //humidityText.setText("Humidity: " + humidity);
            //windText.setText("Wind: "+ wind);

//            image.setImageBitmap(icon);

//            day1.setText(weather.get(3));
//            day2.setText(weather.get(4));
//            day3.setText(weather.get(5));
//            day4.setText(weather.get(6));
//            weatherLink.setText(Html.fromHtml(link));
        }
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
            mTextView_Second.setText(String.valueOf(msg.arg2));

            if (msg.what <= 12)
                mTextView_Noon.setText(R.string.am);
            else
                mTextView_Noon.setText(R.string.pm);
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
