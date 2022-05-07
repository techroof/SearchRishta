package com.techroof.searchrishta.EditProfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.techroof.searchrishta.Preferences.HabitsPrefActivity;
import com.techroof.searchrishta.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HobbiesAndInterests extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    TextInputLayout hobbiesAndInterest_Et, music_et, sports_et, food_et;
    TextInputLayout eatingHabits_Et, drinkingHabits_et, smokinghabits_et;
    Button btnHobbiesAndInterest, btnHabits;
    String[] hobbiesList, musicList, sportsList, foodList, eatingList, drinkingList, smokingList;
    String hobbiesAndInterest, music, sports, food, eatingHabits, drinkingHabits, smokingHabits, uId;
    private ImageView imgBack;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hobbies_and_interests);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        uId = firebaseAuth.getCurrentUser().getUid();

        hobbiesAndInterest_Et = findViewById(R.id.et_hobbies_and_interests);
        music_et = findViewById(R.id.et_music);
        sports_et = findViewById(R.id.et_sports);
        food_et = findViewById(R.id.et_food);
        eatingHabits_Et = findViewById(R.id.et_eating_habits);
        drinkingHabits_et = findViewById(R.id.et_drinking_habits);
        smokinghabits_et = findViewById(R.id.et_smoking_habits);
        //btnHobbiesAndInterest = findViewById(R.id.btn_update_hobbies_and_interests);
        btnHabits = findViewById(R.id.btn_update_habits_details);

        eatingList = getResources().getStringArray(R.array.eating);
        drinkingList = getResources().getStringArray(R.array.drinker);
        smokingList = getResources().getStringArray(R.array.smoker);

        hobbiesList = getResources().getStringArray(R.array.hobbiesAndInterest);
        musicList = getResources().getStringArray(R.array.Music);
        sportsList = getResources().getStringArray(R.array.Sports);
        foodList = getResources().getStringArray(R.array.Food);

        imgBack=findViewById(R.id.edit_profile_back_btn);

        final boolean[] checkedItems = new boolean[hobbiesList.length];
        final List<String> selectedItems = Arrays.asList(hobbiesList);

        final boolean[] checkedItemsmusic = new boolean[musicList.length];
        final List<String> selectedItemsmusic = Arrays.asList(musicList);

        final boolean[] checkedItemssports = new boolean[sportsList.length];
        final List<String> selectedItemssports = Arrays.asList(sportsList);

        final boolean[] checkedItemssfood = new boolean[foodList.length];
        final List<String> selectedItemsfood = Arrays.asList(foodList);

        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent moveEditProfile=new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(moveEditProfile);
                finish();
            }
        });

        getHabitsDetails();

        hobbiesAndInterest_Et.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hobbiesAndInterest_Et.getEditText().getText().clear();

                AlertDialog.Builder builder = new AlertDialog.Builder(HobbiesAndInterests.this);

                builder.setTitle("Select hobbies and interest");

                builder.setMultiChoiceItems(hobbiesList, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checkedItems[which] = isChecked;
                        String currentItem = selectedItems.get(which);
                    }
                });

                builder.setCancelable(false);

                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    //@SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            if (checkedItems[i]) {

                                hobbiesAndInterest_Et.getEditText().setText(hobbiesAndInterest_Et.getEditText().getText() + selectedItems.get(i) + ", ");


                            }

                           /* int length = hobbiesAndInterest_Et.getText().length();
                            if (length > 0) {


                            }*/
                        }

                        int lengthhobbies = hobbiesAndInterest_Et.getEditText().getText().length();
                        if (lengthhobbies > 0) {
                            hobbiesAndInterest_Et.getEditText().getText().delete(lengthhobbies - 2, lengthhobbies);
                        }



                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setNeutralButton("CLEAR ALL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            checkedItems[i] = false;
                        }

                        hobbiesAndInterest_Et.getEditText().setText("");
                    }
                });

                builder.create();

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


        music_et.getEditText().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                music_et.getEditText().getText().clear();

                AlertDialog.Builder builder = new AlertDialog.Builder(HobbiesAndInterests.this);

                builder.setTitle("Select favourite music");

                builder.setMultiChoiceItems(musicList, checkedItemsmusic, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checkedItemsmusic[which] = isChecked;
                        String currentItem = selectedItemsmusic.get(which);
                    }
                });

                builder.setCancelable(false);

                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    //@SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < checkedItemsmusic.length; i++) {
                            if (checkedItemsmusic[i]) {
                                music_et.getEditText().setText(music_et.getEditText().getText() + selectedItemsmusic.get(i) + ", ");
                            }


                           /* int length = music_et.getText().length();
                            if (length > 0) {
                                music_et.getText().delete(length - 1, length);
                            }*/
                        }
                        int length = music_et.getEditText().getText().length();
                        if (length > 0) {
                            music_et.getEditText().getText().delete(length - 2, length);
                        }
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setNeutralButton("CLEAR ALL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < checkedItemsmusic.length; i++) {
                            checkedItemsmusic[i] = false;
                        }
                        music_et.getEditText().getText().clear();

                    }

                });

                builder.create();

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        sports_et.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sports_et.getEditText().getText().clear();

                AlertDialog.Builder builder = new AlertDialog.Builder(HobbiesAndInterests.this);

                builder.setTitle("Select favourite sports");

                builder.setMultiChoiceItems(sportsList, checkedItemssports, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checkedItemssports[which] = isChecked;
                        String currentItem = selectedItemssports.get(which);
                    }
                });

                builder.setCancelable(false);

                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    //@SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < checkedItemssports.length; i++) {
                            if (checkedItemssports[i]) {
                                sports_et.getEditText().setText(sports_et.getEditText().getText() + selectedItemssports.get(i) + ", ");
                            }


                        }
                        /*int length = sports_et.getText().length();
                        if (length > 0) {
                            sports_et.getText().delete(length - 1, length);
                        }*/

                        int lengthsports = sports_et.getEditText().getText().length();
                        if (lengthsports > 0) {
                            sports_et.getEditText().getText().delete(lengthsports - 2, lengthsports);
                        }
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setNeutralButton("CLEAR ALL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < checkedItemssports.length; i++) {
                            checkedItemssports[i] = false;
                        }
                        sports_et.getEditText().getText().clear();

                    }
                });

                builder.create();

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        food_et.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                food_et.getEditText().getText().clear();

                AlertDialog.Builder builder = new AlertDialog.Builder(HobbiesAndInterests.this);

                builder.setTitle("Select favourite food");

                builder.setMultiChoiceItems(foodList, checkedItemssfood, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checkedItemssfood[which] = isChecked;
                        String currentItem = selectedItemsfood.get(which);
                    }
                });

                builder.setCancelable(false);

                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    //@SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < checkedItemssfood.length; i++) {
                            if (checkedItemssfood[i]) {

                                food_et.getEditText().setText(food_et.getEditText().getText() + selectedItemsfood.get(i) + ", ");
                            }
                        }
                        int length = food_et.getEditText().getText().length();
                        if (length > 0) {
                            food_et.getEditText().getText().delete(length - 2, length);
                        }
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setNeutralButton("CLEAR ALL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < checkedItemssfood.length; i++) {
                            checkedItemssfood[i] = false;
                        }

                        food_et.getEditText().getText().clear();

                    }
                });

                builder.create();

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        smokinghabits_et.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(HobbiesAndInterests.this).setTitle("Select your smoking habits")
                        .setSingleChoiceItems(smokingList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                smokinghabits_et.getEditText().setText(smokingList[selectedPosition]);
                                smokingHabits = smokingList[selectedPosition];
                            }
                        })
                        .show();
            }
        });

        drinkingHabits_et.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(HobbiesAndInterests.this).setTitle("Select your drinking habits")
                        .setSingleChoiceItems(drinkingList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                drinkingHabits_et.getEditText().setText(drinkingList[selectedPosition]);
                                drinkingHabits = drinkingList[selectedPosition];
                            }
                        })
                        .show();
            }
        });

        eatingHabits_Et.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new AlertDialog.Builder(HobbiesAndInterests.this).setTitle("Select your eating habits")
                        .setSingleChoiceItems(eatingList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                eatingHabits_Et.getEditText().setText(eatingList[selectedPosition]);
                                eatingHabits = eatingList[selectedPosition];
                            }
                        })
                        .show();
            }
        });

        /*btnHobbiesAndInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



               *//* hobbiesAndInterest = hobbiesAndInterest_Et.getText().toString();
                food = food_et.getText().toString();
                sports = sports_et.getText().toString();
                music = music_et.getText().toString();*//*


            }


        });
*/
        btnHabits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(eatingHabits_Et.getEditText().getText().toString())) {

                    eatingHabits_Et.getEditText().setError("enter your eating habits");

                }

                if (TextUtils.isEmpty(drinkingHabits_et.getEditText().getText().toString())) {

                    drinkingHabits_et.getEditText().setError("Enter your drinking habits");

                }

                if (TextUtils.isEmpty(smokinghabits_et.getEditText().getText().toString())) {

                    smokinghabits_et.setError("Enter your smoking habits");

                }


                if (TextUtils.isEmpty(hobbiesAndInterest_Et.getEditText().getText().toString())) {

                    hobbiesAndInterest_Et.setError("enter your hobbies and interest");

                }

                if (TextUtils.isEmpty(food_et.getEditText().getText().toString())) {

                    food_et.setError("Enter your favourite food");

                }

                if (TextUtils.isEmpty(sports_et.getEditText().getText().toString())) {

                    sports_et.setError("Enter your favourite sport");

                }

                if (TextUtils.isEmpty(music_et.getEditText().getText().toString())) {

                    music_et.setError("Enter your favourite music");

                }



                if (!TextUtils.isEmpty(hobbiesAndInterest_Et.getEditText().getText().toString()) && !TextUtils.isEmpty(food_et.getEditText().getText().toString())
                        && !TextUtils.isEmpty(sports_et.getEditText().getText().toString()) && !TextUtils.isEmpty(music_et.getEditText().getText().toString()
                )){




                    hobbiesAndInterest=hobbiesAndInterest_Et.getEditText().getText().toString();
                    //hobbiesAndInterest = hobbiesAndInterest_Et.getText().toString().substring(0,hobbiesAndInterest_Et.getText().toString().length()-2);
                    food = food_et.getEditText().getText().toString();
                    sports = sports_et.getEditText().getText().toString();
                    music = music_et.getEditText().getText().toString();


                    UpdateHobbiesAndInterest(hobbiesAndInterest, music, food, sports);


                }else{


                    Toast.makeText(getApplicationContext(), "Please enter above fields", Toast.LENGTH_SHORT).show();
                }




                if (!TextUtils.isEmpty(eatingHabits_Et.getEditText().getText().toString()) && !TextUtils.isEmpty(drinkingHabits_et.getEditText().getText().toString())
                        && !TextUtils.isEmpty(smokinghabits_et.getEditText().getText().toString())){


                    UpdateHabits(eatingHabits, drinkingHabits, smokingHabits);



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

    private void UpdateHobbiesAndInterest(String hobbiesAndInterst, String music, String food, String sports) {


        Map<String, Object> userlocationDetailprefMap = new HashMap<>();
        userlocationDetailprefMap.put("hobbiesAndInterest", hobbiesAndInterst);
        userlocationDetailprefMap.put("favouriteMusic", music);
        userlocationDetailprefMap.put("favouriteSports", food);
        userlocationDetailprefMap.put("favouriteFood", sports);

        firestore.collection("users").document(uId)
                .update(userlocationDetailprefMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getApplicationContext(), "Hobbies and  updated successfully", Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }

    private void UpdateHabits(String eatingHabits, String drinkingHabits, String smokingHabits) {

        Map<String, Object> userlocationDetailprefMap = new HashMap<>();
        userlocationDetailprefMap.put("eatingHabits", eatingHabits);
        userlocationDetailprefMap.put("drinkingHabits", drinkingHabits);
        userlocationDetailprefMap.put("smokingHabits", smokingHabits);

        firestore.collection("users").document(uId)
                .update(userlocationDetailprefMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getApplicationContext(), "habits updated successfully", Toast.LENGTH_LONG).show();

                Intent moveEditProfile=new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(moveEditProfile);
                //finish();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }


    public void getHabitsDetails() {


        firestore.collection("users").document(uId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isComplete()) {
                    if (task.getResult().exists()) {

                        pd.dismiss();
                    } else {

                        pd.dismiss();
                    }
                    eatingHabits = task.getResult().getString("eatingHabits");
                    drinkingHabits = task.getResult().getString("drinkingHabits");
                    smokingHabits = task.getResult().getString("smokingHabits");

                    eatingHabits_Et.getEditText().setText(eatingHabits);
                    drinkingHabits_et.getEditText().setText(drinkingHabits);
                    smokinghabits_et.getEditText().setText(smokingHabits);

                    getHobbiesAndInterest();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                pd.dismiss();
            }
        });

    }

    public void getHobbiesAndInterest() {


        firestore.collection("users").document(uId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isComplete()) {
                    if (task.getResult().exists()) {

                        pd.dismiss();
                    } else {

                        pd.dismiss();
                    }
                    hobbiesAndInterest = task.getResult().getString("hobbiesAndInterest");
                    music = task.getResult().getString("favouriteMusic");
                    sports = task.getResult().getString("favouriteSports");
                    food = task.getResult().getString("favouriteFood");


                   hobbiesAndInterest_Et.getEditText().setText(hobbiesAndInterest);
                    music_et.getEditText().setText(music);
                    sports_et.getEditText().setText(sports);
                    food_et.getEditText().setText(food);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                pd.dismiss();
            }
        });


    }


}