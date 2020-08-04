package edu.ranken.jbuesking.gettogether.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import edu.ranken.jbuesking.gettogether.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.ranken.jbuesking.gettogether.data.GetTogetherApp;
import edu.ranken.jbuesking.gettogether.data.entity.Group;
import edu.ranken.jbuesking.gettogether.ui.activity.ViewGroupActivity;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.GroupViewHolder> {
    private String mUser;
    private List<Group> mItems;
    private final LayoutInflater mInflater;
    private final Activity mContext;

    public GroupListAdapter(Activity context, List<Group> items, String user) {
        mUser = user;
        mItems = items;
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public void setItems(List<Group> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { return mItems != null ? mItems.size() : 0; }

    @NonNull
    @Override
    public GroupListAdapter.GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = mInflater.inflate(R.layout.item_group_card, parent, false);
        return new GroupViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupListAdapter.GroupViewHolder holder, int position) {
        if(mItems != null) {
            Group item = mItems.get(position);
            String memberCount = "Members: " + item.getMembers().size();

            holder.mGroupName.setText(item.getName());
            holder.mGroupDescription.setText(item.getDescription());
            holder.mGroupMemberCount.setText(memberCount);
        }
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mGroupName;
        public final TextView mGroupDescription;
        public final TextView mGroupMemberCount;

        public GroupViewHolder(View itemView) {
            super(itemView);

            mGroupName = itemView.findViewById(R.id.dash_group_title);
            mGroupDescription = itemView.findViewById(R.id.dash_group_description);
            mGroupMemberCount = itemView.findViewById(R.id.dash_group_member_count);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ViewGroupActivity.class);
            intent.putExtra(GetTogetherApp.EXTRA_VIEW_GROUP_NAME, mGroupName.getText().toString());
            intent.putExtra(GetTogetherApp.EXTRA_VIEW_GROUP_USER, mUser);

            mContext.startActivity(intent);
        }
    }
}
