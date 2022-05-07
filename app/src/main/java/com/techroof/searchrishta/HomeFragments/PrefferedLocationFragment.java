package com.techroof.searchrishta.HomeFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.techroof.searchrishta.Adapter.DashboardFragmentRecyclerViewAdapter;
import com.techroof.searchrishta.Adapter.EducationalPreferenceAdapter;
import com.techroof.searchrishta.Adapter.LocationPreferenceAdapter;
import com.techroof.searchrishta.Adapter.PrefferedEducationRecyclerViewAdapter;
import com.techroof.searchrishta.Adapter.PrefferedLocationFragmentRecyclerViewAdapter;
import com.techroof.searchrishta.Interfaces.ClickListener;
import com.techroof.searchrishta.Interfaces.DasboardClickListener;
import com.techroof.searchrishta.Model.Users;
import com.techroof.searchrishta.R;
import com.techroof.searchrishta.Shortlisted;
import com.techroof.searchrishta.ViewModel.ShortlistedViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PrefferedLocationFragment extends Fragment implements ClickListener, DasboardClickListener {

    private static final String TAG = "MainActivity";
    private ArrayList<Users> userArrayList;
    private PrefferedLocationFragmentRecyclerViewAdapter recyclerViewAdapter;
    private LocationPreferenceAdapter locationPreferenceAdapter;
    RecyclerView prefferedLocationRv,getPrefferedLocationlist;
    private FirebaseFirestore firestore;
    private LinearLayoutManager layoutManagerdashboard;
    private List<String> educationPreferArrayList;
    private RecyclerView.LayoutManager layoutManagerlocation;
    private ShortlistedViewModel getViewmodel;
    private ArrayList<String> storedId;
    DasboardClickListener dasboardClickListener;
    FirebaseUser currentFirebaseUser;
    public String educationStatus,uId;




    public PrefferedLocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PrefferedLocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PrefferedLocationFragment newInstance(String param1, String param2) {
        PrefferedLocationFragment fragment = new PrefferedLocationFragment();
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
        View view= inflater.inflate(R.layout.fragment_preffered_location, container, false);
        prefferedLocationRv = view.findViewById(R.id.preffered_location_rv);
        getPrefferedLocationlist=view.findViewById(R.id.preffered_location_list);
        educationPreferArrayList = Arrays.asList(getResources().getStringArray(R.array.Location));
        firestore = FirebaseFirestore.getInstance();
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        uId=currentFirebaseUser.getUid();

        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");//arraylist decleration
        userArrayList = new ArrayList<>();

        /////////------Profile creator layout manager-------/////////

        layoutManagerlocation = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        getPrefferedLocationlist.setHasFixedSize(true);
        getPrefferedLocationlist.setLayoutManager(layoutManagerlocation);

        //adapter
        locationPreferenceAdapter = new LocationPreferenceAdapter(getContext(), educationPreferArrayList,this);
        getPrefferedLocationlist.setAdapter(locationPreferenceAdapter);

        storedId = new ArrayList();

        getViewmodel = new ViewModelProvider(this).get(ShortlistedViewModel.class);
        getViewmodel.getAllshortlisted().observe(getViewLifecycleOwner(), new Observer<List<Shortlisted>>() {
            @Override
            public void onChanged(List<Shortlisted> shortlisteds) {

                for (int i = 0; i < shortlisteds.size(); i++) {


                    storedId.add(shortlisteds.get(i).getUserid());
                    //storedId= String.valueOf(shortlisteds.get(i).getUserid());


                }
                //Toast.makeText(getContext(), ""+storedId.size(), Toast.LENGTH_SHORT).show();

            }


        });

        recyclerViewAdapter = new PrefferedLocationFragmentRecyclerViewAdapter(userArrayList,
                requireActivity(), this, storedId);

        //methods
        //getData();

        return view;
    }

/*
    private void getData() {

        firestore.collection("users").whereEqualTo("country","pakistan")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                    Users listData = documentSnapshot.toObject(Users.class);
                    userArrayList.add(listData);

                }

                layoutManagerdashboard = new LinearLayoutManager(getContext(),
                        LinearLayoutManager.HORIZONTAL, false);
               prefferedLocationRv.setLayoutManager(layoutManagerdashboard);
                recyclerViewAdapter = new DashboardFragmentRecyclerViewAdapter(userArrayList,
                        getContext());
                prefferedLocationRv.setAdapter(recyclerViewAdapter);


            }
        });


    }
*/

    @Override
    public void onItemclick(String click) {

        userArrayList.clear();

        firestore.collection("users").whereEqualTo("country",click).whereNotEqualTo("userId", uId)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                    Users listData = documentSnapshot.toObject(Users.class);
                    userArrayList.add(listData);

                }

                layoutManagerdashboard = new LinearLayoutManager(getContext(),
                        LinearLayoutManager.HORIZONTAL, false);
                prefferedLocationRv.setLayoutManager(layoutManagerdashboard);
                prefferedLocationRv.setAdapter(recyclerViewAdapter);
                recyclerViewAdapter=new PrefferedLocationFragmentRecyclerViewAdapter(userArrayList,getContext(),
                        dasboardClickListener,storedId);
                locationPreferenceAdapter.notifyDataSetChanged();


            }
        });
    }

    @Override
    public void onItemclickk(String userId, String name, String dob, String height, String relegion, String education, String maritalstatus, String city, String province, String country) {

        getViewmodel= new ViewModelProvider(this).get(ShortlistedViewModel.class);
        Shortlisted shortlisted=new Shortlisted(userId,name,dob,height,relegion,
                education,maritalstatus,city,province,country);
        getViewmodel.insert(shortlisted);
    }

    @Override
    public void onRemoveClick(String userId, String name, String dob, String height, String relegion, String education, String maritalstatus, String city, String province, String country) {

        getViewmodel = new ViewModelProvider(this).get(ShortlistedViewModel.class);
        getViewmodel.deletenote(userId);

    }
}