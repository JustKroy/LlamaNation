package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public abstract class Inimigo {

    // --- POSIÇÃO E NAVEGAÇÃO ---
    public Vector2 posicao;
    private Vector2 tempDirecao = new Vector2();

    public int pontoAtual = 1;

    // --- NOVA VARIÁVEL: O SEGREDO DA MIRA PERFEITA ---
    // Guarda a distância exata em pixels que este inimigo já andou no mapa
    public float distanciaPercorrida = 0;

    // --- STATUS ---
    public int vida;
    protected float velocidade;
    public int recompensaMoedas;

    // --- TAMANHO VISUAL ---
    public float largura = 50f;
    public float altura = 50f;

    // --- ANIMAÇÃO ---
    protected Animation<TextureRegion> animacaoNormal;
    protected Animation<TextureRegion> animacaoVirada;
    private float tempoAnim = 0;
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

            tempDirecao.set(alvo.x - posicao.x, alvo.y - posicao.y);

            if (tempDirecao.x < -0.1f) indoParaEsquerda = true;
            else if (tempDirecao.x > 0.1f) indoParaEsquerda = false;

            float distanciaFrame = velocidade * delta;
            float distanciaAteAlvo = tempDirecao.len(); // Mede exatamente quanto falta pro ponto

            if (distanciaAteAlvo <= distanciaFrame) {
                posicao.set(alvo);
                pontoAtual++;

                // Soma exatamente o pedacinho que ele andou pra chegar na curva
                distanciaPercorrida += distanciaAteAlvo;
            } else {
                tempDirecao.nor().scl(distanciaFrame);
                posicao.add(tempDirecao);

                // Soma o passo normal que ele deu na reta
                distanciaPercorrida += distanciaFrame;
            }
            return false;
        }

        return true;
    }

    public void desenhar(SpriteBatch batch) {
        TextureRegion frame = indoParaEsquerda ?
            animacaoVirada.getKeyFrame(tempoAnim, true) :
            animacaoNormal.getKeyFrame(tempoAnim, true);

        batch.draw(frame, posicao.x, posicao.y, largura, altura);
    }
}
