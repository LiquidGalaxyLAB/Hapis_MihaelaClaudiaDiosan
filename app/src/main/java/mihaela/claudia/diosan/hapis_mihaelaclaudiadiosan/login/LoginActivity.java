package mihaela.claudia.diosan.hapis_mihaelaclaudiadiosan.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import mihaela.claudia.diosan.hapis_mihaelaclaudiadiosan.MainActivity;
import mihaela.claudia.diosan.hapis_mihaelaclaudiadiosan.R;
import mihaela.claudia.diosan.hapis_mihaelaclaudiadiosan.donor.HomeDonor;
import mihaela.claudia.diosan.hapis_mihaelaclaudiadiosan.register.RegisterActivity;
import mihaela.claudia.diosan.hapis_mihaelaclaudiadiosan.volunteer.HomeVolunteer;

public class LoginActivity extends MainActivity implements View.OnClickListener{

    /*TextViews*/
    TextView forgotPassword;
    TextView signUp;
    TextView statistics;

    /*Buttons*/
    Button loginBtn;

    /*EditTexts*/
    TextInputEditText loginEmailEditText;
    TextInputEditText loginPasswordEditText;

    String loginEmailValue;
    String loginPasswordValue;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();


        makeFullscreenActivity();
        initViews();


        loginBtn.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        signUp.setOnClickListener(this);
        statistics.setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forgot_password_text_view:
                Intent forgotPassActivity = new Intent(LoginActivity.this, ForgotPasswordActivity.class );
                startActivity(forgotPassActivity);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.signup:
                Intent registerActivity = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerActivity);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.statistics_tv:
                Intent statisticsActivity = new Intent(LoginActivity.this, StatisticsActivity.class);
                startActivity(statisticsActivity);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.login_button:
                validateForm();
                login();

        }
    }


    private void makeFullscreenActivity() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
    }

    private void initViews() {
        forgotPassword = findViewById(R.id.forgot_password_text_view);
        signUp = findViewById(R.id.signup);
        statistics = findViewById(R.id.statistics_tv);
        loginEmailEditText = findViewById(R.id.login_email_edit_text);
        loginPasswordEditText = findViewById(R.id.login_password_edit_text);
        loginBtn = findViewById(R.id.login_button);
    }


    public void login(){
        loginEmailValue = loginEmailEditText.getText().toString();
        loginPasswordValue = loginPasswordEditText.getText().toString();

        mAuth.signInWithEmailAndPassword(loginEmailValue, loginPasswordValue)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            isDonor(loginEmailValue);
                            isVolunteer(loginEmailValue);

                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, getString(R.string.error_login),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void isDonor(String email){
        DocumentReference donorsDocument = mFirestore.collection("donors").document(email);
        donorsDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()){
                        Intent donorIntent = new Intent(LoginActivity.this, HomeDonor.class);
                        startActivity(donorIntent);
                    }
                }
            }
        });
    }


    private void isVolunteer(String email){
        DocumentReference volunteersDocument = mFirestore.collection("volunteers").document(email);
        volunteersDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()){
                        Intent volunteerIntent = new Intent(LoginActivity.this, HomeVolunteer.class);
                        startActivity(volunteerIntent);
                    }
                }
            }
        });
    }

    private void validateForm(){
        if (loginEmailEditText.getText().toString().isEmpty()){
            loginEmailEditText.setError(getString(R.string.email_error_text));
        }

        if (loginPasswordEditText.getText().toString().isEmpty()){
            loginPasswordEditText.setError(getString(R.string.password_error_text));
        }
    }


    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right );
    }

}
