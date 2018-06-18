package com.github.nakamotossh.fishtool.adapters;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.github.nakamotossh.fishtool.R;
import com.github.nakamotossh.fishtool.activity.AquaInfo;

import static com.github.nakamotossh.fishtool.database.AquaContract.AquaEntry.IMAGE_URI_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.AquaEntry.NAME_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.AquaEntry.TYPE_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.AquaEntry._aquaID;

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardViewParent;
        Button expandButton;
        ConstraintLayout constraintLayout;
        TableLayout tableLayout;
        TextView title;
        TextView subtitle;
        ImageView photo;

        public ViewHolder(View view) {
            super(view);
            cardViewParent = view.findViewById(R.id.card_parent);
            expandButton = view.findViewById(R.id.expand_collapse_button);
            constraintLayout = view.findViewById(R.id.constraint_card_child);
            tableLayout = view.findViewById(R.id.card_tableLayout);
            title = view.findViewById(R.id.title_card);
            subtitle = view.findViewById(R.id.subtitle_card);
            photo = view.findViewById(R.id.aqua_image);
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
    public void onBindViewHolder( ViewHolder viewHolder, Cursor cursor) {

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

       final int aquaId = cursor.getInt(cursor.getColumnIndexOrThrow(_aquaID));

        /* Show aqua features */
        viewHolder.cardViewParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AquaInfo.class);
                /**
                 * Neither getAdapterPosition or getPosition could work.
                 *
                 * The viewHolder.getAdapterPosition changes if itens on db are deleted.
                 *
                 * Cursor.getPositon won't works correctly,
                 * because cursor stops on which views are displayed on screen.
                 *
                 * */
                intent.putExtra("aquaId", (aquaId));
                mContext.startActivity(intent);
            }
        });

        String name = cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN));
        int type = cursor.getInt(cursor.getColumnIndexOrThrow(TYPE_COLUMN));
        String image = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_URI_COLUMN));

        /* Set Title name */
        viewHolder.title.setText(name);
        /* Photo */
        viewHolder.photo.setImageURI(image != null ? Uri.parse(image) : null);
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