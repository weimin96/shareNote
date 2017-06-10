
package com.aoliao.notebook.fragment;

import com.aoliao.notebook.xmvp.XBasePresenter;

import java.util.HashMap;
import java.util.Map;




public abstract class BaseMainFragment<T extends XBasePresenter> extends BaseFragment<T> {

    //是否更新过用户信息
    private boolean updatedUser = false;

//    /** Fragment当前状态是否可见 */
//    protected boolean isVisible;

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//
//        if(getUserVisibleHint()) {
//            isVisible = true;
//            onVisible();
//        } else {
//            isVisible = false;
//            onInvisible();
//        }
//    }

    /**
     * 可见
     */
//    protected void onVisible() {
//        lazyLoad();
//    }

    /**
     * 不可见
     */
//    protected void onInvisible() {
//
//
//    }

//    /**
//     * 延迟加载
//     * 子类必须重写此方法
//     */
//    protected abstract void lazyLoad();

    //保存一些数据
    private static Map<String, Object> dataSave = new HashMap<>(2);


    /**
     * 获取数据
     * @param key
     * @param <D>
     * @return
     */
    public static <D> D getData(String key) {
        Object obj = dataSave.get(key);
        if (obj == null) {
            return null;
        } else {
            return (D) obj;
        }
    }

    public static void clearData() {
        dataSave.clear();
    }

    /**
     * 保存数据
     * @param key
     * @param obj
     */
    public static void saveData(String key, Object obj) {
        dataSave.put(key, obj);
    }

    @Override
    protected void onInit() {
        super.onInit();
    }



    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (updatedUser && !hidden) {
            updateUserInfo();
            updatedUser = false;
        }
    }

    /**
     * 更新显示新的头像
     */
    protected void updateUserInfo() {

    }


    public void setUpdatedUser(boolean updatedUser) {
        this.updatedUser = updatedUser;
    }

}
