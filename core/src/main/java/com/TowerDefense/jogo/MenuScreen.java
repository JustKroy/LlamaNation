package com.TowerDefense.jogo;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
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
    private final ShapeRenderer shapeRenderer;
    private FrameBuffer fbo;
    private Texture fboTexture;
    private ShaderProgram blurShader;

    //------------- CONSTRUTOR ------------ --
    public MenuScreen(Main game) {
        this.game = game;
        this.batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        this.viewport = new StretchViewport(1920, 1080);
        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        popup = new PopupConfig();
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, 1920, 1080, false);

        //--------- SHADER ---------

        ShaderProgram.pedantic = false;

        blurShader = new ShaderProgram(
            Gdx.files.internal("blur.vert"),
            Gdx.files.internal("blur.frag")
        );

        if (!blurShader.isCompiled()) {
            System.out.println(blurShader.getLog());
        }

        HUDbtn = new Botao[4];
            HUDbtn[0] = new Botao(
                new Texture("Play_Button.png"),
                new Texture("Play_ButtonHover.png"),
                760, 720, 400, 100
            );
            HUDbtn[1] = new Botao(
                new Texture("Heroes_Button.png"),
                new Texture("Heroes_ButtonHover.png"),
                760, 520, 400, 100
            );
            HUDbtn[2] = new Botao(
                new Texture("Settings_Button.png"),
                new Texture("Settings_Button.png"),
                1800, 950, 100, 100
            );
            HUDbtn[3] = new Botao(
                new Texture("Shop_Button.png"),
                new Texture("Shop_ButtonHover.png"),
                760, 320, 400, 100
            );


        HUDimg = new Texture[3];
            HUDimg[0] = new Texture("MenuScreen_Background.png");
            HUDimg[1] = new Texture("Popup_Background.png");
            HUDimg[2] = new Texture("painel.jpg"); //Painel popup
        }

    @Override
    public void render(float delta) {
        // Limpa a tela com uma cor de fundo (preto no caso)
        ScreenUtils.clear(0, 0, 0, 1);
        boolean clicou = Gdx.input.justTouched();

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        // ATUALIZA O MOUSE EM TODO FRAME
        posMouse = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        // Cursor padrão
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

        if (popup.isAberto()) {

            if (clicou && HUDbtn[2].foiClicado(posMouse, clicou)) {
                popup.toggle();
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                popup.toggle();
            }

            popup.handleInput(mouseX, mouseY);

            //DESENHA FUNDO (batch)
            batch.begin();
            batch.draw(HUDimg[0], 0, 0, 1920, 1080);
            batch.end();

            //DESENHA FORMAS (popup - ShapeRenderer)
            popup.renderShapes();

            //DESENHA TEXTO (batch)
            batch.begin();
            popup.render(batch);
            batch.end();
            popup.renderDropdownTop(batch);

            return;
        }


        for(Botao btn : HUDbtn) {
            btn.atualizarCursor(posMouse);
        }

        // Lógica de clique
        if (clicou) {

            if (HUDbtn[0].foiClicado(posMouse, clicou)) {
                game.setScreen(new GameScreen(game)); // Inicia o jogo
            }

            if (HUDbtn[1].foiClicado(posMouse, clicou)) {
                // Aqui você pode colocar para ir para uma tela de heróis no futuro
                game.setScreen(new HeroScreen(game)); // Inicia o jogo
            }
            if (HUDbtn[2].foiClicado(posMouse, clicou)) {
                popup.toggle();
            }
        }

        // DESENHA NA TELA

        // 1. FUNDO (renderiza no FBO)
        fbo.begin();
        ScreenUtils.clear(0, 0, 0, 1);

        batch.begin();

        float offsetX = (posMouse.x - 960) * 0.01f;
        float offsetY = (posMouse.y - 540) * 0.01f;

        batch.draw(HUDimg[0], offsetX, offsetY, 1920, 1080);

        batch.end();
        fbo.end();

        fboTexture = fbo.getColorBufferTexture();
        fboTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);


        // DESENHA NA TELA (com ou sem blur)
        if (popup.isAberto()) {
            batch.setShader(blurShader);
        } else {
            batch.setShader(null);
        }

        batch.begin();

        // CORREÇÃO DO FBO INVERTIDO
        batch.draw(fboTexture, 0, 1080, 1920, -1080);

        batch.end();

        // SEMPRE resetar shader
        batch.setShader(null);


        //2. SOMBRAS
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (Botao btn : HUDbtn) {
            Rectangle r = btn.getArea();

            // sombra leve
            shapeRenderer.setColor(0, 0, 0, 0.08f);
            shapeRenderer.rect(r.x + 6, r.y - 6, r.width, r.height);

            // sombra principal
            shapeRenderer.setColor(0, 0, 0, 0.2f);
            shapeRenderer.rect(r.x + 3, r.y - 3, r.width, r.height);
        }

        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        // 3. BOTÕES
        batch.begin();

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
