package com.waperr.aalaundry.main.handle_fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.waperr.aalaundry.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment {

    private ExpandableRelativeLayout expandableLayout1, expandableLayout2, expandableLayout3, expandableLayout4,
            expandableLayout5, expandableLayout6, expandableLayout7;
    private Button bt1,bt2,bt3,bt4,bt5,bt6,bt7;

    public HelpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_help, container, false);
        //((MainActivity) getActivity()).setTitle("Bantuan");

        bt1 = (Button) rootView.findViewById(R.id.expandableButton1);
        bt2 = (Button) rootView.findViewById(R.id.expandableButton2);
        bt3 = (Button) rootView.findViewById(R.id.expandableButton3);
        bt4 = (Button) rootView.findViewById(R.id.expandableButton4);
        bt5 = (Button) rootView.findViewById(R.id.expandableButton5);
        bt6 = (Button) rootView.findViewById(R.id.expandableButton6);
        bt7 = (Button) rootView.findViewById(R.id.expandableButton7);
        expandableLayout1 = (ExpandableRelativeLayout) rootView.findViewById(R.id.expandableLayout1);
        expandableLayout2 = (ExpandableRelativeLayout) rootView.findViewById(R.id.expandableLayout2);
        expandableLayout3 = (ExpandableRelativeLayout) rootView.findViewById(R.id.expandableLayout3);
        expandableLayout4 = (ExpandableRelativeLayout) rootView.findViewById(R.id.expandableLayout4);
        expandableLayout5 = (ExpandableRelativeLayout) rootView.findViewById(R.id.expandableLayout5);
        expandableLayout6 = (ExpandableRelativeLayout) rootView.findViewById(R.id.expandableLayout6);
        expandableLayout7 = (ExpandableRelativeLayout) rootView.findViewById(R.id.expandableLayout7);

        expandableLayout1.post(new Runnable() {
            @Override
            public void run() {
                expandableLayout1.collapse(-1, null);
            }
        });

        expandableLayout2.post(new Runnable() {
            @Override
            public void run() {
                expandableLayout2.collapse(-1, null);
            }
        });

        expandableLayout3.post(new Runnable() {
            @Override
            public void run() {
                expandableLayout3.collapse(-1, null);
            }
        });

        expandableLayout4.post(new Runnable() {
            @Override
            public void run() {
                expandableLayout4.collapse(-1, null);
            }
        });

        expandableLayout5.post(new Runnable() {
            @Override
            public void run() {
                expandableLayout5.collapse(-1, null);
            }
        });

        expandableLayout6.post(new Runnable() {
            @Override
            public void run() {
                expandableLayout6.collapse(-1, null);
            }
        });

        expandableLayout7.post(new Runnable() {
            @Override
            public void run() {
                expandableLayout7.collapse(-1, null);
            }
        });

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandableLayout1.toggle();
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandableLayout2.toggle();
            }
        });

        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandableLayout3.toggle();
            }
        });

        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandableLayout4.toggle();
            }
        });

        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandableLayout5.toggle();
            }
        });

        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandableLayout6.toggle();
            }
        });

        bt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandableLayout7.toggle();
            }
        });


        return rootView;
    }

}
