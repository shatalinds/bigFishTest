package bigfish.bigfishtest;

import android.app.Application;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Named;
import javax.inject.Singleton;

import bigfish.bigfishtest.storage.Storage;
import bigfish.bigfishtest.storage.TinyDB;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dmitry Shatalin
 * mailto: shatalinds@gmail.com
 * BigFishTest
 * 21.12.17
 */

@Module
public class NetModule {
    private Application mApplication;

    public NetModule(Application application) {
        this.mApplication = application;
    }

    @Provides
    @Singleton
    Cache provideOkHttpCache() {
        return new Cache(mApplication.getCacheDir(), 10 * 1024 * 1024);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        gsonBuilder.setLenient();
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    Retrofit.Builder provideApiServiceRetrofitBuilder(Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(Storage.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }

    @Provides
    @Singleton
    @Named("apiService")
    Retrofit provideApiService(Retrofit.Builder builder, Cache cache) {
        return builder.build();
    }

    @Provides
    @Named("tokenService")
    Retrofit provideTokenService(Retrofit.Builder builder, Cache cache) {
        return builder.client(
                new OkHttpClient.Builder().cache(cache)
                        .addInterceptor(chain -> chain.proceed(
                                requestTokenBuild(chain.request())
                                        .build())
                        ).build()).build();
    }

    @Provides
    @Named("authService")
    Retrofit provideAuthService(Retrofit.Builder builder, Cache cache, TinyDB tinyDB) {
        return builder.client(
                new OkHttpClient.Builder().cache(cache)
                        .addInterceptor(chain -> chain.proceed(
                                requestAuthBuild(chain.request(), tinyDB)
                                        .build())
                        ).build()).build();
    }

    private Request.Builder requestTokenBuild(Request request) {
        Request.Builder builder = request.newBuilder()
                .method(request.method(), request.body());
        return builder;
    }

    private Request.Builder requestAuthBuild(Request request, TinyDB tinyDB) {
        Request.Builder builder = request.newBuilder()
                .header("Accept", "application/json")
                .header("Authorization", String.format("Bearer %s", tinyDB.getString(Storage.OAUTH_KEY)))
                .method(request.method(), request.body());
        return builder;
    }
}