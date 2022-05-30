package com.example.a108820027_final;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private GridLayout mGridLayout;
    private static final int N = 4;
    private final List<FrameLayout> gameCards = new ArrayList<FrameLayout>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GameFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static GameFrag newInstance(String param1, String param2) {
        GameFrag fragment = new GameFrag();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState ) {
        super.onViewCreated(view, savedInstanceState);
        mGridLayout = getView().findViewById(R.id.gameContent);
//        mGridLayout.removeAllViews();
        mGridLayout.setColumnCount(N);
        mGridLayout.setRowCount(N);
        Button homeButton = getView().findViewById(R.id.homeButton);
        Button revealButton = getView().findViewById(R.id.revealButton);

        homeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.main_container, MainFrag.class, null);
                ft.commit();
            }
        });

        revealButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.main_container, ResultFrag.class, null);
                ft.commit();
            }
        });

//        for(int r = 0; r < N; r++){
//            for(int c = 0; c < N; c++){
//                createGridCardView(getContext(), r, c);
//            }
//        }
//        createGridCardView(getContext(), 0, 0);
//        createGridCardView(getContext(), 0, 1);
//        createGridCardView(getContext(), 1, 0);
//        createGridCardView(getContext(), 1, 1);
    }


    public void flipCard(Context context,View visibleView,View inVisibleView) {

        visibleView.setVisibility(View.VISIBLE);

        AnimatorSet flipOutAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.flip_out);
        flipOutAnimatorSet.setTarget(inVisibleView);

        AnimatorSet flipInAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.flip_in);
        flipInAnimatorSet.setTarget(visibleView);

        flipOutAnimatorSet.start();
        flipInAnimatorSet.start();
        flipInAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                inVisibleView.setVisibility(View.GONE);
            }
        });
    }



    private FrameLayout getFrameLayout(int r, int c){
        int index = r * N + c;
        return gameCards.get(index);
    }

    private void createGridCardView(Context context, int row, int col){


//        GridLayout.LayoutParams gridParams = new GridLayout.LayoutParams();
//        gridParams.columnSpec = GridLayout.spec(col);
//        gridParams.rowSpec = GridLayout.spec(row);

//生成FrameLayout
        FrameLayout frameLayout = new FrameLayout(context);
        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        frameParams.gravity = Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL;
        frameLayout.setLayoutParams(frameParams);

//生成card背面
        CardView back = new CardView(context);
        ViewGroup.MarginLayoutParams cardParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        cardParams.setMargins(32, 32, 32, 32);
        back.setLayoutParams(cardParams);
        back.setForegroundGravity(Gravity.FILL);
        back.setCardBackgroundColor(getResources().getColor(R.color.teal_200));

        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(64);
        shape.setColor(getResources().getColor(R.color.teal_200));
        back.setBackground(shape);

//生成Card裡面的內容(LinearLayout)
        LinearLayout linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams((int)(getResources().getDimension(R.dimen.card_linear_size)), (int)(getResources().getDimension(R.dimen.card_linear_size)));
        linearParams.gravity = Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL;
        linearLayout.setLayoutParams(linearParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        ImageView imageView = new ImageView(context);
        ViewGroup.LayoutParams imageParams = new ViewGroup.LayoutParams(R.dimen.card_image_size, R.dimen.card_image_size);
        imageView.setLayoutParams(imageParams);
        imageView.setImageResource(R.drawable.ic_launcher_foreground);

        TextView textView = new TextView(context);
        ViewGroup.LayoutParams textParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(textParams);
        textView.setText(R.string.card_back_text);
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//內容加入到一個LinearLayout
        linearLayout.addView(imageView);
        linearLayout.addView(textView);

//        View l = getLayoutInflater().from(context).inflate(linearLayout, null);
//加入Card
        back.addView(linearLayout);
//加入FrameLayout
        frameLayout.addView(back);
//加入GridLayout
        mGridLayout.addView(frameLayout, new GridLayout.LayoutParams(
                GridLayout.spec(row, GridLayout.CENTER), GridLayout.spec(col, GridLayout.CENTER)));

    }
}