package com.example.a108820027_final;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFrag extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String THEME_KEY = "GameTheme";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Bundle gameBundle;
    private static final int POKER_THEME = 1;
    private static final int WORD_THEME = 2;

    public MainFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFrag newInstance(String param1, String param2) {
        MainFrag fragment = new MainFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        gameBundle = new Bundle();
        gameBundle.putInt(THEME_KEY, POKER_THEME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState ) {
        super.onViewCreated(view, savedInstanceState);
        Button startButton = getView().findViewById(R.id.startButton);
        Button pokerButton = getView().findViewById(R.id.pokerButton);
        Button wordButton = getView().findViewById(R.id.wordButton);
        ImageView mainImage = getView().findViewById(R.id.mainImage);

        startButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.main_container, GameFrag.class, gameBundle);
                ft.commit();
            }
        });

        pokerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                pokerButton.setClickable(false);
                wordButton.setClickable(true);
                setBackgroundTint(pokerButton, R.color.orange);
                setBackgroundTint(wordButton, R.color.purple_500);
                mainImage.setImageResource(R.drawable.poker);
                gameBundle = new Bundle();
                gameBundle.putInt(THEME_KEY, POKER_THEME);
            }
        });

        wordButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                pokerButton.setClickable(true);
                wordButton.setClickable(false);
                setBackgroundTint(pokerButton, R.color.purple_500);
                setBackgroundTint(wordButton, R.color.orange);
                mainImage.setImageResource(R.drawable.word);
                gameBundle = new Bundle();
                gameBundle.putInt(THEME_KEY, WORD_THEME);
            }
        });

        pokerButton.setClickable(false);

    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }


    private void setBackgroundTint(Button button, int color){
        Drawable buttonDrawable = button.getBackground();
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
        DrawableCompat.setTint(buttonDrawable, getResources().getColor(color));
        button.setBackground(buttonDrawable);
    }

}