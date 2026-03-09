package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Hud {
    private BitmapFont font;
    private Texture texBotaoBranco; // Usado para desenhar o fundo colorido do botão

    public Rectangle btnHitbox;
    public boolean mostrarHitbox = false;

    public Hud() {
        font = new BitmapFont();
        font.getData().setScale(2f);

        // Criando o botão na loja
        btnHitbox = new Rectangle(1580, 480, 160, 50);

        // Criando um pixel branco na memória para usarmos como fundo do botão
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        texBotaoBranco = new Texture(pixmap);
        pixmap.dispose();
    }

    // Método chamado pela GameScreen para checar se o botão foi clicado
    public void verificarClique(float x, float y) {
        if (btnHitbox.contains(x, y)) {
            mostrarHitbox = !mostrarHitbox;
        }
    }

    public void desenhar(SpriteBatch batch, int vidas, int dinheiro, int waveAtual) {
        font.setColor(Color.WHITE);

        // Desenha as informações básicas do jogador (ajuste a posição se precisar)
        font.draw(batch, "Vidas: " + vidas, 50, 1050);
        font.draw(batch, "Dinheiro: $" + dinheiro, 250, 1050);
        font.draw(batch, "Wave: " + waveAtual, 500, 1050);

        // --- DESENHO DO BOTÃO DE HITBOX ---
        // 1. Cor do fundo
        if (mostrarHitbox) {
            batch.setColor(0, 0.7f, 0, 1); // Verde (Ligado)
        } else {
            batch.setColor(0.8f, 0, 0, 1); // Vermelho (Desligado)
        }

        // 2. Desenha o fundo esticando o pixel branco
        batch.draw(texBotaoBranco, btnHitbox.x, btnHitbox.y, btnHitbox.width, btnHitbox.height);
        batch.setColor(Color.WHITE); // Volta a cor para branco para não bugar o resto do jogo

        // 3. Desenha o texto do botão centralizado
        font.getData().setScale(1.5f);
        String textoBtn = mostrarHitbox ? "Hitboxes ON" : "Hitboxes OFF";
        font.draw(batch, textoBtn, btnHitbox.x + 15, btnHitbox.y + 35);
        font.getData().setScale(2f); // Volta o tamanho da fonte ao normal
    }

    public void dispose() {
        font.dispose();
        texBotaoBranco.dispose();
    }
}
