package com.techroof.searchrishta.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.techroof.searchrishta.ChatBot.ChatActivity;
import com.techroof.searchrishta.Interfaces.DasboardClickListener;
import com.techroof.searchrishta.R;
import com.techroof.searchrishta.Shortlisted;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShortlistedAdapterRecyclerview extends RecyclerView.Adapter<ShortlistedAdapterRecyclerview.ShortlistHolder> {


    public ShortlistedAdapterRecyclerview(List<Shortlisted> shortlisteds, DasboardClickListener mlistener, Context context) {
        this.shortlisteds = shortlisteds;
        this.mlistener = mlistener;
        this.context = context;

        //firestore

        firestore=FirebaseFirestore.getInstance();

        firebaseAuth=FirebaseAuth.getInstance();

        uId=firebaseAuth.getCurrentUser().getUid();
    }

    private List<Shortlisted> shortlisteds=new ArrayList<>();
    public DasboardClickListener mlistener;
    private Context context;

    //firebase initialization

    private FirebaseFirestore firestore;

    private FirebaseAuth firebaseAuth;

    //----------------------------------//

    //Uid initialization

    private String uId;

    //status initialization

    private String statuss=null;

    //----------------------------------//


    @NonNull
    @Override
    public ShortlistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.custom_recyclerview_dashboard,parent,false);

        return new ShortlistHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShortlistHolder holder, @SuppressLint("RecyclerView") int position) {

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

        Shortlisted listed=shortlisteds.get(position);
        holder.tvid.setText(listed.getUserid());
        holder.tvState.setText(listed.getProvince());
        holder.tvHeight.setText(listed.getHeight());
        holder.tvCountry.setText(listed.getCountry());
        holder.tvCity.setText(listed.getCity());
        holder.tvdob.setText(listed.getDOB());
        holder.tvrelegion.setText(listed.getRelegion());
        holder.tvStatus.setText(listed.getMaritalstatus());
        holder.tvEducation.setText(listed.getEducation());
        holder.tvname.setText(listed.getName());

        //Remove
        holder.removeItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userId = listed.getUserid();
                String name =listed.getName();
                String dob = listed.getDOB();
                String height = listed.getHeight();
                String relegion = listed.getRelegion();
                String education = listed.getEducation();
                String maritalstatus = listed.getMaritalstatus();
                String city = listed.getCity();
                mlistener.onRemoveClick
                        (userId, name, dob, height,
                                relegion, education, maritalstatus, city,"xyz","xyz");


                notifyItemRemoved(position);
            }
        });

        //Chat

        holder.imgChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(statuss.equals("activated")){

                    String userId = listed.getUserid();
                    String name = listed.getName();

                    Intent moveChat = new Intent(context.getApplicationContext(), ChatActivity.class);
                    moveChat.putExtra("userId", userId);
                    moveChat.putExtra("userName", name);
                    context.startActivity(moveChat);

                }else if(statuss.equals("not activated")){

                    Toast.makeText(context.getApplicationContext(), "switch to our premium account to enable chat system", Toast.LENGTH_SHORT).show();

                }
            }
        });

        //Send Interest

        holder.imgSendInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //function
                Map<String, Object> ViewerMap = new HashMap<>();
                ViewerMap.put("InterestSent", listed.getUserid());
                ViewerMap.put("InterestedPerson", uId);
                ViewerMap.put("Status","Unseen");

                //firestorestatus check

                firestore.collection("SentInterests")
                        .whereEqualTo("InterestSent", listed.getUserid()).whereEqualTo("InterestedPerson", uId)
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

        return shortlisteds.size();
    }

    public void setShortlisted(List<Shortlisted> shortlisted){
        this.shortlisteds=shortlisted;
        notifyDataSetChanged();
    }





    class ShortlistHolder extends RecyclerView.ViewHolder{

        TextView tvid,tvname,tvdob,tvrelegion,tvState,tvEducation,tvCity,tvStatus,
                tvCountry,tvHeight;
        CardView cardViewvieww;
        ImageView imgChat,imgSendInterest;
        Button removeItems;
        public ShortlistHolder(@NonNull View itemView) {
            super(itemView);
            tvid = itemView.findViewById(R.id.tv_id);
            tvname = itemView.findViewById(R.id.tv_name);
            tvdob=itemView.findViewById(R.id.tv_dob);
            tvrelegion=itemView.findViewById(R.id.tv_crd_relegion);
            tvEducation=itemView.findViewById(R.id.tv_crd_education);
            tvStatus=itemView.findViewById(R.id.tv_crd_working_status);
            tvCity=itemView.findViewById(R.id.tv_city);
            tvCountry=itemView.findViewById(R.id.tv_nationality);
            tvState=itemView.findViewById(R.id.tv_state);
            tvHeight=itemView.findViewById(R.id.tv_crd_height);
            cardViewvieww=itemView.findViewById(R.id.parentlayout_dashboard);
            removeItems=itemView.findViewById(R.id.btn_remove);
            imgChat=itemView.findViewById(R.id.img_chat);
            imgSendInterest=itemView.findViewById(R.id.img_send_interest);
        }
    }
}
