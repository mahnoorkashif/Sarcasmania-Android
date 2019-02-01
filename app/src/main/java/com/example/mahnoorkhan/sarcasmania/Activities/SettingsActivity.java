package com.example.mahnoorkhan.sarcasmania.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mahnoorkhan.sarcasmania.Classes.FirebaseHelper;
import com.example.mahnoorkhan.sarcasmania.Classes.Post;
import com.example.mahnoorkhan.sarcasmania.Classes.User;
import com.example.mahnoorkhan.sarcasmania.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import javax.xml.transform.Templates;

public class SettingsActivity extends AppCompatActivity {

    android.support.v7.app.ActionBar actionBar;
    EditText username;
    TextView usernamedisplay;
    EditText fullname;
    EditText password;
    EditText email;
    DatabaseReference databaseReference;
    String userFromMain;
    TextView numberofposts;
    TextView average;
    FirebaseAuth firebaseAuth;
    String currentPassword;
    String currentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);     //  Fixed Portrait orientation
        setContentView(R.layout.activity_settings);

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        actionBar = this.getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.actionBarColor)));

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusBarColor));
        }

        username = (EditText) findViewById(R.id.username);
        username.setEnabled(false);
        username.setInputType(InputType.TYPE_NULL);
        username.setFocusable(false);

        userFromMain = getIntent().getExtras().getString("usernamefrommain");

        usernamedisplay = (TextView) findViewById(R.id.settingsusername);
        usernamedisplay.setText(userFromMain);

        username.setText(userFromMain);

        fullname = (EditText) findViewById(R.id.name);

        password = (EditText) findViewById(R.id.password);

        email = (EditText) findViewById(R.id.email);
        email.setEnabled(false);
        email.setInputType(InputType.TYPE_NULL);
        email.setFocusable(false);

        average = (TextView) findViewById(R.id.average);

        firebaseAuth = FirebaseAuth.getInstance();

        numberofposts = (TextView) findViewById(R.id.postnumber);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            User user;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    if(dsp != null) {
                        user = dsp.getValue(User.class);
                        if(user != null) {
                            if(user.getUsername().equals(userFromMain)) {
                                fullname.setText(user.getFullname());
                                password.setText(user.getPassword());
                                email.setText(user.getEmail());
                          }
                      }
                    }
                }
                databaseReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        currentPassword = password.getText().toString();
        currentEmail = email.getText().toString();

        databaseReference.child("Posts").addValueEventListener(new ValueEventListener() {
            Post post;
            int number = 0;
            int averagee = 0;
            float temp = 0;
            float temp2 = 0;
            ArrayList<Float> sarcasmRating = new ArrayList<Float>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    if(dsp != null) {
                        post = dsp.getValue(Post.class);
                        if(post != null) {
                            if(post.getUsername().equals(userFromMain)) {
                                number++;
                                temp  = post.getSarcasm()/20;
                                temp2 = (float)(Math.round(temp*100.0)/100.0);
                                sarcasmRating.add(temp2);
                            }
                        }
                    }
                }
                if(sarcasmRating.size() > 0) {
                    for (int i = 0; i < sarcasmRating.size(); i++) {
                        averagee += sarcasmRating.get(i);
                    }
                    averagee = averagee / sarcasmRating.size();
                }
                numberofposts.setText(Integer.toString(number));
                average.setText(Integer.toString(averagee));
                databaseReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onUpdateClick (View view) {
    }
}
