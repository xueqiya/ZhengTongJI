package com.permissionutil;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class PermissionUtil {

    private static final String TAG = PermissionUtil.class.getSimpleName();

    private PermissionFragment fragment;

    public PermissionUtil(@NonNull FragmentActivity activity) {
        fragment = getRxPermissionsFragment(activity);
    }

    private PermissionFragment getRxPermissionsFragment(FragmentActivity activity) {
        if (activity==null){
            return null;
        }
        PermissionFragment fragment = (PermissionFragment) activity.getSupportFragmentManager().findFragmentByTag(TAG);
        boolean isNewInstance = fragment == null;
        if (isNewInstance && !activity.isDestroyed()) {
            fragment = new PermissionFragment();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(fragment, TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return fragment;
    }

    /**
     * 外部使用 申请权限
     * @param permissions 申请授权的权限
     * @param listener 授权回调的监听
     */
    public void requestPermissions(String[] permissions, PermissionListener listener) {
        if (fragment != null) {
            fragment.setListener(listener);
            fragment.requestPermissions(permissions);
        }


    }

}
