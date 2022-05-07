package com.techroof.searchrishta.ChatBot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.techroof.searchrishta.Adapter.MessagesAdapter;
import com.techroof.searchrishta.Model.Messages;
import com.techroof.searchrishta.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private String mChatUser, userId, chatUser, otherUserName;

    private DatabaseReference mRootRef;
    private TextView mTitleView;
    private FirebaseAuth mAuth;
    private String mCurrentUserId;
    private EditText mChatMessageView;
    private ImageButton mChatSendBtn;
    private RecyclerView mMessagesList;
    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager mLinearLayout;
    private MessagesAdapter mAdapter;

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();

        mTitleView = findViewById(R.id.toolbar_title);
        mChatMessageView=findViewById(R.id.chat_message_view);
        mChatSendBtn = findViewById(R.id.chat_send_btn);

        mRootRef = FirebaseDatabase.getInstance().getReference();

        mCurrentUserId = mAuth.getCurrentUser().getUid();

        Bundle extras = getIntent().getExtras();
        if(extras!=null){

            mChatUser = getIntent().getStringExtra("userId");
            otherUserName=getIntent().getStringExtra("userName");

        }else{

            Toast.makeText(getApplicationContext(), "no", Toast.LENGTH_SHORT).show();
        }


        mTitleView.setText(otherUserName);

        if (TextUtils.isEmpty(chatUser)) {
            chatUser = mChatUser;

        } else if (TextUtils.isEmpty(mChatUser)) {

            mChatUser = chatUser;
        }

        //  Toast.makeText(ChatActivity.this, chatUser, Toast.LENGTH_SHORT).show();

        // String userName = getIntent().getStringExtra("user_name");

        //mChatAddBtn = (ImageButton) findViewById(R.id.chat_add_btn);

        mAdapter = new MessagesAdapter(messagesList);
        mMessagesList = (RecyclerView) findViewById(R.id.messages_list);
        mLinearLayout = new LinearLayoutManager(this);
        mMessagesList.setHasFixedSize(true);
        mMessagesList.setLayoutManager(mLinearLayout);

        mMessagesList.setAdapter(mAdapter);
        loadMessages();

        mChatSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });


        //mTitleView.setText(name);

        mRootRef.child("chat").child(mCurrentUserId).addValueEventListener
                (new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.hasChild(mChatUser)) {

                            Map chatAddMap = new HashMap();
                            chatAddMap.put("seen", false);
                            chatAddMap.put("timestamp", ServerValue.TIMESTAMP);
                            Map mChatUserMap = new HashMap();
                            mChatUserMap.put("chat/" + mCurrentUserId + "/" + mChatUser, chatAddMap);
                            mChatUserMap.put("chat/" + mChatUser + "/" + mCurrentUserId, chatAddMap);
                            mRootRef.updateChildren(mChatUserMap,
                                    new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            if (databaseError != null) {

                                                Log.d("CHAT_LOG", databaseError.getMessage().toString());

                                            }
                                        }
                                    });
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

    }





    //load messahes

    private void loadMessages() {
        mRootRef.child("messages").child(mCurrentUserId)
                .child(mChatUser).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Messages message = dataSnapshot.getValue(Messages.class);
                messagesList.add(message);
                mMessagesList.scrollToPosition(messagesList.size() - 1);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void sendMessage() {
        String message = mChatMessageView.getText().toString();
        if (!TextUtils.isEmpty(message)) {
            String current_user_ref = "messages/" + mCurrentUserId + "/" + mChatUser;
            String chat_user_ref = "messages/" + mChatUser + "/" + mCurrentUserId;
            DatabaseReference user_message_push = mRootRef.child("messages")
                    .child(mCurrentUserId).child(mChatUser).push();
            String push_id = user_message_push.getKey();
            Map messageMap = new HashMap();
            messageMap.put("message", message);
            messageMap.put("seen", false);
            messageMap.put("type", "text");
            messageMap.put("time", ServerValue.TIMESTAMP);
            messageMap.put("from", mCurrentUserId);
            Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
            messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);
            mChatMessageView.setText("");
            mRootRef.updateChildren(messageUserMap);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mRootRef.child("online").setValue("true");
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            mRootRef.child("online").setValue(ServerValue.TIMESTAMP);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
//            Intent main = new Intent(ChatActivity.this, MainActivity.class);
            //        startActivity(main);
        }
        return super.onOptionsItemSelected(item);
    }
}