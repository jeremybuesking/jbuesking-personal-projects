package edu.ranken.jbuesking.gettogether.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import edu.ranken.jbuesking.gettogether.R;
import edu.ranken.jbuesking.gettogether.data.GetTogetherApp;
import edu.ranken.jbuesking.gettogether.data.entity.User;

public class CreateUserActivity extends AppCompatActivity {
    private DatabaseReference mDatabaseReference;

    private List<String> mInterestList;

    private EditText mUsername;
    private EditText mPassword;
    private EditText mEmail;
    private EditText mLocation;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mContext = this;

        mInterestList = new ArrayList<>();

        mUsername = findViewById(R.id.text_username);
        mPassword = findViewById(R.id.text_password);
        mEmail = findViewById(R.id.text_email);
        mLocation = findViewById(R.id.text_location);
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
            mInterestList.add(interest);
        }else {
            mInterestList.remove(interest);
        }
    }

    public void onSaveUser(View view) {
        final String username = mUsername.getText().toString();
        final String password = mPassword.getText().toString();
        final String email = mEmail.getText().toString();
        final String location = mLocation.getText().toString();

        if(TextUtils.isEmpty(username) ||
            TextUtils.isEmpty(password) ||
            TextUtils.isEmpty(email) ||
            TextUtils.isEmpty(location) ||
            mInterestList.size() == 0) {
            displayToast("Please enter values for all fields");
        }else {
            try{
                mDatabaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(username).exists()){
                            displayToast("That Username is already in use!");
                        }else{
                            List<String> interests = mInterestList;
                            User newUser = new User(username, password, email, location, interests);
                            mDatabaseReference.child("users").child(username).setValue(newUser);

                            Intent replyIntent = new Intent(mContext, MainActivity.class);
                            replyIntent.putExtra(GetTogetherApp.EXTRA_USER_LOGIN, username);
                            setResult(RESULT_OK, replyIntent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }catch(Exception ex){
                Log.e(GetTogetherApp.LOG_TAG, ex.toString());
            }
        }
    }

    public void displayToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
