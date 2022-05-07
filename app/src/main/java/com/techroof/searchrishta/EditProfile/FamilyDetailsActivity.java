package com.techroof.searchrishta.EditProfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.techroof.searchrishta.EditProfileActivity;
import com.techroof.searchrishta.R;

import java.util.HashMap;
import java.util.Map;

public class FamilyDetailsActivity extends AppCompatActivity {


    TextInputLayout etFathersProfession,etMothersProfession,etFamilyOrigin,etNoOfBrothers,etNoOfSisters;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    String fatherProfession,mothersProfession,familyOrigin,noOfBrothers,noOfSisters,uId;
    private String[] fatherProfessionList, mothersProfessionList, familyOriginList;
    Button btnUpdateFamilyDetails;
    private ImageView imgBackeditprofile;
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_details);

        etFathersProfession=findViewById(R.id.et_edit_father_occupation);
        etMothersProfession=findViewById(R.id.et_edit_mother_occupation);
        etFamilyOrigin=findViewById(R.id.et_edit_family_origin);
        etNoOfBrothers=findViewById(R.id.et_edit_number_of_brothers);
        etNoOfSisters=findViewById(R.id.et_edit_number_of_sisters);
        btnUpdateFamilyDetails=findViewById(R.id.btn_edit_family_details);
        imgBackeditprofile=findViewById(R.id.edit_profile_back_btn);

        firestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        uId=firebaseAuth.getCurrentUser().getUid();

        fatherProfessionList=getResources().getStringArray(R.array.FatherOccupation);
        mothersProfessionList=getResources().getStringArray(R.array.MotherOccupation);
        familyOriginList=getResources().getStringArray(R.array.FamilyOrigin);

        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        GetFamilyDetails();

        etFathersProfession.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(FamilyDetailsActivity.this).setTitle("Select Your Fathers Occupation")
                        .setSingleChoiceItems(fatherProfessionList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etFathersProfession.getEditText().setText(fatherProfessionList[selectedPosition]);
                                fatherProfession= fatherProfessionList[selectedPosition];
                            }
                        })
                        .show();


            }
        });

        etMothersProfession.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new AlertDialog.Builder(FamilyDetailsActivity.this).setTitle("Select Your Mothers Occupation")
                        .setSingleChoiceItems(mothersProfessionList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etMothersProfession.getEditText().setText(mothersProfessionList[selectedPosition]);
                                mothersProfession= mothersProfessionList[selectedPosition];
                            }
                        })
                        .show();


            }
        });

        etFamilyOrigin.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(FamilyDetailsActivity.this).setTitle("Select Your Family")
                        .setSingleChoiceItems(familyOriginList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etFamilyOrigin.getEditText().setText(familyOriginList[selectedPosition]);
                                familyOrigin= familyOriginList[selectedPosition];
                            }
                        })
                        .show();
            }
        });

        etNoOfSisters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        etNoOfBrothers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        btnUpdateFamilyDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                noOfSisters=etNoOfSisters.getEditText().getText().toString();

                noOfBrothers=etNoOfBrothers.getEditText().getText().toString();
                if (TextUtils.isEmpty(etFathersProfession.getEditText().getText().toString())) {

                    etFathersProfession.setError("enter your fathers profession");

                }

                if (TextUtils.isEmpty(etMothersProfession.getEditText().getText().toString())) {

                    etMothersProfession.setError("Enter your mother profession");

                }

                if (TextUtils.isEmpty(etFamilyOrigin.getEditText().getText().toString())) {

                    etFamilyOrigin.setError("Enter your family origin");

                }

                if (TextUtils.isEmpty(etNoOfBrothers.getEditText().getText().toString())) {

                    etNoOfBrothers.setError("Enter your number of brothers");

                }


                if (TextUtils.isEmpty(etNoOfSisters.getEditText().getText().toString())) {

                    etNoOfSisters.setError("Enter your number of sisters");

                }



                if (!TextUtils.isEmpty(etFathersProfession.getEditText().getText().toString()) && !TextUtils.isEmpty(etMothersProfession.getEditText().getText().toString())
                        && !TextUtils.isEmpty(etFamilyOrigin.getEditText().getText().toString()) && !TextUtils.isEmpty(etNoOfBrothers.getEditText().getText().toString()
                ) && !TextUtils.isEmpty(etNoOfSisters.getEditText().getText().toString())){


                    UpdateFamilyDetails(fatherProfession,mothersProfession,familyOrigin,noOfBrothers,noOfSisters);


                }else{


                    Toast.makeText(getApplicationContext(), "Please enter above fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

        imgBackeditprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent moveEditProfile=new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(moveEditProfile);
                finish();
                //FamilyDetailsActivity.super.onBackPressed();
            }
        });


    }

    @Override
    public void onBackPressed() {

        Intent moveEditProfile=new Intent(getApplicationContext(), EditProfileActivity.class);
        startActivity(moveEditProfile);
        finish();
    }

    private void UpdateFamilyDetails(String FathersOccupation, String MothersOccupation, String FamilyOrigin, String NoOfBrothers, String NoOfSisters) {

        Map<String, Object> userbasicDetailprefMap = new HashMap<>();
        userbasicDetailprefMap.put("FatherProfession", FathersOccupation);
        userbasicDetailprefMap.put("MothersProfession", MothersOccupation);
        userbasicDetailprefMap.put("FamilyOrigin", FamilyOrigin);
        userbasicDetailprefMap.put("NumberOfBrothers", NoOfBrothers);
        userbasicDetailprefMap.put("NumbersOfSisters", NoOfSisters);

        firestore.collection("users").document(uId).update(userbasicDetailprefMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getApplicationContext(), "Family Details Updated Successfully", Toast.LENGTH_LONG).show();

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

    private void GetFamilyDetails(){


        firestore.collection("users").document(uId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isComplete()) {
                    if (task.getResult().exists()) {

                        pd.dismiss();

                    }else{

                        pd.dismiss();
                    }
                    fatherProfession = task.getResult().getString("FatherProfession");
                    mothersProfession = task.getResult().getString("MothersProfession");
                    familyOrigin = task.getResult().getString("FamilyOrigin");
                    noOfBrothers = task.getResult().getString("NumberOfBrothers");
                    noOfSisters = task.getResult().getString("NumbersOfSisters");

                    etFathersProfession.getEditText().setText(fatherProfession);
                    etMothersProfession.getEditText().setText(mothersProfession);
                    etFamilyOrigin.getEditText().setText(familyOrigin);
                    etNoOfBrothers.getEditText().setText(noOfBrothers);
                    etNoOfSisters.getEditText().setText(noOfSisters);

                    pd.dismiss();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                pd.dismiss();

            }
        });



    }



}