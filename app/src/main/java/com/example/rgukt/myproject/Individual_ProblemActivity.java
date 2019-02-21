package com.example.rgukt.myproject;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelStore;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Individual_ProblemActivity extends AppCompatActivity {


    ImageButton imageButton;
    Spinner spinnerUpdateStatus;
    Button buttonUpdate;

    DatabaseReference databaseReference;
    StorageReference storageReference;
    Query query;

    Context context;

    String problem_desc;

    Toolbar toolbar;
    String probId;

    ProgressDialog progressDialog;
    private Uri imageUri=null ;



    private static  int GALLERY_REQUEST=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual__problem);

        imageButton=(ImageButton)findViewById(R.id.imageButtonAdd);
        spinnerUpdateStatus=(Spinner)findViewById(R.id.spinner_status_update);
        buttonUpdate=(Button)findViewById(R.id.button_update);
        progressDialog=new ProgressDialog(this);

        Bundle bundle=getIntent().getExtras();
        probId=bundle.getString("probId");




        storageReference= FirebaseStorage.getInstance().getReference();



        toolbar=(Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Update Problem Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);




        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateProblem();
            }
        });


    }

    private void updateProblem() {
        final String problemStatus=spinnerUpdateStatus.getSelectedItem().toString();

        if(!TextUtils.isEmpty(problemStatus) && imageButton!=null){
            progressDialog.setMessage( "updating please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();


            StorageReference filepath=storageReference.child("Images").child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    final Uri downloadUrl=taskSnapshot.getDownloadUrl();

                    final String image=downloadUrl.toString();

                    databaseReference=FirebaseDatabase.getInstance().getReference("Problem").child(probId);

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                                Problem problem = dataSnapshot.getValue(Problem.class);
                                if(problem!=null) {

                                    Problem problem1 = new Problem(probId, problem.getTitle(), problem.getDescription(), image, problem.getUsername(), problem.getVillage(), problemStatus, problem.getDate());
                                    databaseReference.setValue(problem1);
                                }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    progressDialog.dismiss();
                    Toast.makeText(Individual_ProblemActivity.this, "problem updated successfully", Toast.LENGTH_SHORT).show();



                }
            });

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode==RESULT_OK){
            imageUri=data.getData();
            imageButton.setImageURI(imageUri);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
