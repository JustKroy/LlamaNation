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
import com.badlogic.gdx.utils.Array;
import java.util.HashMap;

public class TelaSelecaoDeck implements Screen {

    private Game game;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;

    // Texturas genéricas que você já tem
    private Texture imgLhama, imgLhamaNinja, imgLhamaMage, imgLlamaCyborg, imgLlamaAngel, imgLlamaBurguesa, imgLlamaChef;
    private HashMap<TipoLlama, TextureRegion> icones;

    // O Deck final
    public Array<TipoLlama> deckEscolhido;

    // Hitboxes para os cliques
    private HashMap<TipoLlama, Rectangle> botoesCatalogo;
    private Array<Rectangle> slotsDeck;
    private Rectangle btnJogar;

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

        carregarTexturas();
        configurarBotoes();
    }

    private void carregarTexturas() {
        imgLhama = new Texture("llama.png");
        imgLhamaNinja = new Texture("lhamaninja.png");
        imgLhamaMage = new Texture("llamamage.png");
        imgLlamaCyborg = new Texture("LlamaCyborg.png");
        imgLlamaAngel = new Texture("llamaAngel.png");
        imgLlamaBurguesa = new Texture("llamaBurguesa.png");
        imgLlamaChef = new Texture("llamaChef.png");

        // Corta só o primeiro frame de cada lhama para ser o ícone
        icones.put(TipoLlama.NORMAL, new TextureRegion(imgLhama, 0, 0, imgLhama.getWidth() / 9, imgLhama.getHeight()));
        icones.put(TipoLlama.NINJA, new TextureRegion(imgLhamaNinja));
        icones.put(TipoLlama.MAGE, new TextureRegion(imgLhamaMage, 0, 0, imgLhamaMage.getWidth() / 11, imgLhamaMage.getHeight()));
        icones.put(TipoLlama.CYBORG, new TextureRegion(imgLlamaCyborg, 0, 0, imgLlamaCyborg.getWidth() / 18, imgLlamaCyborg.getHeight()));
        icones.put(TipoLlama.ANGEL, new TextureRegion(imgLlamaAngel, 0, 0, imgLlamaAngel.getWidth() / 5, imgLlamaAngel.getHeight()));
        icones.put(TipoLlama.BURGUESA, new TextureRegion(imgLlamaBurguesa, 0, 0, imgLlamaBurguesa.getWidth() / 17, imgLlamaBurguesa.getHeight()));
        icones.put(TipoLlama.CHEF, new TextureRegion(imgLlamaChef, 0, 0, imgLlamaChef.getWidth() / 5, imgLlamaChef.getHeight()));
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
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tratarInputs();

        shape.begin(ShapeRenderer.ShapeType.Filled);

        // Fundo dos slots do Deck (cinza escuro)
        shape.setColor(Color.DARK_GRAY);
        for (Rectangle slot : slotsDeck) {
            shape.rect(slot.x, slot.y, slot.width, slot.height);
        }

        // Botão Jogar (Verde se o deck tiver 6, vermelho se faltar)
        if (deckEscolhido.size == 6) shape.setColor(Color.GREEN);
        else shape.setColor(Color.FIREBRICK);
        shape.rect(btnJogar.x, btnJogar.y, btnJogar.width, btnJogar.height);

        shape.end();

        batch.begin();
        font.getData().setScale(0.8f);
        font.draw(batch, "MONTE SEU DECK (ESCOLHA 6)", 400, 950);

        font.getData().setScale(0.4f);
        font.draw(batch, "JOGAR!", btnJogar.x + 80, btnJogar.y + 65);

        // Desenha Lhamas do Catálogo
        for (TipoLlama tipo : TipoLlama.values()) {
            Rectangle rect = botoesCatalogo.get(tipo);
            TextureRegion icon = icones.get(tipo);

            // Se já foi escolhida, fica meio transparente
            if (deckEscolhido.contains(tipo, true)) batch.setColor(1, 1, 1, 0.3f);
            else batch.setColor(Color.WHITE);

            desenharIcone(icon, rect);
            font.draw(batch, tipo.nome, rect.x - 10, rect.y - 10);
        }

        batch.setColor(Color.WHITE);

        // Desenha Lhamas que já estão no Deck
        for (int j = 0; j < deckEscolhido.size; j++) {
            TipoLlama tipo = deckEscolhido.get(j);
            Rectangle slot = slotsDeck.get(j);
            desenharIcone(icones.get(tipo), slot);
        }

        batch.end();
    }

    private void desenharIcone(TextureRegion icon, Rectangle rect) {
        float proporcao = (float) icon.getRegionWidth() / icon.getRegionHeight();
        float larguraIdeal = rect.height * proporcao;
        float xCentralizado = rect.x + (rect.width - larguraIdeal) / 2f;
        batch.draw(icon, xCentralizado, rect.y, larguraIdeal, rect.height);
    }

    private void tratarInputs() {
        if (Gdx.input.justTouched()) {
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

            // Clicou no catálogo para adicionar ao deck
            for (TipoLlama tipo : TipoLlama.values()) {
                if (botoesCatalogo.get(tipo).contains(mouseX, mouseY)) {
                    if (!deckEscolhido.contains(tipo, true) && deckEscolhido.size < 6) {
                        deckEscolhido.add(tipo);
                    }
                    return; // Sai após o clique
                }
            }

            // Clicou no slot do deck para remover
            for (int j = 0; j < deckEscolhido.size; j++) {
                if (slotsDeck.get(j).contains(mouseX, mouseY)) {
                    deckEscolhido.removeIndex(j);
                    return;
                }
            }

            // Clicou no botão JOGAR
            if (btnJogar.contains(mouseX, mouseY) && deckEscolhido.size == 6) {
                // 🔥 AGORA VAI! Descomentado e com (Main) para passar na verificação do GameScreen
                game.setScreen(new GameScreen((Main) game, deckEscolhido));
            }
        }
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
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
    }
}
