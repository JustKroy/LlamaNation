package com.TowerDefense.jogo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class GameScreen extends ScreenAdapter {

    private final Main game;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private OrthographicCamera camera;
    private StretchViewport viewport;

    private Mapa mapa;
    private Hud hud;
    private GerenciadorDeOndas ondas;
    private ConstrutorDeTorres construtor;
    private Array<TipoLlama> deckAtual;

    private Array<Inimigo> listaInimigos = new Array<>();
    private Array<Torre> listaTorres = new Array<>();
    private Array<Projetil> listaProjeteis = new Array<>();

    private int vidas = 5, dinheiro = 200;
    private Vector2 posMouse = new Vector2();

    private Texture gameOverTexture;
    private Texture btnRestart, btnRestartHover;
    private Texture btnExit, btnExitHover;
    private boolean jogoAcabou = false;

    public GameScreen(Main game, Array<TipoLlama> deckEscolhido) {
        this.game = game;
        this.deckAtual = deckEscolhido;

        this.batch = new SpriteBatch();
        this.shape = new ShapeRenderer();
        this.camera = new OrthographicCamera();
        this.viewport = new StretchViewport(1920, 1080, camera);

        this.mapa = new Mapa();
        this.hud = new Hud();
        this.ondas = new GerenciadorDeOndas();
        this.construtor = new ConstrutorDeTorres(deckEscolhido);

        this.gameOverTexture = new Texture(Gdx.files.internal("GameOver.png"));
        this.btnRestart = new Texture(Gdx.files.internal("Button_Restart.png"));
        this.btnRestartHover = new Texture(Gdx.files.internal("Button_RestartHover.png"));
        this.btnExit = new Texture(Gdx.files.internal("Button_Exit.png"));
        this.btnExitHover = new Texture(Gdx.files.internal("Button_ExitHover.png"));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        posMouse = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        boolean clicouRestart = false;
        boolean clicouExit = false;

        boolean isMobile = Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.Android ||
                           Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.iOS;

        // 🔥 1. LÓGICA DO JOGO 🔥
        if (!jogoAcabou) {

            // PRIMEIRO: Combate, movimento e recompensas
            if (!hud.pausado) {
                if (construtor.jogoAcelerado) delta *= 3f;
                ondas.atualizar(delta, listaInimigos, mapa);

                // Inimigos: Movimento e Mortes
                for (int i = listaInimigos.size - 1; i >= 0; i--) {
                    Inimigo inimigo = listaInimigos.get(i);

                    // Checa morte e concede recompensa
                    if (inimigo.vida <= 0) {
                        dinheiro += inimigo.recompensaMoedas;
                        listaInimigos.removeIndex(i);
                        continue;
                    }

                    // Checa se chegou na base (perde vida)
                    if (inimigo.atualizar(delta, mapa.caminho)) {
                        vidas--;
                        listaInimigos.removeIndex(i);
                    }
                }

                // Torres: Tiros e Geração de dinheiro (ex: Burguesa)
                for (Torre t : listaTorres) {
                    dinheiro += t.atualizar(delta, listaInimigos, listaProjeteis);
                }

                // Projéteis: Colisões
                for (int i = listaProjeteis.size - 1; i >= 0; i--) {
                    Projetil p = listaProjeteis.get(i);
                    p.atualizar(delta);
                    p.checarColisao(listaInimigos);
                    if (!p.ativo) listaProjeteis.removeIndex(i);
                }
            }

            // SEGUNDO: Loja e UI (Recebe o dinheiro com todas as somas/subtrações do frame já aplicadas)
            dinheiro = construtor.atualizar(posMouse, Gdx.input.justTouched(), Gdx.input.isTouched(), dinheiro, listaTorres, mapa, hud);

            if (vidas <= 0) jogoAcabou = true;
        }

        // 2. DESENHO
        batch.setProjectionMatrix(camera.combined);
        shape.setProjectionMatrix(camera.combined);

        // Fundo e Mapa
        batch.begin();
        mapa.desenharMapa(batch);
        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        mapa.desenharFundo(shape);
        if (!jogoAcabou) construtor.desenharHitboxes(shape, posMouse, hud);
        shape.end();

        // Entidades e HUD
        batch.begin();
        for (Inimigo in : listaInimigos) in.desenhar(batch);
        for (Torre t : listaTorres) t.desenhar(batch);
        for (Projetil p : listaProjeteis) p.desenhar(batch);
        construtor.desenharLojaEArrasto(batch, posMouse, hud);
        hud.desenhar(batch, vidas, dinheiro, ondas.waveAtual, posMouse.x, posMouse.y);
        batch.end();

        // 3. TELA DE GAME OVER
        if (jogoAcabou) {
            // 1. Habilita a transparência (blending) no OpenGL
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            shape.begin(ShapeRenderer.ShapeType.Filled);
            // O último valor (0.7f) controla a transparência.
            // 0.0f = 100% invisível / 1.0f = 100% sólido. Pode ajustar a gosto!
            shape.setColor(0, 0, 0, 0.7f);
            shape.rect(0, 0, 1920, 1080);
            shape.end();

            // 2. Desabilita o blending para não zoar a renderização de outras coisas
            Gdx.gl.glDisable(GL20.GL_BLEND);

            batch.begin();
            float escalaGO = 7.5f;
            float wGO = gameOverTexture.getWidth() * escalaGO;
            float hGO = gameOverTexture.getHeight() * escalaGO;
            float xGO = (1920 - wGO) / 2f;
            float yGO = (1080 - hGO) / 2f + 150;
            batch.draw(gameOverTexture, xGO, yGO, wGO, hGO);

            float escalaBtn = 3.0f;
            float wBtn = btnRestart.getWidth() * escalaBtn;
            float hBtn = btnRestart.getHeight() * escalaBtn;
            float espacamento = 40;
            float xRestart = (1920 / 2f) - wBtn - (espacamento / 2f);
            float xExit = (1920 / 2f) + (espacamento / 2f);
            float yBotoes = yGO - hBtn - 50;

            Rectangle rectRestart = new Rectangle(xRestart, yBotoes, wBtn, hBtn);
            Rectangle rectExit = new Rectangle(xExit, yBotoes, wBtn, hBtn);

            // Lógica Botão Restart
            if (!isMobile && rectRestart.contains(posMouse)) {
                batch.draw(btnRestartHover, xRestart, yBotoes, wBtn, hBtn);
            } else {
                batch.draw(btnRestart, xRestart, yBotoes, wBtn, hBtn);
            }
            if (Gdx.input.justTouched() && rectRestart.contains(posMouse)) clicouRestart = true;

            // Lógica Botão Exit
            if (!isMobile && rectExit.contains(posMouse)) {
                batch.draw(btnExitHover, xExit, yBotoes, wBtn, hBtn);
            } else {
                batch.draw(btnExit, xExit, yBotoes, wBtn, hBtn);
            }
            if (Gdx.input.justTouched() && rectExit.contains(posMouse)) clicouExit = true;
            batch.end();
        }

        // 4. TROCA DE TELA SEGURA E CURSOR
        if (clicouRestart) {
            game.setScreen(new GameScreen(game, deckAtual));
            this.dispose();
            return;
        }

        if (clicouExit || hud.voltarAoMenu) {
            game.setScreen(new MenuScreen(game));
            this.dispose();
            return;
        }

        if (hud.mostrarHitbox) renderizarDebugHitbox();

        // Cursor (Somente PC)
        if (!isMobile) {
            CursorManager.setDefault();
            if (hud.estaSobreAlgo(posMouse)) CursorManager.setHover();
            CursorManager.aplicarCursorInvisivel();

            batch.begin();
            CursorManager.desenhar(batch, posMouse);
            batch.end();
        }
    }

    private void renderizarDebugHitbox() {
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(1, 0, 0, 1);
        for (Rectangle r : mapa.hitboxesCaminho) shape.rect(r.x, r.y, r.width, r.height);
        shape.end();
    }

    @Override
    public void resize(int w, int h) { viewport.update(w, h, true); }

    @Override
    public void dispose() {
        batch.dispose();
        shape.dispose();
        mapa.dispose();
        hud.dispose();
        ondas.dispose();
        construtor.dispose();
        gameOverTexture.dispose();
        btnRestart.dispose();
        btnRestartHover.dispose();
        btnExit.dispose();
        btnExitHover.dispose();
    }
}
