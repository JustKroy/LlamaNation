package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class LlamaNeves extends Torre {

    private Animation<TextureRegion> animacaoLhama;
    private float tempoEstado;
    private float tempoTiro = 0f; // ⏱️ Cronômetro para o tempo de recarga
    private boolean olhandoEsquerda = false;
    private Texture sheetAtaque;
    private ProjetilNeves soproAtual = null;

    public LlamaNeves(float x, float y, Texture sheetLhama, Texture sheetAtaque) {
        super(x, y);
        this.posicao = new Vector2(x, y);
        this.tipoLlama = TipoLlama.NEVES;
        this.sheetAtaque = sheetAtaque;

        this.dano = 15;
        this.raio = TipoLlama.NEVES.raioInicial;

        // ⏱️ TEMPO DE RECARGA: 1.5 segundos entre cada sopro (ajuste se quiser mais rápido/lento)
        this.cooldown = 1.5f;

        // ✂️ Corte da Lhama
        int larguraFrame = sheetLhama.getWidth() / 12;
        int alturaFrame = sheetLhama.getHeight();
        TextureRegion[][] pedacosLhama = TextureRegion.split(sheetLhama, larguraFrame, alturaFrame);

        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < 12; i++) {
            frames.add(pedacosLhama[0][i]);
        }

        // 🏃 ANIMAÇÃO SINCRONIZADA com o tiro
        this.animacaoLhama = new Animation<>(0.045f, frames, Animation.PlayMode.LOOP);
        this.tempoEstado = 0f;

        // 📐 Ajuste de tamanho
        this.larguraDesenho = 55f;
        float proporcao = (float) alturaFrame / larguraFrame;
        this.alturaDesenho = this.larguraDesenho * proporcao;
        this.hitbox = new Rectangle(x, y, larguraDesenho, alturaDesenho);
    }

    @Override
    public int atualizar(float delta, Array<Inimigo> inimigos, Array<Projetil> listaProjeteis) {
        this.tempoTiro += delta; // Faz o relógio rodar

        Inimigo alvoAtual = null;

        // Procura inimigo VIVO dentro do raio
        for (Inimigo in : inimigos) {
            if (in.vida > 0) {
                float centroX = posicao.x + (larguraDesenho / 2f);
                float centroY = posicao.y + (alturaDesenho / 2f);
                float dist = Vector2.dst(centroX, centroY, in.posicao.x + 25, in.posicao.y + 25);

                if (dist <= raio) {
                    alvoAtual = in;
                    break;
                }
            }
        }

        if (alvoAtual != null) {
            olhandoEsquerda = (alvoAtual.posicao.x < this.posicao.x);

            // Só atira se o tiro anterior acabou E se o cronômetro atingiu o cooldown
            if ((soproAtual == null || !soproAtual.ativo) && tempoTiro >= cooldown) {

                tempoTiro = 0f; // Zera o recarregamento
                tempoEstado = 0f; // Volta a animação pro frame zero

                float bocaY = posicao.y + (alturaDesenho * 1.2f);
                float bocaX;

                if (olhandoEsquerda) {
                    bocaX = posicao.x + 55f;
                } else {
                    bocaX = posicao.x + 95f;
                }

                // Cria o tiro e salva na variável
                soproAtual = new ProjetilNeves(bocaX, bocaY, alvoAtual, sheetAtaque, dano, raio, inimigos);
                listaProjeteis.add(soproAtual);
            }

            // A lhama SÓ anima se estiver soltando o gelo ou voltando para a pose original
            if ((soproAtual != null && soproAtual.ativo) || tempoEstado > 0) {
                tempoEstado += delta;

                // Se completou a volta e não está atirando, trava no frame 0
                if (animacaoLhama.getKeyFrameIndex(tempoEstado) == 0 && (soproAtual == null || !soproAtual.ativo)) {
                    tempoEstado = 0f;
                }
            }

        } else {
            // Se o inimigo morrer/sumir, finaliza a animação de forma limpa e para
            if (tempoEstado > 0) {
                tempoEstado += delta;
                if (animacaoLhama.getKeyFrameIndex(tempoEstado) == 0) {
                    tempoEstado = 0f;
                }
            }
        }

        return 0;
    }

    @Override
    public void desenhar(SpriteBatch batch) {
        TextureRegion frameAtual = animacaoLhama.getKeyFrame(tempoEstado);

        if (olhandoEsquerda && !frameAtual.isFlipX()) {
            frameAtual.flip(true, false);
        } else if (!olhandoEsquerda && frameAtual.isFlipX()) {
            frameAtual.flip(true, false);
        }

        batch.draw(frameAtual, posicao.x, posicao.y, larguraDesenho, alturaDesenho);
    }
}
