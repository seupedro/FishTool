package com.github.nakamotossh.fishtool.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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

import com.github.nakamotossh.fishtool.MyGlideEngine;
import com.github.nakamotossh.fishtool.R;
import com.github.nakamotossh.fishtool.database.AquaDbHelper;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.nio.channels.IllegalSelectorException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static android.text.format.DateFormat.getDateFormat;
import static com.github.nakamotossh.fishtool.database.AquaContract.AquaEntry.AQUA_CONTENT_URI;
import static com.github.nakamotossh.fishtool.database.AquaContract.AquaEntry.CO2_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.AquaEntry.DATE_AQUA_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.AquaEntry.DOSAGE_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.AquaEntry.FILTER_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.AquaEntry.IMAGE_URI_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.AquaEntry.LIGHT_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.AquaEntry.LITERS_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.AquaEntry.NAME_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.AquaEntry.NOTES_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.AquaEntry.SIZE_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.AquaEntry.STATUS_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.AquaEntry.SUBSTRATE_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.AquaEntry.TYPE_COLUMN;
import static com.github.nakamotossh.fishtool.debug.WakeUp.riseAndShine;

public class AquaNew extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    //TODO: [ERROR] E/ActivityThread: Performing stop of activity that is already stopped
    //TODO: Handle correctly the date
    //TODO: [POS-Release] Save data before on Destroy. Handle Activity lifecycle in case receve a call
    //TODO: Handle Images

    private static final String TAG = "AquaNew";
    private static final int STORAGE_PERMISSION = 320;
    private static final int REQUEST_IMAGE = 330;
    private final int LOADER_ID = 0;
    private Uri aquaIdUri;
    private boolean hasExtraUri = false;
    private static long aquaDateMilliseconds;

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
    private EditText aquaSize;
    private EditText aquaFilter;
    private FloatingActionButton fab;
    private AquaDbHelper dbHelper;
    private ImageButton getDateButton;

    private Uri photoUri = null;

    public boolean onChanged = false;
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            onChanged = true;
            return false;
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aquanew);
        /* TODO: remove this before release */
        riseAndShine(this);

        /* Find views on layout */
        aquaImage = findViewById(R.id.aqua_photo);
        aquaName = findViewById(R.id.aqua_name);
        aquaSize = findViewById(R.id.aqua_size);
        aquaType = findViewById(R.id.aqua_type);
        aquaStatus = findViewById(R.id.aqua_status);
        aquaLiters = findViewById(R.id.aqua_liters);
        aquaLight = findViewById(R.id.aqua_light);
        aquaCo2 = findViewById(R.id.aqua_co2);
        aquaSubstrate = findViewById(R.id.aqua_substrate);
        aquaDose = findViewById(R.id.aqua_dose);
        aquaDate = findViewById(R.id.aqua_date);
        aquaNote = findViewById(R.id.aqua_note);
        aquaFilter = findViewById(R.id.aqua_filter);
        getDateButton = findViewById(R.id.date_button);
        fab = findViewById(R.id.fab_frag);

        /* Track changes */
        fab.setOnTouchListener(touchListener);
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
        aquaSize.setOnTouchListener(touchListener);
        aquaFilter.setOnTouchListener(touchListener);

        aquaDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        /* Define Activity Layout */
        hasExtraUri = getIntent().hasExtra("aquaId");
        if (hasExtraUri) {
            /* Define Title */
            setTitle("Edit Aquarium");
            /* Parse Uri */
            aquaIdUri = Uri.parse(getIntent()
                    .getExtras()
                    .getString("aquaId"));
            /* Start Loader */
            getLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            /* Set Title*/
            setTitle("New Aquarium");
            /* Get and Set Current Date */
            Calendar calendar = Calendar.getInstance();
            long dateInMilli = calendar.getTimeInMillis();
            SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
            /* Format Date based on Locale */
            String locateDate = formater.format(calendar.getTime());
            aquaDate.setText(locateDate);
        }

        /* Show Dialog Date Picker */
        getDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

        /* Get image */
        //TODO: check if is correctly implemennted
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Handle Permissions */
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1){
                    /* Check Permission */
                    if (ContextCompat.checkSelfPermission(
                            AquaNew.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED){
                        /* Show Explanation */
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                AquaNew.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                            AlertDialog.Builder builder = new AlertDialog.Builder(AquaNew.this);
                            builder.setMessage("In order to add photo for your aquarium, you must give permission first");
                            builder.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (dialog != null){
                                        dialog.dismiss();
                                    }
                                }
                            }).create().show();
                        }
                    } else {
                        /* Request Directly Permission */
                        ActivityCompat.requestPermissions(AquaNew.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
                    }

                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case STORAGE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    Matisse.from(AquaNew.this)
                            .choose(MimeType.allOf())
                            .countable(false)
                            .imageEngine(new MyGlideEngine())
                            .forResult(REQUEST_IMAGE);
                }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE && resultCode == RESULT_OK){
            List<Uri> imagesUriSelected;
            imagesUriSelected = Matisse.obtainResult(data);
            aquaImage.setImageURI(imagesUriSelected.get(0));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_aqua, menu);
        /* Menu Itens */
        final int SAVE_ITEM = 0;
        final int DELETE_ITEM = 1;
        /* Delete Visibility */
        menu.getItem(DELETE_ITEM).setVisible(hasExtraUri);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_button:
                checkAndSaveValues();
                return true;
            case R.id.delete_button:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Deleting Aquarium")
                        .setMessage("Are you sure? This action cannot be undone.")
                        .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /* Delete Data and Notify */
                                final int NOTHING_DELETED = 0;
                                int deleteUri = getContentResolver().delete(aquaIdUri, null, null);
                                if (deleteUri != NOTHING_DELETED){
                                    getContentResolver().notifyChange(aquaIdUri, null);
                                    Toast.makeText(AquaNew.this, "Deleted " + deleteUri, Toast.LENGTH_SHORT).show();
                                    /* Close this activity */
                                    startActivity(new Intent(AquaNew.this, AquaMain.class));
                                } else {
                                    Toast.makeText(AquaNew.this, "Delete failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (dialog != null){
                                    dialog.dismiss();
                                }
                            }
                        })
                        .create()
                        .show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayDummyData(){
        /* Dummy Data */
        aquaName.setText("Neon");
        aquaType.setOnTouchListener(touchListener);
        aquaStatus.setOnTouchListener(touchListener);
        aquaLiters.setText("200");
        aquaLight.setText("12w Bulb");
        aquaCo2.setText("No Co2");
        aquaSubstrate.setText("Gravel");
        aquaDose.setText("1000/liters");
       // aquaDate.setText("26/09/1994");
        aquaNote.setText("Leticia doida");
        aquaSize.setText("100x26x50");
        aquaFilter.setText("Hang on");
    }

    private void checkAndSaveValues() {

        /* TODO: Check values before insert */

        ContentValues values = new ContentValues();
        /* Image */
        if (photoUri != null){
            values.put(IMAGE_URI_COLUMN, photoUri.toString());
        }
        /* Name */
        if (aquaName != null && !aquaName.getText().toString().isEmpty()){
            values.put(NAME_COLUMN, aquaName.getText().toString().trim());
        } else {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        /* Date */
        if (aquaDate != null && !aquaDate.getText().toString().trim().isEmpty()){
            String dateString = aquaDate.getText().toString().trim();
            //TODO: convert date to locale br date before regex. Regex only works in brLocale.
            /* Match a regex */
            boolean dateRegex = Pattern
                    .compile("(^(\\d|0[\\d]|1[\\d]|2[\\d]|3[0-1])\\/(\\d|0[1-9]|1[0-2])\\/[1-2][0][0-2]\\d$)")
                    .matcher(dateString)
                    .matches();
            if (dateRegex){
                Log.d(TAG, "checkAndSaveValues: regex works");
                return;
            }

            Log.d(TAG, "checkAndSaveValues: fail");
            return;
        }

        /* Other fields */
        values.put(LITERS_COLUMN, aquaLiters.getText().toString().trim());
        values.put(STATUS_COLUMN, aquaStatus.getSelectedItemId());
        values.put(TYPE_COLUMN, aquaType.getSelectedItemId());
        values.put(CO2_COLUMN, aquaCo2.getText().toString().trim());
        values.put(SIZE_COLUMN, aquaSize.getText().toString().trim());
        values.put(LIGHT_COLUMN, aquaLight.getText().toString().trim());
        values.put(DOSAGE_COLUMN, aquaDose.getText().toString().trim());
        values.put(FILTER_COLUMN, aquaFilter.getText().toString().trim());
        values.put(NOTES_COLUMN, aquaNote.getText().toString().trim());
        values.put(SUBSTRATE_COLUMN, aquaSubstrate.getText().toString().trim());

        Log.d(TAG, "checkAndSaveValues: rest executed");
        /* TODO: Handle low storage with try/catch on insert/update */

        if ( hasExtraUri){
            final int NOTHING_UPDATED = 0;
            /* Update Data and Notify */
            int rowsUpdated = getContentResolver().update(aquaIdUri, values, null, null);
            if (rowsUpdated > NOTHING_UPDATED) {
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Updated failed", Toast.LENGTH_SHORT).show();
            }
        } else {
            /* Insert Data and Notify */
            Uri insertUri = getContentResolver().insert(AQUA_CONTENT_URI, values);
            if (insertUri != null){
                getContentResolver().notifyChange(insertUri, null);
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Save failed", Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (onChanged){
            builder.setMessage("Do you want to discard this changes?");
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
                    finish();
                }
            });
            builder.create().show();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                aquaIdUri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {

        if (c.moveToNext()){

            /* Get All Fields */
            String name = c.getString(c.getColumnIndexOrThrow(NAME_COLUMN));
            String liters = c.getString(c.getColumnIndexOrThrow(LITERS_COLUMN));
            String date = c.getString(c.getColumnIndexOrThrow(DATE_AQUA_COLUMN));
            int type = c.getInt(c.getColumnIndexOrThrow(TYPE_COLUMN));
            int status = c.getInt(c.getColumnIndexOrThrow(STATUS_COLUMN));
            String co2 = c.getString(c.getColumnIndexOrThrow(CO2_COLUMN));
            String dosage = c.getString(c.getColumnIndexOrThrow(DOSAGE_COLUMN));
            String substrate = c.getString(c.getColumnIndexOrThrow(SUBSTRATE_COLUMN));
            String notes = c.getString(c.getColumnIndexOrThrow(NOTES_COLUMN));
            String size = c.getString(c.getColumnIndexOrThrow(SIZE_COLUMN));
            String filter = c.getString(c.getColumnIndexOrThrow(FILTER_COLUMN));
            String light = c.getString(c.getColumnIndexOrThrow(LIGHT_COLUMN));
            String image = c.getString(c.getColumnIndexOrThrow(IMAGE_URI_COLUMN));

            /* Name */
            aquaName.setText(name != null && !name.isEmpty() ? name : "" );
            /* Liters */
            aquaLiters.setText(liters != null && !liters.isEmpty() ? liters : "" );
            /* CO2 */
            aquaCo2.setText(co2 != null && !co2.isEmpty() ? co2 : "" );
            /* Dose */
            aquaDose.setText(dosage != null && !dosage.isEmpty() ? dosage : "" );
            /* Substrate */
            aquaSubstrate.setText(substrate != null && !substrate.isEmpty() ? substrate : "" );
            /* Note */
            aquaNote.setText(notes != null && !notes.isEmpty() ? notes : "" );
            /* Size */
            aquaSize.setText(size != null && !size.isEmpty() ? size : "" );
            /* Filter */
            aquaFilter.setText(filter != null && !filter.isEmpty() ? filter : "" );
            /* Light */
            aquaLight.setText(light != null && !light.isEmpty() ? light : "" );
            /* Status*/
            final int STATUS_WORKING = 0;
            final int STATUS_DISABLED = 1;
            aquaStatus.setSelection(status == STATUS_WORKING ? STATUS_WORKING : STATUS_DISABLED);
            /* Image */
            if (image != null){
                photoUri = Uri.parse(image);
                aquaImage.setImageURI(photoUri);
                aquaImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            /* Date */
            if (date != null) {

            }
            /* Type */
            final int FRESHWATER = 0;
            final int MARINE = 1;
            final int BRACKISH = 2;
            switch (type) {
                case FRESHWATER:
                    aquaType.setSelection(FRESHWATER);
                    break;
                case MARINE:
                    aquaType.setSelection(MARINE);
                    break;
                case BRACKISH:
                    aquaType.setSelection(BRACKISH);
                    break;
                default:
                    Toast.makeText(this, "Sorry, Illegal type selected", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onLoadFinished: setting type", new IllegalSelectorException());
           }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        aquaIdUri = null;
    }


    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(AquaNew.this.getSupportFragmentManager(), "datePicker");
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
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
    }

        public void onDateSet(DatePicker view, int year, int month, int day) {
           // aquaDate.setText(day + "/" + month + "/" + year);
            String datePickerResult = String.valueOf(day + "/" +  month + "/" + year);
            /* Specify which format is */
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
            try {
                /* Parse to date setup based user locale */
                date = format.parse(datePickerResult);
                String localeDate = getDateFormat(getContext()).format(date);
                aquaDate.setText(localeDate);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                aquaDateMilliseconds = calendar.getTimeInMillis();
                Log.d(TAG, "onDateSet: mili locale " + localeDate);
                Log.d(TAG, "onDateSet: mili " + aquaDateMilliseconds);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
