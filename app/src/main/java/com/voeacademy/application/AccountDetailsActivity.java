package com.voeacademy.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.voeacademy.application.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class AccountDetailsActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    TextView email_address_text, name_text;
    EditText mobile_text;
    MaterialAutoCompleteTextView gender_text, class_text;


    ProgressBar progressBar;

    Button verify_btn;
    FirebaseDatabase voe_db;
    FirebaseFirestore voe_db_firestore;
    String userPhoneNumber, current_class, user_gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

        email_address_text = findViewById(R.id.email_address_text);
        name_text = findViewById(R.id.name_text);
        mobile_text = findViewById(R.id.mobile_text);
        gender_text = findViewById(R.id.gender_text);
        class_text = findViewById(R.id.class_text);
        progressBar = findViewById(R.id.progress_bar);

        verify_btn = findViewById(R.id.verify_btn);
        mAuth = FirebaseAuth.getInstance();

        email_address_text.setText(acct.getEmail());
        name_text.setText(acct.getDisplayName());

        voe_db_firestore = FirebaseFirestore.getInstance();


        String[] gender = new String[]{"Male", "Female"};
        gender_text.setSimpleItems(gender);

        String[] classes = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
        class_text.setSimpleItems(classes);

        verify_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                userPhoneNumber = mobile_text.getText().toString().trim();
                current_class = class_text.getText().toString().trim();
                user_gender = gender_text.getText().toString().trim();


//                Checks if the message has anything.
                if (userPhoneNumber.length() != 10) {
                    mobile_text.setError("Invalid Number");
                    verify_btn.setError("");

                    return;
                }
                if (user_gender.equals("")) {
                    gender_text.setError("Select Gender");
                    verify_btn.setError("");

                    return;
                }

                if (current_class.equals("")) {
                    class_text.setError("Select Class");
                    verify_btn.setError("");

                    return;

                }
                if (name_text.getText().toString().trim().equals("")) {
                    name_text.setError("Enter Valid Name");
                    verify_btn.setError("");

                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                verify_btn.setVisibility(View.GONE);

                sendVerificationOTP();
            }
        });
    }

    public void sendVerificationOTP() {
//        Toast.makeText(this, "Verify Clicked", Toast.LENGTH_SHORT).show();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + mobile_text.getText().toString())       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(AccountDetailsActivity.this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                Toast.makeText(AccountDetailsActivity.this, "OTP Verified Successfully", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(AccountDetailsActivity.this, "Wrong OTP!" + e, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                Toast.makeText(AccountDetailsActivity.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                                Intent go_to_otp_verification_activity = new Intent(getApplicationContext(), OTPVerificationActivity.class);
                                go_to_otp_verification_activity.putExtra("backend_otp", s);
                                go_to_otp_verification_activity.putExtra("name", name_text.getText().toString());
                                go_to_otp_verification_activity.putExtra("mobile_no", mobile_text.getText().toString());
                                go_to_otp_verification_activity.putExtra("gender", gender_text.getText().toString());
                                go_to_otp_verification_activity.putExtra("current_class", class_text.getText().toString());

                                startActivity(go_to_otp_verification_activity);
                                progressBar.setVisibility(View.GONE);
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


}