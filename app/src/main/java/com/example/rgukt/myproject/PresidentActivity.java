package com.example.rgukt.myproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
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

public class PresidentActivity extends AppCompatActivity  {


    ProgressDialog progressDialog;

    DatabaseReference databaseReference;
    DatabaseReference presidentReference;
    DatabaseReference userReference;

    ListView listViewProblem;
    List<Problem> problemList;
    Query query;
    String village,problemId;

    Toolbar toolbar;
    String pId;

    CircleImageView presidentProfile;

    TextView textViewChatPresident;

   // SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_president);

        listViewProblem=(ListView)findViewById(R.id.listView_Problem);
        problemList=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Problem");

        presidentProfile=(CircleImageView)findViewById(R.id.profile_image);

        textViewChatPresident=(TextView)findViewById(R.id.textView_chat_president);



        toolbar=(Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("\t\t\t\tSOLVillage");



       // searchView=(SearchView)findViewById(R.id.searchView);

       // searchView.setOnQueryTextListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle bundle=getIntent().getExtras();
        assert bundle != null;
        pId=bundle.getString("pId");


        textViewChatPresident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PresidentActivity.this,ChatActivity.class);
                Bundle extras=new Bundle();
                extras.putString("uId",pId);
                extras.putString("key","president");
                intent.putExtras(extras);
                startActivity(intent);
            }
        });


        presidentReference=FirebaseDatabase.getInstance().getReference("President");
        query=presidentReference.child(pId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                President p=dataSnapshot.getValue(President.class);

                village=p.getpVillage();
                Glide.with(getApplicationContext()).load(p.getpProfile()).into(presidentProfile);

                query=databaseReference.orderByChild("village").equalTo(village);
                progressDialog.setMessage("Posts are loading please wait...");
                progressDialog.show();
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            problemList.clear();

                            for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                                final Problem problem = Snapshot.getValue(Problem.class);

                                problemList.add(problem);

                            }

                            ProblemList adapter = new ProblemList(PresidentActivity.this, problemList);
                            listViewProblem.setAdapter(adapter);
                            progressDialog.dismiss();
                        }else{
                            progressDialog.cancel();
                            Toast.makeText(PresidentActivity.this, "No new posts", Toast.LENGTH_SHORT).show();
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

        listViewProblem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Problem problem=problemList.get(position);

                //Log.d("qwerty",prid);
                Intent intent=new Intent(PresidentActivity.this,Individual_ProblemActivity.class);

                intent.putExtra("probId",problem.getProblemId());

                startActivity(intent);
            }
        });

        progressDialog=new ProgressDialog(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.president_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_logout){
            Intent intent=new Intent(PresidentActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        if(item.getItemId()==R.id.action_profile){
            Intent intent=new Intent(PresidentActivity.this,ProfileActivity.class);
            intent.putExtra("pId",pId);
            startActivity(intent);
        }
        if(item.getItemId()==R.id.action_all_users){


            AlertDialog.Builder builder=new AlertDialog.Builder(this);

            LayoutInflater inflater=getLayoutInflater();
            final View view=inflater.inflate(R.layout.all_users,null);
            builder.setView(view);
            builder.setTitle("      \t\t\t\t\tAll users ");
            final AlertDialog alertDialog=builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();


            RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
            final List<User> userList=new ArrayList<>();
            final UserRecyclerViewApdapter userRecyclerViewApdapter=new UserRecyclerViewApdapter(getApplicationContext(),userList);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            recyclerView.setAdapter(userRecyclerViewApdapter);



            userReference=FirebaseDatabase.getInstance().getReference("User");
            query=userReference.orderByChild("uVillage").equalTo(village);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot userSnapshot:dataSnapshot.getChildren()){
                        User user=userSnapshot.getValue(User.class);
                        userList.add(user);

                        userRecyclerViewApdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



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
