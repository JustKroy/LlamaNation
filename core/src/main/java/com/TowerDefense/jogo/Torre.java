package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public abstract class Torre {
    public TipoLlama tipoLlama;
    public enum ModoAlvo {
        PRIMEIRO, ULTIMO, MAIS_FORTE
    }

    public ModoAlvo modoAlvoAtual = ModoAlvo.PRIMEIRO;

    public Vector2 posicao;
    public Rectangle hitbox;
    public Texture textura;
    public Texture imgProjetil;

    public float raio;
    public float cooldown;
    public int dano;
    public float tamanhoProjetil;
    public float offsetProjetil;

    public float velocidadeProjetil = 800f;

    public float tempoTiro = 0;
    public float alturaDesenho = 80f;
    public float larguraDesenho;
    public boolean viradaParaEsquerda = false;

    // 🔥 NOVO: Construtor simplificado para torres que não atiram (como a LlamaBurguesa)
    public Torre(float x, float y) {
        this.posicao = new Vector2(x, y);
        this.hitbox = new Rectangle(x, y, 80, 80); // Tamanho padrão
        this.larguraDesenho = 80f;
        this.alturaDesenho = 80f;
    }

    // Construtor original para as torres de ataque
    public Torre(float x, float y, Texture textura, Texture imgProjetil) {
        this.posicao = new Vector2(x, y);
        this.textura = textura;
        this.imgProjetil = imgProjetil;

        float proporcao = (float) textura.getWidth() / textura.getHeight();
        this.larguraDesenho = this.alturaDesenho * proporcao;

        this.hitbox = new Rectangle(x, y, larguraDesenho, alturaDesenho);
    }

    // 🔥 ATUALIZADO: Agora retorna "int", que é o dinheiro gerado
    public int atualizar(float delta, Array<Inimigo> listaInimigos, Array<Projetil> listaProjeteis) {
        tempoTiro += delta;

        float centroTorreX = posicao.x + (larguraDesenho / 2f);
        float centroTorreY = posicao.y + (alturaDesenho / 2f);

        // 1. PASSO: Coletar apenas os inimigos que estão DENTRO do raio da torre
        Array<Inimigo> inimigosNoRange = new Array<>();
        for (Inimigo in : listaInimigos) {
            float centroInimigoX = in.posicao.x + (in.largura / 2f);
            float centroInimigoY = in.posicao.y + (in.altura / 2f);
            float distancia = Vector2.dst(centroTorreX, centroTorreY, centroInimigoX, centroInimigoY);

            if (distancia <= raio) {
                inimigosNoRange.add(in);
            }
        }

        // 2. PASSO: Escolher o melhor alvo baseado estritamente na regra do botão
        Inimigo alvoEscolhido = null;

        if (inimigosNoRange.size > 0) {
            alvoEscolhido = inimigosNoRange.get(0); // Começa assumindo que o primeiro da lista é o alvo

            for (int i = 1; i < inimigosNoRange.size; i++) {
                Inimigo in = inimigosNoRange.get(i);

                if (modoAlvoAtual == ModoAlvo.PRIMEIRO) {
                    // Quem tem a MAIOR distância percorrida está na frente
                    if (in.distanciaPercorrida > alvoEscolhido.distanciaPercorrida) {
                        alvoEscolhido = in;
                    }
                }
                else if (modoAlvoAtual == ModoAlvo.ULTIMO) {
                    // Quem tem a MENOR distância percorrida está atrás
                    if (in.distanciaPercorrida < alvoEscolhido.distanciaPercorrida) {
                        alvoEscolhido = in;
                    }
                }
                else if (modoAlvoAtual == ModoAlvo.MAIS_FORTE) {
                    // Quem tem a MAIOR vida no momento
                    if (in.vida > alvoEscolhido.vida) {
                        alvoEscolhido = in;
                    }
                }
            }
        }

        // 3. PASSO: Mirar e Atirar no alvo definitivo
        if (alvoEscolhido != null) {
            viradaParaEsquerda = (alvoEscolhido.posicao.x < posicao.x);

            if (tempoTiro >= cooldown) {
                float bocaX = viradaParaEsquerda ? posicao.x + (larguraDesenho * 0.2f) : posicao.x + (larguraDesenho * 0.8f);
                float bocaY = posicao.y + (alturaDesenho * 0.55f);

                float propProjetil = (float) imgProjetil.getWidth() / imgProjetil.getHeight();
                float alturaTiro = tamanhoProjetil;

                if (offsetProjetil == 0f) {
                    alturaTiro = tamanhoProjetil * 0.6f;
                }

                float larguraTiro = alturaTiro * propProjetil;

                listaProjeteis.add(new Projetil(bocaX, bocaY, alvoEscolhido, imgProjetil, dano, larguraTiro, alturaTiro, offsetProjetil, velocidadeProjetil));

                tempoTiro = 0;
            }
        }

        // Retorna 0 de dinheiro, pois torre de ataque não faz dinheiro
        return 0;
    }

    public void trocarModoAlvo() {
        switch (modoAlvoAtual) {
            case PRIMEIRO: modoAlvoAtual = ModoAlvo.ULTIMO; break;
            case ULTIMO: modoAlvoAtual = ModoAlvo.MAIS_FORTE; break;
            case MAIS_FORTE: modoAlvoAtual = ModoAlvo.PRIMEIRO; break;
        }
    }

    public void desenhar(SpriteBatch batch) {
        batch.draw(textura, posicao.x, posicao.y, larguraDesenho, alturaDesenho,
            0, 0, textura.getWidth(), textura.getHeight(), viradaParaEsquerda, false);
    }
}
