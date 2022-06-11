package com.example.a108820027_final;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
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
    private static final String GAME_FINISH_BROADCAST = BuildConfig.APPLICATION_ID + ".GAME_FINISH_CALL";

    public MatchingGame(Context ct){
        this.context = ct;
    }

    public void initializeGame(){


        mCardData = new ArrayList<>();

        mCardData.clear();
        TypedArray imageResource = context.getResources().obtainTypedArray(R.array.cards_images);

        for(int id = 0; id < imageResource.length(); id++){
            mCardData.add(new Card(id, imageResource.getResourceId(id, 0)));
            mCardData.add(new Card(id, imageResource.getResourceId(id, 0)));
        }

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
            Intent gameBroadcastIntent = new Intent(GAME_FINISH_BROADCAST);
            LocalBroadcastManager.getInstance(context).sendBroadcast(gameBroadcastIntent);
        }
    }

    public String getAccuracy(){
        int result = matchCount * 100 / matchTimes;
        return Integer.toString(result) + " %";
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
}
