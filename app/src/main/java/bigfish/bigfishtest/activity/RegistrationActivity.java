package bigfish.bigfishtest.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class RegistrationActivity extends CustomActivity {
    public final static String TAG = RegistrationActivity.class.getSimpleName();

    @BindView(R.id.metPhoneNumber) MaskedEditText metPhoneNumber;
    @BindView(R.id.btnGetCode) Button btnGetCode;

    @Inject @Named("apiService") Retrofit mApiRetrofit;
    private ApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((App)getApplication()).getAppComponent().inject(this);
        mApiService = mApiRetrofit.create(ApiService.class);

        setContentView(R.layout.activity_registration);
        slideAnimation(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestFocusAndShowKeyboard(metPhoneNumber, true);
    }

    @Override
    protected void onStop() {
        hideKeyboard();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        hideKeyboard();
        //super.onBackPressed();
    }

    @OnClick(R.id.btnGetCode)
    void btnGetCodeClick(View view) {
        final String phone = "7" + metPhoneNumber.getRawText();
        if (phone.length() == 11) {
            mTinyDB.putString(Storage.USER_PHONE, phone);
            mTinyDB.putBoolean(Storage.USER_PHONE_CONFIRM, false);

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("phone", phone);
            mApiService.registration(requestBody)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .filter(registrationModel -> registrationModel != null)
                    .subscribe(responseBody -> {
                        if (responseBody.getStatusCode() == 200) {
                            startActivity(ConfirmActivity.class);
                        } else Toast.makeText(mCtx, responseBody.getError().toString(), Toast.LENGTH_SHORT).show();
                        }, throwable -> Timber.e(throwable));
        } else {
            Toast.makeText(mCtx, mResources.getString(R.string.enter_phone_number), Toast.LENGTH_SHORT).show();
            metPhoneNumber.requestFocus();
        }
    }
}