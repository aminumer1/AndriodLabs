package com.example.andriodlabs;

import android.os.Bundle;

public class DadJoke extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_dad_joke);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Dad Joke");
        }
    }
}
