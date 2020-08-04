package edu.ranken.jbuesking.gettogether.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.ranken.jbuesking.gettogether.R;
import edu.ranken.jbuesking.gettogether.data.GetTogetherApp;
import edu.ranken.jbuesking.gettogether.data.entity.Group;
import edu.ranken.jbuesking.gettogether.data.entity.User;

public class CreateGroupActivity extends AppCompatActivity implements TextView.OnEditorActionListener {
    private GetTogetherApp mApp;
    private DatabaseReference mDatabaseReference;

    private List<String> mGroupInterests;
    private List<String> mGroupMembers;
    private TextView mGroupOrganizer;
    private EditText mGroupName;
    private EditText mGroupDescription;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_scrollview);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        mApp = (GetTogetherApp) getApplication();
        mDatabaseReference = mApp.getDatabaseReference();

        mGroupInterests = new ArrayList<>();
        mGroupMembers = new ArrayList<>();
        mGroupOrganizer = findViewById(R.id.organizer_name);
        mGroupName = findViewById(R.id.text_group_create_name);
        mGroupDescription = findViewById(R.id.text_group_create_description);

        mGroupOrganizer.setText(getIntent().getStringExtra(GetTogetherApp.EXTRA_CREATE_GROUP_USERNAME));
        mGroupMembers.add(mGroupOrganizer.getText().toString());

        mGroupDescription.setOnEditorActionListener(this);

        listenForUser();
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

    public void onCheckboxClicked(View view) {
        CheckBox checkbox = (CheckBox) view;
        boolean isChecked = checkbox.isChecked();
        String interest = checkbox.getText().toString();

        if(isChecked) {
            mGroupInterests.add(interest);
        }else {
            mGroupInterests.remove(interest);
        }
    }

    public void onSaveGroup(View view) {
        final String name = mGroupName.getText().toString().trim();
        final String description = mGroupDescription.getText().toString().trim();
        final String organizer = mGroupOrganizer.getText().toString();

        if(TextUtils.isEmpty(name) ||
           TextUtils.isEmpty(description) ||
           mGroupInterests.isEmpty()) {
            displayToast(getString(R.string.toast_enter_all_fields));
        }else {
            try{
                mDatabaseReference.child("groups").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(name).exists()){
                            displayToast(getString(R.string.toast_group_name_in_use));
                        }else{
                            Group group = new Group(organizer, name, description, mGroupInterests, mGroupMembers);
                            String username = mGroupOrganizer.getText().toString();
                            String groupIndex = String.valueOf(mUser.getGroupSize());
                            String groupName = group.getName();

                            mDatabaseReference.child("groups").child(name).setValue(group);
                            mDatabaseReference.child("users").child(username).child("groups").child(groupIndex).setValue(groupName);
                            displayToast("Your group has been created!");

                            Intent replyIntent = new Intent();

                            setResult(RESULT_OK, replyIntent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(GetTogetherApp.LOG_TAG, databaseError.getMessage());
                    }
                });
            }catch(Exception ex){
                Log.e(GetTogetherApp.LOG_TAG, "Inserting Group Failed..");
            }
        }
    }

    private void listenForUser() {
        mDatabaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.child(mGroupOrganizer.getText().toString()).getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    @Override
    public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
            (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            onSaveGroup(view);
            return true;
        }
        return false;
    }

    public void displayToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
