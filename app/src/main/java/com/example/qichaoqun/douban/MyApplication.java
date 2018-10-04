package com.example.qichaoqun.douban;

import android.app.Application;

import com.aitangba.swipeback.ActivityLifecycleHelper;

import org.litepal.LitePalApplication;

/**
 * @author qichaoqun
 * @date 2018/10/4
 */
public class MyApplication extends LitePalApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        //滑动返回注册
        registerActivityLifecycleCallbacks(ActivityLifecycleHelper.build());
    }
}
