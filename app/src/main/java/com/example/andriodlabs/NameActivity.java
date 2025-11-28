package com.example.andriodlabs;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        TextView welcome = findViewById(R.id.text_welcome);
        Button thanks = findViewById(R.id.button_thanks);
        Button dontCall = findViewById(R.id.button_dontcall);

        // Get name from intent
        String name = getIntent().getStringExtra(MainActivity.EXTRA_NAME);

        // If user entered a name, show "Welcome <name>"
        if (name != null && !name.isEmpty()) {
            welcome.setText(getString(R.string.welcome_name, name));
        } else {
            welcome.setText(getString(R.string.welcome_plain));
        }

        // “Don’t call me that” → result 0
        dontCall.setOnClickListener(v -> {
            setResult(0);
            finish();
        });

        // “Thank You” → result 1
        thanks.setOnClickListener(v -> {
            setResult(1);
            finish();
        });
    }
}