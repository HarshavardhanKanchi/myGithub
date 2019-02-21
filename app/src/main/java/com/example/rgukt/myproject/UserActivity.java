package com.example.rgukt.myproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends AppCompatActivity {


    private DatabaseReference databaseReference;
    private DatabaseReference userReference;


    ListView listViewProblem;
    List<Problem> problemList;

    ProgressDialog progressDialog;

    TextView textViewChat;

    Toolbar toolbar;

    Query query;

    CircleImageView profileImage;

    String uId,village,username;

    //TextView textViewDiaplay;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        listViewProblem=(ListView)findViewById(R.id.listView_Problem);
        problemList=new ArrayList<>();

        progressDialog=new ProgressDialog(this);



       // textViewDiaplay=(TextView)findViewById(R.id.textView_display);

        Bundle bundle=getIntent().getExtras();
        uId=bundle.getString("uId");

        FloatingActionButton fab = findViewById(R.id.add_problem);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserActivity.this,PostProblemActivity.class);
                intent.putExtra("uId",uId);
                startActivity(intent);
            }
        });

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Problem");
        userReference=FirebaseDatabase.getInstance().getReference().child("User");

        profileImage=(CircleImageView)findViewById(R.id.profile_image);

        textViewChat=(TextView)findViewById(R.id.textView_chat);

        toolbar=(Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("\t\t\t\tSOLVillage");


        progressDialog.setMessage("Loading please wait...");
        progressDialog.show();



        query=userReference.child(uId);


       // Log.d("hello",uId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user=dataSnapshot.getValue(User.class);

                username=user.getuName();
                village=user.getuVillage();

                Glide.with(UserActivity.this).load(user.getuProfile()).into(profileImage);

                query=databaseReference.orderByChild("village").equalTo(village);

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {

                            problemList.clear();
                            for (DataSnapshot problemSnapshot : dataSnapshot.getChildren()) {
                                Problem problem = problemSnapshot.getValue(Problem.class);

                                problemList.add(problem);
                            }
                            ProblemList adapter = new ProblemList(UserActivity.this, problemList);
                            listViewProblem.setAdapter(adapter);
                            progressDialog.dismiss();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(UserActivity.this, "No new Posts", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listViewProblem.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Problem problem=problemList.get(position);
                final AlertDialog.Builder builder=new AlertDialog.Builder(UserActivity.this);

                builder.setTitle("Remove Post");
                builder.setMessage("Are you sure want to remove post?");
                final AlertDialog alertDialog=builder.create();

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(problem.getUsername().equals(username)) {

                            databaseReference = FirebaseDatabase.getInstance().getReference("Problem").child(problem.getProblemId());
                            databaseReference.removeValue();
                            alertDialog.dismiss();
                            Toast.makeText(UserActivity.this, "Removed successfully", Toast.LENGTH_SHORT).show();

                        }else{

                            Toast.makeText(UserActivity.this, "You can't delete the post,because it is posted by someone", Toast.LENGTH_LONG).show();
                        }

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

        textViewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserActivity.this,ChatActivity.class);
                Bundle extras=new Bundle();
                extras.putString("uId",uId);
                extras.putString("key","user");
                intent.putExtras(extras);
                startActivity(intent);
            }
        });



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_add){
            Intent intent=new Intent(UserActivity.this,PostProblemActivity.class);
            intent.putExtra("uId",uId);
            startActivity(intent);

        }
        if(item.getItemId()==R.id.action_logout){
            Intent intent=new Intent(UserActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        if(item.getItemId()==R.id.action_profile){
            Intent intent=new Intent(UserActivity.this,UserProfileActivity.class);
            intent.putExtra("uId",uId);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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
