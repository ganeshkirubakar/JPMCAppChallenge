package com.jpmc.network;

import com.jpmc.BuildConfig;
import com.jpmc.constants.ApiConstants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RestAdapter {

    private String TAG = RestAdapter.class.getName();
    private static Retrofit retrofit;

    /**
     * Create an instance of Retrofit object
     * */
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiConstants.APP_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getOkHttpClient())
                    .build();
        }
        return retrofit;
    }

    /*
     * getOkHttpClient - To Build OkHttpClient with 30 seconds tome out option, to avoid SocketTimeOutException.
     *
     * @return OkHttpClient
     * */
    private static OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .retryOnConnectionFailure(true)
                .cache(null)
                .connectTimeout(ApiConstants.SOCKET_TIME_OUT, TimeUnit.SECONDS);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if(BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(interceptor);
            IdlingResources.registerOkHttp(clientBuilder.build());
        }
        return clientBuilder.build();
    }

}
