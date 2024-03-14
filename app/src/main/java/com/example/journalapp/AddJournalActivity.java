package com.example.journalapp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class AddJournalActivity extends AppCompatActivity {

    private Button saveButton;
    private ImageView addPhotoBtn;
    private ProgressBar progressBar;
    private EditText titleEditText;

    private EditText thoughtsEditText;
    private ImageView imageView;


    private FirebaseFirestore db= FirebaseFirestore.getInstance();


    private CollectionReference collectionReference= db.collection("Journal");

    private StorageReference storageReference;

    private String currentUserId;
    private String currentUserName;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    ActivityResultLauncher<String>mTakePhoto;

    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);

        progressBar= findViewById(R.id.post_progressBar);
        titleEditText= findViewById(R.id.post_title_et);
        thoughtsEditText= findViewById(R.id.post_description_et);
        imageView= findViewById(R.id.post_imageView);
        saveButton= findViewById(R.id.post_save_journal_button);
        addPhotoBtn= findViewById(R.id.postCameraButton);
        progressBar.setVisibility(View.INVISIBLE);

        storageReference= FirebaseStorage.getInstance().getReference();

        firebaseAuth= FirebaseAuth.getInstance();

        if(user!=null){
            currentUserId=user.getUid();
            currentUserName= user.getDisplayName();

        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveJournal();
            }
        });


        mTakePhoto= registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {

                        imageView.setImageURI(result);
                        imageUri= result;
                    }
                }
        );

        addPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTakePhoto.launch("image/*");

            }
        });
}


        private void SaveJournal(){
           String title= titleEditText.getText().toString().trim();
           String thoughts = thoughtsEditText.getText().toString().trim();

           progressBar.setVisibility(View.VISIBLE);

           if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(thoughts) && imageUri!= null){
               final StorageReference filepath = storageReference.child("journal_images")
                       .child("my_image_"+ Timestamp.now().getSeconds());

               filepath.putFile(imageUri)
                       .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                           @Override
                           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                               filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                   @Override
                                   public void onSuccess(Uri uri) {
                                       String imageUrl= uri.toString();

                                       Journal journal= new Journal();
                                       journal.setTitle(title);
                                       journal.setThoughts(thoughts);
                                       journal.setImageUrl(imageUrl);

                                       journal.setTimestamp(new Timestamp(new Date()));
                                       journal.setUserName(currentUserName);


                                       journal.setUserId(currentUserId);



                                       collectionReference.add(journal).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                           @Override
                                           public void onSuccess(DocumentReference documentReference) {
                                               progressBar.setVisibility(View.INVISIBLE);

                                               Intent i = new Intent(AddJournalActivity.this, JournalListActivity.class);

                                               startActivity(i);
                                               finish();
                                           }
                                       }).addOnFailureListener(new OnFailureListener() {
                                           @Override
                                           public void onFailure(@NonNull Exception e) {
                                               Toast.makeText(AddJournalActivity.this, "Failed :"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                           }
                                       });



                                   }
                               });
                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               progressBar.setVisibility(View.INVISIBLE);
                               Toast.makeText(AddJournalActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                           }
                       });
           }else{
               progressBar.setVisibility(View.INVISIBLE);
           }

        }

    @Override
    protected void onStart() {
        super.onStart();
        user = firebaseAuth.getCurrentUser();
    }
}