package edu.ranken.jbuesking.gettogether.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.ranken.jbuesking.gettogether.R;
import edu.ranken.jbuesking.gettogether.data.GetTogetherApp;
import edu.ranken.jbuesking.gettogether.data.entity.Group;
import edu.ranken.jbuesking.gettogether.ui.adapter.SearchListAdapter;

public class SearchActivity extends AppCompatActivity {
    private GetTogetherApp mApp;
    private DatabaseReference mDatabaseReference;

    private RecyclerView mRecyclerView;
    private SearchListAdapter mAdapter;

    private EditText mSearchText;

    private String mUsername;
    private List<Group> mGroups = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mApp = (GetTogetherApp) getApplication();
        mDatabaseReference = mApp.getDatabaseReference();
        mUsername = getIntent().getStringExtra(GetTogetherApp.EXTRA_VIEW_GROUP_USER);

        mSearchText = findViewById(R.id.text_search);

        mRecyclerView = findViewById(R.id.recyclerViewSearch);
        mAdapter = new SearchListAdapter(this, mGroups, mUsername);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(mAdapter);
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

    public void onSearch(View view) {
        hideSoftKeyboard(view);
        final String searchQuery = mSearchText.getText().toString();
        mGroups.clear();

        if(TextUtils.isEmpty(searchQuery)){
            displayToast("Enter a search term!");
        }else{
            Query groups = mDatabaseReference.child("groups");
            groups.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try{
                        for(DataSnapshot group : dataSnapshot.getChildren()){
                            Group obj = group.getValue(Group.class);
                            String name = obj.getName();
                            List<String> tags = obj.getInterestTags();
                            if(name.toLowerCase().contains(searchQuery.toLowerCase())){
                                mGroups.add(obj);
                            }
                            for(String tag : tags){
                                if(tag.toLowerCase().contains(searchQuery.toLowerCase())){
                                    mGroups.add(obj);
                                }
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }catch(Exception ex) {
                        Log.e(GetTogetherApp.LOG_TAG, ex.toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
        }
    }

    private void hideSoftKeyboard(View view) {
        InputMethodManager ime = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        if (ime != null) {
            ime.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void displayToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
