package com.example.mahnoorkhan.sarcasmania.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
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

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
    Random rand;
    Boolean check;
    String usernameFromLogin;
    TextView postUsername;
    TextView profileUsername;
    DatabaseReference databaseReference;
    GestureDetector gestureDetector;
    View selected;
    RatingBar ratingBar;
    TextView sarcasmScale;
    int num;

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
        rand = new Random();
        final List<Post> temp = new ArrayList<Post>();
        temp.addAll(modelList);
        final int count = temp.size()-1;

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        sarcasmScale = (TextView) findViewById(R.id.textView5);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                sarcasmScale.setVisibility(View.VISIBLE);
                sarcasmScale.setText(String.valueOf(rating));
                switch ((int) ratingBar.getRating()) {
                    case 1:
                        sarcasmScale.setText("Very bad Sarcasm");
                        break;
                    case 2:
                        sarcasmScale.setText("So so hai");
                        break;
                    case 3:
                        sarcasmScale.setText("You getting there bro!");
                        break;
                    case 4:
                        sarcasmScale.setText("Great Sarcasm");
                        break;
                    case 5:
                        sarcasmScale.setText("Oo Burnnn!");
                        break;
                    default:
                        sarcasmScale.setText("");
                }
            }
        });

        swipeCardsView.setCardsSlideListener(new SwipeCardsView.CardsSlideListener() {
            @Override
            public void onShow(int index) {

            }

            @Override
            public void onCardVanish(int index, SwipeCardsView.SlideType type) {
                ratingBar.setRating(0);
                sarcasmScale.setVisibility(View.GONE);
            }

            @Override
            public void onItemClick(View cardImageView, int index) {

            }
        });
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
        final ImageView v=findViewById(R.id.profilePicture);
        final TextView t = (TextView) findViewById(R.id.profileUsername);
        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            User u;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    u = dsp.getValue(User.class);
                    if(u != null) {
                        if (u.getUsername().equals(t.getText().toString())) {
                            String pic = u.getPicture();
                            if(pic != null) {
                                byte outImage[] = Base64.decode(pic, Base64.DEFAULT);
                                ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                                Bitmap theImage = BitmapFactory.decodeStream(imageStream);
                                theImage.compress(Bitmap.CompressFormat.PNG, 50, stream);
                                v.setImageBitmap(theImage);
                            }
                        }
                    }
                }
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
