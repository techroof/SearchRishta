package com.techroof.searchrishta.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.techroof.searchrishta.Adapter.MaritalStatusAttributeAdapter;
import com.techroof.searchrishta.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity2 extends AppCompatActivity {

    private TextView stepText;
    private TextInputLayout countryEt,stateEt,cityEt,citizenshipEt;
    private RecyclerView maritalStatusRv;
    private RecyclerView.LayoutManager layoutManager;
    private MaritalStatusAttributeAdapter adapter;
    private Button continueBtn;
    private List<String> maritalStatusList;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        stepText=findViewById(R.id.step_text);
        countryEt=findViewById(R.id.country_et);
        stateEt=findViewById(R.id.state_et);
        cityEt=findViewById(R.id.city_et);
        citizenshipEt=findViewById(R.id.citizenship_et);
        maritalStatusRv=findViewById(R.id.marital_status_rv);
        continueBtn=findViewById(R.id.reg_continue_btn);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        stepText.setVisibility(View.VISIBLE);
        stepText.setText("2/3");

        maritalStatusList = Arrays.asList(getResources().getStringArray(R.array.marital_status));

        layoutManager = new LinearLayoutManager(RegisterActivity2.this, RecyclerView.HORIZONTAL, false);
        maritalStatusRv.setHasFixedSize(true);
        maritalStatusRv.setLayoutManager(layoutManager);

        adapter=new MaritalStatusAttributeAdapter(RegisterActivity2.this,maritalStatusList);
        maritalStatusRv.setAdapter(adapter);

        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Please wait...");

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String country, state, city, citizenship,maritalStatus;

                maritalStatus = adapter.maritalStatus;

                country = countryEt.getEditText().getText().toString();
                state = stateEt.getEditText().getText().toString();
                city = cityEt.getEditText().getText().toString();
                citizenship = citizenshipEt.getEditText().getText().toString();

                if (maritalStatus.equals("null")) {

                    Toast.makeText(RegisterActivity2.this, "Please select marital status", Toast.LENGTH_SHORT).show();

                }
                if (TextUtils.isEmpty(country)) {

                    countryEt.setError("Please Enter Country");

                }
                if (TextUtils.isEmpty(state)) {

                    stateEt.setError("Please Enter State");

                }
                if (TextUtils.isEmpty(city)) {

                    cityEt.setError("Please Enter City");

                }
                if (TextUtils.isEmpty(citizenship)) {

                    citizenshipEt.setError("Please Enter Citizenship");

                }

                if (!TextUtils.isEmpty(country) && !TextUtils.isEmpty(state) && !TextUtils.isEmpty(city)
                        && !TextUtils.isEmpty(citizenship)
                        && !TextUtils.isEmpty(maritalStatus))
                {
                    addDetails(country,state,city,citizenship,maritalStatus);
                }


            }
        });


    }

    private void addDetails(String country, String state, String city, String citizenship, String maritalStatus) {

        Map<String, Object> userMap = new HashMap<>();

        userMap.put("country", country);
        userMap.put("state", state);
        userMap.put("city", city);
        userMap.put("citizenship", citizenship);
        userMap.put("maritalStatus", maritalStatus);

        firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid())
                .update(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                pd.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                pd.dismiss();
                Toast.makeText(RegisterActivity2.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}