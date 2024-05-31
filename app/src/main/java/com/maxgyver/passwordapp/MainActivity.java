package com.maxgyver.passwordapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.database.Cursor;

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private PasswordAdapter passwordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        String code = getIntent().getExtras().getString("code");
        dbHelper = new DBHelper(this);

        final ListView listView = findViewById(R.id.listView);
        final Button addButton = findViewById(R.id.addButton);

        addButton.setOnClickListener(v -> {
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            View dialogView = inflater.inflate(R.layout.password_diolog, null);

            final EditText editTextWebsite = dialogView.findViewById(R.id.editTextWebsite);
            final EditText editTextUsername = dialogView.findViewById(R.id.editTextUsername);
            final EditText editTextPassword = dialogView.findViewById(R.id.editTextPassword);

            new AlertDialog.Builder(MainActivity.this)
                .setView(dialogView)
                .setTitle("Add Password")
                .setPositiveButton("Add", (dialog, which) -> {
                    dbHelper.addPassword(new Password(
                        editTextWebsite.getText().toString().trim(),
                        editTextUsername.getText().toString().trim(),
                        DBHelper.encrypt(editTextPassword.getText().toString().trim(), code)));

                    updateListView(listView);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create().show();
        });

        passwordAdapter = new PasswordAdapter(this, null, code);
        listView.setAdapter(passwordAdapter);

        updateListView(listView);
    }

    private void updateListView(ListView listView) {
        Cursor cursor = dbHelper.getPasswords();
        passwordAdapter.changeCursor(cursor);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        else return super.onOptionsItemSelected(item);
    }
}