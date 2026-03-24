package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Color;
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
    public float distanciaPercorrida = 0;

    // --- STATUS ---
    public int vida;
    protected float velocidade;
    public int recompensaMoedas;

    // 🔥 STATUS DA METRALHADORA (NOVO) 🔥
    public float timerBurn = 0;
    public float timerSlow = 0;
    private float relogioDano = 0;

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

        // 🔥 1. APLICA O SLOW 🔥
        float vFinal = velocidade;
        if (timerSlow > 0) {
            vFinal = velocidade * 0.5f; // Metade da velocidade
            timerSlow -= delta;
        }

        // 🔥 2. APLICA O BURN (50 dano a cada 0.5s) 🔥
        if (timerBurn > 0) {
            timerBurn -= delta;
            relogioDano += delta;
            if (relogioDano >= 1f) {
                this.vida -= 25;
                relogioDano = 0;
            }
        } else {
            relogioDano = 0;
        }

        // --- MOVIMENTAÇÃO ---
        if (pontoAtual < caminho.size) {
            Vector2 alvo = caminho.get(pontoAtual);
            tempDirecao.set(alvo.x - posicao.x, alvo.y - posicao.y);

            if (tempDirecao.x < -0.1f) indoParaEsquerda = true;
            else if (tempDirecao.x > 0.1f) indoParaEsquerda = false;

            // Usamos a vFinal que pode estar com Slow
            float distanciaFrame = vFinal * delta;
            float distanciaAteAlvo = tempDirecao.len();

            if (distanciaAteAlvo <= distanciaFrame) {
                posicao.set(alvo);
                pontoAtual++;
                distanciaPercorrida += distanciaAteAlvo;
            } else {
                tempDirecao.nor().scl(distanciaFrame);
                posicao.add(tempDirecao);
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

        // 🔥 PISCA VERMELHO SE ESTIVER QUEIMANDO 🔥
        if (timerBurn > 0 && (int)(timerBurn * 15) % 2 == 0) {
            batch.setColor(Color.RED);
        }

        batch.draw(frame, posicao.x, posicao.y, largura, altura);

        // Retorna a cor ao normal
        batch.setColor(Color.WHITE);
    }
}
