package com.example.lzlq;

import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SignManager {

    private static final String TAG = "SignManager_DEBUG";
    private static SignManager instance;
    private List<SignData> signList = new ArrayList<>();

    private SignManager() {}

    public static synchronized SignManager getInstance() {
        if (instance == null) {
            instance = new SignManager();
        }
        return instance;
    }

    public void loadSigns(Context context) {
        if (!signList.isEmpty()) {
            Log.d(TAG, "仓库已有库存，无需重复加载。");
            return;
        }

        Log.d(TAG, "开始加载signs.json文件...");
        String json;
        try {
            InputStream is = context.getAssets().open("signs.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            Log.e(TAG, "读取assets/signs.json文件失败！", ex);
            return;
        }

        try {
            JSONArray jsonArray = new JSONArray(json);
            //【第一关：开箱验货】
            Log.d(TAG, "报告！成功将字符串解析为JSON数组！数组长度: " + jsonArray.length());

            for (int i = 0; i < jsonArray.length(); i++) {
                //【第二关：遍历装载】
                Log.d(TAG, "报告！正在装载第 " + i + " 号签...");
                JSONObject obj = jsonArray.getJSONObject(i);
                SignData sign = new SignData(
                        obj.getInt("id"),
                        obj.getString("title"),
                        obj.getString("type"),
                        obj.getString("poem"),
                        obj.getString("interpretation"),
                        obj.getString("advice")
                );
                signList.add(sign);
            }
            //【第三关：入库盘点】
            Log.d(TAG, "报告！所有货物，全部入库完毕！最终库存数量: " + signList.size());

        } catch (JSONException e) {
            Log.e(TAG, "解析JSON数据时发生致命错误！", e);
        }
    }

    public SignData getSign(int signNumber) {
        if (signList.isEmpty() || signNumber < 1 || signNumber > signList.size()) {
            Log.e(TAG, "提货失败！仓库为空或签号无效！请求的签号: " + signNumber + ", 当前库存: " + signList.size());
            return new SignData(0, "错误", "", "数据加载失败或签号无效", "", "");
        }
        return signList.get(signNumber - 1);
    }

    public static class SignData {
        // ... (内部类代码保持不变)
        private int id; private String title; private String type; private String poem; private String interpretation; private String advice;
        public SignData(int id, String title, String type, String poem, String interpretation, String advice) { this.id = id; this.title = title; this.type = type; this.poem = poem; this.interpretation = interpretation; this.advice = advice; }
        public int getId() { return id; } public String getTitle() { return title; } public String getType() { return type; } public String getPoem() { return poem; } public String getInterpretation() { return interpretation; } public String getAdvice() { return advice; }
    }
}
