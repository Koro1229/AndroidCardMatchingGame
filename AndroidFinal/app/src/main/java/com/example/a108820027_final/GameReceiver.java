package com.example.a108820027_final;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class GameReceiver extends BroadcastReceiver {

    private static final String GAME_FINISH_BROADCAST = BuildConfig.APPLICATION_ID + "AndroidFinal.a108820027.GAME_FINISH_CALL";
    private FragmentManager fragmentManager;
    private Bundle resultData;
    @Override
    public void onReceive(Context context, Intent intent){
        String intentAction = intent.getAction();
        ResultFrag resultFrag = new ResultFrag();
        resultFrag.setArguments(resultData);
        if(intentAction.equals(GAME_FINISH_BROADCAST)){
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_container, ResultFrag.class, resultData);
            fragmentTransaction.commit();
        }
    }

    public void setFragmentManager(FragmentManager fm){
        this.fragmentManager = fm;
    }

    public void setNewBundle(Bundle bundle){
        resultData = bundle;
    }
}
