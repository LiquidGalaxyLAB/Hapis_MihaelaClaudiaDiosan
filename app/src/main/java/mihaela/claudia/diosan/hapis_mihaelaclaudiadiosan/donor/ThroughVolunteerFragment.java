package mihaela.claudia.diosan.hapis_mihaelaclaudiadiosan.donor;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mihaela.claudia.diosan.hapis_mihaelaclaudiadiosan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThroughVolunteerFragment extends Fragment implements View.OnClickListener {


    private View view;
    private PlacesClient placesClient;
    private List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
    private AutocompleteSupportFragment autocompleteSupportFragment;


    private TextView selectedDateDonor;
    private TextView selectedTimeDonor;
    private TextView locationDonor;

    private MaterialButton datePickerBtn;
    private MaterialButton timePickerBtn;
    private MaterialButton confirmBtn;

    /*Firebase*/
    private StorageReference storageReference;
    private FirebaseUser user;
    private FirebaseFirestore mFirestore;

    private Map<String,String> donor = new HashMap<>();




    private DatePickerDialog.OnDateSetListener setListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_through_volunteer, container, false);

        initViews(view);
        firebaseInit();

        initPlaces();
        setupPlaceAutoComplete();
        setTextDateListener();

        return view;
    }

    private void initViews(View view) {
        locationDonor = view.findViewById(R.id.selected_location_donor);
        datePickerBtn = view.findViewById(R.id.date_picker_donor);
        timePickerBtn = view.findViewById(R.id.time_picker_donor);

        selectedDateDonor = view.findViewById(R.id.selected_date_donor);
        selectedTimeDonor = view.findViewById(R.id.selected_time_donor);

        confirmBtn = view.findViewById(R.id.donor_confirm_button);
    }

    private void firebaseInit(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mFirestore = FirebaseFirestore.getInstance();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        datePickerBtn.setOnClickListener(this);
        timePickerBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.date_picker_donor:
                selectDate();
                break;
            case R.id.time_picker_donor:
                selectTime();
                break;
            case R.id.donor_confirm_button:
                if (isValidForm()){
                    uploadDataToFirebase();
                    showSuccessToast(getString(R.string.fr_tv_confirm_toast));
                    startActivity(new Intent(getContext(), HomeDonor.class));
                }
                break;
        }

    }

    private void uploadDataToFirebase(){

        donor.put("donationLocation", locationDonor.getText().toString());
        donor.put("donationHour", selectedTimeDonor.getText().toString());
        donor.put("donationDate", selectedDateDonor.getText().toString());

        mFirestore.collection("donors").document(user.getEmail()).set(donor, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //  successfullyUploadedInfoToast();
                    }
                }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String error = e.getMessage();
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private boolean isValidForm(){
        if (selectedDateDonor.getText().toString().equals(getString(R.string.fr_tv_date))){
            showErrorToast( getString(R.string.date_error_toast));
            return false;
        }else if (selectedTimeDonor.getText().toString().equals(getString(R.string.fr_tv_hour))){
            showErrorToast(getString(R.string.time_error_toast));
            return false;
        }else if (locationDonor.getText().toString().equals(getString(R.string.fr_tv_location))){
            showErrorToast( getString(R.string.location_error_toast));
            return false;
        }
        return true;
    }

    public void showErrorToast(String message){
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
        View view =toast.getView();
        view.setBackgroundColor(Color.WHITE);
        TextView toastMessage =  toast.getView().findViewById(android.R.id.message);
        toastMessage.setTextColor(Color.RED);
        toastMessage.setGravity(Gravity.CENTER);
        toastMessage.setTextSize(15);
        toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.error_drawable, 0,0,0);
        toastMessage.setPadding(10,10,10,10);
        toast.show();
    }

    public void showSuccessToast(String message){
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
        View view =toast.getView();
        view.setBackgroundColor(Color.WHITE);
        TextView toastMessage =  toast.getView().findViewById(android.R.id.message);
        toastMessage.setTextColor(Color.GREEN);
        toastMessage.setGravity(Gravity.CENTER);
        toastMessage.setTextSize(15);
        toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_black_24dp, 0,0,0);
        toastMessage.setPadding(10,10,10,10);
        toast.show();
    }

    private void initPlaces() {
        Places.initialize(view.getContext(), getString(R.string.API_KEY));
        placesClient = Places.createClient(view.getContext());
    }

    private void setupPlaceAutoComplete(){
        autocompleteSupportFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_donor);
        assert autocompleteSupportFragment != null;
        autocompleteSupportFragment.setPlaceFields(placeFields);
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull final Place place) {

                if (place.getLatLng() != null) {

                    locationDonor.setText(place.getAddress());
                }

            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(view.getContext(), ""+status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectDate(){
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), setListener, year, month, day);
      //  Objects.requireNonNull(datePickerDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();

    }


    private void setTextDateListener(){
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                selectedDateDonor.setText(date);
            }
        };
    }

    private void selectTime(){
       final Calendar calendar = Calendar.getInstance();
       final int hour = calendar.get(Calendar.HOUR_OF_DAY);
       final int minute = calendar.get(Calendar.MINUTE);

       TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
           @Override
           public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
               String time = hourOfDay + ":" + minute + "h";
               selectedTimeDonor.setText(time);
           }
       }, hour, minute, false);
        timePickerDialog.show();
    }


}
