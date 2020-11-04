package com.example.drawertest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import javax.xml.transform.Result;

public class  LoginActivity extends AppCompatActivity {

    Button loginUser;
    Button registerUser;
    EditText username;
    EditText password;
    TextView loginScreenTv;
    static Boolean loginSuccess = false;
    static RestMethods test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginScreenTv = (TextView) findViewById(R.id.textView);
        loginUser = (Button) findViewById(R.id.login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().apply();

        if(loginSuccess)
        {
            Toast.makeText(getApplicationContext(),"Successfully Logged In",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(LoginActivity.this,MainActivity.class);
            MainActivity.loggedIn = true;
            startActivity(i);
        }



        loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getApplicationContext(),x,Toast.LENGTH_SHORT).show();
                LoginAsync obj = new LoginAsync();
                obj.execute();

            }
        });

        registerUser = (Button) findViewById(R.id.register);
        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchToRegister = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(switchToRegister);
                String data = "";
                loginScreenTv.setText(data);

            }
        });

    }



 private class LoginAsync extends AsyncTask<Void, Void, String> {
    @Override
    protected String doInBackground(Void... params) {
        //String result = RestMethods.checkUserCredentials(username.getText().toString(),password.getText().toString());
        String hashedPassword = PassowrdHasher(password.getText().toString());
        String result = RestMethods.checkUserCredentials(username.getText().toString(),hashedPassword);
        return result;
    }

    @Override
    protected void onPostExecute(String result) {

        Intent i;
        String[] data = result.split(" ");
        SharedPreferences preferences;
        String xyz="";
        if(data[0].equals("Success")) {

            loginSuccess = true;
            preferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            try {
                String d = "";
                for(int j = 1; j < data.length; j++)
                    d += data[j];
                JSONObject credData = new JSONObject(d);
                JSONObject userData = credData.getJSONObject("userId");
                editor.putString("address",((JSONObject) userData).getString("address"));
                editor.putString("firstName",((JSONObject) userData).getString("firstName"));
                editor.putInt("postCode",((JSONObject) userData).getInt("postCode"));
                editor.putInt("userId",((JSONObject) userData).getInt("userId"));
                editor.putString("login","Success");
                editor.putInt("calorieGoal",0);
                editor.apply();
                xyz = preferences.getString("firstName",null);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            TextView tv = (TextView) findViewById(R.id.textView);
            tv.setText(data[0]+" "+xyz);
            i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
        }
        else
            i = new Intent(LoginActivity.this, LoginActivity.class);
            startActivity(i);

    }

     public String PassowrdHasher(String password) {
         try {
             java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
             byte[] bytes = md.digest(password.getBytes("UTF-8"));
             StringBuffer sb = new StringBuffer();
             for (byte b : bytes) {
                 sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1,3));
             }
             return sb.toString();
         } catch (java.security.NoSuchAlgorithmException e) {
         } catch(UnsupportedEncodingException ex){
         }
         return null;
     }
}



    /*public static String findAllCourses() {final String methodPath = "student.course/";
        //initialise
        final String BASE_URL = "http://192.168.43.34:19838/Test1/webresources/restws.credential/";

        URL url = null;
        HttpURLConnection conn = null;
        String textResult = ""; //Making HTTP request
        try {
            url = new URL(BASE_URL);//open the connection
            conn = (HttpURLConnection) url.openConnection();//set the timeou
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);//set the connection method to GET
            conn.setRequestMethod("GET");//add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");//Read the response
            Scanner inStream = new Scanner(conn.getInputStream());//read the input steream and store it as string
            while (inStream.hasNextLine()) {textResult += inStream.nextLine();}}
        catch (Exception e) {e.printStackTrace();} finally {conn.disconnect();}return textResult;
    }*/

}

