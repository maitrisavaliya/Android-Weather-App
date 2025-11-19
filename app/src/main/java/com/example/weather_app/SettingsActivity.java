package com.example.weather_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingsActivity extends AppCompatActivity {

    RadioGroup tempUnitGroup;
    RadioButton celsiusBtn, fahrenheitBtn;
    Switch darkModeSwitch, notificationsSwitch;
    Button saveBtn, backBtn;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        tempUnitGroup = findViewById(R.id.tempUnitGroup);
        celsiusBtn = findViewById(R.id.celsiusBtn);
        fahrenheitBtn = findViewById(R.id.fahrenheitBtn);
        darkModeSwitch = findViewById(R.id.darkModeSwitch);
        notificationsSwitch = findViewById(R.id.notificationsSwitch);
        saveBtn = findViewById(R.id.saveBtn);
        backBtn = findViewById(R.id.backBtn);

        sharedPreferences = getSharedPreferences("WeatherAppSettings", MODE_PRIVATE);

        // Load saved settings
        loadSettings();

        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
                Toast.makeText(SettingsActivity.this, "Settings saved!", Toast.LENGTH_SHORT).show();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadSettings() {
        String tempUnit = sharedPreferences.getString("temp_unit", "celsius");
        if (tempUnit.equals("celsius")) {
            celsiusBtn.setChecked(true);
        } else {
            fahrenheitBtn.setChecked(true);
        }

        boolean darkMode = sharedPreferences.getBoolean("dark_mode", false);
        darkModeSwitch.setChecked(darkMode);

        boolean notifications = sharedPreferences.getBoolean("notifications", true);
        notificationsSwitch.setChecked(notifications);
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String tempUnit = celsiusBtn.isChecked() ? "celsius" : "fahrenheit";
        editor.putString("temp_unit", tempUnit);

        editor.putBoolean("dark_mode", darkModeSwitch.isChecked());
        editor.putBoolean("notifications", notificationsSwitch.isChecked());

        editor.apply();
    }
}