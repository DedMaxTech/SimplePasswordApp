package com.maxgyver.passwordapp;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
public class PasswordAdapter extends CursorAdapter {

    String code;
    public PasswordAdapter(Context context, Cursor cursor, String code) {
        super(context, cursor, 0);
        this.code = code;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.password_list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView websiteTextView = view.findViewById(R.id.websiteTextView);
        TextView usernameTextView = view.findViewById(R.id.usernameTextView);
        TextView passwordTextView = view.findViewById(R.id.passwordTextView);
        Button deleteButton = view.findViewById(R.id.deleteButton);
        Button copyButton = view.findViewById(R.id.copyButton);

        final int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ID));
        final String website = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_WEBSITE));
        final String username = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_LOGIN));
        final String password = DBHelper.decrypt(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PASSWORD)), code);

        websiteTextView.setText(website);
        usernameTextView.setText(username);
        passwordTextView.setText(password);

        deleteButton.setOnClickListener(v -> {
            DBHelper dbHelper = new DBHelper(context);
            dbHelper.deletePassword(id);
            Toast.makeText(context, "Password deleted", Toast.LENGTH_SHORT).show();
            swapCursor(dbHelper.getPasswords());
        });
        copyButton.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Password", password);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Password copied to clipboard", Toast.LENGTH_SHORT).show();
        });
    }
}
