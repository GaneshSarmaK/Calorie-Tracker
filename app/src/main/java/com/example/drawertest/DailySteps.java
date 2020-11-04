package com.example.drawertest;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

public class DailySteps extends Fragment {
    View vDailySteps;
    DailyStepsDBDatabase db  = null;
    EditText editText = null;
    TextView textView_insert = null;
    TextView textView_read = null;
    TextView textView_delete = null;
    TextView textView_update = null;
    EditText timeText;
    EditText stepsText;

    TextView textView_insert1 = null;
    TextView textView_read1 = null;
    TextView textView_delete1 = null;
    TextView textView_update1 = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vDailySteps = inflater.inflate(R.layout.fragment_daily_steps, container, false);



        db = Room.databaseBuilder(getContext(),
                    DailyStepsDBDatabase.class, "DailyStepsDBDatabase")
                    .fallbackToDestructiveMigration()
                    .build();


            editText = (EditText) vDailySteps.findViewById(R.id.editText);
            timeText = (EditText) vDailySteps.findViewById(R.id.editText1);
            stepsText = (EditText) vDailySteps.findViewById(R.id.editText2);


        //ds
            Button addButton = (Button) vDailySteps.findViewById(R.id.addButton);
            textView_insert = (TextView) vDailySteps.findViewById(R.id.textView);
            Button readButton = (Button) vDailySteps.findViewById(R.id.readButton);
            textView_read = (TextView) vDailySteps.findViewById(R.id.textView_read);
            Button updateButton = (Button) vDailySteps.findViewById(R.id.updateButton);
            textView_update = (TextView) vDailySteps.findViewById(R.id.textView_update);


            //r
            addButton.setOnClickListener(new View.OnClickListener() {
                //including onClick() method
                public void onClick(View v) {
                    if(editText.getText().toString().isEmpty())
                        Toast.makeText(getContext(),"Please enter steps",Toast.LENGTH_SHORT).show();
                    else
                    {
                    InsertDatabaseDailySteps addDatabase = new InsertDatabaseDailySteps();
                    addDatabase.execute();
                    }
                }
            });

            readButton.setOnClickListener(new View.OnClickListener() {
                //including onClick() method
                public void onClick(View v) {
                    ReadDatabaseDailySteps readDatabase = new ReadDatabaseDailySteps();
                    readDatabase.execute();
                }
            });
            updateButton.setOnClickListener(new View.OnClickListener() {
                //including onClick() method
                public void onClick(View v) {
                    if(timeText.getText().toString().isEmpty() || stepsText.getText().toString().isEmpty() )
                        Toast.makeText(getContext(),"Please enter time and steps",Toast.LENGTH_SHORT).show();
                    else {
                        UpdateDatabaseDailySteps updateDatabase = new UpdateDatabaseDailySteps();
                        updateDatabase.execute();
                    }
                }
            });
            return vDailySteps;
        }



        private class InsertDatabaseDailySteps extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... params) {

                if (!(editText.getText().toString().isEmpty())) {
                    int stepsTaken = Integer.parseInt(editText.getText().toString());
                    if (stepsTaken > 0) {
                        String dateTime = getTime();
                        DailyStepsDB steps = new DailyStepsDB( dateTime, stepsTaken );
                        db.dailyStepsDBDao().insert(steps);
                        return (stepsTaken + " " + dateTime);
                    } else
                        return "";
                } else
                    return "";
            }

            @Override
            protected void onPostExecute(String details) {
                textView_insert.setText("Added Record: " + details);
            }
        }


        private class ReadDatabaseDailySteps extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... params) {
                List<DailyStepsDB> steps = db.dailyStepsDBDao().getAll();
                if (!(steps.isEmpty() || steps == null) ){
                    String allData = "";
                    for (DailyStepsDB temp : steps) {
                        //   stepsCount += temp.getStepsRecord();
                        String userstr = (temp.getReportTime() + "\t\t\t\t" + temp.getStepsRecord() +"\n" );
                        allData = allData + userstr;
                    }
                    return allData;
                }
                else
                    return "";
            }
            @Override
            protected void onPostExecute(String details) {
                textView_read.setText(details);
            }
        }
        private class DeleteDatabaseDailySteps extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... params) {
                db.dailyStepsDBDao().deleteAll();
                return null;
            }
            protected void onPostExecute(Void param) {
                textView_delete.setText("All data was deleted");
            }
        }
        private class UpdateDatabaseDailySteps extends AsyncTask<Void, Void, String> {
            @Override protected String doInBackground(Void... params) {
                DailyStepsDB steps=null;
                String details= timeText.getText().toString();
                int stepsTaken =Integer.parseInt( stepsText.getText().toString());
                    steps = db.dailyStepsDBDao().findDailyStepsDB(details);
                    steps.setStepsRecord(stepsTaken);
                if (steps!=null) {
                    try{
                        db.dailyStepsDBDao().updateDailyStepsDB(steps);
                        return (details + " " + stepsTaken);

                    }
                    catch (Exception e)
                    {                    }
                }
                return "";
            }
            @Override
            protected void onPostExecute(String details) {
                textView_update.setText("Updated details: "+ details);
            }
        }

        private class GetTotalStepsPerDay extends AsyncTask<Void, Void, String> {

            String[] data = editText.getText().toString().split(" ");
            int id = Integer.parseInt(data[1]);
            String reportTime = data[0];


            @Override
            protected String doInBackground(Void... params) {
                List<DailyStepsDB> steps = db.dailyStepsDBDao().getAll();
                if (!(steps.isEmpty() || steps == null) ){
                    String allUsers = "";
                    int stepsCount = 0;
                    for (DailyStepsDB temp : steps) {
                        if(temp.getReportTime().equals(reportTime))
                            stepsCount += temp.getStepsRecord();
                        String userstr = (temp.getStepsRecord() + " " + temp.getReportTime() +" , " );
                        allUsers = allUsers + userstr;
                    }
                    return Integer.toString(stepsCount);
                }
                else
                    return "";
            }
            @Override
            protected void onPostExecute(String details) {
                textView_read.setText("All data: " + details);
            }
        }

        public String getTime()
        {
            return android.text.format.DateFormat.format("HH:mm:ss", new java.util.Date()).toString();
        }
    }