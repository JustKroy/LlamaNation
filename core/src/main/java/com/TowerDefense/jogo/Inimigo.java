package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Inimigo {
    public Vector2 posicao;
    public int vida = 100;

    private Animation<TextureRegion> animacao;
    private float tempoAnim = 0;
    private int pontoAtual = 1;
    private float velocidade = 100f;

    public Inimigo(float x, float y, Animation<TextureRegion> animacao) {
        this.posicao = new Vector2(x, y);
        this.animacao = animacao;
    }

    public boolean atualizar(float delta, Array<Vector2> caminho) {
        tempoAnim += delta;

        if (pontoAtual < caminho.size) {
            Vector2 alvo = caminho.get(pontoAtual);
            Vector2 direcao = new Vector2(alvo.x - posicao.x, alvo.y - posicao.y);

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
        TextureRegion frame = animacao.getKeyFrame(tempoAnim, true);
        batch.draw(frame, posicao.x, posicao.y, 50, 50);
    }
}
