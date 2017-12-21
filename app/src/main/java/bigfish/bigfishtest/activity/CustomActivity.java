package bigfish.bigfishtest.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.CallSuper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Map;

import javax.inject.Inject;

import bigfish.bigfishtest.App;
import bigfish.bigfishtest.R;
import bigfish.bigfishtest.storage.TinyDB;
import butterknife.ButterKnife;

/**
 * Created by Dmitry Shatalin
 * mailto: shatalinds@gmail.com
 * BigFishTest
 * 21.12.17
 */

public class CustomActivity extends AppCompatActivity {
    @Inject Context mCtx;
    @Inject TinyDB mTinyDB;
    @Inject Resources mResources;

    @CallSuper
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ((App)getApplication()).getAppComponent().inject(this);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        ButterKnife.setDebug(true);
        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        hideKeyboard();
    }

    protected void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected void hideKeyboard() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    protected void requestFocusAndShowKeyboard(EditText editText, boolean showKeyboard) {
        editText.postDelayed(() -> {
            editText.setSelection(editText.getText().toString().length());
            editText.requestFocus();
            if (showKeyboard)
                showKeyboard();
        }, 500);
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideKeyboard();
    }

    public int getSceenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public int getSceenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public void hideStatusBar() {
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public void showStatusBar() {
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public void slideAnimation(boolean back) {
        if (back)
            overridePendingTransition(R.anim.slide_left_to_right, R.anim.slide_right_to_left);
        else overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void startActivity(Class<?> cls) {
        startActivity(cls, true, true, null);
    }

    public <T> void startActivity(Class<?> cls, Map<String, T> params) {
        startActivity(cls, true, true, params);
    }

    public <T> void startActivity(Class<?> cls, boolean newTask, boolean clearStack, Map<String, T> params) {
        Intent intent = new Intent(mCtx, cls);
        if (params != null) {
            for (Map.Entry<String, T> entry : params.entrySet()) {
                String key = entry.getKey();
                T value = entry.getValue();

                if (value instanceof Integer) {
                    intent.putExtra(key, (Integer) value);
                } else if (value instanceof String) {
                    intent.putExtra(key, (String) value);
                } else if (value instanceof Boolean) {
                    intent.putExtra(key, (Boolean) value);
                }
            }
        }

        if (newTask)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (clearStack)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        mCtx.startActivity(intent);
    }
}