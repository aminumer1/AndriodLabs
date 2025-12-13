package com.example.andriodlabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class FavouritesActivity extends BaseActivity {

    private ArrayAdapter<Article> adapter;
    private List<Article> favs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        setupToolbarAndDrawer(getString(R.string.favourites_title));

        ListView list = findViewById(R.id.favListView);

        FavouritesRepository repo = new FavouritesRepository(this);
        favs = repo.getAll();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, favs);
        list.setAdapter(adapter);

        list.setOnItemClickListener((p, v, pos, id) -> {
            Article a = favs.get(pos);
            Intent i = new Intent(this, ArticleDetailActivity.class);
            i.putExtra("title", a.title);
            i.putExtra("link", a.link);
            i.putExtra("desc", a.description);
            i.putExtra("date", a.pubDate);
            startActivity(i);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        FavouritesRepository repo = new FavouritesRepository(this);
        favs.clear();
        favs.addAll(repo.getAll());
        adapter.notifyDataSetChanged();
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
                    .setMessage(R.string.help_favourites)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
