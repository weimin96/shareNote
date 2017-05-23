package com.aoliao.notebook.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aoliao.notebook.R;


/**
 * Created by Administrator on 2017/4/15 0015.
 */

public class ShoppingFragment extends android.support.v4.app.Fragment {
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.shopping_fragment,container,false);
        return view;
    }
}
