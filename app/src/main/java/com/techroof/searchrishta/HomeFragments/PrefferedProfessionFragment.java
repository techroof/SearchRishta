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
import android.widget.Button;
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
import com.techroof.searchrishta.Adapter.PrefferedEducationRecyclerViewAdapter;
import com.techroof.searchrishta.Adapter.PrefferedLocationFragmentRecyclerViewAdapter;
import com.techroof.searchrishta.Adapter.PrefferedProfessionRecyclerViewAdapter;
import com.techroof.searchrishta.Interfaces.ClickListener;
import com.techroof.searchrishta.Interfaces.DasboardClickListener;
import com.techroof.searchrishta.Model.Users;
import com.techroof.searchrishta.R;
import com.techroof.searchrishta.Shortlisted;
import com.techroof.searchrishta.ViewModel.ShortlistedViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PrefferedProfessionFragment extends Fragment implements ClickListener, DasboardClickListener {


    private static final String TAG = "MainActivity";
    private ArrayList<Users> userArrayList;
    private PrefferedProfessionRecyclerViewAdapter recyclerViewAdapter;
    public EducationalPreferenceAdapter educationalPreferenceAdapter;
    RecyclerView prefferedProfessionRv, getPrefferedProfessionlist;
    private FirebaseFirestore firestore;
    private RecyclerView.LayoutManager layoutManagerdashboard;
    private FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    private List<String> educationPreferArrayList;
    private RecyclerView.LayoutManager layoutManagereducation;
    public String professionStatus, uId;
    private Button btn;
    public PrefferedEducationFragment prefferedEducationFragment;
    FirebaseUser currentFirebaseUser;
    private ShortlistedViewModel getViewmodel;
    private ArrayList<String> storedId;
    DasboardClickListener dasboardClickListener;



    public PrefferedProfessionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PrefferedProfessionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PrefferedProfessionFragment newInstance(String param1, String param2) {
        PrefferedProfessionFragment fragment = new PrefferedProfessionFragment();
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
        View view = inflater.inflate(R.layout.fragment_preffered_profession, container, false);
        prefferedProfessionRv = view.findViewById(R.id.preffered_profession_rv);
        getPrefferedProfessionlist = view.findViewById(R.id.preffered_profession_list);
        educationPreferArrayList = Arrays.asList(getResources().getStringArray(R.array.Occupation));
        firestore = FirebaseFirestore.getInstance();
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uId = currentFirebaseUser.getUid();
        storedId = new ArrayList();
        /////////------Profile creator layout manager-------/////////

        layoutManagereducation = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        getPrefferedProfessionlist.setHasFixedSize(true);
        getPrefferedProfessionlist.setLayoutManager(layoutManagereducation);

        //adapter
        educationalPreferenceAdapter = new EducationalPreferenceAdapter(requireActivity(), educationPreferArrayList, this);
        getPrefferedProfessionlist.setAdapter(educationalPreferenceAdapter);

        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        //arraylist decleration
        userArrayList = new ArrayList<>();
        /*recyclerViewAdapter = new PrefferedEducationRecyclerViewAdapter(userArrayList,
                getContext(), this);*/
        /*btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                educationStatus=educationalPreferenceAdapter.educationStatuss;
                Toast.makeText(getContext(), ""+educationStatus, Toast.LENGTH_SHORT).show();
            }
        });*/
        //methods

        //educationStatus= educationalPreferenceAdapter.educationStatuss;
        //Toast.makeText(getContext(), "yes"+educationStatus, Toast.LENGTH_SHORT).show();
        ;

        // getData();

        //methods
        //getData();

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

        recyclerViewAdapter = new PrefferedProfessionRecyclerViewAdapter(userArrayList,
                requireActivity(), this, storedId);


        return view;
    }

    @Override
    public void onItemclick(String click) {

        userArrayList.clear();
        professionStatus = click;
        //Toast.makeText(getContext(), "yes" + uId, Toast.LENGTH_SHORT).show();
        firestore.collection("users").whereEqualTo("occupation", professionStatus).whereNotEqualTo("userId", uId)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                    Users listData = documentSnapshot.toObject(Users.class);
                    userArrayList.add(listData);


                }
                //Toast.makeText(getContext(), "yes"+click+userArrayList, Toast.LENGTH_SHORT).show();

                layoutManagerdashboard = new LinearLayoutManager(getContext());
                prefferedProfessionRv.setLayoutManager(layoutManagerdashboard);
                prefferedProfessionRv.setAdapter(recyclerViewAdapter);
                recyclerViewAdapter= new PrefferedProfessionRecyclerViewAdapter(userArrayList,requireContext(),
                        dasboardClickListener,storedId);
                recyclerViewAdapter.notifyDataSetChanged();


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
