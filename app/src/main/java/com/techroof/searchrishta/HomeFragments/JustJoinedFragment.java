package com.techroof.searchrishta.HomeFragments;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.techroof.searchrishta.Adapter.DashboardFragmentRecyclerViewAdapter;
import com.techroof.searchrishta.Adapter.JustJoinedFragmentRecyclerViewAdapter;
import com.techroof.searchrishta.Interfaces.DasboardClickListener;
import com.techroof.searchrishta.Model.Users;
import com.techroof.searchrishta.R;
import com.techroof.searchrishta.Shortlisted;
import com.techroof.searchrishta.ViewModel.ShortlistedViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JustJoinedFragment extends Fragment implements DasboardClickListener {

    private static final String TAG = "MainActivity";
    private ArrayList<Users> userArrayList;
    private DashboardFragmentRecyclerViewAdapter recyclerViewAdapter;
    RecyclerView justJoinedrv;
    private FirebaseFirestore firestore;
    private LinearLayoutManager layoutManagerdashboard;
    private FirebaseAuth firebaseAuth;
    private String currentDate;
    private String uId;
    private ShortlistedViewModel getViewmodel;

    //Date dateObj = null;
    private ArrayList<String> storedId;

    private FirebaseUser currentFirebaseUser;
    //progress dialog

    private ProgressDialog progressDialog;

    //imageviewNotification
    private ImageView imgViewNotification;

    private TextView tvNotificationCounter;

    FirebaseAuth.AuthStateListener authStateListener;
    public JustJoinedFragment() {
        // Required empty public constructor
    }


    public static JustJoinedFragment newInstance(String param1, String param2) {
        JustJoinedFragment fragment = new JustJoinedFragment();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_just_joined, container, false);
        justJoinedrv = view.findViewById(R.id.rv_justjoined);
        firestore = FirebaseFirestore.getInstance();

        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading");
        progressDialog.show();
        //arraylist decleration
        userArrayList = new ArrayList<>();
        storedId = new ArrayList();

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        uId=currentFirebaseUser.getUid();
        /*DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        currentDate= dtf.format(now);*/
         currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        //Toast.makeText(getActivity(), ""+currentDate, Toast.LENGTH_LONG).show();
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
        recyclerViewAdapter = new DashboardFragmentRecyclerViewAdapter(userArrayList,
                requireActivity(), this, storedId);
        //methods
        getData();


        return view;
    }

    private void getData() {

        firestore.collection("users").whereNotEqualTo("userId",uId).whereEqualTo("dateOfRegistration",currentDate)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                    Users listData = documentSnapshot.toObject(Users.class);
                    userArrayList.add(listData);


                }


                layoutManagerdashboard = new LinearLayoutManager(getContext(),
                        LinearLayoutManager.VERTICAL, false);
                justJoinedrv.setLayoutManager(layoutManagerdashboard);
                /*recyclerViewAdapter = new DashboardFragmentRecyclerViewAdapter(userArrayList,
                        requireActivity(), recyclerViewAdapter.mlistener);*/
                justJoinedrv.setAdapter(recyclerViewAdapter);

                progressDialog.dismiss();

            }
        });


    }

    @Override
    public void onItemclickk(String userId, String name, String dob, String height, String relegion, String education, String maritalstatus, String city, String province, String country) {

        getViewmodel = new ViewModelProvider(this).get(ShortlistedViewModel.class);
        Shortlisted shortlisted = new Shortlisted(userId, name, dob, height, relegion,
                education, maritalstatus, city, province, country);
        getViewmodel.insert(shortlisted);
    }

    @Override
    public void onRemoveClick(String userId, String name, String dob, String height, String relegion, String education, String maritalstatus, String city, String province, String country) {

        getViewmodel.deletenote(userId);
    }
}