package com.goldenkey.quoties;

import java.util.ArrayList;

/**
 * Created by wisam on 3/24/2018.
 */

public class Quote {
    String text;
    String author;
    int likes;
    String date;
    String user;
    String uid;
    String quoteid;
    ArrayList<String> likedUser;
    ArrayList<String> staredUser;

    public Quote(String text, String author, String date, String uid, String user) {
        this.text = text;
        this.author = author;
        this.likes = 0;
        this.date = date;
        this.user = user;
        this.uid = uid;
        likedUser = new ArrayList<>();
        likedUser.add("test");
        staredUser = new ArrayList<>();
        staredUser.add("test");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Quote quote = (Quote) o;

        if (text != null ? !text.equals(quote.text) : quote.text != null) return false;
        if (author != null ? !author.equals(quote.author) : quote.author != null) return false;
        if (date != null ? !date.equals(quote.date) : quote.date != null) return false;
        if (user != null ? !user.equals(quote.user) : quote.user != null) return false;
        return uid != null ? uid.equals(quote.uid) : quote.uid == null;
    }

    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        return result;
    }

    public Quote() {

    }
}
