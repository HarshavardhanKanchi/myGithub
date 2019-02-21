package com.example.rgukt.myproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {
    EditText editTextUserName,editTextPassword,editTextMobile,editTextEmail;
    Spinner spinnerState,spinnerDistrict,spinnerMandal,spinnerPanchayat,spinnerVillage;
    Button buttonSignUp;

    ProgressDialog progressDialog;

    DatabaseReference userReference;

    FirebaseAuth firebaseAuth;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextUserName=(EditText)findViewById(R.id.editTextUsername);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword);
        editTextEmail=(EditText)findViewById(R.id.editTextEmail);
        editTextMobile=(EditText) findViewById(R.id.editTextMobile);

        spinnerState=(Spinner)findViewById(R.id.spinnerState);
        spinnerDistrict=(Spinner)findViewById(R.id.spinnerDistrict);
        spinnerMandal=(Spinner)findViewById(R.id.spinnerMandal);
        spinnerPanchayat=(Spinner)findViewById(R.id.spinnerPanchayat);
        spinnerVillage=(Spinner)findViewById(R.id.spinnerVillage);

        progressDialog=new ProgressDialog(this);

        toolbar=(Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sign Up");


        buttonSignUp=(Button)findViewById(R.id.buttonSignup);

        firebaseAuth=FirebaseAuth.getInstance();
        userReference= FirebaseDatabase.getInstance().getReference("User");


        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSignUp();
            }
        });



    }

    private void userSignUp() {

        final String uState=spinnerState.getSelectedItem().toString().trim();
        final String uDistrict=spinnerDistrict.getSelectedItem().toString().trim();
        final String uMandal=spinnerMandal.getSelectedItem().toString().trim();
        final String uPanchayat=spinnerPanchayat.getSelectedItem().toString().trim();
        final String uVillage=spinnerVillage.getSelectedItem().toString().trim();

        final String uName=editTextUserName.getText().toString().trim();
        final String uPassword=editTextPassword.getText().toString().trim();
        final String uEmail=editTextEmail.getText().toString().trim();
        final String uMobile=editTextMobile.getText().toString().trim();



        if(!TextUtils.isEmpty(uName) && !TextUtils.isEmpty(uPassword) && !TextUtils.isEmpty(uEmail) && !TextUtils.isEmpty(uMobile) && !TextUtils.isEmpty(uDistrict) && !TextUtils.isEmpty(uMandal) &&
                !TextUtils.isEmpty(uPanchayat) && !TextUtils.isEmpty(uVillage) &&
                !TextUtils.isEmpty(uState) ){
            progressDialog.setTitle(" SignUp");
            progressDialog.setMessage("Regestering...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            Query query=userReference.orderByChild("uEmail").equalTo(uEmail);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, "Email is already exist,Try new one", Toast.LENGTH_LONG).show();
                    }else{
                        String id=userReference.push().getKey();
                        User user=new User(id,uName,uEmail,uPassword,uMobile,uState,uDistrict,uMandal,uPanchayat,uVillage,"profile");
                        userReference.child(id).setValue(user);
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, "Registered Successfully", Toast.LENGTH_LONG).show();

                        Intent intent=new Intent(SignUpActivity.this,Loginactivity.class);
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

    public void onClickToLogin(View view) {
        startActivity(new Intent(SignUpActivity.this,Loginactivity.class));
    }
    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Do you want to quit?");

        final AlertDialog alertDialog=builder.create();
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
                Intent intent=new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        builder.show();

    }
}
