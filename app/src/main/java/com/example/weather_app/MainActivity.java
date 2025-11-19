package com.example.weather_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText cityInput;
    Button searchBtn, historyBtn, favoritesBtn, settingsBtn, aboutBtn;
    TextView temperatureText, cityNameText, descriptionText, humidityText, windText;
    ImageView weatherIcon;
    CardView weatherCard;
    String apiUrl;

    class GetWeather extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    // Get main weather data
                    JSONObject main = jsonObject.getJSONObject("main");
                    double temp = main.getDouble("temp") - 273.15; // Convert to Celsius
                    int humidity = main.getInt("humidity");

                    // Get weather description
                    JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
                    String description = weather.getString("description");

                    // Get wind data
                    JSONObject wind = jsonObject.getJSONObject("wind");
                    double windSpeed = wind.getDouble("speed");

                    // Get city name
                    String city = jsonObject.getString("name");

                    // Update UI
                    weatherCard.setVisibility(View.VISIBLE);
                    temperatureText.setText(String.format("%.1f°C", temp));
                    cityNameText.setText(city);
                    descriptionText.setText(description.toUpperCase());
                    humidityText.setText("Humidity: " + humidity + "%");
                    windText.setText("Wind: " + String.format("%.1f", windSpeed) + " m/s");

                    // Save to history
                    WeatherDatabase db = new WeatherDatabase(MainActivity.this);
                    db.addHistory(city, String.format("%.1f°C", temp), description);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error parsing weather data", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "City not found!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        cityInput = findViewById(R.id.cityInput);
        searchBtn = findViewById(R.id.searchBtn);
        historyBtn = findViewById(R.id.historyBtn);
        favoritesBtn = findViewById(R.id.favoritesBtn);
        settingsBtn = findViewById(R.id.settingsBtn);
        aboutBtn = findViewById(R.id.aboutBtn);

        temperatureText = findViewById(R.id.temperatureText);
        cityNameText = findViewById(R.id.cityNameText);
        descriptionText = findViewById(R.id.descriptionText);
        humidityText = findViewById(R.id.humidityText);
        windText = findViewById(R.id.windText);
        weatherIcon = findViewById(R.id.weatherIcon);
        weatherCard = findViewById(R.id.weatherCard);

        // Search button click
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = cityInput.getText().toString().trim();
                if (!city.isEmpty()) {
                    apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=0dae6c75ebe35dfcab4f66c8974ba061";
                    GetWeather task = new GetWeather();
                    task.execute(apiUrl);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a city name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // History button
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        // Favorites button
        favoritesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
                startActivity(intent);
            }
        });

        // Settings button
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        // About button
        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
    }
}