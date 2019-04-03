package com.example.mahnoorkhan.sarcasmania.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.example.mahnoorkhan.sarcasmania.Activities.MainActivity;
import com.example.mahnoorkhan.sarcasmania.Classes.Post;
import com.example.mahnoorkhan.sarcasmania.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class profileAdapter extends RecyclerView.Adapter<profileAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Post> List;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        TextView textView2;
        ImageView haha;
        ImageView insult;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            textView2 = (TextView) itemView.findViewById(R.id.timess);
//            haha = (ImageView) itemView.findViewById(R.id.imageView3);
//            insult = (ImageView) itemView.findViewById(R.id.imageView4);
        }
    }

    public profileAdapter(Context context, ArrayList<Post> list) {
        this.context = context;
        List = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.profileitem, viewGroup, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TextView tweet = viewHolder.textView;
        TextView times = viewHolder.textView2;
//        ImageView insult = viewHolder.insult;
//        ImageView haha = viewHolder.haha;
        Post post = List.get(i);
        tweet.setText(post.getTweet());
        times.setText(post.getTime());
//        Glide.with(context).load(R.mipmap.insult).into(insult);
//        Glide.with(context).load(R.mipmap.haha).into(haha);
//        haha.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "HUMOROUS", Toast.LENGTH_LONG).show();
//
//            }
//        });
//        insult.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "INSULTING", Toast.LENGTH_LONG).show();
//
//            }
//        });
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("Posts").addValueEventListener(new ValueEventListener() {
                    Post postt;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            if (dsp != null) {
                                postt = dsp.getValue(Post.class);
                                if(postt != null && postt.getTweet().equals(post.getTweet())) {
                                    final Dialog dialog = new Dialog(context);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                                    dialog.setCancelable(false);
                                    dialog.setContentView(R.layout.profilepostinfo);
                                    TextView timee = (TextView) dialog.findViewById(R.id.timesss);
                                    TextView posts = (TextView) dialog.findViewById(R.id.textt);
                                    RatingBar sarcasmRating = (RatingBar) dialog.findViewById(R.id.ratingBar2) ;

                                    timee.setText(post.getTime());
                                    posts.setText(post.getTweet());
                                    float postSarcasmRating = postt.getSarcasm()/20;
                                    sarcasmRating.setRating((float)(Math.round(postSarcasmRating*100.0)/100.0));

                                    float humorValue = postt.getHumor();
                                    float insultValue = postt.getInsult();

                                    TextView humor = (TextView) dialog.findViewById(R.id.humors);
                                    TextView insult = (TextView) dialog.findViewById(R.id.insults);

                                    humor.setText("\tHumor: " + humorValue);
                                    insult.setText("\tInsult: " + insultValue);

                                    dialog.setCancelable(true);
                                    dialog.show();


                                }
                            }
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return List.size();
    }

}
