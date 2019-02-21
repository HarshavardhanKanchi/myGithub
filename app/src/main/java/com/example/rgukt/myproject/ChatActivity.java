package com.example.rgukt.myproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    EditText editTextMessage;
    ImageButton imageButtonSend;

    DatabaseReference databaseReference;

    DatabaseReference userReference;

    android.support.v7.widget.Toolbar toolbar;

    String uId,username,village,key;

    ListView listViewMessage;
    List<Message> messageList;

    NotificationManager notificationManager;

    Query query;

    //NotificationCompat.Builder builder;
    //
    // private static  final int uniqueID=1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        uId=bundle.getString("uId");
        key=bundle.getString("key");

        listViewMessage=(ListView)findViewById(R.id.messages_view);
        messageList=new ArrayList<>();

        editTextMessage=(EditText)findViewById(R.id.editTextMessage);
        imageButtonSend=(ImageButton)findViewById(R.id.imageButtonSend);

        databaseReference= FirebaseDatabase.getInstance().getReference("Message");
        if(key.equals("user")) {
            userReference = FirebaseDatabase.getInstance().getReference("User");
        }else{
            userReference = FirebaseDatabase.getInstance().getReference("President");
        }

        toolbar=findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CHAT");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        imageButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });



    }

    private void sendMessage() {

        final String message=editTextMessage.getText().toString().trim();

        if(!TextUtils.isEmpty(message)){

            final String msgId=databaseReference.push().getKey();



            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        if(key.equals("user")) {
                            User user = userSnapshot.getValue(User.class);
                            if (user.getuId().equals(uId)) {
                                username = user.getuName();
                                village = user.getuVillage();
                                Message message1 = new Message(msgId, username, village, message);
                                databaseReference.child(msgId).setValue(message1);

                            }
                        }else{
                            President president =userSnapshot.getValue(President.class);
                            if(president.getpId().equals(uId)){
                                username = president.getpUsername();
                                village = president.getpVillage();
                                Message message1 = new Message(msgId, username, village, message);
                                databaseReference.child(msgId).setValue(message1);
                            }
                        }



                    }

            query=databaseReference.orderByChild("msgSenderVillage").equalTo(village);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()){

                        messageList.clear();
                        for(DataSnapshot msgSnapshoshot:dataSnapshot.getChildren()) {
                            Message message1 = msgSnapshoshot.getValue(Message.class);
                            messageList.add(message1);
                        }

                        MessageList adapter = new MessageList(ChatActivity.this, messageList);
                        listViewMessage.setAdapter(adapter);
                        editTextMessage.setText(" ");

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }});
            }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }});


            ///Notification sending///

            /*builder = new NotificationCompat.Builder(ChatActivity.this);
            builder.setAutoCancel(true);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle(username);
            builder.setContentText(message);

            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.notify(uniqueID, builder.build());*/
        }



    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
