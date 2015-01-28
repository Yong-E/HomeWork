package constants.android.commsware.com.taaaaab;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Message;
import android.util.JsonReader;
import android.util.Log;

public class HttpConnect {

    final static HttpClient httpClient = new DefaultHttpClient();
    private static final String OPEN_WEATHER_MAP_API = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";

    URL url = null;
    HttpURLConnection connection = null;

    private void getHttpURLConnection(String city) {
        try {
            url = new URL(String.format(OPEN_WEATHER_MAP_API, city));
            connection = (HttpURLConnection) url.openConnection();
        } catch (Exception e) {
            Log.e("HttpURLConnection : ", e.toString());
        }
    }

    public JSONObject getJasonWithBufferedReader(String city) {
        getHttpURLConnection(city);

        JSONObject dataObj = null;
        try {
            //*********Do not use "\n"**********
            /*
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            while ((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            dataObj = new JSONObject(json.toString());
            */

            // not using while but this way using buffer
            /*
            Reader reader = new InputStreamReader(connection.getInputStream());
            char[] buffer = new char[1024];
            reader.read(buffer);

            reader.close();

            dataObj = new JSONObject(new String(buffer));
            */

            dataObj = new JSONObject(IOUtils.toString(connection.getInputStream()));

            // This value will be 404 if the request was not
            // successful
            if(dataObj.getInt("cod") != 200) {
                return null;
            }

            //return data;
        }catch(Exception e) {
            Log.e("getJasonUsingBufferedReader (HTTPConnect) : ", e.toString());
        }
        return dataObj;
    }

    public JsonReader getJasonStream (String city) {
        getHttpURLConnection(city);

        JsonReader jsonRdr = null;
        try {
            jsonRdr = new JsonReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

        } catch (Exception e) {
            Log.e("getJasonUsingJsonReader (HTTPConnect) : ", e.toString());
        }

        return jsonRdr;
    }
}


