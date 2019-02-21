package com.example.rgukt.myproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

public class PostProblemActivity extends AppCompatActivity {
    private ImageButton imageButtonAdd;
    private EditText editTextProblemTitle,editTextProblemDesc;
    private Spinner spinnerStatus;
    private Button buttonSubmit;
    private Uri imageUri=null ;

    private ProgressDialog progressDialog;

    private StorageReference storageReference;
    private DatabaseReference problemReference;
    private DatabaseReference userReference;

    //FirebaseAuth firebaseAuth;
   // FirebaseUser currentUser;
   String uId,probId;

   Toolbar toolbar;

   Query query;




    private static  int GALLERY_REQUEST=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_problem);

        imageButtonAdd=(ImageButton)findViewById(R.id.imageButtonAdd);
        editTextProblemTitle=(EditText)findViewById(R.id.editTextProblemTitle);
        editTextProblemDesc=(EditText)findViewById(R.id.editTextProblemDesc);
        buttonSubmit=(Button)findViewById(R.id.buttonSubmit);
        spinnerStatus=(Spinner)findViewById(R.id.spinnerStatus);

        progressDialog=new ProgressDialog(this);



       // firebaseAuth=FirebaseAuth.getInstance();
        //currentUser=firebaseAuth.getCurrentUser();

        storageReference= FirebaseStorage.getInstance().getReference();
        problemReference= FirebaseDatabase.getInstance().getReference().child("Problem");
        userReference= FirebaseDatabase.getInstance().getReference().child("User");

        Bundle bundle=getIntent().getExtras();
        uId=bundle.getString("uId");

        toolbar=(Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Post Your Problem");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        imageButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postProblem();
            }
        });

    }


    private void postProblem() {


        final String title=editTextProblemTitle.getText().toString().trim();
        final String desc=editTextProblemDesc.getText().toString().trim();
        final String status=spinnerStatus.getSelectedItem().toString().trim();


        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(desc) && imageButtonAdd !=null){


            progressDialog.setMessage("Problem is posting please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            StorageReference filepath= storageReference.child("Images").child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    final Uri downloadUrl=taskSnapshot.getDownloadUrl();


                    probId= problemReference.push().getKey();

                    final String image=downloadUrl.toString();



                    userReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for(DataSnapshot userSnapshot:dataSnapshot.getChildren()){
                                User user=userSnapshot.getValue(User.class);
                                if(user.getuId().equals(uId)){
                                   final String username= user.getuName();
                                   final String village =user.getuVillage();

                                    final Calendar today=Calendar.getInstance();
                                    today.set(Calendar.HOUR_OF_DAY,0);
                                    final String date=today.getTime().toString();
                                    Problem problem=new Problem(probId,title,desc,image,username,village,status,date);
                                    problemReference.child(probId).setValue(problem);

                                }
                            }
                            progressDialog.dismiss();
                            Toast.makeText(PostProblemActivity.this, "Problem posted successfully", Toast.LENGTH_SHORT).show();

                            //newProblem.child("email").setValue(currentUser.getEmail());

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            });
        }else{
            Toast.makeText(this, "Fill all the details", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode==RESULT_OK){
            imageUri=data.getData();
            imageButtonAdd.setImageURI(imageUri);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
