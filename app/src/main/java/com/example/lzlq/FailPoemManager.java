package com.example.lzlq;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FailPoemManager {

    private static FailPoemManager instance;
    private final List<String> poems = new ArrayList<>();
    private final Random random = new Random();

    private FailPoemManager() {}

    public static synchronized FailPoemManager getInstance() {
        if (instance == null) {
            instance = new FailPoemManager();
        }
        return instance;
    }

    public void loadPoems(Context context) {
        if (!poems.isEmpty()) return;
        try {
            InputStream is = context.getAssets().open("fail_poems.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                poems.add(jsonArray.getString(i));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public String getRandomPoem() {
        if (poems.isEmpty()) {
            return "加载诗篇失败，请重启应用。";
        }
        return poems.get(random.nextInt(poems.size()));
    }
}
