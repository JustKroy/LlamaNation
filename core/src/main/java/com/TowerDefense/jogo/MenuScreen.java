package com.TowerDefense.jogo;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class MenuScreen extends ScreenAdapter {

    private final Main game;
    private final SpriteBatch batch;
    private final StretchViewport viewport;
    private Vector2 posMouse = new Vector2();

    private Botao[] HUDbtn;
    private Texture[] HUDimg;

    private PopupConfig popup;
    private final ShapeRenderer shapeRenderer;
    private FrameBuffer fbo;
    private Texture fboTexture;
    private ShaderProgram blurShader;

    public MenuScreen(Main game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
        this.viewport = new StretchViewport(1920, 1080);
        this.fbo = game.fbo;
        this.blurShader = game.blurShader;

        popup = new PopupConfig(game);

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        HUDbtn = new Botao[4];
        HUDbtn[0] = new Botao(new Texture("Play_Button.png"), new Texture("Play_ButtonHover.png"), 760, 720, 400, 100);
        HUDbtn[1] = new Botao(new Texture("Heroes_Button.png"), new Texture("Heroes_ButtonHover.png"), 760, 520, 400, 100);
        HUDbtn[2] = new Botao(new Texture("Settings_Button.png"), new Texture("Settings_Button.png"), 1800, 950, 100, 100);
        HUDbtn[3] = new Botao(new Texture("Shop_Button.png"), new Texture("Shop_ButtonHover.png"), 760, 320, 400, 100);

        HUDimg = new Texture[5];
        HUDimg[0] = new Texture("MenuScreen_Background.png");
        HUDimg[1] = new Texture("Popup_Background.png");
        HUDimg[2] = new Texture("Painel.jpg");
        HUDimg[3] = new Texture("Cursor_normal.png");
        HUDimg[4] = new Texture("Cursor_selected.png");
    }

    @Override
    public void show() {
        CursorManager.aplicarCursorInvisivel();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        boolean clicou = Gdx.input.justTouched();
        CursorManager.setDefault();

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        // --- LÓGICA DE MOUSE CENTRALIZADA ---
        // 1. Processamos o Mouse (Aqui ele inverte se a config de mouse mandar)
        Vector2 mouseCru = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        posMouse.set(
            ConfigManager.processarMouseX(mouseCru.x),
            ConfigManager.processarMouseY(mouseCru.y)
        );

        // 2. Cálculo do Parallax focado APENAS no Cursor Visual (posMouse)
        // Se Invert Camera estiver OFF, ele segue o padrão.
        // Se Invert Camera estiver ON, ele inverte a relação com o cursor.
        float direcaoCamX = ConfigManager.getDirecaoCameraX(); // invertCameraX ? -1f : 1f
        float direcaoCamY = ConfigManager.getDirecaoCameraY();

        // Note que aqui NÃO usamos o mouseCru e NÃO usamos a inversão do mouse.
        // O offsetX vai depender apenas de onde o posMouse está.
        float offsetX = (posMouse.x - 960) * 0.01f * direcaoCamX;
        float offsetY = (posMouse.y - 540) * 0.01f * direcaoCamY;

        // LÓGICA DE INPUT
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && popup.isAberto()) {
            popup.toggle();
        }

        if (popup.isAberto()) {
            popup.handleInput(posMouse.x, posMouse.y);
        }

        for(Botao btn : HUDbtn) {
            btn.atualizarCursor(posMouse);
        }

        if (clicou) {
            if (HUDbtn[2].foiClicado(posMouse, clicou)) {
                popup.toggle();
            } else if (!popup.isAberto()) {
                if (HUDbtn[0].foiClicado(posMouse, clicou)) game.setScreen(new TelaSelecaoDeck(game));
                if (HUDbtn[1].foiClicado(posMouse, clicou)) game.setScreen(new HeroScreen(game));
            }
        }

        // --- DESENHO DO FUNDO (PARALLAX) ---
        fbo.begin();
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();

        batch.draw(HUDimg[0], offsetX, offsetY, 1920, 1080);

        batch.end();
        fbo.end();

        fboTexture = fbo.getColorBufferTexture();
        fboTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        if (popup.isAberto()) batch.setShader(blurShader);
        batch.begin();
        batch.draw(fboTexture, 0, 1080, 1920, -1080); // FBO Flip Y
        batch.end();
        batch.setShader(null);

        // --- DESENHO DOS BOTÕES ---
        renderizarSombras();

        batch.begin();
        for (Botao btn : HUDbtn) btn.Exibir(batch, posMouse);
        batch.end();

        // --- DESENHO DO POPUP ---
        if (popup.isAberto()) {
            renderizarPopup();
        }

        // --- CURSOR FINAL ---
        renderizarCursor();
    }

    private void renderizarSombras() {
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.2f);
        for (Botao btn : HUDbtn) {
            Rectangle r = btn.getArea();
            shapeRenderer.rect(r.x + 3, r.y - 3, r.width, r.height);
        }
        shapeRenderer.end();
    }

    private void renderizarPopup() {
        popup.setProjectionMatrix(viewport.getCamera().combined);
        popup.renderShapes(posMouse.x, posMouse.y);

        batch.begin();
        popup.render(batch, posMouse.y, posMouse.x);
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        popup.renderDropdownFundo(shapeRenderer, posMouse.x, posMouse.y);
        shapeRenderer.end();

        batch.begin();
        popup.renderDropdownTextos(batch, posMouse.x, posMouse.y);
        batch.end();
    }

    private void renderizarCursor() {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            if (popup.isAberto() && popup.estaSobreElementoInterativo(posMouse)) {
                CursorManager.setHover();
            }
            CursorManager.aplicarCursorInvisivel();
            batch.begin();
            CursorManager.desenhar(batch, posMouse);
            batch.end();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        popup.atualizarLayout();
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        fbo.dispose();
        blurShader.dispose();
        for(Botao btn : HUDbtn) btn.dispose();
        for(Texture t : HUDimg) t.dispose();
    }
}
