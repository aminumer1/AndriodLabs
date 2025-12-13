package com.example.andriodlabs;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<JSONObject> characters = new ArrayList<>();
    CharacterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listView);
        adapter = new CharacterAdapter();
        listView.setAdapter(adapter);

        new StarWarsTask().execute();

        listView.setOnItemClickListener((parent, view, position, id) -> {

            JSONObject obj = characters.get(position);

            Bundle bundle = new Bundle();
            bundle.putString("name", obj.optString("name"));
            bundle.putString("height", obj.optString("height"));
            bundle.putString("mass", obj.optString("mass"));

            View fragmentContainer = findViewById(R.id.fragmentContainer);

            if (fragmentContainer == null) {
                Intent intent = new Intent(this, EmptyActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                DetailsFragment fragment = new DetailsFragment();
                fragment.setArguments(bundle);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentContainer, fragment);
                ft.commit();
            }
        });
    }

    class StarWarsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("https://swapi.dev/api/people/?format=json");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                );

                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                JSONArray array = new JSONObject(result.toString())
                        .getJSONArray("results");

                for (int i = 0; i < array.length(); i++) {
                    characters.add(array.getJSONObject(i));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            adapter.notifyDataSetChanged();
        }
    }

    class CharacterAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return characters.size();
        }

        @Override
        public Object getItem(int position) {
            return characters.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, android.view.ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater()
                        .inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            ((TextView) convertView).setText(
                    characters.get(position).optString("name")
            );
            return convertView;
        }
    }
}
