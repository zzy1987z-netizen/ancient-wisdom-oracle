package com.example.lzlq;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

// 继承我们修复好的BaseActivity
public class RetryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retry);

        Button btnRetryShake = findViewById(R.id.btn_retry_shake);

        btnRetryShake.setOnClickListener(v -> {
            finish(); // 点击后，关闭当前“天机阁”，返回“摇签页”
        });
    }
}
