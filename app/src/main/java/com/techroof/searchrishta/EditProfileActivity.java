package com.techroof.searchrishta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.techroof.searchrishta.EditProfile.EditBasicDetailsActivity;
import com.techroof.searchrishta.EditProfile.EditLocationActivity;
import com.techroof.searchrishta.EditProfile.EditProfessionalInformationActivity;
import com.techroof.searchrishta.EditProfile.FamilyDetailsActivity;
import com.techroof.searchrishta.EditProfile.HobbiesAndInterests;
import com.techroof.searchrishta.EditProfile.RelegiousInformationActivity;
import com.techroof.searchrishta.Preferences.BasicDetailsPref;
import com.techroof.searchrishta.Preferences.HabitsPrefActivity;
import com.techroof.searchrishta.Preferences.LocationPrefActivity;
import com.techroof.searchrishta.Preferences.PartnerDescription;
import com.techroof.searchrishta.Preferences.ProfessionalPrefActivity;
import com.techroof.searchrishta.Preferences.ReligionPrefActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {


    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    CollapsingToolbarLayout toolbarLayout;
    ImageView imgPrfdesc, imgPrfbasicdetails, imgRelegiousdetails, imgProfessionaldetails, imgLocationpref, imgHabitspref, imgPhone, imgMail, imgedit, imgEditProfessionalInformation, imgeditLocation, imgeditRelegious, imgeditFamilyDetails,imgHabitsandhobbies;
    String Uid;
    TextView tvAboutmyself, tvname, dateOfBirth, tvProfileCreated, tvGender, tvHeight, tvMaritalstatus, tvMothertongue, tvPhysicalStatus, tvEducation, tvEmployed, tvOccupation, tvAnnualIncome, tvCountry, tvState, tvCitizenShip, tvCity, tvRelegion, tvRelegiousValues, tvClan, tvFatherOccupation, tvMotherOccupation, tvFamilyOrigin, tvNumberOfSisters, tvNumberOfBrothers,tvEatingHabits,tvDrinkingHabits,tvSmokingHabits,tvHobbiesAndInterest,tvFavouriteMusic,tvFavouriteSports,tvFavouriteFood;
    TextView tvAgePref, tvHeightPref, tvProfileCreatorPref, tvPhysicalStatusPref, tvMotherTonguePref, tvRelegionPref,getTvRelegionPref,tvrelegionSect, tvOccupationPref, tvEducationpref, tvAnnualIncomePref, tvCountryPref, tvCitizenShipPrf, tvResidentStatusPrf, tvEatingHabitsPref, tvDrinkingHabitsPref, tvSmokingHAbitsPref,tvDescPref,tvLabelPartnerPreferences;
    //private CollapsingToolbarLayout toolbar;
    private Dialog dialog, dialogPhone, dialogMail;
    private String[] relegiousValues;
    private String relegiousvalues, uId, editPhone, editMail;
    private String eatingHabits,drinkingHabits,smokingHabits;
    EditText edtPhone, edtMail, AccountNo;
    String fathersOccupation,mothersOccupation,familyOrigin,noOfBrothers,noOfSisters;
    private NestedScrollView myView;
    private AppBarLayout myAppbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        Uid = firebaseAuth.getCurrentUser().getUid();
        toolbar = findViewById(R.id.toolbarr);
        toolbarLayout = findViewById(R.id.collapsing_toolbar);
        tvAboutmyself = findViewById(R.id.tv_about_myself);
        tvname = findViewById(R.id.tv_Name);
        dateOfBirth = findViewById(R.id.tv_Age);
        tvProfileCreated = findViewById(R.id.tv_Profile_created);
        tvGender = findViewById(R.id.tv_gender);
        tvHeight = findViewById(R.id.tv_height);
        tvMaritalstatus = findViewById(R.id.tv_marital_status);
        tvMothertongue = findViewById(R.id.tv_mother_tongue);
        tvPhysicalStatus = findViewById(R.id.tv_physical_status);
        tvEducation = findViewById(R.id.tv_education);
        tvEmployed = findViewById(R.id.tv_employed);
        tvOccupation = findViewById(R.id.tv_occupation);
        tvAnnualIncome = findViewById(R.id.tv_annual_income);
        tvCountry = findViewById(R.id.tv_country);
        tvCitizenShip = findViewById(R.id.tv_citizenship);
        tvState = findViewById(R.id.tv_state);
        tvCity = findViewById(R.id.tv_city);
        tvRelegiousValues = findViewById(R.id.tv_relegious_values);
        getTvRelegionPref=findViewById(R.id.tv_relegion_prf);
        tvrelegionSect=findViewById(R.id.tv_relegious_sect_prf);
        tvRelegion = findViewById(R.id.tv_relegion);
        tvClan = findViewById(R.id.tv_sect);
        tvFatherOccupation = findViewById(R.id.tv_fathers_occupation);
        tvMotherOccupation = findViewById(R.id.tv_mother_occupation);
        tvFamilyOrigin = findViewById(R.id.tv_family_origin);
        tvNumberOfSisters = findViewById(R.id.tv_no_of_sisters);
        tvNumberOfBrothers = findViewById(R.id.tv_no_brothers);
        tvEatingHabits=findViewById(R.id.tv_eating);
        tvDrinkingHabits=findViewById(R.id.tv_drinking);
        tvSmokingHabits=findViewById(R.id.tv_smoking);
        tvHobbiesAndInterest=findViewById(R.id.tv_hobbies_and_interests);
        tvFavouriteMusic=findViewById(R.id.tv_music);
        tvFavouriteSports=findViewById(R.id.tv_sports);
        tvFavouriteFood=findViewById(R.id.tv_food);
        myView=findViewById(R.id.scrollviews);
        tvLabelPartnerPreferences=findViewById(R.id.partner_preferences);
        myAppbar=findViewById(R.id.app_Bar_layout);
        //TextView Preferences

        tvDescPref=findViewById(R.id.tv_description);
        tvAgePref = findViewById(R.id.tv_age_prf);
        tvHeightPref = findViewById(R.id.tv_height_prf);
        tvProfileCreatorPref = findViewById(R.id.tv_marital_status_prf);
        tvPhysicalStatusPref = findViewById(R.id.tv_physical_status_prf);
        tvMotherTonguePref = findViewById(R.id.tv_mother_tongue_prf);

        tvRelegionPref = findViewById(R.id.tv_relegion_prf);

        tvOccupationPref = findViewById(R.id.tv_occupation_prf);
        tvEducationpref = findViewById(R.id.tv_education_prf);
        tvAnnualIncomePref = findViewById(R.id.tv_annual_income_prf);

        tvCountryPref = findViewById(R.id.tv_country_prf);
        tvCitizenShipPrf = findViewById(R.id.tv_citizenship_prf);
        tvResidentStatusPrf = findViewById(R.id.tv_resident_status_prf);

        tvEatingHabitsPref = findViewById(R.id.tv_eating_habits_prf);
        tvDrinkingHabitsPref = findViewById(R.id.tv_drinking_habits_prf);
        tvSmokingHAbitsPref=findViewById(R.id.tv_smoking_habits_prf);

        imgPrfdesc = findViewById(R.id.edit_partner_description);
        imgPrfbasicdetails = findViewById(R.id.edit_basic_details_prf);
        imgRelegiousdetails = findViewById(R.id.edit_relegious_information_prf);
        imgProfessionaldetails = findViewById(R.id.edit_professional_details_preferences_prf);

        imgPhone = findViewById(R.id.img_call);
        imgMail = findViewById(R.id.img_mail);
        imgedit = findViewById(R.id.edit);
        imgLocationpref = findViewById(R.id.edit_professional_location_prf);
        imgHabitspref = findViewById(R.id.edit_habits_prf);
        imgEditProfessionalInformation = findViewById(R.id.edit_professional_details);
        imgeditLocation = findViewById(R.id.edit_location);
        imgeditRelegious = findViewById(R.id.edit_relegious_information);
        imgeditFamilyDetails = findViewById(R.id.edit_label_family_details);
        imgHabitsandhobbies=findViewById(R.id.edit_hobbies_and_interest);
        relegiousValues = getResources().getStringArray(R.array.RelegiousValues);

        uId = FirebaseAuth.getInstance().getUid();


        //dialog box
        dialog = new Dialog(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setContentView(R.layout.relegious_pref_custom_dialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false); //Optional
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog


        //dialogbox phone

        dialogPhone = new Dialog(this);
        dialogPhone.setContentView(R.layout.edit_phone_custom_dialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            dialogPhone.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
        }
        dialogPhone.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogPhone.setCancelable(false); //Optional
        dialogPhone.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

        Button okayPhone = dialogPhone.findViewById(R.id.btn_okay_phone);
        Button cancelPhone = dialogPhone.findViewById(R.id.btn_cancel_phone);

        //dialogbox mail

        dialogMail = new Dialog(this);
        dialogMail.setContentView(R.layout.edit_mail_custom_dialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialogMail.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
        }
        dialogMail.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogMail.setCancelable(false); //Optional
        dialogMail.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

        Button okayMail = dialogMail.findViewById(R.id.btn_okay_mail);
        Button cancelMail = dialogMail.findViewById(R.id.btn_cancel_mail);

        Button Okay = dialog.findViewById(R.id.btn_okay);
        Button Cancel = dialog.findViewById(R.id.btn_cancel);
        AccountNo = dialog.findViewById(R.id.et_relegious_pref);
        edtPhone = dialogPhone.findViewById(R.id.et_edit_phonenumber);
        edtMail = dialogMail.findViewById(R.id.et_edit_mail);

        Intent intent = getIntent();
        if (intent != null) {

            //Is there a string extra in that intent?
            String extra = intent.getStringExtra("preference");
            //Is that string not empty and equals what you're looking for?

            if (!TextUtils.isEmpty(extra) && extra.equals("preference")) {


                myView.postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       myView.smoothScrollTo(0, tvLabelPartnerPreferences.getTop());
                       myAppbar.setExpanded(false);

                   }
               }, 1500);
            }
        }

////////////////------------------------\\\\\\\\\\\\\\\\\\\
        AccountNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(EditProfileActivity.this).setTitle("Select Relegious Values")
                        .setSingleChoiceItems(relegiousValues, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                AccountNo.setText(relegiousValues[selectedPosition]);
                                relegiousvalues = relegiousValues[selectedPosition];
                            }
                        })
                        .show();
            }
        });


        Okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddRelegiousValues(relegiousvalues);
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        //edit phone

        imgPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogPhone.show();
                getPhone();
            }
        });


        okayPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editPhone = edtPhone.getText().toString();
                EditPhone(editPhone);
            }
        });

        cancelPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogPhone.dismiss();
            }
        });

        //edit mail
        imgMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogMail.show();
                getMail();
            }
        });

        okayMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editMail = edtMail.getText().toString();
                EditMail(editMail);
            }
        });


        cancelMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogMail.dismiss();
            }
        });

        imgPrfdesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent movePartnerdesc = new Intent(getApplicationContext(), PartnerDescription.class);
                startActivity(movePartnerdesc);
            }
        });

        imgPrfbasicdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent movePrfbasicdetails = new Intent(getApplicationContext(), BasicDetailsPref.class);
                startActivity(movePrfbasicdetails);
            }
        });
        imgRelegiousdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //dialog.show();

                Intent movePrfreligiousdetails = new Intent(getApplicationContext(), ReligionPrefActivity.class);
                startActivity(movePrfreligiousdetails);
            }
        });

        imgProfessionaldetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent movePrfprofessionaldetails = new Intent(getApplicationContext(), ProfessionalPrefActivity.class);
                startActivity(movePrfprofessionaldetails);

            }
        });
        imgEditProfessionalInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent moveEditPrfprofessionaldetails = new Intent(getApplicationContext(), EditProfessionalInformationActivity.class);
                startActivity(moveEditPrfprofessionaldetails);
            }
        });


        imgedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveEditBasicDetails = new Intent(getApplicationContext(), EditBasicDetailsActivity.class);
                startActivity(moveEditBasicDetails);
            }
        });


        imgeditRelegious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent moveEditrelegiousDetails = new Intent(getApplicationContext(), RelegiousInformationActivity.class);
                startActivity(moveEditrelegiousDetails);
            }
        });

        imgeditLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent moveEditlocationDetails = new Intent(getApplicationContext(), EditLocationActivity.class);
                startActivity(moveEditlocationDetails);
            }
        });

        imgeditFamilyDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent moveFamilyDetails = new Intent(getApplicationContext(), FamilyDetailsActivity.class);
                startActivity(moveFamilyDetails);
            }
        });


        imgLocationpref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveEditLocationPref = new Intent(getApplicationContext(), LocationPrefActivity.class);
                startActivity(moveEditLocationPref);
            }
        });

        imgHabitspref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveHabitspref = new Intent(getApplicationContext(), HabitsPrefActivity.class);
                startActivity(moveHabitspref);
            }
        });
        imgHabitsandhobbies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveHobbiesAndInterest = new Intent(getApplicationContext(), HobbiesAndInterests.class);
                startActivity(moveHobbiesAndInterest);
            }
        });

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbarLayout.setTitle("Edit Profile");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ShowBasicFamilyDetails();
        getRelegiousValues();
        getdata();

    }

    private void getdata() {

        firestore.collection("users")
                .whereEqualTo("userId", Uid).
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        String aboutmySelf = document.getString("aboutMe");
                        String citizenship = document.getString("citizenship");
                        String city = document.getString("city");
                        String clan = document.getString("clan");
                        String country = document.getString("country");
                        String dob = document.getString("dob");
                        String education = document.getString("education");
                        String employedIn = document.getString("employedIn");
                        String ethnicity = document.getString("ethnicity");
                        String gender = document.getString("gender");
                        String height = document.getString("height");
                        String maritalstatus = document.getString("maritalStatus");
                        String motherTongue = document.getString("motherTongue");
                        String name = document.getString("name");
                        String phone = document.getString("phone");
                        String physicalStatus = document.getString("Normal");
                        String profileCreator = document.getString("profileCreator");
                        String relegion = document.getString("relegion");
                        String relegiousValue = document.getString("relegiousValue");
                        String salary = document.getString("salary");
                        String state = document.getString("state");
                        String occupation = document.getString("occupation");
                        String habitsAndInterests=document.getString("hobbiesAndInterest");
                        String favouriteFood=document.getString("favouriteFood");
                        String favouriteMusic=document.getString("favouriteMusic");
                        String favouriteSports=document.getString("favouriteSports");

                        //Toast.makeText(getContext(), " id" + uid, Toast.LENGTH_SHORT).show();
                        tvAboutmyself.setText(aboutmySelf);
                        tvname.setText(name);
                        dateOfBirth.setText(dob);
                        tvProfileCreated.setText(profileCreator);
                        tvGender.setText(gender);
                        tvHeight.setText(height);
                        tvMaritalstatus.setText(maritalstatus);
                        tvMothertongue.setText(motherTongue);
                        tvPhysicalStatus.setText(physicalStatus);
                        tvEducation.setText(education);
                        tvEmployed.setText(employedIn);
                        tvOccupation.setText(occupation);
                        tvAnnualIncome.setText(salary);
                        tvHobbiesAndInterest.setText(habitsAndInterests);
                        tvFavouriteFood.setText(favouriteFood);
                        tvFavouriteMusic.setText(favouriteMusic);
                        tvFavouriteSports.setText(favouriteSports);


                    }
                    getHabitsDetails();

                    getdataProfessionalInformation();
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

    // get professional data

    private void getdataProfessionalInformation() {

        firestore.collection("users")
                .whereEqualTo("userId", Uid).
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {


                        String education = document.getString("education");
                        String employedIn = document.getString("employedIn");
                        String salary = document.getString("salary");
                        String occupation = document.getString("occupation");

                        tvEducation.setText(education);
                        tvEmployed.setText(employedIn);
                        tvOccupation.setText(occupation);
                        tvAnnualIncome.setText(salary);

                    }
                    GetLocation();
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

    //getLocation
    private void GetLocation() {

        firestore.collection("users")
                .whereEqualTo("userId", Uid).
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {


                        String country = document.getString("country");
                        String citizenship = document.getString("citizenShip");
                        String state = document.getString("state");
                        String city = document.getString("city");

                        tvCountry.setText(country);
                        tvCitizenShip.setText(citizenship);
                        tvState.setText(state);
                        tvCity.setText(city);

                    }
                    GetRelegiousInformation();
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

    // get Relegious Information

    private void GetRelegiousInformation() {

        firestore.collection("users")
                .whereEqualTo("userId", Uid).
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        String relegion = document.getString("religion");
                        String religiousvaluess = document.getString("religiousValue");
                        String clan = document.getString("clan");

                        tvRelegion.setText(relegion);
                        tvRelegiousValues.setText(religiousvaluess);
                        tvClan.setText(clan);

                    }

                    //ShowBasicDetailsPref();

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


    private void ShowBasicFamilyDetails(){

        firestore.collection("users")
                .whereEqualTo("userId", Uid).
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        fathersOccupation = document.getString("FatherProfession");
                         mothersOccupation = document.getString("MothersProfession");
                        familyOrigin = document.getString("FamilyOrigin");
                         noOfBrothers = document.getString("NumberOfBrothers");
                         noOfSisters = document.getString("NumbersOfSisters");

                        tvFatherOccupation.setText(fathersOccupation);
                        tvMotherOccupation.setText(mothersOccupation);
                        tvFamilyOrigin.setText(familyOrigin);
                        tvNumberOfBrothers.setText(noOfBrothers);
                        tvNumberOfSisters.setText(noOfSisters);

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


    private void AddRelegiousValues(String relegiousValues) {

        Map<String, Object> userDescMap = new HashMap<>();
        userDescMap.put("RelegiousValues", relegiousValues);

        firestore.collection("users").document(uId).collection("Preferrences").document(uId)
                .update(userDescMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getApplicationContext(), "Relegious Values Updated", Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }

    private void EditPhone(String phone) {


        Map<String, Object> editPhoneMap = new HashMap<>();
        editPhoneMap.put("phone", phone);

        firestore.collection("users").document(uId)
                .update(editPhoneMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getApplicationContext(), "Phone Number Updated", Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }


    private void EditMail(String mail) {


        Map<String, Object> editMailMap = new HashMap<>();
        editMailMap.put("email", mail);

        firestore.collection("users").document(uId)
                .update(editMailMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getApplicationContext(), "email address updated", Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }

    private void getMail() {

        firestore.collection("users").document(uId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                editMail = task.getResult().getString("email");

                edtMail.setText(editMail);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void getPhone() {


        firestore.collection("users").document(uId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                editPhone = task.getResult().getString("phone");

                edtPhone.setText(editPhone);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void getHabitsDetails() {


        firestore.collection("users").document(uId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isComplete()) {
                    if (task.getResult().exists()) {

                    } else {

                    }
                    eatingHabits = task.getResult().getString("eatingHabits");
                    drinkingHabits = task.getResult().getString("drinkingHabits");
                    smokingHabits = task.getResult().getString("smokingHabits");

                    tvEatingHabits.setText(eatingHabits);
                    tvDrinkingHabits.setText(drinkingHabits);
                    tvSmokingHabits.setText(smokingHabits);

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void getRelegiousValues() {


        firestore.collection("users").document(uId).collection("Preferrences").document(uId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isComplete()) {
                    if (task.getResult().exists()) {

                        String descPref = task.getResult().getString("PartnerDescription");
                        String profileCreatorPref = task.getResult().getString("profileCreatedFor");
                        String agepref = task.getResult().getString("age");
                        String motherTonguepref = task.getResult().getString("motherTongue");
                        String physicalStatuspref = task.getResult().getString("physicalStatus");
                        String heightpref = task.getResult().getString("Height");
                        String RelegiousPref = task.getResult().getString("relegiousValue");
                        String RelegionPref = task.getResult().getString("religion");
                        String RelegionSectPref = task.getResult().getString("clan");
                        String occupationPref = task.getResult().getString("Occupation");
                        String educationpref = task.getResult().getString("education");
                        String AnnualIncomepref = task.getResult().getString("income");
                        String countryPref = task.getResult().getString("Country");
                        String citizenshipPref = task.getResult().getString("Citizenship");
                        String residentStatusPref = task.getResult().getString("ResidentStatus");
                        String eatingPref = task.getResult().getString("Eating");
                        String smokingPref = task.getResult().getString("Smoking");
                        String drinkingPref = task.getResult().getString("Drinking");



                        tvProfileCreatorPref.setText(profileCreatorPref);
                        tvAgePref.setText(agepref);
                        tvMotherTonguePref.setText(motherTonguepref);
                        tvPhysicalStatusPref.setText(physicalStatuspref);
                        tvHeightPref.setText(heightpref);
                        tvRelegionPref.setText(RelegiousPref);
                        getTvRelegionPref.setText(RelegionPref);
                        tvrelegionSect.setText(RelegionSectPref);
                        tvOccupationPref.setText(occupationPref);
                        tvEducationpref.setText(educationpref);
                        tvAnnualIncomePref.setText(AnnualIncomepref);
                        tvCountryPref.setText(countryPref);
                        tvCitizenShipPrf.setText(citizenshipPref);
                        tvResidentStatusPrf.setText(residentStatusPref);
                        tvEatingHabitsPref.setText(eatingPref);
                        tvDrinkingHabitsPref.setText(drinkingPref);
                        tvSmokingHAbitsPref.setText(smokingPref);
                        tvDescPref.setText(descPref);



                    } else {

                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}