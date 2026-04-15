package com.TowerDefense.jogo;

import com.badlogic.gdx.Game;

// A classe Main herda de "Game" do LibGDX.
// Ela é o motor principal que gerencia as múltiplas telas do seu jogo (Menu, Jogo, Game Over).
public class Main extends Game {

    // --- MÉTODO CREATE (O "Start" do Motor) ---
    // É chamado automaticamente pelo sistema apenas UMA VEZ, assim que você abre o jogo.
    @Override
    public void create() {


        // ESSA LINHA É OBRIGATÓRIA:
        ConfigManager.carregar();

        // Se você quiser que o jogo já abra na resolução salva:
        ConfigManager.aplicarVideo();

        // Define a tela inicial como o Menu

        // Define qual "canal" a TV vai sintonizar primeiro.
        // O 'this' está passando o próprio Main (a TV inteira) para o Menu,
        // assim o Menu consegue trocar de tela depois usando esse mesmo Main.
        this.setScreen(new MenuScreen(this));
    }

    // --- MÉTODO RENDER (A Atualização Contínua) ---
    // Roda dezenas de vezes por segundo (ex: 60 FPS) enquanto o jogo estiver aberto.
    @Override
    public void render() {
        // O super.render() é vital! É ele quem repassa a ordem de desenhar e atualizar
        // para a tela que está aberta no momento (seja o Menu, seja a GameScreen).
        // Se você apagar essa linha, sua tela vai ficar toda preta!
        super.render();
    }
}
