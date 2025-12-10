package com.example.lzlq;

import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class FavoritesActivity extends BaseActivity {

    private boolean hasInitialized = false;

    private ListView listView;
    private FavoritesDbHelper dbHelper;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
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
        setupGlobalUI();

        dbHelper = new FavoritesDbHelper(this);
        listView = findViewById(R.id.lv_favorites);

        final String[] from = new String[]{FavoritesDbHelper.COLUMN_SIGN_ID, FavoritesDbHelper.COLUMN_SIGN_ATTRIBUTE};
        final int[] to = new int[]{R.id.tv_sign_number, R.id.tv_sign_attribute};

        adapter = new SimpleCursorAdapter(this, R.layout.list_item_favorite, null, from, to, 0);

        adapter.setViewBinder((view, cursor, columnIndex) -> {
            String text = cursor.getString(columnIndex);
            TextView textView = (TextView) view;
            if (view.getId() == R.id.tv_sign_number) {
                textView.setText("第 " + text + " 签");
                return true;
            } else if (view.getId() == R.id.tv_sign_attribute) {
                textView.setText("[" + text + "]");
                return true;
            }
            return false;
        });

        listView.setAdapter(adapter);
        setupDoubleClickListener();

        // 关键修复：在界面初始化时，立刻加载并显示收藏数据
        Cursor initialCursor = dbHelper.getAllFavorites();
        adapter.changeCursor(initialCursor);
    }

    private void setupDoubleClickListener() {
        GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                int position = listView.pointToPosition((int) e.getX(), (int) e.getY());
                if (position != AdapterView.INVALID_POSITION) {
                    Cursor cursor = (Cursor) adapter.getItem(position);
                    int signId = cursor.getInt(cursor.getColumnIndexOrThrow(FavoritesDbHelper.COLUMN_SIGN_ID));

                    Intent intent = new Intent(FavoritesActivity.this, ResultActivity.class);
                    intent.putExtra("SIGN_NUMBER", signId);
                    intent.putExtra("FROM_FAVORITES", true);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(FavoritesActivity.this);
                    startActivity(intent, options.toBundle());
                }
                return true;
            }
        });

        listView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasInitialized) {
            Cursor newCursor = dbHelper.getAllFavorites();
            adapter.changeCursor(newCursor);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (adapter != null && adapter.getCursor() != null && !adapter.getCursor().isClosed()) {
            adapter.changeCursor(null);
        }
    }

    @Override
    protected void onDestroy() {
        if (adapter != null && adapter.getCursor() != null && !adapter.getCursor().isClosed()) {
            adapter.getCursor().close();
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroy();
    }
}
