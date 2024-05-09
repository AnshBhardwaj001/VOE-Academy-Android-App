package com.voeacademy.application;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Query;
import com.voeacademy.application.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationMenuFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FirebaseFirestore voe_db_firestore;
    CollectionReference notificationsRef;

    private RecyclerView notificationView;
//    private ArrayList<PaymentNotification> notifications;
    //adapter
    private NotificationsRecyclerViewAdapter notificationsRecyclerViewAdapter;
    GoogleSignInAccount signedInAccount;
    private String userID;
    Context context;

    public NotificationMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationMenuFragment newInstance(String param1, String param2) {
        NotificationMenuFragment fragment = new NotificationMenuFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notification_menu, container, false);
        context = container.getContext();

        voe_db_firestore = FirebaseFirestore.getInstance();
        signedInAccount= GoogleSignIn.getLastSignedInAccount(context);
        userID = signedInAccount.getId();
        notificationsRef = voe_db_firestore.collection("ClassBooking");

        notificationView = v.findViewById(R.id.not_recyclerView);
        notificationView.setLayoutManager(new LinearLayoutManager(context));
//        notificationView.setLayoutManager(new LinearLayoutManager(context));
        notificationView.setHasFixedSize(true);

        fetchnotifications(userID,v,notificationView);

        return v;
    }

    public void fetchnotifications(String userID, View v , RecyclerView notificationView) {

        ArrayList<PaymentNotification> notifications= new ArrayList<>();

        notificationsRef.orderBy("timeStamp", Query.Direction.DESCENDING).whereEqualTo("userID", userID).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                notifications.add(new PaymentNotification(document.getId(), (String) document.get("userName"),(String)document.get("userMoblileNo"),(String)document.get("userEmail"),(String)document.get("userID"),"Teacher Name : "+document.get("teacherName"),"No. of Classes : "+document.get("no_of_classes"),"Amount Paid : "+document.get("amount"),"Payment ID : "+document.get("paymentID"),"Subject : "+document.get("subject"),"Class : "+document.get("selectedClass"), (Timestamp) document.get("timeStamp") , (Boolean) document.get("check_read") , (Boolean)document.get("isClassesCompleted") , (String)document.get("classStatus") , (String)document.get("conform")));

                            }

                            notificationsRecyclerViewAdapter = new NotificationsRecyclerViewAdapter(context,notifications,userID);
                            notificationView.setAdapter(notificationsRecyclerViewAdapter);

                        } else {
                            Log.d(TAG, "Something Bad Happened...", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}