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
import com.techroof.searchrishta.EditProfileActivity;
import com.techroof.searchrishta.R;

import java.util.HashMap;
import java.util.Map;

public class HabitsPrefActivity extends AppCompatActivity {

    private TextInputLayout etEatingpref, etDrinkingpref, etSmokingPreferences;
    private String eating, drinking, smoking;
    private FirebaseFirestore firestore;
    String uId;
    String[] eatingList, drinkingList, smokingList;
    Button btnUpdatehabits;
    private ImageView imgBack;
    private ProgressDialog pd;
    private String prefExist="null";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habits_pref);

        etEatingpref = findViewById(R.id.et_eating_preferences);
        etDrinkingpref = findViewById(R.id.et_drinking_preferences);
        etSmokingPreferences = findViewById(R.id.et_smoking_preferences);
        imgBack=findViewById(R.id.edit_profile_back_btn);
        btnUpdatehabits = findViewById(R.id.btn_update_habits_preferences);
        eatingList = getResources().getStringArray(R.array.eating);
        drinkingList = getResources().getStringArray(R.array.drinker);
        firestore = FirebaseFirestore.getInstance();
        uId = FirebaseAuth.getInstance().getUid();
        smokingList = getResources().getStringArray(R.array.smoker);

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



        getHabitsDetails();


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent moveEditProfile=new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(moveEditProfile);
                finish();

            }
        });
        etEatingpref.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(HabitsPrefActivity.this).setTitle("Select Your Habits Preference")
                        .setSingleChoiceItems(eatingList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etEatingpref.getEditText().setText(eatingList[selectedPosition]);
                                eating = eatingList[selectedPosition];
                            }
                        })
                        .show();
            }
        });

        etDrinkingpref.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(HabitsPrefActivity.this).setTitle("Select Your Habits Preferences")
                        .setSingleChoiceItems(drinkingList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etDrinkingpref.getEditText().setText(drinkingList[selectedPosition]);
                                drinking = drinkingList[selectedPosition];
                            }
                        })
                        .show();
            }
        });


        etSmokingPreferences.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(HabitsPrefActivity.this).setTitle("Select Your Smoking Preferences")
                        .setSingleChoiceItems(smokingList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etSmokingPreferences.getEditText().setText(smokingList[selectedPosition]);
                                smoking = smokingList[selectedPosition];
                            }
                        })
                        .show();
            }
        });



        btnUpdatehabits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(etEatingpref.getEditText().getText().toString())) {

                    etEatingpref.setError("enter your partner eating preference");

                }

                if (TextUtils.isEmpty(etDrinkingpref.getEditText().getText().toString())) {

                    etDrinkingpref.setError("Enter your partner drinking prefernces");

                }

                if (TextUtils.isEmpty(etSmokingPreferences.getEditText().getText().toString())) {

                    etSmokingPreferences.setError("Enter your partner smoking preferences");

                }

                if (!TextUtils.isEmpty(etEatingpref.getEditText().getText().toString()) && !TextUtils.isEmpty(etDrinkingpref.getEditText().getText().toString())
                        && !TextUtils.isEmpty(etSmokingPreferences.getEditText().getText().toString())){



                    if(prefExist.equals("yes")){


                        UpdateHabitsDetails(eating, drinking, smoking);


                    }else if(prefExist.equals("no")){


                        SetHabitsDetails(eating, drinking, smoking);


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
    }

    private void UpdateHabitsDetails(String eating, String drinking, String smoking) {


        Map<String, Object> userlocationDetailprefMap = new HashMap<>();
        userlocationDetailprefMap.put("Eating", eating);
        userlocationDetailprefMap.put("Drinking", drinking);
        userlocationDetailprefMap.put("Smoking", smoking);

        firestore.collection("users").document(uId).collection("Preferrences").document(uId)
                .update(userlocationDetailprefMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getApplicationContext(), "Habits Details Updated Successfully", Toast.LENGTH_LONG).show();

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


    public void getHabitsDetails() {


        firestore.collection("users").document(uId).collection("Preferrences").document(uId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isComplete()) {
                    if (task.getResult().exists()) {

                        pd.dismiss();
                    } else {

                        pd.dismiss();
                    }
                    eating = task.getResult().getString("Eating");
                    drinking = task.getResult().getString("Drinking");
                    smoking = task.getResult().getString("Smoking");


                    etEatingpref.getEditText().setText(eating);
                    etDrinkingpref.getEditText().setText(drinking);
                    etSmokingPreferences.getEditText().setText(smoking);
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


    private void SetHabitsDetails(String eating, String drinking, String smoking) {

        Map<String, Object> userlocationDetailprefMap = new HashMap<>();
        userlocationDetailprefMap.put("Eating", eating);
        userlocationDetailprefMap.put("Drinking", drinking);
        userlocationDetailprefMap.put("Smoking", smoking);

        firestore.collection("users").document(uId).collection("Preferrences").document(uId)
                .set(userlocationDetailprefMap).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getApplicationContext(), "Habits details has been set successfully", Toast.LENGTH_LONG).show();

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