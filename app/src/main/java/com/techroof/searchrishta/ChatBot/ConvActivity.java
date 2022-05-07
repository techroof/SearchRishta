package com.techroof.searchrishta.ChatBot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.techroof.searchrishta.Model.Conv;
import com.techroof.searchrishta.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConvActivity extends AppCompatActivity {


    private RecyclerView mConvList;
    private DatabaseReference mConvDatabase;
    private DatabaseReference mMessageDatabase;
    private DatabaseReference mMessageDatabasestatus;
    private FirebaseFirestore mUsersDatabase;
    private FirebaseAuth mAuth;
    private String mCurrent_user_id,names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conv);

        mConvList = findViewById(R.id.chats_list);
        mAuth = FirebaseAuth.getInstance();

        try {
            mCurrent_user_id = mAuth.getCurrentUser().getUid();
            mConvDatabase = FirebaseDatabase.getInstance().getReference().child("chat").child(mCurrent_user_id);

            mConvDatabase.keepSynced(true);
            mMessageDatabasestatus = FirebaseDatabase.getInstance().getReference().child("users");
            mUsersDatabase = FirebaseFirestore.getInstance();
            mMessageDatabase = FirebaseDatabase.getInstance().getReference().child("messages").child(mCurrent_user_id);
            mMessageDatabasestatus.keepSynced(true);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ConvActivity.this);
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            // RecyclerView.LayoutManager layoutManager=new android.support.v7.widget.LinearLayoutManager(this, android.support.v7.widget.LinearLayoutManager.VERTICAL,true);

            mConvList.setHasFixedSize(true);
            mConvList.setLayoutManager(linearLayoutManager);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();


        try {

            Query conversationQuery = mConvDatabase.orderByChild("timestamp");

            final FirebaseRecyclerAdapter<Conv, ConvActivity.ConvViewHolder> firebaseConvAdapter =
                    new FirebaseRecyclerAdapter<Conv, ConvActivity.ConvViewHolder>(
                            Conv.class,
                            R.layout.users_single_layout,
                            ConvActivity.ConvViewHolder.class,
                            conversationQuery
                    ) {
                        @Override
                        protected void populateViewHolder(final ConvActivity.ConvViewHolder convViewHolder,
                                                          final Conv conv, final int i) {

                            final String list_user_id = getRef(i).getKey();

                            Query lastMessageQuery = mMessageDatabase.child(list_user_id).limitToLast(1);

                            lastMessageQuery.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                    String data = dataSnapshot.child("message").getValue().toString();
                                    convViewHolder.setMessage(data, conv.isSeen());

                                }

                                @Override
                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onChildRemoved(DataSnapshot dataSnapshot) {
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            mUsersDatabase.collection("users").whereEqualTo("userId", list_user_id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {


                                    if (task.isSuccessful()) {


                                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {


                                             names = documentSnapshot.getString("name");

                                            convViewHolder.setName(names);

                                        }


                                        convViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                Intent chatIntent = new Intent(getApplicationContext(), ChatActivity.class);
                                                chatIntent.putExtra("userId", list_user_id);
                                                chatIntent.putExtra("userName", names);
                                                startActivity(chatIntent);
                                            }
                                        });

                                    } else {


                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });

                            mMessageDatabasestatus.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    try {
                                        /*final String userName = dataSnapshot.child("name").getValue().toString();
                                        String userThumb = dataSnapshot.child("thumb_image").getValue().toString();*/

                                        if (dataSnapshot.hasChild("online")) {

                                            String userOnline = dataSnapshot.child("online").getValue().toString();
                                            convViewHolder.setUserOnline(userOnline);

                                        }

                                        /*convViewHolder.setName(userName);
                                        convViewHolder.setUserImage(userThumb, getApplicationContext());*/
/*
                                        convViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Toast.makeText(getApplicationContext(), "yes", Toast.LENGTH_SHORT).show();
                                                Intent chatIntent = new Intent(getApplicationContext(), ChatActivity.class);
                                                chatIntent.putExtra("userId", list_user_id);
                                                chatIntent.putExtra("userName", userName);
                                                startActivity(chatIntent);

                                            }
                                        });*/
                                        convViewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                                            @Override
                                            public boolean onLongClick(View view) {
                                                notifyDataSetChanged();
                                                convViewHolder.mView.setVisibility(View.GONE);
                                                Toast.makeText(getApplicationContext(), "long clicked", Toast.LENGTH_SHORT).show();
                                                return false;

                                            }
                                        });


                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {


                                }
                            });

                        }
                    };

            mConvList.setAdapter(firebaseConvAdapter);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public static class ConvViewHolder extends RecyclerView.ViewHolder {
        Context context;
        View mView;

        public ConvViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setMessage(String message, boolean isSeen) {

            TextView userStatusView = (TextView) mView.findViewById(R.id.user_single_status);
            userStatusView.setText(message);

            if (!isSeen) {
                userStatusView.setTypeface(userStatusView.getTypeface(), Typeface.BOLD);
            } else {
                userStatusView.setTypeface(userStatusView.getTypeface(), Typeface.NORMAL);
            }

        }

        public void setName(String name) {

            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);

        }

        public void setUserImage(String thumb_image, Context ctx) {

            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);
            Picasso.get().load(thumb_image).placeholder
                    (R.drawable.male_avatar).into(userImageView);

        }

        public void setUserOnline(String online_status) {

            ImageView userOnlineView = (ImageView) mView.findViewById(R.id.user_single_online_icon);

            if (online_status.equals("true")) {

                userOnlineView.setVisibility(View.VISIBLE);

            } else {

                userOnlineView.setVisibility(View.INVISIBLE);

            }

        }

    }
}
