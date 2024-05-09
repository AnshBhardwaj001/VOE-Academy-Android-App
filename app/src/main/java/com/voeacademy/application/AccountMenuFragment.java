package com.voeacademy.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.voeacademy.application.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountMenuFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public AccountMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountMenuFragment newInstance(String param1, String param2) {
        AccountMenuFragment fragment = new AccountMenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    TextView username;
    TextInputEditText current_class,email,mobile_no;
    ImageView avatar;
    FirebaseFirestore voe_db_firestore;
    GoogleSignInAccount signedInAccount;
    DocumentReference docRef;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        voe_db_firestore=FirebaseFirestore.getInstance();
        signedInAccount= GoogleSignIn.getLastSignedInAccount(getActivity().getApplicationContext());
        docRef = voe_db_firestore.collection("users").document(signedInAccount.getId());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                username.setText(document.getData().get("name").toString());
                current_class.setText(document.getData().get("current_class").toString());
                email.setText(document.getData().get("email").toString());
                mobile_no.setText(document.getData().get("mobile_no").toString());

                String gender = document.getData().get("gender").toString();
                if(gender.equals("Male")||gender.equals("male")){
                    avatar.setImageResource(R.drawable.male_avatar);
                }else {
                    avatar.setImageResource(R.drawable.female_avatar);
                }
            }
        });



        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_account_menu, container, false);
        Button logout;
        username = view.findViewById(R.id.username_textview);
        current_class = view.findViewById(R.id.current_class_text);
        email = view.findViewById(R.id.email_address_text);
        mobile_no = view.findViewById(R.id.user_phone_number_text);
        avatar = view.findViewById(R.id.username_gender_img_view);
        logout = view.findViewById(R.id.logoutButton);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "Log out", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                Intent main_activity_intent = new Intent(getContext(), MainActivity.class);
                main_activity_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(main_activity_intent);


            }
        });

        return view;
    }
}