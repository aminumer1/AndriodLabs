package com.example.andriodlabs;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

    private static final String TAG = "MainActivity";

    private EditText editTodo;
    private Switch switchUrgent;
    private ListView listView;

    private final List<TodoItem> items = new ArrayList<>();
    private TodoAdapter adapter;

    private TodoDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTodo = findViewById(R.id.edit_todo);
        switchUrgent = findViewById(R.id.switch_urgent);
        listView = findViewById(R.id.list_todos);
        Button buttonAdd = findViewById(R.id.button_add);

        dbHelper = new TodoDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        loadTodosFromDatabase();

        adapter = new TodoAdapter();
        listView.setAdapter(adapter);

        buttonAdd.setOnClickListener(v -> {
            String text = editTodo.getText().toString().trim();
            if (text.isEmpty()) {
                return;
            }
            boolean urgent = switchUrgent.isChecked();

            ContentValues cv = new ContentValues();
            cv.put(TodoDatabaseHelper.COL_TEXT, text);
            cv.put(TodoDatabaseHelper.COL_URGENT, urgent ? 1 : 0);
            long newId = db.insert(TodoDatabaseHelper.TABLE_TODOS, null, cv);

            items.add(new TodoItem(newId, text, urgent));

            editTodo.setText("");
            switchUrgent.setChecked(false);
            adapter.notifyDataSetChanged();
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            TodoItem item = items.get(position);
            String msg = getString(R.string.dialog_message, position);

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(R.string.dialog_title)
                    .setMessage(msg)
                    .setPositiveButton(R.string.dialog_delete, (dialog, which) -> {
                        db.delete(TodoDatabaseHelper.TABLE_TODOS,
                                TodoDatabaseHelper.COL_ID + "=?",
                                new String[]{String.valueOf(item.getId())});

                        items.remove(position);
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton(R.string.dialog_cancel, null)
                    .show();

            return true;
        });
    }

    private void loadTodosFromDatabase() {
        items.clear();

        Cursor c = db.query(TodoDatabaseHelper.TABLE_TODOS,
                null, null, null, null, null, null);

        printCursor(c);

        c.moveToPosition(-1);

        while (c.moveToNext()) {
            long id = c.getLong(c.getColumnIndexOrThrow(TodoDatabaseHelper.COL_ID));
            String text = c.getString(c.getColumnIndexOrThrow(TodoDatabaseHelper.COL_TEXT));
            boolean urgent = c.getInt(c.getColumnIndexOrThrow(TodoDatabaseHelper.COL_URGENT)) == 1;
            items.add(new TodoItem(id, text, urgent));
        }
        c.close();
    }

    private void printCursor(Cursor c) {
        Log.d(TAG, "======= printCursor() =======");
        Log.d(TAG, "DB Version: " + db.getVersion());

        int columnCount = c.getColumnCount();
        Log.d(TAG, "Number of columns: " + columnCount);

        for (int i = 0; i < columnCount; i++) {
            Log.d(TAG, "Column " + i + ": " + c.getColumnName(i));
        }

        Log.d(TAG, "Number of rows: " + c.getCount());

        for (int row = 0; row < c.getCount(); row++) {
            c.moveToPosition(row);
            StringBuilder sb = new StringBuilder();
            for (int col = 0; col < columnCount; col++) {
                sb.append(c.getColumnName(col))
                        .append("=")
                        .append(c.getString(col))
                        .append("  ");
            }
            Log.d(TAG, "Row " + row + ": " + sb);
        }
        Log.d(TAG, "=============================");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    private class TodoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return items.get(position).getId();
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