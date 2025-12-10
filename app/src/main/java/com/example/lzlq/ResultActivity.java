package com.example.lzlq;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends BaseActivity {

    private FavoritesDbHelper dbHelper;
    private int currentSignNumber;
    private SignManager.SignData currentSignData;
    private boolean isFromFavorites;

    private boolean hasInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !hasInitialized) {
            hasInitialized = true;
            initializeAndSetupContent();
        }
    }

    private void initializeAndSetupContent() {
        dbHelper = new FavoritesDbHelper(this);
        Intent intent = getIntent();
        currentSignNumber = intent.getIntExtra("SIGN_NUMBER", 1);
        isFromFavorites = intent.getBooleanExtra("FROM_FAVORITES", false);
        currentSignData = SignManager.getInstance().getSign(currentSignNumber);

        if (currentSignData == null) {
            Toast.makeText(this, "签文加载失败", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (isFromFavorites) {
            showFullContentImmediately();
        } else {
            startInitialAnimation();
        }
    }

    private void startInitialAnimation() {
        TypewriterTextView tvPoem = findViewById(R.id.tv_poem);
        findViewById(R.id.btn_interpret).setOnClickListener(v -> {
            triggerButtonFeedback();
            findViewById(R.id.btn_interpret).setVisibility(View.GONE);
            startInterpretationAnimation();
        });
        tvPoem.setCharacterDelay(120);
        tvPoem.animateText(currentSignData.getPoem());
    }

    private void startInterpretationAnimation() {
        findViewById(R.id.ll_result_buttons).setVisibility(View.VISIBLE);
        findViewById(R.id.sv_full_text_container).setVisibility(View.VISIBLE);
        
        TypewriterTextView tvFullInterpretation = findViewById(R.id.tv_full_interpretation);
        CharSequence fullText = getFullInterpretationText();
        tvFullInterpretation.setCharacterDelay(50);
        tvFullInterpretation.animateText(fullText);
        
        setupResultButtons();
    }
    
    private void showFullContentImmediately() {
        findViewById(R.id.btn_interpret).setVisibility(View.GONE);
        findViewById(R.id.ll_result_buttons).setVisibility(View.VISIBLE);
        findViewById(R.id.sv_full_text_container).setVisibility(View.VISIBLE);

        ((TextView) findViewById(R.id.tv_poem)).setText(currentSignData.getPoem());
        ((TextView) findViewById(R.id.tv_full_interpretation)).setText(getFullInterpretationText());

        setupResultButtons();
    }

    private CharSequence getFullInterpretationText() {
        String baseText = "【解签】:\n" + currentSignData.getInterpretation() +
                          "\n\n【指引】:\n" + currentSignData.getAdvice();
        String warningText = "\n\n\n" + getString(R.string.promote_culture_no_superstition);
        SpannableString fullSpannableText = new SpannableString(baseText + warningText);
        fullSpannableText.setSpan(new ForegroundColorSpan(Color.BLACK), baseText.length(), fullSpannableText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return fullSpannableText;
    }

    private void setupResultButtons() {
        Button btnFavorite = findViewById(R.id.btn_result_favorite);
        Button btnShare = findViewById(R.id.btn_result_share);
        Button btnBack = findViewById(R.id.btn_result_back);

        if (isFromFavorites) {
            btnBack.setOnClickListener(v -> {
                triggerButtonFeedback();
                finish();
            });
        } else {
            btnBack.setOnClickListener(v -> {
                triggerButtonFeedback();
                Intent intent = new Intent(this, PreIncenseActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
                startActivity(intent, options.toBundle());
                finish();
            });
        }

        updateFavoriteButtonState(btnFavorite);
        btnFavorite.setOnClickListener(v -> {
            triggerButtonFeedback();
            if (dbHelper.isFavorite(currentSignNumber)) {
                dbHelper.removeFavorite(currentSignNumber);
                Toast.makeText(this, "已取消收藏", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.addFavorite(currentSignNumber, currentSignData.getType());
                Toast.makeText(this, R.string.added_to_favorites, Toast.LENGTH_SHORT).show();
            }
            updateFavoriteButtonState(btnFavorite);
        });

        btnShare.setOnClickListener(v -> {
            triggerButtonFeedback();
            shareSign();
        });
    }

    private void shareSign() {
        String shareText = "我抽到了吕祖灵签第 " + currentSignNumber + " 签：\n\n" +
                           "【签诗】\n" + currentSignData.getPoem() + "\n\n" +
                           "【解签】\n" + currentSignData.getInterpretation() + "\n\n" +
                           "【指引】\n" + currentSignData.getAdvice() + "\n\n" +
                           "(来自【吕祖灵签】App)";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(intent, "分享到"));
    }

    private void updateFavoriteButtonState(Button btn) {
        if (dbHelper.isFavorite(currentSignNumber)) {
            btn.setText("取消收藏");
        } else {
            btn.setText("收藏");
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
