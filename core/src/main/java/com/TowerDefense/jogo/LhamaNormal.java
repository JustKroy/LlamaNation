package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class LhamaNormal extends Torre {

    private Animation<TextureRegion> animacaoAtirando;
    private float tempoEstado = 0f;
    private boolean jaAtirouNesteCiclo = false;
    private boolean estaAtacando = false;
    private Inimigo alvoFixo;

    public LhamaNormal(float x, float y, Texture sheetLlama, Texture imgAtaque) {
        super(x, y, sheetLlama, imgAtaque);

        this.dano = 50;

        // 🔥 Puxando o raio direto do Enum 🔥
        this.raio = TipoLlama.NORMAL.raioInicial;

        this.cooldown = 1.0f;
        this.velocidadeProjetil = 800f;

        this.tamanhoProjetil = 10f;
        this.offsetProjetil = 0f;

        // Força a altura padrão (80px)
        this.alturaDesenho = 80f;

        // --- 9 FRAMES ---
        float larguraDeUmFrameOriginal = (float) sheetLlama.getWidth() / 9f;
        float proporcao = larguraDeUmFrameOriginal / (float) sheetLlama.getHeight();

        this.larguraDesenho = this.alturaDesenho * proporcao;
        this.hitbox.set(x, y, larguraDesenho, alturaDesenho);

        // Recorte dos 9 frames
        TextureRegion[][] tmp = TextureRegion.split(sheetLlama, (int)larguraDeUmFrameOriginal, sheetLlama.getHeight());

        // Array com tamanho 9 e o For indo até 9!
        TextureRegion[] frames = new TextureRegion[9];
        for (int i = 0; i < 9; i++) {
            frames[i] = tmp[0][i];
        }

        // Velocidade da animação (0.08f = tempo que cada frame fica na tela)
        animacaoAtirando = new Animation<>(0.08f, frames);
    }

    @Override
    public int atualizar(float delta, Array<Inimigo> listaInimigos, Array<Projetil> listaProjeteis) {
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

        // O resto continua igualzinho: cuida da animação e do cuspe!
        if (alvoAtual != null && tempoTiro >= cooldown && !estaAtacando) {
            estaAtacando = true;
            tempoTiro = 0;
            tempoEstado = 0f;
            jaAtirouNesteCiclo = false;
            alvoFixo = alvoAtual; // Trava a mira nele enquanto a animação roda
        }

        if (estaAtacando) {
            tempoEstado += delta;
            int frameAtual = animacaoAtirando.getKeyFrameIndex(tempoEstado);

            // --- ATIRA NO FRAME 6 (índice 5) ---
            if (frameAtual == 5 && !jaAtirouNesteCiclo) {

                float recuoHorizontal = 10f; // Ajuste pra puxar o cuspe pra boca
                float alturaDoTiro = 0.65f; // Ajuste a altura pra sair da cabeça

                float saidaX;
                if (viradaParaEsquerda) {
                    saidaX = posicao.x + recuoHorizontal;
                } else {
                    saidaX = (posicao.x + larguraDesenho) - recuoHorizontal;
                }

                float saidaY = posicao.y + (alturaDesenho * alturaDoTiro);

                float propProjetil = (float) imgProjetil.getWidth() / imgProjetil.getHeight();
                float larguraTiro = tamanhoProjetil * propProjetil;

                listaProjeteis.add(new Projetil(saidaX, saidaY, alvoFixo, imgProjetil, dano,
                    larguraTiro, tamanhoProjetil, offsetProjetil, velocidadeProjetil));

                jaAtirouNesteCiclo = true;
            }

            if (animacaoAtirando.isAnimationFinished(tempoEstado)) {
                estaAtacando = false;
            }
        }

        // Retorna 0 de dinheiro, pois a Lhama Normal só distribui porrada, não dinheiro!
        return 0;
    }

    @Override
    public void desenhar(SpriteBatch batch) {
        TextureRegion frame = estaAtacando ?
            animacaoAtirando.getKeyFrame(tempoEstado) :
            animacaoAtirando.getKeyFrames()[0];

        batch.draw(frame.getTexture(),
            posicao.x, posicao.y,
            larguraDesenho, alturaDesenho,
            frame.getRegionX(), frame.getRegionY(),
            frame.getRegionWidth(), frame.getRegionHeight(),
            viradaParaEsquerda, false);
    }
}
