package com.example.mahnoorkhan.sarcasmania.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mahnoorkhan.sarcasmania.R;


public class reportedtweets extends Fragment {

    private OnFragmentInteractionListener mListener;

    public reportedtweets() {
    }


    public static reportedtweets newInstance(String param1, String param2) {
        reportedtweets fragment = new reportedtweets();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reportedtweets, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
//        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        mListener.setRT();
    }




    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
        void setRT();
    }
}
