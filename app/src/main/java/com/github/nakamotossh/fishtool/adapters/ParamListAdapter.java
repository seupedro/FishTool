package com.github.nakamotossh.fishtool.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nakamotossh.fishtool.R;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.DATE_PARAM_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.NH3_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.NH4_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.PARAM_CONTENT_URI;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.PH_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.TEMP_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry._paramID;

public class ParamListAdapter extends CursorRecyclerViewAdapter<ParamListAdapter.ViewHolder>{

    private static final String TAG = "ParamListAdapter";
    private int paramCode;
    private Context mContext;

    /* Param Codes */
    public static final int PH_PARAM = 0;
    public static final int TEMP_PARAM = 1;
    public static final int NH3_PARAM = 2;
    public static final int NH4_PARAM = 3;

    public ParamListAdapter(Context context, Cursor aquaCursor, int param) {
        super(context, aquaCursor, param);
        paramCode = param;
        mContext = context;
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

    @Override
    public void onBindViewHolder(ViewHolder vh, Cursor c) {
        int currentPosition = c.getPosition();
        final int currentId = c.getInt(c.getColumnIndexOrThrow(_paramID));

        /* Remove on Long Click */
        vh.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Would you like to remove this item?")
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
                                String where = _paramID + " = " + currentId;
                                int rowDeleted = mContext.getContentResolver()
                                        .delete(PARAM_CONTENT_URI, where, null);
                                int NOTHING_DELETED = 0;
                                if (rowDeleted != NOTHING_DELETED){
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

        //TODO: Class instead of Switch/Case
        /* Param Value */
        float value = 0;
        switch (paramCode){
            case PH_PARAM:
                value = c.getFloat(c.getColumnIndexOrThrow(PH_COLUMN));
                break;
            case TEMP_PARAM:
                value = c.getFloat(c.getColumnIndexOrThrow(TEMP_COLUMN));
                break;
            case NH3_PARAM:
                value = c.getFloat(c.getColumnIndexOrThrow(NH3_COLUMN));
                break;
            case NH4_PARAM:
                value = c.getFloat(c.getColumnIndexOrThrow(NH4_COLUMN));
                break;
            default:
                try {
                    throw new Exception();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        vh.value.setText((String.valueOf(value)));

        /* Variation */
        if (c.moveToNext()){
            float nextValue = c.getFloat(c.getColumnIndexOrThrow(PH_COLUMN));
            /* Format Decimal digits */
            Float variationResult = value - nextValue;
            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
            numberFormat.setMaximumFractionDigits(1);
            numberFormat.setMinimumFractionDigits(1);
            numberFormat.setRoundingMode(RoundingMode.HALF_UP);

            if(variationResult > 0){
                vh.variation.setText(String.valueOf("+" + numberFormat.format(variationResult)));
                vh.variation.setTextColor(Color.rgb(76,175,80));
            } else if (variationResult == 0){
                vh.variation.setText(String.valueOf(numberFormat.format(variationResult)));
            } else {
                vh.variation.setText(String.valueOf(numberFormat.format(variationResult)));
                vh.variation.setTextColor(Color.RED);
            }

        } else {
            vh.variation.setText("0.0");
        }

        /* Always return cursor to current position after changes */
        c.moveToPosition(currentPosition);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.params_item, parent, false);
        return new ViewHolder(view);
    }
}
