package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class LlamaChef extends Torre {
    private Texture imgArmaChef;
    private TextureRegion[] framesLlama;
    private TextureRegion[] framesArma;

    private TextureRegion frameAtual;

    private float tempoAnimacao = 0;
    private float tempoCooldown = 0f;
    private int frameAtualArma = 0;
    private int frameAtualLlama = 0;

    private boolean viradoParaEsquerda = false;
    private float anguloVertical = 0f;

    public LlamaChef(float x, float y, Texture imgLlama, Texture imgArma) {
        super(x, y);

        this.raio = TipoLlama.CHEF.raioInicial;
        this.dano = 10;
        this.cooldown = 0.05f;

        this.imgArmaChef = imgArma;

        framesLlama = new TextureRegion[5];
        int largLlama = imgLlama.getWidth() / 5;
        for (int i = 0; i < 5; i++) {
            framesLlama[i] = new TextureRegion(imgLlama, i * largLlama, 0, largLlama, imgLlama.getHeight());
        }

        framesArma = new TextureRegion[11];
        int largArma = imgArma.getWidth() / 11;
        for (int i = 0; i < 11; i++) {
            framesArma[i] = new TextureRegion(imgArma, i * largArma, 0, largArma, imgArma.getHeight());
        }

        this.frameAtual = framesLlama[0];
        this.larguraDesenho = 80f;
        this.alturaDesenho = 80f * ((float) imgLlama.getHeight() / largLlama);

        this.hitbox = new Rectangle(x, y, larguraDesenho, alturaDesenho);
    }

    private Inimigo encontrarAlvo(Array<Inimigo> inimigos) {
        float meuCentroX = posicao.x + (larguraDesenho / 2f);
        float meuCentroY = posicao.y + (alturaDesenho / 2f);

        for (Inimigo inimigo : inimigos) {
            // 🔥 A MÁGICA ESTÁ AQUI: Ignora inimigos mortos!
            if (inimigo.vida <= 0) continue;

            float dist = (float) Math.sqrt(Math.pow(inimigo.posicao.x - meuCentroX, 2) + Math.pow(inimigo.posicao.y - meuCentroY, 2));
            if (dist <= raio) {
                return inimigo;
            }
        }
        return null;
    }

    @Override
    public int atualizar(float delta, Array<Inimigo> inimigos, Array<Projetil> listaProjeteis) {
        tempoCooldown += delta;

        Inimigo alvoAtual = encontrarAlvo(inimigos);

        if (alvoAtual != null) {
            float meuCentroX = posicao.x + (larguraDesenho / 2f);
            float meuCentroY = posicao.y + (alturaDesenho / 2f);

            viradoParaEsquerda = alvoAtual.posicao.x < meuCentroX;

            float rad = MathUtils.atan2(alvoAtual.posicao.y - meuCentroY, alvoAtual.posicao.x - meuCentroX);
            anguloVertical = MathUtils.radiansToDegrees * rad;

            if (viradoParaEsquerda) {
                if (anguloVertical > 0) anguloVertical = 180 - anguloVertical;
                else anguloVertical = -180 - anguloVertical;
            }

            tempoAnimacao += delta;
            if (tempoAnimacao >= 0.015f) {
                tempoAnimacao = 0;
                frameAtualArma++;
                if (frameAtualArma >= 11) frameAtualArma = 0;

                frameAtualLlama = (frameAtualArma / 3) % 5;
                this.frameAtual = framesLlama[frameAtualLlama];
            }

            if (tempoCooldown >= cooldown) {
                tempoCooldown = 0;
                listaProjeteis.add(new ProjetilChef(posicao.x, posicao.y, alvoAtual, dano, imgArmaChef, inimigos));
            }
        } else {
            frameAtualArma = 0;
            frameAtualLlama = 0;
            this.frameAtual = framesLlama[0];
            anguloVertical = 0f;
        }
        return 0;
    }

    @Override
    public void desenhar(SpriteBatch batch) {
        float larguraOriginal = framesArma[0].getRegionWidth();
        float alturaOriginal = framesArma[0].getRegionHeight();

        float larguraArma = 180f;
        float alturaArma = larguraArma * (alturaOriginal / larguraOriginal);

        float offsetX = 10f;
        float offsetY = -5f;

        float maoX = posicao.x + (larguraDesenho / 2f) + (viradoParaEsquerda ? -offsetX : offsetX);
        float maoY = posicao.y + (alturaDesenho / 2f) + offsetY;

        float caboX = larguraArma * 0.2f;
        float caboY = alturaArma * 0.5f;

        float drawXArma, drawYArma, batchOriginX, drawWidthArma;

        if (!viradoParaEsquerda) {
            drawXArma = maoX - caboX;
            drawWidthArma = larguraArma;
            batchOriginX = caboX;
        } else {
            drawXArma = maoX + caboX;
            drawWidthArma = -larguraArma;
            batchOriginX = -caboX;
        }

        drawYArma = maoY - caboY;
        float anguloFinal = viradoParaEsquerda ? -anguloVertical : anguloVertical;

        batch.draw(framesArma[frameAtualArma],
            drawXArma, drawYArma,
            batchOriginX, caboY,
            drawWidthArma, alturaArma,
            1f, 1f,
            anguloFinal);

        if (frameAtual != null) {
            float drawX = viradoParaEsquerda ? posicao.x + larguraDesenho : posicao.x;
            float drawWidth = viradoParaEsquerda ? -larguraDesenho : larguraDesenho;

            batch.draw(frameAtual, drawX, posicao.y, drawWidth, alturaDesenho);
        }
    }
}
