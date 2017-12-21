package bigfish.bigfishtest.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import bigfish.bigfishtest.R;
import bigfish.bigfishtest.storage.Storage;
import butterknife.BindView;

public class SplashActivity extends CustomActivity {
    @BindView(R.id.tvCounter) TextView tvCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();

        new CountDownTimer(Storage.SPLASH_DELAY, Storage.SPLASH_DELAY_INTERVAL) {
            @Override
            public void onTick(long l) {
                String counter = String.valueOf(l / 1000);
                tvCounter.setText(counter);
            }

            @Override
            public void onFinish() {
                tvCounter.setText("0");
                if (!mTinyDB.getBoolean(Storage.USER_PHONE_CONFIRM))
                    startActivity(RegistrationActivity.class);
                else startActivity(MainActivity.class);
            }
        }.start();
    }
}