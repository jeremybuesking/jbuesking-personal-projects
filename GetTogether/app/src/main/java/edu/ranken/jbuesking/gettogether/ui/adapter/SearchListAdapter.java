package edu.ranken.jbuesking.gettogether.ui.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
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
import edu.ranken.jbuesking.gettogether.data.entity.Group;
import edu.ranken.jbuesking.gettogether.ui.activity.ViewGroupActivity;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.SearchViewHolder> {
    private String mUser;
    private List<Group> mItems;
    private final LayoutInflater mInflater;
    private final Activity mContext;

    public SearchListAdapter(Activity context, List<Group> items, String user) {
        mUser = user;
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
    public SearchListAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = mInflater.inflate(R.layout.item_search_result, parent, false);
        return new SearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchListAdapter.SearchViewHolder holder, int position) {
        if(mItems != null){
            Group item = mItems.get(position);

            List<String> interests = item.getInterestTags();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i < interests.size(); ++i) {
                sb.append(interests.get(i));
                sb.append(", ");
            }
            String groupSize = String.valueOf(item.getMembers().size());

            holder.mName.setText(item.getName());
            holder.mMembers.setText(groupSize);
            holder.mDescription.setText(item.getDescription());
            holder.mInterests.setText(sb.toString());
        }
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mName;
        public TextView mMembers;
        public TextView mInterests;
        public TextView mDescription;

        public SearchViewHolder(@NonNull View itemView){
            super(itemView);

            mName = itemView.findViewById(R.id.search_result_name);
            mMembers = itemView.findViewById(R.id.search_result_members);
            mInterests = itemView.findViewById(R.id.search_result_interests);
            mDescription = itemView.findViewById(R.id.search_result_description);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ViewGroupActivity.class);
            intent.putExtra(GetTogetherApp.EXTRA_VIEW_GROUP_NAME, mName.getText().toString());
            intent.putExtra(GetTogetherApp.EXTRA_VIEW_GROUP_USER, mUser);

            /*ActivityOptions options = ActivityOptions
                .makeSceneTransitionAnimation(mContext, mName, "robot");*/

            mContext.startActivity(intent);
        }
    }
}
