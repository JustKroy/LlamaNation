package com.TowerDefense.jogo;

import com.badlogic.gdx.Input;
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

    //--------- VARIÁVEIS GLOBAIS -----------
    private final Main game; //Referência para o jogo
    private final SpriteBatch batch; //Desenho
    private final StretchViewport viewport; //Viewport - Adaptar o tamanho da tela
    private Vector2 posMouse = new Vector2(); //Posição do mouse

    //----------- ARRAYS -----------
    private Botao[] HUDbtn;
    private Texture[] HUDimg;

    //----------- OUTRAS VARIÁVEIS -----------
    private PopupConfig popup;

    //------------- CONSTRUTOR ------------ --
    public MenuScreen(Main game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.viewport = new StretchViewport(1920, 1080);
        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        popup = new PopupConfig();

        HUDbtn = new Botao[4];
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
                new Texture("shop_0.png"),
                new Texture("shop_hover.png"),
                760, 320, 400, 100
            );


        HUDimg = new Texture[3];
            HUDimg[0] = new Texture("backgroundHero.jpg");
            HUDimg[1] = new Texture("fundoPopup.png");
            HUDimg[2] = new Texture("painel.jpg"); //Painel popup
        }

    @Override
    public void render(float delta) {
        // Limpa a tela com uma cor de fundo (preto no caso)
        ScreenUtils.clear(0, 0, 0, 1);

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        // ATUALIZA O MOUSE EM TODO FRAME
        posMouse = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        // Cursor padrão
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

        if (popup.isAberto()) {

            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                popup.toggle();
            }

            popup.handleInput(mouseX, mouseY);

            //DESENHA FUNDO (batch)
            batch.begin();
            batch.draw(HUDimg[0], 0, 0, 1920, 1080);
            batch.end();

            //DESENHA FORMAS (popup - ShapeRenderer)
            popup.renderShapes(); // 👈 vamos criar isso

            //DESENHA TEXTO (batch)
            batch.begin();
            popup.render(batch);
            batch.end();

            return;
        }


        for(Botao btn : HUDbtn) {
            btn.atualizarCursor(posMouse);
        }

        // Lógica de clique
        if (Gdx.input.justTouched()) {

            if (HUDbtn[0].foiClicado(posMouse)) {
                game.setScreen(new GameScreen(game)); // Inicia o jogo
            }

            if (HUDbtn[1].foiClicado(posMouse)) {
                // Aqui você pode colocar para ir para uma tela de heróis no futuro
                game.setScreen(new HeroScreen(game)); // Inicia o jogo
            }

            if (HUDbtn[2].foiClicado(posMouse)) {
                popup.toggle();
            }
        }

        // DESENHA NA TELA
        batch.begin();

            batch.draw(HUDimg[0], 0, 0, 1920, 1080);

            for (Botao btn : HUDbtn) {
                btn.Exibir(batch, posMouse);
            }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        popup.atualizarLayout();
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
