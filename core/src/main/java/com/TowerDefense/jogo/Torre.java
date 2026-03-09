package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Torre {
    public Vector2 posicao;
    public Rectangle hitbox;
    public float raio;
    public Texture textura;
    public Texture imgProjetil;

    public float tempoTiro = 0;
    public float cooldown = 1.0f;
    public int dano = 50;

    public float tamanhoProjetil;
    public float offsetProjetil;

    public float alturaDesenho = 80f;
    public float larguraDesenho;

    public boolean viradaParaEsquerda = false;

    public Torre(float x, float y, Texture textura, Texture imgProjetil) {
        this.posicao = new Vector2(x, y);
        this.textura = textura;
        this.imgProjetil = imgProjetil;
        this.hitbox = new Rectangle(x, y, 80, 80);

        float proporcao = (float) textura.getWidth() / textura.getHeight();
        this.larguraDesenho = this.alturaDesenho * proporcao;

        if (textura.toString().contains("ninja")) {
            this.raio = 250f;
            this.cooldown = 0.5f;
            this.dano = 75;
            this.tamanhoProjetil = 30f;
            this.offsetProjetil = -45f;
        } else {
            this.raio = 200f;
            this.tamanhoProjetil = 250f;
            this.offsetProjetil = 0f;
        }
    }

    public void atualizar(float delta, Array<Inimigo> listaInimigos, Array<Projetil> listaProjeteis) {
        tempoTiro += delta;

        for (Inimigo in : listaInimigos) {
            float distancia = Vector2.dst(posicao.x + 40, posicao.y + 40, in.posicao.x + 25, in.posicao.y + 25);

            if (distancia <= raio) {
                if (in.posicao.x < posicao.x) {
                    viradaParaEsquerda = true;
                } else {
                    viradaParaEsquerda = false;
                }

                if (tempoTiro >= cooldown) {
                    // --- AJUSTE DA BOCA DA LHAMA ---
                    float bocaX;
                    if (viradaParaEsquerda) {
                        // Se olha para a esquerda, o tiro sai em 20% da largura da imagem (mais pra trás)
                        bocaX = posicao.x + (larguraDesenho * 0.15f);
                    } else {
                        // Se olha para a direita, o tiro sai em 80% da largura da imagem (mais pra frente)
                        bocaX = posicao.x + (larguraDesenho * 0.75f);
                    }

                    // Aproveitei para deixar a altura (bocaY) proporcional também (75% da altura total)
                    float bocaY = posicao.y + (alturaDesenho * 0.55f);

                    listaProjeteis.add(new Projetil(bocaX, bocaY, in, imgProjetil, dano, tamanhoProjetil, offsetProjetil));
                    tempoTiro = 0;
                }

                break;
            }
        }
    }

    public void desenhar(SpriteBatch batch) {
        batch.draw(textura,
            posicao.x, posicao.y,
            larguraDesenho, alturaDesenho,
            0, 0,
            textura.getWidth(), textura.getHeight(),
            viradaParaEsquerda, false);
    }
}
