package com.example.mahnoorkhan.sarcasmania.Adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.example.mahnoorkhan.sarcasmania.Classes.Post;
import com.example.mahnoorkhan.sarcasmania.R;
import com.huxq17.swipecardsview.BaseCardAdapter;

import java.util.List;

public class CardAdapter extends BaseCardAdapter {

    private List<Post> modelList;
    private Context context;

    public CardAdapter(List<Post> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public int getCardLayoutId() {
        return R.layout.card_item;
    }

    @Override
    public void onBindData(int position, View cardview) {
        if (modelList == null || modelList.size() == 0) {
            return;
        }
        //-------------------- username or tweet set ho rahi hai bruh-------------------
        cardview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context,"Tweet Reported",Toast.LENGTH_LONG).show();
                return true;
            }
        });

        Post post = modelList.get(position);

        TextView tweet = (TextView) cardview.findViewById(R.id.theTweet);
        TextView userName = (TextView) cardview.findViewById(R.id.theUsername);
        //RatingBar sarcasmRating = (RatingBar) cardview.findViewById(R.id.ratingBar);
        //CrystalRangeSeekbar crystalRangeSeekbar = (CrystalRangeSeekbar) cardview.findViewById(R.id.rangeSeekbar1);
        //TextView humor = (TextView) cardview.findViewById(R.id.textView4);
        //TextView insult = (TextView) cardview.findViewById(R.id.textView3);
        TextView timeStamp = (TextView) cardview.findViewById(R.id.time) ;

        float humorValue = post.getHumor();
        float insultValue = post.getInsult();
        Drawable drawable1 = context.getResources().getDrawable(R.mipmap.thumbend);
        final Bitmap chandler = ((BitmapDrawable) drawable1).getBitmap();
        Drawable drawable2 = context.getResources().getDrawable(R.mipmap.thumbstart);
        final Bitmap purpledot = ((BitmapDrawable) drawable2).getBitmap();

        tweet.setText(post.getTweet());
        userName.setText(post.getUsername());
        timeStamp.setText(post.getTime());
        float postSarcasmRating = post.getSarcasm()/20;
        /*sarcasmRating.setRating((float)(Math.round(postSarcasmRating*100.0)/100.0));
        if(humorValue > insultValue) {
            crystalRangeSeekbar.setMinStartValue(0);
            crystalRangeSeekbar.setMaxStartValue(humorValue*10);
            crystalRangeSeekbar.setLeftThumbBitmap(purpledot);
            crystalRangeSeekbar.setRightThumbBitmap(chandler);
        }
        if(insultValue > humorValue) {
            crystalRangeSeekbar.setMinStartValue(insultValue*-10);
            crystalRangeSeekbar.setMaxStartValue(0);
            crystalRangeSeekbar.setRightThumbBitmap(purpledot);
            crystalRangeSeekbar.setLeftThumbBitmap(chandler);
        }
        if(humorValue == insultValue) {
            crystalRangeSeekbar.setMinStartValue(0);
            crystalRangeSeekbar.setMaxStartValue(0);
            crystalRangeSeekbar.setLeftThumbBitmap(chandler);
            crystalRangeSeekbar.setRightThumbBitmap(chandler);
        }
        humor.setText("\tHumor: " + humorValue*100 + "%");
        insult.setText("\tInsult: " + insultValue*100 + "%");

        crystalRangeSeekbar.setEnabled(false);
        crystalRangeSeekbar.apply();
        */
    }
}
