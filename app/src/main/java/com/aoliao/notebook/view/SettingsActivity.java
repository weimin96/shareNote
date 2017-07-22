package com.aoliao.notebook.view;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aoliao.notebook.fragment.SettingsFragment;


public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getFragmentManager().beginTransaction().replace(android.R.id.content,new SettingsFragment()).commit();

    }

}
