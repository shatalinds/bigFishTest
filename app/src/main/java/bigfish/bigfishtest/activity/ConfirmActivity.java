package bigfish.bigfishtest.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import bigfish.bigfishtest.ApiService;
import bigfish.bigfishtest.App;
import bigfish.bigfishtest.R;
import bigfish.bigfishtest.storage.Storage;
import br.com.sapereaude.maskedEditText.MaskedEditText;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import timber.log.Timber;

public class ConfirmActivity extends CustomActivity {

    @BindView(R.id.metCodeSMS) MaskedEditText metCodeSMS;
    @BindView(R.id.tvResendSMS) TextView tvResendSMS;

    @Inject @Named("apiService") Retrofit mApiRetrofit;
    private ApiService mApiService;


    private static int idx = 0;
    private volatile Boolean bStopRepeat = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((App) getApplication()).getAppComponent().inject(this);
        mApiService = mApiRetrofit.create(ApiService.class);

        setContentView(R.layout.activity_confirm);
        slideAnimation(false);

        metCodeSMS.setOnKeyListener((View v, int keyCode, KeyEvent event) -> {
            if (event.getAction() != KeyEvent.ACTION_DOWN)
                return true;

            char pressedKey = (char) event.getUnicodeChar();
            final String code = metCodeSMS.getRawText() + pressedKey;
            if (code.length() == 4) {

                Map<String, String> requestBody = new HashMap<>();
                requestBody.put("phone", mTinyDB.getString(Storage.USER_PHONE));
                requestBody.put("code", code);

                mApiService.registration_confirm(requestBody)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .filter(registrationModel -> registrationModel != null)
                        .subscribe(responseBody -> {
                            if (responseBody.getStatusCode() == 200) {
                                mTinyDB.putBoolean(Storage.USER_PHONE_CONFIRM, true);
                                mTinyDB.putString(Storage.USER_PIN, code);
                                startActivity(ConfirmSmsSuccessActivity.class);
                            } else Toast.makeText(mCtx, responseBody.getError().toString(), Toast.LENGTH_SHORT).show();
                        }, throwable -> {
                            metCodeSMS.setText("");
                            Toast.makeText(mCtx, getString(R.string.code_verification_fail), Toast.LENGTH_SHORT).show();
                            if (++idx >= 2)
                                tvResendSMS.setVisibility(View.VISIBLE);
                        });
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestFocusAndShowKeyboard(metCodeSMS, true);
    }

    @OnClick(R.id.tvResendSMS)
    void resendSMSClick(View view) {
        metCodeSMS.setText("");
        finish();
        slideAnimation(true);
    }
}