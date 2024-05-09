package com.voeacademy.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.voeacademy.application.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OTPVerificationActivity extends AppCompatActivity {
    EditText otp_enter_text;
    Button otp_submit_btn;
    String backend_otp,name,mobile_no,gender,current_class;
    FirebaseDatabase voe_db;
    FirebaseFirestore voe_db_firestore;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);
        backend_otp = getIntent().getStringExtra("backend_otp");
        name = getIntent().getStringExtra("name");
        mobile_no = getIntent().getStringExtra("mobile_no");
        gender = getIntent().getStringExtra("gender");
        current_class = getIntent().getStringExtra("current_class");

        otp_enter_text = findViewById(R.id.otp_enter_text);
        otp_submit_btn = findViewById(R.id.otp_submit_btn);

        voe_db = FirebaseDatabase.getInstance("https://voeacademy-default-rtdb.asia-southeast1.firebasedatabase.app");
        voe_db_firestore= FirebaseFirestore.getInstance();
        progressBar=findViewById(R.id.progress_bar);

        otp_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                otp_submit_btn.setVisibility(View.GONE);
                verifyOTP();
            }
        });

    }

    public void verifyOTP(){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(backend_otp, otp_enter_text.getText().toString());

        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(OTPVerificationActivity.this, "OTP Verified Successfully", Toast.LENGTH_SHORT).show();
                    Intent go_to_home = new Intent(getApplicationContext(), HomeActivity.class);
                    go_to_home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    updateDatabase();
                    startActivity(go_to_home);
                    // Update UI
                } else {

                    Toast.makeText(OTPVerificationActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                    otp_submit_btn.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void updateDatabase(){
        GoogleSignInAccount signedInAccount= GoogleSignIn.getLastSignedInAccount(this);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String date_of_account_creation = sdf.format(new Date());

       NewUserDataHolder newUserDataHolder=new NewUserDataHolder(name,signedInAccount.getEmail(),gender,current_class,mobile_no,date_of_account_creation,true);

//       voe_db.getReference("voe_db/users/"+signedInAccount.getId()+"/account_detail").setValue(newUserDataHolder);
//        voe_db.getReference("voe_db/mobile_no/"+mobile_no).setValue(signedInAccount.getId());
        voe_db_firestore.collection("users").document(signedInAccount.getId()).set(newUserDataHolder);
    }


}