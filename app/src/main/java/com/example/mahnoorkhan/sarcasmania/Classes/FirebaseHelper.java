package com.example.mahnoorkhan.sarcasmania.Classes;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {
    private static DatabaseReference databaseReference;

    public FirebaseHelper() {
        if(databaseReference == null) {
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
    }

    public static DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public void newUser(String username, String fullname, String email, String password, String picture) {
        User user = new User(username, fullname, email, password, picture);
        databaseReference.child("Users").child(user.getUsername()).setValue(user);
    }

    public void newPost(int tweetID, String tweet, String username, float sarcasm, float humor, float insult, String time) {
        Post post = new Post(tweetID, tweet, username, sarcasm, humor, insult, time);
        databaseReference.child("Posts").child(Integer.toString(post.getTweetID())).setValue(post);
    }
}
