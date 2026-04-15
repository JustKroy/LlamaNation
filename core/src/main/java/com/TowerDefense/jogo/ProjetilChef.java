package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class ProjetilChef extends Projetil {

    private float tempoDeVida = 0.05f;
    private Array<Inimigo> listaInimigos;

    public ProjetilChef(float x, float y, Inimigo alvo, float dano, Texture imgFake, Array<Inimigo> inimigos) {
        super(x, y, alvo, imgFake, (int) dano, 0f, 10f, 10f, 5f);
        this.listaInimigos = inimigos;
        this.ativo = true;
    }

    @Override
    public void atualizar(float delta) {
        tempoDeVida -= delta;

        if (tempoDeVida <= 0 && ativo) {
            this.ativo = false;

            if (alvo != null && listaInimigos != null) {
                // 🔥 Usando o FOR tradicional para evitar bugs no LibGDX
                for (int i = 0; i < listaInimigos.size; i++) {
                    Inimigo inimigo = listaInimigos.get(i);

                    // Só machuca quem está vivo e próximo!
                    if (inimigo.vida > 0 && alvo.posicao.dst(inimigo.posicao) <= 80f) {
                        inimigo.vida -= dano;        // 10 da bala instantâneo
                        inimigo.timerBurn = 2.0f;    // Ativa fogo (50 dano por 0.5s)
                        inimigo.timerSlow = 2.0f;    // Ativa slow
                    }
                }
            }
        }
    }

    @Override
    public void desenhar(SpriteBatch batch) {}
}
