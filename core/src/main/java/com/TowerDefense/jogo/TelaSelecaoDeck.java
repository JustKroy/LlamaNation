package com.TowerDefense.jogo;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.HashMap;

public class TelaSelecaoDeck implements Screen {

    private Game game;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;

    private Texture imgLhama, imgLhamaNinja, imgLhamaMage, imgLlamaCyborg, imgLlamaAngel, imgLlamaBurguesa, imgLlamaChef, imgLlamaNeves;
    private HashMap<TipoLlama, TextureRegion> icones;

    public Array<TipoLlama> deckEscolhido;
    private final StretchViewport viewport;

    private HashMap<TipoLlama, Rectangle> botoesCatalogo;
    private Array<Rectangle> slotsDeck;
    private Rectangle btnJogar;
    private Vector2 posMouse = new Vector2();

    public TelaSelecaoDeck(Game game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.shape = new ShapeRenderer();
        this.viewport = new StretchViewport(1920, 1080);

        font = new BitmapFont(Gdx.files.internal("Fontes.fnt"));

        deckEscolhido = new Array<>();
        icones = new HashMap<>();
        botoesCatalogo = new HashMap<>();
        slotsDeck = new Array<>();

        carregarTexturas();
        configurarBotoes();
    }

    private void carregarTexturas() {
        imgLhama = new Texture("LlamaSS.png");
        imgLhamaNinja = new Texture("NinjaLlama.png");
        imgLhamaMage = new Texture("MageLlamaSS.png");
        imgLlamaCyborg = new Texture("CyborgLlamaSS.png");
        imgLlamaAngel = new Texture("AngelLlamaSS.png");
        imgLlamaBurguesa = new Texture("BourgeoisLlamaSS.png");
        imgLlamaChef = new Texture("ChefLlamaSS.png");
        imgLlamaNeves = new Texture("YetiLlamaSS.png");

        icones.put(TipoLlama.NORMAL, new TextureRegion(imgLhama, 0, 0, imgLhama.getWidth() / 9, imgLhama.getHeight()));
        icones.put(TipoLlama.NINJA, new TextureRegion(imgLhamaNinja));
        icones.put(TipoLlama.MAGE, new TextureRegion(imgLhamaMage, 0, 0, imgLhamaMage.getWidth() / 11, imgLhamaMage.getHeight()));
        icones.put(TipoLlama.CYBORG, new TextureRegion(imgLlamaCyborg, 0, 0, imgLlamaCyborg.getWidth() / 18, imgLlamaCyborg.getHeight()));
        icones.put(TipoLlama.ANGEL, new TextureRegion(imgLlamaAngel, 0, 0, imgLlamaAngel.getWidth() / 5, imgLlamaAngel.getHeight()));
        icones.put(TipoLlama.BURGUESA, new TextureRegion(imgLlamaBurguesa, 0, 0, imgLlamaBurguesa.getWidth() / 17, imgLlamaBurguesa.getHeight()));
        icones.put(TipoLlama.CHEF, new TextureRegion(imgLlamaChef, 0, 0, imgLlamaChef.getWidth() / 5, imgLlamaChef.getHeight()));
        icones.put(TipoLlama.NEVES, new TextureRegion(imgLlamaNeves, 0, 0, imgLlamaNeves.getWidth() / 12, imgLlamaNeves.getHeight()));
    }

    private void configurarBotoes() {
        int i = 0;
        for (TipoLlama tipo : TipoLlama.values()) {
            float x = 200 + (i % 3) * 200;
            float y = 700 - (i / 3) * 200;
            botoesCatalogo.put(tipo, new Rectangle(x, y, 120, 120));
            i++;
        }
        for (int j = 0; j < 6; j++) {
            slotsDeck.add(new Rectangle(250 + (j * 160), 150, 120, 120));
        }
        btnJogar = new Rectangle(1400, 100, 300, 100);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.2f, 1);
        CursorManager.setDefault();

        viewport.apply();

        // --- 1. PROCESSAMENTO DE MOUSE (CONFIG MANAGER) ---
        Vector2 mouseCru = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        posMouse.set(
            ConfigManager.processarMouseX(mouseCru.x),
            ConfigManager.processarMouseY(mouseCru.y)
        );

        // --- 2. LÓGICA ---
        tratarInputs();

        // --- 3. DESENHO DE SHAPES ---
        shape.setProjectionMatrix(viewport.getCamera().combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.DARK_GRAY);
        for (Rectangle slot : slotsDeck) {
            shape.rect(slot.x, slot.y, slot.width, slot.height);
        }
        shape.setColor(deckEscolhido.size == 6 ? Color.GREEN : Color.FIREBRICK);
        shape.rect(btnJogar.x, btnJogar.y, btnJogar.width, btnJogar.height);
        shape.end();

        // --- 4. DESENHO DE TEXTURAS ---
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        font.getData().setScale(0.8f);
        font.draw(batch, "MONTE SEU DECK (ESCOLHA 6)", 400, 950);
        font.getData().setScale(0.4f);
        font.draw(batch, "JOGAR!", btnJogar.x + 80, btnJogar.y + 65);

        for (TipoLlama tipo : TipoLlama.values()) {
            Rectangle rect = botoesCatalogo.get(tipo);
            if (deckEscolhido.contains(tipo, true)) batch.setColor(1, 1, 1, 0.3f);
            else batch.setColor(Color.WHITE);
            desenharIcone(icones.get(tipo), rect);
            font.draw(batch, tipo.nome, rect.x - 10, rect.y - 10);
        }

        batch.setColor(Color.WHITE);
        for (int j = 0; j < deckEscolhido.size; j++) {
            desenharIcone(icones.get(deckEscolhido.get(j)), slotsDeck.get(j));
        }
        batch.end();

        // --- 5. CURSOR FINAL ---
        renderizarCursor();
    }

    private void renderizarCursor() {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            boolean hover = false;

            // Checa catálogo
            for (Rectangle rect : botoesCatalogo.values()) {
                if (rect.contains(posMouse)) { hover = true; break; }
            }
            // Checa slots ocupados
            if (!hover) {
                for (int j = 0; j < deckEscolhido.size; j++) {
                    if (slotsDeck.get(j).contains(posMouse)) { hover = true; break; }
                }
            }
            // Checa botão jogar
            if (!hover && btnJogar.contains(posMouse) && deckEscolhido.size == 6) hover = true;

            if (hover) CursorManager.setHover();
            CursorManager.aplicarCursorInvisivel();

            batch.begin();
            CursorManager.desenhar(batch, posMouse);
            batch.end();
        }
    }

    private void tratarInputs() {
        if (Gdx.input.justTouched()) {
            // Catálogo
            for (TipoLlama tipo : TipoLlama.values()) {
                if (botoesCatalogo.get(tipo).contains(posMouse)) {
                    if (!deckEscolhido.contains(tipo, true) && deckEscolhido.size < 6) {
                        deckEscolhido.add(tipo);
                    }
                    return;
                }
            }
            // Remover do deck
            for (int j = 0; j < deckEscolhido.size; j++) {
                if (slotsDeck.get(j).contains(posMouse)) {
                    deckEscolhido.removeIndex(j);
                    return;
                }
            }
            // Botão Jogar
            if (btnJogar.contains(posMouse) && deckEscolhido.size == 6) {
                game.setScreen(new GameScreen((Main) game, deckEscolhido));
            }
        }
    }

    private void desenharIcone(TextureRegion icon, Rectangle rect) {
        if (icon == null) return;
        float proporcao = (float) icon.getRegionWidth() / icon.getRegionHeight();
        float larguraIdeal = rect.height * proporcao;
        float xCentralizado = rect.x + (rect.width - larguraIdeal) / 2f;
        batch.draw(icon, xCentralizado, rect.y, larguraIdeal, rect.height);
    }

    @Override public void show() { CursorManager.aplicarCursorInvisivel(); }
    @Override public void resize(int width, int height) { viewport.update(width, height, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        shape.dispose();
        font.dispose();
        imgLhama.dispose();
        imgLhamaNinja.dispose();
        imgLhamaMage.dispose();
        imgLlamaCyborg.dispose();
        imgLlamaAngel.dispose();
        imgLlamaBurguesa.dispose();
        imgLlamaChef.dispose();
        imgLlamaNeves.dispose();
    }
}
