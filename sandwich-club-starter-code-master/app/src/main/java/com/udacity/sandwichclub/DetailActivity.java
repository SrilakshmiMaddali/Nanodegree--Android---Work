package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    protected static final String NA = "Not Available";
    ContentLoadingProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);
        mProgressBar = findViewById(R.id.loadingprogress);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv, new Callback() {
                    @Override
                    public void onSuccess() {
                        mProgressBar.hide();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(DetailActivity.this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
                    }
                });

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(@NonNull Sandwich sandwich) {
        mProgressBar.show();
        TextView placeOfOrigin = (TextView)findViewById(R.id.origin_tv);
        String place = sandwich.getPlaceOfOrigin();
        if (!place.isEmpty()) {
            placeOfOrigin.setText(place);
        } else {
            placeOfOrigin.setText(NA);
        }


        TextView aka = (TextView)findViewById(R.id.also_known_tv);
        if (sandwich.getAlsoKnownAs().size() > 0) {
            aka.setText(sandwich.getAlsoKnownAs().toString());
        } else {
            aka.setText(NA);
        }

        TextView ingredients = (TextView)findViewById(R.id.ingredients_tv);
        ingredients.setText(sandwich.getIngredients().toString());

        TextView description = (TextView)findViewById(R.id.description_tv);
        description.setText(sandwich.getDescription());
    }
}
