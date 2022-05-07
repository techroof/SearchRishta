package com.techroof.searchrishta;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.techroof.searchrishta.Adapter.ProfileAdapter;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    private RecyclerView rvProfile;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager linearlayoutmanager;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private CircleImageView userImg;
    public static Context context;
    private static final int PICK_IMAGE = 100;
    public Uri imageUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    String strUserimg, uId;
    private TextView tvProgressbarstatus, tvName, tvId, tvMemberShip;
    private ProgressBar progressBar;
    private String checkProfilephone, checkProfileimg, checkProfilephysicalStatus, checkprofileemail, checkProfileRelegion;
    private String userId, memberShip, userName, userImage;
    private String[] name = {"Edit Partner Preferences", "Chat", "Daily Matches", "Edit Profile"
            , "Upgrade Now", "Notifications", "log out"};
    private int[] images = {R.drawable.matches, R.drawable.email, R.drawable.dailymatches, R.drawable.editprofile,
            R.drawable.upgradeprofile, R.drawable.notifications, R.drawable.logout};

    // Folder path for Firebase Storage.
    String Storage_Path = "All_User_Images_Uploads/";
    String Database_Path = "All_Image_Uploads_Database";


    public ProfileFragment() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        rvProfile = view.findViewById(R.id.rv_view);
        userImg = view.findViewById(R.id.image);
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("UserImages");
        databaseReference = FirebaseDatabase.getInstance().getReference("UserImages");
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading Image");
        progressDialog.setCanceledOnTouchOutside(false);
        rvProfile.setHasFixedSize(true);
        linearlayoutmanager = new LinearLayoutManager(getContext());
        rvProfile.setLayoutManager(linearlayoutmanager);
        adapter = new ProfileAdapter(getContext(), name, images);
        rvProfile.setAdapter(adapter);
        mAuth = FirebaseAuth.getInstance();
        uId = FirebaseAuth.getInstance().getUid();
        progressBar = view.findViewById(R.id.progressBar);
        tvProgressbarstatus = view.findViewById(R.id.label_progess_bar_status_text);
        tvName = view.findViewById(R.id.label_Name);
        tvId = view.findViewById(R.id.label_Id);
        tvMemberShip = view.findViewById(R.id.label_Membership_status);
        context=getContext();
        checkProfileCompleted();

        userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getimagefromgallery();
            }
        });
        return view;
    }

    public void getimagefromgallery() {

        try {
            Intent i = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, PICK_IMAGE);
        } catch (Exception exp) {
            Log.i("Error", exp.toString());
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(imageUri!=null) {
            imageUri = data.getData();
            userImg.setImageURI(imageUri);

            UploadImageFileToFirebaseStorage();
        }
    }


    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void UploadImageFileToFirebaseStorage() {

        if (imageUri != null) {

            progressDialog.show();

            StorageReference storageReference2nd =
                    storageReference
                            .child(Storage_Path + System.currentTimeMillis() + "." +
                                    GetFileExtension(imageUri));

            storageReference2nd.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            strUserimg = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                            progressDialog.dismiss();

                            Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    strUserimg = uri.toString();
                                    //Toast.makeText(getContext(), "URI" + strUserimg, Toast.LENGTH_SHORT).show();

                                    addImg(strUserimg);
                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            progressDialog.dismiss();

                            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d("http", "onFailure: " + exception.getMessage());
                        }
                    })

                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.
                            //progressDialog.setTitle("Image is Uploading...");

                        }
                    });


        } else {

            Toast.makeText(getContext(), "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }


    }

    private void addImg(String userimg) {
        //String pId = firestore.collection("products").document().getId();

        Map<String, Object> userimgMap = new HashMap<>();
        userimgMap.put("img", userimg);


        firestore.collection("users")
                .document(uId)
                .update(userimgMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Image Submitted Successfully", Toast.LENGTH_SHORT).show();

                            checkProfileCompleted();

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();


            }
        });
    }


    private void checkProfileCompleted() {

        firestore.collection("users").document(uId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isComplete()) {

                    checkProfilephone = task.getResult().getString("phone");
                    checkProfileimg = task.getResult().getString("img");
                    checkProfilephysicalStatus = task.getResult().getString("physicalStatus");
                    checkprofileemail = task.getResult().getString("email");
                    checkProfileRelegion = task.getResult().getString("religion");
                    userId = task.getResult().getString("userId");
                    memberShip = task.getResult().getString("activatedstatus");
                    userName = task.getResult().getString("name");
                    userImage = task.getResult().getString("img");


                    tvId.setText(userId);
                    tvName.setText(userName);

                    if (context != null) {

                        Glide.with(ProfileFragment.context)
                                .load(userImage) // image url
                                .placeholder(R.drawable.user_avatar)
                                .override(200, 200) // resizing
                                .centerCrop()
                                .into(userImg);
                    }
                      // imageview object


                    if (memberShip.equals("normal")) {

                        tvMemberShip.setText("Membership-free");

                    } else if (memberShip.equals("Activated")) {

                        tvMemberShip.setText("Premium Account");

                    }



                  /*  if(checkProfileRelegion.&&checkProfilephysicalStatus&&checkProfileimg&&checkProfilephone
                    &&checkprofileemail!=null)*/
                    if (!TextUtils.isEmpty(checkprofileemail) && !TextUtils.isEmpty(checkProfilephone) &&
                            !TextUtils.isEmpty(checkProfileimg) && !TextUtils.isEmpty(checkProfilephysicalStatus) &&
                            !TextUtils.isEmpty(checkProfileRelegion)) {


                        progressBar.setProgress(100);
                        tvProgressbarstatus.setText("Your Profile Score is 100%");

                    } else if (!TextUtils.isEmpty(checkprofileemail) && !TextUtils.isEmpty(checkProfilephone) &&
                            TextUtils.isEmpty(checkProfileimg) && !TextUtils.isEmpty(checkProfilephysicalStatus) &&
                            !TextUtils.isEmpty(checkProfileRelegion)) {

                        progressBar.setProgress(80);
                        tvProgressbarstatus.setText("Your Profile Score is 90%");

                    } else if (TextUtils.isEmpty(checkprofileemail) && TextUtils.isEmpty(checkProfilephone) &&
                            TextUtils.isEmpty(checkProfileimg) && !TextUtils.isEmpty(checkProfilephysicalStatus) &&
                            !TextUtils.isEmpty(checkProfileRelegion)) {

                        progressBar.setProgress(60);
                        tvProgressbarstatus.setText("Your Profile Score is 60%");

                    } else if (TextUtils.isEmpty(checkprofileemail) && TextUtils.isEmpty(checkProfilephone) &&
                            TextUtils.isEmpty(checkProfileimg) && TextUtils.isEmpty(checkProfilephysicalStatus) &&
                            !TextUtils.isEmpty(checkProfileRelegion)) {

                        progressBar.setProgress(30);
                        tvProgressbarstatus.setText("Your Profile Score is 30%");

                    } else if (TextUtils.isEmpty(checkprofileemail) && TextUtils.isEmpty(checkProfilephone) &&
                            TextUtils.isEmpty(checkProfileimg) && TextUtils.isEmpty(checkProfilephysicalStatus) &&
                            TextUtils.isEmpty(checkProfileRelegion)) {

                        progressBar.setProgress(0);
                        tvProgressbarstatus.setText("Your Profile Score is 0%");

                    }

                } else {
                    Log.d("d", "Error getting documents: ", task.getException());

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
