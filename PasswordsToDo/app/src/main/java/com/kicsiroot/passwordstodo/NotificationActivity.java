package com.kicsiroot.passwordstodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class NotificationActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    AlarmReminderDbHelper alarmReminderDbHelper = new AlarmReminderDbHelper(this);
    ListView reminderList;
    AlarmCursorAdapter mCursorAdapter;

    private static final int VEHICLE_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Toolbar mToolbar;
        FloatingActionButton addReminder;
        mToolbar = findViewById(R.id.addReminderToolbar);
        setSupportActionBar(mToolbar);

        reminderList = findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        reminderList.setEmptyView(emptyView);

        mCursorAdapter = new AlarmCursorAdapter(this, null);
        reminderList.setAdapter(mCursorAdapter);

        reminderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(NotificationActivity.this, AddReminderActivity.class);
                Uri currentVUri = ContentUris.withAppendedId(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, l);
                intent.setData(currentVUri);
                startActivity(intent);
            }
        });

        addReminder = (FloatingActionButton) findViewById(R.id.fab);
        addReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationActivity.this, AddReminderActivity.class);
                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });

        getLoaderManager().initLoader(VEHICLE_LOADER, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                AlarmReminderContract.AlarmReminderEntry._ID,
                AlarmReminderContract.AlarmReminderEntry.KEY_TITLE,
                AlarmReminderContract.AlarmReminderEntry.KEY_DATE,
                AlarmReminderContract.AlarmReminderEntry.KEY_TIME,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE,
                AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE

        };

        return new CursorLoader(this,   // Parent activity context
                AlarmReminderContract.AlarmReminderEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}