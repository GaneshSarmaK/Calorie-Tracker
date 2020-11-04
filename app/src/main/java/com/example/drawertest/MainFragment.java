package com.example.drawertest;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainFragment extends Fragment {
    View vMain;
    EditText editText;
    Button button;
    TextView textView1,firstName;
    TextView textView2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vMain = inflater.inflate(R.layout.fragment_main, container, false);

        editText = (EditText) vMain.findViewById(R.id.editText);
        button = (Button) vMain.findViewById(R.id.button);
        textView1 = (TextView) vMain.findViewById(R.id.date);
        textView2 = (TextView) vMain.findViewById(R.id.time);
        firstName = (TextView)vMain.findViewById(R.id.userFName);
        SharedPreferences preferences = getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);

        firstName.setText("Welcome " + preferences.getString("firstName", "").toUpperCase());
        String date = android.text.format.DateFormat.format("dd-MM-yyyy", new java.util.Date()).toString() ;
        String time = android.text.format.DateFormat.format("hh:mm:ss a", new java.util.Date()).toString();

        textView1.setText(date);
        textView2.setText(time);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
                //preferences.edit().remove("calorieGoal").commit();
                SharedPreferences.Editor edit = preferences.edit();
                String data = editText.getText().toString();
                try
                {
                    if(Integer.parseInt(data) > 0)
                    {
                        edit.putInt("calorieGoal",Integer.parseInt(data));
                        edit.apply();
                        Toast.makeText(getContext(), "Calorie goal set to: "+ Integer.parseInt(data) + " cal", Toast.LENGTH_SHORT).show();

                    }
                    else
                        Toast.makeText(getContext(), "Invalid Format", Toast.LENGTH_SHORT).show();

                }catch(NumberFormatException e)
                {
                    Toast.makeText(getContext(), "Invalid Format", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });
        return vMain;
    }
}

