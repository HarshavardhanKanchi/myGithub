package com.example.rgukt.myproject;

import android.app.Application;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    //DatabaseReference databaseReference;
   // private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent=new Intent(MainActivity.this,Loginactivity.class);
        startActivity(intent);

       /* databaseReference= FirebaseDatabase.getInstance().getReference("Admin");
        String id=databaseReference.push().getKey();


        Admin admin=new Admin(id,"admin","admin","admin@gmail.com","555555");

        databaseReference.child(id).setValue(admin);*/

    }


}
