package com.example.andriodlabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class DetailsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details, container, false);

        Bundle data = getArguments();

        if (data != null) {
            ((TextView) view.findViewById(R.id.name)).setText(data.getString("name"));
            ((TextView) view.findViewById(R.id.height)).setText(data.getString("height"));
            ((TextView) view.findViewById(R.id.mass)).setText(data.getString("mass"));
        }

        return view;
    }
}
