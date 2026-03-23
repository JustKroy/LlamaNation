package com.TowerDefense.jogo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class MenuScreen extends ScreenAdapter {
    private final Main game;
    private final SpriteBatch batch;
    private final StretchViewport viewport;
    private Vector2 posMouse = new Vector2();
    private Botao[] HUDbtn;
    private Texture[] HUDimg;
    private boolean popupAberto;


    public MenuScreen(Main game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.viewport = new StretchViewport(1920, 1080);
        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        popupAberto = false;

        HUDbtn = new Botao[6];
            HUDbtn[0] = new Botao(
                new Texture("Botao_play.png"),
                new Texture("BotaoPlayHover.png"),
                760, 720, 400, 100
            );
            HUDbtn[1] = new Botao(
                new Texture("Botao_heroes.png"),
                new Texture("BotaoHeroHover.png"),
                760, 520, 400, 100
            );
            HUDbtn[2] = new Botao(
                new Texture("settings.png"),
                new Texture("settings.png"),
                1800, 950, 100, 100
            );
            HUDbtn[3] = new Botao(
                new Texture("Botao_play.png"),
                new Texture("Botao_play.png"),
                760, 320, 400, 100
            );
            HUDbtn[4] = new Botao(
                new Texture("Botao_fecharPopup.png"),
                new Texture("Botao_fecharPopup.png"),
                860, 220, 200, 80
            );
            HUDbtn[5] = new Botao(
                new Texture("Botao_som.png"),
                new Texture("Botao_som.png"),
                760, 400, 400, 100
            );

        HUDimg = new Texture[3];
            HUDimg[0] = new Texture("backgroundHero.jpg");
            HUDimg[1] = new Texture("fundoPopup.png");
            HUDimg[2] = new Texture("painelPopup");
    }

    @Override
    public void render(float delta) {
        // Limpa a tela com uma cor de fundo (preto no caso)
        ScreenUtils.clear(0, 0, 0, 1);

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        // ATUALIZA O MOUSE EM TODO FRAME
        posMouse = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        // Cursor padrão
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);


        for(Botao btn : HUDbtn) {
            btn.atualizarCursor(posMouse);
        }

        batch.begin();

            batch.draw(HUDimg[0], 0, 0, 1920, 1080);

            for (Botao btn : HUDbtn) {
                btn.Exibir(batch, posMouse);
            }

        batch.end();

        // Lógica de clique
        if (Gdx.input.justTouched()) {
            posMouse = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

            if (HUDbtn[0].foiClicado(posMouse)) {
                game.setScreen(new GameScreen(game)); // Inicia o jogo
            }

            if (HUDbtn[1].foiClicado(posMouse)) {
                // Aqui você pode colocar para ir para uma tela de heróis no futuro
                game.setScreen(new HeroScreen(game)); // Inicia o jogo
            }
            if (HUDbtn[2].foiClicado(posMouse)) {
//                game.setScreen(new SettingsScreen(game));
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
        for(Botao btn : HUDbtn) {
            btn.dispose();
        }

        for (Texture t : HUDimg) {
            t.dispose();
        }
    }
}
