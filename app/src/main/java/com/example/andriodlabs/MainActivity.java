package com.example.andriodlabs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView catImageView;
    private ProgressBar progressBar;

    // Local images array
    private int[] cats = {
            R.drawable.cat1,
            R.drawable.cat2,
            R.drawable.cat3
    };

    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        catImageView = findViewById(R.id.catImageView);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);

        // Start slideshow
        new CatSlideShowTask().execute();
    }

    private class CatSlideShowTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            while (true) {
                try {
                    publishProgress(0);

                    for (int i = 0; i < 100; i++) {
                        Thread.sleep(30);
                        publishProgress(i);
                    }

                    // Next image
                    currentIndex = (currentIndex + 1) % cats.length;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress = values[0];
            progressBar.setProgress(progress);

            // Every time progress resets to 0 → new image
            if (progress == 0) {
                catImageView.setImageResource(cats[currentIndex]);
            }
        }
    }
}
