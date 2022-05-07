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
import com.techroof.searchrishta.Authentication.RegisterActivity;
import com.techroof.searchrishta.EditProfileActivity;
import com.techroof.searchrishta.R;

import java.util.HashMap;
import java.util.Map;

public class ProfessionalPrefActivity extends AppCompatActivity {

    private TextInputLayout etOccupationpref, etEducationalpref, etIncomePreferences;
    private FirebaseFirestore firestore;
    private String uId,occupation,education,income;
    String[] occupationList, educationList, incomeList;
    Button btnUpdateproffesional;
    ImageView imgBack;
    private ProgressDialog pd;
    private String prefExist="null";


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_pref);

        etOccupationpref = findViewById(R.id.et_occupation_preferences);
        etEducationalpref = findViewById(R.id.et_education_preferences);
        etIncomePreferences = findViewById(R.id.et_income_preferences);
        btnUpdateproffesional=findViewById(R.id.btn_update_professional_preferences);
        occupationList = getResources().getStringArray(R.array.Occupation);
        educationList = getResources().getStringArray(R.array.Education);
        firestore=FirebaseFirestore.getInstance();
        uId= FirebaseAuth.getInstance().getUid();
        incomeList = getResources().getStringArray(R.array.IncomeRange);
        imgBack=findViewById(R.id.edit_profile_back_btn);

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

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent moveEditProfile=new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(moveEditProfile);
                finish();
            }
        });
        etOccupationpref.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(ProfessionalPrefActivity.this).setTitle("Select your partner  occupation preference")
                        .setSingleChoiceItems(occupationList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etOccupationpref.getEditText().setText(occupationList[selectedPosition]);
                                occupation = occupationList[selectedPosition];
                            }
                        })
                        .show();
            }
        });

        etEducationalpref.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(ProfessionalPrefActivity.this).setTitle("Select your partner educational preference")
                        .setSingleChoiceItems(educationList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etEducationalpref.getEditText().setText(educationList[selectedPosition]);
                                education = educationList[selectedPosition];
                            }
                        })
                        .show();
            }
        });


        etIncomePreferences.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new AlertDialog.Builder(ProfessionalPrefActivity.this).setTitle("Select your partner income preference")
                        .setSingleChoiceItems(incomeList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etIncomePreferences.getEditText().setText(incomeList[selectedPosition]);
                                income = incomeList[selectedPosition];
                            }
                        })
                        .show();
            }
        });
        btnUpdateproffesional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (TextUtils.isEmpty(etOccupationpref.getEditText().getText().toString())) {

                    etOccupationpref.setError("enter your partner occupation preference");

                }

                if (TextUtils.isEmpty(etEducationalpref.getEditText().getText().toString())) {

                    etEducationalpref.setError("Enter your partner education prefernces");

                }

                if (TextUtils.isEmpty(etIncomePreferences.getEditText().getText().toString())) {

                    etIncomePreferences.setError("Enter your partner income preferences");

                }

                if (!TextUtils.isEmpty(etOccupationpref.getEditText().getText().toString()) && !TextUtils.isEmpty(etEducationalpref.getEditText().getText().toString())
                        && !TextUtils.isEmpty(etIncomePreferences.getEditText().getText().toString())){


                    if(prefExist.equals("yes")){

                        UpdateProffesionalDetails(occupation,education,income);


                    }else if(prefExist.equals("no")){

                        SetProffesionalDetails(occupation,education,income);


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
        super.onBackPressed();
        Intent moveEditProfile=new Intent(getApplicationContext(), EditProfileActivity.class);
        startActivity(moveEditProfile);
        finish();
    }

    private void UpdateProffesionalDetails(String occupation, String education, String income) {

        Map<String, Object> userbasicDetailprefMap = new HashMap<>();
        userbasicDetailprefMap.put("Occupation", occupation);
        userbasicDetailprefMap.put("education", education);
        userbasicDetailprefMap.put("income", income);

        firestore.collection("users").document(uId).collection("Preferrences").document(uId)
                .update(userbasicDetailprefMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getApplicationContext(), "Professional Details Updated Successfully", Toast.LENGTH_LONG).show();

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

    private void SetProffesionalDetails(String occupation, String education, String income) {

        Map<String, Object> userbasicDetailprefMap = new HashMap<>();
        userbasicDetailprefMap.put("Occupation", occupation);
        userbasicDetailprefMap.put("education", education);
        userbasicDetailprefMap.put("income", income);

        firestore.collection("users").document(uId).collection("Preferrences").document(uId)
                .set(userbasicDetailprefMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getApplicationContext(), "Professional Details Set Successfully", Toast.LENGTH_LONG).show();

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
                    occupation = task.getResult().getString("Occupation");
                    education = task.getResult().getString("education");
                    income = task.getResult().getString("income");


                    etOccupationpref.getEditText().setText(occupation);
                    etEducationalpref.getEditText().setText(education);
                    etIncomePreferences.getEditText().setText(income);

                    pd.dismiss();

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

}