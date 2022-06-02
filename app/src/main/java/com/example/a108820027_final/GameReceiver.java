package com.example.a108820027_final;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;

public class GameReceiver extends BroadcastReceiver {

    private static final String GAME_FINISH_BROADCAST = BuildConfig.APPLICATION_ID + ".GAME_FINISH_CALL";
    public FragmentTransaction fragmentTransaction;
    @Override
    public void onReceive(Context context, Intent intent){
        String intentAction = intent.getAction();

        if(intentAction == GAME_FINISH_BROADCAST){
            Toast.makeText(context, "FINISH", Toast.LENGTH_SHORT).show();
            fragmentTransaction.replace(R.id.main_container, ResultFrag.class, null);
            fragmentTransaction.commit();
        }
    }

    public void setFragmentTransaction(FragmentTransaction ft){
        this.fragmentTransaction = ft;
    }
}
