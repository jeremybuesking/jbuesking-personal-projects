package edu.ranken.jbuesking.gettogether.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.ranken.jbuesking.gettogether.R;
import edu.ranken.jbuesking.gettogether.data.GetTogetherApp;


public class MainActivity extends AppCompatActivity implements TextView.OnEditorActionListener{
    private static final int REQUEST_CODE_NEW_USER = 4;
    private DatabaseReference mDatabaseReference;

    private EditText mUsername;
    private EditText mPassword;
    private DataSnapshot mUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mUsername = findViewById(R.id.text_login_username);
        mPassword = findViewById(R.id.text_login_password);

        mUsername.setOnEditorActionListener(this);
        mPassword.setOnEditorActionListener(this);

        mDatabaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers = dataSnapshot;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void onLogin(View view) {
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();

        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            displayToast("Fill all fields please!");
            return;
        }

        if (mUsers.child(username).exists() &&
            mUsers.child(username).child("password").getValue().equals(password)) {

            Intent intent = new Intent(this, DashboardActivity.class);
            intent.putExtra(GetTogetherApp.EXTRA_USER_LOGIN, username);
            startActivity(intent);
        }else {
            displayToast("Incorrect Username/Password!");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_NEW_USER && resultCode == RESULT_OK) {
            if(data != null) {
                mUsername.setText(data.getStringExtra(GetTogetherApp.EXTRA_USER_LOGIN));
            }
        }
    }

    public void startCreateUser(View view) {
        Intent intent = new Intent(this, CreateUserActivity.class);
        startActivityForResult(intent, REQUEST_CODE_NEW_USER);
    }

    @Override
    public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
            (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            onLogin(view);
            return true;
        }
        return false;
    }

    public void displayToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
