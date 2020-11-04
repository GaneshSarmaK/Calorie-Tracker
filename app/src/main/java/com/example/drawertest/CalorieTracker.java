package com.example.drawertest;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CalorieTracker extends Fragment {

    View vCalorieTracker;
    TextView tvCalConumed;
    TextView tvCalBurned;
    TextView tvSteps;
    TextView tvGoal;
    static  SharedPreferences preferences ;
    DailyStepsDBDatabase db  = null;
    static int totalSteps ;
    JSONObject report;
    Button addReport;
    static int calConsumed;
    static int calBurned;
    static int setGoal;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vCalorieTracker = inflater.inflate(R.layout.fragment_calorie_tracker, container, false);
        tvCalBurned = (TextView) vCalorieTracker.findViewById(R.id.calBurned) ;
        tvCalConumed = (TextView) vCalorieTracker.findViewById(R.id.calConsumed) ;
        tvGoal = (TextView) vCalorieTracker.findViewById(R.id.goal) ;
        tvSteps = (TextView) vCalorieTracker.findViewById(R.id.steps) ;
        addReport = (Button) vCalorieTracker.findViewById(R.id.button);

        db = Room.databaseBuilder(getContext(),
                DailyStepsDBDatabase.class, "DailyStepsDBDatabase")
                .fallbackToDestructiveMigration()
                .build();

        GetCaloriesBurned obj = new GetCaloriesBurned();
        obj.execute();


        preferences = getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);

        addReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostToReport obj = new PostToReport();
                obj.execute(report);

            }
        });
        return vCalorieTracker;
    }

    private class GetCaloriesBurned extends AsyncTask<Integer, Void, Double> {
        @Override
        protected Double doInBackground(Integer... params) {
            preferences = getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
            int id = preferences.getInt("userId",0);
            JSONObject obj = RestMethods.getData(id+"","userdetails/calculateCaloriesBurntAtRest/","array");
            JSONObject obj2 = RestMethods.getData(id+"","userdetails/calculateCaloriesBurntPerStep/","array");
            double calBurned = 0;
            try {
                double calAtRest = obj.getDouble("Calories burnt at Rest:");
                double catPerStep = ((JSONObject)obj2).getDouble("Calories burnt per step:");
                calBurned = totalSteps*catPerStep+calAtRest;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return calBurned;
        }

        @Override
        protected void onPostExecute(Double calories) {
            tvCalBurned.setText(calories.intValue()+"");
            calBurned = calories.intValue();
            ReadDatabaseDailySteps steps = new ReadDatabaseDailySteps();
            steps.execute();
        }
    }
    private class ReadDatabaseDailySteps extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            List<DailyStepsDB> steps = db.dailyStepsDBDao().getAll();
            if (!(steps.isEmpty() || steps == null) ){
                int allData = 0;
                for (DailyStepsDB temp : steps) {
                    //   stepsCount += temp.getStepsRecord();
                   allData += temp.getStepsRecord();
                }
                return allData;
            }
            else
                return 0;
        }
        @Override
        protected void onPostExecute(Integer details) {
            totalSteps = details;
            tvSteps.setText(details+"");
            GetCaloriesConsumed obj = new GetCaloriesConsumed();
            obj.execute();
        }
    }

    private class GetCaloriesConsumed extends AsyncTask<Integer, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Integer... params) {
            preferences = getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
            int id = preferences.getInt("userId",0);
            String date = android.text.format.DateFormat.format("yyyy-MM-dd", new java.util.Date()).toString();
            return RestMethods.getData(id+"/"+date,"consumption/CaloriesConsumedByUserInOneDay/","array");
        }

        @Override
        protected void onPostExecute(JSONObject data) {
            int calories = 0;
            try {
                calories = ((JSONObject)data).getInt("Total calories consumed: ");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            tvCalConumed.setText(calories+"");
            String a = tvCalBurned.getText().toString();
            preferences = getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
            tvGoal.setText(preferences.getInt("calorieGoal",0)+"");
            calConsumed = calories;
            setGoal = preferences.getInt("calorieGoal",0);
        }


    }


    private class PostToReport extends AsyncTask<JSONObject, Void, Integer> {
        @Override
        protected Integer doInBackground(JSONObject... params) {
            int reportId = RestMethods.getId("report");
            report = new JSONObject();
            int userId = preferences.getInt("userId",0);
            JSONObject userData = RestMethods.getData(userId+"","userdetails/","object");
            try{
                report.put("calorieGoal",setGoal);
                report.put("caloriesBurned",calBurned);
                report.put("caloriesConsumed",calConsumed);
                report.put("reportDate",android.text.format.DateFormat.format("yyyy-MM-dd", new java.util.Date()).toString());
                report.put("reportId",reportId);
                report.put("stepsTaken",totalSteps);
                report.put("userId",userData);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return RestMethods.addData(report.toString(),"report");
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(result == 204) {
                Toast.makeText(getContext(), "Report Successful", Toast.LENGTH_SHORT).show();
                DeleteData obj = new DeleteData();
                obj.execute();
            }
            else
                Toast.makeText(getContext(), "There was an error.", Toast.LENGTH_SHORT).show();

        }

        private class DeleteData extends AsyncTask<JSONObject, Void, Void> {
            @Override
            protected Void doInBackground(JSONObject... params) {

                SharedPreferences preferences = getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
                //preferences.edit().remove("calorieGoal").commit();
                SharedPreferences.Editor edit = preferences.edit();
                edit.putInt("calorieGoal",0);
                edit.apply();

                db.dailyStepsDBDao().deleteAll();

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {

            }


        }
    }
}
