package edu.ranken.jbuesking.gettogether.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.ranken.jbuesking.gettogether.R;
import edu.ranken.jbuesking.gettogether.data.GetTogetherApp;
import edu.ranken.jbuesking.gettogether.data.entity.Group;
import edu.ranken.jbuesking.gettogether.data.entity.User;
import edu.ranken.jbuesking.gettogether.ui.adapter.MemberListAdapter;

public class ViewGroupActivity extends AppCompatActivity {
    public static final int EVENT_INTENT_REQUEST_CODE = 2;

    private GetTogetherApp mApp;
    private DatabaseReference mDatabaseReference;

    private RecyclerView mMemberRecyclerView;
    private MemberListAdapter mAdapter;

    private DataSnapshot mUserSnapshot;
    private Group mGroup;
    private User mUser;
    private String mCurrentUsername;
    private String mGroupName;
    private String mOrganizer;
    private List<User> mUserList = new ArrayList<>();
    private List<String> mMembers = new ArrayList<>();

    private TextView mName;
    private TextView mDescription;
    private TextView mInterests;
    private Button mJoinGroupButton;
    private Button mAddEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        mApp = (GetTogetherApp) getApplication();
        mDatabaseReference = mApp.getDatabaseReference();

        mGroupName = getIntent().getStringExtra(GetTogetherApp.EXTRA_VIEW_GROUP_NAME);
        mCurrentUsername = getIntent().getStringExtra(GetTogetherApp.EXTRA_VIEW_GROUP_USER);

        mMemberRecyclerView = findViewById(R.id.recyclerViewMembers);
        mAdapter = new MemberListAdapter(this, mUserList);
        mMemberRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMemberRecyclerView.setAdapter(mAdapter);

        mName = findViewById(R.id.group_view_name);
        mDescription = findViewById(R.id.group_view_description);
        mInterests = findViewById(R.id.group_view_interests);
        mJoinGroupButton = findViewById(R.id.button_join_group);
        mAddEventButton = findViewById(R.id.button_create_event);

        listenForGroup();
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

    public void onJoinGroup(View view) {
        mDatabaseReference.child("groups").child(mGroupName).child("members").child(String.valueOf(mMembers.size())).setValue(mCurrentUsername);
        mDatabaseReference.child("users").child(mCurrentUsername).child("groups").child(String.valueOf(mUser.getGroupSize())).setValue(mGroup.getName());
    }

    public void onAddEvent(View view) {
        Intent intent = new Intent(this, CreateEventActivity.class);
        intent.putExtra(GetTogetherApp.EXTRA_VIEW_GROUP_NAME, mGroupName);
        intent.putExtra(GetTogetherApp.EXTRA_VIEW_GROUP_USER, mCurrentUsername);

        startActivityForResult(intent, EVENT_INTENT_REQUEST_CODE);
    }

    private void listenForGroup(){
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mGroup = dataSnapshot.child("groups").child(mGroupName).getValue(Group.class);

                if(mGroup != null) {
                    mOrganizer = mGroup.getOrganizer();

                    List<String> interests = mGroup.getInterestTags();
                    StringBuilder sb = new StringBuilder();
                    for(int i=0; i < interests.size(); ++i) {
                        sb.append(interests.get(i));
                        sb.append(", ");
                    }
                    mInterests.setText(sb.toString());
                    mName.setText(mGroup.getName());
                    mDescription.setText(mGroup.getDescription());

                    mUserList.clear();
                    mMembers = mGroup.getMembers();
                    if(mMembers != null) {
                        for(int i=0; i < mMembers.size(); ++i) {
                            mUserList.add(dataSnapshot.child("users").child(mMembers.get(i)).getValue(User.class));
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }
                checkIfInGroup();
                checkIfOrganizer();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void listenForUser() {
        mDatabaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.child(mCurrentUsername).getValue(User.class);
                mUserSnapshot = dataSnapshot.child(mCurrentUsername);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkIfInGroup() {
        for(int i=0; i < mMembers.size(); ++i) {
            if(mMembers.get(i).equals(mCurrentUsername)) {
                mJoinGroupButton.setVisibility(View.INVISIBLE);
                break;
            }
        }
    }

    private void checkIfOrganizer(){
        if(mCurrentUsername.equals(mOrganizer)){
            mAddEventButton.setVisibility(View.VISIBLE);
        }
    }
}
