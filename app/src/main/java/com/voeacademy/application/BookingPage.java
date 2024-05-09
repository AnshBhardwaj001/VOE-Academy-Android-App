package com.voeacademy.application;

import static android.content.ContentValues.TAG;
import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.voeacademy.application.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class BookingPage extends AppCompatActivity implements PaymentResultListener {

    String CHANNEL_ID = "100";
    FirebaseFirestore voe_db_firestore;
    GoogleSignInAccount signedInAccount;
    DocumentReference teacherRef , docRef;
    CollectionReference notification;
    PaymentNotification details;
    Button plus , minus , pay;
    ImageView teacherImage;
    Spinner selectClass, selectSubject;
    ProgressBar progressBar;
    int lectures , amount;
    TextView subject ,prizeMatrix, teacherDetails ,teacherName , teacherRating ,classes , ppc , totalPaymentCount;
    String userName , userMoblileNo , userEmail , userID;
    String no_of_classes , selectedClass , teachername , selectedSubject , samount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_page);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(BookingPage.this,Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(BookingPage.this,new String[]{Manifest.permission.POST_NOTIFICATIONS},101);
            }
        }


        voe_db_firestore=FirebaseFirestore.getInstance();
        signedInAccount= GoogleSignIn.getLastSignedInAccount(this);

        Intent i = getIntent();
        String sub = i.getStringExtra("subject");
        String teacherID = i.getStringExtra("teacher id");

        teacherName = findViewById(R.id.teacher_name);
        teacherRating = findViewById(R.id.teacher_rating);
        teacherImage = findViewById(R.id.teacher_profile_pic);
        selectClass=findViewById(R.id.select_class_dropdown);
        selectSubject=findViewById(R.id.select_subject_dropdown);
        progressBar=findViewById(R.id.progress_bar);
        prizeMatrix = findViewById(R.id.price_matrix);

        teacherRef = voe_db_firestore.collection("Teachers").document(teacherID);


        teacherDetails = findViewById(R.id.tecaher_profile_detail);
        teacherDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Teacher_Profile_Details.class);
                i.putExtra("subject",sub);
                i.putExtra("teacherID",teacherID);
                startActivity(i);
            }
        });


        prizeMatrix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // inflate the layout of the popup window
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.pop_layout, null);

                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });

            }
        });



        //Razorpay payment gateway integration
        Checkout.preload(getApplicationContext());
        pay = findViewById(R.id.pay_button);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BookingPage.this, "Processing...", Toast.LENGTH_SHORT).show();
                selectedClass = selectClass.getSelectedItem().toString();
                selectedSubject = selectSubject.getSelectedItem().toString();

                selectedClass = selectedClass.trim();
                selectedSubject = selectedSubject.trim();

                //Checks if the message has anything.
                if (selectedSubject.equals("Select Subject") || selectedClass.equals("Select Class") || lectures == 0)
                {
                    pay.setError("");
                    Toast.makeText(BookingPage.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                startPayment();
            }
        });


        teacherRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        progressBar.setVisibility(View.GONE);
                        teacherImage.setVisibility(View.VISIBLE);
                        Log.d(TAG,"DocumentSnapshot data: " + document.getData());
                        teachername = document.get("teacher_name").toString();
                        teacherName.setText(teachername);
                        teacherRating.setText(document.get("teacher_rating").toString());
                        Glide.with(getApplicationContext()).load(document.get("teacher_profile_url")).into(teacherImage);

//                        subject drop down menu
                        List<String> subjects=(List<String>)  document.get(sub);

                        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, subjects);
                        selectSubject.setAdapter(subjectAdapter);

//                        class drop down menu

                        List<String> classList= (List<String>)  document.get(sub+"-classes");
                        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, classList);
                        selectClass.setAdapter(classAdapter);


                    }else {
                        Log.d(TAG,"No such data");
                    }
                }else {
                    Log.d(TAG,"Get failed with ", task.getException());
                }
            }
        });

        subject = findViewById(R.id.subject);
        subject.setText(""+sub);

        plus = findViewById(R.id.plus);
        minus = findViewById(R.id.minus);

        classes = findViewById(R.id.no_of_classes);
        ppc = findViewById(R.id.price_per_class);
        totalPaymentCount=findViewById(R.id.total_payment_count);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lectures = Integer.parseInt(String.valueOf(classes.getText()));
                lectures++;
                classes.setText(String.valueOf(lectures));
//                pay.setText("Pay "+amount(lectures)+" Rs");
                totalPaymentCount.setText("₹ "+amount(lectures));
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lectures = Integer.parseInt(String.valueOf(classes.getText()));
                if(lectures <= 0){
                    Toast.makeText(BookingPage.this, "Book at least one class to continue", Toast.LENGTH_SHORT).show();
                }
                else {
                    lectures--;
                    classes.setText(String.valueOf(lectures));
//                    pay.setText("Pay "+amount(lectures)+" Rs");
                    totalPaymentCount.setText("₹ "+amount(lectures));
                }

            }
        });

    }

    private void startPayment() {

        DocumentReference docRef = voe_db_firestore.collection("users").document( signedInAccount.getId());

        final String[] name = new String[1];
        final String[] current_class = new String[1];
        final String[] email = new String[1];


        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                name[0] = document.get("name").toString();
                current_class[0] = document.get("current_class").toString();
                email[0] = document.get("email").toString();

            }
        });

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_live_NSVDvJ0sqbppvp");
        samount = String.valueOf(amount(lectures));
//        samount = String.valueOf("1");
        amount = Math.round(Float.parseFloat(samount)*100);


        JSONObject orderRequest = new JSONObject();
        try {
            orderRequest.put("amount",amount);
            orderRequest.put("currency","INR");
            orderRequest.put("receipt", "receipt#1");
            orderRequest.put("name","VOE Academy");
            orderRequest.put("description","Lecture payment");
            orderRequest.put("image","https://lh5.googleusercontent.com/ngi-JlJK22d7lBGiInI_XUm4fPQdieTj8ty45aqNUHHs_LgmJNMoN6UKVUlvYUkVFSsVok3aQLz0i2pAhRS7VM8=w16383");

            checkout.open(BookingPage.this,orderRequest);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }


    //Calculating Payable amount
    private int amount(int lectures) {
        int pay;
        if(lectures<10){
            pay = lectures*100;
            ppc.setText("100Rs/class");
            return pay;
        } else if (lectures<20) {
            pay = lectures*95;
            ppc.setText("95Rs/class");
            return pay;
        } else if (lectures<40) {
            pay = lectures*90;
            ppc.setText("90Rs/class");
            return pay;
        }
        else {
            pay = lectures*85;
            ppc.setText("85Rs/class");
            return pay;
        }
    }

    @Override
    public void onPaymentSuccess(String paymentID) {
        Toast.makeText(this, "Payment Successfull\nPayment ID : " + paymentID, Toast.LENGTH_SHORT).show();
        no_of_classes = String.valueOf(lectures);

        signedInAccount= GoogleSignIn.getLastSignedInAccount(this);
        docRef = voe_db_firestore.collection("users").document(signedInAccount.getId());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                userName = document.getData().get("name").toString();
                userEmail = document.getData().get("email").toString();
                userMoblileNo = document.getData().get("mobile_no").toString();
                userID = document.getId();


                details = new PaymentNotification(paymentID,userName , userMoblileNo , userEmail , userID ,teachername, no_of_classes ,samount,paymentID,selectedSubject,selectedClass, Timestamp.now() , false , false , "Class will be approved soon.\nIn case We will not approve the classes\nYou will get a refund within 24 hrs." , "Confirm Class");
                notification = voe_db_firestore.collection("ClassBooking");
                notification.add(details);

                System.out.println("details"+details);

                SendNotification(samount);
            }
        });
    }

    private void SendNotification(String samount) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("NotificationMenuFragment", "NotificationMenuFragment");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.voe_logo)
                .setContentTitle("Hooray!!!")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Your payment of Rs" + samount + " had been received. We will confirm your class soon."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
            if(notificationChannel == null){
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(CHANNEL_ID,getString(R.string.app_name),importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        notificationManager.notify(0, builder.build());
    }


    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
    }


}