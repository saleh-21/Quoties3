package com.goldenkey.quoties;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.female)
    Button female;
    @BindView(R.id.profileImage)
    CircleImageView profileImage;
    @BindView(R.id.DisplayName)
    EditText DisplayName;
    @BindView(R.id.male)
    Button male;
    @BindView(R.id.nextBtn)
    Button nextBtn;
    @BindView(R.id.EditBtn)
    ImageView EditBtn;
    @BindView(R.id.etBio)
    EditText etBio;
    String gender = "Male";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        male.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.pink_rounded_bitton));
        female.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.blue));
    }

    @OnClick({R.id.female, R.id.male, R.id.nextBtn, R.id.EditBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.female:
                female.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.pink_rounded_bitton));
                male.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.blue));
                gender = "Female";
                break;
            case R.id.male:
                male.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.pink_rounded_bitton));
                female.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.blue));
                gender = "Male";
                break;
            case R.id.nextBtn:
                if (DisplayName.getText().length() == 0) {
                    DisplayName.setError("This field is required");
                } else {
                    if (etBio.getText().length() == 0) {
                        etBio.setError("This field is required");
                    } else {
                        Intent i = new Intent(this, selectTopicActivity.class);
                        i.putExtra("DisplayName", DisplayName.getText().toString());
                        i.putExtra("Bio", etBio.getText().toString());
                        i.putExtra("Gender", gender);
                        startActivity(i);
                    }
                }
                break;
            case R.id.EditBtn:
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            final Uri targetUri = data.getData();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();
            if (targetUri != null) {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                StorageReference ref = storageReference.child("profile_pictures/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
                ref.putFile(targetUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                try {
                                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                                    profileImage.setImageBitmap(bitmap);
                                    progressDialog.dismiss();
                                    Toast.makeText(RegisterActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                } catch (FileNotFoundException e) {
                                    Toast.makeText(RegisterActivity.this, "Failed to read file, please try again!", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Saving " + (int) progress + "%");
                            }
                        });
            }
        }
    }
}
