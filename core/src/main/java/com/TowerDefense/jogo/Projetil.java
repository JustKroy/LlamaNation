package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Projetil {

    // --- FÍSICA E POSIÇÃO ---
    public Vector2 posicao;
    public Vector2 velocidade;
    public Rectangle hitbox;
    public boolean ativo = true;

    // --- INFORMAÇÕES DO TIRO ---
    public Inimigo alvo;
    public int dano;

    // --- IMAGEM E ROTAÇÃO ---
    public TextureRegion textura;
    private float rotacao;
    private float velocidadeTiro = 800f;

    // --- TAMANHOS ---
    public float tamanho;
    public float metade;

    // --- VARIÁVEL NOVA ---
    private float anguloOffset;

    // --- CONSTRUTOR ---
    public Projetil(float bocaX, float bocaY, Inimigo alvo, Texture imgProjetil, int dano, float tamanhoOriginal, float anguloOffset) {

        // --- AQUI DIMINUÍMOS O TAMANHO ---
        // Multiplicamos por 0.4f (40% do tamanho). Se quiser maior, use 0.6f. Se quiser menor, 0.2f.
        this.tamanho = tamanhoOriginal * 0.4f;

        this.metade = this.tamanho / 2;
        this.anguloOffset = anguloOffset;

        // Posiciona o tiro centralizado na boca da lhama
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
            batch.draw(textura,
                posicao.x, posicao.y,
                metade, metade,
                tamanho, tamanho,
                0.2f, 0.2f,
                rotacao);
        }
    }

    public Inimigo checarColisao(Array<Inimigo> listaInimigos) {
        if (!ativo) return null;

        for (Inimigo in : listaInimigos) {
            float centroTiroX = posicao.x + metade;
            float centroTiroY = posicao.y + metade;
            float centroInimigoX = in.posicao.x + 25;
            float centroInimigoY = in.posicao.y + 25;

            float distancia = Vector2.dst(centroTiroX, centroTiroY, centroInimigoX, centroInimigoY);

            // Ajustado para 15 pixels pois o tiro agora é menor
            if (distancia <= 15) {
                in.vida -= this.dano;
                this.ativo = false;
                return in;
            }
        }
        return null;
    }
}
