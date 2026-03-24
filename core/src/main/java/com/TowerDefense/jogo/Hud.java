package com.TowerDefense.jogo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;

public class Hud {

    private Texture imgCoracao;
    private Texture imgMoeda;
    private BitmapFont font;
    private Texture texBotaoBranco;
    private GlyphLayout layout;

    private Texture imgSettings;
    private Texture texFundoPausa;

    private Texture texBtnContinue;
    private Texture texBtnContinueHover;
    private Texture texBtnLeave;
    private Texture texBtnLeaveHover;

    public Rectangle btnHitbox;
    public Rectangle btnSettings;

    // Retângulos para DESENHAR a imagem completa
    public Rectangle rectDesenhoContinue;
    public Rectangle rectDesenhoLeave;

    // Hitboxes REAIS para o clique e o hover
    public Rectangle btnContinue;
    public Rectangle btnLeave;

    public boolean mostrarHitbox = false;
    public boolean pausado = false;
    public boolean voltarAoMenu = false;

    public Hud() {
        font = new BitmapFont(Gdx.files.internal("fontes.fnt"));
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        font.getData().setScale(1.0f);

        imgCoracao = new Texture("coracao.png");
        imgMoeda = new Texture("moeda.png");

        imgSettings = new Texture("settings.png");

        texBtnContinue = new Texture("BUTTON_continue.png");
        texBtnContinueHover = new Texture("BUTTON_continuehover.png");
        texBtnLeave = new Texture("BUTTON_leavebattle.png");
        texBtnLeaveHover = new Texture("BUTTON_leavebattlehover.png");

        btnHitbox = new Rectangle(1600, 50, 240, 60);
        btnSettings = new Rectangle(1820, 970, 70, 70);

        float centroX = 1920 / 2f;
        float btnLargura = 420f;

        float altContinue = btnLargura * ((float) texBtnContinue.getHeight() / texBtnContinue.getWidth());
        float altLeave = btnLargura * ((float) texBtnLeave.getHeight() / texBtnLeave.getWidth());

        float espacoY = 20f;

        // Áreas de DESENHO
        rectDesenhoContinue = new Rectangle(centroX - (btnLargura / 2f), 480, btnLargura, altContinue);
        rectDesenhoLeave = new Rectangle(centroX - (btnLargura / 2f), 480 - altLeave - espacoY, btnLargura, altLeave);

        // 🔥 HITBOXES REDUZIDAS MAIS UM POUCO: Cortando 12% da largura e 25% da altura
        float margemW = btnLargura * 0.12f;

        float margemContH = altContinue * 0.25f;
        btnContinue = new Rectangle(
            rectDesenhoContinue.x + margemW,
            rectDesenhoContinue.y + margemContH,
            rectDesenhoContinue.width - (margemW * 2),
            rectDesenhoContinue.height - (margemContH * 2)
        );

        float margemLeaveH = altLeave * 0.25f;
        btnLeave = new Rectangle(
            rectDesenhoLeave.x + margemW,
            rectDesenhoLeave.y + margemLeaveH,
            rectDesenhoLeave.width - (margemW * 2),
            rectDesenhoLeave.height - (margemLeaveH * 2)
        );

        layout = new GlyphLayout();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        texBotaoBranco = new Texture(pixmap);
        pixmap.dispose();

        Pixmap pixPausa = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixPausa.setColor(new Color(0, 0, 0, 0.7f));
        pixPausa.fill();
        texFundoPausa = new Texture(pixPausa);
        pixPausa.dispose();
    }

    public void verificarClique(float x, float y) {
        if (pausado) {
            if (btnContinue.contains(x, y)) {
                pausado = false;
            } else if (btnLeave.contains(x, y)) {
                voltarAoMenu = true;
            }
        } else {
            if (btnSettings.contains(x, y)) {
                pausado = true;
            } else if (btnHitbox.contains(x, y)) {
                mostrarHitbox = !mostrarHitbox;
            }
        }
    }

    public void desenhar(SpriteBatch batch, int vidas, int dinheiro, int waveAtual, float mouseX, float mouseY) {
        font.setColor(Color.WHITE);

        // --- CORAÇÃO E VIDAS ---
        float coracaoX = 30;
        float coracaoY = 930;
        float coracaoW = 110;
        float coracaoH = 100;

        batch.draw(imgCoracao, coracaoX, coracaoY, coracaoW, coracaoH);

        font.getData().setScale(0.75f);
        font.getData().markupEnabled = true;

        String textoVidas = String.valueOf(vidas);
        layout.setText(font, textoVidas);

        float centroImagemX = coracaoX + (coracaoW / 2f);
        float centroImagemY = coracaoY + (coracaoH / 2f);

        float textoX = centroImagemX - (layout.width / 2f);
        float textoY = centroImagemY + (layout.height / 2f) + 4;

        font.draw(batch, textoVidas, textoX, textoY);

        // --- RESTANTE DO HUD ---
        font.getData().setScale(1.0f);
        batch.draw(imgMoeda, 1410, 992, 50, 50);
        font.draw(batch, "" + dinheiro, 1465, 1030);

        font.getData().setScale(0.82f);
        font.draw(batch, "Wave: " + waveAtual, 1410, 982);

        font.getData().setScale(1.0f);
        if (mostrarHitbox) batch.setColor(0, 0.7f, 0, 1);
        else batch.setColor(0.8f, 0, 0, 1);

        batch.draw(texBotaoBranco, btnHitbox.x, btnHitbox.y, btnHitbox.width, btnHitbox.height);
        batch.setColor(Color.WHITE);

        font.getData().setScale(0.68f);
        String textoBtn = mostrarHitbox ? "Hitboxes: ON" : "Hitboxes: OFF";
        font.draw(batch, textoBtn, btnHitbox.x + 20, btnHitbox.y + 40);

        batch.draw(imgSettings, btnSettings.x, btnSettings.y, btnSettings.width, btnSettings.height);

        // --- TELA DE PAUSA ---
        if (pausado) {
            batch.draw(texFundoPausa, 0, 0, 1920, 1080);

            if (btnContinue.contains(mouseX, mouseY)) {
                batch.draw(texBtnContinueHover, rectDesenhoContinue.x, rectDesenhoContinue.y, rectDesenhoContinue.width, rectDesenhoContinue.height);
            } else {
                batch.draw(texBtnContinue, rectDesenhoContinue.x, rectDesenhoContinue.y, rectDesenhoContinue.width, rectDesenhoContinue.height);
            }

            if (btnLeave.contains(mouseX, mouseY)) {
                batch.draw(texBtnLeaveHover, rectDesenhoLeave.x, rectDesenhoLeave.y, rectDesenhoLeave.width, rectDesenhoLeave.height);
            } else {
                batch.draw(texBtnLeave, rectDesenhoLeave.x, rectDesenhoLeave.y, rectDesenhoLeave.width, rectDesenhoLeave.height);
            }

            font.getData().setScale(1.5f);
            layout.setText(font, "PAUSED");
            font.draw(batch, "PAUSED", (1920 / 2f) - (layout.width / 2f), 680);
            font.getData().setScale(1.0f);
        }
    }

    public void dispose() {
        font.dispose();
        texBotaoBranco.dispose();
        imgCoracao.dispose();
        imgMoeda.dispose();
        imgSettings.dispose();
        texFundoPausa.dispose();

        texBtnContinue.dispose();
        texBtnContinueHover.dispose();
        texBtnLeave.dispose();
        texBtnLeaveHover.dispose();
    }
}
