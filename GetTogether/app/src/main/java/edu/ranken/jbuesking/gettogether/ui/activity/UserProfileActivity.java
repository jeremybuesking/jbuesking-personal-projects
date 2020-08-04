package edu.ranken.jbuesking.gettogether.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.ranken.jbuesking.gettogether.R;
import edu.ranken.jbuesking.gettogether.data.GetTogetherApp;
import edu.ranken.jbuesking.gettogether.data.entity.User;

public class UserProfileActivity extends AppCompatActivity {
    private GetTogetherApp mApp;
    private DatabaseReference mDatabaseReference;

    private TextView mUsername;
    private TextView mLocation;
    private TextView mInterests;
    private TextView mGroups;

    private String mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mApp = (GetTogetherApp) getApplication();
        mDatabaseReference = mApp.getDatabaseReference();

        mCurrentUser = getIntent().getStringExtra(GetTogetherApp.EXTRA_VIEW_PROFILE_USERNAME);

        mUsername = findViewById(R.id.user_profile_name);
        mLocation = findViewById(R.id.user_profile_location);
        mInterests = findViewById(R.id.user_profile_interests);
        mGroups = findViewById(R.id.user_profile_groups);

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

    private void listenForUser() {
        mDatabaseReference.child("users").child(mCurrentUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if(user != null) {
                    StringBuilder interestSb = new StringBuilder();
                    StringBuilder groupSb = new StringBuilder();
                    List<String> interests = user.getInterests();
                    List<String> groups = user.getGroups();

                    for(int i=0; i < interests.size(); ++i) {
                        interestSb.append(interests.get(i));
                        interestSb.append(", ");
                    }
                    if(groups != null) {
                        for(int i=0; i < groups.size(); ++i) {
                            groupSb.append(groups.get(i));
                            groupSb.append(", ");
                        }
                    }

                    mUsername.setText(user.getUsername());
                    mLocation.setText(user.getLocation());
                    mInterests.setText(interestSb.toString());
                    mGroups.setText(groupSb.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
