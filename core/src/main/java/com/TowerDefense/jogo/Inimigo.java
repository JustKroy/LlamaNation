package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public abstract class Inimigo {
    public Vector2 posicao;

    // As filhas vão preencher isso:
    public int vida;
    public float velocidade;
    public int recompensaMoedas;

    protected Animation<TextureRegion> animacaoNormal;
    protected Animation<TextureRegion> animacaoVirada;
    private float tempoAnim = 0;
    private int pontoAtual = 1;
    private boolean indoParaEsquerda = false;

    public Inimigo(float x, float y, Animation<TextureRegion> animacaoNormal, Animation<TextureRegion> animacaoVirada) {
        this.posicao = new Vector2(x, y);
        this.animacaoNormal = animacaoNormal;
        this.animacaoVirada = animacaoVirada;
    }

    public boolean atualizar(float delta, Array<Vector2> caminho) {
        tempoAnim += delta;

        if (pontoAtual < caminho.size) {
            Vector2 alvo = caminho.get(pontoAtual);
            Vector2 direcao = new Vector2(alvo.x - posicao.x, alvo.y - posicao.y);

            if (direcao.x < -0.5f) indoParaEsquerda = true;
            else if (direcao.x > 0.5f) indoParaEsquerda = false;

            if (direcao.len() <= velocidade * delta) {
                posicao.set(alvo);
                pontoAtual++;
            } else {
                direcao.nor().scl(velocidade * delta);
                posicao.add(direcao);
            }
            return false;
        }
        return true;
    }

    public void desenhar(SpriteBatch batch) {
        TextureRegion frame = indoParaEsquerda ? animacaoVirada.getKeyFrame(tempoAnim, true) : animacaoNormal.getKeyFrame(tempoAnim, true);
        batch.draw(frame, posicao.x, posicao.y, 50, 50);
    }
}
