package com.example.drawertest;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DailyDiet extends Fragment {
    View vDailyDiet;
    Button addFood;
    Button addFoodFromSPinner;
    EditText textData;
    EditText servingData;
    EditText servingData2;
    Spinner category;
    Spinner food;
    String foodSelected = "";
    String foodName = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vDailyDiet = inflater.inflate(R.layout.fragment_daily_diet, container, false);

        category = (Spinner) vDailyDiet.findViewById(R.id.foodCategory);
        food = (Spinner) vDailyDiet.findViewById(R.id.food);
        servingData = (EditText) vDailyDiet.findViewById(R.id.editTextServing);
        servingData2 = (EditText) vDailyDiet.findViewById(R.id.spinnerServings);


        LoadFoodCategory obj = new LoadFoodCategory();
        obj.execute();

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categorySelected = parent.getItemAtPosition(position).toString();
                if(categorySelected != null){
                    Toast.makeText(parent.getContext(),"Food category selected is " + categorySelected,Toast.LENGTH_LONG).show();
                    LoadFoods obj = new LoadFoods();
                    obj.execute(categorySelected);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        food.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                foodSelected = parent.getItemAtPosition(position).toString();
                if(foodSelected != null){
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        addFoodFromSPinner = (Button) vDailyDiet.findViewById(R.id.addFood2);
        addFood = (Button) vDailyDiet.findViewById(R.id.addFood);
        textData = (EditText) vDailyDiet.findViewById(R.id.editText);
        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodName = textData.getText().toString();
                FoodAsyncTask check = new FoodAsyncTask();
                check.execute(foodName);

            }
        });

        addFoodFromSPinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetFoodData obj = new SetFoodData();
                obj.execute(foodSelected,"getFood");
            }
        });






        return vDailyDiet;

    }

        private class FoodAsyncTask extends AsyncTask<String, Void, Integer> {
            @Override
            protected Integer doInBackground(String... params) {
                return RestMethods.getFoodsFromAPI(params[0].replace(" ","+"));
            }

            @Override
            protected void onPostExecute(Integer s){

                GetFoodData obj = new GetFoodData();
                obj.execute(s);
            /*TextView resultTextView = (TextView) findViewById(R.id.result);
            resultTextView.setText(s+"");*/
            }
        }

        private class GetFoodData extends AsyncTask<Integer, Void, String> {
            @Override
            protected String doInBackground(Integer... params) {
                return RestMethods.getFoodDataFromAPI( params[0]);
            }

            @Override
            protected void onPostExecute(String foodData) {
                SetFoodData obj = new SetFoodData();
                obj.execute(foodData,"addFood");
                SearchAsyncTask search = new SearchAsyncTask();
                search.execute(foodName);
            }
        }

        private class SetFoodData extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                int result;
                int foodId;
                JSONObject food;
                double servings;
                if(params[1].equals("addFood")) {
                    result = RestMethods.addFood(params[0]);
                    foodId = RestMethods.getId("food") - 1;
                    food = RestMethods.getData(foodId + "", "food/","object");
                }
                else
                {
                    result = 204;
                    food = RestMethods.getData(params[0],"food/findByFoodName/","array");
                }
                int consumptionId = RestMethods.getId("consumption");
                SharedPreferences preferences = getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
                int userId = preferences.getInt("userId",0);
                JSONObject user = RestMethods.getData(userId+"","userdetails/","object");
                JSONObject object = new JSONObject();
                if(result == 204)
                {
                    // Toast.makeText(getContext(),"Food added to Database", Toast.LENGTH_SHORT).show();

                    try {
                        if(params[1].equals("addFood"))
                            servings = Double.parseDouble(servingData.getText().toString());
                        else
                            servings = Double.parseDouble(servingData2.getText().toString());

                        object.put("consumptionDate" ,new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                        object.put("consumptionId", consumptionId);
                        object.put("foodId",food);
                        object.put("quantity",servings);
                        object.put("userId",user);


                    }
                    catch (NumberFormatException e)
                    {
                        e.printStackTrace();
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }


                return object.toString();
            }

            @Override
            protected void onPostExecute(String object) {

                AddDataToConsumption obj = new AddDataToConsumption();
                obj.execute(object);
            }
        }


    private class LoadFoodCategory extends AsyncTask<Void, Void, List<String>> {
        @Override
        protected List<String> doInBackground(Void... params) {

            return RestMethods.getFoodCategory();
        }

        @Override
        protected void onPostExecute(List<String> result) {
            final ArrayAdapter<String> foodCategoryAdapter = new ArrayAdapter<>(getContext() ,android.R.layout.simple_spinner_item, result);
            foodCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            category.setAdapter(foodCategoryAdapter);
        }
    }

       private class AddDataToConsumption extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... params) {

            //RestMethods.getFoods(params[0]);
            return RestMethods.addData(params[0], "consumption");
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(result == 204)
            {
                Toast.makeText(getContext(),"Food added to consumptions", Toast.LENGTH_SHORT).show();

            }
            else
                Toast.makeText(getContext(),"There was an error when adding requested Food", Toast.LENGTH_SHORT).show();

        }
    }

    private class LoadFoods extends AsyncTask<String, Void, List<String>> {
        @Override
        protected List<String> doInBackground(String... params) {

            return RestMethods.getFoods(params[0]);
        }

        @Override
        protected void onPostExecute(List<String> result) {
            final ArrayAdapter<String> foodSpinnerAdapter = new ArrayAdapter<>(getContext() ,android.R.layout.simple_spinner_item, result);
            foodSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            food.setAdapter(foodSpinnerAdapter);


        }
    }

    private class SearchAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return RestMethods.searchGoogle(params[0], new String[]{"num"}, new String[]{"1"});
        }
        @Override
        protected void onPostExecute(String result) {
            ImageView googleImage = vDailyDiet.findViewById(R.id.foodImage);
            TextView tv= (TextView) vDailyDiet.findViewById(R.id.foodData);
            String text = RestMethods.getLink(result);
            String snippet = RestMethods.getSnippet(result);
            tv.setText(snippet);
            Picasso.with(getContext()).load(text).into(googleImage);
        }
    }

}



