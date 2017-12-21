package bigfish.bigfishtest.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import javax.inject.Inject;
import javax.inject.Named;

import bigfish.bigfishtest.App;
import bigfish.bigfishtest.AuthService;
import bigfish.bigfishtest.R;
import bigfish.bigfishtest.TokenService;
import bigfish.bigfishtest.adapter.ChargingStationAdapter;
import bigfish.bigfishtest.model.ChargeStationsModel;
import bigfish.bigfishtest.model.OAuthToken;
import bigfish.bigfishtest.storage.Storage;
import bigfish.bigfishtest.storage.TinyDB;
import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import timber.log.Timber;

public class MainActivity extends CustomActivity implements Handler.Callback {

    @Inject TinyDB mTinyDB;
    @Inject @Named("tokenService") Retrofit mTokenRetrofit;
    @Inject @Named("authService") Retrofit mAuthRetrofit;

    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    private TokenService mTokenService;
    private AuthService mAuthService;

    private OAuthToken mOAuthToken;
    private Handler mHandler;

    private static int GET_CHARGE_SATATIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((App) getApplication()).getAppComponent().inject(this);
        mTokenService = mTokenRetrofit.create(TokenService.class);
        mAuthService = mAuthRetrofit.create(AuthService.class);

        mHandler = new Handler(this);

        setContentView(R.layout.activity_main);
        slideAnimation(false);
        setupRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTokenService.getToken(Storage.OAUTH_GRANT_TYPE,
                        Storage.OAUTH_CLIENT_ID,
                        Storage.OAUTH_CLIENT_SECRET,
                        mTinyDB.getString(Storage.USER_PHONE),
                        mTinyDB.getString(Storage.USER_PIN))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .filter(oAuthToken -> oAuthToken != null)
                .subscribe(oAuthToken -> {
                    mOAuthToken = oAuthToken;
                    mTinyDB.putString(Storage.OAUTH_KEY, oAuthToken.getAccessToken());
                    mHandler.obtainMessage(GET_CHARGE_SATATIONS).sendToTarget();
                }, throwable -> Timber.e(throwable));
    }

    private ChargeStationsModel mChargeStationsModel;

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == GET_CHARGE_SATATIONS) {
            mAuthService.chargerstations()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(chargeStationsModel -> {
                        mChargeStationsModel = chargeStationsModel;
                        recyclerView.setAdapter(new ChargingStationAdapter(this, mChargeStationsModel));
                    }, throwable -> Timber.e(throwable));

        }
        return false;
    }

    private void setupRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager(new LinearLayoutManager(mCtx));
    }
}