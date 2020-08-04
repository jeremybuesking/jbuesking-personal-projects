package edu.ranken.jbuesking.gettogether.data;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.ranken.jbuesking.gettogether.R;
import edu.ranken.jbuesking.gettogether.ui.activity.DashboardActivity;

public class GetTogetherApp extends Application {
    public static final String LOG_TAG = "edu.jbuesking LOG TAG::";
    public static final String EXTRA_USER_LOGIN = "login_user";
    public static final String EXTRA_CREATE_GROUP_USERNAME = "create_group_username";
    public static final String EXTRA_VIEW_GROUP_NAME = "view_group_name";
    public static final String EXTRA_VIEW_GROUP_USER = "view_group_user";
    public static final String EXTRA_VIEW_PROFILE_USERNAME = "view_profile_username";

    public static final String RSVP_KEY = "rsvp";
    public static final String AUTO_SAVE_KEY = "auto_save";

    private SharedPreferences mPreferences;

    private DatabaseReference mDatabaseReference;

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        FirebaseApp.initializeApp(this);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public DatabaseReference getDatabaseReference() {
        return mDatabaseReference;
    }

    public SharedPreferences getPreferences() {
        return mPreferences;
    }

    public boolean getAutoSave() {
        return mPreferences.getBoolean(AUTO_SAVE_KEY, true);
    }

    public void saveState(boolean isRsvp) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(RSVP_KEY, isRsvp);

        editor.apply();
    }
}
