package com.example.a108820027_final;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {


    private FragmentManager mFManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFManager.beginTransaction();
        transaction.setReorderingAllowed(true);

        // Replace whatever is in the fragment_container view with this fragment
        transaction.replace(R.id.main_container, MainFrag.class, null);

        // Commit the transaction
        transaction.commit();
    }


}