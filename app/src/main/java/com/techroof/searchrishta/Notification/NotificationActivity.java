package com.techroof.searchrishta.Notification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
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
import com.techroof.searchrishta.Adapter.DashboardFragmentRecyclerViewAdapter;
import com.techroof.searchrishta.Adapter.NotificationRecyclerViewAdapter;
import com.techroof.searchrishta.Model.Users;
import com.techroof.searchrishta.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    FirebaseAnalytics mFirebaseAnalytics;
    FirebaseFirestore firebaseFirestore;
    private NotificationRecyclerViewAdapter recyclerViewAdapter;
    RecyclerView notificationRv;
    private LinearLayoutManager layoutManagerdashboard;
    ImageView imgToolbar;
    private TextView tvNotificationCounter;
    private ImageView imgViewNotification;
    private final int max_number=99;
    private int notification_number_counter;
    private String uId;
    private String documentNumber;
    private ArrayList<Users> userArrayList;
    private ArrayList<String> document_list;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        firebaseFirestore=FirebaseFirestore.getInstance();

        userArrayList=new ArrayList<>();
        document_list=new ArrayList<>();

        notificationRv=findViewById(R.id.notification_rv);

        firebaseAuth=FirebaseAuth.getInstance();
        uId=firebaseAuth.getCurrentUser().getUid();

        imgBack=findViewById(R.id.back_btn);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UpdateStatus();
                onBackPressed();
            }
        });
        Bundle extras = getIntent().getExtras();


        if (extras != null) {

            document_list=extras.getStringArrayList("document_list");
            getdata();

            //getStatus();
        }



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UpdateStatus();
    }

    private void getdata() {

        firebaseFirestore.collection("SentInterests").whereEqualTo("InterestSent", uId).whereEqualTo("Status", "Unseen").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    if(task.getResult().isEmpty()){

                        Toast.makeText(getApplicationContext(), "No new notification to show", Toast.LENGTH_SHORT).show();
                    }
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        String Viewer = document.getString("InterestedPerson");

                        firebaseFirestore.collection("users").whereEqualTo("userId", Viewer)
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                    Users listData = documentSnapshot.toObject(Users.class);
                                    userArrayList.add(listData);

                                }
                                layoutManagerdashboard = new LinearLayoutManager(getApplicationContext(),
                                        LinearLayoutManager.VERTICAL, false);
                                notificationRv.setLayoutManager(layoutManagerdashboard);
                /*recyclerViewAdapter = new DashboardFragmentRecyclerViewAdapter(userArrayList,
                        requireActivity(), recyclerViewAdapter.mlistener);*/

                                recyclerViewAdapter=new NotificationRecyclerViewAdapter(userArrayList,
                                        getApplicationContext());
                                notificationRv.setAdapter(recyclerViewAdapter);

                            }
                        });


                    }

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "" + e.toString(), Toast.LENGTH_LONG).show();
            }
        });



    }


    private void UpdateStatus() {

        Map<String, Object> updateNotificationStatusMap = new HashMap<>();
        updateNotificationStatusMap.put("Status", "Seen");

        for (int i = 0; i < document_list.size(); i++) {

            firebaseFirestore.collection("SentInterests").document(document_list.get(i))
                    .update(updateNotificationStatusMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();

                }
            });

        }
    }
}