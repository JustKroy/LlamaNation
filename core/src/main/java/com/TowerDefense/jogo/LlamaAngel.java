package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class LlamaAngel extends Torre {

    private TextureRegion[] framesAnimacao;
    private float tempoEstado;

    private Texture imgNuvemNasc, imgNuvemAtiva, imgNuvemSum, imgAtaque;
    private ProjetilAngel nuvemAtiva = null;
    private float raioExplosao;

    public LlamaAngel(float x, float y, Texture imgLlamaAngel, Texture imgNuvemNasc, Texture imgNuvemAtiva, Texture imgNuvemSum, Texture imgAtaque) {
        super(x, y, imgLlamaAngel, imgAtaque);

        this.imgNuvemNasc = imgNuvemNasc;
        this.imgNuvemAtiva = imgNuvemAtiva;
        this.imgNuvemSum = imgNuvemSum;
        this.imgAtaque = imgAtaque;

        this.dano = 100; // Dano total do raio
        this.raio = TipoLlama.ANGEL.raioInicial;
        this.cooldown = 3.0f; // Tempo entre tempestades

        // Reduzi o multiplicador de 0.8f para 0.6f para diminuir a área de explosão real do raio
        this.raioExplosao = (imgAtaque.getWidth() / 10f) * 0.6f;

        int larguraFrame = imgLlamaAngel.getWidth() / 5;
        int alturaFrame = imgLlamaAngel.getHeight();

        framesAnimacao = new TextureRegion[5];
        for (int i = 0; i < 5; i++) {
            framesAnimacao[i] = new TextureRegion(imgLlamaAngel, i * larguraFrame, 0, larguraFrame, alturaFrame);
        }

        this.larguraDesenho = 90f;
        this.alturaDesenho = 90f * ((float) alturaFrame / larguraFrame);
        this.hitbox.setSize(larguraDesenho, alturaDesenho);
    }

    @Override
    public int atualizar(float delta, Array<Inimigo> listaInimigos, Array<Projetil> listaProjeteis) {
        tempoTiro += delta;
        tempoEstado += delta;

        float centroX = posicao.x + (larguraDesenho / 2f);
        float centroY = posicao.y + (alturaDesenho / 2f);

        // Busca alvos no alcance
        Inimigo alvoEscolhido = selecionarAlvo(listaInimigos, centroX, centroY);

        if (alvoEscolhido != null) {
            viradaParaEsquerda = (alvoEscolhido.posicao.x < posicao.x);

            // Atualiza o foco da nuvem existente
            if (nuvemAtiva != null && nuvemAtiva.ativo) {
                nuvemAtiva.mudarAlvo(alvoEscolhido);
            }

            // Invoca nova nuvem
            if (tempoTiro >= cooldown && (nuvemAtiva == null || !nuvemAtiva.ativo)) {
                nuvemAtiva = new ProjetilAngel(alvoEscolhido, dano, imgNuvemNasc, imgNuvemAtiva, imgNuvemSum, imgAtaque, centroX, centroY, raio, raioExplosao);
                listaProjeteis.add(nuvemAtiva);
                tempoTiro = 0;
            }
        }
        return 0;
    }

    private Inimigo selecionarAlvo(Array<Inimigo> inimigos, float cx, float cy) {
        Inimigo melhorAlvo = null;
        for (Inimigo in : inimigos) {
            if (Vector2.dst(cx, cy, in.posicao.x + in.largura/2, in.posicao.y + in.altura/2) <= raio) {
                if (melhorAlvo == null || in.distanciaPercorrida > melhorAlvo.distanciaPercorrida) {
                    melhorAlvo = in;
                }
            }
        }
        return melhorAlvo;
    }

    @Override
    public void desenhar(SpriteBatch batch) {
        int frameAtual = (int) (tempoEstado / 0.15f) % 5;
        batch.draw(framesAnimacao[frameAtual], posicao.x, posicao.y, larguraDesenho, alturaDesenho);
    }

    public void destruirNuvem() {
        if (nuvemAtiva != null) { nuvemAtiva.ativo = false; nuvemAtiva = null; }
    }
}
