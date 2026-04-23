package com.TowerDefense.jogo;

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

    private float scrollY = 0;
    private float maxScroll = 0;

    public TelaSelecaoDeck(Game game) {
        this.game = game;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();

        font = new BitmapFont(Gdx.files.internal("fontes.fnt"));
        font.getData().setScale(0.4f);

        deckEscolhido = new Array<>();
        icones = new HashMap<>();
        botoesCatalogo = new HashMap<>();
        slotsDeck = new Array<>();
        viewport = new StretchViewport(1920, 1080);

        carregarTexturas();
        configurarBotoes();

        // Limite do scroll baseado em 5 colunas (ocupa a tela inteira)
        int linhas = (TipoLlama.values().length - 1) / 5 + 1;
        maxScroll = Math.max(0, (linhas * 250) - 400);

        Gdx.input.setInputProcessor(new com.badlogic.gdx.InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                scrollY -= amountY * 60;
                if (scrollY < 0) scrollY = 0;
                if (scrollY > maxScroll) scrollY = maxScroll;
                return true;
            }
        });
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

        icones.put(TipoLlama.NORMAL,    new TextureRegion(imgLhama, 0, 0, imgLhama.getWidth() / 9, imgLhama.getHeight()));
        icones.put(TipoLlama.NINJA,     new TextureRegion(imgLhamaNinja));
        icones.put(TipoLlama.MAGE,      new TextureRegion(imgLhamaMage, 0, 0, imgLhamaMage.getWidth() / 11, imgLhamaMage.getHeight()));
        icones.put(TipoLlama.CYBORG,    new TextureRegion(imgLlamaCyborg, 0, 0, imgLlamaCyborg.getWidth() / 18, imgLlamaCyborg.getHeight()));
        icones.put(TipoLlama.ANGEL,     new TextureRegion(imgLlamaAngel, 0, 0, imgLlamaAngel.getWidth() / 5, imgLlamaAngel.getHeight()));
        icones.put(TipoLlama.BURGUESA,  new TextureRegion(imgLlamaBurguesa, 0, 0, imgLlamaBurguesa.getWidth() / 17, imgLlamaBurguesa.getHeight()));
        icones.put(TipoLlama.CHEF,      new TextureRegion(imgLlamaChef, 0, 0, imgLlamaChef.getWidth() / 5, imgLlamaChef.getHeight()));
        icones.put(TipoLlama.NEVES,     new TextureRegion(imgLlamaNeves, 0, 0, imgLlamaNeves.getWidth() / 12, imgLlamaNeves.getHeight()));
    }

    private void configurarBotoes() {
        int i = 0;
        // Layout de Tela Inteira: 5 colunas bem espaçadas
        for (TipoLlama tipo : TipoLlama.values()) {
            float x = 260 + (i % 5) * 280;
            float y = 750 - (i / 5) * 280;
            botoesCatalogo.put(tipo, new Rectangle(x, y, 140, 140));
            i++;
        }

        // Slots centralizados na parte de baixo
        for (int j = 0; j < 6; j++) {
            slotsDeck.add(new Rectangle(450 + (j * 150), 60, 120, 120));
        }

        btnJogar = new Rectangle(1450, 70, 300, 100);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1);

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        shape.setProjectionMatrix(viewport.getCamera().combined);
        posMouse = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        // --- ATUALIZA A POSIÇÃO COM SCROLL ---
        int i = 0;
        for (TipoLlama tipo : TipoLlama.values()) {
            float x = 260 + (i % 5) * 280;
            float y = 750 - (i / 5) * 280 + scrollY;
            botoesCatalogo.get(tipo).setPosition(x, y);
            i++;
        }

        tratarInputs();

        boolean sobreAlgo = false;
        // Ignora hover no catálogo se o mouse estiver na área do deck (abaixo do Y 250)
        if (posMouse.y > 250) {
            for (Rectangle r : botoesCatalogo.values()) if (r.contains(posMouse)) sobreAlgo = true;
        }
        for (Rectangle r : slotsDeck) if (r.contains(posMouse)) sobreAlgo = true;
        if (btnJogar.contains(posMouse)) sobreAlgo = true;

        if (sobreAlgo) CursorManager.setHover();
        else CursorManager.setDefault();


        // 1. DESENHA O CATÁLOGO PRIMEIRO (Ele vai ficar por baixo do painel do deck)
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.15f, 0.15f, 0.2f, 1);
        for (Rectangle rect : botoesCatalogo.values()) {
            shape.rect(rect.x, rect.y, rect.width, rect.height);
        }
        shape.end();

        batch.begin();
        for (TipoLlama tipo : TipoLlama.values()) {
            Rectangle rect = botoesCatalogo.get(tipo);
            TextureRegion icon = icones.get(tipo);

            if (deckEscolhido.contains(tipo, true)) batch.setColor(1, 1, 1, 0.3f);
            else batch.setColor(Color.WHITE);

            desenharIcone(icon, rect);

            font.getData().setScale(0.4f);
            font.draw(batch, tipo.nome, rect.x, rect.y - 10);
        }
        batch.end();


        // 2. DESENHA O PAINEL INFERIOR (Oculta o scroll que desce demais)
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.05f, 0.05f, 0.08f, 1); // Fundo bem escuro
        shape.rect(0, 0, 1920, 250); // Painel

        // Linha de divisão superior do painel
        shape.setColor(Color.DARK_GRAY);
        shape.rect(0, 250, 1920, 5);

        // Slots do deck
        shape.setColor(0.2f, 0.2f, 0.2f, 1);
        for (Rectangle slot : slotsDeck) {
            shape.rect(slot.x, slot.y, slot.width, slot.height);
        }

        // Botão Jogar
        shape.setColor(deckEscolhido.size == 6 ? Color.GREEN : Color.FIREBRICK);
        shape.rect(btnJogar.x, btnJogar.y, btnJogar.width, btnJogar.height);

        // Scrollbar Visual na direita
        if (maxScroll > 0) {
            shape.setColor(Color.DARK_GRAY);
            shape.rect(1850, 300, 10, 600);
            shape.setColor(Color.LIGHT_GRAY);
            float handleY = 850 - ((scrollY / maxScroll) * 500);
            shape.rect(1845, handleY, 20, 50);
        }
        shape.end();


        // 3. DESENHA OS ITENS DO PAINEL E TEXTOS (Por cima de tudo)
        batch.begin();
        batch.setColor(Color.WHITE);
        font.getData().setScale(0.8f);
        font.draw(batch, "MONTE SEU DECK", 80, 180);

        font.getData().setScale(0.4f);
        font.draw(batch, "ESCOLHA 6 LHAMAS", 80, 110);
        font.draw(batch, "JOGAR!", btnJogar.x + 80, btnJogar.y + 65);

        for (int j = 0; j < deckEscolhido.size; j++) {
            desenharIcone(icones.get(deckEscolhido.get(j)), slotsDeck.get(j));
        }
        batch.end();

        // --- CURSOR CUSTOMIZADO ---
        CursorManager.aplicarCursorInvisivel();
        batch.begin();
        CursorManager.desenhar(batch, posMouse);
        batch.end();
    }

    private void desenharIcone(TextureRegion icon, Rectangle rect) {
        if (icon == null) return;
        float proporcao = (float) icon.getRegionWidth() / icon.getRegionHeight();
        float larguraIdeal = rect.height * proporcao;
        float xCentralizado = rect.x + (rect.width - larguraIdeal) / 2f;
        batch.draw(icon, xCentralizado, rect.y, larguraIdeal, rect.height);
    }

    private void tratarInputs() {
        if (Gdx.input.justTouched()) {
            // Adicionar ao deck (apenas se clicar acima do painel inferior)
            if (posMouse.y > 250) {
                for (TipoLlama tipo : TipoLlama.values()) {
                    if (botoesCatalogo.get(tipo).contains(posMouse)) {
                        if (!deckEscolhido.contains(tipo, true) && deckEscolhido.size < 6) {
                            deckEscolhido.add(tipo);
                        }
                        return;
                    }
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

    @Override public void show() { CursorManager.aplicarCursorInvisivel(); }
    @Override public void resize(int width, int height) { viewport.update(width, height, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() { Gdx.input.setInputProcessor(null); }

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
