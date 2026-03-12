package com.TowerDefense.jogo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

    private Array<Inimigo> listaInimigos = new Array<>();
    private Array<Torre> listaTorres = new Array<>();
    private Array<Projetil> listaProjeteis = new Array<>();

    private Texture texturaCaramujo, imgLhama, imgLhamaNinja, imgCuspe, imgKunai;
    private Animation<TextureRegion> animCaramujo;

    private int vidas = 3, dinheiro = 1000, waveAtual = 1;
    private int inimigosParaSpawnar;
    private float timerSpawn = 0, intervaloSpawn = 1.8f, timerDescanso = 0;
    private boolean waveEmAndamento = false, arrastando = false;
    private Texture texturaArrastando = null;
    private Vector2 posMouse = new Vector2();
    private Torre torreSelecionada = null;
    private boolean posicaoValida = false;

    public GameScreen(Main game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.shape = new ShapeRenderer();
        this.camera = new OrthographicCamera();
        this.viewport = new StretchViewport(1920, 1080, camera);
        this.mapa = new Mapa();
        this.hud = new Hud();

        this.imgLhama = new Texture("Llama.png");
        this.imgLhamaNinja = new Texture("LlamaNinja.png");
        this.imgCuspe = new Texture("guspe.png");
        this.imgKunai = new Texture("kunai.png");

        carregarInimigo();
        inimigosParaSpawnar = 5;
    }

    private void carregarInimigo() {
        texturaCaramujo = new Texture("caramujo.png");
        TextureRegion[][] tmp = TextureRegion.split(texturaCaramujo, texturaCaramujo.getWidth() / 5, texturaCaramujo.getHeight());
        animCaramujo = new Animation<>(0.08f, tmp[0]);
        animCaramujo.setPlayMode(Animation.PlayMode.LOOP);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        camera.update();
        posMouse = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        // --- LÓGICA DE WAVES ---
        if (waveEmAndamento) {
            timerSpawn += delta;
            if (inimigosParaSpawnar > 0 && timerSpawn >= intervaloSpawn) {
                listaInimigos.add(new Inimigo(mapa.caminho.get(0).x, mapa.caminho.get(0).y, animCaramujo));
                inimigosParaSpawnar--;
                timerSpawn = 0;
            }
            if (inimigosParaSpawnar <= 0 && listaInimigos.size == 0 && waveAtual < 10) {
                waveEmAndamento = false; waveAtual++;
                inimigosParaSpawnar = waveAtual * 5;
            }
        } else {
            timerDescanso += delta;
            if (timerDescanso >= 5.0f) waveEmAndamento = true;
        }

        // --- LÓGICA DE CLIQUE E ARRASTO ---
        if (Gdx.input.justTouched()) {

            hud.verificarClique(posMouse.x, posMouse.y);

            torreSelecionada = null;
            for (Torre t : listaTorres) {
                if (posMouse.dst(t.posicao.x + 40, t.posicao.y + 40) < 60) torreSelecionada = t;
            }
            if (posMouse.x >= 1600 && posMouse.x <= 1720) {
                if (posMouse.y >= 850 && posMouse.y <= 970 && dinheiro >= 50) { arrastando = true; texturaArrastando = imgLhama; }
                else if (posMouse.y >= 650 && posMouse.y <= 770 && dinheiro >= 150) { arrastando = true; texturaArrastando = imgLhamaNinja; }
            }
        }

        if (arrastando) {
            posicaoValida = true;
            Rectangle hitboxTemp = new Rectangle(posMouse.x - 40, posMouse.y - 40, 80, 80);

            if (posMouse.x > 1350) posicaoValida = false;

            for (Rectangle rectCaminho : mapa.hitboxesCaminho) {
                if (rectCaminho.overlaps(hitboxTemp)) posicaoValida = false;
            }

            for (Torre t : listaTorres) {
                if (t.hitbox.overlaps(hitboxTemp)) posicaoValida = false;
            }
        }

        if (arrastando && !Gdx.input.isTouched()) {
            if (posicaoValida) {
                Texture tiroCerto = (texturaArrastando == imgLhamaNinja) ? imgKunai : imgCuspe;
                listaTorres.add(new Torre(posMouse.x - 40, posMouse.y - 40, texturaArrastando, tiroCerto));
                dinheiro -= (texturaArrastando == imgLhamaNinja) ? 150 : 50;
            }
            arrastando = false;
            texturaArrastando = null;
        }

        // --- ATUALIZAR INIMIGOS E TORRES ---
        for (int i = 0; i < listaInimigos.size; i++) {
            if (listaInimigos.get(i).atualizar(delta, mapa.caminho)) { vidas--; listaInimigos.removeIndex(i); }
        }
        for (Torre t : listaTorres) {
            t.atualizar(delta, listaInimigos, listaProjeteis);
        }

        // --- ATUALIZAR PROJÉTEIS E DANO ---
        for (int i = listaProjeteis.size - 1; i >= 0; i--) {
            Projetil p = listaProjeteis.get(i);
            p.atualizar(delta);

            if (p.ativo) {
                for (int j = listaInimigos.size - 1; j >= 0; j--) {
                    Inimigo in = listaInimigos.get(j);

                    // Ajustado para o novo centro do projétil (tamanho 30 / 2 = 15f)
                    float centroTiroX = p.posicao.x + p.metade;
                    float centroTiroY = p.posicao.y + p.metade;
                    float centroCaracolX = in.posicao.x + 25;
                    float centroCaracolY = in.posicao.y + 25;

                    float distanciaColisao = Vector2.dst(centroTiroX, centroTiroY, centroCaracolX, centroCaracolY);

                    if (distanciaColisao <= 20) {
                        in.vida -= p.dano;
                        p.ativo = false;

                        if (in.vida <= 0) {
                            listaInimigos.removeIndex(j);
                            dinheiro += 20;
                        }
                        break;
                    }
                }
            }

            if (!p.ativo) listaProjeteis.removeIndex(i);
        }

        // --- DESENHO NA TELA ---
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        mapa.desenharMapa(batch);
        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shape.setProjectionMatrix(camera.combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);

        mapa.desenharFundo(shape);

        if (torreSelecionada != null) {
            shape.setColor(1, 1, 1, 0.15f);
            shape.circle(torreSelecionada.posicao.x + 40, torreSelecionada.posicao.y + 40, torreSelecionada.raio);
        }
        if (arrastando) {
            shape.setColor(posicaoValida ? 1 : 1, posicaoValida ? 1 : 0, 0, 0.2f);
            float raioP = (texturaArrastando == imgLhamaNinja) ? 250f : 200f;
            shape.circle(posMouse.x, posMouse.y, raioP);
        }
        shape.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();
        for (Inimigo in : listaInimigos) in.desenhar(batch);
        for (Torre t : listaTorres) t.desenhar(batch);
        for (Projetil p : listaProjeteis) p.desenhar(batch);

        // Calcula as larguras automáticas para a loja (altura fixa de 120)
        float propLhama = (float) imgLhama.getWidth() / imgLhama.getHeight();
        batch.draw(imgLhama, 1600, 850, 120 * propLhama, 120);

        float propNinja = (float) imgLhamaNinja.getWidth() / imgLhamaNinja.getHeight();
        batch.draw(imgLhamaNinja, 1600, 650, 120 * propNinja, 120);

        if (arrastando && texturaArrastando != null) {
            batch.setColor(1, posicaoValida ? 1 : 0.3f, posicaoValida ? 1 : 0.3f, 0.7f);

            // Calcula largura automática para a Lhama que está no mouse (altura fixa de 80)
            float propArrastando = (float) texturaArrastando.getWidth() / texturaArrastando.getHeight();
            batch.draw(texturaArrastando, posMouse.x - 40, posMouse.y - 40, 80 * propArrastando, 80);

            batch.setColor(1, 1, 1, 1);
        }

        hud.desenhar(batch, vidas, dinheiro, waveAtual);
        batch.end();

        // =======================================================
        // DEPURADOR LIGADO/DESLIGADO PELO HUD
        // =======================================================
        if (hud.mostrarHitbox) {
            shape.begin(ShapeRenderer.ShapeType.Line);

            shape.setColor(1, 0, 0, 1);
            for (Rectangle r : mapa.hitboxesCaminho) shape.rect(r.x, r.y, r.width, r.height);

            shape.setColor(0, 0, 1, 1);
            for (Torre t : listaTorres) shape.rect(t.hitbox.x, t.hitbox.y, t.hitbox.width, t.hitbox.height);

            for (Inimigo in : listaInimigos) {
                shape.setColor(0, 1, 0, 1);
                shape.rect(in.posicao.x, in.posicao.y, 50, 50);
                shape.setColor(1, 0, 1, 1);
                shape.circle(in.posicao.x + 25, in.posicao.y + 25, 20);
            }

            for (Projetil p : listaProjeteis) {
                shape.setColor(1, 1, 0, 1);
                shape.rect(p.hitbox.x, p.hitbox.y, p.hitbox.width, p.hitbox.height);
                shape.setColor(1, 1, 1, 1);
                // Ajustado para o novo centro de colisão (15f) também no modo visual
                shape.circle(p.posicao.x + p.metade, p.posicao.y + p.metade, 5);
            }

            shape.end();
        }

        if (vidas <= 0) game.setScreen(new MenuScreen(game));
    }

    @Override public void resize(int w, int h) { viewport.update(w, h, true); }

    @Override public void dispose() {
        batch.dispose(); shape.dispose(); mapa.dispose(); hud.dispose();
        texturaCaramujo.dispose(); imgLhama.dispose(); imgLhamaNinja.dispose();
        imgCuspe.dispose(); imgKunai.dispose();
    }
}
