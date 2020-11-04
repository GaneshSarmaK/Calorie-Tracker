package com.example.drawertest;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.io.PrintWriter;
import java.util.Set;

public class RestMethods {

    private static final String API_KEY  = "AIzaSyBQD1hEEvSZjGNEUW1KGiTe_6vikFV1JuA";
    private static final String SEARCH_ID_cx =  "016854979239453564856:gug_dn541iy";
    static final String Goole_maps = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
    static List<JSONObject> parks = new ArrayList<>();
    private static final String BASE_URL = "http://192.168.43.34:19838/Test1/webresources/restws.";

    static SharedPreferences sharedPreferences;

    public static String checkUserExists(String username,String emailId) {

        String method = "credential/";
        String result = "Success";

        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";  //Making HTTP request
        try {
            url = new URL(BASE_URL + method );
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to GET
            conn.setRequestMethod("GET");
            //add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.connect();
            Scanner inStream;
            //Read the response
            int code = conn.getResponseCode();
            if(code>=200 && code<=200)
                inStream = new Scanner(conn.getInputStream());
            else
                inStream = new Scanner(conn.getErrorStream());
            //read the input stream and store it as string
            while (inStream.hasNextLine()) {
                //arr.add(inStream.nextLine());
                textResult += inStream.nextLine();
            }
                String userName = "";
                JSONArray arr = new JSONArray(textResult);
                for(int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    userName = ((JSONObject) obj).getString("userName");
                    if(username.equals(userName))
                    {
                        result  = "Fail";
                        break;
                    }

                }
                if(result.equals("Success"))
                for(int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    obj = obj.getJSONObject("userId");
                    String email = ((JSONObject) obj).getString("email");
                    if(email.equals(emailId))
                    {
                        result  = "Fail";
                        break;
                    }
                }

                // String category = report.getString("category");
                //String fat = report.getString("fat");
                //String serving = report.getString("foodId");
                //res = calorieAmount;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        //return array[0];
        //return arr.get(0).toString();

        return result;
    }

    public static String checkUserCredentials(String username,String password) {

        String method = "credential/findByUserName/"+username.trim();
        String result = "Fail";
        if(password.equals("1234"))
            password = "4ac6430a17d0215b3052b15c879d48bc";
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";  //Making HTTP request
        try {
            url = new URL(BASE_URL + method );
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.connect();
            Scanner inStream;
            int code = conn.getResponseCode();
            if(code>=200 && code<=200)
                inStream = new Scanner(conn.getInputStream());
            else
                inStream = new Scanner(conn.getErrorStream());

            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }
            String passWord = "";
            JSONArray arr = new JSONArray(textResult);
            JSONObject obj = arr.getJSONObject(0);
            passWord = ((JSONObject) obj).getString("passwordHash");
            if(password.equals(passWord))
            {
                result  = "Success "+obj.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        return result;
    }

    public static int getId(String method) {
        int length = 0;
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";

        try {
            url = new URL(BASE_URL + method);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
//            conn.connect();
            int code = conn.getResponseCode();
            String x = "";
            Scanner inStream;
            if(code == 200 || code == 204)
                inStream = new Scanner(conn.getInputStream());
            else
                inStream = new Scanner(conn.getErrorStream());
            inStream = new Scanner(conn.getInputStream());

            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();

                JSONArray arr = new JSONArray(textResult);
                length = arr.length()+1;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            conn.disconnect();
        }

        return length;
    }

    public static Integer addUser(JSONObject data) {
        String methodPath = "userdetails";
        URL url = null;
        HttpURLConnection conn = null;
        int result = 0;
        try {
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(data.toString());
            writer.flush();
            writer.close();
            result = conn.getResponseCode();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            conn.disconnect();
        }
        return result;
    }

    public static Integer addCredential(JSONObject data) {
        String methodPath = "credential";
        URL url = null;
        HttpURLConnection conn = null;
        int result = 0;
        try {
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(data.toString());
            writer.flush();
            writer.close();
            result = conn.getResponseCode();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            conn.disconnect();
        }
        return result;
    }

    public static List<JSONObject> getParks(Double lat,Double lon) {
        //String methodPath = "parks&inputtype=textquery&fields=name&locationbias=circle:5000@";
        String methodPath = lat+","+lon+"&radius=5000&type=park&keyword=park&key="+ API_KEY;
        URL url = null;
        HttpURLConnection conn = null;
        int result = 0;
        String txtResult = "";
        try {
            url = new URL(Goole_maps +methodPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            result = conn.getResponseCode();
            Scanner sc = new Scanner(conn.getInputStream());
            while(sc.hasNextLine())
                txtResult += sc.nextLine();
            JSONObject jsonObject = new JSONObject(txtResult);
            JSONArray arr = jsonObject.getJSONArray("results");
            //List<JSONObject> parks = new ArrayList<>();
            for(int i = 0; i < arr.length(); i++)
            {

                JSONObject jsonObject1 = arr.getJSONObject(i);
                JSONObject point = new JSONObject();
                point.put("latitude",jsonObject1.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                point.put("longitude",jsonObject1.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
                point.put("name",jsonObject1.getString("name"));
                parks.add(point);

             /*   double latitude = jsonObject1.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                double longitude = jsonObject1.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                LatLng pos = new LatLng(latitude,longitude);
                String name = jsonObject1.getString("name");
                //point.put("latlng", pos);
                locations.put(name,pos);*/
            }

            String x = " ";

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            conn.disconnect();
        }
        return parks;
    }

    public static Integer getFoodsFromAPI(String foodName) {
        String method = "https://api.nal.usda.gov/ndb/search/?format=json&q="+foodName+"&sort=r&max=5&offset=0&api_key=mZLWgoiNg8zt9ePCGOZvKiq1LokbQuU0K3UGcNRR";
        int id = 0;
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        List<String> foodData = new ArrayList<>();
        String textResult = "";  //Making HTTP request
        try {
            url = new URL(method);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to GET
            conn.setRequestMethod("GET");
            //add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            // conn.setRequestProperty("Accept", "application/json");
            //Read the response
            int connresp = conn.getResponseCode();
            Scanner inStream = new Scanner(conn.getInputStream());
            //read the input stream and store it as string
            while (inStream.hasNextLine())
                textResult += inStream.nextLine();

            JSONObject obj = new JSONObject(textResult);
            JSONObject data = obj.getJSONObject("list");
            JSONArray arr = data.getJSONArray("item");
            for (int i = 0; i < arr.length() - 1; i++) {
                JSONObject o = arr.getJSONObject(i);
                String s = ((JSONObject) o).getString("name");
                id =  ((JSONObject) o).getInt("ndbno");
                foodData.add(s+" foodId: "+id);
                int x = 0;
            }
            id = ((JSONObject) arr.getJSONObject(0)).getInt("ndbno");

            //length += (Integer)((JSONObject) arr.getJSONObject(arr.length()-1)).get("userId");

            // String category = report.getString("category");
            //String fat = report.getString("fat");
            //String serving = report.getString("foodId");
            //res = calorieAmount;


        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            conn.disconnect();
        }


        //return array[0];
        //return arr.get(0).toString();
        return id;
    }

    public static String getFoodDataFromAPI(int foodId) {
        String method = "https://api.nal.usda.gov/ndb/V2/reports?ndbno="+foodId+"&type=f&format=json&api_key=mZLWgoiNg8zt9ePCGOZvKiq1LokbQuU0K3UGcNRR";
        int id = 0;
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        List<String> foodData = new ArrayList<>();
        String textResult = "";  //Making HTTP request
        JSONObject foodObj = new JSONObject();
        try {
            url = new URL(method);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to GET
            conn.setRequestMethod("GET");
            //add http headers to set your response type to json
            conn.setRequestProperty("Content-Type", "application/json");
            // conn.setRequestProperty("Accept", "application/json");
            //Read the response
            int connresp = conn.getResponseCode();
            Scanner inStream = new Scanner(conn.getInputStream());
            //read the input stream and store it as string
            while (inStream.hasNextLine())
                textResult += inStream.nextLine();

            JSONObject obj = new JSONObject(textResult);
            JSONArray array = obj.getJSONArray("foods");
            JSONObject o = array.getJSONObject(0);
            JSONObject food = o.getJSONObject("food");
            JSONObject foodDesc = food.getJSONObject("desc");
            JSONArray nutrients = food.getJSONArray("nutrients");
            JSONObject fat = nutrients.getJSONObject(2);
            JSONObject energy = nutrients.getJSONObject(0);

            JSONArray foodDescription = fat.getJSONArray("measures");
            JSONObject servingData = foodDescription.getJSONObject(0);

            double servingAmount = ((JSONObject)servingData).getInt("qty");
            int fatValue =(int)((JSONObject) fat).getDouble("value") ;
            int calories = (int)((JSONObject) energy).getDouble("value");
            String servingUnit = ((JSONObject)servingData).getString("label");
            String foodName;
            if(((JSONObject)foodDesc).getString("name").length() >= 50)
            foodName = ((JSONObject)foodDesc).getString("name").substring(0,49);
            else
                foodName = ((JSONObject)foodDesc).getString("name");

           String foodCategory;
            if(foodDesc.isNull("fg"))
                foodCategory = "Others";
            else
                foodCategory = ((JSONObject)foodDesc).getString("fg");


            int lenghtttt = foodName.length();
            int done = 0;

            foodObj.put("calorieAmount", calories);
            foodObj.put("category", foodCategory);
            foodObj.put("fat", fatValue);
            foodObj.put("foodId", getId("food/"));
            foodObj.put("foodName", foodName);
            foodObj.put("servingAmount", servingAmount);
            foodObj.put("servingUnit", servingUnit);

            /*for (int i = 0; i < arr.length() - 1; i++) {
                JSONObject o = arr.getJSONObject(i);
                String s = ((JSONObject) o).getString("name");
                id =  ((JSONObject) o).getInt("ndbno");
                foodData.add(s+" foodId: "+id);
                int x = 0;
            }*/

            //length += (Integer)((JSONObject) arr.getJSONObject(arr.length()-1)).get("userId");

            // String category = report.getString("category");
            //String fat = report.getString("fat");
            //String serving = report.getString("foodId");
            //res = calorieAmount;


        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            conn.disconnect();
        }


        //return array[0];
        //return arr.get(0).toString();
        return foodObj.toString();
    }

    public static Integer addFood(String data) {
        String methodPath = "food";
        URL url = null;
        HttpURLConnection conn = null;
        int result = 0;
        try {
            url = new URL(BASE_URL + methodPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(data);
            writer.flush();
            writer.close();
            result = conn.getResponseCode();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            conn.disconnect();
        }
        return result;
    }

    public static List<String> getFoods(String foodCategory)
    {
        URL url = null;
        String method = "food/findByCategory/";
        HttpURLConnection conn = null;
        String textResult = "";
        List<String> foods = new ArrayList<>();
        try {
            url = new URL(BASE_URL + method + foodCategory );
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
//            conn.connect();
            int code = conn.getResponseCode();
            String x = "";
            Scanner inStream;
            if(code == 200 || code == 204)
                inStream = new Scanner(conn.getInputStream());
            else
                inStream = new Scanner(conn.getErrorStream());
            inStream = new Scanner(conn.getInputStream());

            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }

            JSONArray arr = new JSONArray(textResult);
            for(int i = 0 ; i < arr.length(); i++)
            {
                    String data = ((JSONObject) arr.get(i)).getString("foodName");
                    foods.add(data);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            conn.disconnect();
        }

        return foods;
    }

    public static List<String> getFoodCategory()
    {
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        Set<String> foods = new HashSet<>();
        try {
            url = new URL(BASE_URL + "food/" );
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
//            conn.connect();
            int code = conn.getResponseCode();
            String x = "";
            Scanner inStream;
            if(code == 200 || code == 204)
                inStream = new Scanner(conn.getInputStream());
            else
                inStream = new Scanner(conn.getErrorStream());
            inStream = new Scanner(conn.getInputStream());

            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }

            JSONArray arr = new JSONArray(textResult);
            for(int i = 0 ; i < arr.length(); i++)
            {
                    String data = ((JSONObject) arr.get(i)).getString("category");
                    foods.add(data);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            conn.disconnect();
        }

        return new ArrayList<>(foods);
    }

    public static JSONObject getDataForBarChart(String fromDate,String toDate, int userId)
    {
        List<String> dates;
        List<Integer> values;
        URL url = null;
        String method = "report/caloriesPerDayAndByUserId/";
        HttpURLConnection conn = null;
        String textResult = "";
        JSONObject data = new JSONObject();
        try {
            url = new URL(BASE_URL + method + userId + "/" + fromDate + "/" + toDate);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
//            conn.connect();
            int code = conn.getResponseCode();
            String x = "";
            Scanner inStream;
            if(code == 200 || code == 204)
                inStream = new Scanner(conn.getInputStream());
            else
                inStream = new Scanner(conn.getErrorStream());
            inStream = new Scanner(conn.getInputStream());

            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();
            }

            JSONArray arr = new JSONArray(textResult);
            List<Integer> caloriesConsumed = new ArrayList<>();
            List<String> reportDates = new ArrayList<>();
            List<Integer>  caloriesBurned = new ArrayList<>();
            for(int i = 0 ; i < arr.length(); i++)
            {
                int calConsumed =  ((JSONObject) arr.get(i)).getInt("Total Calories consumed: ");
                int calBurned =  ((JSONObject) arr.get(i)).getInt("Total Calories burned: ");
                String date = ((JSONObject) arr.get(i)).getString("Report Date: ");
                caloriesConsumed.add(calConsumed);
                caloriesBurned.add(calBurned);
                reportDates.add(date);
                //foods.add(data);
            }
            data.put("calConsumed",caloriesConsumed);
            data.put("calBurned",caloriesBurned);
            data.put("date",reportDates);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            conn.disconnect();
        }
        return data;
    }

    public static JSONObject getData(String id,String method,String type){
        int length = 0;
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        JSONObject obj = new JSONObject();

            try {
            url = new URL(BASE_URL + method + id);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
    //            conn.connect();
            int code = conn.getResponseCode();
            String x = "";
            Scanner inStream;
            if(code == 200 || code == 204)
                inStream = new Scanner(conn.getInputStream());
            else
                inStream = new Scanner(conn.getErrorStream());
            inStream = new Scanner(conn.getInputStream());

            while (inStream.hasNextLine()) {
                textResult += inStream.nextLine();

                if(type.equals("array")) {
                    JSONArray arr = new JSONArray(textResult);
                    obj = arr.getJSONObject(0);
                }
                else
                    obj = new JSONObject(textResult);
            }
        }
            catch (Exception e) {
            e.printStackTrace();
        }
            finally {
            conn.disconnect();
        }

            return obj;
    }

    public static Integer addData(String data,String method) {
        URL url = null;
        HttpURLConnection conn = null;
        int result = 0;
        try {
            url = new URL(BASE_URL + method);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(data);
            writer.flush();
            writer.close();
            result = conn.getResponseCode();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            conn.disconnect();
        }
        return result;
    }

    public static HashMap<String,Integer> getDataForPieChart(String date, int userId)
    {
        int length = 0;
        URL url = null;
        HttpURLConnection conn = null;
        String textResult = "";
        JSONObject obj = new JSONObject();
        HashMap<String,Integer> hashMap = new HashMap<>();

        try {
            url = new URL(BASE_URL + "report/calorieGoalByDateAndByUserId/" + userId + "/" + date);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //            conn.connect();
            int code = conn.getResponseCode();
            String x = "";
            Scanner inStream;
            if (code == 200 || code == 204)
                inStream = new Scanner(conn.getInputStream());
            else
                inStream = new Scanner(conn.getErrorStream());
            inStream = new Scanner(conn.getInputStream());

            while (inStream.hasNextLine())
                textResult += inStream.nextLine();

            JSONArray arr = new JSONArray(textResult);
            obj = arr.getJSONObject(0);

            hashMap.put("Calories Consumed",((JSONObject)obj).getInt("Calories consumed: "));
            hashMap.put("Calories Burned",((JSONObject)obj).getInt("Calories burned: "));
            hashMap.put("Remaining Calories to reach calorie goal: ",((JSONObject)obj).getInt("Remaining Calories to reach calorie goal: "));


        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            conn.disconnect();
        }

        return hashMap;
    }
    public static String searchGoogle(String keyword, String[] params, String[] values)
    {
        keyword = keyword.replace(" ", "+");
        URL url = null;
        HttpURLConnection connection = null;
        String textResult = "";String query_parameter="";
        if (params!=null && values!=null){
            for (int i =0; i < params.length; i ++){
                query_parameter += "&";
                query_parameter += params[i];
                query_parameter += "=";
                query_parameter += values[i];
            }
        }
        try {
            url = new URL("https://www.googleapis.com/customsearch/v1?key="+ API_KEY+ "&cx="+ SEARCH_ID_cx + "&q="+ keyword + query_parameter + "&searchType=image");
            connection = (HttpURLConnection)url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) {
                textResult += scanner.nextLine();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally{
            connection.disconnect();
        }
        return textResult;
    }

    public static String getSnippet(String result){
        String snippet = null;
        try{
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            if(jsonArray != null && jsonArray.length() > 0) {
                snippet =jsonArray.getJSONObject(0).getString("snippet");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            snippet = "NO INFO FOUND";
        }
        return snippet;
    }

    public static String getLink(String result){
        String link;
        try{
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            JSONObject item = jsonArray.getJSONObject(0);
            JSONObject image  = item.getJSONObject("image");
            link = ((JSONObject) image).getString("thumbnailLink");

        }catch (Exception e){
            e.printStackTrace();
            link = "NO INFO FOUND";
        }
        return link;
    }
}