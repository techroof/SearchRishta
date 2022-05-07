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

public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationRecyclerViewAdapter.ViewAdapter> {
    private static final String TAG = "RecyclerViewAdapter";
    public String flag = "true";

    private ArrayList<Users> UserlistData;
    private Context context;



    public NotificationRecyclerViewAdapter(ArrayList<Users> UserlistData, Context context) {
        this.UserlistData = UserlistData;
        this.context = context;






    }

    @NonNull
    @Override
    public NotificationRecyclerViewAdapter.ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: 1");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recyclerview_notifications, parent, false);
        return new ViewAdapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationRecyclerViewAdapter.ViewAdapter holder, int position) {


        Users ld = UserlistData.get(position);


            Log.d(TAG, "onBindViewHolder: called");
            holder.textViewname.setText(ld.getName() +  " Have shown interest in you ");

        Glide.with(context)
                .load(ld.getUserId())
                .placeholder(R.drawable.user_avatar) // image url
                .override(200, 200) // resizing
                .centerCrop()
                .into(holder.userImage);







    }


    @Override
    public int getItemCount() {
        return UserlistData.size();
    }

    public class ViewAdapter extends RecyclerView.ViewHolder {
        TextView  textViewname;

        CardView cardViewview;
        CircleImageView userImage;

        public ViewAdapter(@NonNull View itemView) {
            super(itemView);

            textViewname=itemView.findViewById(R.id.tv_name_notifiations);
            cardViewview = itemView.findViewById(R.id.parentlayout_notifications);
            userImage=itemView.findViewById(R.id.img_user_image_notifications);

        }
    }


}
