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
import android.widget.TextView;

import com.example.nakamoto.fishtool.R;
import com.example.nakamoto.fishtool.activity.AquaInfo;

import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.NAME_COLUMN;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.TYPE_COLUMN;

public class AquaListCursorAdapter extends CursorRecyclerViewAdapter<AquaListCursorAdapter.ViewHolder>{

    //TODO: Fix Fab overrinding last aquarium

    private static final String TAG = "AQUA_ADAPTER";
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
        TextView title;
        TextView subtitle;

        public ViewHolder(View view) {
            super(view);
            cardViewParent = view.findViewById(R.id.card_parent);
            expandButton = view.findViewById(R.id.expand_collapse_button);
            constraintLayout = view.findViewById(R.id.constraint_card_child);
            tableLayout = view.findViewById(R.id.card_tableLayout);
            title = view.findViewById(R.id.title_card);
            subtitle = view.findViewById(R.id.subtitle_card);
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

        /* Show aqua features */
        final int MATCH_DATABASE_ID = 1;
        viewHolder.cardViewParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AquaInfo.class);
                /**
                 * Must be viewHolder.getAdapterPosition. Cursor.getPositon won't works correctly,
                 * because cursor stops on which views are displayed on screen.
                 *
                 * And + 1 cause adapter starts from 0 and table id starts from 1.
                 * */
                intent.putExtra("aquaId", (viewHolder.getAdapterPosition() + MATCH_DATABASE_ID));
                mContext.startActivity(intent);
            }
        });

        String name = cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN));
        int type = cursor.getInt(cursor.getColumnIndexOrThrow(TYPE_COLUMN));

        /* Set Title name */
        viewHolder.title.setText(name);

        /* Aqua type values */
        final int FRESHWATER = 0;
        final int MARINE = 1;
        final int BRACKISH = 2;

        switch (type){
            case FRESHWATER:
                viewHolder.subtitle.setText("Freshwater Aquarium");
                break;
            case MARINE:
                viewHolder.subtitle.setText("Marine Aquarium");
                break;
            case BRACKISH:
                viewHolder.subtitle.setText("Brackish Aquariums");
                break;
            default:
                viewHolder.subtitle.setText("");
        }
    }
}