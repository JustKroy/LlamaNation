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
    private Texture imgVoltar;
    private Texture imgPlay;
    private Texture texFundoPausa;

    public Rectangle btnHitbox;
    public Rectangle btnSettings;
    public Rectangle btnVoltar;
    public Rectangle btnPlayPause;

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
        imgVoltar = new Texture("voltar.png");
        imgPlay = new Texture("play.png");

        btnHitbox = new Rectangle(1600, 50, 240, 60);
        btnSettings = new Rectangle(1820, 970, 70, 70);

        float centroX = 1920 / 2f;
        float btnLargura = 240;
        float btnAltura = 80;
        float espaco = 40;

        btnVoltar = new Rectangle(centroX - btnLargura - (espaco / 2f), 450, btnLargura, btnAltura);
        btnPlayPause = new Rectangle(centroX + (espaco / 2f), 450, btnLargura, btnAltura);

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
            if (btnVoltar.contains(x, y)) {
                voltarAoMenu = true;
            } else if (btnPlayPause.contains(x, y)) {
                pausado = false;
            }
        } else {
            if (btnSettings.contains(x, y)) {
                pausado = true;
            } else if (btnHitbox.contains(x, y)) {
                mostrarHitbox = !mostrarHitbox;
            }
        }
    }

    public void desenhar(SpriteBatch batch, int vidas, int dinheiro, int waveAtual) {
        font.setColor(Color.WHITE);

        // --- CORAÇÃO E VIDAS ---
        float coracaoX = 30;
        float coracaoY = 930;
        float coracaoW = 110; // Aumentei um tiquinho para ajudar na visão
        float coracaoH = 100;

        batch.draw(imgCoracao, coracaoX, coracaoY, coracaoW, coracaoH);

        // Ajuste fino dos números:
        font.getData().setScale(0.75f); // Diminuí um pouco a escala para não sufocar o ícone

        // MÁGICA PARA OS NÚMEROS NÃO FICAREM MUITO SEPARADOS:
        // Reduz o espaçamento entre os caracteres (ajuste esse valor se precisar de mais perto ou longe)
        font.getData().markupEnabled = true;

        String textoVidas = String.valueOf(vidas);
        layout.setText(font, textoVidas);

        // Centralização exata usando o centro da imagem e metade da largura do texto
        float centroImagemX = coracaoX + (coracaoW / 2f);
        float centroImagemY = coracaoY + (coracaoH / 2f);

        // O ponto X de desenho é o centro da imagem menos metade da largura da palavra escrita
        float textoX = centroImagemX - (layout.width / 2f);
        // O ponto Y de desenho é o centro da imagem mais metade da altura da fonte (ajuste de +8 para centralizar no "olho")
        float textoY = centroImagemY + (layout.height / 2f) + 4;

        font.draw(batch, textoVidas, textoX, textoY);

        // --- RESTANTE DO HUD (Mantive igual ao seu) ---
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

        if (pausado) {
            batch.draw(texFundoPausa, 0, 0, 1920, 1080);
            batch.draw(imgPlay, btnPlayPause.x, btnPlayPause.y, btnPlayPause.width, btnPlayPause.height);
            batch.draw(imgVoltar, btnVoltar.x, btnVoltar.y, btnVoltar.width, btnVoltar.height);

            font.getData().setScale(1.5f);
            layout.setText(font, "PAUSED");
            font.draw(batch, "PAUSED", (1920 / 2f) - (layout.width / 2f), 650);
            font.getData().setScale(1.0f);
        }
    }

    public void dispose() {
        font.dispose();
        texBotaoBranco.dispose();
        imgCoracao.dispose();
        imgMoeda.dispose();
        imgSettings.dispose();
        imgVoltar.dispose();
        imgPlay.dispose();
        texFundoPausa.dispose();
    }
}
