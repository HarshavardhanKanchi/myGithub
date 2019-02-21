package com.example.rgukt.myproject;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class MessageList extends ArrayAdapter<Message>  {
    private Activity context;
    private List<Message> messageList;
    private DatabaseReference databaseReference;

    public MessageList(Activity context,List<Message> messageList) {
        super(context,R.layout.activity_chat,messageList);
        this.context= (Activity) context;
        this.messageList=messageList;
        databaseReference= FirebaseDatabase.getInstance().getReference("Message");


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View view=inflater.inflate(R.layout.message_sent,null,true);

        TextView textViewMessage=(TextView)view.findViewById(R.id.message_send);
        TextView textViewUser=(TextView)view.findViewById(R.id.textView_user);

        Message message=messageList.get(position);

        textViewUser.setText("@ "+message.getMsgSender());


        textViewMessage.setText(message.getMsgText());

        return view;


    }
}
