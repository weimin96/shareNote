package com.aoliao.notebook.utils.helper;

import android.view.View;

import com.aoliao.notebook.R;
import com.aoliao.notebook.AppController;
import com.balysv.materialripple.MaterialRippleLayout;



public class MaterialRippleHelper {
    private MaterialRippleHelper() {
    }

    public static void ripple(View view) {
        MaterialRippleLayout.on(view)
                .rippleColor(AppController.getAppContext().getResources().getColor(R.color.colorAccent))
                .rippleAlpha(0.2f)
                .rippleHover(true)
                .create();
    }
}
