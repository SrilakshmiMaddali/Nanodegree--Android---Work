package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    protected static final String NA = "Not Available";
    @BindView(R.id.content_description)
    TextView mContentDescription;
    @BindView(R.id.loadingprogress)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        final ImageView ingredientsIv = findViewById(R.id.image_iv);
        mContentDescription = (TextView)findViewById(R.id.content_description);

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
                        // image loading finished, so remove progressbar.
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        // On error , while loading sandwich image, display error message.
                        ingredientsIv.setVisibility(View.INVISIBLE);
                        mProgressBar.setVisibility(View.GONE);
                        mContentDescription.setText(getString(R.string.detail_image_error_message));
                        mContentDescription.setVisibility(View.VISIBLE);
                    }
                });

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Helper method to trim first and last characters from input string.
     * @param input
     * @return
     */
    private String trimStartAndEndChars(@NonNull String input) {
        char trimmed[] = new char[input.length()-2];
        input.getChars(1,input.length()-1,trimmed,0);
        return String.valueOf(trimmed);
    }

    private void populateUI(@NonNull Sandwich sandwich) {
        // Load views, from model object
        TextView placeOfOrigin = (TextView)findViewById(R.id.origin_tv);
        mProgressBar.setVisibility(View.VISIBLE);
        String place = sandwich.getPlaceOfOrigin();
        if (!place.isEmpty()) {
            placeOfOrigin.setText(place);
        } else {
            placeOfOrigin.setText(NA);
        }

        TextView akaTextView = (TextView)findViewById(R.id.also_known_tv);
        if (sandwich.getAlsoKnownAs().size() > 0) {
            String akaString = sandwich.getAlsoKnownAs().toString();
            akaTextView.setText(trimStartAndEndChars(akaString));
        } else {
            akaTextView.setText(NA);
        }

        TextView ingredients = (TextView)findViewById(R.id.ingredients_tv);
        if (sandwich.getIngredients().size() > 0) {
            String ingredientsStr = sandwich.getIngredients().toString();
            ingredients.setText(trimStartAndEndChars(ingredientsStr));
        } else {
            ingredients.setText(NA);
        }

        TextView description = (TextView)findViewById(R.id.description_tv);
        description.setText(sandwich.getDescription());
    }
}
