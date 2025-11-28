package com.example.andriodlabs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final int REQ_NAME = 100;
    public static final String EXTRA_NAME = "extra_name";

    private EditText editName;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        editName = findViewById(R.id.edit_name);
        Button next = findViewById(R.id.button_next);

        // Load saved name into EditText if it exists
        String savedName = prefs.getString("saved_name", "");
        if (savedName != null && !savedName.isEmpty()) {
            editName.setText(savedName);
        }

        // Navigate to second screen
        next.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            Intent intent = new Intent(MainActivity.this, NameActivity.class);
            intent.putExtra(EXTRA_NAME, name);
            startActivityForResult(intent, REQ_NAME);   // Lab requires startActivityForResult
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Save the current EditText value
        prefs.edit().putString("saved_name", editName.getText().toString()).apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_NAME) {
            if (resultCode == 0) {
                // User clicked “Don’t call me that”
                // Returning to this screen — do nothing
            } else if (resultCode == 1) {
                // User clicked “Thank You”
                // Close app
                finish();
            }
        }
    }
}