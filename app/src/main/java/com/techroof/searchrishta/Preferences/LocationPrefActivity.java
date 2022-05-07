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

public class LocationPrefActivity extends AppCompatActivity {

    private TextInputLayout etCountrypref, etCitizenpref, etResidentPreferences;
    private String country, citizenship, resident;
    private FirebaseFirestore firestore;
    String uId;
    String[] countryList, citizenList, residentList;
    Button btnUpdatelocation;
    ImageView imgBack;
    private ProgressDialog pd;
    private String prefExist="null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_pref);

        etCountrypref = findViewById(R.id.et_country_preferences);
        etCitizenpref = findViewById(R.id.et_citizen_preferences);
        etResidentPreferences = findViewById(R.id.et_resident_preferences);
        imgBack=findViewById(R.id.edit_profile_back_btn);
        btnUpdatelocation=findViewById(R.id.btn_update_location_preferences);
        countryList = getResources().getStringArray(R.array.countries);
        citizenList = getResources().getStringArray(R.array.citizenship);
        firestore=FirebaseFirestore.getInstance();
        uId= FirebaseAuth.getInstance().getUid();
        residentList = getResources().getStringArray(R.array.residentstatus);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent moveEditProfile =new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(moveEditProfile);
                finish();
            }
        });

        //location
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


        etCountrypref.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(LocationPrefActivity.this).setTitle("Select Your Country Preference")
                        .setSingleChoiceItems(countryList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etCountrypref.getEditText().setText(countryList[selectedPosition]);
                                country = countryList[selectedPosition];
                            }
                        })
                        .show();
            }
        });

        btnUpdatelocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(etCountrypref.getEditText().getText().toString())) {

                    etCountrypref.setError("enter your partner country preference");

                }

                if (TextUtils.isEmpty(etCitizenpref.getEditText().getText().toString())) {

                    etCitizenpref.setError("Enter your partner citizenship prefernces");

                }

                if (TextUtils.isEmpty(etResidentPreferences.getEditText().getText().toString())) {

                    etResidentPreferences.setError("Enter your partner resident preferences");

                }

                if (!TextUtils.isEmpty(etCountrypref.getEditText().getText().toString()) && !TextUtils.isEmpty(etCitizenpref.getEditText().getText().toString())
                        && !TextUtils.isEmpty(etResidentPreferences.getEditText().getText().toString())){


                    if(prefExist.equals("yes")){


                        UpdateLocationDetails(country,citizenship,resident);


                    }else if(prefExist.equals("no")){



                        SetLocationDetails(country,citizenship,resident);



                    }else{


                        Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                    }



                }else{


                    Toast.makeText(getApplicationContext(), "Please enter above fields", Toast.LENGTH_SHORT).show();
                }



                }




        });

        etCitizenpref.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(LocationPrefActivity.this).setTitle("Select Your Citizen Preferences")
                        .setSingleChoiceItems(citizenList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etCitizenpref.getEditText().setText(citizenList[selectedPosition]);
                                citizenship =citizenList[selectedPosition];
                            }
                        })
                        .show();
            }
        });


        etResidentPreferences.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(LocationPrefActivity.this).setTitle("Select Resident Preferences")
                        .setSingleChoiceItems(residentList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etResidentPreferences.getEditText().setText(residentList[selectedPosition]);
                                resident = residentList[selectedPosition];
                            }
                        })
                        .show();

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

    private void UpdateLocationDetails(String country, String citizenship, String resident) {

        Map<String, Object> userlocationDetailprefMap = new HashMap<>();
        userlocationDetailprefMap.put("Country", country);
        userlocationDetailprefMap.put("Citizenship", citizenship);
        userlocationDetailprefMap.put("ResidentStatus", resident);

        firestore.collection("users").document(uId).collection("Preferrences").document(uId)
                .update(userlocationDetailprefMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getApplicationContext(), "Location Details Updated Successfully", Toast.LENGTH_LONG).show();

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
                    country = task.getResult().getString("Country");
                    citizenship = task.getResult().getString("Citizenship");
                    resident = task.getResult().getString("ResidentStatus");


                    etCountrypref.getEditText().setText(country);
                    etCitizenpref.getEditText().setText(citizenship);
                    etResidentPreferences.getEditText().setText(resident);


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

    private void SetLocationDetails(String country, String citizenship, String resident) {

        Map<String, Object> userlocationDetailprefMap = new HashMap<>();
        userlocationDetailprefMap.put("Country", country);
        userlocationDetailprefMap.put("Citizenship", citizenship);
        userlocationDetailprefMap.put("ResidentStatus", resident);

        firestore.collection("users").document(uId).collection("Preferrences").document(uId)
                .set(userlocationDetailprefMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getApplicationContext(), "Location details set successfully", Toast.LENGTH_LONG).show();

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