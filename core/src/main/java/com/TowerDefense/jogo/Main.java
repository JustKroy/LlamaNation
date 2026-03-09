package com.TowerDefense.jogo;

import com.badlogic.gdx.Game;

public class Main extends Game {
    @Override
    public void create() {
        // Define a tela inicial como o Menu
        this.setScreen(new MenuScreen(this));
    }

    @Override
    public void render() {
        // Chama o render da tela ativa (Menu ou Jogo)
        super.render();
    }
}
