package com.TowerDefense.jogo;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
    public static AssetManager manager;

    public static void load() {
        manager = new AssetManager();

        manager.load("Back_Button.png", Texture.class);
        manager.load("Select_Button.png", Texture.class);
        manager.load("Select_ButtonHover.png", Texture.class);
        manager.load("Skins_Button.png", Texture.class);
        manager.load("Skins_ButtonHover.png", Texture.class);
        manager.load("Infos_Button.png", Texture.class);
        manager.load("Infos_ButtonHover.png", Texture.class);

        //--------------------------- MENUSCREEN -------------------------------
        manager.load("Play_Button.png", Texture.class);
        manager.load("Play_ButtonHover.png", Texture.class);
        manager.load("Heroes_Button.png", Texture.class);
        manager.load("Heroes_ButtonHover.png", Texture.class);
        manager.load("Settings_Button.png", Texture.class);
        manager.load("Shop_Button.png", Texture.class);
        manager.load("Shop_ButtonHover.png", Texture.class);

        manager.load("MenuScreen_Background.png", Texture.class);
        manager.load("Popup_Background.png", Texture.class);
        manager.load("Painel.jpg", Texture.class);
        manager.load("Cursor_normal.png", Texture.class);
        manager.load("Cursor_selected.png", Texture.class);
    }

    public static void finish() {
        manager.finishLoading();
    }

    public static Texture get(String path) {
        return manager.get(path, Texture.class);
    }
}
