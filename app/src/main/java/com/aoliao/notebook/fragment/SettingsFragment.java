package com.aoliao.notebook.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.aoliao.notebook.R;


/**
 * Created by Administrator on 2017/4/27 0027.
 */

public class SettingsFragment extends PreferenceFragment{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.addPreferencesFromResource(R.xml.preferences);

    }

}
