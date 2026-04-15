package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class ProjetilNeves extends Projetil {

    private float raioArea;
    private Array<Inimigo> listaInimigos;

    // Controladores do sopro
    private float tempoDeVida = 0.3f; // Tempo que o sopro fica visível na tela (0.3 segundos)
    private float tempoAtual = 0f;
    private boolean danoAplicado = false; // Garante que o dano seja causado só uma vez por sopro

    public ProjetilNeves(float x, float y, Inimigo alvo, Texture sheetAtaque, int dano, float raioArea, Array<Inimigo> listaInimigos) {
        // Passei 0f na velocidade (penúltimo float) porque ele não vai se mover!
        // E já passei 120f de largura e altura direto no super.
        super(x, y, alvo, sheetAtaque, dano, 120f, 120f, 0f, raioArea);

        this.raioArea = raioArea;
        this.listaInimigos = listaInimigos;

        // 🚨 DEIXANDO O SOPRO GIGANTE
        this.largura = 120f; // Aumente esses valores se quiser o sopro ainda maior
        this.altura = 120f;
        this.textura = new TextureRegion(sheetAtaque);

        // Centralizando o desenho do sopro na boca (ajuste fino no eixo Y)
        this.posicao.y -= this.altura / 2f;
    }

    @Override
    public void atualizar(float delta) {
        if (!ativo) return;

        this.tempoAtual += delta;

        // 💥 Aplica o dano em área no momento que o sopro aparece
        if (!danoAplicado) {
            causarDanoEmArea();
            this.danoAplicado = true;
        }

        // ⏱️ O sopro FICA PARADO na posição inicial.
        // Quando o cronômetro bate o tempo de vida, ele desaparece.
        if (tempoAtual >= tempoDeVida) {
            this.ativo = false;
        }
    }

    private void causarDanoEmArea() {
        // ❄️ Congela geral: Pega todo mundo que estiver no raio de alcance
        for (Inimigo in : listaInimigos) {
            if (in.vida > 0) {
                // Calcula a distância a partir da boca da lhama (posição do sopro)
                float dist = Vector2.dst(this.posicao.x, this.posicao.y, in.posicao.x, in.posicao.y);
                if (dist <= raioArea) {
                    in.vida -= this.dano; // Aplica o dano!
                }
            }
        }
    }

    @Override
    public void desenhar(SpriteBatch batch) {
        if (ativo && textura != null) {
            batch.draw(textura, posicao.x, posicao.y, largura, altura);
        }
    }
}
