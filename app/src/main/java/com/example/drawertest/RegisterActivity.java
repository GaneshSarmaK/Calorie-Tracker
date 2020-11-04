package com.example.drawertest;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    EditText fname;
    EditText lname;
    EditText email;

//DATE

    private DatePickerDialog.OnDateSetListener rDateSetListener;


    EditText height;
    EditText weight;
    EditText stepspermile;
    EditText address;
    EditText postcode;
    EditText username;
    EditText password;
    Spinner loa;
    Button submit;
    TextView dob;

    String heightData;
    String weightData;
    String stepspermileData;
    String addressData;
    String postcodeData;
    String usernameData;
    String passwordData;
    String fnameData;
    String lnameData;
    String emailData;
    Integer levelOfActivity;
    String date;

    private DatePickerDialog.OnDateSetListener dateSetListener;


    JSONObject userData;
    JSONObject credentialData;
    Integer userId;


    static Boolean register = false;
    static Boolean userExists = false;

    RadioGroup groupGender;
    RadioButton buttonGender;
    String gender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        fname = (EditText) findViewById(R.id.et_firstname);
        lname = (EditText) findViewById(R.id.et_surname);
        email = (EditText) findViewById(R.id.et_email);
        height = (EditText) findViewById(R.id.et_height);
        weight = (EditText) findViewById(R.id.et_weight);
        stepspermile = (EditText) findViewById(R.id.et_stepspermile);
        address = (EditText) findViewById(R.id.et_address);
        postcode = (EditText) findViewById(R.id.et_postcode);
        username = (EditText) findViewById(R.id.et_username);
        password = (EditText) findViewById(R.id.et_password);
        submit = (Button) findViewById(R.id.btn_submit);
        dob = (TextView) findViewById(R.id.et_dob) ;

        groupGender = (RadioGroup) findViewById(R.id.radio_gender);
        //buttonGender = (Button) findViewById( R.id.)


        loa = (Spinner) findViewById(R.id.spinner_loa) ;

        ArrayAdapter<CharSequence> loaAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.levelOfActivity, android.R.layout.simple_dropdown_item_1line);

        loa.setAdapter(loaAdapter);
        loa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getId()==R.id.spinner_loa){
                    String item = parent.getItemAtPosition(position).toString();
                    levelOfActivity = Integer.parseInt(item);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                Integer year = cal.get(Calendar.YEAR);
                Integer month = cal.get(Calendar.MONTH);
                Integer day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(RegisterActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth
                        ,dateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });


        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String mt,dt;
                if(month < 10)
                    mt = "0"+month;
                else
                    mt = month+"";
                if(dayOfMonth<10)
                    dt= "0"+dayOfMonth;
                else
                    dt = dayOfMonth+"";
                //Log.d(, year + "/" + month + "/" + dayOfMonth);
                String dateOfBirth = year + "-" + mt + "-" + dt;
                dob.setText(dateOfBirth);
                date = dateOfBirth;
            }
        };



        loa = findViewById(R.id.spinner_loa);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                heightData = height.getText().toString();
                weightData = weight.getText().toString();
                stepspermileData = stepspermile.getText().toString();
                addressData = address.getText().toString();
                postcodeData = postcode.getText().toString();
                usernameData = username.getText().toString();
                passwordData = password.getText().toString();
                fnameData = fname.getText().toString();
                lnameData = lname.getText().toString();
                emailData = email.getText().toString();



                UserExists obj =  new UserExists();
                obj.execute();

                if(!userExists)
                {
                    Toast.makeText(getApplicationContext(),"Username Already Exits", Toast.LENGTH_SHORT);
                    obj =  new UserExists();
                    obj.execute();
                }
                //checkUserNameExists = checkUsername(usernameData);
            }
        });




    }
    public void radioChoice(View v){
        int id = groupGender.getCheckedRadioButtonId();
        buttonGender = findViewById(id);
        gender = buttonGender.getText().toString();
    }



    private class UserExists extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String result = RestMethods.checkUserExists(usernameData,emailData);
            userId = RestMethods.getId("userdetails/");
            setData();
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            TextView tv = (TextView) findViewById(R.id.textView);
            tv.setText(result);
            if(result.equals("Fail")) {
                userExists = true;
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        heightData = height.getText().toString();
                        weightData = weight.getText().toString();
                        stepspermileData = stepspermile.getText().toString();
                        addressData = address.getText().toString();
                        postcodeData = postcode.getText().toString();
                        usernameData = username.getText().toString();
                        passwordData = password.getText().toString();
                        fnameData = fname.getText().toString();
                        lnameData = lname.getText().toString();
                        emailData = email.getText().toString();

                        UserExists obj =  new UserExists();
                        obj.execute();

                        if(!userExists)
                        {
                            Toast.makeText(getApplicationContext(),"Username Already Exits", Toast.LENGTH_SHORT);
                            obj =  new UserExists();
                            obj.execute();
                        }

                        //checkUserNameExists = checkUsername(usernameData);
                    }
                });
            }

            else
            {
                RegistrationUser registration = new RegistrationUser();
                registration.execute();

            }
        }
    }


    private class RegistrationUser extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            int result = RestMethods.addUser(userData);
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(result >= 200 && result < 300)
            {
                RegistrationCredential registrationCredential = new RegistrationCredential();
                registrationCredential.execute();
            }
            else
                register = false;
        }
    }

    private class RegistrationCredential extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            int result = RestMethods.addCredential(credentialData);
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(result >= 200 && result < 300)
            {
                register = true;
                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
            }
            else
                register = false;
        }
    }

    public void setData()
    {
        userData = new JSONObject();
        credentialData = new JSONObject();

        try {
            userData.put("address",addressData);
            if (date.isEmpty())
                date = android.text.format.DateFormat.format("yyyy-MM-dd", new java.util.Date()).toString();
            userData.put("dateOfBirth",date);
            userData.put("email",emailData);
            userData.put("firstName",fnameData);
            userData.put("gender","male");
            userData.put("height",Integer.parseInt(heightData));
            userData.put("lastName",lnameData);
            userData.put("levelOfActivity",levelOfActivity);
            userData.put("postCode",Integer.parseInt(postcodeData));
            userData.put("stepsPerMile",Integer.parseInt(stepspermileData));
            userData.put("userId",userId);
            userData.put("weight",Integer.parseInt(weightData));

            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String hashedPassword = PasswordHasher(passwordData);
            credentialData.put("passwordHash",hashedPassword);
            credentialData.put("signupDate",date);
            credentialData.put("userId",userData);
            credentialData.put("userName",usernameData);



        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String PasswordHasher(String password) {
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
