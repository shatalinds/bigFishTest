package bigfish.bigfishtest.activity;

import android.os.Bundle;
import android.view.View;

import bigfish.bigfishtest.R;
import butterknife.OnClick;

public class ConfirmSmsSuccessActivity extends CustomActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_sms_success);
        slideAnimation(false);
    }

    @OnClick(R.id.btnNext)
    void nextClick(View view) {
        startActivity(MainActivity.class);
    }
}
