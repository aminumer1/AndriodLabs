package com.example.andriodlabs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.text.DateFormat;
import java.util.Date;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setupToolbarAndDrawer(getString(R.string.settings_title));

        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);

        TextView tv = findViewById(R.id.tvLastInfo);

        long lastRefresh = prefs.getLong("last_refresh_time", 0);
        String lastLink = prefs.getString("last_opened_link", "");

        String timeText = lastRefresh == 0
                ? getString(R.string.never)
                : DateFormat.getDateTimeInstance().format(new Date(lastRefresh));

        tv.setText(getString(R.string.settings_info, timeText, lastLink));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_help) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.help)
                    .setMessage(R.string.help_settings)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
