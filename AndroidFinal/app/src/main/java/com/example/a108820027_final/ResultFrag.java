package com.example.a108820027_final;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String IS_REVEAL_ALL = "IsReveal";
    private static final String TIME_KEY = "Time";
    private static final String ACCURACY_KEY = "Accuracy";
    private static final String THEME_KEY = "GameTheme";
    private static final long MAX_POINT_DEFAULT_TIME = 60000;
    private static final long MAX_POINT = 10000;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean isReveal;
    private long resultTime;
    private float resultAccuracy;
    private int usedTheme;

    public ResultFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResultFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static ResultFrag newInstance(String param1, String param2) {
        ResultFrag fragment = new ResultFrag();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            usedTheme = getArguments().getInt(THEME_KEY);
            isReveal = getArguments().getBoolean(IS_REVEAL_ALL);
            if(!isReveal){
                resultTime = getArguments().getLong(TIME_KEY);
                resultAccuracy = getArguments().getFloat(ACCURACY_KEY);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState ) {
        super.onViewCreated(view, savedInstanceState);
        Button retryButton = getView().findViewById(R.id.retryButton);
        Button returnButton = getView().findViewById(R.id.returnButton);
        retryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Bundle gameBundle = new Bundle();
                gameBundle.putInt(THEME_KEY, usedTheme);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.main_container, GameFrag.class, gameBundle);
                ft.commit();
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.main_container, MainFrag.class, null);
                ft.commit();
            }
        });

        TextView accText = getView().findViewById(R.id.accuracyText);
        TextView timeText = getView().findViewById(R.id.timeText);
        TextView scoreText = getView().findViewById(R.id.pointText);
        String accTemp;
        String timeTemp;
        String scoreTemp;
        if (!isReveal) {
            accTemp = getResources().getString(R.string.accuracy) + Integer.toString((int) resultAccuracy) + " %";
            timeTemp = getResources().getString(R.string.time) + Integer.toString((int) (resultTime / 1000)) + " s";
            scoreTemp = getResources().getString(R.string.points) + Integer.toString(getPoints());
        }else{
            accTemp = getResources().getString(R.string.accuracy) + " -- ";
            timeTemp = getResources().getString(R.string.time) + " -- ";
            scoreTemp = getResources().getString(R.string.points) + " -- ";
        }
        accText.setText(accTemp);
        timeText.setText(timeTemp);
        scoreText.setText(scoreTemp);
    }

    private int getPoints(){
        float result = MAX_POINT;
        if(resultTime > MAX_POINT_DEFAULT_TIME){
            result = MAX_POINT - ((float)(resultTime - MAX_POINT_DEFAULT_TIME)/ 1000 * 300);
        }
        result = result * (resultAccuracy / 100);

        return (int)result;
    }
}