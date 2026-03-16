package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Texture;

public class LhamaNinja extends Torre {

    public LhamaNinja(float x, float y, Texture textura, Texture imgProjetil) {
        super(x, y, textura, imgProjetil);


        this.raio = 250f;
        this.cooldown = 0.5f;
        this.dano = 75;
        this.tamanhoProjetil = 30f;
        this.offsetProjetil = -45f;
    }
}
