package com.example.rgukt.myproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class UserRecyclerViewApdapter  extends RecyclerView.Adapter<UserRecyclerViewApdapter.ViewHolder>{
    private List<User> userList;
    private Context context;

   DatabaseReference databaseReference;

    public UserRecyclerViewApdapter(Context context,List<User> userList){
        this.userList=userList;
        this.context=context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext().getApplicationContext()).inflate(R.layout.user_row,parent,false);
        return  new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewUserName.setText(userList.get(position).getuName());

        holder.textViewUserVillage.setText(userList.get(position).getuVillage());

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View view;
        private TextView textViewUserName,textViewUserVillage;


        public ViewHolder(View itemView) {
            super(itemView);

            view=itemView;
            textViewUserName=(TextView)view.findViewById(R.id.textView_user_name);
            textViewUserVillage=(TextView)view.findViewById(R.id.textView_user_village);
        }
    }
}
