package com.goldenkey.quoties;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sohel97 on 1/17/18.
 */

public class ActivityBaseClass extends AppCompatActivity {
    int RC_SIGN_IN = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            gotoLogin();
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    protected void gotoLogin() {
        List<AuthUI.IdpConfig> loginProviders = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                //new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()
        );
        //Intent -> for login screen
        Intent loginIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setLogo(R.drawable.quote_icon)
                .setAvailableProviders(loginProviders)
                .build();


        startActivityForResult(loginIntent, RC_SIGN_IN);
    }

    protected void showError(String error) {
        new AlertDialog.Builder(this).setTitle(error).setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gotoLogin();
            }
        }).setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).show();
    }
}
