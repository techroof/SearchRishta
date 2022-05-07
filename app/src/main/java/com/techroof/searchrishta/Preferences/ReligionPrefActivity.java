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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.techroof.searchrishta.EditProfile.RelegiousInformationActivity;
import com.techroof.searchrishta.EditProfileActivity;
import com.techroof.searchrishta.R;

import java.util.HashMap;
import java.util.Map;

public class ReligionPrefActivity extends AppCompatActivity {

    TextInputLayout etRelegion, etRelegiousValues, etRelegiousSect;
    Button btnEditRelegiousValues;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    String relegion, relegiousValues, relegiousSect, uId;
    private String[] relegionList, relegiousvaluesList, relegionsectIslamList, relegionSectChristianList, relegionSectHinduList, relegionSectParsiList;
    private ImageView imgBack;
    private ProgressDialog pd;
    private String prefExist="null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_religion_pref);

        etRelegion = findViewById(R.id.et_edit_relegion);
        etRelegiousValues = findViewById(R.id.et_relegious_values);
        etRelegiousSect = findViewById(R.id.et_edit_sect);
        btnEditRelegiousValues = findViewById(R.id.btn_edit_relegious_values);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        uId = firebaseAuth.getCurrentUser().getUid();

        relegionList = getResources().getStringArray(R.array.Relegion);
        relegiousvaluesList = getResources().getStringArray(R.array.RelegiousValues);
        relegionsectIslamList = getResources().getStringArray(R.array.sectIslam);
        relegionSectChristianList = getResources().getStringArray(R.array.ClanChristian);
        relegionSectHinduList = getResources().getStringArray(R.array.ClanHindu);
        relegionSectParsiList = getResources().getStringArray(R.array.ClanParsi);

        imgBack = findViewById(R.id.edit_profile_back_btn);

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



        getRelegiousValues();


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent moveEditProfile = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(moveEditProfile);
                finish();

            }
        });

        etRelegion.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ReligionPrefActivity.this).setTitle("Select Your Relegious Information")
                        .setSingleChoiceItems(relegionList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etRelegion.getEditText().setText(relegionList[selectedPosition]);
                                relegion = relegionList[selectedPosition];
                            }
                        })
                        .show();

            }

        });

        etRelegiousValues.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(ReligionPrefActivity.this).setTitle("Select Your Relegious Values")
                        .setSingleChoiceItems(relegiousvaluesList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etRelegiousValues.getEditText().setText(relegiousvaluesList[selectedPosition]);
                                relegiousValues = relegiousvaluesList[selectedPosition];

                                etRelegiousSect.getEditText().getText().clear();
                            }
                        })
                        .show();
            }
        });

        etRelegiousSect.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (relegion == null) {

                    Toast.makeText(getApplicationContext(), "please fill above fields", Toast.LENGTH_SHORT).show();

                } else if (relegion.equals("Christian")) {

                    new AlertDialog.Builder(ReligionPrefActivity.this).setTitle("Select Your Sect")
                            .setSingleChoiceItems(relegionSectChristianList, 0, null)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {

                                    dialog.dismiss();

                                    int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                    etRelegiousSect.getEditText().setText(relegionSectChristianList[selectedPosition]);
                                    relegiousSect = relegionSectChristianList[selectedPosition];
                                }
                            })
                            .show();


                } else if (relegion.equals("Muslim-Abbasi") || relegion.equals("Muslim-Brailvi")
                        || relegion.equals("Muslim-Deobandi") || relegion.equals("Muslim-Others")
                        || relegion.equals("Muslim-Shia") || relegion.equals("Muslim-Ahle Hadith") ||
                        relegion.equals("Muslim-Sunnis")) {

                    new AlertDialog.Builder(ReligionPrefActivity.this).setTitle("Select Your Sect")
                            .setSingleChoiceItems(relegionsectIslamList, 0, null)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {

                                    dialog.dismiss();

                                    int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                    etRelegiousSect.getEditText().setText(relegionsectIslamList[selectedPosition]);
                                    //strClanislam = relegionList[selectedPosition];
                                    relegiousSect = relegionsectIslamList[selectedPosition];

                                    // Toast.makeText(getApplicationContext(), ""+strClanislam, Toast.LENGTH_SHORT).show();
                                }
                            })
                            .show();

                } else if (relegion.equals("Hindu")) {

                    new AlertDialog.Builder(ReligionPrefActivity.this).setTitle("Select Your Sect")
                            .setSingleChoiceItems(relegionSectHinduList, 0, null)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {

                                    dialog.dismiss();

                                    int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                    etRelegiousSect.getEditText().setText(relegionSectHinduList[selectedPosition]);
                                    relegiousSect = relegionSectHinduList[selectedPosition];

                                    //strClanhindu = relegionList[selectedPosition];
                                }
                            })
                            .show();


                } else if (relegion.equals("Parsi")) {

                    new AlertDialog.Builder(ReligionPrefActivity.this).setTitle("Select Your Sect")
                            .setSingleChoiceItems(relegionSectParsiList, 0, null)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {

                                    dialog.dismiss();

                                    int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                    etRelegiousSect.getEditText().setText(relegionSectParsiList[selectedPosition]);
                                    // strClanparsi = relegionList[selectedPosition];
                                    relegiousSect = relegionSectParsiList[selectedPosition];

                                }
                            })
                            .show();


                }
            }
        });

        btnEditRelegiousValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(etRelegion.getEditText().getText().toString())) {

                    etRelegion.setError("enter your religion");

                }

                if (TextUtils.isEmpty(etRelegiousSect.getEditText().getText().toString())) {

                    etRelegiousSect.setError("Enter your religious sect");

                }

                if (TextUtils.isEmpty(etRelegiousValues.getEditText().getText().toString())) {

                    etRelegiousValues.setError("Enter your religious values");

                }

                if (!TextUtils.isEmpty(etRelegion.getEditText().getText().toString()) && !TextUtils.isEmpty(etRelegiousSect.getEditText().getText().toString())
                        && !TextUtils.isEmpty(etRelegiousValues.getEditText().getText().toString())) {

                    if(prefExist.equals("yes")){

                        UpdateRelegiousValues(relegion, relegiousSect, relegiousValues);


                    }else if(prefExist.equals("no")){

                        SetRelegiousValues(relegion, relegiousSect, relegiousValues);


                    }else{


                        Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                    }

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

    private void UpdateRelegiousValues(String Relegion, String RelegiousSect, String RelegiousValues) {

        Map<String, Object> userbasicDetailprefMap = new HashMap<>();
        userbasicDetailprefMap.put("religion", Relegion);
        userbasicDetailprefMap.put("clan", RelegiousSect);
        userbasicDetailprefMap.put("relegiousValue", RelegiousValues);

        firestore.collection("users").document(uId).collection("Preferrences").document(uId).update(userbasicDetailprefMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getApplicationContext(), "Religious Details Updated Successfully", Toast.LENGTH_LONG).show();

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


    private void getRelegiousValues() {


        firestore.collection("users").document(uId).collection("Preferrences").document(uId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isComplete()) {
                    if (task.getResult().exists()) {

                        pd.dismiss();
                    } else {

                        pd.dismiss();
                    }
                    relegion = task.getResult().getString("religion");
                    relegiousSect = task.getResult().getString("clan");
                    relegiousValues = task.getResult().getString("relegiousValue");

                    etRelegion.getEditText().setText(relegion);
                    etRelegiousSect.getEditText().setText(relegiousSect);
                    etRelegiousValues.getEditText().setText(relegiousValues);

                    pd.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                pd.dismiss();

                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void SetRelegiousValues(String Relegion, String RelegiousSect, String RelegiousValues) {

        Map<String, Object> userbasicDetailprefMap = new HashMap<>();
        userbasicDetailprefMap.put("religion", Relegion);
        userbasicDetailprefMap.put("clan", RelegiousSect);
        userbasicDetailprefMap.put("relegiousValue", RelegiousValues);

        firestore.collection("users").document(uId).collection("Preferrences").document(uId).set(userbasicDetailprefMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getApplicationContext(), "Religious Details Updated Successfully", Toast.LENGTH_LONG).show();

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