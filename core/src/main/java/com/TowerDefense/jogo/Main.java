package com.TowerDefense.jogo;

import com.badlogic.gdx.Game;

public class Main extends Game {
    @Override
    public void create() {

        // ESSA LINHA É OBRIGATÓRIA:
        ConfigManager.carregar();

        // Se você quiser que o jogo já abra na resolução salva:
        ConfigManager.aplicarVideo();

        // Define a tela inicial como o Menu
        this.setScreen(new MenuScreen(this));
    }

    @Override
    public void render() {
        // Chama o render da tela ativa (Menu ou Jogo)
        super.render();
    }
}
