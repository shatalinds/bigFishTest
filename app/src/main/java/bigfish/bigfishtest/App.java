package bigfish.bigfishtest;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

/**
 * Created by Dmitry Shatalin
 * mailto: shatalinds@gmail.com
 * BigFishTest
 * 21.12.17
 */

public class App extends MultiDexApplication {
    private AppComponent mAppComponent;
    private Context mContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
        initRealm(mContext);
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this, mContext))
                .netModule(new NetModule(this))
                .configModule(new ConfigModule(this))
                .build();

        Timber.plant(new CrashReportingTree());
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    private void initRealm(Context context) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    class CrashReportingTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if (t != null)
                Toast.makeText(mContext, message + "\n" + t.getMessage(), Toast.LENGTH_SHORT).show();
            Log.i(tag, message);
        }
    }
}