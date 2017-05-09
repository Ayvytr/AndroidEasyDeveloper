package com.ayvytr.easyandroidtest.network;

import com.ayvytr.logger.L;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by Do on 2017/5/9.
 */

public class Network
{

    private static Cache cache = new Cache(new File("/storage/emulated/0/cache"), 10 * 1024 * 1024);
    private static OkHttpClient okHttpClient = new OkHttpClient();

    private static final String API_SERVER = "http://c.3g.163.com/nc/article/";
    private static Retrofit mRetrofit;
    private static final NetService service;

    static
    {
        okHttpClient = new OkHttpClient.Builder()
                //.addInterceptor(new MyIntercepter())
                //.addNetworkInterceptor(new CookiesInterceptor(MyApplication.getInstance().getApplicationContext()))
                //设置请求读写的超时时间
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .cache(cache)
                .build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(API_SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        service = mRetrofit.create(NetService.class);

    }

    interface NetService
    {
        @GET("headline/T1348647853363/0-100.html")
        Observable<News> getNews();
    }

    public static void getNews(SingleObserver<List<News.ContentBean>> observer)
    {
        service.getNews()
               .subscribeOn(Schedulers.newThread())
               .flatMap(new Function<News, Observable<News.ContentBean>>()
               {
                   @Override
                   public Observable<News.ContentBean> apply(News news) throws Exception
                   {
                       L.e();
                       return Observable.fromIterable(news.getT1348647853363());
                   }
               })
               .toList()
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(observer);
    }

    public static <T> void setSubscribe(Observable<T> observable, Observer<T> observer)
    {
        observable.subscribeOn(Schedulers.newThread())//子线程访问网络
                  .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                  .subscribe(observer);
    }

}
