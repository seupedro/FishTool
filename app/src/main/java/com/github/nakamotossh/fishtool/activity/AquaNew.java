package com.github.nakamotossh.fishtool.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.graphics.drawable.DrawableCompat;
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

import com.github.nakamotossh.fishtool.R;
import com.github.nakamotossh.fishtool.database.AquaDbHelper;
import com.myhexaville.smartimagepicker.ImagePicker;
import com.myhexaville.smartimagepicker.OnImagePickedListener;

import java.nio.channels.IllegalSelectorException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.github.nakamotossh.fishtool.database.AquaContract.AquaEntry.AQUA_CONTENT_URI;
import static com.github.nakamotossh.fishtool.database.AquaContract.AquaEntry.CO2_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.AquaEntry.DOSAGE_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.AquaEntry.FILTER_COLUMN;
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

    private static final String TAG = "AquaNew";
    private final int LOADER_ID = 0;
    private Uri aquaIdUri;

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
    private ImagePicker imagePicker;
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

        Log.d(TAG, "onCreate: getExtra " + getIntent().getExtras());
        Log.d(TAG, "onCreate: getStringExtra " + getIntent().getExtras().getString("aquaId"));

        /* Define Title */
        setTitle("New Aquarium");

        /* Check if intent has Data */
        if (getIntent().hasExtra("aquaId")){
            /* Define Title */
            setTitle("Edit Aquarium");
            /* Parse Uri */
            aquaIdUri = Uri.parse(getIntent()
                    .getExtras()
                    .getString("aquaId"));
            Log.d(TAG, "onCreate: aquaIdUri " + aquaIdUri);
            /* Start Loader */
            getLoaderManager().initLoader(LOADER_ID, null, this);
        }

        /* TODO: Fix menu button color compatibility */
        Drawable icon;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            icon = getDrawable(R.drawable.ic_check);
            DrawableCompat.setTint(icon, Color.WHITE);
        }

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
        fab = findViewById(R.id.fab_frag);
        getDateButton = findViewById(R.id.date_button);

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

        /* Set listener on fab */
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Image Picker */
//                pickImage();
                /* Insert Dummy Data */
                displayDummyData();
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

    private void pickImage() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_aqua, menu);

        /* TODO: Display delete menu option dinamically */
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_button:
                checkAndSaveValues();
                return true;
            case R.id.delete_button:

                /* TODO: DIALOG - Ask user if has sure */

                /* TODO: Delete is not deleting */

                /* Delete Data and Notify */
                final int NOTHING_DELETED = 0;
                int deleteUri = getContentResolver().delete(aquaIdUri, null, null);
                if (deleteUri > NOTHING_DELETED){
                    getContentResolver().notifyChange(aquaIdUri, null);
                    Toast.makeText(this, "Deleted" + deleteUri, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
                }

                /* Close this activity */
                startActivity(new Intent(this, AquaMain.class));
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
        aquaDate.setText("26/09/1994");
        aquaNote.setText("Leticia doida");
        aquaSize.setText("100x26x50");
        aquaFilter.setText("Hang on");
    }

    private void checkAndSaveValues() {

        /* TODO: Check values before insert */

        ContentValues values = new ContentValues();
        values.put(NAME_COLUMN, aquaName.getText().toString().trim());
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

        /* TODO: Handle low storage with try/catch */
        /* Insert Data and Notify */
        Uri insertUri = getContentResolver().insert(AQUA_CONTENT_URI, values);
        if (insertUri != null){
            getContentResolver().notifyChange(insertUri, null);
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Save failed", Toast.LENGTH_SHORT).show();
        }
        finish();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.handleActivityResult(resultCode, requestCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.handlePermission(requestCode, grantResults);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(AquaNew.this.getSupportFragmentManager(), "datePicker");
        Log.d(TAG, "showDatePickerDialog: ");
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


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: aquaIdUri" + aquaIdUri);
        return new CursorLoader(this,
                Uri.withAppendedPath(AQUA_CONTENT_URI, String.valueOf(3)),
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {

//        Log.d(TAG, "columns: " + c.getColumnCount());
        Log.d(TAG, "position : " + c.getPosition() );
//
//        Log.d(TAG, "move: " + c.moveToNext() );
//        Log.d(TAG, "position: " + c.getPosition() );
//        Log.d(TAG, "name : " + c.getString(c.getColumnIndexOrThrow(NAME_COLUMN)) );

        if (c.moveToNext()){
            Log.d(TAG, "position : " + c.getPosition() );

            /* Get All Fields */
            String name = c.getString(c.getColumnIndexOrThrow(NAME_COLUMN));
            String liters = c.getString(c.getColumnIndexOrThrow(LITERS_COLUMN));
            //String date = c.getString(c.getColumnIndexOrThrow(DATE_AQUA_COLUMN));
            int type = c.getInt(c.getColumnIndexOrThrow(TYPE_COLUMN));
            int status = c.getInt(c.getColumnIndexOrThrow(STATUS_COLUMN));
            String co2 = c.getString(c.getColumnIndexOrThrow(CO2_COLUMN));
            String dosage = c.getString(c.getColumnIndexOrThrow(DOSAGE_COLUMN));
            String substrate = c.getString(c.getColumnIndexOrThrow(SUBSTRATE_COLUMN));
            String notes = c.getString(c.getColumnIndexOrThrow(NOTES_COLUMN));
            //String image = c.getString(c.getColumnIndexOrThrow(IMAGE_URI_COLUMN));
            String size = c.getString(c.getColumnIndexOrThrow(SIZE_COLUMN));
            String filter = c.getString(c.getColumnIndexOrThrow(FILTER_COLUMN));

            /* Name */
            aquaName.setText(name != null && !name.isEmpty() ? name : "" );
            /* Status*/
            final int STATUS_WORKING = 0;
            final int STATUS_DISABLED = 1;
            aquaStatus.setSelection(status == STATUS_WORKING ? STATUS_WORKING : STATUS_DISABLED);
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
                    Log.e(TAG, "onLoadFinished: setting type", new IllegalSelectorException());
            }

            aquaName.setText(name != null && !name.isEmpty() ? name : "" );
            aquaName.setText(name != null && !name.isEmpty() ? name : "" );
            aquaName.setText(name != null && !name.isEmpty() ? name : "" );
            aquaName.setText(name != null && !name.isEmpty() ? name : "" );
            aquaName.setText(name != null && !name.isEmpty() ? name : "" );
            aquaName.setText(name != null && !name.isEmpty() ? name : "" );
            aquaName.setText(name != null && !name.isEmpty() ? name : "" );
            aquaName.setText(name != null && !name.isEmpty() ? name : "" );

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        aquaIdUri = null;

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
