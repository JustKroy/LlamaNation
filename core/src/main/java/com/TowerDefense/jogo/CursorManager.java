package com.TowerDefense.jogo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class CursorManager {

    public static Cursor cursorInvisivel;

    private static Texture cursorNormal;
    private static Texture cursorHover;

    private static boolean hover = false;

    private static final float OFFSET_X = 16;
    private static final float OFFSET_Y = 16;

    public static void init() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0);
        pixmap.fill();

        cursorInvisivel = Gdx.graphics.newCursor(pixmap, 0, 0);
        Gdx.graphics.setCursor(cursorInvisivel);

        pixmap.dispose();

        cursorNormal = new Texture("Cursor_normal.png");
        cursorHover = new Texture("Cursor_selected.png");
    }

    public static void inicializar() {
        init();
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
        if (cursorInvisivel != null) {
            Gdx.graphics.setCursor(cursorInvisivel);
        }
    }

    public static void desenhar(Batch batch, Vector2 posMouse) {
        if (Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.Android ||
            Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.iOS) {
            return;
        }

        Texture cursorAtual = hover ? cursorHover : cursorNormal;

        if (cursorAtual != null) {
            batch.draw(
                cursorAtual,
                posMouse.x - OFFSET_X,
                posMouse.y - OFFSET_Y,
                32,
                32
            );
        }
    }

    public static void dispose() {
        if (cursorNormal != null) cursorNormal.dispose();
        if (cursorHover != null) cursorHover.dispose();
        if (cursorInvisivel != null) cursorInvisivel.dispose();
    }
}
