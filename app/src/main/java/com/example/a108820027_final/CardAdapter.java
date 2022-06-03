package com.example.a108820027_final;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;

public class CardAdapter extends BaseAdapter {

    Context context;
    ArrayList<Card> cards;
    LayoutInflater layoutInflater;
    private final int TARGET_FLIP = 2;
    private int currentFlip = 0;
    private float matchedCards = 0;
    private int firstId = -1;
    private int secondId = -1;
    private View firstView;
    private View secondView;
    private int animation_flag = 0;
    private static final String GAME_FINISH_BROADCAST = BuildConfig.APPLICATION_ID + ".GAME_FINISH_CALL";

    public CardAdapter(Context context, ArrayList<Card> card){
        this.context = context;
        this.cards = card;
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
        TextView text = view.findViewById(R.id.front_card_text);
        text.setText(Integer.toString(cards.get(i).id));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCard(i, view);
                if(front.getVisibility() == View.GONE){
                    if(currentFlip < TARGET_FLIP){
                        flipCard(view, front, back);
                        currentFlip += 1;
                    }
                }else{
                    flipCard(view, back, front);
                    currentFlip -= 1;
                }
        }
        });
        return view;
    }

    public void saveCard(int i, View view){
        if(firstId == -1){
            firstId = i;
            firstView = view;
        }else if(secondId == -1){
            secondId = i;
            secondView = view;
        }
    }

    public void CheckMatch(){
        if(cards.get(firstId).id == cards.get(secondId).id){
            cards.get(firstId).isMatched = true;
            cards.get(secondId).isMatched = true;
            matchedCards += 1;
            showPass();
            Toast.makeText(context, Float.toString(matchedCards), Toast.LENGTH_SHORT).show();
        }else{
            CardView front = firstView.findViewById(R.id.front_card);
            CardView back = firstView.findViewById(R.id.back_card);
            flipCard(firstView, back, front);
            front = secondView.findViewById(R.id.front_card);
            back = secondView.findViewById(R.id.back_card);
            flipCard(secondView, back, front);
        }
        currentFlip = 0;
        firstId = -1;
        secondId = -1;
    }

    public void showPass(){
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
            }
            @Override
            public void onAnimationEnd(Animator animation){
                super.onAnimationEnd(animation);
                front.setVisibility(View.GONE);
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

        View secondFront = secondView.findViewById(R.id.front_card);
        View secondPass = secondView.findViewById(R.id.pass);
        secondPass.setVisibility(View.VISIBLE);
        AnimatorSet secondFlipOutAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.flip_out);
        secondFlipOutAnimatorSet.setTarget(secondFront);
        secondFlipOutAnimatorSet.addListener(new AnimatorListenerAdapter(){
            @Override
            public void onAnimationStart(Animator animation){
                super.onAnimationStart(animation);
                secondView.setClickable(false);
            }
            @Override
            public void onAnimationEnd(Animator animation){
                super.onAnimationEnd(animation);
                secondFront.setVisibility(View.GONE);
            }
        });
        AnimatorSet secondFadeInAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.fade_in);
        secondFadeInAnimatorSet.setTarget(secondPass);
        secondFadeInAnimatorSet.addListener(new AnimatorListenerAdapter(){
            @Override
            public void onAnimationEnd(Animator animation){
                super.onAnimationEnd(animation);
                if(matchedCards == 8){
                    Intent gameBroadcastIntent = new Intent(GAME_FINISH_BROADCAST);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(gameBroadcastIntent);
                }
            }
        });
        secondFlipOutAnimatorSet.start();
        secondFadeInAnimatorSet.start();
    }


    public void flipCard(View mainView,View visibleView,View inVisibleView) {

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
                animation_flag += 1;
            }
        });
        flipInAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                inVisibleView.setVisibility(View.GONE);
                mainView.setClickable(true);
                animation_flag -= 1;
                if(firstId != -1 && secondId != -1 && animation_flag == 0){
                    CheckMatch();
                }
            }
        });

        flipOutAnimatorSet.start();
        flipInAnimatorSet.start();
    }

    public void revealAll(){

    }

}
