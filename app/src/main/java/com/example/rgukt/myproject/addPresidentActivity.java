package com.example.rgukt.myproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class addPresidentActivity extends AppCompatActivity {

    Toolbar toolbar;

    EditText editTextUserName,editTextPassword,editTextMobile,editTextEmail;
    Spinner spinnerState,spinnerDistrict,spinnerMandal,spinnerPanchayat,spinnerVillage;
    Button buttonAdd;

    DatabaseReference databasePresident;
    SharedPreferences sp;

    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_president);

        editTextUserName=(EditText)findViewById(R.id.editTextUsername);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword);
        editTextEmail=(EditText)findViewById(R.id.editTextEmail);
        editTextMobile=(EditText) findViewById(R.id.editTextMobile);

        spinnerState=(Spinner)findViewById(R.id.spinnerState);
        spinnerDistrict=(Spinner)findViewById(R.id.spinnerDistrict);
        spinnerMandal=(Spinner)findViewById(R.id.spinnerMandal);
        spinnerPanchayat=(Spinner)findViewById(R.id.spinnerPanchayat);
        spinnerVillage=(Spinner)findViewById(R.id.spinnerVillage);

        toolbar=(Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add President");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        buttonAdd=(Button)findViewById(R.id.buttonAdd);


        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPresident();
            }
        });

    }

    private void addPresident() {

        final String pState=spinnerState.getSelectedItem().toString().trim();
        final String pDistrict=spinnerDistrict.getSelectedItem().toString().trim();
        final String pMandal=spinnerMandal.getSelectedItem().toString().trim();
        final String pPanchayat=spinnerPanchayat.getSelectedItem().toString().trim();
        final String pVillage=spinnerVillage.getSelectedItem().toString().trim();

        final String pUname=editTextUserName.getText().toString().trim();
        final String pPassword=editTextPassword.getText().toString().trim();
        final String pEmail=editTextEmail.getText().toString().trim();
        final String pMobile=editTextMobile.getText().toString().trim();

        databasePresident= FirebaseDatabase.getInstance().getReference("President");

        final String id=databasePresident.push().getKey();

        if(!TextUtils.isEmpty(pUname) && !TextUtils.isEmpty(pPassword) && !TextUtils.isEmpty(pEmail) && !TextUtils.isEmpty(pMobile) && !TextUtils.isEmpty(pDistrict) && !TextUtils.isEmpty(pMandal) &&
                !TextUtils.isEmpty(pPanchayat) && !TextUtils.isEmpty(pVillage) &&
                !TextUtils.isEmpty(pState) ){

            query=databasePresident.orderByChild("pEmail").equalTo(pEmail);
            query.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        Toast.makeText(addPresidentActivity.this, "Email is already exist,Try new one", Toast.LENGTH_SHORT).show();
                    }else {

                        President president = new President(id, pState, pDistrict, pMandal, pPanchayat, pVillage, pUname, pPassword, pEmail, pMobile,"profile" +
                                "");

                        databasePresident.child(id).setValue(president);

                        Toast.makeText(addPresidentActivity.this, "President added successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(addPresidentActivity.this, addPresidentActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }




                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }else{
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
