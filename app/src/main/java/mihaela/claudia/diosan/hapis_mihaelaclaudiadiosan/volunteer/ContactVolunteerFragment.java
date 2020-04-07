package mihaela.claudia.diosan.hapis_mihaelaclaudiadiosan.volunteer;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import mihaela.claudia.diosan.hapis_mihaelaclaudiadiosan.R;


public class ContactVolunteerFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_volunteer, container, false);

        MaterialButton volunteerContactBtn = view.findViewById(R.id.volunteer_contact_button);


        volunteerContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showToast();
            }
        });
        return view;
    }


    public void showToast(){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.contact_custom_toast,
                (ViewGroup) getActivity().findViewById(R.id.toast_layout_root));
        // get the reference of TextView and ImageVIew from inflated layout
        TextView toastTextView = (TextView) layout.findViewById(R.id.toastTextView);
        ImageView toastImageView = (ImageView) layout.findViewById(R.id.toastImageView);
        // set the text in the TextView
        toastTextView.setText(getString(R.string.contact_toast_message));
        // set the Image in the ImageView
        toastImageView.setImageResource(R.drawable.message_toast);
        // create a new Toast using context
        Toast toast = new Toast(getActivity());
        toast.setDuration(Toast.LENGTH_LONG); // set the duration for the Toast
        toast.setView(layout); // set the inflated layout
        toast.show(); // display the custom Toast
    }

}
