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
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.HashMap;

public class TelaSelecaoDeck implements Screen {

    private Game game;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;

    // Texturas genéricas
    private Texture imgLhama, imgLhamaNinja, imgLhamaMage, imgLlamaCyborg, imgLlamaAngel, imgLlamaBurguesa, imgLlamaChef;

    // 🔥 NOVO: Textura da Lhama das Neves
    private Texture imgLlamaNeves;

    private HashMap<TipoLlama, TextureRegion> icones;

    // O Deck final
    public Array<TipoLlama> deckEscolhido;
    private final StretchViewport viewport; //Viewport - Adaptar o tamanho da tela

    // Hitboxes para os cliques
    private HashMap<TipoLlama, Rectangle> botoesCatalogo;
    private Array<Rectangle> slotsDeck;
    private Rectangle btnJogar;
    private Vector2 posMouse = new Vector2();

    public TelaSelecaoDeck(Game game) {
        this.game = game;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();

        font = new BitmapFont(Gdx.files.internal("Fontes.fnt"));
        font.getData().setScale(0.4f);

        deckEscolhido = new Array<>();
        icones = new HashMap<>();
        botoesCatalogo = new HashMap<>();
        slotsDeck = new Array<>();
        viewport = new StretchViewport(1920, 1080);

        viewport.getCamera().position.set(1920 / 2f, 1080 / 2f, 0);
        viewport.getCamera().update();

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

        // Corta só o primeiro frame de cada lhama para ser o ícone
        icones.put(TipoLlama.NORMAL, new TextureRegion(imgLhama, 0, 0, imgLhama.getWidth() / 9, imgLhama.getHeight()));
        icones.put(TipoLlama.NINJA, new TextureRegion(imgLhamaNinja));
        icones.put(TipoLlama.MAGE, new TextureRegion(imgLhamaMage, 0, 0, imgLhamaMage.getWidth() / 11, imgLhamaMage.getHeight()));
        icones.put(TipoLlama.CYBORG, new TextureRegion(imgLlamaCyborg, 0, 0, imgLlamaCyborg.getWidth() / 18, imgLlamaCyborg.getHeight()));
        icones.put(TipoLlama.ANGEL, new TextureRegion(imgLlamaAngel, 0, 0, imgLlamaAngel.getWidth() / 5, imgLlamaAngel.getHeight()));
        icones.put(TipoLlama.BURGUESA, new TextureRegion(imgLlamaBurguesa, 0, 0, imgLlamaBurguesa.getWidth() / 17, imgLlamaBurguesa.getHeight()));
        icones.put(TipoLlama.CHEF, new TextureRegion(imgLlamaChef, 0, 0, imgLlamaChef.getWidth() / 5, imgLlamaChef.getHeight()));

        // 🔥 NOVO: Cortando o primeiro frame (dos 12) da Neves e guardando no dicionário de ícones
        icones.put(TipoLlama.NEVES, new TextureRegion(imgLlamaNeves, 0, 0, imgLlamaNeves.getWidth() / 12, imgLlamaNeves.getHeight()));
    }

    private void configurarBotoes() {
        // Posição das Lhamas para escolher (Catálogo)
        int i = 0;
        for (TipoLlama tipo : TipoLlama.values()) {
            float x = 200 + (i % 3) * 200;
            float y = 700 - (i / 3) * 200;
            botoesCatalogo.put(tipo, new Rectangle(x, y, 120, 120));
            i++;
        }

        // Posição dos 6 espaços em branco na parte de baixo (O seu Deck)
        for (int j = 0; j < 6; j++) {
            slotsDeck.add(new Rectangle(250 + (j * 160), 150, 120, 120));
        }

        // Botão Jogar
        btnJogar = new Rectangle(1400, 100, 300, 100);
    }

    @Override
    public void render(float delta) {
        CursorManager.setDefault();
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        viewport.getCamera().update();

        batch.setProjectionMatrix(viewport.getCamera().combined);
        shape.setProjectionMatrix(viewport.getCamera().combined); // 🔥 ESSENCIAL

        // Mouse CORRETO (mesmo sistema do resto do jogo)
        posMouse = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        if (ConfigManager.invertMouseX) {
            posMouse.x = 1920 - posMouse.x;
        }
        if (ConfigManager.invertMouseY) {
            posMouse.y = 1080 - posMouse.y;
        }

        tratarInputs(); // agora usa posMouse corretamente

        // -------- SHAPES --------
        shape.begin(ShapeRenderer.ShapeType.Filled);

        shape.setColor(Color.DARK_GRAY);
        for (Rectangle slot : slotsDeck) {
            shape.rect(slot.x, slot.y, slot.width, slot.height);
        }

        // Botão Jogar
        shape.setColor(deckEscolhido.size == 6 ? Color.GREEN : Color.FIREBRICK);
        shape.rect(btnJogar.x, btnJogar.y, btnJogar.width, btnJogar.height);

        shape.end();

        // -------- TEXTOS E IMAGENS --------
        batch.begin();

        font.getData().setScale(0.8f);
        font.draw(batch, "MONTE SEU DECK (ESCOLHA 6)", 400, 950);

        font.getData().setScale(0.4f);
        font.draw(batch, "JOGAR!", btnJogar.x + 80, btnJogar.y + 65);

        // Catálogo
        for (TipoLlama tipo : TipoLlama.values()) {
            Rectangle rect = botoesCatalogo.get(tipo);
            TextureRegion icon = icones.get(tipo);

            if (deckEscolhido.contains(tipo, true))
                batch.setColor(1, 1, 1, 0.3f);
            else
                batch.setColor(Color.WHITE);

            desenharIcone(icon, rect);
            font.draw(batch, tipo.nome, rect.x - 10, rect.y - 10);
        }

        batch.setColor(Color.WHITE);

        // Deck
        for (int j = 0; j < deckEscolhido.size; j++) {
            TipoLlama tipo = deckEscolhido.get(j);
            Rectangle slot = slotsDeck.get(j);
            desenharIcone(icones.get(tipo), slot);
        }

        batch.end();

        boolean hover = false;

// Verifica se está sobre algum botão do catálogo
        for (Rectangle rect : botoesCatalogo.values()) {
            if (rect.contains(posMouse.x, posMouse.y)) {
                hover = true;
                break;
            }
        }

// Verifica se está sobre os slots do deck (apenas os ocupados ou todos, conforme sua preferência)
        if (!hover) {
            for (int j = 0; j < deckEscolhido.size; j++) {
                if (slotsDeck.get(j).contains(posMouse.x, posMouse.y)) {
                    hover = true;
                    break;
                }
            }
        }

// Verifica botão jogar
        if (!hover && btnJogar.contains(posMouse.x, posMouse.y) && deckEscolhido.size == 6) {
            hover = true;
        }

// Só processa cursor visual se não for mobile
        if (Gdx.app.getType() != Application.ApplicationType.Android && Gdx.app.getType() != Application.ApplicationType.iOS) {
            if (hover) CursorManager.setHover();
            else CursorManager.setDefault();

            CursorManager.aplicarCursorInvisivel();

            batch.begin();
            CursorManager.desenhar(batch, posMouse);
            batch.end();
        }
    }

    private void desenharIcone(TextureRegion icon, Rectangle rect) {
        // Previne o NullPointerException que estava fechando o jogo!
        if (icon == null) return;

        float proporcao = (float) icon.getRegionWidth() / icon.getRegionHeight();
        float larguraIdeal = rect.height * proporcao;
        float xCentralizado = rect.x + (rect.width - larguraIdeal) / 2f;
        batch.draw(icon, xCentralizado, rect.y, larguraIdeal, rect.height);
    }

    private void tratarInputs() {
        if (Gdx.input.justTouched()) {

            float mouseX = posMouse.x;
            float mouseY = posMouse.y;

            // Catálogo
            for (TipoLlama tipo : TipoLlama.values()) {
                if (botoesCatalogo.get(tipo).contains(mouseX, mouseY)) {
                    if (!deckEscolhido.contains(tipo, true) && deckEscolhido.size < 6) {
                        deckEscolhido.add(tipo);
                    }
                    return;
                }
            }

            // Remover do deck
            for (int j = 0; j < deckEscolhido.size; j++) {
                if (slotsDeck.get(j).contains(mouseX, mouseY)) {
                    deckEscolhido.removeIndex(j);
                    return;
                }
            }

            // Jogar
            if (btnJogar.contains(mouseX, mouseY) && deckEscolhido.size == 6) {
                game.setScreen(new GameScreen((Main) game, deckEscolhido));
            }
        }
    }

    @Override
    public void show() {
        CursorManager.aplicarCursorInvisivel();
    }
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
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
        imgLlamaChef.dispose(); // 🔥 NOVO: Consertei esse pequeno vazamento de memória!
        imgLlamaNeves.dispose(); // 🔥 NOVO: Limpando a textura da Neves no final
    }
}
