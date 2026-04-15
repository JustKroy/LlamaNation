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
    private boolean olhandoEsquerda = false;
    private Texture sheetAtaque;

    public LlamaNeves(float x, float y, Texture sheetLhama, Texture sheetAtaque) {
        super(x, y);
        this.posicao = new Vector2(x, y);
        this.tipoLlama = TipoLlama.NEVES;
        this.sheetAtaque = sheetAtaque; // Guardamos para passar para o ProjetilNeves

        this.dano = 15;
        this.raio = 150f;
        this.cooldown = 1.0f;

        // ✂️ Corte da Lhama
        int larguraFrame = sheetLhama.getWidth() / 12;
        int alturaFrame = sheetLhama.getHeight();
        TextureRegion[][] pedacosLhama = TextureRegion.split(sheetLhama, larguraFrame, alturaFrame);

        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < 12; i++) {
            frames.add(pedacosLhama[0][i]);
        }
        this.animacaoLhama = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);
        this.tempoEstado = 0f;

        // 📐 Ajuste de tamanho
        this.larguraDesenho = 55f;
        float proporcao = (float) alturaFrame / larguraFrame;
        this.alturaDesenho = this.larguraDesenho * proporcao;
        this.hitbox = new Rectangle(x, y, larguraDesenho, alturaDesenho);
    }

    @Override
    public int atualizar(float delta, Array<Inimigo> inimigos, Array<Projetil> listaProjeteis) {
        this.tempoTiro += delta;
        Inimigo alvoAtual = null;

        // Procura inimigo VIVO dentro do raio
        for (Inimigo in : inimigos) {
            if (in.vida > 0) {
                // Usa o centro da Lhama para calcular a distância
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
            tempoEstado += delta;
            // Define a direção que a lhama está olhando
            olhandoEsquerda = (alvoAtual.posicao.x < this.posicao.x);

            // Atira!
            if (tempoTiro >= cooldown) {
                tempoTiro = 0;

                // Ajusta de onde o tiro sai dependendo de onde ela tá olhando
                float bocaX = olhandoEsquerda ? posicao.x + 10 : posicao.x + larguraDesenho - 10;
                float bocaY = posicao.y + (alturaDesenho * 0.6f);

                // 🔥 DISPARA O SOPRO DE GELO EM ÁREA
                listaProjeteis.add(new ProjetilNeves(bocaX, bocaY, alvoAtual, sheetAtaque, dano, raio, inimigos));
            }
        } else {
            tempoEstado = 0;
        }

        return 0;
    }
    @Override
    public void desenhar(SpriteBatch batch) {
        // Pega o "quadro" (frame) certo da animação com base no tempo que passou
        TextureRegion frameAtual = animacaoLhama.getKeyFrame(tempoEstado);

        // Lógica para virar a imagem (flip) dependendo de para onde a Lhama tá olhando
        if (olhandoEsquerda && !frameAtual.isFlipX()) {
            frameAtual.flip(true, false); // Vira pra esquerda
        } else if (!olhandoEsquerda && frameAtual.isFlipX()) {
            frameAtual.flip(true, false); // Desvira (volta pra direita)
        }

        // Desenha a nossa lhama na tela com a largura e altura certas!
        batch.draw(frameAtual, posicao.x, posicao.y, larguraDesenho, alturaDesenho);
    }
}
