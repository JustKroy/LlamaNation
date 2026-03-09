package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Projetil {
    public Vector2 posicao;
    public Vector2 velocidade;
    public Rectangle hitbox;
    public boolean ativo = true;
    public Inimigo alvo;
    public int dano;

    public TextureRegion textura;
    private float rotacao;
    private float velocidadeTiro = 800f;

    public float tamanho;
    public float metade;
    // --- VARIÁVEL NOVA ---
    private float anguloOffset;

    // Construtor atualizado para receber o offset
    public Projetil(float bocaX, float bocaY, Inimigo alvo, Texture imgProjetil, int dano, float tamanho, float anguloOffset) {
        this.tamanho = tamanho;
        this.metade = tamanho / 2;
        this.anguloOffset = anguloOffset; // Guarda o offset vindo da torre
        this.posicao = new Vector2(bocaX - metade, bocaY - metade);
        this.alvo = alvo;
        this.dano = dano;
        this.textura = new TextureRegion(imgProjetil);
        this.hitbox = new Rectangle(posicao.x, posicao.y, tamanho, tamanho);

        mirarNoAlvo();
    }

    private void mirarNoAlvo() {
        if (alvo != null && alvo.vida > 0) {
            float centroAlvoX = alvo.posicao.x + 25;
            float centroAlvoY = alvo.posicao.y + 25;

            float centroTiroX = posicao.x + metade;
            float centroTiroY = posicao.y + metade;

            float deltaX = centroAlvoX - centroTiroX;
            float deltaY = centroAlvoY - centroTiroY;

            // --- LÓGICA DE ROTAÇÃO ARRUMADA ---
            // Usamos o anguloOffset que a torre nos passou (0 para guspe, -45 para kunai)
            this.rotacao = (MathUtils.atan2(deltaY, deltaX) * MathUtils.radiansToDegrees) + anguloOffset;

            this.velocidade = new Vector2(deltaX, deltaY).nor().scl(velocidadeTiro);
        }
    }

    public void atualizar(float delta) {
        if (!ativo) return;

        mirarNoAlvo();

        posicao.x += velocidade.x * delta;
        posicao.y += velocidade.y * delta;
        hitbox.setPosition(posicao.x, posicao.y);

        if (posicao.x < -100 || posicao.x > 2000 || posicao.y < -100 || posicao.y > 1200) {
            ativo = false;
        }
    }

    public void desenhar(SpriteBatch batch) {
        if (ativo) {
            // Se o guspe 250 ficar cortado, aumente o tamanho da textura gerada no construtor.
            batch.draw(textura,
                posicao.x, posicao.y,
                metade, metade,
                tamanho, tamanho,
                1.0f, 1.0f,
                rotacao);
        }
    }
}
