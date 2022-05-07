package com.techroof.searchrishta.EditProfile;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

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
import com.techroof.searchrishta.Interfaces.JsonPlaceHolderAPI;
import com.techroof.searchrishta.Model.City;
import com.techroof.searchrishta.Model.CountryStateCity;
import com.techroof.searchrishta.Model.States;
import com.techroof.searchrishta.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditLocationActivity extends AppCompatActivity {

    private String BASE_URL = "https://api.countrystatecity.in/v1/";
    TextInputLayout etCountry,etCitizenship,etCity,etState;
    Button btnEditLocation;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    String country,citizenShip,city,state,uId;
    private String[] countryList, citizenShipList, cityList,stateList;
    private ImageView imageView;
    private ProgressDialog pd;
    private List<CountryStateCity> countryArraylist;
    private List<States> statesList;
    private List<String> countryNameList;
    private List<String> statesnameList;
    private List<String> cityNameList;
    private List<City> cityArrayList;


    private  int countryId;
    private String stateId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);

        etCountry=findViewById(R.id.et_edit_Country);
        etCitizenship=findViewById(R.id.et_edit_CitizenShip);
        etState=findViewById(R.id.et_edit_state);
        etCity=findViewById(R.id.et_edit_city);
        btnEditLocation=findViewById(R.id.btn_edit_location_details);
        imageView=findViewById(R.id.edit_profile_back_btn);
        firestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        uId=firebaseAuth.getCurrentUser().getUid();

        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        countryList=getResources().getStringArray(R.array.countries);
        citizenShipList=getResources().getStringArray(R.array.citizenship);


        getCountries();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent moveEditProfile =new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(moveEditProfile);
                finish();
            }
        });

        etCountry.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(EditLocationActivity.this).setTitle("Select Your Country")
                        .setSingleChoiceItems(countryNameList.toArray(new String[countryNameList.size()]), 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etCountry.getEditText().setText(countryNameList.get(selectedPosition));
                                country = countryNameList.get(selectedPosition);
                                countryId=countryArraylist.get(selectedPosition).getId();

                                statesnameList=new ArrayList<>();
                                statesList=new ArrayList<>();

                                getStates(countryId);
                            }


                        })
                        .show();
            }

        });

        etState.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(country)) {

                    etState.setEnabled(false);

                    Toast.makeText(getApplicationContext(), "Please select your country first", Toast.LENGTH_SHORT).show();

                }else {

                    new AlertDialog.Builder(EditLocationActivity.this).setTitle("Select Your State")
                            .setSingleChoiceItems(statesnameList.toArray(new String[statesnameList.size()]), 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {

                                    dialog.dismiss();

                                    int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                    etState.getEditText().setText(statesnameList.get(selectedPosition));
                                    state = statesnameList.get(selectedPosition);

                                    stateId= statesList.get(selectedPosition).getIso2();

                                    cityNameList=new ArrayList<>();
                                    cityArrayList=new ArrayList<>();
                                    getCities(countryId,stateId);
                                }


                            })
                            .show();

                }
            }

        });

        etCitizenship.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(EditLocationActivity.this).setTitle("Select Your Nationality")
                        .setSingleChoiceItems(citizenShipList, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dialog.dismiss();

                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                etCitizenship.getEditText().setText(citizenShipList[selectedPosition]);
                            }
                        })
                        .show();

            }

        });

        etCity.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(country)&&TextUtils.isEmpty(state)) {

                    etCity.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "please select your country and state first", Toast.LENGTH_SHORT).show();

                }else {

                    new AlertDialog.Builder(EditLocationActivity.this).setTitle("Select Your City")
                            .setSingleChoiceItems(cityNameList.toArray(new String[cityNameList.size()]), 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    //Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {

                                    dialog.dismiss();

                                    int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                    etCity.getEditText().setText(cityNameList.get(selectedPosition));
                                    city = cityNameList.get(selectedPosition);
                                }
                            })
                            .show();
                }}

        });





        btnEditLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(etCountry.getEditText().getText().toString())) {

                    etCountry.setError("Enter your country");

                }

                if (TextUtils.isEmpty(etCitizenship.getEditText().getText().toString())) {

                    etCitizenship.setError("Enter your citizenship");

                }

                if (TextUtils.isEmpty(etState.getEditText().getText().toString())) {

                    etState.setError("Enter your state");

                }

                if (TextUtils.isEmpty(etCity.getEditText().getText().toString())) {

                    etCity.setError("Enter your city");

                }



                if (!TextUtils.isEmpty(etCountry.getEditText().getText().toString()) && !TextUtils.isEmpty(etCitizenship.getEditText().getText().toString())
                        && !TextUtils.isEmpty(etState.getEditText().getText().toString()) && !TextUtils.isEmpty(etCity.getEditText().getText().toString()
                )){



                    UpdateLocation(country,citizenShip,state,city);


                }else{


                    Toast.makeText(getApplicationContext(), "Please enter above fields", Toast.LENGTH_SHORT).show();
                }




            }
        });

        getLocationn();


    }

    @Override
    public void onBackPressed() {
        Intent moveEditProfile =new Intent(getApplicationContext(), EditProfileActivity.class);
        startActivity(moveEditProfile);
        finish();

    }

    private void UpdateLocation(String Country, String CitizenShip, String State, String City) {

        Map<String, Object> userbasicDetailprefMap = new HashMap<>();
        userbasicDetailprefMap.put("Country", Country);
        userbasicDetailprefMap.put("citizenShip", CitizenShip);
        userbasicDetailprefMap.put("state", State);
        userbasicDetailprefMap.put("city", City);

        firestore.collection("users").document(uId)
                .update(userbasicDetailprefMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getApplicationContext(), "Location Updated Successfully", Toast.LENGTH_LONG).show();

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

    private void getLocationn() {



        firestore.collection("users").document(uId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isComplete()) {
                    if(task.getResult().exists()){

                        pd.dismiss();
                    }
                    country = task.getResult().getString("country");
                    citizenShip = task.getResult().getString("citizenShip");
                    state = task.getResult().getString("state");
                    city = task.getResult().getString("city");

                    etCountry.getEditText().setText(country);
                    etCitizenship.getEditText().setText(citizenShip);
                    etState.getEditText().setText(state);
                    etCity.getEditText().setText(city);
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

    private void getStates(int id) {

        pd.show();

        // Toast.makeText(getApplicationContext(), "Please wait while the states are getting", Toast.LENGTH_LONG).show();

        //countryArraylist = new ArrayList<>();

        // statesList=new ArrayList<>();
        //statesnameList = new ArrayList<>();

        etState.setEnabled(false);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        JsonPlaceHolderAPI jsonPlaceHolderAPI = retrofit.create(JsonPlaceHolderAPI.class);
        Call<List<States>> callstates = jsonPlaceHolderAPI.getStates(id);

        callstates.enqueue(new Callback<List<States>>() {
            @Override
            public void onResponse(Call<List<States>> call, Response<List<States>> response) {

                if (!response.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), "" + response.code(), Toast.LENGTH_LONG).show();
                    etState.setEnabled(false);
                    pd.dismiss();

                    return;
                }

                List<States> states = response.body();
                //String content = "";

                for (States states1 : states) {
                    //names.add(countryStateCity.getName());
                    //countryStateCitieslist.add(countryStateCity);
                    statesnameList.add(states1.getName());
                    //countryStateCities.add(countryStateCity);
                    statesList.add(states1);
                }
                // Toast.makeText(getApplicationContext(), "size"+states.size(), Toast.LENGTH_SHORT).show();
                etState.setEnabled(true);
                etCity.setEnabled(false);
                pd.dismiss();
                // Toast.makeText(getApplicationContext(), "now you can select states", Toast.LENGTH_SHORT).show();

                /* for (States states : statess) {



                 *//*content += "id:" + states.getId() + "\n";
                    content += "name:" + states.getName() + "\n";
                    content += "state:" + states.getIso2() + "\n";*//*


                }*/

                //Toast.makeText(getApplicationContext(), "" + content, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<List<States>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    private void getCities(int countryId, String stateName) {

        pd.show();

        // countryArraylist = new ArrayList<>();
        cityNameList =new ArrayList<>();
        //cityArrayList=new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        JsonPlaceHolderAPI jsonPlaceHolderAPI = retrofit.create(JsonPlaceHolderAPI.class);
        Call<List<City>> callcity = jsonPlaceHolderAPI.getCities(countryId, stateName);

        callcity.enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {

                if (!response.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), "" + response.code(), Toast.LENGTH_LONG).show();
                    pd.dismiss();
                    return;
                }

                List<City> cities = response.body();
                String content = "";
                for (City cityy : cities) {


                    cityNameList.add(cityy.getName());
                    //content += "id:" + cities.getId() + "\n";
                    //content += "name:" + cityy.getName() + "\n";
                    //content += "state:" + states.getIso2() + "\n";

                }
                pd.dismiss();
                etCity.setEnabled(true);
                // Toast.makeText(getApplicationContext(), "Now you can select cities", Toast.LENGTH_SHORT).show();

                //Toast.makeText(getApplicationContext(), "" + content, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }

    private void getCountries() {

        countryArraylist=new ArrayList<>();
        countryNameList = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        JsonPlaceHolderAPI jsonPlaceHolderAPI = retrofit.create(JsonPlaceHolderAPI.class);
        Call<List<CountryStateCity>> call = jsonPlaceHolderAPI.getCountryStateCity();

        call.enqueue(new Callback<List<CountryStateCity>>() {
            @Override
            public void onResponse(Call<List<CountryStateCity>> call, Response<List<CountryStateCity>> response) {

                if (!response.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), "" + response.code(), Toast.LENGTH_LONG).show();

                    return;
                }

                //countryArraylist = response.body();

                List<CountryStateCity> countriesList=response.body();

                // List<String> names = new ArrayList<String>();
                //Toast.makeText(getApplicationContext(), ""+countryArraylist, Toast.LENGTH_SHORT).show();
                for (CountryStateCity countryStateCity : countriesList) {
                    //names.add(countryStateCity.getName());
                    //countryStateCitieslist.add(countryStateCity);
                    countryNameList.add(countryStateCity.getName());
                    countryArraylist.add(countryStateCity);

                }

            }

            @Override
            public void onFailure(Call<List<CountryStateCity>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }



}
