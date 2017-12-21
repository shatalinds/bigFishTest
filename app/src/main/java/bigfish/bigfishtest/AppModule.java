package bigfish.bigfishtest;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Dmitry Shatalin
 * mailto: shatalinds@gmail.com
 * BigFishTest
 * 21.12.17
 */

@Module
public class AppModule {
    private Application mApplication;
    private Context mContext;

    public AppModule(Application application, Context context) {
        this.mApplication = application;
        this.mContext = context;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    Context providesContext() {
        return mContext;
    }

    @Provides
    @Singleton
    Resources providesResources() {
        return mContext.getResources();
    }

    @Provides
    @Singleton
    LayoutInflater providesLayoutInflater() {
        return LayoutInflater.from(mContext);
    }

    @Provides
    LinearLayoutManager providesLinearLayoutManager() {
        return new LinearLayoutManager(mContext);
    }
}