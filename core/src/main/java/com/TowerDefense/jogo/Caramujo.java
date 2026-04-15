package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Caramujo extends Inimigo {

    public Caramujo(float x, float y, Animation<TextureRegion> animNormal, Animation<TextureRegion> animVirada) {
        super(x, y, animNormal, animVirada);

        // Status únicos do Caramujo!
        this.vida = 100;
        this.velocidade = 100f;
        this.recompensaMoedas = 20;
    }
}
