package com.example.andriodlabs;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private TextView titleText;
    private EditText editInput;
    private Button pressMe;
    private CheckBox checkBox;
    private Switch theSwitch;
    private ImageButton flagButton; // not required to click, but present per layout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Pick one layout to preview at a time; the lab requires all three files exist:
        // setContentView(R.layout.activity_main_linear);
        // setContentView(R.layout.activity_main_grid);
        setContentView(R.layout.activity_main_constraint); // spelled as in the lab brief

        titleText = findViewById(R.id.text_title);
        editInput = findViewById(R.id.edit_input);
        pressMe   = findViewById(R.id.button_press);
        checkBox  = findViewById(R.id.checkbox1);
        theSwitch = findViewById(R.id.switch1);
        flagButton= findViewById(R.id.image_flag);

        // Button: copy EditText to TextView + localized Toast
        pressMe.setOnClickListener(v -> {
            String text = editInput.getText() != null ? editInput.getText().toString().trim() : "";
            if (TextUtils.isEmpty(text)) {
                editInput.setError(getString(R.string.hint_love_android));
                return;
            }
            titleText.setText(text);
            Toast.makeText(this, getString(R.string.toast_message), Toast.LENGTH_SHORT).show();
        });

        // Checkbox: Snackbar shows ON/OFF and Undo flips back
        checkBox.setOnCheckedChangeListener((CompoundButton cb, boolean isChecked) -> {
            String state = getString(isChecked ? R.string.state_on : R.string.state_off);
            String msg = getString(R.string.checkbox_now, state);
            Snackbar.make(cb, msg, Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.undo), click -> cb.setChecked(!isChecked))
                    .show();
        });

        // (Optional) Switch listener if you want to show it’s interactive
        theSwitch.setOnCheckedChangeListener((sw, on) -> {
            // no rubric requirement; leave empty or log if desired
        });
    }
}
