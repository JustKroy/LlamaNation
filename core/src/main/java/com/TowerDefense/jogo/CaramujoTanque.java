package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CaramujoTanque extends Inimigo {

    public CaramujoTanque(float x, float y, Animation<TextureRegion> animacaoNormal, Animation<TextureRegion> animacaoVirada) {
        super(x, y, animacaoNormal, animacaoVirada);

        // --- STATUS DO TANQUE ---
        this.vida = 3000;
        this.velocidade = 40f;      // Aumentado (antes era 25f, agora é quase o dobro, mas ainda mais lento que um normal)
        this.recompensaMoedas = 40;

        // --- TAMANHO GIGANTE ---
        this.largura = 75f;         // 50% maior que o normal!
        this.altura = 75f;
    }
}
