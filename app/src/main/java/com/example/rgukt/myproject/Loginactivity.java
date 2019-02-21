package com.example.rgukt.myproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Loginactivity extends AppCompatActivity {
    EditText editTextEmail, editTextPassword;
    Spinner spinnerUsers;
    TextView textViewSignUp;
    FloatingActionButton floatingActionButtonLogin;


    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    private ProgressDialog progressDialog;
    String pId,uId;

    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);

        MobileAds.initialize(Loginactivity.this,"ca-app-pub-8744173306474720~7476113655");
        adView=findViewById(R.id.adView);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        textViewSignUp=(TextView)findViewById(R.id.textViewSignup);
        spinnerUsers=(Spinner)findViewById(R.id.spinnerUsers);
        floatingActionButtonLogin=(FloatingActionButton)findViewById(R.id.floating_login);

        firebaseAuth = FirebaseAuth.getInstance();
       // toolbar=(Toolbar)findViewById(R.id.my_toolbar);
       // setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("SolVillage");




        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Loginactivity.this,SignUpActivity.class));
            }
        });

        progressDialog=new ProgressDialog(this);

        floatingActionButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });


        adView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }

    public void userLogin() {


        final String uemail = editTextEmail.getText().toString().trim();
        final String upassword = editTextPassword.getText().toString().trim();
        final String userType=spinnerUsers.getSelectedItem().toString().trim();
        final int typeId=spinnerUsers.getSelectedItemPosition();

        progressDialog.setMessage("Logging in please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        if(!TextUtils.isEmpty(uemail) && !TextUtils.isEmpty(upassword)) {
            progressDialog.show();
            switch (typeId) {
                case 0:
                    databaseReference = FirebaseDatabase.getInstance().getReference("President");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                int flag = 0;
                                for (DataSnapshot presidentSnapshot : dataSnapshot.getChildren()) {
                                    final President president = presidentSnapshot.getValue(President.class);
                                    if (president.getpEmail().equals(uemail) && president.getpPassword().equals(upassword)) {
                                        flag = 1;
                                        pId = president.getpId();
                                    }
                                }
                                if (flag == 0) {
                                    progressDialog.dismiss();
                                    Toast.makeText(Loginactivity.this, "Incorrect details", Toast.LENGTH_SHORT).show();
                                } else {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(getApplicationContext(), PresidentActivity.class);
                                    intent.putExtra("pId", pId);
                                    startActivity(intent);

                                    Toast.makeText(getApplicationContext(), "You are logged in Successfully :)", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Loginactivity.this, "User not found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                /*firebaseAuth.signInWithEmailAndPassword(uemail, upassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            startActivity(new Intent(Loginactivity.this, PresidentActivity.class));
                            Toast.makeText(Loginactivity.this, "You are loggedin successfully", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(Loginactivity.this, "Incorrect email/Password ", Toast.LENGTH_SHORT).show();
                        }

                    }
                });*/
                    break;
                case 1:
                    databaseReference = FirebaseDatabase.getInstance().getReference("User");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                int flag = 0;
                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                    User user = userSnapshot.getValue(User.class);
                                    if (user.getuEmail().equals(uemail) && user.getuPassword().equals(upassword)) {
                                        flag = 1;
                                        uId = user.getuId();
                                    }
                                }
                                if (flag == 0) {
                                    progressDialog.dismiss();
                                    Toast.makeText(Loginactivity.this, "Incorrect details", Toast.LENGTH_SHORT).show();
                                } else {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                                    intent.putExtra("uId", uId);
                                    startActivity(intent);

                                    Toast.makeText(getApplicationContext(), "You are logged in Successfully :)", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Loginactivity.this, "User not found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
               /* firebaseAuth.signInWithEmailAndPassword(uemail, upassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();

                            Intent intent=new Intent(Loginactivity.this,UserActivity.class);
                            intent.putExtra("uEmail",firebaseAuth.getCurrentUser());
                            startActivity(intent);
                            Toast.makeText(Loginactivity.this, "You are loggedin successfully", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(Loginactivity.this, "Incorrect email/Password ", Toast.LENGTH_SHORT).show();
                        }

                    }
                });*/
                    break;

                default:
                    databaseReference = FirebaseDatabase.getInstance().getReference("Admin");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                int flag = 0;
                                for (DataSnapshot adminSnapshot : dataSnapshot.getChildren()) {
                                    Admin admin = adminSnapshot.getValue(Admin.class);
                                    if (admin.getaEmail().equals(uemail) && admin.getaPassword().equals(upassword)) {
                                        flag = 1;
                                    }
                                }
                                if (flag == 0) {
                                    progressDialog.dismiss();
                                    Toast.makeText(Loginactivity.this, "Incorrect details", Toast.LENGTH_SHORT).show();
                                } else {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "You are logged in Successfully :)", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Loginactivity.this, "User not found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
               /* firebaseAuth.signInWithEmailAndPassword(uemail, upassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Intent intent = new Intent(Loginactivity.this, AdminActivity.class);
                            startActivity(intent);
                            Toast.makeText(Loginactivity.this, "You are logged in successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Loginactivity.this, "Incorrect email/Password", Toast.LENGTH_SHORT).show();
                        }
                    }


                });*/
                    break;
            }
        }else{
            Toast.makeText(this, "You should enter details to login", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
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
