package com.example.lzlq;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GuideActivity extends BaseActivity {

    private static final String CURRENT_STATE_KEY = "CURRENT_STATE_KEY";
    private boolean hasInitialized = false;

    private ImageView ivImage;
    private View llTextContainer;
    private TextView tvTitle, tvContent;
    private Button btnPrev, btnNext;

    private String[] chapterTitles;
    private String[] chapterContents;
    private int currentState = 0;
    private final int totalStates = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 关键修复：从“记忆胶囊”中唤醒进度
        if (savedInstanceState != null) {
            currentState = savedInstanceState.getInt(CURRENT_STATE_KEY, 0);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !hasInitialized) {
            hasInitialized = true;
            initializeAndSetupContent();
        }
    }

    // 关键修复：在即将被销毁时，将进度存入“记忆胶囊”
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_STATE_KEY, currentState);
    }

    private void initializeAndSetupContent() {
        setupGlobalUI();
        initializeViews();
        prepareChapters();
        displayState(currentState);
    }

    private void initializeViews() {
        ivImage = findViewById(R.id.iv_guide_image);
        llTextContainer = findViewById(R.id.ll_text_container);
        tvTitle = findViewById(R.id.tv_guide_title);
        tvContent = findViewById(R.id.tv_guide_content);
        btnPrev = findViewById(R.id.btn_guide_prev);
        btnNext = findViewById(R.id.btn_guide_next);
        findViewById(R.id.btn_guide_skip).setOnClickListener(v -> goToNextActivity());
    }

    private void prepareChapters() {
        chapterTitles = new String[]{
            getString(R.string.guide_title_1), getString(R.string.guide_title_2), getString(R.string.guide_title_3),
            getString(R.string.guide_title_4), getString(R.string.guide_title_5), getString(R.string.guide_title_6)
        };
        chapterContents = new String[]{
            getString(R.string.guide_content_1), getString(R.string.guide_content_2), getString(R.string.guide_content_3),
            getString(R.string.guide_content_4), getString(R.string.guide_content_5), getString(R.string.guide_content_6)
        };
    }

    private void displayState(int state) {
        if (state == 1) {
            llTextContainer.setVisibility(View.GONE);
            ivImage.setVisibility(View.VISIBLE);
            ivImage.setImageResource(R.drawable.lvzu_portrait);
        } else {
            ivImage.setVisibility(View.GONE);
            llTextContainer.setVisibility(View.VISIBLE);
            int chapterIndex = state > 1 ? state - 1 : state;
            tvTitle.setText(chapterTitles[chapterIndex]);
            tvContent.setText(chapterContents[chapterIndex]);
        }

        btnPrev.setVisibility(state == 0 ? View.INVISIBLE : View.VISIBLE);
        btnNext.setText(state == totalStates - 1 ? getString(R.string.start_requesting_sign) : getString(R.string.next_chapter));

        btnPrev.setOnClickListener(v -> {
            triggerButtonFeedback();
            if (currentState > 0) {
                currentState--;
                displayState(currentState);
            }
        });
        btnNext.setOnClickListener(v -> {
            triggerButtonFeedback();
            if (currentState < totalStates - 1) {
                currentState++;
                displayState(currentState);
            } else {
                goToNextActivity();
            }
        });
    }

    private void goToNextActivity() {
        triggerButtonFeedback();
        Intent intent = new Intent(this, PreIncenseActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
        startActivity(intent, options.toBundle());
        finish();
    }
}
