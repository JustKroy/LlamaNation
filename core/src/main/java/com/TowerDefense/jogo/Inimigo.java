package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public abstract class Inimigo {

    // --- POSIÇÃO E NAVEGAÇÃO ---
    public Vector2 posicao;
    private Vector2 tempDirecao = new Vector2(); // Reutilizado para economizar memória
    private int pontoAtual = 1;

    // --- STATUS (Definidos pelos filhos, ex: CaramujoRapido) ---
    protected int vida;
    protected float velocidade;
    protected int recompensaMoedas;

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

            // Calcula a direção usando o vetor temporário
            tempDirecao.set(alvo.x - posicao.x, alvo.y - posicao.y);

            // Define a orientação visual
            if (tempDirecao.x < -0.1f) indoParaEsquerda = true;
            else if (tempDirecao.x > 0.1f) indoParaEsquerda = false;

            // Distância que ele vai percorrer NESTE frame
            float distanciaFrame = velocidade * delta;

            // Se a distância para o alvo for menor que o passo do frame, ele chegou no ponto
            if (tempDirecao.len() <= distanciaFrame) {
                posicao.set(alvo);
                pontoAtual++;
            } else {
                // Normaliza e move
                tempDirecao.nor().scl(distanciaFrame);
                posicao.add(tempDirecao);
            }
            return false;
        }

        return true; // Chegou ao fim do caminho
    }

    public void desenhar(SpriteBatch batch) {
        // Usa o frame correto baseado na direção
        TextureRegion frame = indoParaEsquerda ?
            animacaoVirada.getKeyFrame(tempoAnim, true) :
            animacaoNormal.getKeyFrame(tempoAnim, true);

        // Dica: Use constantes para o tamanho (50, 50) se todos forem iguais
        batch.draw(frame, posicao.x, posicao.y, 50, 50);
    }

    // Métodos úteis para o Gerenciador de Dano futuramente
    public void tomarDano(int quantidade) {
        this.vida -= quantidade;
    }

    public boolean estaMorto() {
        return vida <= 0;
    }
}
