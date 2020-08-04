package edu.ranken.jbuesking.gettogether.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import edu.ranken.jbuesking.gettogether.R;
import edu.ranken.jbuesking.gettogether.data.GetTogetherApp;
import edu.ranken.jbuesking.gettogether.data.entity.User;
import edu.ranken.jbuesking.gettogether.ui.activity.UserProfileActivity;

public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.MemberViewHolder> {
    private List<User> mItems;
    private final LayoutInflater mInflater;
    private final Activity mContext;

    public MemberListAdapter(Activity context, List<User> items) {
        mItems = items;
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    @NonNull
    @Override
    public MemberListAdapter.MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = mInflater.inflate(R.layout.item_member, parent, false);
        return new MemberViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberListAdapter.MemberViewHolder holder, int position) {
        if(mItems != null){
            User item = mItems.get(position);

            List<String> interests = item.getInterests();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i < interests.size(); ++i) {
                sb.append(interests.get(i));
                sb.append(", ");
            }

            holder.mUsername.setText(item.getUsername());
            holder.mLocation.setText(item.getLocation());
            holder.mInterests.setText(sb.toString());
        }
    }

    public class MemberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mUsername;
        public final TextView mLocation;
        public final TextView mInterests;

        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);

            mUsername = itemView.findViewById(R.id.member_username);
            mLocation = itemView.findViewById(R.id.member_location);
            mInterests = itemView.findViewById(R.id.member_interests);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, UserProfileActivity.class);
            intent.putExtra(GetTogetherApp.EXTRA_VIEW_PROFILE_USERNAME, mUsername.getText().toString());

            mContext.startActivity(intent);
        }
    }
}
