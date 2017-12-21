package bigfish.bigfishtest.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;
import javax.inject.Named;

import bigfish.bigfishtest.App;
import bigfish.bigfishtest.R;
import bigfish.bigfishtest.TokenService;
import bigfish.bigfishtest.model.OAuthToken;
import bigfish.bigfishtest.storage.Storage;
import br.com.sapereaude.maskedEditText.MaskedEditText;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class StartUpActivity extends CustomActivity {

    @BindView(R.id.metPhoneNumber) MaskedEditText metPhoneNumber;
    @BindView(R.id.metCodeSMS) MaskedEditText metCodeSMS;
    @BindView(R.id.btnAuth) Button btnAuth;
    @BindView(R.id.tvRegistration) TextView tvRegistration;

    @Inject @Named("tokenService") Retrofit mTokenRetrofit;
    private TokenService mTokenService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
        slideAnimation(false);

        ((App) getApplication()).getAppComponent().inject(this);
        mTokenService = mTokenRetrofit.create(TokenService.class);

        String phone = mTinyDB.getString(Storage.USER_PHONE);
        if (!TextUtils.isEmpty(phone))
            metPhoneNumber.setText(phone);

        String code = mTinyDB.getString(Storage.USER_PIN);
        if (!TextUtils.isEmpty(code))
            metCodeSMS.setText(code);
    }

    private OAuthToken mOAuthToken;

    @OnClick(R.id.btnAuth)
    void authClick(View view) {
        final String phone = "7" + metPhoneNumber.getRawText();
        if (phone.length() != 11) {
            Toast.makeText(mCtx, mResources.getString(R.string.enter_phone_number), Toast.LENGTH_SHORT).show();
            metPhoneNumber.requestFocus();
            return;
        }

        final String code = metCodeSMS.getRawText();
        if (code.length() != 4) {
            Toast.makeText(mCtx, getString(R.string.enter_code), Toast.LENGTH_SHORT).show();
            return;
        }

        mTinyDB.putString(Storage.USER_PHONE, phone);
        mTinyDB.putString(Storage.USER_PIN, code);
        mTinyDB.putBoolean(Storage.USER_PHONE_CONFIRM, false);

        mTokenService.getToken(Storage.OAUTH_GRANT_TYPE,
                Storage.OAUTH_CLIENT_ID,
                Storage.OAUTH_CLIENT_SECRET,
                phone, code)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .filter(oAuthToken -> oAuthToken != null)
                .subscribe(oAuthToken -> {
                        mOAuthToken = oAuthToken;
                        mTinyDB.putString(Storage.OAUTH_KEY, oAuthToken.getAccessToken());
                        mTinyDB.putBoolean(Storage.USER_PHONE_CONFIRM, true);
                        startActivity(MainActivity.class);
                }, throwable -> {
                    Toast.makeText(mCtx, getString(R.string.fail_autorization), Toast.LENGTH_SHORT).show();
                    metPhoneNumber.setText("");
                    metCodeSMS.setText("");
                    metPhoneNumber.requestFocus();
                });
    }

    @OnClick(R.id.tvRegistration)
    void registrationClick(View view) {
        startActivity(RegistrationActivity.class);
    }
}