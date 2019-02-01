package com.example.mahnoorkhan.sarcasmania.Classes;

import java.io.Serializable;

public class Post implements Serializable {

    private int tweetID;
    private String tweet;
    private String username;
    private float sarcasm;
    private float humor;
    private float insult;
    private String time;

    public Post() {
    }

    public Post(int tweetID, String tweet, String username, float sarcasm, float humor, float insult, String time) {
        this.tweetID = tweetID;
        this.tweet = tweet;
        this.username = username;
        this.sarcasm = sarcasm;
        this.humor = humor;
        this.insult = insult;
        this.time = time;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public float getSarcasm() {
        return sarcasm;
    }

    public void setSarcasm(float sarcasm) {
        this.sarcasm = sarcasm;
    }

    public float getHumor() {
        return humor;
    }

    public void setHumor(float humor) {
        this.humor = humor;
    }

    public float getInsult() {
        return insult;
    }

    public void setInsult(float insult) {
        this.insult = insult;
    }

    public int getTweetID() { return tweetID; }

    public void setTweetID(int tweetID) { this.tweetID = tweetID; }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }
}
