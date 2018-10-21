package com.invendordev.invendor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ChildFragment extends Fragment {
    public TextView tvParent,tvChild;

    int row=0;
    int column=0;

    public ChildFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =null;
        Bundle bundle = getArguments();
        row=Integer.parseInt(bundle.getString("parent"));
        column=Integer.parseInt(bundle.getString("child"));
        if(bundle.getString("child").equals("0")){
             view = inflater.inflate(R.layout.fragment_video, container, false);
        }
        else {
             view = inflater.inflate(R.layout.fragment_child, container, false);
            // Inflate the layout for this fragment
            tvParent = (TextView) view.findViewById(R.id.tvParent);
            tvChild = (TextView) view.findViewById(R.id.tvChild);

            int topic = (Integer.parseInt(bundle.getString("parent"))+1);
            tvParent.setText("Topic: " + topic);
            tvChild.setText("Detail: " + bundle.getString("child"));
        }
        return view;
    }

  }
