package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Texture;

public class LhamaNormal extends Torre { // "extends" significa que ela herda de Torre

    public LhamaNormal(float x, float y, Texture textura, Texture imgProjetil) {
        super(x, y, textura, imgProjetil); // Passa a posição e as imagens pro molde pai

        // Aqui definimos os status únicos da Lhama Normal!
        this.raio = 200f;
        this.cooldown = 1.0f;
        this.dano = 50;
        this.tamanhoProjetil = 250f;
        this.offsetProjetil = 0f;
    }
}
