package com.example.nakamoto.fishtool.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nakamoto.fishtool.R;
import com.example.nakamoto.fishtool.database.AquaDbHelper;
import com.myhexaville.smartimagepicker.ImagePicker;
import com.myhexaville.smartimagepicker.OnImagePickedListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.AQUA_TABLE;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.CO2_COLUMN;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.DATE_AQUA_COLUMN;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.DOSAGE_COLUMN;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.IMAGE_URI_COLUMN;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.LIGHT_COLUMN;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.LITERS_COLUMN;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.NAME_COLUMN;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.NOTES_COLUMN;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.STATUS_COLUMN;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.SUBSTRATE_COLUMN;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.TYPE_COLUMN;

public class AquaNew extends AppCompatActivity {

    //TODO: [ERROR] E/ActivityThread: Performing stop of activity that is already stopped

    private static final String TAG = "NEWAQUA";

    private ImageView aquaImage;
    private EditText aquaName;
    private static EditText aquaDate;
    private Spinner aquaType;
    private Spinner aquaStatus;
    private EditText aquaLiters;
    private EditText aquaLight;
    private EditText aquaCo2;
    private EditText aquaDose;
    private EditText aquaSubstrate;
    private EditText aquaNote;
    private FloatingActionButton fab;
    private AquaDbHelper dbHelper;
    private ImageButton getDateButton;
    private ImagePicker imagePicker;
    private Uri photoUri = null;

    private boolean onChanged = false;
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            onChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aqua_new);

        /* Find views on layout */
        aquaImage = findViewById(R.id.aqua_photo);
        aquaName = findViewById(R.id.aqua_name);
        aquaType = findViewById(R.id.aqua_type);
        aquaStatus = findViewById(R.id.aqua_status);
        aquaLiters = findViewById(R.id.aqua_liters);
        aquaLight = findViewById(R.id.aqua_light);
        aquaCo2 = findViewById(R.id.aqua_co2);
        aquaSubstrate = findViewById(R.id.aqua_substrate);
        aquaDose = findViewById(R.id.aqua_dose);
        aquaDate = findViewById(R.id.aqua_date);
        aquaNote = findViewById(R.id.aqua_note);
        fab = findViewById(R.id.fab);
        getDateButton = findViewById(R.id.date_button);

        /* Track changes */
        aquaImage.setOnTouchListener(touchListener);
        aquaName.setOnTouchListener(touchListener);
        aquaType.setOnTouchListener(touchListener);
        aquaStatus.setOnTouchListener(touchListener);
        aquaLiters.setOnTouchListener(touchListener);
        aquaLight.setOnTouchListener(touchListener);
        aquaCo2.setOnTouchListener(touchListener);
        aquaSubstrate.setOnTouchListener(touchListener);
        aquaDose.setOnTouchListener(touchListener);
        aquaDate.setOnTouchListener(touchListener);
        getDateButton.setOnTouchListener(touchListener);
        aquaNote.setOnTouchListener(touchListener);

        /* Open DB Connection */
        dbHelper = new AquaDbHelper(this);

        /* Set listener on fab */
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Image Picker! */
                imagePicker = new ImagePicker(AquaNew.this, null, new OnImagePickedListener() {
                    @Override
                    public void onImagePicked(Uri imageUri) {
                        aquaImage.setImageURI(imageUri);
                        photoUri = imageUri;
                    }
                }).setWithImageCrop(1,1);

                /* Ask where get images from */
                AlertDialog.Builder builder = new AlertDialog.Builder(AquaNew.this);
                builder.setTitle("Add Photo");
                /* Dialog Option */
                final int CAMERA_OPTION = 0;
                final int GALLERY_OPTION = 1;
                builder.setItems(new CharSequence[]{"Camera", "Gallery"},
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichOption) {
                        if (whichOption == CAMERA_OPTION){
                            imagePicker.openCamera();
                        }
                        if (whichOption == GALLERY_OPTION){
                            imagePicker.choosePicture(false);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog !=  null){
                            dialog.dismiss();
                        }
                    }
                });
                builder.create().show();
            }
        });

        /* Show Date Picker on date field */
        aquaDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        /* Get current date on Click */
        getDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String currentDate = simpleDateFormat.format(calendar.getTime());
                aquaDate.setText(currentDate);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.handleActivityResult(resultCode, requestCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.handlePermission(requestCode, grantResults);
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
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

    private void readFromDb() {
        /* Get database */
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        /* Define data retrive */
        Cursor c = db.query(
                AQUA_TABLE,
                null,
                null,
                null,
                null,
                null,
                null
        );

        c.moveToFirst();
        aquaName.setText(c.getString(c.getColumnIndex(NAME_COLUMN)));
        aquaType.setSelection(c.getInt(c.getColumnIndex(TYPE_COLUMN)));
        aquaStatus.setSelection(c.getInt(c.getColumnIndex(STATUS_COLUMN)), true);
    }

    private void saveValues() {

        /* Parsing Fields to string */
        String strAquaName = aquaName.getText().toString().trim();
        String strAquaDate = aquaDate.getText().toString().trim();
        String strAquaLiters = aquaLiters.getText().toString().trim();
        String strAquaLight = aquaLight.getText().toString().trim();
        String strAquaCo2 = aquaCo2.getText().toString().trim();
        String strAquaDose = aquaDose.getText().toString().trim();
        String strAquaSubstrate = aquaSubstrate.getText().toString().trim();
        String strAquaNote = aquaNote.getText().toString().trim();
        int intAquaStatus = ((int) aquaStatus.getSelectedItemId());
        int intAquaType = ((int) aquaType.getSelectedItemId());

        /* Get Database */
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        /* Insert data */
        ContentValues values = new ContentValues();

        /* Insert only filled fields */
        if (photoUri != null){
            values.put(IMAGE_URI_COLUMN, photoUri.toString());
        }
        /* Prevent saving without a name */
        if (!TextUtils.isEmpty(strAquaName)){
            values.put(NAME_COLUMN, strAquaName);
        } else {
            /* Give a feedback to the user */
            Toast.makeText(this, "Name field cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isEmpty(strAquaDate)){
            values.put(DATE_AQUA_COLUMN, strAquaDate);
        }
        if (!TextUtils.isEmpty(strAquaLiters)){
            values.put(LITERS_COLUMN, strAquaLiters);
        }
        if (!TextUtils.isEmpty(strAquaLight)){
            values.put(LIGHT_COLUMN, strAquaLight);
        }
        if (!TextUtils.isEmpty(strAquaCo2)){
            values.put(CO2_COLUMN, strAquaCo2);
        }
        if (!TextUtils.isEmpty(strAquaDose)){
            values.put(DOSAGE_COLUMN, strAquaDose);
        }
        if (!TextUtils.isEmpty(strAquaSubstrate)){
            values.put(SUBSTRATE_COLUMN, strAquaSubstrate);
        }
        if (!TextUtils.isEmpty(strAquaNote)){
            values.put(NOTES_COLUMN, strAquaNote);
        }
        /* Unnecessary verification */
        values.put(TYPE_COLUMN, intAquaType);
        values.put(STATUS_COLUMN, intAquaStatus);
        /* Insert into Db */
        long newRows = db.insert(AQUA_TABLE, null, values);
        /* TODO: [Remove] Debug purpose */
        Toast.makeText(this, String.valueOf(newRows), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_aqua, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                saveValues();
                //finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
        Log.d(TAG, "showDatePickerDialog: ");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            Log.d(TAG, "onCreateDialog: ");
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            aquaDate.setText(day + "/" + month + "/" + year);
            Log.d(TAG, "onDateSet: ");
        }
    }
}
