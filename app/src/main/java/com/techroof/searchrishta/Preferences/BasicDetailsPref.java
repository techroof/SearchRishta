package com.techroof.searchrishta.Preferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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


public class BasicDetailsPref extends AppCompatActivity {

    private TextInputLayout etAge,etHeight,etMotherTongue,etPhysicalStatus,etProfileCreatedFor;
    private String profileCreatedFor, age, Height, motherTongue, physicalStatus, uId;
    private FirebaseFirestore firestore;
    private Button btnBasicdetails;
    String[]  ageList, heightList,motherTongueList, physicalStatusList,profileCreatedForList;
    private ImageView imgBack;
    private ProgressDialog pd;
    private String prefExist="null";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_details_pref);
        uId = FirebaseAuth.getInstance().getUid();
        etProfileCreatedFor = findViewById(R.id.et_edit_profile_created_for_prf);
        etAge = findViewById(R.id.et_age_preferences);
        etHeight = findViewById(R.id.et_height_preferences);
        etMotherTongue = findViewById(R.id.et_edit_mother_tongue_prf);
        etPhysicalStatus = findViewById(R.id.et_physicalstatus_preferences);
        imgBack=findViewById(R.id.edit_profile_back_btn);
        btnBasicdetails = findViewById(R.id.btn_update_basic_preffer_details);
        firestore = FirebaseFirestore.getInstance();
        profileCreatedForList = getResources().getStringArray(R.array.profile_creator);
        ageList = getResources().getStringArray(R.array.age);
        heightList = getResources().getStringArray(R.array.height);
        motherTongueList = getResources().getStringArray(R.array.mother_tongue);
        physicalStatusList = getResources().getStringArray(R.array.physical_status);

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

            getDataBasicDetails();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent moveEditProfile=new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(moveEditProfile);
                finish();
            }
        });


        etProfileCreatedFor.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(BasicDetailsPref.this).setTitle("Select Your Profile Created For")
                        .setSingleChoiceItems(profileCreatedForList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etProfileCreatedFor.getEditText().setText(profileCreatedForList[selectedPosition]);
                                profileCreatedFor = profileCreatedForList[selectedPosition];


                            }
                        })
                        .show();
            }
        });


        etAge.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(BasicDetailsPref.this).setTitle("Select Your BasicDetails Preferences")
                        .setSingleChoiceItems(ageList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etAge.getEditText().setText(ageList[selectedPosition]);
                                age =ageList[selectedPosition];
                            }
                        })
                        .show();
            }
        });


        etMotherTongue.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(BasicDetailsPref.this).setTitle("Select MotherTongue Preferences")
                        .setSingleChoiceItems(motherTongueList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etMotherTongue.getEditText().setText(motherTongueList[selectedPosition]);
                                motherTongue = motherTongueList[selectedPosition];
                            }
                        })
                        .show();

            }
        });

        etHeight.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(BasicDetailsPref.this).setTitle("Select height Preferences")
                        .setSingleChoiceItems(heightList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etHeight.getEditText().setText(heightList[selectedPosition]);
                                Height = heightList[selectedPosition];
                            }
                        })
                        .show();

            }
        });

        etPhysicalStatus.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(BasicDetailsPref.this).setTitle("Select Physical Preferences")
                        .setSingleChoiceItems(physicalStatusList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etPhysicalStatus.getEditText().setText(physicalStatusList[selectedPosition]);
                                physicalStatus = physicalStatusList[selectedPosition];
                            }
                        })
                        .show();

            }
        });


        btnBasicdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (TextUtils.isEmpty(etProfileCreatedFor.getEditText().getText().toString())) {

                    etProfileCreatedFor.setError("enter your partner profile creator preference");

                }

                if (TextUtils.isEmpty(etAge.getEditText().getText().toString())) {

                    etAge.setError("Enter your partner age preferences");

                }

                if (TextUtils.isEmpty(etHeight.getEditText().getText().toString())) {

                    etHeight.setError("Enter your partner height preferences");

                }

                if (TextUtils.isEmpty(etMotherTongue.getEditText().getText().toString())) {

                    etMotherTongue.setError("Enter your partner mother tongue preferences");

                }

                if (TextUtils.isEmpty(etPhysicalStatus.getEditText().getText().toString())) {

                    etPhysicalStatus.setError("Enter your partner physical status preferences");

                }

                if (!TextUtils.isEmpty(etProfileCreatedFor.getEditText().getText().toString()) && !TextUtils.isEmpty(etAge.getEditText().getText().toString())
                        && !TextUtils.isEmpty(etHeight.getEditText().getText().toString())&& !TextUtils.isEmpty(etMotherTongue.getEditText().getText().toString())
                        && !TextUtils.isEmpty(etPhysicalStatus.getEditText().getText().toString())){


                    if(prefExist.equals("yes")){

                        UpdateBasicDetails(profileCreatedFor,age,Height,motherTongue,physicalStatus);


                    }else if(prefExist.equals("no")){


                        SetBasicDetails(profileCreatedFor,age,Height,motherTongue,physicalStatus);


                    }else{


                        Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                    }


                }else{


                    Toast.makeText(getApplicationContext(), "Please enter above fields", Toast.LENGTH_SHORT).show();
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

    private void UpdateBasicDetails(String profileCreatedFor, String age, String Height, String motherTongue, String physicalStatus) {

        Map<String, Object> userbasicDetailprefMap = new HashMap<>();
        userbasicDetailprefMap.put("profileCreatedFor", profileCreatedFor);
        userbasicDetailprefMap.put("age", age);
        userbasicDetailprefMap.put("Height", Height);
        userbasicDetailprefMap.put("motherTongue", motherTongue);
        userbasicDetailprefMap.put("physicalStatus", physicalStatus);

        firestore.collection("users").document(uId).collection("Preferrences").document(uId)
                .update(userbasicDetailprefMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getApplicationContext(), "Basic Details Updated Successfully", Toast.LENGTH_LONG).show();

                Intent moveEditProfile =new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(moveEditProfile);
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }




        });

    }

    public void getDataBasicDetails(){

        firestore.collection("users").document(uId).collection("Preferrences").document(uId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isComplete()) {
                    if (task.getResult().exists()) {

                        pd.dismiss();
                    } else {

                        pd.dismiss();
                    }
                    profileCreatedFor = task.getResult().getString("profileCreatedFor");
                    age = task.getResult().getString("age");
                    Height = task.getResult().getString("Height");
                    motherTongue = task.getResult().getString("motherTongue");
                    physicalStatus = task.getResult().getString("physicalStatus");

                    etProfileCreatedFor.getEditText().setText(profileCreatedFor);
                    etAge.getEditText().setText(age);
                    etHeight.getEditText().setText(Height);
                    etMotherTongue.getEditText().setText(motherTongue);
                    etPhysicalStatus.getEditText().setText(physicalStatus);
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


    private void SetBasicDetails(String profileCreatedFor, String age, String Height, String motherTongue, String physicalStatus) {

        Map<String, Object> userbasicDetailprefMap = new HashMap<>();
        userbasicDetailprefMap.put("profileCreatedFor", profileCreatedFor);
        userbasicDetailprefMap.put("age", age);
        userbasicDetailprefMap.put("Height", Height);
        userbasicDetailprefMap.put("motherTongue", motherTongue);
        userbasicDetailprefMap.put("physicalStatus", physicalStatus);

        firestore.collection("users").document(uId).collection("Preferrences").document(uId)
                .set(userbasicDetailprefMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getApplicationContext(), "Basic Details Updated Successfully", Toast.LENGTH_LONG).show();

                Intent moveEditProfile =new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(moveEditProfile);
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }

        });

    }


}

