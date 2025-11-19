package com.example.weather_app;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class FavoritesActivity extends AppCompatActivity {

    EditText cityInput;
    Button addBtn, backBtn;
    ListView favoritesList;
    WeatherDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        cityInput = findViewById(R.id.cityInput);
        addBtn = findViewById(R.id.addBtn);
        backBtn = findViewById(R.id.backBtn);
        favoritesList = findViewById(R.id.favoritesList);

        database = new WeatherDatabase(this);

        loadFavorites();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = cityInput.getText().toString().trim();
                if (!city.isEmpty()) {
                    database.addFavorite(city);
                    cityInput.setText("");
                    loadFavorites();
                    Toast.makeText(FavoritesActivity.this, "Added to favorites!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FavoritesActivity.this, "Enter city name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        favoritesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                database.removeFavorite(id);
                loadFavorites();
                Toast.makeText(FavoritesActivity.this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadFavorites() {
        Cursor cursor = database.getFavorites();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No favorites added yet", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] from = {"city"};
        int[] to = {R.id.favCityText};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.favorite_item,
                cursor,
                from,
                to,
                0
        );

        favoritesList.setAdapter(adapter);
    }
}