package com.goldenkey.quoties;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class favFragment extends Fragment {
    View v;
    User s;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_fav, container, false);;

        final ArrayList<Quote> quotes = new ArrayList<>();
        //1) find the recycler by id.
        final RecyclerView rvquotes = v.findViewById(R.id.rvQuotes);
        rvquotes.setHasFixedSize(true);
        //the adapter takes movies and provides Views for the movies.
        final quoteAdapter adapter = new quoteAdapter(this.getContext(),quotes,1, (TextView)v.findViewById(R.id.tvNoQuote));

        //3) recycler -> take the adapter.
        rvquotes.setAdapter(adapter);

        //4)
        rvquotes.setLayoutManager(new LinearLayoutManager(null));
        // findViewById...
        //similar to onCreate of activity

        final ProgressDialog Dialog = new ProgressDialog(v.getContext());
        Dialog.setMessage("Loading Favorite Quotes...");
        Dialog.setCancelable(false);
        Dialog.show();
        LoadUser(FirebaseAuth.getInstance().getCurrentUser().getUid(), Dialog, adapter);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void LoadUser(String uid, final ProgressDialog Dialog, final quoteAdapter adapter) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            public static final String TAG = "Profile Activity";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User value = dataSnapshot.getValue(User.class);
                updateStudent(value);
                Log.d(TAG, "Value is: " + value);
                Dialog.setMessage("Loading Favorite Quotes.");
                LoadPosts(Dialog, adapter, value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void LoadPosts(final ProgressDialog dialog, final quoteAdapter adapter, final User s) {
        DatabaseReference server = FirebaseDatabase.getInstance().getReference();
        server.addListenerForSingleValueEvent(new ValueEventListener() {
            public static final String TAG = "Quotes Fragment";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild("Quotes")) {
                    dialog.dismiss();
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
            public static final String TAG = "Quotes Fragment";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int added = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    Quote value = child.getValue(Quote.class);
                    Log.d(TAG, "Value is: " + value);
                    if(value.staredUser.contains(s.fullName)) {
                        adapter.addItem(value, s, child.getKey());
                        added ++;
                        v.findViewById(R.id.tvNoQuote).setVisibility(View.GONE);
                        dialog.dismiss();
                    }
                }
                if(added == 0) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void updateStudent(User value) {
        s = value;
    }
}
