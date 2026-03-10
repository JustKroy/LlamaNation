package com.TowerDefense.jogo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.math.Rectangle;

public class HeroScreen extends ScreenAdapter {

    private final Main game;
    private SpriteBatch batch;
    private StretchViewport viewport;

    private Texture imgLabel, imgHeroe, imgPainel, imgBackGround, llama, llamaNinja, llamaMage, llamaRobo, llama1, llama2, skin, ataque;

    private Botao btnVoltar, btnSelect;

    // Vetor para armazenar a posição do mouse convertida para o sistema do jogo
    private Vector2 posMouse = new Vector2();

    public HeroScreen(Main game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.viewport = new StretchViewport(1920, 1080);

        //--------- Imagens -----------
        imgBackGround = new Texture("backgroundHero.jpg");
        imgPainel = new Texture("painel.jpg");
        imgLabel = new Texture("teste.png");
        imgHeroe = new Texture("verde.png");
        llama = new Texture("teste.png");
        llamaNinja = new Texture("teste.png");
        llamaMage = new Texture("teste.png");
        llamaRobo = new Texture("teste.png");
        llama1 = new Texture("teste.png");
        llama2 = new Texture("teste.png");
        skin = new Texture("painel.jpg");
        ataque = new Texture("painel.jpg");

        //-------- Botões ----------
        btnVoltar = new Botao(
            new Texture("botaovoltar.png"),
            //hover
            new Texture("botaovoltar.png"),
            20,975,180,80
        );

        btnSelect = new Botao(
            new Texture("verde.png"),
            new Texture("verde.png"),
            1050, 430, 350, 100
        );

    }

    @Override
    public void render(float delta) {

        //Limpa a tela
        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        // ATUALIZA O MOUSE EM TODO FRAME
        posMouse = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        // Cursor padrão
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

        // Se estiver sobre algum botão, vira mão
        btnVoltar.atualizarCursor(posMouse);
        btnSelect.atualizarCursor(posMouse);
        //----------- IMAGENS -------------
        batch.begin();
            batch.draw(imgBackGround, 0, 0, 1920, 1080);
            batch.draw(imgPainel, 0, 0, 500, 950);
            batch.draw(imgLabel, 700, 950, 700, 120);
            batch.draw(imgHeroe, 1050, 600, 350, 300);
            batch.draw(llama, 20, 700, 200, 200);
            batch.draw(llamaNinja, 20, 450, 200, 200);
            batch.draw(llamaMage, 270, 700, 200, 200);
            batch.draw(llamaRobo, 270, 450, 200, 200);
            batch.draw(llama1, 20, 200, 200, 200);
            batch.draw(llama2, 270, 200, 200, 200);
            batch.draw(skin, 850, 430, 100, 100);
            batch.draw(ataque, 700, 430, 100, 100);

        //------------ BOTOES ------------
            btnVoltar.Exibir(batch, posMouse);
            btnSelect.Exibir(batch, posMouse);
        batch.end();

        //---------------- RECTANGLE --------------
        // Detecta se houve um clique na tela ou mouse
        if (Gdx.input.justTouched()) {

            // Converte a posição do mouse da tela para o sistema de coordenadas do jogo
            posMouse = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

            // Verifica se o clique aconteceu dentro da área do botão PLAY
            if (btnVoltar.foiClicado(posMouse)) {
                // Troca a tela atual para a tela do jogo
                game.setScreen(new MenuScreen(game));
            } else {
            }

            if(btnSelect.foiClicado(posMouse)) {
            } else {
            }
        }
    }

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        //------ IMG ------
        imgBackGround.dispose();
        imgPainel.dispose();
        imgLabel.dispose();
        imgHeroe.dispose();
        llama.dispose();
        llamaMage.dispose();
        llamaNinja.dispose();
        llamaRobo.dispose();
        llama1.dispose();
        llama2.dispose();
        skin.dispose();
        ataque.dispose();

        //----- BTN ------
        btnSelect.dispose();
        btnVoltar.dispose();
    }
}
