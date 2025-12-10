package com.example.lzlq;

import android.app.Application;

public class MyApplication extends Application {

    // 全局“守门人”，用于标记欢迎流程是否已在本“会话”中完成
    public static boolean isWelcomeFlowCompleted = false;

    @Override
    public void onCreate() {
        super.onCreate();

        // 在App的创世之源，加载所有核心数据！
        SignManager.getInstance().loadSigns(this);
        FailPoemManager.getInstance().loadPoems(this);
        DaodejingManager.getInstance().loadChapters(this);
        
        // 激活我们的音乐总监和震动总监！
        SoundManager.getInstance(this);
        VibrationManager.getInstance().init(this);
    }
}
