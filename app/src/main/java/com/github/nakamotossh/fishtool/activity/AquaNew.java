package com.github.nakamotossh.fishtool.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.nakamotossh.fishtool.R;
import com.github.nakamotossh.fishtool.fragments.NewFragment;

import static com.github.nakamotossh.fishtool.debug.WakeUp.riseAndShine;
import static com.github.nakamotossh.fishtool.fragments.NewFragment.onChanged;

public class AquaNew extends AppCompatActivity {

    //TODO: [ERROR] E/ActivityThread: Performing stop of activity that is already stopped
    //TODO: [OPTIMIZE] Realease resource: Close Cursor, Close Db

    private static final String TAG = "NEWAQUA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aquanew);

        /* TODO: remove before release */
        riseAndShine(this);

        /* Start Fragment */
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frag_conteiner, new NewFragment())
                .commit();
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to discard this aquarium?");
        if (onChanged){
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (dialog != null){
                        dialog.dismiss();
                    }
                }
            });
            builder.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AquaNew.super.onBackPressed();
                }
            });
            builder.create().show();
        } else {
            super.onBackPressed();
        }
    }



}
