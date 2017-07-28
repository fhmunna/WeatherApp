package com.example.alexander.weatherapp.di.modules;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.alexander.weatherapp.R;
import com.example.alexander.weatherapp.data.network.NetworkUtils;
import com.example.alexander.weatherapp.data.network.api.GooglePlacesApi;
import com.example.alexander.weatherapp.data.network.api.WeatherApi;

import java.net.UnknownHostException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    private final Context appContext;

    public NetworkModule(@NonNull Context appContext) {
        this.appContext = appContext;
    }

    @Provides
    @Singleton
    public Interceptor provideInterceptor() {
        return chain -> {

            Request request = chain.request();

            if (!NetworkUtils.isNetworkAvailable(appContext)) {
                throw new UnknownHostException("Unable to resolve host \"" + request.url().host() + "\"");
            }

            //run real api query
            Response response = null;
            response = chain.proceed(request);

            ResponseBody responseBody = response.body();

            if (responseBody == null) {
                throw new NullPointerException();
            }

            String responseBodyString = responseBody.string();

            //maybe caching
            if (response.code() == 200) {
                //TODO
            }

            return response.newBuilder().body(ResponseBody.create(responseBody.contentType(), responseBodyString.getBytes())).build();
        };
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(Interceptor interceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }

    @Provides
    @Singleton
    WeatherApi provideWeatherApi(Retrofit.Builder builder) {
        String[] meta = appContext.getResources().getStringArray(R.array.weather_api);

        return builder.baseUrl(meta[0])
                .build()
                .create(WeatherApi.class);
    }

    @Provides
    @Singleton
    Retrofit.Builder provideRetrofitBuilder(OkHttpClient client) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client);

    }

    @Provides
    @Singleton
    GooglePlacesApi provideGooglePlacesApi(Retrofit.Builder builder) {
        return builder
                .baseUrl(GooglePlacesApi.URL)
                .build()
                .create(GooglePlacesApi.class);
    }
}
