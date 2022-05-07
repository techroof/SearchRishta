package com.techroof.searchrishta.Preferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.techroof.searchrishta.EditProfileActivity;
import com.techroof.searchrishta.R;

import java.util.HashMap;
import java.util.Map;

public class PartnerDescription extends AppCompatActivity {

    private TextInputLayout et_desc;
    private String description;
    private FirebaseFirestore firestore;
    private FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    private Button btnDescription;
    private String uId;
    private ImageView imgBack;
    private ProgressDialog pd;
    private String prefExist="null";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_description);
        et_desc = findViewById(R.id.text_content);
        firestore = FirebaseFirestore.getInstance();
        btnDescription = findViewById(R.id.btn_add_description);
        imgBack = findViewById(R.id.edit_profile_back_btn);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent moveEditProfile=new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(moveEditProfile);
                finish();
            }
        });
        uId = FirebaseAuth.getInstance().getUid();

        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        firestore.collection("users").document(uId).collection("Preferrences").document(uId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){


                    if(task.getResult().exists()){

                        prefExist="yes";

                    }else{

                        prefExist="no";
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });



        getDescriptionDetails();


        btnDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(et_desc.getEditText().getText().toString())) {

                    et_desc.setError("enter your partner description preference");

                }


                if (!TextUtils.isEmpty(et_desc.getEditText().getText().toString())){

                    if(prefExist.equals("yes")){

                        description = et_desc.getEditText().getText().toString();
                        AddDescription(description);


                    }else if(prefExist.equals("no")){

                        description = et_desc.getEditText().getText().toString();
                        SetDescription(description);


                    }else{


                        Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                    }





                }else{


                    Toast.makeText(getApplicationContext(), "Please enter above field", Toast.LENGTH_SHORT).show();
                }




            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent moveEditProfile=new Intent(getApplicationContext(), EditProfileActivity.class);
        startActivity(moveEditProfile);
        finish();
        super.onBackPressed();
    }

    private void AddDescription(String desc) {

        Map<String, Object> userDescMap = new HashMap<>();
        userDescMap.put("PartnerDescription", desc);

        firestore.collection("users").document(uId).collection("Preferrences").document(uId)
                .update(userDescMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getApplicationContext(), "Description added successfully", Toast.LENGTH_LONG).show();

                Intent moveEditProfile =new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(moveEditProfile);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }

    public void getDescriptionDetails() {


        firestore.collection("users").document(uId).collection("Preferrences").document(uId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isComplete()) {
                    if (task.getResult().exists()) {

                        pd.dismiss();
                    } else {

                        pd.dismiss();
                    }
                    description = task.getResult().getString("PartnerDescription");


                    et_desc.getEditText().setText(description);
                    pd.dismiss();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                pd.dismiss();

                Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }


    private void SetDescription(String desc) {

        Map<String, Object> userDescMap = new HashMap<>();
        userDescMap.put("PartnerDescription", desc);

        firestore.collection("users").document(uId).collection("Preferrences").document(uId)
                .set(userDescMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getApplicationContext(), "Description added successfully", Toast.LENGTH_LONG).show();

                Intent moveEditProfile =new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(moveEditProfile);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }



}