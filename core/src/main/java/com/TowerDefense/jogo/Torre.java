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

    // Velocidade padrão para torres que não definirem uma própria
    public float velocidadeProjetil = 800f;

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

        // Inicializa a hitbox.
        // Nota: Classes filhas como LlamaCyborg devem atualizar isso após dividir a largura por 11.
        this.hitbox = new Rectangle(x, y, larguraDesenho, alturaDesenho);
    }

    public void atualizar(float delta, Array<Inimigo> listaInimigos, Array<Projetil> listaProjeteis) {
        tempoTiro += delta;

        // Ponto central para o cálculo do Range (Raio) bater com o círculo visual
        float centroTorreX = posicao.x + (larguraDesenho / 2f);
        float centroTorreY = posicao.y + (alturaDesenho / 2f);

        for (Inimigo in : listaInimigos) {
            float centroInimigoX = in.posicao.x + 25f;
            float centroInimigoY = in.posicao.y + 25f;

            float distancia = Vector2.dst(centroTorreX, centroTorreY, centroInimigoX, centroInimigoY);

            if (distancia <= raio) {
                viradaParaEsquerda = (in.posicao.x < posicao.x);

                if (tempoTiro >= cooldown) {
                    float bocaX = viradaParaEsquerda ? posicao.x + (larguraDesenho * 0.2f) : posicao.x + (larguraDesenho * 0.8f);
                    float bocaY = posicao.y + (alturaDesenho * 0.55f);

                    float propProjetil = (float) imgProjetil.getWidth() / imgProjetil.getHeight();
                    float alturaTiro = tamanhoProjetil;

                    // Pequeno ajuste de escala se o offset for zero (evita tiro gigante)
                    if (offsetProjetil == 0f) {
                        alturaTiro = tamanhoProjetil * 0.6f;
                    }

                    float larguraTiro = alturaTiro * propProjetil;

                    // CHAMADA CORRIGIDA: Agora com os 9 parâmetros!
                    listaProjeteis.add(new Projetil(bocaX, bocaY, in, imgProjetil, dano, larguraTiro, alturaTiro, offsetProjetil, velocidadeProjetil));

                    tempoTiro = 0;
                }
                break; // Ataca apenas um inimigo por vez
            }
        }
    }

    public void desenhar(SpriteBatch batch) {
        batch.draw(textura, posicao.x, posicao.y, larguraDesenho, alturaDesenho,
            0, 0, textura.getWidth(), textura.getHeight(), viradaParaEsquerda, false);
    }
}
