package com.github.nakamotossh.fishtool.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nakamotossh.fishtool.R;
import com.github.nakamotossh.fishtool.extras.ParamUtils;

import java.util.Calendar;

import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.DATE_PARAM_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.NH3_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.PH_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.TEMP_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry._paramID;

public class ParamListAdapter extends CursorRecyclerViewAdapter<ParamListAdapter.ViewHolder>{

    private static final String TAG = "ParamListAdapter";
    private Context mContext;
    private String paramColumn;
    private final int paramCode;

    public ParamListAdapter(Context context, Cursor aquaCursor, int paramCode) {
        super(context, aquaCursor, paramCode);
        mContext = context;
        this.paramCode = paramCode;
        switch (paramCode){
            case ParamUtils.PH_PARAM:
                paramColumn = PH_COLUMN;
                break;
            case ParamUtils.TEMP_PARAM:
                paramColumn = TEMP_COLUMN;
                break;
            case ParamUtils.AMMONIA_PARAM:
                paramColumn = NH3_COLUMN;
                break;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        TextView date;
        TextView hour;
        TextView value;
        TextView variation;

        public ViewHolder(View v) {
            super(v);
            layout = v.findViewById(R.id.parent_param);
            date = v.findViewById(R.id.date_param);
            hour = v.findViewById(R.id.hour_param);
            value = v.findViewById(R.id.value_param);
            variation = v.findViewById(R.id.variation_param);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_params, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, Cursor c) {
        int currentPosition = c.getPosition();
        final int currentId = c.getInt(c.getColumnIndexOrThrow(_paramID));
        final Uri currentUri = Uri.withAppendedPath(c.getNotificationUri(), String.valueOf(currentId));

        /* Hide item case NULL */
        if (c.isNull(c.getColumnIndexOrThrow(paramColumn))) {
            vh.layout.setVisibility(View.GONE);
            vh.layout.setLayoutParams(new LinearLayoutCompat.LayoutParams(0,0));
            return;
        } else {
            vh.layout.setVisibility(View.VISIBLE);
        }

        /* Remove on Long Click */
        vh.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Would you like to delete this item?")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (dialog != null)
                                    dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /* Set Values to Null, Null Values are not shown on List */
                                ContentValues values = new ContentValues();
                                values.putNull(paramColumn);
                                /* Now Update */
                                int rowsUpdated = mContext.getContentResolver()
                                        .update(currentUri, values, _paramID, new String[]{String.valueOf(currentId)});
                                int NOTHING_UPDATED = 0;
                                if (rowsUpdated != NOTHING_UPDATED){
                                    Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show();
                                } else{
                                    Toast.makeText(mContext, "Deletion Fail", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .create()
                        .show();
                return true;
            }
        });

        /* Date/Time */
        long dateInMilli = c.getLong(c.getColumnIndexOrThrow(DATE_PARAM_COLUMN));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateInMilli);
        /* Format to day/month */
        String date = DateUtils.formatDateTime(mContext, dateInMilli, DateUtils.FORMAT_NUMERIC_DATE);
        vh.date.setText(date);
        /* Format to Hour */
        String time = DateUtils.formatDateTime(mContext, dateInMilli, DateUtils.FORMAT_SHOW_TIME);
        vh.hour.setText(time);

        /* Param Value */
        float value = c.getFloat(c.getColumnIndexOrThrow(paramColumn));
        vh.value.setText((String.valueOf(value)));

        /* Variation */
        if (c.moveToNext()){
            float nextValue = c.getFloat(c.getColumnIndexOrThrow(paramColumn));

            /* Format Decimal digits */
            Float variationResult = value - nextValue;
            String formattedResult = ParamUtils.formatNumber(String.valueOf(variationResult), paramCode);
            if(variationResult > 0){
                vh.variation.setText(String.valueOf("+" + formattedResult));
                vh.variation.setTextColor(Color.rgb(76,175,80));
            } else if (variationResult == 0){
                vh.variation.setText(String.valueOf(formattedResult));
            } else {
                vh.variation.setText(String.valueOf(formattedResult));
                vh.variation.setTextColor(Color.RED);
            }

        } else {
            vh.variation.setText("0.0");
        }

        /* Always return cursor to current position after changes */
        c.moveToPosition(currentPosition);
    }
}
