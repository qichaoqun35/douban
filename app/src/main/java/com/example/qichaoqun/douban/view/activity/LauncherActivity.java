package com.example.qichaoqun.douban.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author qichaoqun
 * @date 2018/9/27
 */
public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //start:事件序列的起始点，这里从0开始
        //count: 总的事件的数量，这里只需要发送一次，因此总的事件的数量是 1
        //initialDelay: 距离第一次返送间隔的时间
        //period: 间隔的时间（出第一次发送）
        //time_unit:事件单位，这里使用的是秒
        Observable.intervalRange(0,1,2,3,TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Intent intent = new Intent(LauncherActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }
}
