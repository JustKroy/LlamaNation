package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Hud {
    private Texture imgCoracao;
    private Texture imgMoeda;

    private BitmapFont font;
    private Texture texBotaoBranco;

    public Rectangle btnHitbox;
    public boolean mostrarHitbox = false;

    public Hud() {
        font = new BitmapFont();
        font.getData().setScale(2.2f);

        imgCoracao = new Texture("coracao.png");
        imgMoeda = new Texture("moeda.png");

        // Botão de Hitbox agora fica mais para baixo para não atrapalhar nada
        btnHitbox = new Rectangle(1600, 50, 240, 60);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        texBotaoBranco = new Texture(pixmap);
        pixmap.dispose();
    }

    public void verificarClique(float x, float y) {
        if (btnHitbox.contains(x, y)) {
            mostrarHitbox = !mostrarHitbox;
        }
    }

    public void desenhar(SpriteBatch batch, int vidas, int dinheiro, int waveAtual) {
        font.setColor(Color.WHITE);

        // --- VIDA (Canto Superior Esquerdo - Coração Maior) ---
        // Aumentei para 70x70 para ficar bem visível
        batch.draw(imgCoracao, 20, 815, 300, 300);
        font.draw(batch, "" + vidas, 70, 1030);

        // --- DINHEIRO (Topo da Loja - Canto Superior Direito) ---
        // Coloquei bem no cantinho para liberar o espaço das lhamas
        batch.draw(imgMoeda, 1410, 992, 50, 50);
        font.draw(batch, "" +dinheiro, 1465, 1030);

        // --- WAVE (Logo abaixo do dinheiro) ---
        font.getData().setScale(1.8f); // Wave um pouco menor para dar charme
        font.draw(batch, "Wave: " + waveAtual, 1410, 982);
        font.getData().setScale(2.2f);

        // --- BOTÃO DE HITBOX (Lá no rodapé da direita) ---
        if (mostrarHitbox) {
            batch.setColor(0, 0.7f, 0, 1);
        } else {
            batch.setColor(0.8f, 0, 0, 1);
        }

        batch.draw(texBotaoBranco, btnHitbox.x, btnHitbox.y, btnHitbox.width, btnHitbox.height);
        batch.setColor(Color.WHITE);

        font.getData().setScale(1.5f);
        String textoBtn = mostrarHitbox ? "Hitboxes: ON" : "Hitboxes: OFF";
        font.draw(batch, textoBtn, btnHitbox.x + 20, btnHitbox.y + 40);
        font.getData().setScale(2.2f);
    }

    public void dispose() {
        font.dispose();
        texBotaoBranco.dispose();
        imgCoracao.dispose();
        imgMoeda.dispose();
    }
}
