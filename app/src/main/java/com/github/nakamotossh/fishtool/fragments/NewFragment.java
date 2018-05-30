package com.github.nakamotossh.fishtool.fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.github.nakamotossh.fishtool.activity.AquaNew.getContextApp;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewFragment extends Fragment {

    private static final String TAG = "NewFragment";

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

    public static boolean onChanged = false;
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            onChanged = true;
            return false;
        }
    };

    public NewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_new, container, false);
        
        /* Find views on layout */
        aquaImage = rootView.findViewById(R.id.aqua_photo);
        aquaName = rootView.findViewById(R.id.aqua_name);
        aquaSize = rootView.findViewById(R.id.aqua_size);
        aquaType = rootView.findViewById(R.id.aqua_type);
        aquaStatus = rootView.findViewById(R.id.aqua_status);
        aquaLiters = rootView.findViewById(R.id.aqua_liters);
        aquaLight = rootView.findViewById(R.id.aqua_light);
        aquaCo2 = rootView.findViewById(R.id.aqua_co2);
        aquaSubstrate = rootView.findViewById(R.id.aqua_substrate);
        aquaDose = rootView.findViewById(R.id.aqua_dose);
        aquaDate = rootView.findViewById(R.id.aqua_date);
        aquaNote = rootView.findViewById(R.id.aqua_note);
        aquaFilter = rootView.findViewById(R.id.aqua_filter);
        fab = rootView.findViewById(R.id.fab_frag);
        getDateButton = rootView.findViewById(R.id.date_button);

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

        /* Set listener on fab */
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Image Picker! */
                imagePicker = new ImagePicker(getActivity(), null, new OnImagePickedListener() {
                    @Override
                    public void onImagePicked(Uri imageUri) {
                        aquaImage.setImageURI(imageUri);
                        photoUri = imageUri;
                    }
                }).setWithImageCrop(1,1);

                /* Ask where get images from */
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.new_aqua, menu);
        //super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.save) {
            Log.d(TAG, "clicked");
            Toast.makeText(getContext(), "context", Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), "activity", Toast.LENGTH_SHORT).show();
            //Toast.makeText(AquaNew.class, "AquaNew", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "not");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        Toast.makeText(getActivity(), "getActivity()", Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "getContext()", Toast.LENGTH_SHORT).show();
        Toast.makeText(getContextApp(), "getContextApp()", Toast.LENGTH_SHORT).show();
        super.onStart();
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
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
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
