package com.example.rgukt.myproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView textViewName,textViewVillageName,textViewChangePassword,textViewLogout;

    DatabaseReference databaseReference;
    StorageReference storageReference;

    ProgressDialog progressDialog;
    CircleImageView circleImageViewProfile;
    ImageView imageViewEdit;


    private Uri profileUri=null;
    private static  int GALLERY_REQUEST=1;

    //String uname,uvillage,uprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        toolbar=(Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        textViewName=(TextView)findViewById(R.id.textView_person_name);
        textViewVillageName=(TextView)findViewById(R.id.textView_village_name);
        textViewChangePassword=(TextView)findViewById(R.id.textView_change_password);
        textViewLogout=(TextView)findViewById(R.id.textView_logout);
        circleImageViewProfile=(CircleImageView) findViewById(R.id.profile_image);
        imageViewEdit=(ImageView)findViewById(R.id.image_edit);


        Bundle bundle=getIntent().getExtras();
        final String uId=bundle.getString("uId");

        progressDialog=new ProgressDialog(this);

        databaseReference= FirebaseDatabase.getInstance().getReference("User").child(uId);
        storageReference= FirebaseStorage.getInstance().getReference();



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);

                textViewName.setText(user.getuName());
                textViewVillageName.setText(user.getuVillage());
                Glide.with(getApplicationContext()).load(user.getuProfile()).into(circleImageViewProfile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
                progressDialog.setMessage( "Updating please wait...");
                progressDialog.show();
            }
        });

        textViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserProfileActivity.this,Loginactivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        textViewChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final  AlertDialog.Builder builder=new AlertDialog.Builder(UserProfileActivity.this);


                LayoutInflater inflater=getLayoutInflater();
                final View view=inflater.inflate(R.layout.change_password,null);

                builder.setView(view);
                builder.setTitle("      \tChange Password ");
                final AlertDialog alertDialog=builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();


                final EditText editTextNew=(EditText)view.findViewById(R.id.editText_new_password);
                final Button buttonChange=(Button)view.findViewById(R.id.button_change_password);




               // databaseReference= FirebaseDatabase.getInstance().getReference("President").child(uId);


                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final User user=dataSnapshot.getValue(User.class);


                        buttonChange.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final String newPassword = editTextNew.getText().toString().trim();

                                if (!TextUtils.isEmpty(newPassword)) {

                                    progressDialog.setMessage("Password is Updating...");
                                    progressDialog.setCanceledOnTouchOutside(false);
                                    progressDialog.show();

                                    User user1 = new User(uId,user.getuName(),user.getuEmail(), newPassword,user.getuMobile(),
                                            user.getuState(), user.getuDistrict(), user.getuMandal(), user.getuPanchayt(),
                                            user.getuVillage(),user.getuProfile());

                                    databaseReference.setValue(user1);

                                    progressDialog.dismiss();
                                    Toast.makeText(UserProfileActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                    Intent intent=new Intent(UserProfileActivity.this,Loginactivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);


                                }else{
                                    Toast.makeText(UserProfileActivity.this, "could not update", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode==RESULT_OK){
            profileUri=data.getData();
            circleImageViewProfile.setImageURI(profileUri);
            StorageReference filepath2=storageReference.child("ProfileImages").child(profileUri.getLastPathSegment());
            filepath2.putFile(profileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downloadUrl=taskSnapshot.getDownloadUrl();
                    final String image=downloadUrl.toString();
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user=dataSnapshot.getValue(User.class);
                            User userProfile = new User(user.getuId(),user.getuName(),user.getuEmail(), user.getuPassword(),user.getuMobile(),
                                    user.getuState(), user.getuDistrict(), user.getuMandal(), user.getuPanchayt(),
                                    user.getuVillage(),image);
                            databaseReference.setValue(userProfile);
                            Toast.makeText(UserProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                            progressDialog.dismiss();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }

            });
        }else{
            progressDialog.dismiss();
            Toast.makeText(this, "Could not update image", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

