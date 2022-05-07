package com.techroof.searchrishta;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.techroof.searchrishta.Authentication.RegisterActivity;

import java.util.HashMap;
import java.util.Map;


public class UpgradeFragment extends Fragment {

    ImageView imgUpload;
    TextView tvPrice, tvAccountnumber, tvBankname, tvAccounntname, tvUploadimage;
    Button btnSubmitdetails;
    private static final int PICK_IMAGE = 100;
    public Uri imageUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    private String activationStatus = "normal";
    public static Context context;
    String price, accountNumber, accountName, bankName, uId, proofImg,name, Status = "Applied";
    private FirebaseUser currentFirebaseUser;

    //private Firestore db;

    // Folder path for Firebase Storage.
    String Storage_Path = "All_Image_Uploads/";
    String Database_Path = "All_Image_Uploads_Database";

    private String productName, categoryName, productImg;

    private FirebaseFirestore firestore;

    public UpgradeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpgradeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpgradeFragment newInstance(String param1, String param2) {
        UpgradeFragment fragment = new UpgradeFragment();
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
        View view = inflater.inflate(R.layout.fragment_upgrade, container, false);
        storageReference = FirebaseStorage.getInstance().getReference("Images");
        databaseReference = FirebaseDatabase.getInstance().getReference("Images");
        imgUpload = view.findViewById(R.id.upload_image);
        tvPrice = view.findViewById(R.id.tv_price);
        tvAccountnumber = view.findViewById(R.id.tv_acccount_number);
        tvBankname = view.findViewById(R.id.tv_bank_name);
        tvAccounntname = view.findViewById(R.id.account_name);
        btnSubmitdetails = view.findViewById(R.id.btn_submit_details);
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uId = currentFirebaseUser.getUid();
        progressDialog = new ProgressDialog(getContext());
        tvUploadimage = view.findViewById(R.id.tv_upload_image);
        progressDialog.setMessage("Uploading Image");
        progressDialog.setCanceledOnTouchOutside(false);

        firestore = FirebaseFirestore.getInstance();
        context=getContext();

        getData();
        imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getimagefromgallery();
            }
        });


        btnSubmitdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                price = tvPrice.getText().toString();
                accountNumber = tvAccountnumber.getText().toString();
                accountName = tvAccounntname.getText().toString();
                bankName = tvBankname.getText().toString();
                addData(Status, proofImg,name);
                Toast.makeText(getContext(),
                        "url" + proofImg,
                        Toast.LENGTH_SHORT).show();

            }
        });



        return view;
    }

    private void getData() {

        firestore.collection("AdminAccountDetails")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                    price = documentSnapshot.getString("price");
                    accountNumber = documentSnapshot.getString("accountNumber");
                    bankName = documentSnapshot.getString("bankName");
                    accountName = documentSnapshot.getString("accountName");

                    tvPrice.setText(price.toString());
                    tvAccountnumber.setText(accountNumber.toString());
                    tvBankname.setText(bankName.toString());
                    tvAccounntname.setText(accountName.toString());

                    getStatusData();

                }
                //Toast.makeText(getContext(), "yes"+educationStatus, Toast.LENGTH_SHORT).show();


            }
        });


    }


    //getstatusdata

    private void getStatusData() {

        firestore.collection("users").whereEqualTo("userId",uId)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                    activationStatus = documentSnapshot.getString("activatedstatus");
                    name=documentSnapshot.getString("name");
                    //Toast.makeText(getActivity(), ""+activationStatus, Toast.LENGTH_SHORT).show();

                }
                //Toast.makeText(getContext(), "yes"+activationStatus, Toast.LENGTH_SHORT).show();

                if (activationStatus.equals("normal")) {
                   // Toast.makeText(getContext(), "yes" + activationStatus, Toast.LENGTH_SHORT).show();

                    if (context != null) {
                        new AlertDialog.Builder(context).setTitle("Upgrade to our Premium account ").
                                setMessage("Activate to our premium account to have verified mobile numbers" +
                                        "and enable chats to other users")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {

                                        dialog.dismiss();

                                    }
                                })
                                .show();


                    }

                } else {
                    //Toast.makeText(getContext(), "yes" + activationStatus, Toast.LENGTH_SHORT).show();

                    imgUpload.setVisibility(View.INVISIBLE);
                    btnSubmitdetails.setVisibility(View.INVISIBLE);
                    tvUploadimage.setText("You have already applied to premium account");

                }



            }
        });
/*
      if(activationStatus.equals("normal")){

            new AlertDialog.Builder(getContext()).setTitle("Upgrade to our Premium account ").
                    setMessage("Activate to our Premium account to have verified mobile numbers" +
                            "and enable chats to other users")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {

                            dialog.dismiss();

                        }
                    })
                    .show();

        }else{

            tvUploadimage.setVisibility(View.INVISIBLE);
            imgUpload.setVisibility(View.INVISIBLE);
            btnSubmitdetails.setVisibility(View.INVISIBLE);
            tvUploadimage.setText("You have already applied to premium account");

        }*/


    }

    private void addData(String Status, String imgProof,String name) {

        //String pId = firestore.collection("products").document().getId();

        Map<String, Object> userproofMap = new HashMap<>();
        userproofMap.put("Status", Status);
        userproofMap.put("image", imgProof);
        userproofMap.put("Uid", uId);
        userproofMap.put("name", name);

        firestore.collection("UserProofDetails")
                .document(uId)
                .set(userproofMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Proof Submitted", Toast.LENGTH_SHORT).show();

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();


            }
        });
    }

//image storing functions


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
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            imgUpload.setImageURI(imageUri);

            UploadImageFileToFirebaseStorage();


        }

    }

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }


    public void UploadImageFileToFirebaseStorage() {


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

                            proofImg = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                            progressDialog.dismiss();

                            Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    proofImg = uri.toString();
                                    Toast.makeText(getContext(), "URI" + proofImg, Toast.LENGTH_SHORT).show();
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


}