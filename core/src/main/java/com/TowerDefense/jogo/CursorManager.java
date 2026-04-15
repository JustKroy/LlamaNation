package com.TowerDefense.jogo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class CursorManager {

    private static Cursor cursorVazio;

    private static Texture cursorNormal;
    private static Texture cursorHover;

    private static boolean hover = false;

    private static final float OFFSET_X = 16;
    private static final float OFFSET_Y = 16;

    public static void init() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0);
        pixmap.fill();

        cursorVazio = Gdx.graphics.newCursor(pixmap, 0, 0);
        Gdx.graphics.setCursor(cursorVazio);

        pixmap.dispose();

        cursorNormal = new Texture("Cursor_normal.png");
        cursorHover = new Texture("Cursor_selected.png");
    }

    // 🔥 RESET TODO FRAME
    public static void setDefault() {
        hover = false;
    }

    // 🔥 CHAMADO PELOS BOTÕES
    public static void setHover() {
        hover = true;
    }

    public static void aplicarCursorInvisivel() {
        Gdx.graphics.setCursor(cursorVazio);
    }

    public static void desenhar(SpriteBatch batch, Vector2 posMouse) {
        Texture cursorAtual = hover ? cursorHover : cursorNormal;

        batch.draw(
            cursorAtual,
            posMouse.x - OFFSET_X,
            posMouse.y - OFFSET_Y,
            32,
            32
        );
    }

    public static void dispose() {
        cursorNormal.dispose();
        cursorHover.dispose();
    }
}
