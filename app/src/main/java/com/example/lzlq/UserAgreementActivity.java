package com.example.lzlq;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

public class UserAgreementActivity extends BaseActivity {

    private static final String PREFS_NAME = "AppPrefs";
    private static final String AGREEMENT_ACCEPTED_KEY = "agreementAccepted";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_agreement);

        CheckBox cbAgreement = findViewById(R.id.cb_agreement);
        Button btnEnter = findViewById(R.id.btn_enter_app);

        // 初始时按钮不可用
        btnEnter.setEnabled(false);

        // 根据 CheckBox 的勾选状态，来决定按钮是否可用
        cbAgreement.setOnCheckedChangeListener((buttonView, isChecked) -> {
            btnEnter.setEnabled(isChecked);
        });

        // 点击按钮，标记为已同意，并跳转到下一个页面
        btnEnter.setOnClickListener(v -> {
            // 使用 SharedPreferences 记录用户已经同意
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(AGREEMENT_ACCEPTED_KEY, true);
            editor.apply();

            // 跳转到文化导读页
            Intent intent = new Intent(UserAgreementActivity.this, GuideActivity.class);
            startActivity(intent);
            finish(); // 结束当前页面，防止用户返回
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        soundManager.startCountingActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
        soundManager.stopCountingActivity();
    }
}
