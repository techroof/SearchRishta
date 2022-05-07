package com.techroof.searchrishta.Authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.techroof.searchrishta.Adapter.MaritalStatusAttributeAdapter;
import com.techroof.searchrishta.Adapter.PhysicalStatusAdapter;
import com.techroof.searchrishta.R;

import java.util.List;

public class RegisterActivity3 extends AppCompatActivity {

    private TextView stepText;
    private TextInputLayout countryEt,stateEt,cityEt,citizenshipEt;
    private RecyclerView maritalStatusRv;
    private RecyclerView.LayoutManager layoutManager;
    private PhysicalStatusAdapter adapter;
    private Button continueBtn;
    private List<String> maritalStatusList;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);

        maritalStatusRv=findViewById(R.id.marital_status_rv);
        stepText=findViewById(R.id.step_text);

        stepText.setVisibility(View.VISIBLE);
        stepText.setText("3/3");




    }
}