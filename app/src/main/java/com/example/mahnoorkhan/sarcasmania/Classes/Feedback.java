package com.example.mahnoorkhan.sarcasmania.Classes;

import java.io.Serializable;

public class Feedback implements Serializable {

    private int humorfeedback;
    private int insultfeedback;
    private String username;
    private int tweetID;

    public Feedback() {
    }

    public Feedback(int humorfeedback, int insultfeedback, String username, int tweetID) {
        this.humorfeedback = humorfeedback;
        this.insultfeedback = insultfeedback;
        this.username = username;
        this.tweetID = tweetID;
    }

    public int getHumorfeedback() {
        return humorfeedback;
    }

    public void setHumorfeedback(int humorfeedback) {
        this.humorfeedback = humorfeedback;
    }

    public int getInsultfeedback() {
        return insultfeedback;
    }

    public void setInsultfeedback(int insultfeedback) {
        this.insultfeedback = insultfeedback;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTweetID() {
        return tweetID;
    }

    public void setTweetID(int tweetID) {
        this.tweetID = tweetID;
    }
}
