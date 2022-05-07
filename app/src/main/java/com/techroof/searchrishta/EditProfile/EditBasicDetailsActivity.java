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
import com.techroof.searchrishta.Preferences.BasicDetailsPref;
import com.techroof.searchrishta.R;

import java.util.HashMap;
import java.util.Map;

public class EditBasicDetailsActivity extends AppCompatActivity {


    private TextInputLayout etName, etAge, etHeight, etMotherTongue, etPhysicalStatus, etProfileCreatedFor, etMaritalStatus, etGender;
    private String maritalStatus, age, Height, motherTongue, physicalStatus, uId, profileCreated, gender, name;
    private FirebaseFirestore firestore;
    private Button btnBasicdetails;
    String[] maritalList, ageList, heightList, motherTongueList, physicalStatusList, profileCreatorList, genderList;
    private ImageView imgBack;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_basic_details);

        uId = FirebaseAuth.getInstance().getUid();
        etMaritalStatus = findViewById(R.id.et_edit_marital_status);
        etName = findViewById(R.id.et_edit_name);
        etAge = findViewById(R.id.et_edit_age);
        etHeight = findViewById(R.id.et_edit_height);
        etMotherTongue = findViewById(R.id.et_edit_mother_tongue);
        etPhysicalStatus = findViewById(R.id.et_edit_physical_status);
        etGender = findViewById(R.id.et_edit_gender);
        etProfileCreatedFor = findViewById(R.id.et_edit_profile_created_for);
        btnBasicdetails = findViewById(R.id.btn_edit_profile_detail);
        imgBack = findViewById(R.id.edit_profile_back_btn);

        firestore = FirebaseFirestore.getInstance();
        maritalList = getResources().getStringArray(R.array.marital_status);
        ageList = getResources().getStringArray(R.array.age);
        heightList = getResources().getStringArray(R.array.height);
        motherTongueList = getResources().getStringArray(R.array.mother_tongue);
        physicalStatusList = getResources().getStringArray(R.array.physical_status);
        genderList = getResources().getStringArray(R.array.gender);
        profileCreatorList = getResources().getStringArray(R.array.profile_creator);

        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent moveEditProfile =new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(moveEditProfile);
                finish();


            }
        });

        etName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = etName.getEditText().getText().toString();
            }
        });

        etMaritalStatus.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(EditBasicDetailsActivity.this).setTitle("Select Your Marital Status")
                        .setSingleChoiceItems(maritalList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etMaritalStatus.getEditText().setText(maritalList[selectedPosition]);
                                maritalStatus = maritalList[selectedPosition];
                            }
                        })
                        .show();

            }
        });

        etProfileCreatedFor.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new AlertDialog.Builder(EditBasicDetailsActivity.this).setTitle("Select Profile Created For")
                        .setSingleChoiceItems(profileCreatorList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etProfileCreatedFor.getEditText().setText(profileCreatorList[selectedPosition]);
                                profileCreated = profileCreatorList[selectedPosition];
                            }
                        })
                        .show();
            }
        });

        etGender.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new AlertDialog.Builder(EditBasicDetailsActivity.this).setTitle("Select Your Gender")
                        .setSingleChoiceItems(genderList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etGender.getEditText().setText(genderList[selectedPosition]);
                                gender = genderList[selectedPosition];
                            }
                        })
                        .show();
            }
        });


        etAge.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(EditBasicDetailsActivity.this).setTitle("Select Your Age")
                        .setSingleChoiceItems(ageList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etAge.getEditText().setText(ageList[selectedPosition]);
                                age = ageList[selectedPosition];
                            }
                        })
                        .show();

            }
        });

        etHeight.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(EditBasicDetailsActivity.this).setTitle("Select Your Height")
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


        etMotherTongue.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(EditBasicDetailsActivity.this).setTitle("Select Your MotherTongue")
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


        etPhysicalStatus.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(EditBasicDetailsActivity.this).setTitle("Select Your Physical Status")
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

        etProfileCreatedFor.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(EditBasicDetailsActivity.this).setTitle("Select profile Created For")
                        .setSingleChoiceItems(profileCreatorList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etProfileCreatedFor.getEditText().setText(profileCreatorList[selectedPosition]);
                                profileCreated = profileCreatorList[selectedPosition];
                            }
                        })
                        .show();

            }
        });

        etGender.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(EditBasicDetailsActivity.this).setTitle("Select Gender Criteria")
                        .setSingleChoiceItems(genderList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etGender.getEditText().setText(genderList[selectedPosition]);
                                gender = genderList[selectedPosition];
                            }
                        })
                        .show();

            }
        });

        etProfileCreatedFor.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(EditBasicDetailsActivity.this).setTitle("Select Profile Created for")
                        .setSingleChoiceItems(profileCreatorList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etProfileCreatedFor.getEditText().setText(profileCreatorList[selectedPosition]);
                                profileCreated = profileCreatorList[selectedPosition];
                            }
                        })
                        .show();
            }
        });


        btnBasicdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                name = etName.getEditText().getText().toString();



                if (TextUtils.isEmpty(etName.getEditText().getText().toString())) {

                    etName.setError("enter your name");

                }

                if (TextUtils.isEmpty(etMaritalStatus.getEditText().getText().toString())) {

                    etMaritalStatus.setError("Enter your marital status");

                }

                if (TextUtils.isEmpty(etAge.getEditText().getText().toString())) {

                    etAge.setError("Enter your age");

                }

                if (TextUtils.isEmpty(etHeight.getEditText().getText().toString())) {

                    etHeight.setError("Enter your height");

                }

                if (TextUtils.isEmpty(etMotherTongue.getEditText().getText().toString())) {

                    etMotherTongue.setError("Enter your mother tongue");

                }

                if (TextUtils.isEmpty(etPhysicalStatus.getEditText().getText().toString())) {

                    etPhysicalStatus.setError("Enter your physical status");

                }

                if (TextUtils.isEmpty(etProfileCreatedFor.getEditText().getText().toString())) {

                    etProfileCreatedFor.setError("Enter your profile creator");

                }

                if (TextUtils.isEmpty(etGender.getEditText().getText().toString())) {

                    etGender.setError("Enter your gender");

                }



                if (!TextUtils.isEmpty(etName.getEditText().getText().toString()) && !TextUtils.isEmpty(etMaritalStatus.getEditText().getText().toString())
                        && !TextUtils.isEmpty(etAge.getEditText().getText().toString()) && !TextUtils.isEmpty(etHeight.getEditText().getText().toString()
                ) && !TextUtils.isEmpty(etMotherTongue.getEditText().getText().toString()) && !TextUtils.isEmpty(etPhysicalStatus.getEditText().getText().toString()
                ) && !TextUtils.isEmpty(etProfileCreatedFor.getEditText().getText().toString()) && !TextUtils.isEmpty(etGender.getEditText().getText().toString())){



                    EditBasicDetails(name, maritalStatus, age, Height, motherTongue, physicalStatus, profileCreated, gender);


                }else{


                    Toast.makeText(getApplicationContext(), "Please enter above fields", Toast.LENGTH_SHORT).show();
                }


            }
        });


        getBasicDetails();
    }

    @Override
    public void onBackPressed() {
        Intent moveEditProfile =new Intent(getApplicationContext(), EditProfileActivity.class);
        startActivity(moveEditProfile);
        finish();
    }

    private void EditBasicDetails(String name, String maritalStatus, String age, String height, String motherTongue, String physicalStatus, String profileCreated, String gender) {


        Map<String, Object> userbasicDetailprefMap = new HashMap<>();
        userbasicDetailprefMap.put("name", name);
        userbasicDetailprefMap.put("maritalStatus", maritalStatus);
        userbasicDetailprefMap.put("age", age);
        userbasicDetailprefMap.put("Height", height);
        userbasicDetailprefMap.put("motherTongue", motherTongue);
        userbasicDetailprefMap.put("physicalStatus", physicalStatus);
        userbasicDetailprefMap.put("profileCreator", profileCreated);
        userbasicDetailprefMap.put("gender", gender);


        firestore.collection("users").document(uId)
                .update(userbasicDetailprefMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getApplicationContext(), "Basic Details Updated Successfully", Toast.LENGTH_LONG).show();

                Intent moveEditProfile =new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(moveEditProfile);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    private void getBasicDetails(){


        firestore.collection("users").document(uId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.getResult().exists()){

                    pd.show();

                }else{

                    pd.dismiss();
                }
                name=task.getResult().getString("name");
                maritalStatus= task.getResult().getString("maritalStatus");
                age=task.getResult().getString("dob");
                Height= task.getResult().getString("height");
                motherTongue=task.getResult().getString("motherTongue");
                physicalStatus=task.getResult().getString("physicalStatus");
                profileCreated=task.getResult().getString("profileCreator");
                gender=task.getResult().getString("gender");


                etName.getEditText().setText(name);
                etMaritalStatus.getEditText().setText(maritalStatus);
                etAge.getEditText().setText(age);
                etHeight.getEditText().setText(Height);
                etMotherTongue.getEditText().setText(motherTongue);
                etPhysicalStatus.getEditText().setText(physicalStatus);
                etProfileCreatedFor.getEditText().setText(profileCreated);
                etGender.getEditText().setText(gender);

                pd.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                pd.dismiss();
            }
        });


    }

}

