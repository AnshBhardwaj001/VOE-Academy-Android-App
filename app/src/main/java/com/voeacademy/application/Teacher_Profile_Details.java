package com.voeacademy.application;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.voeacademy.application.R;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;

public class Teacher_Profile_Details extends AppCompatActivity {

    ImageView teacherImage;
    TextView teacherName, teacherRating, teacherExperience;

    YouTubePlayerView youTubePlayerView;

    FirebaseFirestore voe_db_firestore;
    FlexboxLayout subjectFlexLayout;
    DocumentReference teacherRef;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile_details);

        voe_db_firestore = FirebaseFirestore.getInstance();

        Intent i = getIntent();
        String subject = i.getStringExtra("subject");
        String teacherID = i.getStringExtra("teacherID");

        teacherName = findViewById(R.id.nameText);
        teacherImage = findViewById(R.id.teacherImage);

        teacherExperience = findViewById(R.id.overview);
        teacherRating = findViewById(R.id.ratingText);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        progressBar=findViewById(R.id.progress_bar);


        teacherRef = voe_db_firestore.collection("Teachers").document(teacherID);

        subjectFlexLayout = findViewById(R.id.subjectFlexLayout);

        teacherRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        progressBar.setVisibility(View.GONE);
                        teacherImage.setVisibility(View.VISIBLE);
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        teacherName.setText(document.get("teacher_name").toString());
                        teacherRating.setText("Rating : " + document.get("teacher_rating").toString());
                        Glide.with(getApplicationContext()).load(document.get("teacher_profile_url")).into(teacherImage);

                        List<String> subjects = (List<String>) document.get("subjects");
                        addSubject(subjects);


                        teacherExperience.setText(document.get("overview").toString());
                        youtubeVideoPlayerLoad(document.get("youtube_video_id").toString());
                    } else {
                        Log.d(TAG, "No such data");
                    }
                } else {
                    Log.d(TAG, "Get failed with ", task.getException());
                }
            }
        });



    }

    private void youtubeVideoPlayerLoad(String videoId){
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
//                String videoId = "rDuHoZpnN_0";
                youTubePlayer.cueVideo(videoId, 0);

            }
        });
    }


    private void addSubject(  List<String> subjects) {

//        System.out.println(subjects);

        for (String subject : subjects){
            final View view = getLayoutInflater().inflate(R.layout.flex_box_item_layout, null);
        TextView subjectNameView = view.findViewById(R.id.flexbox_subject_item);
        subjectNameView.setText(subject);
        subjectFlexLayout.addView(view);
    }
    }
}