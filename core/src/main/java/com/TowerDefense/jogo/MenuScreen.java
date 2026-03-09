package com.TowerDefense.jogo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class MenuScreen extends ScreenAdapter {
    private final Main game;
    private SpriteBatch batch;
    private StretchViewport viewport;

    private Texture imgPlay;
    private Texture imgHeroes;
    private Texture imgFundo; // Caso você tenha um fundo para o menu

    private Rectangle btnPlay;
    private Rectangle btnHeroes;
    private Vector2 posMouse = new Vector2();

    public MenuScreen(Main game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.viewport = new StretchViewport(1920, 1080);

        // Carregando as imagens
        imgPlay = new Texture("play.png");
        imgHeroes = new Texture("heroes.png");
        // imgFundo = new Texture("fundo_menu.png"); // Ative se tiver um fundo

        // Definindo o tamanho dos botões
        float larguraBtn = 400;
        float alturaBtn = 150;

        // Posicionando os botões (Centralizados)
        // Play no centro: (1920/2 - largura/2)
        btnPlay = new Rectangle(1920 / 2f - larguraBtn / 2f, 600, larguraBtn, alturaBtn);

        // Heroes um pouco abaixo do Play
        btnHeroes = new Rectangle(1920 / 2f - larguraBtn / 2f, 400, larguraBtn, alturaBtn);
    }

    @Override
    public void render(float delta) {
        // Limpa a tela com uma cor de fundo (preto no caso)
        ScreenUtils.clear(0, 0, 0, 1);

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        // Lógica de clique
        if (Gdx.input.justTouched()) {
            posMouse = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

            if (btnPlay.contains(posMouse.x, posMouse.y)) {
                game.setScreen(new GameScreen(game)); // Inicia o jogo
            }

            if (btnHeroes.contains(posMouse.x, posMouse.y)) {
                // Aqui você pode colocar para ir para uma tela de heróis no futuro
                System.out.println("Clicou em Heróis!");
            }
        }

        batch.begin();

        // Se tiver fundo, desenha aqui
        // batch.draw(imgFundo, 0, 0, 1920, 1080);

        // Desenha o botão de Play
        batch.draw(imgPlay, btnPlay.x, btnPlay.y, btnPlay.width, btnPlay.height);

        // Desenha o botão de Heroes
        batch.draw(imgHeroes, btnHeroes.x, btnHeroes.y, btnHeroes.width, btnHeroes.height);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        imgPlay.dispose();
        imgHeroes.dispose();
        if (imgFundo != null) imgFundo.dispose();
    }
}
