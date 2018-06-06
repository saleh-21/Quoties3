package com.goldenkey.quoties;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class addQuoteActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etPost;
    EditText etAuthor;
    User s = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quote);
        findViewById(R.id.btnAddPost).setOnClickListener(this);
        etPost = findViewById(R.id.etAddPost);
        etAuthor = findViewById(R.id.etAuthor);
        ProgressDialog Dialog = new ProgressDialog(this);
        Dialog.setMessage("Loading Please Wait...");
        Dialog.setCancelable(false);
        Dialog.show();
        LoadUser(FirebaseAuth.getInstance().getCurrentUser().getUid(), Dialog);
    }

    private void LoadUser(String uid, final ProgressDialog Dialog) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            public static final String TAG = "Profile Activity";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User value = dataSnapshot.getValue(User.class);
                updateUser(value);
                Log.d(TAG, "Value is: " + value);
                Dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void updateUser(User value) {
        s = value;

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnAddPost) {
            String str = etPost.getText().toString();
            String author = etAuthor.getText().toString();
            if(str.length() == 0) {
                etPost.setError("This field is required.");
            }
            if(author.length() == 0) {
                etAuthor.setError("This field is required.");
            }
            if(str.length() != 0 && author.length() != 0) {
                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                Quote w = new Quote(str, author, Calendar.getInstance().getTime().toString(), s.uid, s.fullName);
                database.child("Quotes").push().setValue(w);
                etPost.setText("");
                etAuthor.setText("");
                Toast.makeText(this, "Quote Added", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }
    }
}
