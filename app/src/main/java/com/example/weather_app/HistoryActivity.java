package com.example.weather_app;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {

    ListView historyList;
    Button clearHistoryBtn, backBtn;
    WeatherDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyList = findViewById(R.id.historyList);
        clearHistoryBtn = findViewById(R.id.clearHistoryBtn);
        backBtn = findViewById(R.id.backBtn);

        database = new WeatherDatabase(this);

        loadHistory();

        clearHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.clearHistory();
                loadHistory();
                Toast.makeText(HistoryActivity.this, "History cleared!", Toast.LENGTH_SHORT).show();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadHistory() {
        Cursor cursor = database.getHistory();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No search history", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] from = {"city", "temperature", "description", "date"};
        int[] to = {R.id.cityText, R.id.tempText, R.id.descText, R.id.dateText};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.history_item,
                cursor,
                from,
                to,
                0
        );

        historyList.setAdapter(adapter);
    }
}