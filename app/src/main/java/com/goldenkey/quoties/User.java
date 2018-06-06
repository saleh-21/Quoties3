package com.goldenkey.quoties;

import java.util.ArrayList;

/**
 * Created by wisam on 3/24/2018.
 */

public class User {
    String fullName;
    String gender;
    String bio;
    String uid;
    int admin;
    ArrayList<Interest> interests;

    public User(String fullName, String gender, String bio, String uid, ArrayList<Interest> interests, ArrayList<Quote> favQuotes) {
        this.fullName = fullName;
        this.gender = gender;
        this.interests = interests;
        this.bio = bio;
        this.uid = uid;
        admin = 0;
    }

    public  User() {}
}
