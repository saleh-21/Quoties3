package com.goldenkey.quoties;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {
    User s;
    ArrayList<Quote> quotes;
    String profile;
    Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        profile = intent.getExtras().getString("profile");
        quotes = new ArrayList<>();
        //1) find the recycler by id.
        final RecyclerView rvquotes = findViewById(R.id.users_posts);
        final quoteAdapter adapter = new quoteAdapter(this,quotes,1, (TextView)findViewById(R.id.tvNoQuotes));
        //3) recycler -> take the adapter.
        rvquotes.setAdapter(adapter);

        //4)
        rvquotes.setLayoutManager(new LinearLayoutManager(null));
        // findViewById...
        //similar to onCreate of activity

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Calendar cal = Calendar.getInstance();
        ProgressDialog Dialog = new ProgressDialog(this);
        Dialog.setMessage("Loading User Data...");
        Dialog.setCancelable(false);
        Dialog.show();
        LoadStudent(profile, Dialog, adapter);

        findViewById(R.id.circleImageView).setVisibility(View.INVISIBLE);
    }

    private void LoadStudent(String uid, final ProgressDialog Dialog, final quoteAdapter adapter) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            public static final String TAG = "Profile Activity";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User value = dataSnapshot.getValue(User.class);
                updateStudent(value, Dialog, adapter);
                Log.d(TAG, "Value is: " + value);
                Dialog.setMessage("Loading Profile");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void updateStudent(final User value, final ProgressDialog Dialog, final quoteAdapter adapter) {
        s = value;
        String interests = "None";
        for(int i = 0; i < value.interests.size(); i++) {
            if(i == 0) {
                interests = " #" + value.interests.get(0).name;
            } else {
                interests = interests + " " + "#" + value.interests.get(i).name;
            }
        }
        ((TextView)findViewById(R.id.tvBio)).setText(value.bio + "\n" + getString(R.string.interest) + interests);
        ((TextView)findViewById(R.id.tvFullName)).setText(value.fullName);
        // Reference to an image file in Firebase Storage
        FirebaseStorage.getInstance();
        FirebaseStorage.getInstance().getReference().child("profile_pictures/" + value.uid)
                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                findViewById(R.id.pbProfile).setVisibility(View.GONE);
                findViewById(R.id.circleImageView).setVisibility(View.VISIBLE);
                Picasso.with(ProfileActivity.this).load(uri).fit().centerCrop().into((ImageView)findViewById(R.id.circleImageView));
                LoadPosts(value.fullName, Dialog, adapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });;
    }


    private void LoadPosts(final String fullName, final ProgressDialog Dialog, final quoteAdapter adapter) {
        DatabaseReference server = FirebaseDatabase.getInstance().getReference();
        server.addListenerForSingleValueEvent(new ValueEventListener() {
            public static final String TAG = "Profile Activity";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild("Quotes")) {
                    Dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        DatabaseReference database = server.child("Quotes");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            public static final String TAG = "Profile Activity";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Quote value = child.getValue(Quote.class);
                    if(value.staredUser.contains(fullName)) {
                        adapter.addItem(value, s, child.getKey());
                        findViewById(R.id.tvNoQuotes).setVisibility(View.GONE);
                    }
                    Log.d(TAG, "Value is: " + value);
                    Dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        this.menu = menu;
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(profile)) {
            menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_edit));
            menu.getItem(0).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_add_friend: {
                menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_person_add_sent));
                return true;
            }
            case R.id.action_chat: {
                /*Intent i = new Intent(this, chatActivity.class);
                i.putExtra("user", profile);
                startActivity(i);*/
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
