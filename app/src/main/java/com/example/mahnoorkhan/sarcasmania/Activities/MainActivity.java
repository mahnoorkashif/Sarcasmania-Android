package com.example.mahnoorkhan.sarcasmania.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mahnoorkhan.sarcasmania.Adapter.CardAdapter;
import com.example.mahnoorkhan.sarcasmania.Adapter.profileAdapter;
import com.example.mahnoorkhan.sarcasmania.Classes.FirebaseHelper;
import com.example.mahnoorkhan.sarcasmania.Classes.User;
import com.example.mahnoorkhan.sarcasmania.Fragment.NewfeedFragment;
import com.example.mahnoorkhan.sarcasmania.Fragment.PostFragment;
import com.example.mahnoorkhan.sarcasmania.Fragment.ProfileFragment;
import com.example.mahnoorkhan.sarcasmania.Adapter.MainActivityPagerAdapter;
import com.example.mahnoorkhan.sarcasmania.Classes.Post;
import com.example.mahnoorkhan.sarcasmania.R;
import com.example.mahnoorkhan.sarcasmania.Classes.swipeOffViewPager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.huxq17.swipecardsview.SwipeCardsView;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PostFragment.postInterface, NewfeedFragment.newsFeedInterface, ProfileFragment.OnFragmentInteractionListener, NewfeedFragment.OnFragmentInteractionListener, PostFragment.OnFragmentInteractionListener, ProfileFragment.profileInterface {

    private TabLayout tabLayout;
    private com.example.mahnoorkhan.sarcasmania.Classes.swipeOffViewPager swipeOffViewPager;
    private ArrayList<TabLayout.Tab> tabIdHistory = new ArrayList<TabLayout.Tab>();
    private TabLayout.Tab home;
    private TabLayout.Tab upload;
    private TabLayout.Tab profile;
    private android.support.v7.app.ActionBar actionBar;
    private SwipeCardsView swipeCardsView;
    private List<Post> modelList;
    private ArrayList<Post> modelList2;
    private Boolean check;
    private String usernameFromLogin;
    private TextView postUsername;
    private TextView profileUsername;
    private DatabaseReference databaseReference;
    private FirebaseHelper firebaseHelper;
    private TextView sarcasmScale;
    private int num;
    private Button createPost;
    private EditText newPost;
    private ImageView humorous;
    private ImageView insulting;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);     //  Fixed Portrait orientation
        setContentView(R.layout.activity_main);
        setTitle("NewsFeed");


        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        actionBar = this.getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.actionBarColor)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusBarColor));
        }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.bringToFront();

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        home = tabLayout.newTab().setIcon(R.mipmap.home_purple);
        upload = tabLayout.newTab().setIcon(R.mipmap.newpost_grey);
        profile = tabLayout.newTab().setIcon(R.mipmap.profile_grey);

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.hintcolor));

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        tabLayout.addTab(home);
        tabLayout.addTab(upload);
        tabLayout.addTab(profile);

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        tabIdHistory.add(home);

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        swipeOffViewPager = (swipeOffViewPager) findViewById(R.id.viewPager);

        MainActivityPagerAdapter mainActivityPagerAdapter = new MainActivityPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        swipeOffViewPager.setAdapter(mainActivityPagerAdapter);
        swipeOffViewPager.setPagingEnabled(false); // here i am **

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        swipeOffViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                swipeOffViewPager.setCurrentItem(tab.getPosition());
                switch(tab.getPosition())
                {
                    case 0:
                        home.setIcon(R.mipmap.home_purple);
                        upload.setIcon(R.mipmap.newpost_grey);
                        profile.setIcon(R.mipmap.profile_grey);
                        tabIdHistory.remove(tab);
                        tabIdHistory.add(tab);
                        setTitle("NewsFeed");
                        num = 1;
                        break;
                    case 1:
                        home.setIcon(R.mipmap.home_grey);
                        upload.setIcon(R.mipmap.newpost_purple);
                        profile.setIcon(R.mipmap.profile_grey);
                        tabIdHistory.remove(tab);
                        tabIdHistory.add(tab);
                        setTitle("New Post");
                        break;
                    case 2:
                        home.setIcon(R.mipmap.home_grey);
                        upload.setIcon(R.mipmap.newpost_grey);
                        profile.setIcon(R.mipmap.profile_purple);
                        tabIdHistory.remove(tab);
                        tabIdHistory.add(tab);
                        setTitle("Profile");
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseHelper = new FirebaseHelper();
        usernameFromLogin = getIntent().getExtras().getString("usernamefromlogin");

        num = 1;

    }

    @Override
    public void onBackPressed() {
        if (tabIdHistory.size() > 0)
        {
            tabIdHistory.remove(tabIdHistory.size() - 1);
            if(tabIdHistory.size() > 0)
            {
                tabIdHistory.get(tabIdHistory.size()-1).select();
            }
            else
            {
                FragmentManager mgr = getSupportFragmentManager();
                if (mgr.getBackStackEntryCount() == 0) {
                    super.onBackPressed();
                } else {
                    mgr.popBackStack();
                }
            }
        } else
        {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void setNewsFeed() {
        final Context c = this;
        //------------------------------- get swipe cards view----------------------------------------
        swipeCardsView = (SwipeCardsView)findViewById(R.id.swipeCardsView);
        swipeCardsView.retainLastCard(false);
        swipeCardsView.enableSwipe(true);
        check = false;
        modelList = new ArrayList<Post>();
        if(isNetworkAvaliable(this)) {
            databaseReference.child("Posts").addValueEventListener(new ValueEventListener() {
                Post post;
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        if (dsp != null) {
                            post = dsp.getValue(Post.class);
                            if (post != null) {
                                if(!post.getUsername().equals(usernameFromLogin)) {
                                    modelList.add(0, post);
                                }
                            }
                        }
                    }
                    CardAdapter cardAdapter = new CardAdapter(modelList, getBaseContext());
                    swipeCardsView.setAdapter(cardAdapter);

                    if(num == 1) {

                        //------------------------ marking humorous ------------------------------------------
                        humorous = (ImageView) findViewById(R.id.imageView7);

                        humorous.setClickable(true);
                        humorous.setOnClickListener(v -> {

                            final Bitmap bitmap = ((BitmapDrawable) humorous.getDrawable()).getBitmap();
                            Drawable drawable = getDrawable(R.mipmap.heart_grey);
                            Bitmap heartGrey = ((BitmapDrawable) drawable).getBitmap();
                            Drawable drawable2 = getDrawable(R.mipmap.heart_purple);
                            Bitmap heartPurple = ((BitmapDrawable) drawable2).getBitmap();

                            if (bitmap.sameAs(heartPurple)) {
                                humorous.setImageBitmap(heartGrey);
                                firebaseHelper.humorFeed(0, usernameFromLogin, modelList.get(0).getTweetID());
                                RequestQueue queue = Volley.newRequestQueue(c);
                                String url = "https://humor-feedback.herokuapp.com/api/sarcasmania?text=" + modelList.get(0).getTweet() + "&label=0";
                                JsonObjectRequest stringRequest = new JsonObjectRequest(url, null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                Toast.makeText(c, "Thank you for your feedback.", Toast.LENGTH_LONG).show();
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(c, "Sorry! That didn't work, Please try again.", Toast.LENGTH_LONG).show();
                                    }
                                });
                                queue.add(stringRequest);
                            }
                            if (bitmap.sameAs(heartGrey)) {
                                humorous.setImageBitmap(heartPurple);
                                firebaseHelper.humorFeed(1, usernameFromLogin, modelList.get(0).getTweetID());
                                RequestQueue queue = Volley.newRequestQueue(c);
                                String url = "https://humor-feedback.herokuapp.com/api/sarcasmania?text=" + modelList.get(0).getTweet() + "&label=1";
                                JsonObjectRequest stringRequest = new JsonObjectRequest(url, null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                Toast.makeText(c, "Thank you for your feedback.", Toast.LENGTH_LONG).show();
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(c, "Sorry! That didn't work, Please try again.", Toast.LENGTH_LONG).show();
                                    }
                                });
                                queue.add(stringRequest);
                            }
                        });

                        //------------------------ marking insulting ------------------------------------------
                        insulting = (ImageView) findViewById(R.id.imageView6);

                        insulting.setClickable(true);
                        insulting.setOnClickListener(v -> {

                            final Bitmap bitmap2 = ((BitmapDrawable) insulting.getDrawable()).getBitmap();
                            Drawable drawable1 = getDrawable(R.mipmap.unheart_grey);
                            Bitmap unheartGrey = ((BitmapDrawable) drawable1).getBitmap();
                            Drawable drawable3 = getDrawable(R.mipmap.unheart_purple);
                            Bitmap unheartPurple = ((BitmapDrawable) drawable3).getBitmap();

                            if (bitmap2.sameAs(unheartPurple)) {
                                insulting.setImageBitmap(unheartGrey);
                                firebaseHelper.insultFeed(0, usernameFromLogin, modelList.get(0).getTweetID());
                                Toast.makeText(c, "Thank you for your feedback.", Toast.LENGTH_LONG).show();
                            }
                            if (bitmap2.sameAs(unheartGrey)) {
                                insulting.setImageBitmap(unheartPurple);
                                firebaseHelper.insultFeed(1, usernameFromLogin, modelList.get(0).getTweetID());
                                Toast.makeText(c, "Thank you for your feedback.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    swipeCardsView.setCardsSlideListener(new SwipeCardsView.CardsSlideListener() {
                        @Override
                        public void onShow(int index) {

                            num++;

                            //------------------------ marking humorous ------------------------------------------
                            humorous = (ImageView) findViewById(R.id.imageView7);

                            humorous.setClickable(true);
                            humorous.setOnClickListener(v -> {

                                final Bitmap bitmap = ((BitmapDrawable) humorous.getDrawable()).getBitmap();
                                Drawable drawable = getDrawable(R.mipmap.heart_grey);
                                Bitmap heartGrey = ((BitmapDrawable) drawable).getBitmap();
                                Drawable drawable2 = getDrawable(R.mipmap.heart_purple);
                                Bitmap heartPurple = ((BitmapDrawable) drawable2).getBitmap();

                                if (bitmap.sameAs(heartPurple)) {
                                    humorous.setImageBitmap(heartGrey);
                                    firebaseHelper.humorFeed(0, usernameFromLogin, modelList.get(index).getTweetID());
                                    RequestQueue queue = Volley.newRequestQueue(c);
                                    String url = "https://humor-feedback.herokuapp.com/api/sarcasmania?text=" + modelList.get(index).getTweet() + "&label=0";
                                    JsonObjectRequest stringRequest = new JsonObjectRequest(url, null,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    Toast.makeText(c, "Thank you for your feedback.", Toast.LENGTH_LONG).show();
                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(c, "Sorry! That didn't work, Please try again.", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    queue.add(stringRequest);
                                }
                                if (bitmap.sameAs(heartGrey)) {
                                    humorous.setImageBitmap(heartPurple);
                                    firebaseHelper.humorFeed(1, usernameFromLogin, modelList.get(index).getTweetID());
                                    RequestQueue queue = Volley.newRequestQueue(c);
                                    String url = "https://humor-feedback.herokuapp.com/api/sarcasmania?text=" + modelList.get(index).getTweet() + "&label=1";
                                    JsonObjectRequest stringRequest = new JsonObjectRequest(url, null,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    Toast.makeText(c, "Thank you for your feedback.", Toast.LENGTH_LONG).show();
                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(c, "Sorry! That didn't work, Please try again.", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    queue.add(stringRequest);
                                }
                            });

                            //------------------------ marking insulting ------------------------------------------
                            insulting = (ImageView) findViewById(R.id.imageView6);

                            insulting.setClickable(true);
                            insulting.setOnClickListener(v -> {

                                final Bitmap bitmap2 = ((BitmapDrawable) insulting.getDrawable()).getBitmap();
                                Drawable drawable1 = getDrawable(R.mipmap.unheart_grey);
                                Bitmap unheartGrey = ((BitmapDrawable) drawable1).getBitmap();
                                Drawable drawable3 = getDrawable(R.mipmap.unheart_purple);
                                Bitmap unheartPurple = ((BitmapDrawable) drawable3).getBitmap();

                                if (bitmap2.sameAs(unheartPurple)) {
                                    insulting.setImageBitmap(unheartGrey);
                                    firebaseHelper.insultFeed(0, usernameFromLogin, modelList.get(index).getTweetID());
                                    Toast.makeText(c, "Thank you for your feedback.", Toast.LENGTH_LONG).show();
                                }
                                if (bitmap2.sameAs(unheartGrey)) {
                                    insulting.setImageBitmap(unheartPurple);
                                    firebaseHelper.insultFeed(1, usernameFromLogin, modelList.get(index).getTweetID());
                                    Toast.makeText(c, "Thank you for your feedback.", Toast.LENGTH_LONG).show();
                                }
                            });

                        }

                        @Override
                        public void onCardVanish(int index, SwipeCardsView.SlideType type) {
                        }

                        @Override
                        public void onItemClick(View cardImageView, int index) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Network not available. Turn on WIFI/4G/3G", Toast.LENGTH_LONG).show());
        }
    }

    //------------------------options menu for settings bruh-------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.settings){
            Intent intent;
            intent = new Intent(MainActivity.this, SettingsActivity.class);
            intent.putExtra("usernamefrommain",usernameFromLogin);
            startActivity(intent);
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void setProfile() {
        profileUsername = (TextView) findViewById(R.id.profileUsername);
        profileUsername.setText(usernameFromLogin);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setHasFixedSize(true);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        modelList2 = new ArrayList<Post>();
        databaseReference.child("Posts").addValueEventListener(new ValueEventListener() {
            Post post;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    if(dsp != null) {
                        post = dsp.getValue(Post.class);
                        if(post != null) {
                            if(post.getUsername().equals(usernameFromLogin)) {
                                modelList2.add(0, post);
                            }
                        }
                    }
                }
                profileAdapter profileAdapter = new profileAdapter(MainActivity.this,modelList2);
                recyclerView.setAdapter(profileAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void setImage() {
//        final ImageView v=(ImageView)findViewById(R.id.profilePicture);
//        final TextView t = (TextView) findViewById(R.id.profileUsername);
//        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
//            User u;
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
//                    u = dsp.getValue(User.class);
//                    if(u != null) {
//                        if (u.getUsername().equals(t.getText().toString())) {
//                            String pic = u.getPicture();
//                            if(pic != null) {
//                                byte outImage[] = Base64.decode(pic, Base64.DEFAULT);
//                                ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
//                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//
//                                Bitmap theImage = BitmapFactory.decodeStream(imageStream);
//                                theImage.compress(Bitmap.CompressFormat.PNG, 50, stream);
//                                v.setImageBitmap(theImage);
//                            }
//                        }
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    @Override
    public void setPost() {
        postUsername = (TextView) findViewById(R.id.postUsername);
        createPost = (Button) findViewById(R.id.post_submit);
        newPost = (EditText) findViewById(R.id.post_text);
        postUsername.setText(usernameFromLogin);

        final Context c = this;

        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = ProgressDialog.show(c, "Calculating Scores...", "Please wait...", true);
                String text = newPost.getText().toString();
                if(text.length() <= 0 || text.isEmpty()) {
                    progressDialog.dismiss();
                    Toast.makeText(c,"You don't seem to be Sarcastic AT ALL! Type Something",Toast.LENGTH_LONG).show();
                }
                if(text.split("\\s+").length < 4) {
                    progressDialog.dismiss();
                    Toast.makeText(c,"Sentence should have atleast 4 words! :)",Toast.LENGTH_LONG).show();
                }
                else {
                    RequestQueue sarcasm_insult = Volley.newRequestQueue(c);
                    String url1 = "https://sarcasmania-api.herokuapp.com/api/sarcasmania?text=" + text;
                    JsonObjectRequest sarcasmAndInsult = new JsonObjectRequest(url1, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                int sarcasm = jsonObject.optInt("Sarcasm");
                                int insult = jsonObject.optInt("Insult");

                                if(insult >= 51 && insult <= 100) {
                                    insult = 1;
                                }
                                else if (insult >= 0 && insult <=50) {
                                    insult = 0;
                                }
                                final int finalInsult = insult;

                                RequestQueue humorScore = Volley.newRequestQueue(c);
//                                String url2 = "https://humor-score.herokuapp.com/api/sarcasmania?text=" + text;
                                String url2 = "https://sarcasmania-test.herokuapp.com/api/sarcasmania?text=" + text;
                                JsonObjectRequest humorRequest = new JsonObjectRequest(url2, null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject jsonObject) {
                                            int humor = jsonObject.optInt("Humor");
                                            Calendar calendar = Calendar.getInstance();
                                            SimpleDateFormat mdformat = new SimpleDateFormat("EEEE h:mm a");
                                            String dateAndTime = mdformat.format(calendar.getTime());
                                            databaseReference.child("Posts").addListenerForSingleValueEvent(new ValueEventListener() {
                                                Post post;
                                                int count = 0;
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                                        if(dsp != null) {
                                                            post = dsp.getValue(Post.class);
                                                            if(post != null) {
                                                                count = post.getTweetID();
                                                            }
                                                        }
                                                    }
                                                    firebaseHelper.newPost(count+1,text,usernameFromLogin,sarcasm,humor,finalInsult,dateAndTime);
                                                    progressDialog.dismiss();
                                                    Toast.makeText(c, "Posted Successfully!", Toast.LENGTH_LONG).show();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    Toast.makeText(c,"Sorry that didn't work (Firebase)",Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    progressDialog.dismiss();
                                    Toast.makeText(c,"Sorry that didn't work (Humor)",Toast.LENGTH_LONG).show();
                                }});
                                humorScore.add(humorRequest);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Toast.makeText(c,"Sorry that didn't work (Sarcasm+Insult)",Toast.LENGTH_LONG).show();
                    }});
                    sarcasm_insult.add(sarcasmAndInsult);
                }
            }
        });

    }

    public static boolean isNetworkAvaliable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) || (connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState() == NetworkInfo.State.CONNECTED)) {
            return true;
        } else {
            return false;
        }
    }
}
