package com.techroof.searchrishta.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.techroof.searchrishta.ChatBot.ChatActivity;
import com.techroof.searchrishta.Interfaces.DasboardClickListener;
import com.techroof.searchrishta.Model.Users;
import com.techroof.searchrishta.R;
import com.techroof.searchrishta.UserProfile;
import com.techroof.searchrishta.ViewModel.ShortlistedViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MatchesFragmentRecyclerViewAdapter extends RecyclerView.Adapter<MatchesFragmentRecyclerViewAdapter.ViewAdapter> {
    private static final String TAG = "RecyclerViewAdapter";
    private ShortlistedViewModel getViewmodel;
    public DasboardClickListener mlistener;
    public String flag = "true";

    private ArrayList<Users> UserlistData;
    private Context context;
    private ArrayList<String> storedId;

    //firebase initialization

    private FirebaseFirestore firestore;

    private FirebaseAuth firebaseAuth;

    //----------------------------------//


    //Uid initialization

    private String uId;

    //status initialization

    private String statuss=null;

    //----------------------------------//



    public MatchesFragmentRecyclerViewAdapter(ArrayList<Users> UserlistData, Context context, DasboardClickListener listener, ArrayList<String> storedId) {


        this.UserlistData = UserlistData;
        this.context = context;
        this.mlistener = listener;
        this.storedId = storedId;

        //firestore

        firestore=FirebaseFirestore.getInstance();

        firebaseAuth=FirebaseAuth.getInstance();

        uId=firebaseAuth.getCurrentUser().getUid();

    }

    @NonNull
    @Override
    public MatchesFragmentRecyclerViewAdapter.ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: 1");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recyclerview_dashboard, parent, false);
        return new ViewAdapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchesFragmentRecyclerViewAdapter.ViewAdapter holder, int position) {

        Log.d(TAG, "onBindViewHolder: called");
        //checking condition of premium account to enable chat


        firestore.collection("users").whereEqualTo("userId",uId).whereEqualTo("activatedstatus","activated").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.getResult().isEmpty()){

                    statuss="not activated";

                }else{

                    statuss="activated";
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });




        Users ld = UserlistData.get(position);

        //  for(int i=0; i<UserlistData.size();i++) {

        //String id = storedId.get(4);
        if (storedId.contains(UserlistData.get(position).getUserId())) {

            Log.d(TAG, "onBindViewHolder: called");
            holder.textViewid.setText(ld.getUserId());
            holder.textViewState.setText(ld.getState());
            holder.textviewHeight.setText(ld.getHeight());
            holder.textViewCountry.setText(ld.getCountry());
            holder.textViewCity.setText(ld.getCity());
            holder.textViewdob.setText(ld.getDob());
            holder.textViewrelegion.setText(ld.getReligion());
            holder.textViewStatus.setText(ld.getMaritalStatus());
            holder.textViewEducation.setText(ld.getEducation());
            holder.textViewname.setText(ld.getName());
            Glide.with(context)
                    .load(ld.getUserId())
                    .placeholder(R.drawable.user_avatar) // image url
                    .override(200, 200) // resizing
                    .centerCrop()
                    .into(holder.circleImageView);
            //UserlistData.set(i,holder.imgShortlisted.setImageDrawable(context.getResources().getDrawable(R.drawable.shortlist)));
            holder.imgShortlisted.setImageDrawable(
                    context.getResources().getDrawable(R.drawable.shortlist));
            //flag = "true";
            //Toast.makeText(context.getApplicationContext(), "yes", Toast.LENGTH_LONG).show();
            holder.btnremove.setVisibility(View.VISIBLE);

        } else {

            Log.d(TAG, "onBindViewHolder: called");
            holder.textViewid.setText(ld.getUserId());
            holder.textViewState.setText(ld.getState());
            holder.textviewHeight.setText(ld.getHeight());
            holder.textViewCountry.setText(ld.getCountry());
            holder.textViewCity.setText(ld.getCity());
            holder.textViewdob.setText(ld.getDob());
            holder.textViewrelegion.setText(ld.getReligion());
            holder.textViewStatus.setText(ld.getMaritalStatus());
            holder.textViewEducation.setText(ld.getEducation());
            holder.textViewname.setText(ld.getName());
            Glide.with(context)
                    .load(ld.getUserId())
                    .placeholder(R.drawable.user_avatar) // image url
                    .override(200, 200) // resizing
                    .centerCrop()
                    .into(holder.circleImageView);

            holder.imgShortlisted.setImageDrawable(context.getResources().getDrawable(R.drawable.star_border));
            // flag = "false";
            //Toast.makeText(context.getApplicationContext(), "no", Toast.LENGTH_SHORT).show();
            holder.btnremove.setVisibility(View.INVISIBLE);

        }


        holder.imgShortlisted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = ld.getUserId();
                String name = ld.getName();
                String dob = ld.getDob();
                String height = ld.getHeight();
                String relegion = ld.getReligion();
                String education = ld.getEducation();
                String maritalstatus = ld.getMaritalStatus();
                String city = ld.getCity();
                String province = ld.getState();
                String country = ld.getCountry();

                holder.imgShortlisted.setImageDrawable(context.getResources().getDrawable(R.drawable.shortlist));
                mlistener.onItemclickk(userId, name, dob, height, relegion, education, maritalstatus, city, province, country);

                // if(flag.equals("false")){


                notifyDataSetChanged();
                //}
            }
        });

        holder.btnremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.btnremove.setVisibility(View.GONE);

                String userId = ld.getUserId();
                String name = ld.getName();
                String dob = ld.getDob();
                String height = ld.getHeight();
                String relegion = ld.getReligion();
                String education = ld.getEducation();
                String maritalstatus = ld.getMaritalStatus();
                String city = ld.getCity();
                String province = ld.getState();
                String country = ld.getCountry();

                mlistener.onRemoveClick
                        (userId, name, dob, height,
                                relegion, education, maritalstatus, city, province, country);


                notifyDataSetChanged();

            }
        });

        holder.cardViewview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context.getApplicationContext(), "position"+ld.getImage(), Toast.LENGTH_SHORT).show();
                /*Snackbar snackbar = Snackbar
                        .make(Ho, "www.journaldev.com", Snackbar.LENGTH_LONG);
                snackbar.show();*/
                String moveid = ld.getUserId();
                Intent intent = new Intent(context.getApplicationContext(), UserProfile.class);
                intent.putExtra("content", moveid);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

        holder.imgChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(statuss.equals("activated")){

                    String userId = ld.getUserId();
                    String name = ld.getName();
                    String dob = ld.getDob();
                    String height = ld.getHeight();
                    String relegion = ld.getReligion();
                    String education = ld.getEducation();
                    String maritalstatus = ld.getMaritalStatus();
                    String city = ld.getCity();
                    String province = ld.getState();
                    String country = ld.getCountry();

                    Intent moveChat = new Intent(context.getApplicationContext(), ChatActivity.class);
                    moveChat.putExtra("userId", userId);
                    moveChat.putExtra("userName", name);
                    context.startActivity(moveChat);

                }else if(statuss.equals("not activated")){

                    Toast.makeText(context.getApplicationContext(), "switch to our premium account to enable chat system", Toast.LENGTH_SHORT).show();


                }

            }
        });

        holder.imgSendInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //function
                Map<String, Object> ViewerMap = new HashMap<>();
                ViewerMap.put("InterestSent", ld.getUserId());
                ViewerMap.put("InterestedPerson", uId);

                //firestorestatus check

                firestore.collection("SentInterests")
                        .whereEqualTo("InterestSent", ld.getUserId()).whereEqualTo("InterestedPerson", uId)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult().isEmpty()) {

                            firestore.collection("SentInterests")
                                    .document()
                                    .set(ViewerMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                Toast.makeText(context.getApplicationContext(), "Interest Sent", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context.getApplicationContext(), ""+e.toString(), Toast.LENGTH_SHORT).show();

                                }
                            });

                        }else{


                            Toast.makeText(context.getApplicationContext(), "You Have Already Sent Interest", Toast.LENGTH_SHORT).show();

                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });



                //--------------------\\







            }
        });




    }

    @Override
    public int getItemCount() {
        return UserlistData.size();
    }

    public class ViewAdapter extends RecyclerView.ViewHolder {
        TextView textViewid, textViewname, textViewdob, textViewrelegion, textViewState, textViewEducation, textViewCity, textViewStatus,
                textViewCountry, textviewHeight;
        CardView cardViewview;
        ImageView imgShortlisted, imgChat, imgSendInterest;
        Button btnremove;
        CircleImageView circleImageView;

        public ViewAdapter(@NonNull View itemView) {
            super(itemView);

            textViewid = itemView.findViewById(R.id.tv_id);
            textViewname = itemView.findViewById(R.id.tv_name);
            textViewdob = itemView.findViewById(R.id.tv_dob);
            textViewrelegion = itemView.findViewById(R.id.tv_crd_relegion);
            textViewEducation = itemView.findViewById(R.id.tv_crd_education);
            textViewStatus = itemView.findViewById(R.id.tv_crd_working_status);
            textViewCity = itemView.findViewById(R.id.tv_city);
            textViewCountry = itemView.findViewById(R.id.tv_nationality);
            textViewState = itemView.findViewById(R.id.tv_state);
            textviewHeight = itemView.findViewById(R.id.tv_crd_height);
            cardViewview = itemView.findViewById(R.id.parentlayout_dashboard);
            imgShortlisted = itemView.findViewById(R.id.img_shortlisted);
            imgChat = itemView.findViewById(R.id.img_chat);
            imgSendInterest = itemView.findViewById(R.id.img_send_interest);
            btnremove = itemView.findViewById(R.id.btn_remove);
            imgChat = itemView.findViewById(R.id.img_chat);
            circleImageView=itemView.findViewById(R.id.img_icons);

        }
    }

}
