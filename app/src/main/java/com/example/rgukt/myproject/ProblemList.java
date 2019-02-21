package com.example.rgukt.myproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.format.DateUtils;
import android.transition.CircularPropagation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProblemList extends ArrayAdapter<Problem> {
    private Activity context;
    private List<Problem> problemList;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference,userReference;
    Query query,query2;
    String user;


    public ProblemList(Activity context,List<Problem> problemList){
        super(context,R.layout.problem_row, problemList);
        this.context=context;
        this.problemList=problemList;

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("Problem");
        userReference=FirebaseDatabase.getInstance().getReference("User");

    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View view=inflater.inflate(R.layout.problem_row,null,true);

        TextView textViewTitle=(TextView)view.findViewById(R.id.textViewProblemTitle);
        TextView textViewDesc=(TextView)view.findViewById(R.id.textViewProblemDesc);
        TextView textViewUser=(TextView)view.findViewById(R.id.textView_user);

        TextView textViewStatus=(TextView)view.findViewById(R.id.textView_Status);
        TextView textViewDate=(TextView)view.findViewById(R.id.textView_Date);
        final CircleImageView profile=(CircleImageView)view.findViewById(R.id.profile_image);

        ImageView imageViewAdd=(ImageView)view.findViewById(R.id.post_image);
        Problem problem=problemList.get(position);

        textViewTitle.setText(problem.getTitle());
        textViewDesc.setText(problem.getDescription());
        textViewUser.setText(problem.getUsername());

        textViewDate.setText("On : "+problem.getDate());
        //Log.d("statusssss :","Soooolved");
            //textViewStatus.setTextColor(getContext().getResources().getColor(R.color.statusColor));

        textViewStatus.setText("Status: "+problem.getStatus());

       // Log.d("hello",problem.getStatus());
        if(problem.getStatus().equals("Solved")){

            textViewStatus.setTextColor(Color.GREEN);
        }

        Glide.with(context).load(problemList.get(position).getImage()).into(imageViewAdd);

        return view;

    }
}
