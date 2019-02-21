package com.example.rgukt.myproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class UsersFragment extends android.support.v4.app.Fragment {
    public UsersFragment() {
        // Required empty public constructor
    }
    private ProgressDialog progressDialog;
    ListView listViewPresident;
    DatabaseReference databasePresident;
    List<President> presidentList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        listViewPresident = (ListView) view.findViewById(R.id.listView_President);
        presidentList = new ArrayList<>();
        databasePresident = FirebaseDatabase.getInstance().getReference("President");



        FloatingActionButton fab = view.findViewById(R.id.adduser);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), addPresidentActivity.class));
            }
        });

        listViewPresident.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final President president=presidentList.get(position);
                final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());

                builder.setTitle("Remove President");
                builder.setMessage("Are you sure want to remove?");
                final AlertDialog alertDialog=builder.create();

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                            databasePresident = FirebaseDatabase.getInstance().getReference("President").child(president.getpId());
                            databasePresident.removeValue();
                            alertDialog.dismiss();
                        Toast.makeText(getContext(), "removed successfully", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                builder.setCancelable(false);
                builder.show();

                return false;
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        progressDialog=new ProgressDialog(getContext());

        progressDialog.setMessage("fetching data...");
        progressDialog.show();
        databasePresident.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                presidentList.clear();

                for (DataSnapshot presidentSnapshot : dataSnapshot.getChildren()) {
                    President president = presidentSnapshot.getValue(President.class);

                    presidentList.add(president);

                }
                progressDialog.dismiss();
                PresidentList adapter = new PresidentList((Activity) getContext(), presidentList);
                listViewPresident.setAdapter(adapter);


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
