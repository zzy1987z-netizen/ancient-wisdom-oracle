package com.example.lzlq;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

public class TypewriterTextView extends AppCompatTextView {

    private CharSequence mText;
    private int mIndex;
    private long mDelay = 150; // 每个字的延迟，默认150毫秒
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private OnCharacterAppearanceListener mListener;

    public interface OnCharacterAppearanceListener {
        void onCharacterAppeared(char character);
    }

    public TypewriterTextView(Context context) {
        super(context);
    }

    public TypewriterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private final Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            setText(mText.subSequence(0, mIndex++));
            if (mIndex <= mText.length()) {
                if(mListener != null) {
                   mListener.onCharacterAppeared(mText.charAt(mIndex - 1));
                }
                mHandler.postDelayed(characterAdder, mDelay);
            } 
        }
    };

    public void animateText(CharSequence text) {
        mText = text;
        mIndex = 0;
        setText("");
        mHandler.removeCallbacks(characterAdder);
        mHandler.post(characterAdder);
    }

    public void setCharacterDelay(long millis) {
        mDelay = millis;
    }

    public void setOnCharacterAppearanceListener(OnCharacterAppearanceListener listener) {
        mListener = listener;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacks(characterAdder); // 防止内存泄漏
    }
}
