package com.techroof.searchrishta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.techroof.searchrishta.Model.Users;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {


    private TextView tvAge, tvProfileCreatedFor, tvGender, tvHeight, tvMaritalStatus, tvMotherTongue, tvPhysicalStatus, tvName, tvId, tvMemberstatus, tvCountry, tvEducation, tvOccupation, tvRelegion, tvcontactNumber;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private String uId, dob, profileCreator, gender, height, maritalStatus, motherTongue, physicalStatus, phone, name, memberShipstatus, country, education, relegion, occupation, currentUserid;
    private String Status = null;
    private String imgUrl;
    private String contactStatus;
    private CircleImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserid = firebaseAuth.getCurrentUser().getUid();
        tvAge = findViewById(R.id.tv_Age);
        tvProfileCreatedFor = findViewById(R.id.tv_Profile_created);
        tvGender = findViewById(R.id.tv_gender);
        tvHeight = findViewById(R.id.tv_height);
        tvMaritalStatus = findViewById(R.id.tv_marital_status);
        tvMotherTongue = findViewById(R.id.tv_mother_tongue);
        tvPhysicalStatus = findViewById(R.id.tv_physical_status);
        tvName = findViewById(R.id.label_Name);
        tvId = findViewById(R.id.label_Id);
        tvMemberstatus = findViewById(R.id.label_Membership_status);
        tvCountry = findViewById(R.id.tv_country);
        tvEducation = findViewById(R.id.tv_education);
        tvRelegion = findViewById(R.id.tv_relegion);
        tvOccupation = findViewById(R.id.tv_occupation);
        tvcontactNumber=findViewById(R.id.tv_phone);
        img=findViewById(R.id.image);
        Bundle extras = getIntent().getExtras();


        if (extras != null) {

            uId = extras.getString("content");



            getdata();

            getStatus();
        }


    }

    private void getdata() {

        firestore.collection("users")
                .whereEqualTo("userId", uId).
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        contactStatus=document.getString("activatedstatus");
                        country = document.getString("country");
                        dob = document.getString("dob");
                        education = document.getString("education");
                        gender = document.getString("gender");
                        height = document.getString("height");
                        maritalStatus = document.getString("maritalStatus");
                        motherTongue = document.getString("motherTongue");
                        name = document.getString("name");
                        physicalStatus = document.getString("physicalStatus");
                        profileCreator = document.getString("profileCreator");
                        relegion = document.getString("relegion");
                        occupation = document.getString("occupation");
                        relegion=document.getString("religion");
                        imgUrl=document.getString("img");
                        tvName.setText(name);
                        tvAge.setText(dob);
                        tvProfileCreatedFor.setText(profileCreator);
                        tvGender.setText(gender);
                        tvHeight.setText(height);
                        tvMaritalStatus.setText(maritalStatus);
                        tvMotherTongue.setText(motherTongue);
                        tvPhysicalStatus.setText(physicalStatus);
                        tvEducation.setText(education);
                        tvOccupation.setText(occupation);
                        tvRelegion.setText(relegion);
                        tvId.setText(uId);
                        tvMemberstatus.setText(contactStatus);
                        tvCountry.setText(country);

                        Glide.with(UserProfile.this)
                                .load(imgUrl)
                                .placeholder(R.drawable.user_avatar) // image url
                                .override(200, 200) // resizing
                                .centerCrop()
                                .into(img);


                        if(contactStatus.equals("activated")){

                            phone=document.getString("phone");
                            tvcontactNumber.setText(phone);

                        }

                        else if(contactStatus.equals("normal")){


                            new AlertDialog.Builder(UserProfile.this).setTitle("Upgrade to our Premium account ").
                                    setMessage("Activate to our premium account to have verified mobile numbers" +
                                            "and enable chats to other users")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int whichButton) {

                                            dialog.dismiss();

                                        }
                                    })
                                    .show();


                            tvcontactNumber.setText("Switch premium to unlock");
                        }


                    }
                } else {
                    Log.d("d", "Error getting documents: ", task.getException());

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    private void ViewedProfile(String userId, String currentUserId) {

        // String wallet_ID = firestore.collection("Wallet").document().getId();

        Map<String, Object> ViewerMap = new HashMap<>();
        ViewerMap.put("Viewed", userId);
        ViewerMap.put("Viewer", currentUserId);

        firestore.collection("ViewedProfile")
                .document()
                .set(ViewerMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "View Sent", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void getContactNumberStatus() {


        firestore.collection("users").document(uId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                contactStatus=task.getResult().getString("activatedstatus");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


            }
        });

    }



    private void getStatus() {


        firestore.collection("ViewedProfile")
                .whereEqualTo("Viewed", uId).whereEqualTo("Viewer", currentUserid)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult().isEmpty()) {

                    //getContactNumberStatus();

                    ViewedProfile(uId, currentUserid);

                }else{


                    Toast.makeText(getApplicationContext(), "You Have Already Viewed This Profile", Toast.LENGTH_SHORT).show();

                }


            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }














}