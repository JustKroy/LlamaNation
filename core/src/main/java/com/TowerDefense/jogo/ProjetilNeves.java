package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class ProjetilNeves extends Projetil {

    private float raioArea;
    private Array<Inimigo> listaInimigos;

    private Animation<TextureRegion> animacaoAtaque;
    private float tempoEstado = 0f;
    private float tempoDeVida;
    private boolean danoAplicado = false;
    private float angulo;

    public ProjetilNeves(float x, float y, Inimigo alvo, Texture sheetAtaque, int dano, float raioArea, Array<Inimigo> listaInimigos) {
        // 📏 Tamanho reduzido para 90x90
        super(x, y, alvo, sheetAtaque, dano, 90f, 90f, 0f, raioArea);

        this.raioArea = raioArea;
        this.listaInimigos = listaInimigos;
        this.largura = 90f;
        this.altura = 90f;

        // Picotando os 18 frames
        int larguraFrame = sheetAtaque.getWidth() / 18;
        int alturaFrame = sheetAtaque.getHeight();
        TextureRegion[][] pedacos = TextureRegion.split(sheetAtaque, larguraFrame, alturaFrame);

        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < 18; i++) {
            frames.add(pedacos[0][i]);
        }

        this.animacaoAtaque = new Animation<>(0.03f, frames, Animation.PlayMode.NORMAL);
        this.tempoDeVida = this.animacaoAtaque.getAnimationDuration();

        // Centraliza o eixo Y do próprio tiro
        this.posicao.y -= this.altura / 2f;

        // 🎯 MIRA CENTRALIZADA
        if (alvo != null) {
            // Empurra a mira para o meio do caramujo (assumindo que ele tem aprox 50x50, o meio é 25)
            float centroAlvoX = alvo.posicao.x + 25f;
            float centroAlvoY = alvo.posicao.y + 25f;

            // O ponto de origem da rotação do nosso gelo fica no meio da altura dele
            float origemTiroY = this.posicao.y + (this.altura / 2f);

            Vector2 direcao = new Vector2(centroAlvoX - this.posicao.x, centroAlvoY - origemTiroY);
            this.angulo = direcao.angleDeg();
        }
    }

    @Override
    public void atualizar(float delta) {
        if (!ativo) return;

        this.tempoEstado += delta;

        // Aplica o dano e a lentidão na metade da animação
        if (!danoAplicado && tempoEstado >= tempoDeVida / 2f) {
            causarDanoEmArea();
            this.danoAplicado = true;
        }

        // Morre quando o sopro acaba
        if (animacaoAtaque.isAnimationFinished(tempoEstado)) {
            this.ativo = false;
        }
    }

    private void causarDanoEmArea() {
        if (listaInimigos != null) {
            for (int i = 0; i < listaInimigos.size; i++) {
                Inimigo inimigo = listaInimigos.get(i);
                if (inimigo.vida > 0) {
                    float dist = Vector2.dst(this.posicao.x, this.posicao.y, inimigo.posicao.x, inimigo.posicao.y);
                    if (dist <= raioArea) {
                        inimigo.vida -= this.dano;
                        inimigo.timerSlow = 2.0f;
                    }
                }
            }
        }
    }

    @Override
    public void desenhar(SpriteBatch batch) {
        if (ativo) {
            TextureRegion frameAtual = animacaoAtaque.getKeyFrame(tempoEstado);

            // 🔄 Rotação perfeita a partir da base do sopro
            batch.draw(frameAtual,
                posicao.x, posicao.y,
                0, altura / 2f,     // Origem da rotação (na base esquerda)
                largura, altura,
                1f, 1f,
                angulo
            );
        }
    } // <- Esta fecha o método desenhar
} // <- ESSA AQUI FECHA A CLASSE INTEIRA (É ela que costuma faltar!)
