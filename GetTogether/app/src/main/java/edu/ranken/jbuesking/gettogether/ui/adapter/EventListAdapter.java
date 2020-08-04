package edu.ranken.jbuesking.gettogether.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import edu.ranken.jbuesking.gettogether.R;
import edu.ranken.jbuesking.gettogether.data.GetTogetherApp;
import edu.ranken.jbuesking.gettogether.data.entity.Event;
import edu.ranken.jbuesking.gettogether.ui.activity.DashboardActivity;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder> {
    private long mRsvpCount;
    private String mUser;
    private List<Event> mItems;
    private final LayoutInflater mInflater;
    private final Activity mContext;

    private GetTogetherApp mApp;
    private DatabaseReference mDatabaseReference;

    public EventListAdapter(Activity context, List<Event> items, String user) {
        mUser = user;
        mItems = items;
        mInflater = LayoutInflater.from(context);
        mContext = context;

        mApp = (GetTogetherApp) context.getApplication();
        mDatabaseReference = mApp.getDatabaseReference();

        //mApp.getPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public int getItemCount() { return mItems != null ? mItems.size() : 0; }

    @NonNull
    @Override
    public EventListAdapter.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = mInflater.inflate(R.layout.item_event_card, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventListAdapter.EventViewHolder holder, int position) {
        if(mItems != null) {
            Event item = mItems.get(position);
            String attendance = "Attending: " + item.getAttending().size();

            holder.mTitle.setText(item.getTitle());
            holder.mDate.setText(item.getDate());
            holder.mHost.setText(item.getHostingGroup());
            holder.mDescription.setText(item.getDescription());
            holder.mLocation.setText(item.getLocation());
            holder.mAttendance.setText(attendance);
        }
    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mTitle;
        public final TextView mDate;
        public final TextView mHost;
        public final TextView mDescription;
        public final TextView mLocation;
        public final TextView mAttendance;
        public final ImageView mRsvpButton;

        public EventViewHolder(View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.event_title);
            mDate = itemView.findViewById(R.id.event_datetime);
            mHost = itemView.findViewById(R.id.event_host);
            mDescription = itemView.findViewById(R.id.event_description);
            mLocation = itemView.findViewById(R.id.event_location);
            mAttendance = itemView.findViewById(R.id.event_attendance_number);

            mRsvpButton = itemView.findViewById(R.id.rsvp_image_button);
            mRsvpButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String eventTitle = mTitle.getText().toString();
            String group = mHost.getText().toString();

            mDatabaseReference.child("events").child(group).child(eventTitle).child("attending").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String eventTitle = mTitle.getText().toString();
                    String group = mHost.getText().toString();

                    mRsvpCount = dataSnapshot.getChildrenCount();

                    mDatabaseReference.child("events").child(group).child(eventTitle).child("attending").child(String.valueOf(mRsvpCount)).setValue(mUser);
                    mRsvpButton.setImageResource(R.drawable.ic_rsvp_added);
                    mRsvpButton.setEnabled(false);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(mContext, "Failed to RSVP to event ..", Toast.LENGTH_LONG).show();
                }
            });
            Toast.makeText(mContext, "Your RSVP has been added!", Toast.LENGTH_LONG).show();
        }
    }
}
