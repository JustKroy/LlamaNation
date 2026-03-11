package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public abstract class Torre {

    public Vector2 posicao;
    public Rectangle hitbox;
    public Texture textura;
    public Texture imgProjetil;

    public float raio;
    public float cooldown;
    public int dano;
    public float tamanhoProjetil;
    public float offsetProjetil;

    public float tempoTiro = 0;
    public float alturaDesenho = 80f;
    public float larguraDesenho;
    public boolean viradaParaEsquerda = false;

    public Torre(float x, float y, Texture textura, Texture imgProjetil) {
        this.posicao = new Vector2(x, y);
        this.textura = textura;
        this.imgProjetil = imgProjetil;

        float proporcao = (float) textura.getWidth() / textura.getHeight();
        this.larguraDesenho = this.alturaDesenho * proporcao;
        this.hitbox = new Rectangle(x, y, larguraDesenho, alturaDesenho);
    }

    public void atualizar(float delta, Array<Inimigo> listaInimigos, Array<Projetil> listaProjeteis) {
        tempoTiro += delta;

        for (Inimigo in : listaInimigos) {
            float centroTorreX = posicao.x + (larguraDesenho / 2f);
            float centroTorreY = posicao.y + (alturaDesenho / 2f);

            float centroInimigoX = in.posicao.x + 25f;
            float centroInimigoY = in.posicao.y + 25f;

            float distancia = Vector2.dst(centroTorreX, centroTorreY, centroInimigoX, centroInimigoY);

            if (distancia <= raio) {
                viradaParaEsquerda = (in.posicao.x < posicao.x);

                if (tempoTiro >= cooldown) {
                    float bocaX = viradaParaEsquerda ? posicao.x + (larguraDesenho * 0.2f) : posicao.x + (larguraDesenho * 0.8f);
                    float bocaY = posicao.y + (alturaDesenho * 0.55f);

                    // --- MÁGICA DA PROPORÇÃO DO TIRO ---
                    float propProjetil = (float) imgProjetil.getWidth() / imgProjetil.getHeight();

                    float alturaTiro = tamanhoProjetil;

                    // --- REDUTOR DO CUSPE (0.1f) ---
                    // Como a Kunai tem um offset (ex: -45) e o cuspe é 0, usamos isso para separar os dois!
                    if (offsetProjetil == 0f) {
                        alturaTiro = tamanhoProjetil * 0.05f; // Encolhe o cuspe para 10%
                    }

                    float larguraTiro = alturaTiro * propProjetil;

                    // Cria o projétil enviando a LARGURA e ALTURA separadas
                    listaProjeteis.add(new Projetil(bocaX, bocaY, in, imgProjetil, dano, larguraTiro, alturaTiro, offsetProjetil));

                    tempoTiro = 0;
                }
                break;
            }
        }
    }

    public void desenhar(SpriteBatch batch) {
        batch.draw(textura, posicao.x, posicao.y, larguraDesenho, alturaDesenho,
            0, 0, textura.getWidth(), textura.getHeight(), viradaParaEsquerda, false);
    }
}
