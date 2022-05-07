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

public class EditProfessionalInformationActivity extends AppCompatActivity {

    private TextInputLayout etEducation, etEmployedIn, etOccupation, etIncome;
    private String[] educationList, employedInList, occupationList;
    private String education, employedIn, occupation, income,uId;
    private Button btnUpdateProffesional;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private ImageView imgBack;
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_professional_information);
        etEducation = findViewById(R.id.et_edit_education);
        etEmployedIn = findViewById(R.id.et_edit_employedIn);
        etOccupation = findViewById(R.id.et_edit_occupation);
        etIncome = findViewById(R.id.et_edit_income);
        btnUpdateProffesional = findViewById(R.id.btn_edit_professional_details);
        educationList = getResources().getStringArray(R.array.Education);
        employedInList = getResources().getStringArray(R.array.employed_in);
        occupationList = getResources().getStringArray(R.array.Occupation);
        imgBack=findViewById(R.id.edit_profile_back_btn);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        uId=firebaseAuth.getCurrentUser().getUid();

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

        etEducation.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(EditProfessionalInformationActivity.this).setTitle("Select Your Education")
                        .setSingleChoiceItems(educationList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etEducation.getEditText().setText(educationList[selectedPosition]);
                                education = educationList[selectedPosition];
                            }
                        })
                        .show();

            }
        });

        etEmployedIn.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(EditProfessionalInformationActivity.this).setTitle("Select Your EmployedStatus")
                        .setSingleChoiceItems(employedInList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etEmployedIn.getEditText().setText(employedInList[selectedPosition]);
                                employedIn = employedInList[selectedPosition];
                            }
                        })
                        .show();
            }
        });

        etOccupation.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(EditProfessionalInformationActivity.this).setTitle("Select Your Occupation")
                        .setSingleChoiceItems(occupationList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etOccupation.getEditText().setText(occupationList[selectedPosition]);
                                occupation = occupationList[selectedPosition];
                            }
                        })
                        .show();
            }
        });

        etIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                income = etIncome.getEditText().getText().toString();
            }
        });

        btnUpdateProffesional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(etEducation.getEditText().getText().toString())) {

                    etEducation.setError("enter your education");

                }

                if (TextUtils.isEmpty(etEmployedIn.getEditText().getText().toString())) {

                    etEmployedIn.setError("Enter your employing sector");

                }

                if (TextUtils.isEmpty(etOccupation.getEditText().getText().toString())) {

                    etOccupation.setError("Enter your occupation");

                }

                if (TextUtils.isEmpty(etIncome.getEditText().getText().toString())) {

                    etIncome.setError("Enter your income");

                }



                if (!TextUtils.isEmpty(etEducation.getEditText().getText().toString()) && !TextUtils.isEmpty(etEmployedIn.getEditText().getText().toString())
                        && !TextUtils.isEmpty(etOccupation.getEditText().getText().toString()) && !TextUtils.isEmpty(etIncome.getEditText().getText().toString()
                )){


                    UpdateProfessionalInformation(education, employedIn, occupation, income);



                }else{


                    Toast.makeText(getApplicationContext(), "Please enter above fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

        getProfessionalInformation();
    }

    @Override
    public void onBackPressed() {
        Intent moveEditProfile =new Intent(getApplicationContext(), EditProfileActivity.class);
        startActivity(moveEditProfile);
        finish();
    }

    private void UpdateProfessionalInformation(String education, String employedIn, String occupation, String income) {

        Map<String, Object> userprofessionalDetailprefMap = new HashMap<>();
        userprofessionalDetailprefMap.put("education", education);
        userprofessionalDetailprefMap.put("employedIn", employedIn);
        userprofessionalDetailprefMap.put("occupation", occupation);
        userprofessionalDetailprefMap.put("salary", income);


        firestore.collection("users").document(uId)
                .update(userprofessionalDetailprefMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getApplicationContext(), "Professional Information Updated Successfully", Toast.LENGTH_LONG).show();

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

    private void getProfessionalInformation(){




        firestore.collection("users").document(uId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isComplete()) {
                    if (task.getResult().exists()) {

                        pd.dismiss();
                    }
                    education = task.getResult().getString("education");
                    employedIn = task.getResult().getString("employedIn");
                    occupation = task.getResult().getString("occupation");
                    income = task.getResult().getString("salary");

                    etEducation.getEditText().setText(education);
                    etEmployedIn.getEditText().setText(employedIn);
                    etOccupation.getEditText().setText(occupation);
                    etIncome.getEditText().setText(income);

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


}
