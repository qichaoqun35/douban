package com.example.qichaoqun.douban.net_work;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author qichaoqun
 * @date 2018/9/28
 */
public class NetWork {

    public static DouBanApi douBanApi = null;

    /**
     * 对于豆瓣的api进行的网络请求
     */
    public static DouBanApi getDoubanApi(){
        if(douBanApi == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.douban.com/v2/movie/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            douBanApi = retrofit.create(DouBanApi.class);
        }
        return douBanApi;
    }
}
