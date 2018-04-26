package com.udacity.sandwichclub.utils;

import java.util.ArrayList;
import java.util.List;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.content.res.TypedArrayUtils;
import android.util.Log;


public class JsonUtils {

    private static final String TAG = "JsonUtils";
    public static Sandwich parseSandwichJson(String json) {
        JSONObject rootObject = null;
        Sandwich sandwich = null;
        try {
            rootObject = new JSONObject(json);
            sandwich = new Sandwich();
            //JSONObject name = jsonObject.getJSONObject("name");
            JSONObject sandwichJson = rootObject.getJSONObject("name");
            String mainName = sandwichJson.optString("mainName");
            JSONArray alsoKnownAsArray = sandwichJson.getJSONArray("alsoKnownAs");
            sandwich.setMainName(mainName);
            List<String> akaList = new ArrayList();
            for (int i=0; i< alsoKnownAsArray.length(); i++) {
                String akaStr = alsoKnownAsArray.getString(i);
                akaList.add(akaStr);
            }
            sandwich.setAlsoKnownAs(akaList);
            String placeOfOrigin = rootObject.optString("placeOfOrigin");
            sandwich.setPlaceOfOrigin(placeOfOrigin);
            String sandwichDescription = rootObject.optString("description");
            sandwich.setDescription(sandwichDescription);
            String sandwichImageUrl = rootObject.optString("image");
            sandwich.setImage(sandwichImageUrl);

            JSONArray ingredientsJson = rootObject.getJSONArray("ingredients");
            List<String> ingredients = new ArrayList<>();
            for (int i=0; i< ingredientsJson.length(); i++) {
                String ingredient = ingredientsJson.getString(i);
                ingredients.add(ingredient);
            }
            sandwich.setIngredients(ingredients);
            Log.d(TAG, sandwich.getMainName() + " " + sandwich.getPlaceOfOrigin()+ " " +sandwich.getImage() + " " + sandwich.getIngredients().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return sandwich;
    }
}
