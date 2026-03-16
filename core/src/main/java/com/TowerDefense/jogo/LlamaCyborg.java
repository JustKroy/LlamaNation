package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class LlamaCyborg extends Torre {

    private Animation<TextureRegion> animacaoAtirando;
    private float tempoEstado = 0f;
    private boolean jaAtirouNesteCiclo = false;
    private boolean estaAtacando = false;
    private Inimigo alvoFixo;

    public LlamaCyborg(float x, float y, Texture sheetCyborg, Texture imgAtaque) {
        super(x, y, sheetCyborg, imgAtaque);

        // Dano
        this.dano = 60;
        this.raio = 250f;
        // Cooldawn
        this.cooldown = 0.1f;
        this.velocidadeProjetil = 1000f;
        // Tiro
        this.tamanhoProjetil = 5f;
        this.offsetProjetil = 0f;

        this.alturaDesenho = 80f;
        float larguraDeUmFrameOriginal = (float) sheetCyborg.getWidth() / 18f;
        float proporcao = larguraDeUmFrameOriginal / (float) sheetCyborg.getHeight();

        // Define a largura baseada na proporção para não esticar
        this.larguraDesenho = this.alturaDesenho * proporcao;

        // Atualiza a hitbox para o tamanho final do desenho
        this.hitbox.set(x, y, larguraDesenho, alturaDesenho);

        // Recorte dos 18 frames
        TextureRegion[][] tmp = TextureRegion.split(sheetCyborg, (int)larguraDeUmFrameOriginal, sheetCyborg.getHeight());
        TextureRegion[] frames = new TextureRegion[18];
        for (int i = 0; i < 18; i++) {
            frames[i] = tmp[0][i];
        }

        // 3. Animação acelerada (0.03f em vez de 0.06f)
        animacaoAtirando = new Animation<>(0.03f, frames);
    }

    @Override
    public void atualizar(float delta, Array<Inimigo> listaInimigos, Array<Projetil> listaProjeteis) {
        if (!estaAtacando) tempoTiro += delta;

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

            if (frameAtual == 10 && !jaAtirouNesteCiclo) {

                // --- AJUSTE FINO DO SPAWN DO TIRO ---
                // Aumente este valor para trazer o tiro mais para perto do centro da lhama
                float recuoHorizontal = 30f;

                // Mude este valor (0 a 1) para subir ou descer o tiro.
                // 0.5 é no meio da barriga. 0.7 costuma ser na altura da cabeça/olho.
                float alturaDoTiro = 0.70f;

                float saidaX;
                if (viradaParaEsquerda) {
                    // Se olha pra esquerda, nasce do lado esquerdo, mas puxamos pra direita (pra dentro)
                    saidaX = posicao.x + recuoHorizontal;
                } else {
                    // Se olha pra direita, nasce do lado direito, mas puxamos pra esquerda (pra dentro)
                    saidaX = (posicao.x + larguraDesenho) - recuoHorizontal;
                }

                float saidaY = posicao.y + (alturaDesenho * alturaDoTiro);

                float propProjetil = (float) imgProjetil.getWidth() / imgProjetil.getHeight();
                float larguraTiro = tamanhoProjetil * propProjetil;

                // Criar o projetil
                listaProjeteis.add(new Projetil(saidaX, saidaY, alvoFixo, imgProjetil, dano,
                    larguraTiro, tamanhoProjetil, offsetProjetil, velocidadeProjetil));

                jaAtirouNesteCiclo = true;
            }

            if (animacaoAtirando.isAnimationFinished(tempoEstado)) {
                estaAtacando = false;
            }
        }
    }

    @Override
    public void desenhar(SpriteBatch batch) {
        TextureRegion frame = estaAtacando ?
            animacaoAtirando.getKeyFrame(tempoEstado) :
            animacaoAtirando.getKeyFrames()[0];

        // Substituímos os vários 'batch.draw' por um único que espelha a imagem automaticamente
        batch.draw(frame.getTexture(),
            posicao.x, posicao.y,
            larguraDesenho, alturaDesenho,
            frame.getRegionX(), frame.getRegionY(),
            frame.getRegionWidth(), frame.getRegionHeight(),
            viradaParaEsquerda, false); // O true/false aqui que faz a mágica de girar!
    }
}
