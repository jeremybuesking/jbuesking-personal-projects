package edu.ranken.jbuesking.gettogether.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import edu.ranken.jbuesking.gettogether.R;
import edu.ranken.jbuesking.gettogether.data.GetTogetherApp;
import edu.ranken.jbuesking.gettogether.data.entity.Event;
import edu.ranken.jbuesking.gettogether.data.entity.Group;
import edu.ranken.jbuesking.gettogether.data.entity.User;
import edu.ranken.jbuesking.gettogether.ui.adapter.EventListAdapter;
import edu.ranken.jbuesking.gettogether.ui.adapter.GroupListAdapter;

public class DashboardActivity extends AppCompatActivity {
    private static final String SAVED_USERNAME = "saved_username";
    private static final String SAVED_GROUP_LIST = "saved_group_list";
    private static final int REQUEST_CODE_GROUP_CREATE = 1;
    private GetTogetherApp mApp;
    private DatabaseReference mDatabaseReference;
    private DataSnapshot mGroupSnapshot;
    private LinearLayout mLinearLayout;

    // Group Recycler
    private RecyclerView mGroupRecyclerView;
    private GroupListAdapter mGroupAdapter;
    // Event Recycler
    private RecyclerView mEventRecyclerView;
    private EventListAdapter mEventAdapter;

    private String mUsername;
    private User mUser;
    private List<Group> mGroupList = new ArrayList<>();
    private List<Event> mEventList = new ArrayList<>();
    private List<String> mUserGroups = new ArrayList<>();

    private ImageView mRsvpButton;

    private Query mEventsQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mLinearLayout = findViewById(R.id.groupsEmptyLayout);
        mApp = (GetTogetherApp) getApplication();
        mDatabaseReference = mApp.getDatabaseReference();

        mRsvpButton = findViewById(R.id.rsvp_image_button);
        mUsername = getIntent().getStringExtra(GetTogetherApp.EXTRA_USER_LOGIN);

        mGroupRecyclerView = findViewById(R.id.recyclerViewGroups);
        mGroupAdapter = new GroupListAdapter(this, mGroupList, mUsername);
        mGroupRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mGroupRecyclerView.setAdapter(mGroupAdapter);

        mEventRecyclerView = findViewById(R.id.recyclerViewEvents);
        mEventAdapter = new EventListAdapter(this, mEventList, mUsername);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        mEventRecyclerView.setLayoutManager(layoutManager);
        mEventRecyclerView.setAdapter(mEventAdapter);

        listenForUser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_view_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_profile:
                Intent intent = new Intent(this, UserProfileActivity.class);
                intent.putExtra(GetTogetherApp.EXTRA_VIEW_PROFILE_USERNAME, mUsername);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openSearchActivity(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(GetTogetherApp.EXTRA_VIEW_GROUP_USER, mUsername);
        startActivity(intent);
    }

    public void onCreateGroup(View view) {
        Intent intent = new Intent(this, CreateGroupActivity.class);
        intent.putExtra(GetTogetherApp.EXTRA_CREATE_GROUP_USERNAME, mUsername);
        startActivityForResult(intent, REQUEST_CODE_GROUP_CREATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void listenForUser() {
        mUserGroups.clear();
        mDatabaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.child(mUsername).getValue(User.class);
                if(mUser != null) {
                    mUserGroups = mUser.getGroups();
                }

                listenForGroups();
                listenForEvents();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void listenForGroups() {
        mGroupList.clear();

        mDatabaseReference.child("groups").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mGroupList.clear();
                mGroupSnapshot = dataSnapshot;

                if(mUserGroups != null) {
                    for(int i = 0; i < mUserGroups.size(); ++i) {
                        Group group = mGroupSnapshot.child(mUserGroups.get(i)).getValue(Group.class);
                        mGroupList.add(group);
                    }
                }
                mGroupAdapter.notifyDataSetChanged();

                if(!mGroupList.isEmpty()) {
                    mLinearLayout.setVisibility(LinearLayout.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void listenForEvents() {
        if(mUserGroups != null) {
            mEventList.clear();

            for(int i = 0; i < mUserGroups.size(); ++i) {
                mEventsQuery = mDatabaseReference.child("events").child(mUserGroups.get(i)).orderByChild("dateTime");

                mEventsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot event : dataSnapshot.getChildren()) {
                            mEventList.add(event.getValue(Event.class));
                        }
                        mEventAdapter.notifyDataSetChanged();
                        mEventsQuery.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
            }
        }
    }

    public void displayToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
