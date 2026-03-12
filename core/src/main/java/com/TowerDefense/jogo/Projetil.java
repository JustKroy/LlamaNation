package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Projetil {

    public Vector2 posicao;
    public Vector2 velocidade = new Vector2(0, 0);
    public Rectangle hitbox;
    public boolean ativo = true;

    public Inimigo alvo;
    public int dano;

    public TextureRegion textura;
    private float rotacao;

    // Agora a velocidade é definida quando o tiro é criado
    private float velocidadeTiro;

    public float largura;
    public float altura;
    public float metadeX;
    public float metadeY;

    private float anguloOffset;

    // ADICIONEI "float velocidadeDefinida" no final do construtor
    public Projetil(float bocaX, float bocaY, Inimigo alvo, Texture imgProjetil, int dano, float largura, float altura, float anguloOffset, float velocidadeDefinida) {
        this.largura = largura;
        this.altura = altura;
        this.metadeX = largura / 2f;
        this.metadeY = altura / 2f;
        this.anguloOffset = anguloOffset;
        this.velocidadeTiro = velocidadeDefinida; // Define a velocidade aqui!

        this.posicao = new Vector2(bocaX - metadeX, bocaY - metadeY);
        this.alvo = alvo;
        this.dano = dano;
        this.textura = new TextureRegion(imgProjetil);
        this.hitbox = new Rectangle(posicao.x, posicao.y, largura, altura);

        mirarNoAlvo();

        if (velocidade.x == 0 && velocidade.y == 0) {
            this.ativo = false;
        }
    }

    private void mirarNoAlvo() {
        if (alvo != null && alvo.vida > 0) {
            float centroAlvoX = alvo.posicao.x + 25;
            float centroAlvoY = alvo.posicao.y + 25;
            float centroTiroX = posicao.x + metadeX;
            float centroTiroY = posicao.y + metadeY;

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
            batch.draw(textura, posicao.x, posicao.y, metadeX, metadeY, largura, altura, 1.0f, 1.0f, rotacao);
        }
    }

    public Inimigo checarColisao(Array<Inimigo> listaInimigos) {
        if (!ativo) return null;
        for (Inimigo in : listaInimigos) {
            float centroTiroX = posicao.x + metadeX;
            float centroTiroY = posicao.y + metadeY;
            float centroInimigoX = in.posicao.x + 25;
            float centroInimigoY = in.posicao.y + 25;
            if (Vector2.dst(centroTiroX, centroTiroY, centroInimigoX, centroInimigoY) <= 15) {
                in.vida -= this.dano;
                this.ativo = false;
                return in;
            }
        }
        return null;
    }
}
