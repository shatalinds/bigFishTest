package bigfish.bigfishtest;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import bigfish.bigfishtest.storage.TinyDB;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Dmitry Shatalin
 * mailto: shatalinds@gmail.com
 * BigFishTest
 * 21.12.17
 */

@Module
public class ConfigModule {
    private Application mApplication;

    public ConfigModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    TinyDB providesTinyDB(Context context) {
        return new TinyDB(context);
    }
}