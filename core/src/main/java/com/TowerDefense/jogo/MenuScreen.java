package com.TowerDefense.jogo;

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
        this.popup = new PopupConfig();
        this.fbo = new FrameBuffer(Pixmap.Format.RGBA8888, 1920, 1080, false);

        ShaderProgram.pedantic = false;
        blurShader = new ShaderProgram(
            Gdx.files.internal("blur.vert"),
            Gdx.files.internal("blur.frag")
        );

        if (!blurShader.isCompiled()) {
            System.out.println(blurShader.getLog());
        }

        HUDbtn = new Botao[4];
        HUDbtn[0] = new Botao(new Texture("Play_Button.png"), new Texture("Play_ButtonHover.png"), 760, 720, 400, 100);
        HUDbtn[1] = new Botao(new Texture("Heroes_Button.png"), new Texture("Heroes_ButtonHover.png"), 760, 520, 400, 100);
        HUDbtn[2] = new Botao(new Texture("Settings_Button.png"), new Texture("Settings_Button.png"), 1800, 950, 100, 100);
        HUDbtn[3] = new Botao(new Texture("Shop_Button.png"), new Texture("Shop_ButtonHover.png"), 760, 320, 400, 100);

        HUDimg = new Texture[1];
        HUDimg[0] = new Texture("MenuScreen_Background.png");
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

        // --- LÓGICA DO MOUSE E CONFIGS ---
        Vector2 mundoMouse = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        if (ConfigManager.invertMouseX) mundoMouse.x = 1920 - mundoMouse.x;
        if (ConfigManager.invertMouseY) mundoMouse.y = 1080 - mundoMouse.y;
        posMouse = mundoMouse;

        // --- LÓGICA DE INPUT ---
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && popup.isAberto()) {
            popup.toggle();
        }

        if (popup.isAberto()) {
            popup.handleInput(posMouse.x, posMouse.y);
            if (popup.estaSobreElementoInterativo(posMouse)) {
                CursorManager.setHover();
            }
        }

        // Atualiza hover dos botões apenas se popup estiver fechado
        if (!popup.isAberto()) {
            for (Botao btn : HUDbtn) btn.atualizarCursor(posMouse);
        }

        // Lógica de Cliques Unificada
        if (clicou) {
            if (HUDbtn[2].foiClicado(posMouse, clicou)) { // Botão Config
                popup.toggle();
            } else if (!popup.isAberto()) {
                if (HUDbtn[0].foiClicado(posMouse, clicou)) {
                    game.setScreen(new TelaSelecaoDeck(game));
                } else if (HUDbtn[1].foiClicado(posMouse, clicou)) {
                    game.setScreen(new HeroScreen(game));
                } else if (HUDbtn[3].foiClicado(posMouse, clicou)) {
                    // game.setScreen(new ShopScreen(game));
                }
            }
        }

        // --- RENDERIZAÇÃO ---

        // 1. Fundo com Parallax no FBO
        fbo.begin();
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();

        float offsetX = 0;
        float offsetY = 0;

        // Só aplica parallax se NÃO for mobile
        if (Gdx.app.getType() != com.badlogic.gdx.Application.ApplicationType.Android &&
            Gdx.app.getType() != com.badlogic.gdx.Application.ApplicationType.iOS) {
            float pX = ConfigManager.invertMouseX ? 1920 - posMouse.x : posMouse.x;
            float pY = ConfigManager.invertMouseY ? 1080 - posMouse.y : posMouse.y;
            offsetX = (pX - 960) * 0.01f;
            offsetY = (pY - 540) * 0.01f;
        }

        batch.draw(HUDimg[0], offsetX, offsetY, 1920, 1080);
        batch.end();
        fbo.end();

        // 2. Fundo na tela (com Blur se popup aberto)
        fboTexture = fbo.getColorBufferTexture();
        fboTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        if (popup.isAberto()) batch.setShader(blurShader);
        batch.begin();
        batch.draw(fboTexture, 0, 1080, 1920, -1080); // Inversão de Y necessária para FBO
        batch.end();
        batch.setShader(null);

        // 3. Sombras e Botões Principais
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Botao btn : HUDbtn) {
            Rectangle r = btn.getArea();
            shapeRenderer.setColor(0, 0, 0, 0.2f);
            shapeRenderer.rect(r.x + 3, r.y - 3, r.width, r.height);
        }
        shapeRenderer.end();

        batch.begin();
        for (Botao btn : HUDbtn) btn.Exibir(batch, posMouse);
        batch.end();

        // 4. Popup (Camada superior)
        if (popup.isAberto()) {
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

        // 5. Cursor customizado
        CursorManager.aplicarCursorInvisivel();
        batch.begin();
        CursorManager.desenhar(batch, posMouse);
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
        shapeRenderer.dispose();
        fbo.dispose();
        blurShader.dispose();
        for (Botao btn : HUDbtn) btn.dispose();
        for (Texture t : HUDimg) t.dispose();
    }
}
