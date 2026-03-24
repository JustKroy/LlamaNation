package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class LhamaMage extends Torre {

    private Animation<TextureRegion> animacaoAtirando;
    private float tempoEstado = 0f;
    private boolean jaAtirouNesteCiclo = false;
    private boolean estaAtacando = false;
    private Inimigo alvoFixo;

    public LhamaMage(float x, float y, Texture sheetMago, Texture imgMagia) {
        super(x, y, sheetMago, imgMagia);

        this.dano = 150;
        this.raio = 300f; // Diâmetro visual na loja deve ser 600!
        this.cooldown = 2.5f;

        this.tamanhoProjetil = 20f;
        this.offsetProjetil = 0f;

        // Ajuste de escala (1 frame por vez)
        this.larguraDesenho = this.larguraDesenho / 11f;
        this.hitbox.width = this.larguraDesenho;

        int quantidadeDeFrames = 11;
        float larguraDeUmFrame = (float) sheetMago.getWidth() / quantidadeDeFrames;

        TextureRegion[][] tmp = TextureRegion.split(sheetMago, (int)larguraDeUmFrame, sheetMago.getHeight());
        TextureRegion[] frames = new TextureRegion[quantidadeDeFrames];
        for (int i = 0; i < quantidadeDeFrames; i++) {
            frames[i] = tmp[0][i];
        }

        animacaoAtirando = new Animation<>(0.1f, frames);
    }

    @Override
    public int atualizar(float delta, Array<Inimigo> listaInimigos, Array<Projetil> listaProjeteis) {
        if (!estaAtacando) tempoTiro += delta;

        // Centro real da torre para o Range não mentir
        float centroTorreX = posicao.x + (larguraDesenho / 2f);
        float centroTorreY = posicao.y + (alturaDesenho / 2f);

        // --- INÍCIO DO CÉREBRO DE MIRA INTELIGENTE ---

        // 1. Coleta quem está no raio
        Array<Inimigo> inimigosNoRange = new Array<>();
        for (Inimigo in : listaInimigos) {
            float centroInimigoX = in.posicao.x + (in.largura / 2f);
            float centroInimigoY = in.posicao.y + (in.altura / 2f);
            if (Vector2.dst(centroTorreX, centroTorreY, centroInimigoX, centroInimigoY) <= raio) {
                inimigosNoRange.add(in);
            }
        }

        // 2. Filtra pelo botão (Primeiro, Último, Mais Forte)
        Inimigo alvoAtual = null;

        if (inimigosNoRange.size > 0) {
            alvoAtual = inimigosNoRange.get(0);

            for (int i = 1; i < inimigosNoRange.size; i++) {
                Inimigo in = inimigosNoRange.get(i);

                if (modoAlvoAtual == ModoAlvo.PRIMEIRO) {
                    if (in.distanciaPercorrida > alvoAtual.distanciaPercorrida) {
                        alvoAtual = in;
                    }
                }
                else if (modoAlvoAtual == ModoAlvo.ULTIMO) {
                    if (in.distanciaPercorrida < alvoAtual.distanciaPercorrida) {
                        alvoAtual = in;
                    }
                }
                else if (modoAlvoAtual == ModoAlvo.MAIS_FORTE) {
                    if (in.vida > alvoAtual.vida) {
                        alvoAtual = in;
                    }
                }
            }
        }

        if (alvoAtual != null) {
            viradaParaEsquerda = (alvoAtual.posicao.x < posicao.x);
        }

        // --- FIM DO CÉREBRO DE MIRA ---

        if (alvoAtual != null && tempoTiro >= cooldown && !estaAtacando) {
            estaAtacando = true;
            tempoTiro = 0;
            tempoEstado = 0f;
            jaAtirouNesteCiclo = false;
            alvoFixo = alvoAtual;
        }

        if (estaAtacando) {
            tempoEstado += delta;
            int frameAtual = animacaoAtirando.getKeyFrameIndex(tempoEstado);

            if (frameAtual == 6 && !jaAtirouNesteCiclo) {
                // --- AJUSTE DO CAJADO (Saída do outro lado da cabeça) ---
                float deslocamentoLateral = -10f;
                float saidaX;

                if (viradaParaEsquerda) {
                    // Se olha para esquerda, o cajado está do lado DIREITO (atravessa)
                    saidaX = posicao.x + larguraDesenho + deslocamentoLateral;
                } else {
                    // Se olha para direita, o cajado está do lado ESQUERDO
                    saidaX = posicao.x - deslocamentoLateral;
                }

                float saidaY = posicao.y + (alturaDesenho * 0.6f);

                float propProjetil = (float) imgProjetil.getWidth() / imgProjetil.getHeight();
                float larguraTiro = tamanhoProjetil * propProjetil;

                // --- VELOCIDADE ÚNICA AQUI (500f) ---
                Projetil magia = new Projetil(saidaX, saidaY, alvoFixo, imgProjetil, dano, larguraTiro, tamanhoProjetil, offsetProjetil, 500f);

                listaProjeteis.add(magia);
                jaAtirouNesteCiclo = true;
            }

            if (animacaoAtirando.isAnimationFinished(tempoEstado)) estaAtacando = false;
        }
        return 0;
    }

    @Override
    public void desenhar(SpriteBatch batch) {
        TextureRegion frame = estaAtacando ? animacaoAtirando.getKeyFrame(tempoEstado) : animacaoAtirando.getKeyFrames()[0];
        batch.draw(frame.getTexture(), posicao.x, posicao.y, larguraDesenho, alturaDesenho,
            frame.getRegionX(), frame.getRegionY(), frame.getRegionWidth(), frame.getRegionHeight(),
            viradaParaEsquerda, false);
    }
}
