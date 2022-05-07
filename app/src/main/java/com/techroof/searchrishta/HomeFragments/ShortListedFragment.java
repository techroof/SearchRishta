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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.techroof.searchrishta.Adapter.DashboardFragmentRecyclerViewAdapter;
import com.techroof.searchrishta.Adapter.ShortlistedAdapterRecyclerview;
import com.techroof.searchrishta.Interfaces.DasboardClickListener;
import com.techroof.searchrishta.Model.Users;
import com.techroof.searchrishta.R;
import com.techroof.searchrishta.Shortlisted;
import com.techroof.searchrishta.ViewModel.ShortlistedViewModel;

import java.util.ArrayList;
import java.util.List;


public class ShortListedFragment extends Fragment implements DasboardClickListener {

    private static final String TAG = "MainActivity";
    private ArrayList<Users> userArrayList;
    private DashboardFragmentRecyclerViewAdapter recyclerViewAdapter;
    RecyclerView shortlistedRv;
    private FirebaseFirestore firestore;
    private LinearLayoutManager layoutManagerdashboard;
    private FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    private ShortlistedViewModel getViewmodel ;
      Shortlisted shortlistedd;
    List<Shortlisted> shortlisteds;



    public ShortListedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShortListedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShortListedFragment newInstance(String param1, String param2) {
        ShortListedFragment fragment = new ShortListedFragment();
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
        View view= inflater.inflate(R.layout.fragment_short_listed, container, false);

         /*shortlistedd=new Shortlisted("add","adasd","asdasd","asdasd","sdasd",
                "adsdas","addddaf","adasd","dadddf","adadf");
        getViewmodel.insert(shortlistedd);*/

        shortlisteds=new ArrayList<>();

        shortlistedRv = view.findViewById(R.id.shortlisted_rv);
        shortlistedRv.setLayoutManager(new LinearLayoutManager(getContext()));
        shortlistedRv.setHasFixedSize(true);
         ShortlistedAdapterRecyclerview shortlistedAdapter=new ShortlistedAdapterRecyclerview(shortlisteds,this,getContext());
         shortlistedRv.setAdapter(shortlistedAdapter);
        //firestore = FirebaseFirestore.getInstance();

        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        //arraylist decleration
        userArrayList = new ArrayList<>();

         //room database

        getViewmodel= new ViewModelProvider(this).get(ShortlistedViewModel.class);
         /* Shortlisted shortlisted=new Shortlisted("sdasd","ssdsad","asdasd","DSASD","SDASD",
                  "dfsf","dfsdf","dfdf","adaff","adfadf");
        getViewmodel.insert(shortlisted);*/
        //getViewmodel.deleteAllNotes();


        getViewmodel.getAllshortlisted().observe(getViewLifecycleOwner(), new Observer<List<Shortlisted>>() {
            @Override
            public void onChanged(List<Shortlisted> shortlisteds) {
                shortlistedAdapter.setShortlisted(shortlisteds);
                shortlistedRv.setAdapter(shortlistedAdapter);
               // Toast.makeText(getContext(), "changed"+getViewmodel, Toast.LENGTH_SHORT).show();

                if (shortlistedAdapter.getItemCount()!=0)
                {

                    //nothing.setVisibility(View.GONE);
                    //Toast.makeText(getContext(), "something not working"+shortlistedAdapter.getItemCount(),Toast.LENGTH_LONG).show();
                }
                else {
                    //Toast.makeText(getContext(), "working"+shortlistedAdapter.getItemCount(),Toast.LENGTH_LONG).show();

                }
            }


        });

        //methods
        //getData();
        return view;
    }

    @Override
    public void onItemclickk(String userId, String name, String dob, String height, String relegion, String education, String maritalstatus, String city, String province, String country) {

    }

    @Override
    public void onRemoveClick(String userId, String name, String dob, String height, String relegion, String education, String maritalstatus, String city, String province, String country) {

        getViewmodel = new ViewModelProvider(this).get(ShortlistedViewModel.class);
        getViewmodel.deletenote(userId);
    }

    /*private void getData() {

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
                shortlistedRv.setLayoutManager(layoutManagerdashboard);
                recyclerViewAdapter = new DashboardFragmentRecyclerViewAdapter(userArrayList,
                        getContext());
                shortlistedRv.setAdapter(recyclerViewAdapter);


            }
        });*/


    }

