package com.techroof.searchrishta.HomeFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.techroof.searchrishta.Adapter.DashboardFragmentRecyclerViewAdapter;
import com.techroof.searchrishta.Adapter.MutualFragmentRecyclerViewAdapter;
import com.techroof.searchrishta.Interfaces.DasboardClickListener;
import com.techroof.searchrishta.Model.Users;
import com.techroof.searchrishta.R;
import com.techroof.searchrishta.Shortlisted;
import com.techroof.searchrishta.ViewModel.ShortlistedViewModel;

import java.util.ArrayList;


public class MutualFragment extends Fragment implements DasboardClickListener {

    private static final String TAG = "MainActivity";
    private ArrayList<Users> userArrayList;
    private MutualFragmentRecyclerViewAdapter recyclerViewAdapter;
    RecyclerView mutualRv;
    private FirebaseFirestore firestore;
    private LinearLayoutManager layoutManagerdashboard;
    private FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    private ShortlistedViewModel getViewmodel ;


    public MutualFragment() {
        // Required empty public constructor
    }


    public static MutualFragment newInstance(String param1, String param2) {
        MutualFragment fragment = new MutualFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_mutual, container, false);
        mutualRv = view.findViewById(R.id.mutual_rv);
        firestore = FirebaseFirestore.getInstance();

        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        //arraylist decleration
        userArrayList = new ArrayList<>();

        recyclerViewAdapter = new MutualFragmentRecyclerViewAdapter(userArrayList,
                getContext(),this);
        //methods
        getData();

        return view;
    }

    @Override
    public void onItemclickk(String userId, String name, String dob, String height, String relegion, String education, String maritalstatus, String city, String province, String country) {

        Toast.makeText(getContext(), "yes"+userId+name, Toast.LENGTH_SHORT).show();
        getViewmodel= new ViewModelProvider(this).get(ShortlistedViewModel.class);
        Shortlisted shortlisted=new Shortlisted(userId,name,dob,height,relegion,
                education,maritalstatus,city,province,country);
        getViewmodel.insert(shortlisted);
    }

    @Override
    public void onRemoveClick(String userId, String name, String dob, String height, String relegion, String education, String maritalstatus, String city, String province, String country) {

    }

    private void getData() {

        firestore.collection("users")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                    Users listData = documentSnapshot.toObject(Users.class);
                    userArrayList.add(listData);

                }

                layoutManagerdashboard = new LinearLayoutManager(getContext(),
                        LinearLayoutManager.HORIZONTAL, false);
                mutualRv.setLayoutManager(layoutManagerdashboard);
                mutualRv.setAdapter(recyclerViewAdapter);


            }
        });


    }
}