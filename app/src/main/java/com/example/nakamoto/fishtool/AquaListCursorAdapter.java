package com.example.nakamoto.fishtool;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.NAME_COLUMN;

public class AquaListCursorAdapter extends CursorRecyclerViewAdapter<AquaListCursorAdapter.ViewHolder>{

    public AquaListCursorAdapter(Context context, Cursor cursor){
        super(context,cursor);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewRecycler;
        public ViewHolder(View view) {
            super(view);
            textViewRecycler = view.findViewById(R.id.aqua_card_title);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_animated, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, Cursor cursor) {
        viewHolder.textViewRecycler.setText(cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN)));


    }
}