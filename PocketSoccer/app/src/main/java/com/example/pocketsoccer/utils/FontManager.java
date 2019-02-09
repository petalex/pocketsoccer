package com.example.pocketsoccer.utils;

import android.content.res.AssetManager;
import android.graphics.Typeface;

public class FontManager {
    private static Typeface titleFont, menuFont;

    public static void init(AssetManager manager) {
        titleFont = Typeface.createFromAsset(manager, "fonts/Chunkfive.otf");
        menuFont = Typeface.createFromAsset(manager, "fonts/Sanson.otf");
    }

    public static Typeface getTitleFont() {
        return titleFont;
    }

    public static Typeface getMenuFont() {
        return menuFont;
    }
}
