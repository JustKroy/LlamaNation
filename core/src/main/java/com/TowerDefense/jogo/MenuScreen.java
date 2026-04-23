package com.TowerDefense.jogo;

import com.badlogic.gdx.Application;
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


        HUDimg = new Texture[5];
            HUDimg[0] = new Texture("MenuScreen_Background.png");
            HUDimg[1] = new Texture("Popup_Background.png");
            HUDimg[2] = new Texture("Painel.jpg"); //Painel popup
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
        viewport.getCamera().update();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        Vector2 mundoMouse = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        // Só inverte se NÃO for mobile (ou se você realmente quiser esse desafio no touch)
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            if (ConfigManager.invertMouseX) {
                mundoMouse.x = 1920 - mundoMouse.x;
            }
            if (ConfigManager.invertMouseY) {
                mundoMouse.y = 1080 - mundoMouse.y;
            }
        }

        posMouse = mundoMouse;

        // ==========================================
        // LÓGICA DE INPUT E CLIQUES
        // ==========================================

        // LÓGICA DO ESC (Abre/Fecha o Popup)
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (popup.isAberto()) {
                popup.toggle();
            }
        }

        // 1. Lida com os cliques DENTRO do popup primeiro (se ele estiver aberto)
        if (popup.isAberto()) {
            popup.handleInput(posMouse.x, posMouse.y);
        }

        for(Botao btn : HUDbtn) {
            btn.atualizarCursor(posMouse);
        }

        // 2. Lógica de clique do menu principal
        if (clicou) {
            // Só permite clicar no botão de "Configurações" (índice 2) ou nos outros SE o popup estiver fechado
            if (HUDbtn[2].foiClicado(posMouse, clicou)) {
                popup.toggle();
            } else if (!popup.isAberto()) {
                // Impede de clicar em "Play", "Heroes" e "Shop" se o menu de config estiver na frente
                if (HUDbtn[0].foiClicado(posMouse, clicou)) {
                    game.setScreen(new TelaSelecaoDeck(game));
                }
                if (HUDbtn[1].foiClicado(posMouse, clicou)) {
                    game.setScreen(new HeroScreen(game));
                }
                if (HUDbtn[3].foiClicado(posMouse, clicou)) {
                    // Futura tela de Shop
                }
            }
        }

        // ==========================================
        // DESENHOS (A ORDEM AQUI É CRUCIAL!)
        // ==========================================

        // 1. FUNDO (renderiza no FBO)
        // 1. FUNDO (renderiza no FBO)
        fbo.begin();
        ScreenUtils.clear(0, 0, 0, 1);

        batch.begin();

        // Cria coordenadas separadas só para mover a imagem de fundo
        float parallaxX = posMouse.x;
        float parallaxY = posMouse.y;

        // Aplica a inversão APENAS na imagem de fundo, assim provamos que a config funciona!
        if (ConfigManager.invertMouseX) {
            parallaxX = 1920 - parallaxX;
        }
        if (ConfigManager.invertMouseY) {
            parallaxY = 1080 - parallaxY;
        }

        // Calcula o offset do fundo usando as variáveis invertidas (ou não)
        float offsetX = (parallaxX - 960) * 0.01f;
        float offsetY = (parallaxY - 540) * 0.01f;

        batch.draw(HUDimg[0], offsetX, offsetY, 1920, 1080);

        batch.end();
        fbo.end();

        fboTexture = fbo.getColorBufferTexture();
        fboTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);


        // 2. DESENHA O FUNDO NA TELA (com ou sem blur)
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


        // 3. SOMBRAS DOS BOTÕES PRINCIPAIS
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

        // 4. BOTÕES PRINCIPAIS
        batch.begin();
        for (Botao btn : HUDbtn) {
            btn.Exibir(batch, posMouse);
        }
        batch.end();

        // ==========================================
        // 5. O POPUP (POR ÚLTIMO, POR CIMA DE TUDO!)
        // ==========================================
        if (popup.isAberto()) {
            // Sincroniza o ShapeRenderer INTERNO do PopupConfig com a câmera
            popup.setProjectionMatrix(viewport.getCamera().combined);

            // Camada 1: Fundo do popup e botões base
            popup.renderShapes(posMouse.x, posMouse.y);

            // Camada 2: Textos base
            batch.begin();
            // Atenção: PopupConfig recebe Y e X nessa ordem no seu código
            popup.render(batch, posMouse.y, posMouse.x);
            batch.end();

            // Camada 3: Fundo do Dropdown
            shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            popup.renderDropdownFundo(shapeRenderer, posMouse.x, posMouse.y);
            shapeRenderer.end();

            // Camada 4: Textos do Dropdown
            batch.begin();
            popup.renderDropdownTextos(batch, posMouse.x, posMouse.y);
            batch.end();
        }

        // ==========================================
        // 6. DESENHA O CURSOR DO JOGO (POR CIMA DE TUDO)
        // ==========================================

        if(Gdx.app.getType() != Application.ApplicationType.Android && Gdx.app.getType() != Application.ApplicationType.iOS) {
            if (!popup.isAberto()) {
                for (Botao btn : HUDbtn) {
                    btn.atualizarCursor(posMouse);
                }
            } else {
                if (popup.estaSobreElementoInterativo(posMouse)) {
                    CursorManager.setHover();
                }
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
        for(Botao btn : HUDbtn) {
            btn.dispose();
        }

        for (Texture t : HUDimg) {
            t.dispose();
        }
    }
}
