package com.example.lzlq;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DaodejingManager {

    private static DaodejingManager instance;
    private final List<String> chapters = new ArrayList<>();
    private final Random random = new Random();

    private DaodejingManager() {}

    public static synchronized DaodejingManager getInstance() {
        if (instance == null) {
            instance = new DaodejingManager();
        }
        return instance;
    }

    public void loadChapters(Context context) {
        if (!chapters.isEmpty()) return;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("daodejing.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    chapters.add(line.replace("\\n", "\n"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getRandomChapter() {
        if (chapters.isEmpty()) {
            return "道可道，非常道。\n名可名，非常名。";
        }
        return chapters.get(random.nextInt(chapters.size()));
    }
}
