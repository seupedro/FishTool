package com.example.nakamoto.fishtool.adapters;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;

import com.example.nakamoto.fishtool.R;
import com.example.nakamoto.fishtool.activity.AquaInfo;

import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.NAME_COLUMN;

public class AquaListCursorAdapter extends CursorRecyclerViewAdapter<AquaListCursorAdapter.ViewHolder>{

    private static final String TAG = "AquaListCursorAdapter";
    private int mExpandedPosition = -1;
    private int previousExpandedPosition;
    private Context mContext;

    public AquaListCursorAdapter(Context context, Cursor aquaCursor){
        super(context, aquaCursor);
        mContext = context;
    }

    public AquaListCursorAdapter(Context context, Cursor aquaCursor, Cursor paramCursor){
        super(context, aquaCursor, paramCursor);
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardViewParent;
        Button expandButton;
        ConstraintLayout constraintLayout;
        TableLayout tableLayout;

        public ViewHolder(View view) {
            super(view);
            cardViewParent = view.findViewById(R.id.card_parent);
            expandButton = view.findViewById(R.id.expand_collapse_button);
            constraintLayout = view.findViewById(R.id.constraint_card_child);
            tableLayout = view.findViewById(R.id.card_tableLayout);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final Cursor cursor) {

        /* Set Layout Animations on Change */
        viewHolder.cardViewParent.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        viewHolder.constraintLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        /* Check is expanded */
        final int position = viewHolder.getAdapterPosition();
        final boolean isExpanded = position == mExpandedPosition;

        /* Display views on layout */
        viewHolder.tableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        viewHolder.expandButton.setText(isExpanded ? "Collapse" : "Expand");

        viewHolder.tableLayout.setActivated(isExpanded);

        if (isExpanded)
            previousExpandedPosition = position;

        viewHolder.expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1 : position;
                notifyItemChanged(previousExpandedPosition);
                notifyItemChanged(position);
            }
        });

        viewHolder.cardViewParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AquaInfo.class);
                intent.putExtra("id", cursor.getPosition());
                intent.putExtra("name", cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN)));
                mContext.startActivity(intent);
            }
        });

    }
}