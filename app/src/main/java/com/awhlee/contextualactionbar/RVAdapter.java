package com.awhlee.contextualactionbar;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class RVAdapter extends RecyclerView.Adapter<RVAdapter.RViewHolder>
        implements View.OnClickListener, View.OnLongClickListener {

    Context mContext;
    List<ListInfo> mDataSet = new ArrayList<>();
    RecyclerView mParent;
    LinearLayoutManager mLlm;
    MultiSelectManager mMultiSelectManager;

    /**
     * The data objects that this list is managing
     */
    public static class ListInfo {
        int mId;
        String mString;

        public ListInfo(int id, String theString) {
            mId = id;
            mString = theString;
        }
    }

    /**
     * The custom ViewHolder for this adapter
     */
    public static class RViewHolder extends RecyclerView.ViewHolder {
        TextView mTv;
        ImageView mIm;
        View mBaseView;

        public RViewHolder(View itemView) {
            super(itemView);
            mTv = (TextView)itemView.findViewById(R.id.list_row_text);
            mIm  = (ImageView)itemView.findViewById(R.id.list_row_image);
            mBaseView = itemView;
        }
    }

    public RVAdapter(Context context) {
        mContext = context;

        // Generate the fake information to show in the list.
        for (int counter = 0; counter < 100; counter++) {
            mDataSet.add(new ListInfo(counter, UUID.randomUUID().toString()));
        }
        setHasStableIds(true);
    }

    public void setMultiSelectManager(MultiSelectManager mSm) {
        mMultiSelectManager = mSm;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mParent = recyclerView;
        mLlm = new LinearLayoutManager(mContext);
        mParent.setLayoutManager(mLlm);
    }

    /**
     * Create a view holder which will encapsulate the rendering of each ListInfo item
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, null);
        RViewHolder viewHolder = new RViewHolder(view);

        view.setOnClickListener(this);
        view.setOnLongClickListener(this);

        return viewHolder;
    }

    /**
     * Render the item within the view holder's widgets
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RViewHolder holder, int position) {
        if (mMultiSelectManager.isMultiSelect() == true) {
            holder.mIm.setVisibility(View.VISIBLE);
        } else {
            holder.mIm.setVisibility(View.GONE);
        }

        ListInfo info = mDataSet.get(position);

        if (mMultiSelectManager.isSelected(info)) {
            // Give it the selected background
            holder.mBaseView.setBackgroundColor(0xFF36AED6);
        } else {
            // Give it the unselected background
            holder.mBaseView.setBackgroundColor(0xFFFFFFFF);
        }

        String theString = info.mString;
        holder.mTv.setText(theString + " id: " + info.mId);
        return;
    }

    /**
     * RecyclerView scaffolding
     * @return
     */
    @Override
    public int getItemCount() { return mDataSet.size(); }

    /**
     * RecyclerView scaffolding
     * @return
     */
    @Override
    public long getItemId(int position) {
        return mDataSet.get(position).mId;
    }

    @Override
    public void onClick(View v) {
        if (mMultiSelectManager.isMultiSelect()) {
            // Add this item to the multi select manager's list of selected items
            // and force a redraw to reflect its new state.
            int position = mLlm.getPosition(v);
            mMultiSelectManager.add(mDataSet.get(position));
            notifyItemChanged(position);
        } else {
            Toast.makeText(mContext, "Congrats, you selected an item!!!!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mMultiSelectManager.isMultiSelect()) {
            // If we are already in multi select mode...no need to do it again.
            return true;
        }

        // Turn on multi select mode and add this item in the select list.
        mMultiSelectManager.setIsMultiSelect(true);
        int position = mLlm.getPosition(v);
        mMultiSelectManager.add(mDataSet.get(position));

        redrawVisibleItems();
        return true;
    }

    /**
     * Helper function so that the multi select manager can force a redraw of the items in
     * the recycler view when modes change.
     */
    public void redrawVisibleItems() {
        // force a redraw of visible items
        int firstVisible = mLlm.findFirstVisibleItemPosition();
        int lastVisible = mLlm.findLastVisibleItemPosition();

        for (int counter = firstVisible; counter <= lastVisible; counter++) {
            notifyItemChanged(counter);
        }
    }
}