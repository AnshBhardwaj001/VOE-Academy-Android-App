package com.voeacademy.application;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.voeacademy.application.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ClassBookingActivity extends AppCompatActivity {

    TextView subject;
    FirebaseFirestore voe_db_firestore;

    CollectionReference teachersRef;
    ProgressBar progressBar;

    private RecyclerView classTeacherRecyclerView;
    private ArrayList<SubjectTeacher> subjectTeacher;
    private SubjectTeacherRecyclerViewAdapter subjectTeacherRecyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_booking);

        voe_db_firestore = FirebaseFirestore.getInstance();

        teachersRef = voe_db_firestore.collection("Teachers");

        progressBar=findViewById(R.id.progress_bar);


        subject = findViewById(R.id.subject);
        Intent intent = getIntent();
        String sub = intent.getStringExtra("subject");
        subject.setText(sub);

        classTeacherRecyclerView = findViewById(R.id.class_teacher_recycler_view);
        classTeacherRecyclerView.setHasFixedSize(true);
        classTeacherRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        fetchSubjectTeacher(sub, classTeacherRecyclerView);


    }


    public void fetchSubjectTeacher(String subject, RecyclerView classTeacherRecyclerView) {

        teachersRef.whereArrayContains("category", subject).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            subjectTeacher = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                subjectTeacher.add(new SubjectTeacher(document.getId(), document.getData().get("teacher_profile_url").toString(), document.getData().get("teacher_name").toString(), document.getData().get("teacher_rating").toString(), (List<String>) document.get(subject)));

                            }

                            subjectTeacherRecyclerViewAdapter = new SubjectTeacherRecyclerViewAdapter(getApplicationContext(), subjectTeacher, subject);

                            classTeacherRecyclerView.setAdapter(subjectTeacherRecyclerViewAdapter);

                        } else {
                            Log.d(TAG, "Something Bad Happened...", task.getException());
                        }
                    }
                });
    }
}