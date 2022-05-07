package com.techroof.searchrishta.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.techroof.searchrishta.Authentication.LoginActivity;
import com.techroof.searchrishta.ChatBot.ConvActivity;
import com.techroof.searchrishta.EditProfileActivity;
import com.techroof.searchrishta.HomeActivity;
import com.techroof.searchrishta.HomeFragment;
import com.techroof.searchrishta.Notification.NotificationActivity;
import com.techroof.searchrishta.R;
import com.techroof.searchrishta.Upgrade;
import com.techroof.searchrishta.UpgradeFragment;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    // Declare variables to store data from the constructor
    Context context;
    String[] programNameList;
    String[] programDescriptionList;
    int[] images;
    private FirebaseAuth mAuth;

    // Create a static inner class and provide references to all the Views for each data item.
    // This is particularly useful for caching the Views within the item layout for fast access.
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Declare member variables for all the Views in a row
        TextView rowName;
        ImageView rowImage;

        // Create a constructor that accepts the entire row and search the View hierarchy to find each subview
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Store the item subviews in member variables
            rowName = itemView.findViewById(R.id.tv_icon);
            rowImage = itemView.findViewById(R.id.img_icons);


        }
    }

    // Provide a suitable constructor
    public ProfileAdapter(Context context, String[] programNameList, int[] images) {
        // Initialize the class scope variables with values received from constructor
        this.context = context;
        this.programNameList = programNameList;
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a LayoutInflater object
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View view = inflater.inflate(R.layout.custom_recyclerview_profile, parent, false);
        // To attach OnClickListener

        mAuth = FirebaseAuth.getInstance();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView rowName = v.findViewById(R.id.tv_icon);

                if (rowName.getText().toString() == "Edit Profile") {

                    Intent intent = new Intent(context.getApplicationContext(), EditProfileActivity.class);
                    context.startActivity(intent);
                }

                if (rowName.getText().toString() == "Chat") {

                    Intent intent = new Intent(context.getApplicationContext(), ConvActivity.class);
                    context.startActivity(intent);
                }

                if (rowName.getText().toString() == "log out") {
                    mAuth.signOut();

                    Intent intent = new Intent(context.getApplicationContext(), LoginActivity.class);
                    context.startActivity(intent);
                }
                if(rowName.getText().toString()=="Notifications"){

                    Intent intent = new Intent(context.getApplicationContext(), NotificationActivity.class);
                    context.startActivity(intent);


                }


                if(rowName.getText().toString()=="Edit Partner Preferences"){

                    Intent intent = new Intent(context.getApplicationContext(), EditProfileActivity.class);
                    intent.putExtra("preference","preference");
                    context.startActivity(intent);


                }

                if(rowName.getText().toString()=="Daily Matches"){

                    Intent intent = new Intent(context.getApplicationContext(), HomeActivity.class);
                    context.startActivity(intent);

                }

                if(rowName.getText().toString()=="Upgrade Now"){

                    Intent intent = new Intent(context.getApplicationContext(), Upgrade.class);
                    context.startActivity(intent);

                }

                //if(rowName.getText().toString())

            }
        });
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // Create new views to be invoked by the layout manager
    @NonNull


    // Replace the contents of a view to be invoked by the layout manager
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get element from your dataset at this position and replace the contents of the View with that element
        holder.rowName.setText(programNameList[position]);
        holder.rowImage.setImageResource(images[position]);
    }

    // Return the size of your dataset
    @Override
    public int getItemCount() {
        return programNameList.length;
    }
}
