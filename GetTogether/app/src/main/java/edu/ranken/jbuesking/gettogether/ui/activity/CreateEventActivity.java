package edu.ranken.jbuesking.gettogether.ui.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import edu.ranken.jbuesking.gettogether.R;
import edu.ranken.jbuesking.gettogether.data.GetTogetherApp;
import edu.ranken.jbuesking.gettogether.data.entity.Event;

public class CreateEventActivity extends AppCompatActivity implements TextView.OnEditorActionListener {
    private GetTogetherApp mApp;
    private DatabaseReference mDatabaseReference;

    private TextView mCurrentGroupName;
    private TextView mCurrentOrganizer;
    private EditText mTitle;
    private EditText mLocation;
    private EditText mDate;
    private EditText mTime;
    private EditText mDescription;

    String mOrganizer;
    String mGroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event_scrollview);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        mApp = (GetTogetherApp) getApplication();
        mDatabaseReference = mApp.getDatabaseReference();

        mCurrentGroupName = findViewById(R.id.create_event_group_name);
        mCurrentOrganizer = findViewById(R.id.create_event_organizer);
        mTitle = findViewById(R.id.create_event_title);
        mLocation = findViewById(R.id.create_event_location);
        mDate = findViewById(R.id.create_event_datetime);
        mTime = findViewById(R.id.create_event_time);
        mDescription = findViewById(R.id.create_event_description);

        mGroupName = getIntent().getStringExtra(GetTogetherApp.EXTRA_VIEW_GROUP_NAME);
        mOrganizer = getIntent().getStringExtra(GetTogetherApp.EXTRA_VIEW_GROUP_USER);

        mDescription.setOnEditorActionListener(this);
        mCurrentGroupName.setText(mGroupName);
        mCurrentOrganizer.setText(mOrganizer);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    public void onSaveEvent(View view) {
        String title = mTitle.getText().toString();
        String location = mLocation.getText().toString();
        String description = mDescription.getText().toString();
        String date = mDate.getText().toString();
        String time = mTime.getText().toString();

        if(TextUtils.isEmpty(title) ||
            TextUtils.isEmpty(location) ||
            TextUtils.isEmpty(description) ||
            TextUtils.isEmpty(date) ||
            TextUtils.isEmpty(time)) {
            displayToast(getString(R.string.toast_enter_all_fields));
        }else{
            try{
                String dateTime = date + " " + time;
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US);
                format.parse(dateTime);
                List<String> attending = new ArrayList<>();
                attending.add(mOrganizer);

                Event event = new Event(title, description, location, dateTime, attending, mGroupName);
                mDatabaseReference.child("events").child(mGroupName).child(title).setValue(event);
                displayToast("Event Has Been Created!");
            }catch(Exception ex) {
                Log.e(GetTogetherApp.LOG_TAG, ex.toString());
                displayToast("There was an issue when trying to create the event.");
            }

            Intent replyIntent = new Intent();
            setResult(RESULT_OK, replyIntent);
            finish();
        }
    }

    public Calendar getDate() {
        try {
            String eventDate = mDate.getText().toString();
            return Event.parseDate(eventDate);
        } catch (Exception ex) {
            return null;
        }
    }

    public void onPickDate(@NonNull View view) {
        Calendar cal = getDate();
        if (cal == null) { cal = Calendar.getInstance(); }

        DatePickerDialog dlg = new DatePickerDialog(this,
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar newDate = new GregorianCalendar(year, month, dayOfMonth);
                    mDate.setText(Event.formatDate(newDate));
                    mDate.requestFocus();
                    mDate.setSelection(0, mDate.getText().length());
                    //hideSoftKeyboard(mDateTime);
                }
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        );
        dlg.show();
    }

    public Calendar getTime() {
        try {
            String eventTime = mTime.getText().toString();
            return Event.parseTime(eventTime);
        } catch (Exception ex) {
            return null;
        }
    }

    public void onPickTime(View view) {
        LocalDateTime now = LocalDateTime.now();
        Calendar cal = getTime();
        if (cal == null) { cal = Calendar.getInstance(); }

        TimePickerDialog tpd = new TimePickerDialog(this,
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    Calendar newTime = Calendar.getInstance();
                    newTime.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, hourOfDay, minute);
                    mTime.setText(Event.formatTime(newTime));
                }
            },
            now.getHour(),
            now.getMinute(),
            false
        );
        tpd.show();
    }

    @Override
    public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
            (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            onSaveEvent(view);
            return true;
        }
        return false;
    }

    public void displayToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
