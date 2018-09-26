package com.lanihuang.simplewebapp.scrapper;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lanihuang.simplewebapp.constants.Constant;
import com.lanihuang.simplewebapp.entity.Entity;
import org.jsoup.Connection;
import org.jsoup.Jsoup;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Scrapper {
    private Properties properties = new Properties();

    private JsonParser jsonParser = new JsonParser();
    private String authorization ;
    private final String USERS_LIST_API = "https://api.trainerize.com/v03/user/getList";
    private final String WORKOUT_LIST_API = "https://api.trainerize.com/v03/calendar/getList";
    private final String BODY_STATS_API = "https://api.trainerize.com/v03/bodystats/get";
    private final String DAILY_NUTRITION_API = "https://api.trainerize.com/v03/dailyNutrition/Get";
    private final String MEAL_PLAN_API = "https://api.trainerize.com/v03/mealPlan/get";
    public Scrapper() {
//        String propertyFileName = System.getProperty("user.dir") + "/config.properties";
//        InputStream input = null;
//        try {
//            input = new FileInputStream(propertyFileName);
//            properties.load(input);
//            authorization = properties.getProperty("authorization");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
    public List<Entity> scrapTrainingData(String date)
    {
        String usersJsonString = makeApiReques(USERS_LIST_API, usersListRequestBody());
        
        List<Entity> entityList = extractEntities(usersJsonString);
        
        for (int i = 0; i < entityList.size(); i++) 
        {
            String nutritionString = makeApiReques(DAILY_NUTRITION_API, dailyNutritionRequestBody(entityList.get(i).getValue(Constant.CLIENT_ID), date));
            addNutrition(entityList.get(i), nutritionString);
            String bodyStatsString = makeApiReques(BODY_STATS_API,bodyStatusRequestBody(entityList.get(i).getValue(Constant.CLIENT_ID),date));
            addBodyMeasures(entityList.get(i),bodyStatsString);
            String mealPlan = makeApiReques(MEAL_PLAN_API,mealPlanRequestBody(entityList.get(i).getValue(Constant.CLIENT_ID)));
            addGoal(entityList.get(i),mealPlan);
            String workOut = makeApiReques(WORKOUT_LIST_API,workoutRequestBody(entityList.get(i).getValue(Constant.CLIENT_ID),date));
            addWorkOut(entityList.get(i),workOut);
            entityList.get(i).addAttribute(Constant.DATE,date);
        }
        return entityList;
    }

    private List<Entity> extractEntities(String json)
    {
        JsonElement jsonElement = jsonParser.parse(json);
        JsonElement usersElement = getJsonElement(jsonElement.getAsJsonObject(), "users");
        List<Entity> entityList = new ArrayList<>();
        if (usersElement != null) {
            JsonArray users = usersElement.getAsJsonArray();
            for (int i = 0; i < users.size(); i++) {
                Entity entity = new Entity();
                JsonObject userObject = users.get(i).getAsJsonObject();
                String id = getJsonString(userObject, Constant.CLIENT_ID);
                String name = getJsonString(userObject, Constant.NAME);
                String email = getJsonString(userObject, Constant.EMAIL);
                entity.addAttribute(Constant.CLIENT_ID, id);
                entity.addAttribute(Constant.NAME, name);
                entity.addAttribute(Constant.EMAIL, email);
                entityList.add(entity);
            }
        }
        return entityList;
    }

    // NutritionRequestBody , mealPlanRequestBody ,bodyStatusRequestBody
    private void addNutrition(Entity entity, String nutritionString) 
    {
        try 
        {
            JsonElement nutritionElementWrapper = jsonParser.parse(nutritionString);
            JsonElement nutritionElement = getJsonElement(nutritionElementWrapper.getAsJsonObject(),"nutrition");
            
            if (nutritionElement != null) 
            {
                JsonObject nutritionObject = nutritionElement.getAsJsonObject();
                
                String calories = getJsonString(nutritionObject, "calories");
                String protein = getJsonString(nutritionObject, "proteinGrams");
                String carbs = getJsonString(nutritionObject, "carbsGrams");
                String fat = getJsonString(nutritionObject, "fatGrams");
                
                entity.addAttribute(Constant.TOTAL_CALORIES, calories);
                entity.addAttribute(Constant.TOTAL_PROTEIN, protein);
                entity.addAttribute(Constant.TOTAL_CARBOHYDRATES, carbs);
                entity.addAttribute(Constant.TOTAL_FATS, fat);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void addBodyMeasures(Entity entity,String json)
    {
        try 
        {
            JsonElement bodyMeasuresElement = jsonParser.parse(json);
            JsonElement measuresElement = getJsonElement(bodyMeasuresElement.getAsJsonObject(), "bodyMeasures");
            if (measuresElement != null)
            {
                JsonObject measuresObject = measuresElement.getAsJsonObject();
                entity.addAttribute(Constant.WEIGHT, getJsonString(measuresObject, "bodyWeight"));
                entity.addAttribute(Constant.WAIST, getJsonString(measuresObject, "waist"));
            }
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void addWorkOut(Entity entity,String json)
    {
        try
        {
            JsonElement bodyMeasuresElement = jsonParser.parse(json);
            JsonElement measuresElement = getJsonElement(bodyMeasuresElement.getAsJsonObject(),"calendar");
            if(measuresElement!=null && measuresElement.getAsJsonArray().size()>0 )
            {
                JsonArray calendars = measuresElement.getAsJsonArray();
                for(int i=0;i<calendars.size();i++)
                {
                    JsonObject calendarObject = calendars.get(i).getAsJsonObject();
                    JsonElement itemElements = getJsonElement(calendarObject,"items");
                    if(itemElements !=null)
                    {
                        JsonArray items  = itemElements.getAsJsonArray();
                        for(int n=0;n<items.size();n++) 
                        {
                            JsonObject itemObject = items.get(n).getAsJsonObject();
                            String type = getJsonString(itemObject, "type");
                            if (type != null && (type.equals("workoutInterval") || type.equals("workoutRegular"))) 
                            {
                                String status = getJsonString(itemObject, "status");
                                if (status.equals("checkedIn")) 
                                {
                                    entity.addAttribute(Constant.WORKOUTCOMPLETE, Constant.WORKOUTCOMPLETE);
                                }
                            }
                        }
                    }
                }
            }
     /* JsonObject measuresObject = measuresElement.getAsJsonObject();
      entity.addAttribute(Constant.WEIGHT,getJsonString(measuresObject,"bodyWeight"));
      entity.addAttribute(Constant.WAIST,getJsonString(measuresObject,"waist"));*/
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private void addGoal(Entity entity,String json){
        try {
            JsonElement bodyMeasuresElement = jsonParser.parse(json);
            JsonElement mealPlanElement = getJsonElement(bodyMeasuresElement.getAsJsonObject(), "mealPlan");
            if (mealPlanElement != null) {
                JsonObject mealObject = mealPlanElement.getAsJsonObject();
                entity.addAttribute(Constant.GOAL_CALORIES, getJsonString(mealObject, "caloricGoal"));
                entity.addAttribute(Constant.GOAL_PROTEIN, getJsonString(mealObject, "proteinGrams"));
                entity.addAttribute(Constant.GOAL_CARBOHYDRATES, getJsonString(mealObject, "carbsGrams"));
                entity.addAttribute(Constant.GOAL_FAT, getJsonString(mealObject, "fatGrams"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private JsonElement getJsonElement(JsonObject jsonElement, String key) {
        try {
            return jsonElement.get(key);
        } catch (Exception e) {
            return null;
        }
    }

    private String getJsonString(JsonObject jsonElement, String key) {
        try {
            return jsonElement.get(key).getAsString();
        } catch (Exception e) {
            return null;
        }
    }

    private String makeApiReques(String url, String requestBody) {
        try {
            return Jsoup.connect(url)
                    .header("Authorization", "TRAUV1 1537998_JYAVFJCAMTEOIABHRLZH")
                    .header("Host", "api.trainerize.com")
                    .header("Accept", "application/json, text/javascript, */*; q=0.01")
                    .header("Host", "api.trainerize.com")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .header("TR-From", "")
                    .header("Content-Length", "43")
                    .ignoreContentType(true)
                    .userAgent(" Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                    .requestBody(requestBody)
                    //.referrer("https://summitperformance.trainerize.com/app/client/1586249/calendar")
                    .method(Connection.Method.POST)
                    .execute().body();
        } catch (IOException e) {
            // e.printStackTrace();
        }
        return requestBody;
    }

    private String usersListRequestBody() {
        return "{\"view\":\"activated\",\"sortBy\":\"name\",\"userID\":\"0\"}";
    }

    private String bodyStatusRequestBody(String userId, String date) {
        return "{\"userid\":" + Integer.parseInt(userId)+ ",\"date\":\"" + date + "\",\"unitWeight\":\"kg\",\"unitBodystats\":\"cm\"}";
    }

    private String mealPlanRequestBody(String userId) {
        return "{\"userID\":" + Integer.parseInt(userId) + "}";
    }

    private String dailyNutritionRequestBody(String userId, String date) {
        return "{\"userID\":" +Integer.parseInt(userId) + ",\"date\":\"" + date + "\"}";
    }
    private String workoutRequestBody (String userId,String date) {
        //  {"ids":[35416982],"userID":1611633,"unitWeight":"kg","unitDistance":"km"}
        // return "{\"userID\":" +Integer.parseInt(userId) + ",\"unitWeight\":\"kg\",\"unitDistance\":\"km\"}";
        return  "{\"userid\":"+Integer.parseInt(userId)+",\"startDate\":\""+date+"\",\"endDate\":\""+date+" \",\"unitDistance\":\"km\",\"unitWeight\":\"kg\"}";

    }
    // {"userid":1611633,"date":"2018-07-02","unitWeight":"kg","unitBodystats":"cm"}

 /* private String makeApiReques(String url) {
   String body = Jsoup.connect("https://api.trainerize.com/v03/User/getProfile")
           //.data("Authorization", "TRAUV1 1537998_JYAVFJCAMTEOIABHRLZH")
           .header("Authorization", "TRAUV1 1537998_JYAVFJCAMTEOIABHRLZH")
           .header("Host", "api.trainerize.com")

          .header("Host", "api.trainerize.com")
           .header("Accept-Language", "en-US,en;q=0.9")
           .header("Accept-Encoding", "gzip, deflate, br")
           .header("Accept-Encoding", "gzip, deflate, br")
           .header("Content-Type", "application/json; charset=UTF-8")
           .header("TR-From", "web")
           .header("Content-Length", "43")
           .ignoreContentType(true).ignoreHttpErrors(true)
           .userAgent(" Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
           .requestBody("{\"usersid\":[1586249],\"unitBodystats\":\"cm\"}\n")
  //.requestBody("{usersid:[1586249],unitBodystats:cm}")
           .referrer("https://summitperformance.trainerize.com/app/client/1586249/calendar")
           .method(Connection.Method.POST)
           .execute().body();
}  */


}
