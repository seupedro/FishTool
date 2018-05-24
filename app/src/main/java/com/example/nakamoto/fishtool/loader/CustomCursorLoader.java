package com.example.nakamoto.fishtool.loader;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;

import com.example.nakamoto.fishtool.database.AquaDbHelper;

import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.AQUA_TABLE;

public class CustomCursorLoader extends CursorLoader {

    private Context mContext;
    public CustomCursorLoader(Context context) {
        super(context);
        mContext = context;
    }
    //asd

    @Override
    public Cursor loadInBackground() {
        Cursor cursor = new AquaDbHelper(mContext).getWritableDatabase().query(AQUA_TABLE,
                null,
                null,
                null,
                null,
                null,
                null);

        return cursor;

    }
}
