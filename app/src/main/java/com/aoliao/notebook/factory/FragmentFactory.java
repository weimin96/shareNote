package com.aoliao.notebook.factory;

import com.aoliao.notebook.fragment.BaseMainFragment;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;


public class FragmentFactory {
    /**
     * 主页Fragment数量
     */
    private static final int FRAGMENT_NUM = 4;

    /**
     * 统一管理MainActivity中所有Fragment
     */
    public static final Map<String, WeakReference<BaseMainFragment>> MAIN_WIND_FRAG = new HashMap<>(FRAGMENT_NUM);

    /**
     * 更新头像
     */
    public static void updatedUser() {
        for (String key : MAIN_WIND_FRAG.keySet()) {
            WeakReference<BaseMainFragment> wr = MAIN_WIND_FRAG.get(key);
            if (wr == null || wr.get() == null) continue;
            wr.get().setUpdatedUser(true);
        }
    }
}
