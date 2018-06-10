package com.github.nakamotossh.fishtool.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.nakamotossh.fishtool.R;

public class ParamListAdapter extends CursorRecyclerViewAdapter<ParamListAdapter.ViewHolder>{

    public ParamListAdapter(Context context, Cursor aquaCursor) {
        super(context, aquaCursor);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        
        LinearLayout layout;
        TextView date;
        TextView hour;
        TextView value;
        TextView variation;
        ImageButton button;

        public ViewHolder(View v) {
            super(v);
            layout = v.findViewById(R.id.parent_param);
            date = v.findViewById(R.id.date_param);
            hour = v.findViewById(R.id.hour_param);
            value = v.findViewById(R.id.value_param);
            variation = v.findViewById(R.id.variation_param);
            button = v.findViewById(R.id.button_param);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor mAquaCursor) {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.params_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
}
