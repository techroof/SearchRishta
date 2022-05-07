package com.techroof.searchrishta;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.techroof.searchrishta.Adapter.HomeFragmentViewPagerAdapter;
import com.techroof.searchrishta.HomeFragments.DashBoardFragment;
import com.techroof.searchrishta.HomeFragments.JustJoinedFragment;
import com.techroof.searchrishta.HomeFragments.MatchesFragment;
import com.techroof.searchrishta.HomeFragments.MutualFragment;
import com.techroof.searchrishta.HomeFragments.NearedByMatchesFragment;
import com.techroof.searchrishta.HomeFragments.PrefferedEducationFragment;
import com.techroof.searchrishta.HomeFragments.PrefferedLocationFragment;
import com.techroof.searchrishta.HomeFragments.PrefferedProfessionFragment;
import com.techroof.searchrishta.HomeFragments.PremiumFragment;
import com.techroof.searchrishta.HomeFragments.ShortListedFragment;
import com.techroof.searchrishta.HomeFragments.ShortlistedMeFragment;
import com.techroof.searchrishta.HomeFragments.ViewedMyProfileFragment;
import com.techroof.searchrishta.HomeFragments.ViewedNotContactedFragment;
import com.techroof.searchrishta.Notification.NotificationActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class HomeFragment extends Fragment {

    private TabLayout tableLayout;
    private ViewPager viewPager;
    private FirebaseAuth firebaseAuth;
    FirebaseAnalytics mFirebaseAnalytics;
    FirebaseFirestore firebaseFirestore;
    ImageView imgToolbar;
    private TextView tvNotificationCounter;
    private ImageView imgViewNotification;
    private final int max_number = 99;
    private int notification_number_counter=0;
    private String uId;
    private String documentNumber;
    private ArrayList<String> documentArraylist;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tableLayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.viewpager);
        tableLayout.setupWithViewPager(viewPager);

        documentArraylist=new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


       // uId = firebaseAuth.getCurrentUser().getUid();
        //notification counter textview
        tvNotificationCounter = view.findViewById(R.id.tv_notification_counter);


        imgViewNotification = view.findViewById(R.id.img_back_arrow);



        imgViewNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (documentArraylist!=null) {

                   // UpdateStatus();

                    Intent moveNotifications = new Intent(getActivity(), NotificationActivity.class);
                    moveNotifications.putStringArrayListExtra("document_list",documentArraylist);
                    startActivity(moveNotifications);

                } else {


                    Toast.makeText(getContext(), "You dont have new notifications", Toast.LENGTH_SHORT).show();
                }

            }
        });


        HomeFragmentViewPagerAdapter viewPagerAdapter = new HomeFragmentViewPagerAdapter(getFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        //viewPagerAdapter.addfragment(new ProfileFragment(), "PROFILE");
        viewPagerAdapter.addfragment(new DashBoardFragment(), "DASHBOARD");
        viewPagerAdapter.addfragment(new JustJoinedFragment(), "JUST JOINED");
        viewPagerAdapter.addfragment(new MatchesFragment(), "MATCHES");
        viewPagerAdapter.addfragment(new PremiumFragment(), "PREMIUM");
        viewPagerAdapter.addfragment(new ViewedMyProfileFragment(), "VIEWED MY PROFILE");
        viewPagerAdapter.addfragment(new ShortlistedMeFragment(), "SHOWN INTEREST");
        viewPagerAdapter.addfragment(new ShortListedFragment(), "SHORTLISTED");
        viewPagerAdapter.addfragment(new NearedByMatchesFragment(), "NEARBY MATCHES");
        viewPagerAdapter.addfragment(new PrefferedProfessionFragment(), "PREFFERED PROFESSION");
        viewPagerAdapter.addfragment(new PrefferedEducationFragment(), "PREFFERED EDUCATION");
        viewPagerAdapter.addfragment(new PrefferedLocationFragment(), "PREFFERED LOCATION");

        viewPager.setAdapter(viewPagerAdapter);


        CheckNotification();
        return view;
    }


    @Override
    public void onStart() {
        //CheckNotification();
        super.onStart();
        DashBoardFragment dashBoardFragment=new DashBoardFragment();

    }

    private void CheckNotification() {

        uId= firebaseAuth.getCurrentUser().getUid();
        //Toast.makeText(getContext(), ""+uId, Toast.LENGTH_SHORT).show();

        firebaseFirestore.collection("SentInterests").whereEqualTo("InterestSent", uId).whereEqualTo("Status", "Unseen").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {


                    for (QueryDocumentSnapshot document : task.getResult()) {

                        String Viewer = document.get("InterestedPerson").toString();

                        firebaseFirestore.collection("users").whereEqualTo("userId", Viewer)
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                    notification_number_counter++;
                                    documentArraylist.add(document.getId());

                                }

                                if (max_number > notification_number_counter) {

                                    tvNotificationCounter.setVisibility(View.VISIBLE);
                                    tvNotificationCounter.setText(String.valueOf(notification_number_counter));
                                    //documentNumber = document.getId();
                                    //Toast.makeText(getContext(), ""+document.getId(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });


                    }

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getContext(), "" + e.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void UpdateStatus() {

        Map<String, Object> updateNotificationStatusMap = new HashMap<>();
        updateNotificationStatusMap.put("Status", "Seen");

        firebaseFirestore.collection("SentInterests").document(documentNumber)
                .update(updateNotificationStatusMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getContext(), "Updated", Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }


}
