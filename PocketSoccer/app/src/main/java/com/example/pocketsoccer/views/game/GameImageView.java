package com.example.pocketsoccer.views.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class GameImageView extends ImageView {
    public GameImageView(Context context) {
        super(context);
    }

    public GameImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("NewApi")
    public GameImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {

    }
}
