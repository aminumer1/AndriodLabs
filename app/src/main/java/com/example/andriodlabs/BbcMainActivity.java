package com.example.andriodlabs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class BbcMainActivity extends BaseActivity {

    private ProgressBar progress;
    private ListView listView;
    private EditText searchBox;

    private ArrayAdapter<Article> adapter;
    private final List<Article> articles = new ArrayList<>();

    // PLAIN SharedPreferences (NO PreferenceManager)
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbc_main);

        setupToolbarAndDrawer(getString(R.string.bbc_title));

        prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);

        progress = findViewById(R.id.progress);
        listView = findViewById(R.id.listView);
        searchBox = findViewById(R.id.searchBox);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, articles);
        listView.setAdapter(adapter);

        findViewById(R.id.btnRefresh).setOnClickListener(v -> new FetchTask().execute());

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Article a = articles.get(position);

            prefs.edit()
                    .putString("last_opened_link", a.link)
                    .apply();

            Intent i = new Intent(this, ArticleDetailActivity.class);
            i.putExtra("title", a.title);
            i.putExtra("link", a.link);
            i.putExtra("desc", a.description);
            i.putExtra("date", a.pubDate);
            startActivity(i);
        });
    }

    private class FetchTask extends AsyncTask<Void, Void, List<Article>> {

        @Override
        protected void onPreExecute() {
            progress.setVisibility(View.VISIBLE);
            Snackbar.make(listView, R.string.snackbar_loading, Snackbar.LENGTH_SHORT).show();
        }

        @Override
        protected List<Article> doInBackground(Void... voids) {
            try {
                return new BbcRssFetcher().fetch();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Article> result) {
            progress.setVisibility(View.GONE);

            if (result == null) {
                new AlertDialog.Builder(BbcMainActivity.this)
                        .setTitle(R.string.error)
                        .setMessage(R.string.error_loading)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                return;
            }

            articles.clear();
            articles.addAll(result);
            adapter.notifyDataSetChanged();

            prefs.edit()
                    .putLong("last_refresh_time", System.currentTimeMillis())
                    .apply();

            Toast.makeText(
                    BbcMainActivity.this,
                    getString(R.string.loaded_n, articles.size()),
                    Toast.LENGTH_SHORT
            ).show();
        }
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
                    .setMessage(R.string.help_bbc_main)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
