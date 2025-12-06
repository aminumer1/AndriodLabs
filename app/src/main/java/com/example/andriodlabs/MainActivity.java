package com.example.andriodlabs;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText editTodo;
    private Switch switchUrgent;
    private ListView listView;

    private final List<TodoItem> items = new ArrayList<>();
    private TodoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTodo = findViewById(R.id.edit_todo);
        switchUrgent = findViewById(R.id.switch_urgent);
        listView = findViewById(R.id.list_todos);
        Button buttonAdd = findViewById(R.id.button_add);

        adapter = new TodoAdapter();
        listView.setAdapter(adapter);

        // Add button: add item, clear EditText, refresh list
        buttonAdd.setOnClickListener(v -> {
            String text = editTodo.getText().toString().trim();
            if (text.isEmpty()) {
                return;
            }
            boolean urgent = switchUrgent.isChecked();
            items.add(new TodoItem(text, urgent));

            editTodo.setText("");            // clear after adding
            switchUrgent.setChecked(false);  // reset switch
            adapter.notifyDataSetChanged();  // refresh list
        });

        // Long-press row: show AlertDialog with index and delete option
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            String msg = getString(R.string.dialog_message, position);

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(R.string.dialog_title)
                    .setMessage(msg)
                    .setPositiveButton(R.string.dialog_delete, (dialog, which) -> {
                        items.remove(position);
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton(R.string.dialog_cancel, null)
                    .show();

            return true; // consume long-click
        });
    }

    // ===== Custom adapter for the ListView =====
    private class TodoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.size(); // number of rows
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position; // no database yet
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                row = LayoutInflater.from(MainActivity.this)
                        .inflate(R.layout.row_todo, parent, false);
            }

            TextView textView = row.findViewById(R.id.text_todo);
            TodoItem item = items.get(position);
            textView.setText(item.getText());

            if (item.isUrgent()) {
                row.setBackgroundColor(Color.RED);
                textView.setTextColor(Color.WHITE);
            } else {
                row.setBackgroundColor(Color.TRANSPARENT);
                textView.setTextColor(Color.BLACK);
            }

            return row;
        }
    }
}
