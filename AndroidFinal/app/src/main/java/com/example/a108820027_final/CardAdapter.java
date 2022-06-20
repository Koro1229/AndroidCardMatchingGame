package com.example.a108820027_final;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.ArrayList;

public class CardAdapter extends BaseAdapter {

    Context context;
    ArrayList<Card> cards;
    LayoutInflater layoutInflater;
    MatchingGame matchingGame;
    private final Button revealButton;
    private final TextView accText;
    private final int TARGET_FLIP = 2;
    private int currentFlip = 0;
    private int animation_flag = 0;
    private int finish_flag = 0;

    public CardAdapter(Context context, ArrayList<Card> card, TextView at, MatchingGame mg, Button reveal){
        this.matchingGame = mg;
        this.context = context;
        this.cards = card;
        this.accText = at;
        this.revealButton = reveal;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.card_item, null); // inflate the layout
        CardView front = view.findViewById(R.id.front_card);
        CardView back = view.findViewById(R.id.back_card);
        ImageView icon = (ImageView) view.findViewById(R.id.front_card_image);
        icon.setImageResource(cards.get(i).getSource());
//        TextView text = view.findViewById(R.id.front_card_text);
//        text.setText(Integer.toString(cards.get(i).id));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                matchingGame.chooseCard(i, view);
                if(front.getVisibility() == View.GONE){
                    if(currentFlip < TARGET_FLIP){
                        flipCard(view, front, back);
                        currentFlip += 1;//have to be here or it will be a shit
                    }
                }else{
                    flipCard(view, back, front);
                    currentFlip -= 1;
                }
            }
        });
        return view;
    }

    private void checkMatch(){
        if(matchingGame.isChooseFinished()){
            if(matchingGame.checkMatch()){
                showPass();
            }else{
                View firstView = matchingGame.getFirstView();
                CardView front = firstView.findViewById(R.id.front_card);
                CardView back = firstView.findViewById(R.id.back_card);
                flipCard(firstView, back, front);
                View secondView = matchingGame.getSecondView();
                front = secondView.findViewById(R.id.front_card);
                back = secondView.findViewById(R.id.back_card);
                flipCard(secondView, back, front);
            }
            currentFlip = 0;
            setGameText();
        }
    }

    private void showPass(){
        View firstView = matchingGame.getFirstView();
        CardView front = firstView.findViewById(R.id.front_card);
        ImageView pass = firstView.findViewById(R.id.pass);
        pass.setVisibility(View.VISIBLE);
        AnimatorSet flipOutAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.flip_out);
        flipOutAnimatorSet.setTarget(front);
        flipOutAnimatorSet.addListener(new AnimatorListenerAdapter(){
            @Override
            public void onAnimationStart(Animator animation){
                super.onAnimationStart(animation);
                firstView.setClickable(false);
                finish_flag += 1;
            }
            @Override
            public void onAnimationEnd(Animator animation){
                super.onAnimationEnd(animation);
                front.setVisibility(View.GONE);
                finish_flag -= 1;
            }
        });
        AnimatorSet fadeInAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.fade_in);
        fadeInAnimatorSet.setTarget(pass);
        fadeInAnimatorSet.addListener(new AnimatorListenerAdapter(){
            @Override
            public void onAnimationEnd(Animator animation){
                super.onAnimationEnd(animation);
            }
        });
        flipOutAnimatorSet.start();
        fadeInAnimatorSet.start();

        View secondView = matchingGame.getSecondView();
        CardView secondFront = secondView.findViewById(R.id.front_card);
        ImageView secondPass = secondView.findViewById(R.id.pass);
        secondPass.setVisibility(View.VISIBLE);
        AnimatorSet secondFlipOutAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.flip_out);
        secondFlipOutAnimatorSet.setTarget(secondFront);
        secondFlipOutAnimatorSet.addListener(new AnimatorListenerAdapter(){
            @Override
            public void onAnimationStart(Animator animation){
                super.onAnimationStart(animation);
                secondView.setClickable(false);
                finish_flag += 1;
            }
            @Override
            public void onAnimationEnd(Animator animation){
                super.onAnimationEnd(animation);
                secondFront.setVisibility(View.GONE);
                finish_flag -= 1;
            }
        });
        AnimatorSet secondFadeInAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.fade_in);
        secondFadeInAnimatorSet.setTarget(secondPass);
        secondFadeInAnimatorSet.addListener(new AnimatorListenerAdapter(){
            @Override
            public void onAnimationEnd(Animator animation){
                super.onAnimationEnd(animation);
                if(finish_flag == 0) {
                    matchingGame.checkGameFinish();
                }
            }
        });
        secondFlipOutAnimatorSet.start();
        secondFadeInAnimatorSet.start();
    }

    private void flipCard(View mainView,View visibleView,View inVisibleView) {

        visibleView.setVisibility(View.VISIBLE);

        AnimatorSet flipOutAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.flip_out);
        flipOutAnimatorSet.setTarget(inVisibleView);

        AnimatorSet flipInAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.flip_in);
        flipInAnimatorSet.setTarget(visibleView);


        flipOutAnimatorSet.addListener(new AnimatorListenerAdapter(){
            @Override
            public void onAnimationStart(Animator animation){
                super.onAnimationStart(animation);
                mainView.setClickable(false);
                revealButton.setClickable(false);
                animation_flag += 1;
            }
        });
        flipInAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                inVisibleView.setVisibility(View.GONE);
                mainView.setClickable(true);
                revealButton.setClickable(true);
                animation_flag -= 1;
                if(animation_flag == 0){
                    checkMatch();
                }
            }
        });

        flipOutAnimatorSet.start();
        flipInAnimatorSet.start();
    }

    private void setGameText(){
        float temp = matchingGame.getAccuracy();
        String tempText;
        if(temp == -1){
            tempText = context.getString(R.string.accuracy) + "--";
        }else{
            tempText = context.getString(R.string.accuracy) + Integer.toString((int) matchingGame.getAccuracy()) + " %";
        }
        accText.setText(tempText);
    }

    public void revealAll(){
        matchingGame.revealAll();
        currentFlip = 0;
        setGameText();
    }

    public void foldAll(){
        matchingGame.foldAll();
    }

}
