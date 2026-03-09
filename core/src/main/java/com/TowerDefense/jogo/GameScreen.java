package com.TowerDefense.jogo;

import com.badlogic.gdx.Gdx;
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

    // Nossos novos Gerenciadores!
    private GerenciadorDeOndas ondas;
    private ConstrutorDeTorres construtor;

    private Array<Inimigo> listaInimigos = new Array<>();
    private Array<Torre> listaTorres = new Array<>();
    private Array<Projetil> listaProjeteis = new Array<>();

    private int vidas = 100, dinheiro = 1000;
    private Vector2 posMouse = new Vector2();

    public GameScreen(Main game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.shape = new ShapeRenderer();
        this.camera = new OrthographicCamera();
        this.viewport = new StretchViewport(1920, 1080, camera);
        this.mapa = new Mapa();
        this.hud = new Hud();

        this.ondas = new GerenciadorDeOndas();
        this.construtor = new ConstrutorDeTorres();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        posMouse = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        // --- 1. ATUALIZAÇÃO LÓGICA (MANAGERS) ---
        ondas.atualizar(delta, listaInimigos, mapa);
        dinheiro = construtor.atualizar(posMouse, Gdx.input.justTouched(), Gdx.input.isTouched(), dinheiro, listaTorres, mapa, hud);

        // --- 2. ATUALIZAR ENTIDADES (MOVIMENTO, TIROS E DANO) ---
        for (int i = 0; i < listaInimigos.size; i++) {
            if (listaInimigos.get(i).atualizar(delta, mapa.caminho)) {
                vidas--;
                listaInimigos.removeIndex(i);
            }
        }
        for (Torre t : listaTorres) t.atualizar(delta, listaInimigos, listaProjeteis);

        for (int i = listaProjeteis.size - 1; i >= 0; i--) {
            Projetil p = listaProjeteis.get(i);
            p.atualizar(delta);
            Inimigo inimigoAtingido = p.checarColisao(listaInimigos);

            if (inimigoAtingido != null && inimigoAtingido.vida <= 0) {
                listaInimigos.removeValue(inimigoAtingido, true);
                dinheiro += inimigoAtingido.recompensaMoedas;
            }
            if (!p.ativo) listaProjeteis.removeIndex(i);
        }

        // --- 3. DESENHO NA TELA ---
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        mapa.desenharMapa(batch);
        batch.end();

        // Desenhar hitboxes e rastros
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shape.setProjectionMatrix(camera.combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);

        mapa.desenharFundo(shape);
        construtor.desenharHitboxes(shape, posMouse);

        shape.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // Desenhar objetos
        batch.begin();
        for (Inimigo in : listaInimigos) in.desenhar(batch);
        for (Torre t : listaTorres) t.desenhar(batch);
        for (Projetil p : listaProjeteis) p.desenhar(batch);

        construtor.desenharLojaEArrasto(batch, posMouse);
        hud.desenhar(batch, vidas, dinheiro, ondas.waveAtual);
        batch.end();

        // --- 4. DEPURADOR E GAME OVER ---
        if (hud.mostrarHitbox) renderizarDebugHitbox();
        if (vidas <= 0) game.setScreen(new MenuScreen(game));
    }

    // Separei o depurador em um método para a render não ficar gigante
    private void renderizarDebugHitbox() {
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(1, 0, 0, 1);
        for (Rectangle r : mapa.hitboxesCaminho) shape.rect(r.x, r.y, r.width, r.height);
        shape.setColor(0, 0, 1, 1);
        for (Torre t : listaTorres) shape.rect(t.hitbox.x, t.hitbox.y, t.hitbox.width, t.hitbox.height);
        for (Inimigo in : listaInimigos) {
            shape.setColor(0, 1, 0, 1);
            shape.rect(in.posicao.x, in.posicao.y, 50, 50);
        }
        for (Projetil p : listaProjeteis) {
            shape.setColor(1, 1, 0, 1);
            shape.rect(p.hitbox.x, p.hitbox.y, p.hitbox.width, p.hitbox.height);
        }
        shape.end();
    }

    @Override public void resize(int w, int h) { viewport.update(w, h, true); }

    @Override public void dispose() {
        batch.dispose(); shape.dispose(); mapa.dispose(); hud.dispose();
        ondas.dispose(); construtor.dispose();
    }
}
