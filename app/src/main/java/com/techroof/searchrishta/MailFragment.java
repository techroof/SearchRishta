package com.techroof.searchrishta;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.techroof.searchrishta.ChatBot.ChatActivity;
import com.techroof.searchrishta.ChatBot.ConvActivity;
import com.techroof.searchrishta.Model.Conv;

import de.hdodenhof.circleimageview.CircleImageView;


public class MailFragment extends Fragment {

    private RecyclerView mConvList;
    private DatabaseReference mConvDatabase;
    private DatabaseReference mMessageDatabase;
    private DatabaseReference mMessageDatabasestatus;
    private FirebaseFirestore mUsersDatabase;
    private FirebaseAuth mAuth;
    private String mCurrent_user_id,names;

    public MailFragment() {
        // Required empty public constructor
    }


    public static MailFragment newInstance(String param1, String param2) {
        MailFragment fragment = new MailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_mail, container, false);

        mConvList = view.findViewById(R.id.mail_list);
        mAuth = FirebaseAuth.getInstance();

        try {
            mCurrent_user_id = mAuth.getCurrentUser().getUid();
            mConvDatabase = FirebaseDatabase.getInstance().getReference().child("chat").child(mCurrent_user_id);

            mConvDatabase.keepSynced(true);
            mMessageDatabasestatus = FirebaseDatabase.getInstance().getReference().child("users");
            mUsersDatabase = FirebaseFirestore.getInstance();
            mMessageDatabase = FirebaseDatabase.getInstance().getReference().child("messages").child(mCurrent_user_id);
            mMessageDatabasestatus.keepSynced(true);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            // RecyclerView.LayoutManager layoutManager=new android.support.v7.widget.LinearLayoutManager(this, android.support.v7.widget.LinearLayoutManager.VERTICAL,true);

            mConvList.setHasFixedSize(true);
            mConvList.setLayoutManager(linearLayoutManager);

        } catch (Exception e) {
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


        try {

            Query conversationQuery = mConvDatabase.orderByChild("timestamp");

            final FirebaseRecyclerAdapter<Conv, MailFragment.ConvViewHolder> firebaseConvAdapter =
                    new FirebaseRecyclerAdapter<Conv, MailFragment.ConvViewHolder>(
                            Conv.class,
                            R.layout.users_single_layout,
                            MailFragment.ConvViewHolder.class,
                            conversationQuery
                    ) {
                        @Override
                        protected void populateViewHolder(final MailFragment.ConvViewHolder convViewHolder,
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

                                                Toast.makeText(getContext(), "yes", Toast.LENGTH_SHORT).show();
                                                Intent chatIntent = new Intent(getContext(), ChatActivity.class);
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
                                                Toast.makeText(getContext(), "long clicked", Toast.LENGTH_SHORT).show();
                                                return false;

                                            }
                                        });


                                    } catch (Exception e) {
                                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
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