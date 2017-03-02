package com.example.vamsi.WeatherStatus;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {

    private TextView mTextView;
    private EditText key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round_activity_main);
        mTextView = (TextView) findViewById(R.id.textView_display);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void weather(View v) {
        key = (EditText) findViewById(R.id.editext_cityname);
        String s = key.getText().toString();
        String z = s.replace(" ", "_");
        String getURL = "http://api.wunderground.com/api/5bcbc36e4v5145vv/conditions/q/" + z + ".json";
        String response = null;
        BufferedReader bfr = null;
        try {
            URL url = new URL(getURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            bfr = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = bfr.readLine()) != null) {
                // Append server response in string
                sb.append(line + " ");
            }
            response = sb.toString();
        } catch (Exception ex) {
            String Error = ex.getMessage();
        } finally {
            try {
                bfr.close();
            } catch (Exception ex) {

            }
        }
        String i = null;
        JSONObject jsonResult;
        try {
            jsonResult = new JSONObject(response);
            JSONObject a = jsonResult.getJSONObject("response");
            JSONArray array = a.optJSONArray("results");
            JSONObject b = array.getJSONObject(0);
            i = b.getString("state");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String UR = "http://api.wunderground.com/api/5bcbc36e4v5145vv/conditions/q/" + i + "/" + z + ".json";
        String res = null;
        BufferedReader bf = null;
        try {
            URL url = new URL(UR);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder str = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = bf.readLine()) != null) {
                // Append server response in string
                str.append(line + " ");
            }
            res = str.toString();
        } catch (Exception ex) {
            String Error = ex.getMessage();
        } finally {
            try {
                bf.close();
            } catch (Exception ex) {
                String Error = ex.getMessage();
            }
        }
        try {
            JSONObject f = new JSONObject(res);
            JSONObject t = f.getJSONObject("current_observation");
            String dis = t.getString("temperature_string");
           String city1=  ((EditText) findViewById(R.id.editext_cityname)).getText().toString();
            mTextView.setText(""+city1+":"+"\n" + dis.toString());
        } catch (Exception e) {
            String Error = e.getMessage();
        }
    }
}
