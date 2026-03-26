package com.TowerDefense.jogo;

import com.badlogic.gdx.math.Rectangle;

public class OpcaoConfig {

    public String texto;
    public PopupConfig.TipoOpcao tipo;

    // Área clicável
    public Rectangle area;

    // TOGGLE
    public boolean estado;

    // SLIDER
    public float valor;
    public boolean arrastando;

    // DROPDOWN
    public String[] opcoes;
    public int selecionado;
    public boolean aberto;

    // KEYBIND
    public int tecla;
    public boolean esperandoTecla;

    // CONSTRUTOR
    public OpcaoConfig(String texto) {
        this.texto = texto;
        this.tipo = tipo;

        this.estado = false;
        this.valor = 0f;
        this.arrastando = false;

        this.aberto = false;
        this.selecionado = 0;

        this.tecla = -1;
        this.esperandoTecla = false;
    }
}
