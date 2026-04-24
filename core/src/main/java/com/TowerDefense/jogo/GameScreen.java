package com.TowerDefense.jogo;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

    private Array<Inimigo> listaInimigos = new Array<>();
    private Array<Torre> listaTorres = new Array<>();
    private Array<Projetil> listaProjeteis = new Array<>();

    private int vidas = 100, dinheiro = 1000;
    private Vector2 posMouse = new Vector2();

    public GameScreen(Main game, Array<TipoLlama> deckEscolhido) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.shape = new ShapeRenderer();
        this.camera = new OrthographicCamera();
        this.viewport = new StretchViewport(1920, 1080, camera);

        this.mapa = new Mapa();
        this.hud = new Hud();
        this.ondas = new GerenciadorDeOndas();
        this.construtor = new ConstrutorDeTorres(deckEscolhido);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        // --- 1. MOUSE E CÂMERA ---
        camera.update();
        Vector2 mouseCru = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        posMouse.set(
            ConfigManager.processarMouseX(mouseCru.x),
            ConfigManager.processarMouseY(mouseCru.y)
        );

        // --- 2. ATUALIZAÇÃO DE LÓGICA (HUD E CONSTRUTOR) ---
        // O dinheiro é atualizado pelo construtor (compras/vendas)
        dinheiro = construtor.atualizar(posMouse, Gdx.input.justTouched(), Gdx.input.isTouched(), dinheiro, listaTorres, mapa, hud);

        // LÓGICA DO JOGO (Se não estiver pausado)
        if (!hud.pausado) {
            float deltaFinal = construtor.jogoAcelerado ? delta * 3f : delta;

            ondas.atualizar(deltaFinal, listaInimigos, mapa);

            // Inimigos
            for (int i = listaInimigos.size - 1; i >= 0; i--) {
                Inimigo inimigo = listaInimigos.get(i);
                if (inimigo.vida <= 0) {
                    dinheiro += inimigo.recompensaMoedas;
                    listaInimigos.removeIndex(i);
                    continue;
                }
                if (inimigo.atualizar(deltaFinal, mapa.caminho)) {
                    vidas--;
                    listaInimigos.removeIndex(i);
                }
            }

            // Torres
            for (Torre t : listaTorres) {
                dinheiro += t.atualizar(deltaFinal, listaInimigos, listaProjeteis);
            }

            // Projéteis
            for (int i = listaProjeteis.size - 1; i >= 0; i--) {
                Projetil p = listaProjeteis.get(i);
                p.atualizar(deltaFinal);
                p.checarColisao(listaInimigos);
                if (!p.ativo) listaProjeteis.removeIndex(i);
            }
        }

        // --- 3. RENDERIZAÇÃO ---
        batch.setProjectionMatrix(camera.combined);
        shape.setProjectionMatrix(camera.combined);

        // Fundo e Mapa
        batch.begin();
        mapa.desenharMapa(batch);
        batch.end();

        // Camada de Formas (Shapes) - Transparência ligada
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        mapa.desenharFundo(shape);
        construtor.desenharHitboxes(shape, posMouse, hud);
        shape.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // Entidades e UI
        batch.begin();
        for (Inimigo in : listaInimigos) in.desenhar(batch);
        for (Torre t : listaTorres) t.desenhar(batch);
        for (Projetil p : listaProjeteis) p.desenhar(batch);

        construtor.desenharLojaEArrasto(batch, posMouse, hud);
        hud.desenhar(batch, vidas, dinheiro, ondas.waveAtual, posMouse.x, posMouse.y);
        batch.end();

        // --- 4. DEBUG E ESTADOS DE TELA ---
        if (hud.mostrarHitbox) renderizarDebugHitbox();

        verificarEstadosDeTroca();

        // --- 5. CURSOR (Desktop apenas) ---
        renderizarCursor();
    }

    private void verificarEstadosDeTroca() {
        if (hud.voltarAoMenu || vidas <= 0) {
            // Importante: Dispose primeiro para limpar memória antes de trocar
            this.dispose();
            game.setScreen(new MenuScreen((Main) game));
        }
    }

    private void renderizarCursor() {
        if (Gdx.app.getType() != Application.ApplicationType.Android && Gdx.app.getType() != Application.ApplicationType.iOS) {
            CursorManager.setDefault();
            if (hud.estaSobreAlgo(posMouse)) {
                CursorManager.setHover();
            }
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
        shape.setColor(0, 0, 1, 1);
        for (Torre t : listaTorres) shape.rect(t.hitbox.x, t.hitbox.y, t.hitbox.width, t.hitbox.height);
        shape.end();
    }

    @Override
    public void resize(int w, int h) {
        viewport.update(w, h, true);
    }

    @Override
    public void dispose() {
        // Proteção contra múltiplos disposes
        if (batch != null) {
            batch.dispose();
            shape.dispose();
            mapa.dispose();
            hud.dispose();
            ondas.dispose();
            construtor.dispose();
            batch = null;
        }
    }
}
