package com.TowerDefense.jogo;

import com.badlogic.gdx.math.Rectangle;

public class OpcaoConfig {
    String texto;
    boolean estado;
    Rectangle area;

    public OpcaoConfig(String texto) {
        this.texto = texto;
        this.estado = false;
    }
}
