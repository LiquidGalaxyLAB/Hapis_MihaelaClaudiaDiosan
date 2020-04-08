package mihaela.claudia.diosan.hapis_mihaelaclaudiadiosan.donor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.material.button.MaterialButton;

import mihaela.claudia.diosan.hapis_mihaelaclaudiadiosan.R;
import mihaela.claudia.diosan.hapis_mihaelaclaudiadiosan.volunteer.PaymentActivity;

public class DonateDonorFragment extends Fragment {

    View view;
    VideoView vid;
    MediaController mediaController;
    MaterialButton donateBtn;


    public DonateDonorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_donate_donor, container, false);

        donateBtn = view.findViewById(R.id.donor_donate_button);

        vid = (VideoView) view.findViewById(R.id.videoViewDonor);
        String videoPath = "android.resource://" + view.getContext().getPackageName() + "/" + R.raw.homeless_video;
        Uri uri = Uri.parse(videoPath);
        vid.setVideoURI(uri);

        mediaController = new MediaController(view.getContext());
        vid.setMediaController(mediaController);
        mediaController.setAnchorView(vid);

        donateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent paymentIntent = new Intent(getActivity(), PaymentActivity.class);
                startActivity(paymentIntent);
            }
        });

        return view;
    }
}