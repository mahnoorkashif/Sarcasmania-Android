package com.example.mahnoorkhan.sarcasmania.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Paint;
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
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.example.mahnoorkhan.sarcasmania.Adapter.CardAdapter;
import com.example.mahnoorkhan.sarcasmania.Adapter.profileAdapter;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements PostFragment.postInterface, NewfeedFragment.newsFeedInterface, ProfileFragment.OnFragmentInteractionListener, NewfeedFragment.OnFragmentInteractionListener, PostFragment.OnFragmentInteractionListener, ProfileFragment.profileInterface {

    private TabLayout tabLayout;
    private com.example.mahnoorkhan.sarcasmania.Classes.swipeOffViewPager swipeOffViewPager;
    private ArrayList<TabLayout.Tab> tabIdHistory = new ArrayList<TabLayout.Tab>();
    private TabLayout.Tab home;
    private TabLayout.Tab upload;
    private TabLayout.Tab profile;
    android.support.v7.app.ActionBar actionBar;
    private SwipeCardsView swipeCardsView;
    private List<Post> modelList;
    private ArrayList<Post> modelList2;
    private ImageView like;
    private ImageView dislike;
    private TextView skipCard;
    Random rand;
    Boolean check;
    String usernameFromLogin;
    TextView postUsername;
    TextView profileUsername;
    DatabaseReference databaseReference;
    GestureDetector gestureDetector;
    View selected;

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
        usernameFromLogin = getIntent().getExtras().getString("usernamefromlogin");

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
        like = (ImageView) findViewById(R.id.like);
        dislike = (ImageView) findViewById(R.id.dislike);
        Glide.with(getApplicationContext()).load(R.mipmap.insult).into(dislike);
        Glide.with(getApplicationContext()).load(R.mipmap.haha).into(like);

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
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Network not available. Turn on WIFI/4G/3G", Toast.LENGTH_LONG).show());
        }

        //---------------------------------like dislike skipCard buttons and temp data list--------------------------------------
        skipCard = (TextView) findViewById(R.id.skipCard);
        rand = new Random();
        skipCard.setPaintFlags(skipCard.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        final List<Post> temp = new ArrayList<Post>();
        temp.addAll(modelList);
        final int count = temp.size()-1;

    /*    //---------------------------------on swipe listeners--------------------------------------
        swipeCardsView.setCardsSlideListener(new SwipeCardsView.CardsSlideListener() {
            @Override
            public void onShow(int index) {
            }

            @Override
            public void onCardVanish(int index, SwipeCardsView.SlideType type) {
                if(index == count) {
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(),"No More Cards to Display",Toast.LENGTH_LONG).show());
                }

                if(type.equals(SwipeCardsView.SlideType.RIGHT)) {
                    if (check == false) {
                        final Bitmap bitmap = ((BitmapDrawable) like.getDrawable()).getBitmap();
                        Drawable drawable = getResources().getDrawable(R.mipmap.heart_grey);
                        final Bitmap heartGrey = ((BitmapDrawable) drawable).getBitmap();
                        Drawable drawable2 = getResources().getDrawable(R.mipmap.heart_purple);
                        final Bitmap heartPurple = ((BitmapDrawable) drawable2).getBitmap();
                        if (bitmap.sameAs(heartGrey)) {
                            Thread myThread = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        like.setImageBitmap(heartPurple);
                                        sleep(300);
                                        like.setImageBitmap(heartGrey);
                                        temp.remove(0);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            myThread.start();
                        }
                    }
                }
                else if (type.equals(SwipeCardsView.SlideType.LEFT)) {
                    if (check == false) {
                        final Bitmap bitmap = ((BitmapDrawable) dislike.getDrawable()).getBitmap();
                        Drawable drawable = getResources().getDrawable(R.mipmap.unheart_grey);
                        final Bitmap unheartGrey = ((BitmapDrawable) drawable).getBitmap();
                        Drawable drawable2 = getResources().getDrawable(R.mipmap.unheart_purple);
                        final Bitmap unheartPurple = ((BitmapDrawable) drawable2).getBitmap();

                        if (bitmap.sameAs(unheartGrey)) {
                            Thread myThread = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        dislike.setImageBitmap(unheartPurple);
                                        sleep(300);
                                        dislike.setImageBitmap(unheartGrey);
                                        temp.remove(0);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            myThread.start();
                        }
                    }
                }
            }

            @Override
            public void onItemClick(View cardImageView, int index) {
            }
        });

        //----------------------------------on like button click listener-------------------------------------
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bitmap bitmap = ((BitmapDrawable) like.getDrawable()).getBitmap();
                Drawable drawable = getResources().getDrawable(R.mipmap.heart_grey);
                final Bitmap heartGrey = ((BitmapDrawable) drawable).getBitmap();
                Drawable drawable2 = getResources().getDrawable(R.mipmap.heart_purple);
                final Bitmap heartPurple = ((BitmapDrawable) drawable2).getBitmap();

                if(bitmap.sameAs(heartGrey)) {
                    Thread myThread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                if(temp.size() == 0) {
                                    like.setImageBitmap(heartGrey);
                                    runOnUiThread(() -> Toast.makeText(getApplicationContext(),"No More Cards to Display",Toast.LENGTH_LONG).show());
                                    return;
                                }
                                like.setImageBitmap(heartPurple);
                                sleep(300);
                                swipeCardsView.slideCardOut(SwipeCardsView.SlideType.RIGHT);
                                like.setImageBitmap(heartGrey);
                                temp.remove(0);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    myThread.start();
                }
            }
        });

        //-------------------------------on dislike button click listener----------------------------------------
        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bitmap bitmap = ((BitmapDrawable) dislike.getDrawable()).getBitmap();
                Drawable drawable = getResources().getDrawable(R.mipmap.unheart_grey);
                final Bitmap unheartGrey = ((BitmapDrawable) drawable).getBitmap();
                Drawable drawable2 = getResources().getDrawable(R.mipmap.unheart_purple);
                final Bitmap unheartPurple = ((BitmapDrawable) drawable2).getBitmap();

                if(bitmap.sameAs(unheartGrey)) {
                    Thread myThread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                if(temp.size() == 0) {
                                    dislike.setImageBitmap(unheartGrey);
                                    runOnUiThread(() -> Toast.makeText(getApplicationContext(),"No More Cards to Display",Toast.LENGTH_LONG).show());
                                    return;
                                }
                                dislike.setImageBitmap(unheartPurple);
                                sleep(300);
                                swipeCardsView.slideCardOut(SwipeCardsView.SlideType.LEFT);
                                dislike.setImageBitmap(unheartGrey);
                                temp.remove(0);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    myThread.start();
                }
            }
        });

        //-------------------------------on skipCard button click listener----------------------------------------
        skipCard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int value = rand.nextInt(2);
                if (temp.size() > 0) {
                    if (value == 0) {
                        check = true;
                        swipeCardsView.slideCardOut(SwipeCardsView.SlideType.RIGHT);
                        temp.remove(0);
                        check = false;
                    }
                    if (value == 1) {
                        check = true;
                        swipeCardsView.slideCardOut(SwipeCardsView.SlideType.LEFT);
                        temp.remove(0);
                        check = false;
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "No More Cards to Display", Toast.LENGTH_LONG).show());
                }
            }
        });
        */
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
    public void setPost() {
        postUsername = (TextView) findViewById(R.id.postUsername);
        postUsername.setText(usernameFromLogin);
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
