package com.voeacademy.application;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.voeacademy.application.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeMenuFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeMenuFragment newInstance(String param1, String param2) {
        HomeMenuFragment fragment = new HomeMenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    FirebaseFirestore voe_db_firestore;
    ImageView avatar;
    YouTubePlayerView youTubePlayerView;
    ProgressBar progressBar;
    CardView accountDetailCardView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        voe_db_firestore = FirebaseFirestore.getInstance();

        GoogleSignInAccount signedInAccount = GoogleSignIn.getLastSignedInAccount(getContext());


        View v = inflater.inflate(R.layout.fragment_home_menu, container, false);
        youTubePlayerView = v.findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        TextView usernameTextview = v.findViewById(R.id.username_textview);

        CardView mathematicsCardView = v.findViewById(R.id.mathematics_card_view);
        CardView scienceCardView = v.findViewById(R.id.science_card_view);
        CardView languageLearningCardView = v.findViewById(R.id.language_learning_card_view);
        CardView digitalLiteracyCardView = v.findViewById(R.id.digital_literacy_card_view);
        accountDetailCardView = v.findViewById(R.id.account_detail_card_view);
        progressBar = v.findViewById(R.id.progress_bar);


        avatar = v.findViewById(R.id.username_gender_img_view);

        checkValidUser(signedInAccount.getId(), usernameTextview);

        mathematicsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClassBookingActivity.class);

                String subject = classBook(mathematicsCardView);
                intent.putExtra("subject", subject);
                startActivity(intent);
            }
        });


        scienceCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClassBookingActivity.class);

                String subject = classBook(scienceCardView);
                intent.putExtra("subject", subject);
                startActivity(intent);
            }
        });

        languageLearningCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClassBookingActivity.class);

                String subject = classBook(languageLearningCardView);
                intent.putExtra("subject", subject);
                startActivity(intent);
            }
        });

        digitalLiteracyCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClassBookingActivity.class);

                String subject = classBook(digitalLiteracyCardView);
                intent.putExtra("subject", subject);
                startActivity(intent);
            }
        });
        getAnnouncementAndUpdate();

        return v;
    }

    public String classBook(View view) {

        String sub = (String) view.getTag();

        return sub;
    }

    public void getAnnouncementAndUpdate() {

        CollectionReference announcementAndUpdates = voe_db_firestore.collection("announcementAndUpdates");

        announcementAndUpdates.orderBy("timestamp", Query.Direction.DESCENDING).limit(1).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                System.out.println("video_id" + document.get("video_id").toString());
                                youtubeVideoPlayerLoad(document.get("video_id").toString());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        ;
    }

    private void youtubeVideoPlayerLoad(String videoId) {
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
//                String videoId = "rDuHoZpnN_0";
                youTubePlayer.cueVideo(videoId, 0);

            }
        });
    }


    public void checkValidUser(String uniqueUserId, TextView userNameTextView) {


        DocumentReference docRef = voe_db_firestore.collection("users").document(uniqueUserId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
//            code here for fetching data
                        progressBar.setVisibility(View.GONE);
                        accountDetailCardView.setVisibility(View.VISIBLE);


                        userNameTextView.setText(document.getData().get("name").toString());
                        String gender = document.getData().get("gender").toString();
                        if (gender.equals("Male") || gender.equals("male")) {
                            avatar.setImageResource(R.drawable.male_avatar);
                        } else {
                            avatar.setImageResource(R.drawable.female_avatar);
                        }

                    } else {
                        FirebaseAuth.getInstance().signOut();
                        Intent i = new Intent(getContext(), MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);


                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }
}