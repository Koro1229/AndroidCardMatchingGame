package com.example.a108820027_final;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.Collections;

public class MatchingGame extends AppCompatActivity{


    private Context context;
    private ArrayList<Card> mCardData;
    private int firstIndex;
    private int secondIndex;
    private View firstView; // View here only for save id
    private View secondView; // View here only for save id
    private int matchTimes = 0;
    private int matchCount = 0;
    private long startTime;
    private long endTime;
    private static final int MAX_CARD = 8;
    private static final String GAME_FINISH_BROADCAST = BuildConfig.APPLICATION_ID + "AndroidFinal.a108820027.GAME_FINISH_CALL";
    private static final String IS_REVEAL_ALL = "IsReveal";
    private static final String TIME_KEY = "Time";
    private static final String ACCURACY_KEY = "Accuracy";
    private static final String THEME_KEY = "GameTheme";
    private int gameTheme;
    private static final int POKER_THEME = 1;
    private static final int WORD_THEME = 2;
    private final GameReceiver mReceiver;
    private boolean isRevealAll = false;


    public MatchingGame(Context ct, GameReceiver rv){
        this.context = ct;
        this.mReceiver = rv;
    }

    public void initializeGame(int theme){

        mCardData = new ArrayList<>();
        ArrayList<Integer> usedIndex = new ArrayList<>();
        TypedArray imageResource;
        gameTheme = theme;
        if(gameTheme == POKER_THEME){
            imageResource = context.getResources().obtainTypedArray(R.array.poker_images);
        }else{
            imageResource = context.getResources().obtainTypedArray(R.array.word_images);
        }

        int arrayMax = imageResource.length();

        for(int id = 0; id < MAX_CARD;){
            int randomIndex = (int)(Math.random() * arrayMax);
            if(!(usedIndex.contains(randomIndex))){
                usedIndex.add(randomIndex);
                mCardData.add(new Card(id, imageResource.getResourceId(randomIndex, 0)));
                mCardData.add(new Card(id, imageResource.getResourceId(randomIndex, 0)));
                id++;
            }
        }

        imageResource.recycle();
        Collections.shuffle(mCardData);

        startTime = System.currentTimeMillis();
        matchCount = 0;
        matchTimes = 0;
        firstIndex = -1;
        secondIndex = -1;
        firstView = null;
        secondView = null;
    }

    public ArrayList<Card> getCardData(){
        return mCardData;
    }

    public void chooseCard(int i, View view) {
        mCardData.get(i).isFaceUp = !(mCardData.get(i).isFaceUp);
        if(firstIndex == i){
            firstIndex = -1;
        }else if(firstIndex == -1){
            firstIndex = i;
            firstView = view;
        }else if(secondIndex == -1){
            secondIndex = i;
            secondView = view;
        }
    }

    public boolean isChooseFinished(){
        if(firstIndex != -1 && secondIndex != -1){
            return true;
        }else{
            return false;
        }
    }

    public boolean checkMatch(){
        matchTimes += 1;
        if(mCardData.get(firstIndex).id == mCardData.get(secondIndex).id){
            mCardData.get(firstIndex).isMatched = true;
            mCardData.get(secondIndex).isMatched = true;
            matchCount += 1;
            cleanIndex();
            return true;
        }else{
            cleanIndex();
            return false;
        }
    }

    public void checkGameFinish(){
        if(isFinished()){
            endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            Bundle bundle = new Bundle();
            bundle.putBoolean(IS_REVEAL_ALL, isRevealAll);
            bundle.putLong(TIME_KEY, totalTime);
            bundle.putFloat(ACCURACY_KEY, getAccuracy());
            bundle.putInt(THEME_KEY, gameTheme);
            mReceiver.setNewBundle(bundle);
            Intent gameBroadcastIntent = new Intent(GAME_FINISH_BROADCAST);
            LocalBroadcastManager.getInstance(context).sendBroadcast(gameBroadcastIntent);
        }
    }

    public float getAccuracy(){
        if(!isRevealAll){
            return (float)matchCount * 100 / (float)matchTimes;
        }else{
            return -1;
        }
    }

    private boolean isFinished(){
        for(int i = 0; i < mCardData.size(); i++){
            if(!(mCardData.get(i).isMatched)){
                return false;
            }
        }
        return true;
    }

    public View getFirstView(){
        return firstView;
    }

    public View getSecondView(){
        return secondView;
    }

    private void cleanIndex(){
        firstIndex = -1;
        secondIndex = -1;
    }

    public void revealAll(){
        isRevealAll = true;
    }

    public void foldAll(){
        cleanIndex();
    }
}
