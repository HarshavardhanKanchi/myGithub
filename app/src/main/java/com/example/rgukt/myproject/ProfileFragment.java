package com.example.rgukt.myproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ProfileFragment extends android.support.v4.app.Fragment {
    Button buttonLogout;
    //FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_profile, container, false);
        buttonLogout=(Button)view.findViewById(R.id.buttonLogout);

       // firebaseAuth=FirebaseAuth.getInstance();


        databaseReference= FirebaseDatabase.getInstance().getReference("President");
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               // firebaseAuth.signOut();
               Intent intent=new Intent(getActivity().getApplicationContext(),MainActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(intent);


            }
        });
        return view;
    }

}
