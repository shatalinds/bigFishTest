package bigfish.bigfishtest;

import javax.inject.Singleton;

import bigfish.bigfishtest.activity.ConfirmActivity;
import bigfish.bigfishtest.activity.CustomActivity;
import bigfish.bigfishtest.activity.MainActivity;
import bigfish.bigfishtest.activity.RegistrationActivity;
import bigfish.bigfishtest.activity.StartUpActivity;
import bigfish.bigfishtest.adapter.ChargingStationAdapter;
import dagger.Component;

/**
 * Created by Dmitry Shatalin
 * mailto: shatalinds@gmail.com
 * BigFishTest
 * 21.12.17
 */

@Singleton
@Component(modules = {AppModule.class, NetModule.class, ConfigModule.class})
public interface AppComponent {
    void inject(CustomActivity activity);
    void inject(RegistrationActivity activity);
    void inject(ConfirmActivity activity);
    void inject(MainActivity activity);
    void inject(StartUpActivity activity);

    void inject(ChargingStationAdapter adapter);
}